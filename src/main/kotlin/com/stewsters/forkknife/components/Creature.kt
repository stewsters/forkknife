package com.stewsters.forkknife.components

import com.stewsters.forkknife.math.RangedValue
import kotlin.math.min

class Creature(
    val hp: RangedValue,
    var armor: RangedValue,

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

    fun takeDamage(us: Entity, damage: Int) {
        var remainingDamage = damage

        // Remove damage from armor first
        if (remainingDamage > 0 && armor.current > 0) {
            val armorDamage = min(armor.current, remainingDamage)
            armor.damage(armorDamage)
            remainingDamage -= armorDamage
        }
        // Then from health
        if (remainingDamage > 0 && hp.current > 0) {
            val healthDamage = min(hp.current, remainingDamage)
            hp.damage(healthDamage)

            // If the target died we turn him to a container of loot
            if (hp.current <= 0) {
                println("Death of ${us.name}")

                us.appearance?.ch = '%'
                us.appearance?.priority = 0

                if (us.inventory == null)
                    us.inventory = Inventory(mutableListOf())

                val inventory = us.inventory!!

                //  move gear into inventory so it can be looted
                inventory.items.addAll(listOfNotNull(bodyArmor, headArmor, primary, secondary, backpack).map { item ->
                    Entity(
                        name = item.toString(),
                        item = item
                    )
                })


            }

        }
    }
}