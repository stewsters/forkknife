package com.stewsters.forkknife.actions

import com.stewsters.forkknife.components.Entity
import com.stewsters.forkknife.world.World

class Wait : Action {

    override fun onPerform(world: World, entity: Entity): ActionResult {
        // waste a turn
        return Succeeded
    }

}