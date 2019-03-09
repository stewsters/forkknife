package com.stewsters.forkknife.components

import kaiju.math.Vec2

class Entity(
    val name: String,
    val description: String = "",
    var pos: Vec2? = null,
    // size?

    val appearance: Appearance? = null,

    val creature: Creature? = null,

    var inventory: Inventory? = null,

    val item: Item? = null,

    val ai: AI? = null,
    val squad: Squad? = null

) {
    val lastShot: Int = -1

    fun isAlive(): Boolean = creature?.hp?.current ?: 0 > 0
    fun isLootable(): Boolean = inventory?.items?.size ?: 0 > 0
}