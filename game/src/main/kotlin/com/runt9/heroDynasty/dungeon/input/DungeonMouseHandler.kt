package com.runt9.heroDynasty.dungeon.input

import com.badlogic.gdx.InputAdapter
import com.runt9.heroDynasty.dungeon.Dungeon
import com.runt9.heroDynasty.util.AppConst.bigHeight
import com.runt9.heroDynasty.util.AppConst.bigWidth
import com.runt9.heroDynasty.util.AppConst.gridHeight
import com.runt9.heroDynasty.util.AppConst.gridWidth
import squidpony.squidai.DijkstraMap
import squidpony.squidmath.Coord
import squidpony.squidmath.GreasedRegion

class DungeonMouseHandler(private val dungeon: Dungeon) : InputAdapter() {
    private val playerToCursor: DijkstraMap = DijkstraMap(dungeon.rawDungeon, DijkstraMap.Measurement.EUCLIDEAN)

    init {
        dungeon.resetCursorPath = { coord: Coord, blockage: GreasedRegion, fullReset: Boolean ->
            if (fullReset) {
                playerToCursor.clearGoals()
                playerToCursor.resetMap()
            }

            playerToCursor.setGoal(coord)
            playerToCursor.partialScan(13, blockage)
        }
    }

    // TODO: Mouse handling is still a bit wonky at times (namely pathfinding), should work on this
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (dungeon.hasPendingMoves()) return false

        val player = dungeon.playerSprite
        val newScreenX = (screenX + player.gridX - (gridWidth / 2)).toInt()
        val newScreenY = ((gridHeight - 1 - screenY) + player.gridY - (gridHeight / 2)).toInt()

        if (newScreenX < 0 || newScreenY < 0 || newScreenX >= bigWidth || newScreenY >= bigHeight) {
            return false
        }

        var path: MutableList<Coord> = playerToCursor.findPathPreScanned(Coord.get(newScreenX, newScreenY))
        if (!path.isEmpty() && path.size > 1) { // If we click on ourselves, we want to skip the turn
            path = path.subList(1, path.size)
        }

        if (dungeon.isVisible(newScreenX, newScreenY)) {
            dungeon.moveTo(path)
        }

        return true
    }
}
