package com.runt9.heroDynasty.character

// TODO: Constants?
data class Resource(
        var current: Double = 0.0,
        var max: Double = 0.0,
        var base: Double = BASE,
        var perLevel: Double = BASE_PER_LEVEL,
        var regen: Double = BASE_REGEN
) {
    companion object {
        const val BASE: Double = 100.0
        const val BASE_PER_LEVEL: Double = 10.0
        const val BASE_REGEN: Double = 1.0
    }

    fun recalculate(level: Int, addDifference: Boolean = true) {
        val oldMax = max
        max = base + perLevel * level
        val diff = max - oldMax
        if (diff > 0 && addDifference) {
            current += diff
        }

        if (current > max) {
            current = max
        }
    }

    fun doRegen() {
        if (current != max) {
            current = Math.min(current + regen, max)
        }
    }
}