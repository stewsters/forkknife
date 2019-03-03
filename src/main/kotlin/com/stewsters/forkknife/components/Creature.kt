package com.stewsters.forkknife.components

import com.stewsters.forkknife.math.RangedValue

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
    fun getInventorySize(): Int {
        return 6 + (backpack?.quality?.value ?: 0)
    }
}