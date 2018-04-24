package com.runt9.heroDynasty.dungeon

import com.runt9.heroDynasty.lib.AppConst.bigHeight
import com.runt9.heroDynasty.lib.AppConst.bigWidth
import squidpony.squidgrid.mapping.DungeonGenerator

class DungeonGenerator {
    fun generateDungeon(): Array<CharArray> {
        val dungeonGen = DungeonGenerator(bigWidth, bigHeight)
        dungeonGen.addGrass(10)
        dungeonGen.addWater(13, 6)
        dungeonGen.addDoors(15, true)
        return dungeonGen.generate()
    }
}