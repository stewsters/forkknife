package com.stewsters.forkknife.system

import com.stewsters.forkknife.components.PlayerAI
import com.stewsters.forkknife.highlightPath
import com.stewsters.forkknife.leftColumn
import com.stewsters.forkknife.math.Bresenham2d
import com.stewsters.forkknife.math.LosEvaluator
import com.stewsters.forkknife.playAreaScreenSize
import com.stewsters.forkknife.world.World
import org.hexworks.zircon.api.Positions
import org.hexworks.zircon.api.Tiles
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.screen.Screen

object MapRenderSystem {

    fun process(world: World, screen: Screen) {
        val los = LosEvaluator(world)
        val pos = world.getSelectedCharacter().pos!!
        for (x in leftColumn.x until (playAreaScreenSize.x + leftColumn.x)) {
            for (y in 0 until playAreaScreenSize.y) {

                val worldPos = world.screenToMap(x, y)

                var fore: TileColor
                var back: TileColor
                var char: Char

                //Todo: if they shot highlight, if they are allies show number
                if (!world.map.contains(worldPos) ||
                    !(Bresenham2d.los(pos.x, pos.y, worldPos.x, worldPos.y, los) ||
                            Bresenham2d.los(worldPos.x, worldPos.y, pos.x, pos.y, los))
                ) {
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
                    val playerAI = world.characters[world.selectedChar].ai as PlayerAI
                    if (playerAI.highlight(worldPos)) {
                        back = ANSITileColor.YELLOW
                    }
                }


                screen.setTileAt(
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