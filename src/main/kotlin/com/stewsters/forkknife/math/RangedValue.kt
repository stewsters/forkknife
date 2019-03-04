package com.stewsters.forkknife.math

import kaiju.math.min

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
}