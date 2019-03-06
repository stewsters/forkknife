package com.stewsters.forkknife.math

import kaiju.math.Vec2

object Bresenham2d {

    fun los(start: Vec2, end: Vec2, evaluator2d: Evaluator2d) =
        los(start.x, start.y, end.x, end.y, evaluator2d)

    fun los(x1: Int, y1: Int, x2: Int, y2: Int, evaluator2d: Evaluator2d): Boolean {
        var x1 = x1
        var y1 = y1
        val dx = Math.abs(x2 - x1)
        val dy = Math.abs(y2 - y1)

        val sx = if (x1 < x2) 1 else -1
        val sy = if (y1 < y2) 1 else -1

        var err = dx - dy

        while (true) {
            if ((x1 == x2 && y1 == y2))
                return true

            if (!evaluator2d.isGood(x1, y1))
                return false

            if (x1 == x2 && y1 == y2) {
                break
            }

            val e2 = 2 * err

            if (e2 > -dy) {
                err = err - dy
                x1 = x1 + sx
            }

            if (e2 < dx) {
                err = err + dx
                y1 = y1 + sy
            }
        }
        return true
    }
}