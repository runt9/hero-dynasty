package com.runt9.heroDynasty.character.item

import com.runt9.heroDynasty.character.Modifier
import com.runt9.heroDynasty.character.item.armor.Armor
import com.runt9.heroDynasty.character.item.weapon.Weapon

class Inventory {
    var head: Armor? = null
    var body: Armor? = null
    var legs: Armor? = null
    var arms: Armor? = null
    var feet: Armor? = null
    
    var primaryHand: Weapon? = null
    var offHand: Weapon? = null
    
    var ring1: Item? = null
    var ring2: Item? = null
    var amulet: Item? = null
    var rune: Rune? = null

    var gold = 0.0
    
    fun getModifiers(): List<Modifier> {
        val modifiers = mutableListOf<Modifier>()
        if (head != null) modifiers.addAll(head!!.modifiers)
        if (body != null) modifiers.addAll(body!!.modifiers)
        if (legs != null) modifiers.addAll(legs!!.modifiers)
        if (arms != null) modifiers.addAll(arms!!.modifiers)
        if (feet != null) modifiers.addAll(feet!!.modifiers)
        if (primaryHand != null) modifiers.addAll(primaryHand!!.modifiers)
        if (offHand != null) modifiers.addAll(offHand!!.modifiers)
        if (ring1 != null) modifiers.addAll(ring1!!.modifiers)
        if (ring2 != null) modifiers.addAll(ring2!!.modifiers)
        if (amulet != null) modifiers.addAll(amulet!!.modifiers)
        if (rune != null) modifiers.addAll(rune!!.modifiers)
        return modifiers.toList()
    }
}