package com.runt9.heroDynasty.dungeon.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion

class AssetMapper {
    private val atlas: TextureAtlas = TextureAtlas(Gdx.files.internal("tileset.pack"))

    fun getCharacter(): AtlasRegion = atlas.findRegion("draconian_black_male")
    fun getMonster(): AtlasRegion = atlas.findRegion("kobold_new")

    fun getAssetMap(): Map<Char, List<AtlasRegion>> {
        val floors = (1 until 2).map { atlas.findRegion("floor$it") }
        val walls = (0 until 11).map { atlas.findRegion("catacombs$it") }
        val shallowWater = (0 until 1).map { atlas.findRegion("shallow_water$it") }
        val deepWater = (0 until 1).map { atlas.findRegion("deep_water$it") }
        val closedDoors = listOf(atlas.findRegion("closed_door"))
        val openDoors = listOf(atlas.findRegion("open_door"))

        return mapOf('.' to floors, '#' to walls, '~' to deepWater, ',' to shallowWater, '+' to closedDoors, '/' to openDoors)
    }

}