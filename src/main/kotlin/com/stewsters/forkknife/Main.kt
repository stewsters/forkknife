package com.stewsters.forkknife

import com.stewsters.forkknife.activity.Activity
import com.stewsters.forkknife.activity.MenuActivity
import com.stewsters.forkknife.components.Entity
import kaiju.math.Vec2
import org.hexworks.zircon.api.AppConfigs
import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.Sizes
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.input.KeyStroke
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.kotlin.onInput

val screenSize = Vec2[60, 40]
val halfScreenSize = Vec2[screenSize.x / 2, screenSize.y / 2]

val worldSize = Vec2(256, 256)
val worldCenter = Vec2(worldSize.x / 2, worldSize.y / 2)

var highlightPath = listOf<Vec2>()
val characters: MutableList<Entity> = mutableListOf()
var selectedChar = 0

class BrGame(val terminal: TileGrid, var activity: Activity? = null) {

    fun keyPress(keyStroke: KeyStroke) {
        activity?.keyPressed(keyStroke)
        activity?.render()

    }

    fun mouseAction(mouseAction: MouseAction) {
        if (activity?.mouseAction(mouseAction) == true) {
            activity?.render()
        }
    }

    fun render() {
        activity?.render()
    }

}


fun main() {

    val tileGrid = SwingApplications.startTileGrid(
        AppConfigs.newConfig()
            .withSize(Sizes.create(screenSize.x, screenSize.y))
            .withDefaultTileset(CP437TilesetResources.rexPaint20x20())
            .build()
    )

    val game = BrGame(tileGrid)
    game.activity = MenuActivity(game)
    game.render()

    tileGrid.onInput { input ->
        if (input.isKeyStroke()) {
            game.keyPress(input.asKeyStroke().get())

        } else if (input.isMouseAction()) {
            game.mouseAction(input.asMouseAction().get())
        }
    }
}


fun doDamage(character: Entity, damage: Int) {
    character.creature?.hp?.damage(damage)
}
