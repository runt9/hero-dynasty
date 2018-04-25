package com.runt9.heroDynasty.lib

import squidpony.squidgrid.gui.gdx.SColor

object AppConst {
    const val gridWidth = 50f
    const val gridHeight = 25f
    const val bigWidth = gridWidth * 2
    const val bigHeight = gridHeight * 2
    const val cellWidth = 32f
    const val cellHeight = 32f
    const val viewportWidth = gridWidth * cellWidth
    const val viewportHeight = gridHeight * cellHeight
    const val baseFov = 8.0

    val bgColor: SColor = SColor.DARK_SLATE_GRAY
}