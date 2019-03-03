package com.stewsters.forkknife.components

sealed class Item(val quality: Quality)

class Backpack(quality: Quality) : Item(quality)  // boosts item

class BodyArmor(quality: Quality) : Item(quality) // gives secondary life bar, quality+1 hp

class Helmet(quality: Quality) : Item(quality) // reduces crit damage

class Scope(quality: Quality) : Item(quality) // increase critical percentage

class Stock(quality: Quality) : Item(quality) // increase refire accuracy

class Magazine(quality: Quality) : Item(quality) // increase rounds held

class Barrel(quality: Quality) : Item(quality) // increase ranged accuracy

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
)

enum class GunType(val ammoType: AmmoType) {
    // pistol
    PISTOL(AmmoType.LIGHT),

    // smg
    PDW(AmmoType.LIGHT),

    // shotgun
    SHOTGUN(AmmoType.SHOTGUN),

    // assault rifle
    AR(AmmoType.HEAVY),

    // LMG -
    LMG(AmmoType.HEAVY),

    DMR(AmmoType.HEAVY),

    // sniper - long range, needs reload after each fire
    SNIPER(AmmoType.SNIPER)
}

// ammo capacity
// reload time
// fire turn
// range
// damage / dropoff

// overwatch mode?

// r to reload
