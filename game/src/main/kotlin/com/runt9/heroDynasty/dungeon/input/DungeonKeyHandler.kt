package com.runt9.heroDynasty.dungeon.input

import com.runt9.heroDynasty.dungeon.Dungeon
import squidpony.squidgrid.gui.gdx.SquidInput
import squidpony.squidmath.Coord

class DungeonKeyHandler(private val dungeon: Dungeon) : SquidInput.KeyHandler {
    override fun handle(key: Char, alt: Boolean, ctrl: Boolean, shift: Boolean) {
        when (key) {
            // TODO: Handle R for rest
            SquidInput.UP_ARROW -> dungeon.queueNewMove(Coord.get(0, 1))
            SquidInput.DOWN_ARROW -> dungeon.queueNewMove(Coord.get(0, -1))
            SquidInput.LEFT_ARROW -> dungeon.queueNewMove(Coord.get(-1, 0))
            SquidInput.RIGHT_ARROW -> dungeon.queueNewMove(Coord.get(1, 0))
//            SquidInput.ESCAPE -> Gdx.app.exit()
            SquidInput.ESCAPE -> dungeon.pauseMenu()
            'C', 'c' -> dungeon.characterSheet()
        }
    }
}