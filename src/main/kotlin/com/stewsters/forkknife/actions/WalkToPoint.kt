package com.stewsters.forkknife.actions

import com.stewsters.forkknife.components.Entity
import com.stewsters.forkknife.world.World
import com.stewsters.forkknife.worldSize
import kaiju.math.Vec2
import kaiju.math.getEuclideanDistance
import kaiju.pathfinder.findPath2d

class WalkToPoint(
    val destination: Vec2
) : Action {

    override fun onPerform(world: World, entity: Entity): ActionResult {
        if (entity.pos == destination)
            return Succeeded

//        val start = System.currentTimeMillis()
        // path to point
        val path = findPath2d(
            size = worldSize,
            cost = { 1.0 },
            heuristic = { one, two -> getEuclideanDistance(one, two) },
            neighbors = {
                it.vonNeumanNeighborhood().filter { vec ->
                    !world.map[it].type.blocks &&
                            world.map[it].entities.filter { it != entity && it.isAlive() }.isEmpty()
                }
            },
            start = entity.pos!!,
            end = destination
        )
//        println(System.currentTimeMillis() - start)

        if (path == null || path.size < 2)
            return Failed

        val next = world.map[path[1]]

        if (next.type.blocks || next.entities.any { it.isAlive() }) {
            return Failed
        }

        world.move(entity, path[1])

        val meleeTargets = next.entities.filter {
            it.isAlive()
                    && it.squad != null && entity.squad != null
                    && it.squad.id != entity.squad.id
        }
        if (meleeTargets.isNotEmpty()) {
            return Melee(meleeTargets.first()).onPerform(world, entity)
        }

        // get next step, go there
        if (next.entities.filter { !it.isAlive() }.isNotEmpty()) {
            // Free loot pickup
            return Loot().onPerform(world, entity)
            //return Alternative(Loot())
        }


        // if standing on loot box, loot next turn
        if (entity.pos == destination)
            return Succeeded
        else
            return InProgress

    }

}