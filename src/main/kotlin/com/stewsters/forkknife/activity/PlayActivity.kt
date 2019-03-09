package com.stewsters.forkknife.activity

import com.stewsters.forkknife.BrGame
import com.stewsters.forkknife.actions.ShootNearest
import com.stewsters.forkknife.actions.WalkToPoint
import com.stewsters.forkknife.highlightPath
import com.stewsters.forkknife.system.HudRenderSystem
import com.stewsters.forkknife.system.MapRenderSystem
import com.stewsters.forkknife.world.World
import com.stewsters.forkknife.worldSize
import kaiju.math.getEuclideanDistance
import kaiju.pathfinder.findPath2d
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.input.KeyStroke
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.input.MouseActionType

class PlayActivity(val game: BrGame, val world: World) : Activity {
    var target: Position? = null

    val screen = Screens.createScreenFor(game.terminal)

    init {
        screen.display()
    }

    override fun mouseAction(mouseAction: MouseAction): Boolean {
        return when (mouseAction.actionType) {
            MouseActionType.MOUSE_PRESSED -> {

                val character = world.getSelectedCharacter()
                val pos = world.screenToMap(mouseAction.position)

                if (character.ai == null) {
                    return false
                }
                character.ai.setAction(WalkToPoint(pos))
                highlightPath = listOf()

                return true
            }
            MouseActionType.MOUSE_MOVED -> {
                if (target == mouseAction.position) {
                    return false
                }
                target = mouseAction.position

                val character = world.getSelectedCharacter()
                val destination = world.screenToMap(mouseAction.position)

                val path = findPath2d(
                    size = worldSize,
                    cost = { 1.0 },
                    heuristic = { one, two -> getEuclideanDistance(one, two) },
                    neighbors = {
                        it.vonNeumanNeighborhood().filter { vec ->
                            !world.map[it].type.blocks &&
                                    world.map[it].entities.filter { it != character && it.creature != null }.isEmpty()
                        }
                    },
                    start = character.pos!!,
                    end = destination
                )

                highlightPath = path ?: listOf()

                return true

            }
            else -> false

        }

    }

    override fun keyPressed(keyStroke: KeyStroke): Boolean {
        when (keyStroke.getCharacter()) {
            '1' -> world.selectedChar = 0
            '2' -> world.selectedChar = 1
            '3' -> world.selectedChar = 2
            'f' -> world.getSelectedCharacter()?.ai?.setAction(ShootNearest())
            ' ' -> {
                world.passTime(world)
                if (world.characters.none { it.creature?.hp?.current ?: 0 > 0 }) {
                    println("All characters dead")
                    game.activity = LoseActivity(game, world)
                }
                if (world.actors.none { it.creature?.hp?.current ?: 0 > 0 && it.squad?.id != 0 }) {
                    println("Opposition Dead")
                    game.activity = WinActivity(game)
                }


            }
        }

        highlightPath = listOf()
        return true
    }

    override fun render() {

        MapRenderSystem.process(world, screen)
        HudRenderSystem.process(world, screen)
        screen.display()
    }

}