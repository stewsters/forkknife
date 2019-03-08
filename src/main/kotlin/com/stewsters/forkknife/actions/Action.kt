package com.stewsters.forkknife.actions

import com.stewsters.forkknife.components.Entity
import com.stewsters.forkknife.world.World

interface Action {
    fun onPerform(world: World, entity: Entity): ActionResult

}