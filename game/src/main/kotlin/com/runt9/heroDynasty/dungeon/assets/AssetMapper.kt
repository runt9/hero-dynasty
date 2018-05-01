package com.runt9.heroDynasty.dungeon.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion

class AssetMapper {
    private val atlas: TextureAtlas = TextureAtlas(Gdx.files.internal("hero-dynasty.atlas"))

    fun getRegion(region: String): AtlasRegion = atlas.findRegion(region)

    // TODO: Can use atlas.findRegions instead
    fun getAssetMap(): Map<Char, List<AtlasRegion>> {
        val floors = atlas.findRegions("floor").toList()
        val walls = atlas.findRegions("wall").toList()
        val shallowWater = atlas.findRegions("shallow_water").toList()
        val deepWater = atlas.findRegions("deep_water").toList()
        val closedDoors = atlas.findRegions("closed_door").toList()
        val openDoors = atlas.findRegions("open_door").toList()

        return mapOf('.' to floors, '#' to walls, '~' to deepWater, ',' to shallowWater, '+' to closedDoors, '/' to openDoors)
    }

}