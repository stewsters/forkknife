package com.stewsters.forkknife.math

import com.stewsters.forkknife.world.World

class LosEvaluator(private val world: World) : Evaluator2d {

    override fun isGood(tx: Int, ty: Int): Boolean {
        return !world.map[tx, ty].type.blocks
    }
}
