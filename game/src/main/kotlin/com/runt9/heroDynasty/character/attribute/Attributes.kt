package com.runt9.heroDynasty.character.attribute

class Attributes(physical: Int, mental: Int, magical: Int, social: Int, luck: Int) {
    val physical = Physical(physical)
    val mental = Mental(mental)
    val magical = Magical(magical)
    val social = Social(social)
    val luck = Luck(luck)

    fun copy() = Attributes(physical.value, mental.value, magical.value, social.value, luck.value)
}