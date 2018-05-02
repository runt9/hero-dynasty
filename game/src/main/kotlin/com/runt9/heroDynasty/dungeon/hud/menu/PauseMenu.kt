package com.runt9.heroDynasty.dungeon.hud.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin

class PauseMenu(skin: Skin) : AbstractMenu("Menu", skin) {
    enum class Result { SAVE, SETTINGS, EXIT }

    override fun result(result: Any) {
        if (result === Unit) return

        when (result as Result) {
            PauseMenu.Result.SAVE -> TODO()
            PauseMenu.Result.SETTINGS -> TODO()
            PauseMenu.Result.EXIT -> Gdx.app.exit()
        }
    }

    init {
        button("Save Game", Result.SAVE) { width(150f).padBottom(10f) }
        buttonTable.row()
        button("Settings", Result.SETTINGS) { width(150f).padBottom(10f) }
        buttonTable.row()
        button("Exit", Result.EXIT) { width(150f).padBottom(10f) }
    }
}