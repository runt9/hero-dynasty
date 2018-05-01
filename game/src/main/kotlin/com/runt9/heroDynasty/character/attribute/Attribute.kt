package com.runt9.heroDynasty.character.attribute

import com.runt9.heroDynasty.character.Modifier

// TODO: Utility functions?
abstract class Attribute(open var value: Int = 0) {
    abstract fun getModifiers(): List<Modifier>
}