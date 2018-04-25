package com.runt9.heroDynasty.dungeon.input

import com.badlogic.gdx.Gdx
import com.runt9.heroDynasty.dungeon.Dungeon
import squidpony.squidgrid.gui.gdx.SquidInput
import squidpony.squidmath.Coord

class DungeonKeyHandler(private val dungeon: Dungeon) : SquidInput.KeyHandler {
    override fun handle(key: Char, alt: Boolean, ctrl: Boolean, shift: Boolean) {
        lateinit var direction: Coord

        when (key) {
            SquidInput.UP_ARROW -> direction = Coord.get(0, 1)
            SquidInput.DOWN_ARROW -> direction = Coord.get(0, -1)
            SquidInput.LEFT_ARROW -> direction = Coord.get(-1, 0)
            SquidInput.RIGHT_ARROW -> direction = Coord.get(1, 0)
            SquidInput.ESCAPE -> Gdx.app.exit()
        }

        dungeon.queueNewMove(direction)
    }
}