package com.stewsters.forkknife.actions

import com.stewsters.forkknife.components.Entity
import com.stewsters.forkknife.world.World

class Loot:Action{

    override fun onPerform(world: World, entity: Entity): ActionResult {

        val toLoot = world.map[entity.pos!!].entities.filter { it.creature == null }

        // TODO: equip with the finest gear they have

        return Succeeded
    }

}