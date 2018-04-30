package com.runt9.heroDynasty.dungeon

import com.runt9.heroDynasty.util.AppConst.dungeonHeight
import com.runt9.heroDynasty.util.AppConst.dungeonWidth
import squidpony.squidgrid.mapping.DungeonGenerator
import squidpony.squidgrid.mapping.DungeonUtility

class DungeonGenerator {
    fun generateDungeon(): Array<CharArray> {
        val dungeonGen = DungeonGenerator(dungeonWidth.toInt(), dungeonHeight.toInt())
        dungeonGen.addDoors(5, true)
        val rawDungeon = dungeonGen.generate()
        return DungeonUtility.closeDoors(rawDungeon)
    }
}