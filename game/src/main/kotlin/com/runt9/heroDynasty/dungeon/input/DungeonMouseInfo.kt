package com.runt9.heroDynasty.dungeon.input

import squidpony.squidai.DijkstraMap
import squidpony.squidmath.Coord
import squidpony.squidmath.GreasedRegion

data class DungeonMouseInfo(
        private val dungeon: Array<CharArray>,
        var cursor: Coord = Coord.get(-1, -1),
        val playerToCursor: DijkstraMap = DijkstraMap(dungeon, DijkstraMap.Measurement.EUCLIDEAN),
        var toCursor: MutableList<Coord> = ArrayList(200)
) {
    fun resetPlayerToCursor(playerCoord: Coord, blockage: GreasedRegion, fullReset: Boolean = false) {
        if (fullReset) {
            playerToCursor.clearGoals()
            playerToCursor.resetMap()
        }

        playerToCursor.setGoal(playerCoord)
        playerToCursor.partialScan(13, blockage)
    }
}