package com.runt9.heroDynasty.dungeon.input

import com.badlogic.gdx.Gdx
import com.runt9.heroDynasty.dungeon.Dungeon
import squidpony.squidgrid.Direction
import squidpony.squidgrid.gui.gdx.SquidInput

class DungeonKeyHandler(private val dungeon: Dungeon) : SquidInput.KeyHandler {
    override fun handle(key: Char, alt: Boolean, ctrl: Boolean, shift: Boolean) {
        lateinit var direction: Direction

        when (key) {
            SquidInput.UP_ARROW -> direction = Direction.UP
            SquidInput.DOWN_ARROW -> direction = Direction.DOWN
            SquidInput.LEFT_ARROW -> direction = Direction.LEFT
            SquidInput.RIGHT_ARROW -> direction = Direction.RIGHT
            SquidInput.ESCAPE -> Gdx.app.exit()
        }

        dungeon.moveCharacter(direction)
    }
}