package com.runt9.heroDynasty.dungeon.input

import com.badlogic.gdx.InputAdapter
import com.runt9.heroDynasty.dungeon.Dungeon
import com.runt9.heroDynasty.lib.AppConst.gridHeight
import com.runt9.heroDynasty.lib.AppConst.gridWidth
import com.runt9.heroDynasty.util.getCoord
import squidpony.squidmath.Coord

class DungeonMouseHandler(private val dungeon: Dungeon, private val mouseInfo: DungeonMouseInfo) : InputAdapter() {
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        dungeon.moveToMouse()
        mouseMoved(screenX, screenY)
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return mouseMoved(screenX, screenY)
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        if (dungeon.hasPendingMoves()) return false

        val player = dungeon.player.getCoord()
        val newScreenX = screenX + player.x - (gridWidth / 2)
        val newScreenY = screenY + player.y - (gridHeight / 2)

        if (newScreenX < 0 || newScreenY < 0 || newScreenX >= gridWidth || newScreenY >= gridHeight || mouseInfo.cursor.x == newScreenX && mouseInfo.cursor.y == newScreenY) {
            return false
        }

        mouseInfo.cursor = Coord.get(newScreenX, newScreenY)
        mouseInfo.toCursor = mouseInfo.playerToCursor.findPathPreScanned(mouseInfo.cursor)
        if (!mouseInfo.toCursor.isEmpty()) {
            mouseInfo.toCursor = mouseInfo.toCursor.subList(1, mouseInfo.toCursor.size)
        }

        return false
    }
}
