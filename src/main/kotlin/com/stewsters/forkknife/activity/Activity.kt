package com.stewsters.forkknife.activity

import org.hexworks.zircon.api.input.KeyStroke
import org.hexworks.zircon.api.input.MouseAction

interface Activity {
    fun keyPressed(keyStroke: KeyStroke): Boolean
    fun mouseAction(mouseAction: MouseAction): Boolean = false
    fun render()
}