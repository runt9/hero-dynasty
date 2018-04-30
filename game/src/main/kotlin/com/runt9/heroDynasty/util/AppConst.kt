package com.runt9.heroDynasty.util

import squidpony.squidgrid.gui.gdx.SColor

object AppConst {
    const val DOUBLE_ADDER = 1e-53

    const val gridWidth = 50f
    const val gridHeight = 26f
    const val dungeonWidth = 85f
    const val dungeonHeight = 85f
    const val cellWidth = 32f
    const val cellHeight = 32f
    const val viewportWidth = gridWidth * cellWidth
    const val viewportHeight = gridHeight * cellHeight
    const val baseFov = 8.0

    val bgColor: SColor = SColor.DARK_SLATE_GRAY
}