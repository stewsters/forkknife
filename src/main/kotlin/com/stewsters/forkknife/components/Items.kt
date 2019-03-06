package com.stewsters.forkknife.components

sealed class Item(val quality: Quality)

class Backpack(quality: Quality) : Item(quality)  // boosts item

class BodyArmor(quality: Quality) : Item(quality) // gives secondary life bar, quality+1 hp

class Helmet(quality: Quality) : Item(quality) // reduces crit damage

class Scope(quality: Quality) : Item(quality) // increase critical percentage

class Stock(quality: Quality) : Item(quality) // increase refire accuracy

class Magazine(quality: Quality) : Item(quality) // increase rounds held

class Barrel(quality: Quality) : Item(quality) // increase ranged accuracy

class AmmoBox(val ammoType: AmmoType, var quantity: Int) : Item(Quality.WHITE)

enum class AmmoType {
    LIGHT, // pistol
    HEAVY, // rifle
    SNIPER,
    SHOTGUN
}

class Gun(
    val gunType: GunType,
    var scope: Scope? = null,
    var magazine: Magazine? = null,
    var barrel: Barrel? = null,
    var stock: Stock? = null
) : Item(Quality.WHITE) {

    fun getRange(): Int {
        return gunType.baseRange + (scope?.quality?.value ?: 0)
    }

    fun getDamage(): Int {
        return gunType.baseDamage + (barrel?.quality?.value ?: 0)
    }
    //TODO: magazine, stock
}

enum class GunType(
    val ammoType: AmmoType,
    val baseDamage: Int,
    val baseRange: Int
) {
    // pistol
    PISTOL(AmmoType.LIGHT, 1, 5),

    // smg
    PDW(AmmoType.LIGHT, 2, 5),

    // shotgun
    SHOTGUN(AmmoType.SHOTGUN, 3, 4),

    // assault rifle
    AR(AmmoType.HEAVY, 2, 10),

    // LMG -
    LMG(AmmoType.HEAVY, 3, 8),

    DMR(AmmoType.HEAVY, 1, 14),

    // sniper - long range, needs reload after each fire
    SNIPER(AmmoType.SNIPER, 2, 18)
}

// ammo capacity
// reload time
// fire turn
// range
// damage / dropoff

// overwatch mode?

// r to reload
