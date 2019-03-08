package com.stewsters.forkknife.actions

import com.stewsters.forkknife.components.Entity
import com.stewsters.forkknife.doDamage
import com.stewsters.forkknife.world.World
import kaiju.math.getChebyshevDistance

class Melee(
    val victim: Entity
) : Action {

    override fun onPerform(world: World, entity: Entity): ActionResult {

        if (victim.squad!!.id == entity.squad!!.id)
            return Failed

        if (getChebyshevDistance(victim.pos!!, entity.pos!!) > 1) {
            return Failed
        }

        doDamage(victim, 1)

        return Succeeded

    }

}