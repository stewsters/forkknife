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

    fun getAmmoCapacity(): Int {
        return gunType.baseAmmoCapacity + (magazine?.quality?.value ?: 0)
    }

    fun getStability(): Int {
        return gunType.baseStability + (stock?.quality?.value ?: 0)
    }

    //TODO: magazine, stock
}

enum class GunType(
    val ammoType: AmmoType,
    val baseDamage: Int,
    val baseRange: Int,
    val baseAmmoCapacity: Int,
    val baseStability: Int,
) {
    // pistol
    PISTOL(
        ammoType = AmmoType.LIGHT,
        baseDamage = 1,
        baseRange = 5,
        baseAmmoCapacity = 2,
        baseStability = 1
    ),

    // smg
    PDW(
        ammoType = AmmoType.LIGHT,
        baseDamage = 2,
        baseRange = 5,
        baseAmmoCapacity = 4,
        baseStability = 1
    ),

    // shotgun
    SHOTGUN(
        ammoType = AmmoType.SHOTGUN,
        baseDamage = 3,
        baseRange = 3,
        baseAmmoCapacity = 1,
        baseStability = 1
    ),

    // assault rifle
    AR(
        ammoType = AmmoType.HEAVY,
        baseDamage = 2,
        baseRange = 10,
        baseAmmoCapacity = 5,
        baseStability = 1
    ),

    // LMG -
    LMG(
        ammoType = AmmoType.HEAVY,
        baseDamage = 2,
        baseRange = 8,
        baseAmmoCapacity = 10,
        baseStability = 4
    ),

    DMR(
        ammoType = AmmoType.HEAVY,
        baseDamage = 2,
        baseRange = 14,
        baseAmmoCapacity = 2,
        baseStability = 1
    ),

    // sniper - long range, needs reload after each fire
    SNIPER(
        ammoType = AmmoType.SNIPER,
        baseDamage = 2,
        baseRange = 18,
        baseAmmoCapacity = 1,
        baseStability = 0

    )
}

// ammo capacity
// reload time
// fire turn
// range
// damage / dropoff

// overwatch mode?

// r to reload
