package com.stewsters.forkknife.components

import com.stewsters.forkknife.math.RangedValue
import kotlin.math.min

class Creature(
    val hp: RangedValue,
    val armor: RangedValue,
    val speed: Int,

    var bodyArmor: BodyArmor? = null,
    var headArmor: Helmet? = null,
    var primary: Gun? = null,
    var secondary: Gun? = null,
    var backpack: Backpack? = null

) {
    var shooting: Boolean = false

    fun getInventorySize(): Int {
        return 6 + (backpack?.quality?.value ?: 0)
    }

    fun takeDamage(damage: Int) {
        var remainingDamage = damage
        //TODO: need to deal damage
        // if we have armor, remove from that


        if (remainingDamage > 0 && armor.current > 0) {
            val armorDamage = min(armor.current, remainingDamage)
            armor.damage(armorDamage)
            remainingDamage -= armorDamage
        }
        if (remainingDamage > 0 && hp.current > 0) {
            val healthDamage = min(hp.current, remainingDamage)
            hp.damage(healthDamage)
            // todo: overdamage?

            // todo: death
            if (hp.current <= 0)
                println("Death")
        }
    }
}