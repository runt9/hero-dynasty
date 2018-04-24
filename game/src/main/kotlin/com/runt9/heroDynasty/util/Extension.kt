package com.runt9.heroDynasty.util

import com.badlogic.gdx.graphics.Color
import squidpony.squidgrid.gui.gdx.AnimatedEntity
import squidpony.squidmath.Coord

fun Array<Array<Color>>.toFloats(): Array<FloatArray> = this.map { it.map { it.toFloatBits() }.toFloatArray() }.toTypedArray()
fun AnimatedEntity.getCoord(): Coord = Coord.get(this.gridX, this.gridY)
