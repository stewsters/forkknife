package com.stewsters.forkknife.components

import com.stewsters.forkknife.actions.*
import com.stewsters.forkknife.sightRange
import com.stewsters.forkknife.world.World
import kaiju.math.Vec2
import kaiju.math.getChebyshevDistance
import kaiju.math.getDoubleInRange
import kaiju.math.getEuclideanDistance
import java.lang.Math.cos
import java.lang.Math.sin

interface AI {
    // receives instructions
    fun setAction(action: Action) = Unit

    // acts on them
    fun getNextAction(entity: Entity, world: World): Action

    fun highlight(worldPos: Vec2): Boolean = false
}

class PlayerAI : AI {
    private var action: Action? = null

    override fun getNextAction(entity: Entity, world: World): Action {
        return action ?: Wait()
    }

    override fun setAction(action: Action) {
        this.action = action
    }

    override fun highlight(worldPos: Vec2): Boolean {
        if (action is WalkToPoint)
            return (action as WalkToPoint).destination == worldPos
        return false
    }
}

class OpponentAI : AI {

    val alreadyLooted = mutableListOf<Entity>()

    override fun getNextAction(entity: Entity, world: World): Action {
        // TODO: SICK SQUAD AI

        //    val nearbyThings = world.visibleThings(entity, 18)

//        val livingCreatures = nearbyThings.filter { it.creature?.hp?.current ?: 0 > 0 }
//
//        val enemies = livingCreatures
//            .filter {it.squad != null
//                    && (it.squad.id != entity.squad!!.id)
//            }

        val distanceToRing = world.distanceToRing(entity)
        if (distanceToRing < 10) {
            // move to center
            return WalkToPoint(world.ringCenter)
        }


        // hierarchy of needs


        // shoot if we can

        // see enemy
        world.closestVisibleEnemyInRange(entity, sightRange)?.let { nearestOpponent ->
            val primary = entity.creature?.primary
            if (primary != null) {
                // we are armed, get close enough to shoot
                val dist = getEuclideanDistance(entity.pos!!, nearestOpponent.pos!!)
                if (dist <= primary.getRange()) {
                    return ShootNearest()
                } else {
                    return WalkToPoint(nearestOpponent.pos!!)
                }
            } else {
                // get away
                return WalkToPoint((entity.pos!! + entity.pos!!) - nearestOpponent.pos!!)
            }
        }

        // see treasure
        // loot it - mark that we are done with it?

        world.visibleThings(entity, 18).asSequence()
            .filter { !it.isAlive() && it.isLootable() }
            .filter { !alreadyLooted.contains(it) }
            .minBy { getChebyshevDistance(it.pos!!, entity.pos!!) }
            ?.let {

                val dist = getChebyshevDistance(it.pos!!, entity.pos!!)
                if (dist > 0) {
                    return WalkToPoint(it.pos!!)
                } else {
                    alreadyLooted.add(it) // no need to recheck
                    return Loot()
                }
            }


        // explore - need to have a general walking path, probably a squad goal to scout the next place
        // we basically want to explore unopened areas, keeping away from the edges

        entity.squad?.goal?.let { goal ->
            if (getEuclideanDistance(goal, entity.pos!!) < 5) {

                val angle = getDoubleInRange(0.0, Math.PI * 2)
                val x = 20.0 * cos(angle)
                val y = 20.0 * sin(angle)
                val potential = entity.pos!! + Vec2[x.toInt(), y.toInt()]

                if (world.map.contains(potential) && !world.map[potential].type.blocks)
                    entity.squad.goal = potential
            }
            return WalkToPoint(goal)
        }


        return Wait()

        // perceive situation, find anything interesting around you

        // communicate back to team with all sights

        // prioritize goals as a squad - probably will want some kind of hierarchy
        // give those goals out as tasks

        //

    }


}

