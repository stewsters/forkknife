package com.stewsters.forkknife.math

import kotlin.math.min

class RangedValue(
    var max: Int,
    var current: Int = max
) {
    fun heal(amt: Int) {
        current = min(max, current + amt)
    }

    fun damage(damage: Int) {
        current -= damage
    }

    override fun toString(): String {
        return "${current}/${max}"
    }
}