package com.stewsters.forkknife.components

import com.stewsters.forkknife.actions.Action
import com.stewsters.forkknife.actions.Loot
import com.stewsters.forkknife.actions.ShootNearest
import com.stewsters.forkknife.actions.Wait
import com.stewsters.forkknife.actions.WalkToPoint
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

        val entityPos = entity.pos
        if (entityPos == null) return Wait()


        // If you are near the ring, run!
        val distanceToRing = world.distanceToRing(entity)
        if (distanceToRing < 10) {
            // move to center
            return WalkToPoint(world.ringCenter)
        }


        // hierarchy of needs

        // shoot if we can see enemy
        val nearestOpponent = world.closestVisibleEnemyInRange(entity, sightRange)
        val nearestOpponentPos = nearestOpponent?.pos
        if (nearestOpponentPos != null) {

            val primary = entity.creature?.primary
            return if (primary != null) {
                // we are armed, get close enough to shoot
                val dist = getEuclideanDistance(entityPos, nearestOpponentPos)
                if (dist <= primary.getRange()) {
                    ShootNearest()
                } else {
                    WalkToPoint(nearestOpponentPos)
                }
            } else {
                // get away
                WalkToPoint(entityPos + (entityPos - nearestOpponentPos))
            }
        }

        // see treasure, loot it - mark that we are done with it?
        val nearestTreasure =
            world.visibleThings(entity, 18).asSequence()
                .filter { !it.isAlive() && it.isLootable() }
                .filter { !alreadyLooted.contains(it) }
                .minByOrNull { getChebyshevDistance(it.pos!!, entityPos) }

        if (nearestTreasure != null) {
            val dist = getChebyshevDistance(nearestTreasure.pos!!, entityPos)
            if (dist > 0) {
                return WalkToPoint(nearestTreasure.pos!!)
            } else {
                alreadyLooted.add(nearestTreasure) // no need to recheck
                return Loot()
            }
        }


        // explore - need to have a general walking path, probably a squad goal to scout the next place
        // we basically want to explore unopened areas, keeping away from the edges

        val goal = entity.squad?.goal
        if (goal == null || getEuclideanDistance(goal, entityPos) < 5) {

            val angle = getDoubleInRange(0.0, Math.PI * 2)
            val x = 20.0 * cos(angle)
            val y = 20.0 * sin(angle)
            val potential = entityPos + Vec2(x.toInt(), y.toInt())

            if (world.map.contains(potential) && !world.map[potential].type.blocks)
                entity.squad?.goal = potential

            return WalkToPoint(potential)
        } else {
            return WalkToPoint(goal)
        }


    }
}

