package com.runt9.heroDynasty.character

// TODO: Constants?
data class Resource(
        var current: Double = 0.0,
        // TODO: Max calculated from base and modifiers
        var max: Double = 0.0,
        var base: Double = 100.0,
        var perLevel: Double = 10.0,
        var regen: Double = 1.0
) {
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