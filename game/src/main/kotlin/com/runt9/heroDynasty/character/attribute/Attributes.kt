package com.runt9.heroDynasty.character.attribute

import com.runt9.heroDynasty.character.Modifier

class Attributes(physical: Int, mental: Int, social: Int, luck: Int) {
    private val physical = Physical(physical)
    private val mental = Mental(mental)
    private val social = Social(social)
    private val luck = Luck(luck)

    fun copy() = Attributes(physical.value, mental.value, social.value, luck.value)
    fun getModifiers(): List<Modifier> {
        val modifiers = mutableListOf<Modifier>()

        modifiers.addAll(physical.getModifiers())
        modifiers.addAll(mental.getModifiers())
        modifiers.addAll(social.getModifiers())
        modifiers.addAll(luck.getModifiers())

        return modifiers
    }

    fun forEach(loop: (Attribute) -> Unit) {
        loop(physical)
        loop(mental)
        loop(social)
        loop(luck)
    }
}