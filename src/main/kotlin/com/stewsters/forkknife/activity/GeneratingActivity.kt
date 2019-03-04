package com.stewsters.forkknife.activity

import com.stewsters.forkknife.BrGame
import com.stewsters.forkknife.world.MapGen
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.input.KeyStroke
import org.hexworks.zircon.api.screen.Screen

class GeneratingActivity(var game: BrGame) : Activity {

    val screen: Screen

    init {
        screen = Screens.createScreenFor(game.terminal)

        val header = Components.header()
            .withPosition(25, 20)
            .withText("Generating Map")
            .build()

        screen.addComponent(header)

    }

    override fun render() {
        screen.display()

        val world = MapGen.gen()

        game.activity = PlayActivity(game, world)
        game.render()
    }

    override fun keyPressed(keyStroke: KeyStroke): Boolean {
        return false
    }

}