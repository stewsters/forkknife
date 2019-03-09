package com.stewsters.forkknife.activity

import com.stewsters.forkknife.BrGame
import com.stewsters.forkknife.world.World
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.input.KeyStroke
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.kotlin.onMousePressed

class LoseActivity(val game: BrGame, val world: World) : Activity {
    val screen = Screens.createScreenFor(game.terminal)

    init {
        val header = Components.header()
            .withPosition(Position.create(15, 10))
            .withText("Your Squad Has Been Eliminated")
            .build()

        val exit = Components.button()
            .withText("Back To Main")
            .withPosition(Position.create(0, 4).relativeToBottomOf(header))
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

    override fun mouseAction(mouseAction: MouseAction): Boolean {
        return false
    }

    override fun render() {
        screen.display()
    }
}