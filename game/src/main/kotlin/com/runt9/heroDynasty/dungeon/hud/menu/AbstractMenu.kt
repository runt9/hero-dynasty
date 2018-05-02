package com.runt9.heroDynasty.dungeon.hud.menu

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton

abstract class AbstractMenu(title: String, skin: Skin) : Dialog(title, skin) {
    init {
        isModal = false
        isMovable = false
        isResizable = false
        // TODO: This isn't how the key function works
        this.key(Input.Keys.ESCAPE, this.hide())
    }

    protected fun button(text: String, result: Any, init: Cell<TextButton>.() -> Unit) {
        val button = TextButton(text, skin)
        buttonTable.add(button).init()
        setObject(button, result)
    }
}