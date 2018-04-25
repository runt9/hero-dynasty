package com.runt9.heroDynasty.dungeon

import com.runt9.heroDynasty.lib.AppConst.bigHeight
import com.runt9.heroDynasty.lib.AppConst.bigWidth
import squidpony.squidgrid.mapping.DungeonGenerator
import squidpony.squidgrid.mapping.DungeonUtility

class DungeonGenerator {
    fun generateDungeon(): Array<CharArray> {
        val dungeonGen = DungeonGenerator(bigWidth.toInt(), bigHeight.toInt())
        dungeonGen.addDoors(5, true)
        val rawDungeon = dungeonGen.generate()
        return DungeonUtility.closeDoors(rawDungeon)
    }
}