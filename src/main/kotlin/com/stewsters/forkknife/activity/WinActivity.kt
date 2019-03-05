package com.stewsters.forkknife.activity

import com.stewsters.forkknife.BrGame
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.input.KeyStroke
import org.hexworks.zircon.api.kotlin.onMousePressed
import org.hexworks.zircon.api.screen.Screen

class WinActivity(val game: BrGame) : Activity {
    val screen: Screen

    init {
        screen = Screens.createScreenFor(game.terminal)

        val header = Components.header()
            .withPosition(Position.create(20, 15))
            .withText("You Win")
            .build()

        // todo: print some stuff


        val exit = Components.button()
            .withText("Back To Main")
            .withPosition(Position.create(0, 20).relativeToBottomOf(header))
            .build()

        exit.onMousePressed {
            game.activity = MenuActivity(game)
            game.render()
        }

        screen.addComponent(header)
        screen.addComponent(exit)
    }


    override fun keyPressed(keyStroke: KeyStroke): Boolean {
        return false
    }

    override fun render() {
        screen.display()
    }

}