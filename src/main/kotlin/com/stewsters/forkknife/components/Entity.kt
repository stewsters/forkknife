package com.stewsters.forkknife.components

import kaiju.math.Vec2

class Entity(
    val name: String,
    val description: String = "",
    var pos: Vec2? = null,
    // size?

    val appearance: Appearance? = null,

    val creature: Creature? = null,

    val inventory: Inventory? = null,

    val item: Item? = null,

    val ai: AI? = null,
    val squad: Squad? = null

)