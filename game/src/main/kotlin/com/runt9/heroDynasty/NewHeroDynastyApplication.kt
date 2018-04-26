package com.runt9.heroDynasty

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.GL20
import com.runt9.heroDynasty.dungeon.Dungeon
import com.runt9.heroDynasty.dungeon.input.DungeonKeyHandler
import com.runt9.heroDynasty.dungeon.input.DungeonMouseHandler
import com.runt9.heroDynasty.dungeon.input.DungeonMouseInfo
import com.runt9.heroDynasty.lib.AppConst.bgColor
import com.runt9.heroDynasty.lib.AppConst.cellHeight
import com.runt9.heroDynasty.lib.AppConst.cellWidth
import com.runt9.heroDynasty.lib.AppConst.gridHeight
import com.runt9.heroDynasty.lib.AppConst.gridWidth
import squidpony.squidgrid.gui.gdx.SquidInput
import squidpony.squidgrid.gui.gdx.SquidMouse

class NewHeroDynastyApplication : ApplicationAdapter() {
    private lateinit var input: SquidInput
    private lateinit var dungeon: Dungeon

    override fun create() {
        dungeon = Dungeon()
        val mouseInfo = DungeonMouseInfo(dungeon.rawDungeon)
        val mouseHandler = DungeonMouseHandler(dungeon, mouseInfo)
        dungeon.mouseInfo = mouseInfo
        dungeon.rebuildFov()

        input = SquidInput(DungeonKeyHandler(dungeon), SquidMouse(cellWidth, cellHeight, gridWidth, gridHeight, 0, 0, mouseHandler))
        Gdx.input.inputProcessor = InputMultiplexer(dungeon.stage, input)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)

        val currentZoomX = width.toFloat() / gridWidth
        val currentZoomY = height.toFloat() / gridHeight
        val mouseOffsetX = (gridWidth.toInt() and 1) * (currentZoomX * -0.5f).toInt()
        val mouseOffsetY = (gridHeight.toInt() and 1) * (currentZoomY * -0.5f).toInt()
        input.mouse.reinitialize(currentZoomX, currentZoomY, gridWidth, gridHeight, mouseOffsetX, mouseOffsetY)
    }

    override fun render() {
        Gdx.gl.glClearColor(bgColor.r / 255.0f, bgColor.g / 255.0f, bgColor.b / 255.0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        dungeon.handleMoves(input.hasNext()) { input.next() }
        dungeon.draw()
    }
}