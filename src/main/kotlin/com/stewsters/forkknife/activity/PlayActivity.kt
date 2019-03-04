package com.stewsters.forkknife.activity

import com.stewsters.forkknife.*
import com.stewsters.forkknife.components.PlayerAI
import com.stewsters.forkknife.world.World
import kaiju.math.Vec2
import kaiju.math.getChebyshevDistance
import kaiju.math.getEuclideanDistance
import kaiju.pathfinder.findPath2d
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.input.KeyStroke
import org.hexworks.zircon.api.input.MouseAction
import org.hexworks.zircon.api.input.MouseActionType

class PlayActivity(val game: BrGame, val world: World) :
    Activity {
    var target: Position? = null

    val screen = Screens.createScreenFor(game.terminal)

    init {
        screen.display()
    }

    override fun mouseAction(mouseAction: MouseAction): Boolean {
        return when (mouseAction.actionType) {
            MouseActionType.MOUSE_PRESSED -> {

                val character = characters[selectedChar]
                val pos = world.screenToMap(mouseAction.position)

                if (character.ai != null) {

                    val path = findPath2d(
                        size = worldSize,
                        cost = { 1.0 },
                        heuristic = { one, two -> getEuclideanDistance(one, two).toDouble() },
                        neighbors = {
                            it.vonNeumanNeighborhood().filter { vec ->
                                !world.map[it].type.blocks &&
                                world.map[it].entities.filter { it != character }.isEmpty()
                            }
                        },
                        start = character.pos!!,
                        end = Vec2(pos.x, pos.y)
                    )

                    if (path != null) {
                        character.ai.go(path)
                        highlightPath = listOf()
                    }
                }
                return true
            }
            MouseActionType.MOUSE_MOVED -> {
                if (target == mouseAction.position) {
                    return false
                }
                target = mouseAction.position

                val character = characters[selectedChar]
                val pos = world.screenToMap(mouseAction.position)

                val path = findPath2d(
                    size = worldSize,
                    cost = { 1.0 },
                    heuristic = { one, two -> getChebyshevDistance(one, two).toDouble() },
                    neighbors = {
                        it.vonNeumanNeighborhood().filter { vec ->
                            !world.map[it].type.blocks &&
                            world.map[it].entities.filter { it != character }.isEmpty()
                        }
                    },
                    start = character.pos!!,
                    end = Vec2(pos.x, pos.y)
                )

                highlightPath = path ?: listOf()

                return true

            }
            else -> false

        }

    }

    override fun keyPressed(keyStroke: KeyStroke): Boolean {
        when (keyStroke.getCharacter()) {
            '1' -> selectedChar = 0
            '2' -> selectedChar = 1
            '3' -> selectedChar = 2
            ' ' -> world.passTime(world)
        }

        highlightPath = listOf()
        return true
    }

    override fun render() {

        for (x in 0 until screenSize.x) {
            for (y in 0 until screenSize.y) {
                // TODO: if lit

                val worldPos = world.screenToMap(x, y)

                var fore: TileColor
                var back: TileColor
                var char: Char

                if (!world.map.contains(worldPos)) {
                    fore = ANSITileColor.BLACK
                    back = ANSITileColor.BLACK
                    char = ' '

                } else {

                    val cell = world.map[worldPos]
                    val entity = cell.entities.firstOrNull()

                    if (entity?.appearance != null) {
                        fore = entity.appearance.color
                        back = entity.appearance.back ?: cell.type.back
                        char = entity.appearance.ch

                    } else {
                        fore = cell.type.fore
                        back = cell.type.back
                        char = cell.type.ch
                    }
                }

                if (highlightPath.contains(worldPos)) {
                    back = ANSITileColor.GREEN
                } else {
                    val playerAI = characters[selectedChar].ai as PlayerAI
                    if (playerAI.highlight(worldPos)) {
                        back = ANSITileColor.YELLOW
                    }
                }


                game.terminal.setTileAt(
                    Positions.create(x, y),
                    Tiles.newBuilder()
                        .withBackgroundColor(back)
                        .withForegroundColor(fore)
                        .withCharacter(char)
                        .build()
                )
            }
        }
    }

}