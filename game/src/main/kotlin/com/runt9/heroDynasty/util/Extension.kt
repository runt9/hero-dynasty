package com.runt9.heroDynasty.util

import squidpony.squidgrid.gui.gdx.AnimatedEntity
import squidpony.squidmath.Coord

fun AnimatedEntity.getCoord(): Coord = Coord.get(this.gridX, this.gridY)
