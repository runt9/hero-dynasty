package com.runt9.heroDynasty.dungeon.input

import com.badlogic.gdx.InputAdapter
import com.runt9.heroDynasty.dungeon.Dungeon
import com.runt9.heroDynasty.lib.AppConst.bigHeight
import com.runt9.heroDynasty.lib.AppConst.bigWidth
import com.runt9.heroDynasty.lib.AppConst.gridHeight
import com.runt9.heroDynasty.lib.AppConst.gridWidth
import squidpony.squidmath.Coord

class DungeonMouseHandler(private val dungeon: Dungeon, private val mouseInfo: DungeonMouseInfo) : InputAdapter() {
    // TODO: Mouse handling is still a bit wonky at times, should work on this
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (dungeon.hasPendingMoves()) return false

        val player = dungeon.player
        val newScreenX = (screenX + player.gridX - (gridWidth / 2)).toInt()
        val newScreenY = ((gridHeight - 1 - screenY) + player.gridY - (gridHeight / 2)).toInt()

        if (newScreenX < 0 || newScreenY < 0 || newScreenX >= bigWidth || newScreenY >= bigHeight || mouseInfo.cursor.x == newScreenX && mouseInfo.cursor.y == newScreenY) {
            return false
        }

        mouseInfo.cursor = Coord.get(newScreenX, newScreenY)
        mouseInfo.toCursor = mouseInfo.playerToCursor.findPathPreScanned(mouseInfo.cursor)
        if (!mouseInfo.toCursor.isEmpty()) {
            mouseInfo.toCursor = mouseInfo.toCursor.subList(1, mouseInfo.toCursor.size)
        }

        dungeon.moveToMouse()
        return true
    }
}
