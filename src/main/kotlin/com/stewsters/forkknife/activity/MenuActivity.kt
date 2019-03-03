package com.stewsters.forkknife.activity

import com.stewsters.forkknife.BrGame
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.input.KeyStroke
import org.hexworks.zircon.api.kotlin.onMousePressed
import org.hexworks.zircon.api.screen.Screen

class MenuActivity(val game: BrGame) : Activity {
    val screen: Screen

    init {


        screen = Screens.createScreenFor(game.terminal)

        // this will be 1x1 left and down from the top left
        // corner of the panel
        val header = Components.header()
            .withPosition(Position.create(20, 15))
            .withText("Battle Royale")
            .build()

        val play = Components.button()
            .withText("Play")
            .withPosition(Position.create(0, 2).relativeToBottomOf(header))
            .build()

        val exit = Components.button()
            .withText("Exit")
            .withPosition(Position.create(0, 2).relativeToBottomOf(play))
            .build()

        play.onMousePressed { mouseAction ->
            game.activity = GeneratingActivity(game)
            game.render()
        }

        exit.onMousePressed { mouseAction ->
            println("Bye")
            System.exit(0)
        }

        screen.addComponent(header)
        screen.addComponent(play)
        screen.addComponent(exit)
    }


    override fun keyPressed(keycode: KeyStroke): Boolean {
        return false
    }

    override fun render() {
        screen.display()
    }


}