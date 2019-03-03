package com.stewsters.forkknife.world

import com.stewsters.forkknife.components.Entity

class Cell(
    val type: TerrainType,
    val entities: MutableList<Entity>
)