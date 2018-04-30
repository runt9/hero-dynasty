package com.runt9.heroDynasty.character.item

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
    var light: Item? = null
    var rune: Rune? = null

    var gold = 0.0
}