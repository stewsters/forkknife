package com.stewsters.forkknife.components

import com.stewsters.forkknife.actions.Action
import com.stewsters.forkknife.actions.Wait
import com.stewsters.forkknife.actions.WalkToPoint
import com.stewsters.forkknife.world.World
import kaiju.math.Vec2

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

object OpponentAI : AI {

    override fun getNextAction(entity: Entity, world: World): Action {
        // TODO: SICK SQUAD AI
        val nearestOpponent = world.closestVisibleEnemyInRange(entity, 18)

        // if we are prepared to fight it, do so, otherwise get away
        val pos = nearestOpponent?.pos
        if (pos != null)
            return WalkToPoint(pos)

        return Wait()

        // perceive situation, find anything interesting around you

        // communicate back to team with all sights

        // prioritize goals as a squad - probably will want some kind of hierarchy
        // give those goals out as tasks

        //

    }


}

