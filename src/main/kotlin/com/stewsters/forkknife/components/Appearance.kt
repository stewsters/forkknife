package com.stewsters.forkknife.components

import org.hexworks.zircon.api.color.TileColor

class Appearance(
    val ch: Char,
    val color: TileColor,
    val back: TileColor? = null,
    val priority:Int = 0
)