package com.stewsters.forkknife.activity

import com.stewsters.forkknife.BrGame
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.input.KeyStroke
import org.hexworks.zircon.api.input.MouseAction

class LoseActivity(val game: BrGame) : Activity {
    val screen = Screens.createScreenFor(game.terminal)

    override fun keyPressed(keyStroke: KeyStroke): Boolean {
        return false
    }

    override fun mouseAction(mouseAction: MouseAction): Boolean {
        return false
    }


    override fun render() {

    }
}