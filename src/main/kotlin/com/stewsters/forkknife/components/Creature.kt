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
    var shooting: Boolean = false

    fun getInventorySize(): Int {
        return 6 + (backpack?.quality?.value ?: 0)
    }

    fun doDamage(damage: Int) {
        //TODO: need to deal damage
        // if we have armor, remove from that

        // if we have life, remove from that

        // if we reach zero, we ded

//        var damage =
//            if(damage>0 && target.creature.armor.current>0){
//                val doneToArm
//                target.creature.armor.damage()
//            }
//
//        target.creature.hp
    }
}