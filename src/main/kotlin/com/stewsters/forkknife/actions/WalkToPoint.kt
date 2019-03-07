package com.stewsters.forkknife.actions

import com.stewsters.forkknife.components.Entity
import com.stewsters.forkknife.world.World
import com.stewsters.forkknife.worldSize
import kaiju.math.Vec2
import kaiju.math.getEuclideanDistance
import kaiju.pathfinder.findPath2d

class WalkToPoint(
    val pos: Vec2
) : Action {

    override fun onPerform(world: World, entity: Entity): ActionResult {
        if (entity.pos == pos)
            return Succeeded

        // path to point
        val path = findPath2d(
            size = worldSize,
            cost = { 1.0 },
            heuristic = { one, two -> getEuclideanDistance(one, two).toDouble() },
            neighbors = {
                it.vonNeumanNeighborhood().filter { vec ->
                    !world.map[it].type.blocks &&
                            world.map[it].entities.filter { it != entity && it.creature != null }.isEmpty()
                }
            },
            start = entity.pos!!,
            end = Vec2(pos.x, pos.y)
        )

        if (path == null || path.size < 2)
            return Failed

        val next = world.map[path[1]]

        if (next.type.blocks) {
            return Failed
        }

        world.move(entity, path[1])

        // get next step, go there

        // if standing on loot box, loot next turn

        if (entity.pos == pos)
            return Succeeded
        else
            return InProgress

    }

}