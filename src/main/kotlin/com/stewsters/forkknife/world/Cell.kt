package com.stewsters.forkknife.world

import com.stewsters.forkknife.components.Entity

class Cell(
    var type: TerrainType,
    val entities: MutableList<Entity>
)