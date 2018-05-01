package com.runt9.heroDynasty.character.attribute

import com.runt9.heroDynasty.character.Modifier

class Social(override var value: Int) : Attribute(value) {
    override fun getModifiers(): List<Modifier> = mutableListOf()
}