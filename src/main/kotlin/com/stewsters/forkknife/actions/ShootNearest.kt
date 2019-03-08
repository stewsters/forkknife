package com.stewsters.forkknife.actions

import com.stewsters.forkknife.components.Entity
import com.stewsters.forkknife.world.World

class ShootNearest : Action {

    override fun onPerform(world: World, entity: Entity): ActionResult {
        val gun = entity.creature?.primary
        if (gun == null)
            return Failed

        val target = world.closestVisibleEnemyInRange(entity, gun.getRange())

        if (target == null) {
            // no targets
            return Failed
        }

        if (target.creature == null) {
            println("Not alive to shoot")
            return Failed
        }

        //if target exists, is alive, is in range, and have a clear LOS
        if (target.creature.hp.current <= 0) {
            println("Shooting a dead horse")
            return Failed
        }

        target.creature.takeDamage(gun.getDamage())

        return Succeeded
    }

}