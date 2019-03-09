package com.stewsters.forkknife.components

import org.hexworks.zircon.api.color.TileColor

class Appearance(
    var ch: Char,
    val color: TileColor,
    val back: TileColor? = null,
    var priority: Int = 0
)