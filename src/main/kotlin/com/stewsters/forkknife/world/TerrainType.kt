package com.stewsters.forkknife.world

import org.hexworks.zircon.api.color.ANSITileColor

enum class TerrainType(val blocks: Boolean, val fore: ANSITileColor, val back: ANSITileColor, val ch: Char) {
    GROUND(
        false,
        ANSITileColor.BRIGHT_BLUE,
        ANSITileColor.GRAY,
        '.'
    ),
    WALL(
        true,
        ANSITileColor.WHITE,
        ANSITileColor.BLACK,
        'X'
    ),
    DOOR(
        false,
        ANSITileColor.CYAN,
        ANSITileColor.BLACK,
        '/'
    )


}