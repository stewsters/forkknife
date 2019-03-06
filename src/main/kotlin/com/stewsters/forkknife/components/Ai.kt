package com.stewsters.forkknife.components

import com.stewsters.forkknife.world.World
import kaiju.math.Vec2
import kaiju.math.getChebyshevDistance

interface AI {
    // receives instructions
    fun go(path: List<Vec2>)

    // acts on them
    fun act(entity: Entity, world: World)

    fun highlight(worldPos: Vec2): Boolean = false
}

sealed class Plan

class WalkTo(var path: List<Vec2>) : Plan()

class ShootAt() : Plan()

class Loot(val target: Entity) : Plan()

class Sit() : Plan()

class PlayerAI : AI {
    private var plan: Plan = Sit()

    override fun act(entity: Entity, world: World) {

        when (plan) {
            is WalkTo -> with(plan as WalkTo) {

                if (path.isNotEmpty()) {

                    val next = world.map[path[0]]

                    // todo: check if its still clear
                    if (next.type.blocks) {
                        path = listOf()
                        return
                    }

                    world.move(entity, path[0])

                    if (path.size == 1)
                        path = listOf()
                    else
                        path = path.subList(1, path.size)
                    return
                }
            }
            is ShootAt -> with(plan as ShootAt) {

                val gun = entity.creature?.primary
                if (gun == null)
                    return

                val target = world.closestVisibleEnemyInRange(entity, gun.getRange())

                if(target==null){
                    // no targets
                    return
                }

                if (target.creature == null) {
                    println("Not alive to shoot")
                    return
                }

                //if target exists, is alive, is in range, and have a clear LOS
                if (target.creature.hp.current <= 0) {
                    println("Shooting a dead horse")
                    return
                }

                target.creature.doDamage(gun.getDamage())

            }
            is Loot -> with(plan as Loot) {
                val pos = target.pos
                val ourPos = entity.pos
                if (pos == null || ourPos == null)
                    return

                // if we are adjacent, pick stuff up
                if (getChebyshevDistance(pos, ourPos) <= 1) {
                    //pick it up

                } else {
                    // move towards it
                }


            }
            is Sit -> Unit

        }


    }

    override fun go(path: List<Vec2>) {
        this.plan = WalkTo(path)
    }

    override fun highlight(worldPos: Vec2): Boolean {
        if (plan is WalkTo)
            return (plan as WalkTo).path.contains(worldPos)
        return false
    }
}

object OpponentAI : AI {

    override fun act(entity: Entity, world: World) {
        // TODO: SICK SQUAD AI

        // perceive situation, find anything interesting around you

        // communicate back to team with all sights

        // prioritize goals as a squad - probably will want some kind of hierarchy
        // give those goals out as tasks

        //

    }

    override fun go(path: List<Vec2>) {

    }

}

