package com.runt9.heroDynasty.dungeon

import squidpony.squidgrid.mapping.DungeonGenerator
import squidpony.squidgrid.mapping.DungeonUtility

class DungeonGenerator {
    fun generateDungeon(): Array<CharArray> {
        val dungeonGen = DungeonGenerator(85, 85)
        dungeonGen.addDoors(5, true)
        val rawDungeon = dungeonGen.generate()
        return DungeonUtility.closeDoors(rawDungeon)
    }
}