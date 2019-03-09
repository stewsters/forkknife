package com.stewsters.forkknife.system

import com.stewsters.forkknife.leftColumn
import com.stewsters.forkknife.rightColumn
import com.stewsters.forkknife.screenSize
import com.stewsters.forkknife.world.World
import kaiju.math.getChebyshevDistance
import kaiju.math.getEuclideanDistance
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.StyleSet
import org.hexworks.zircon.api.screen.Screen

object HudRenderSystem {

    fun process(world: World, screen: Screen) {
        // blank columns out, something is weird with the clear
        val tile = Tile.createCharacterTile(' ', StyleSet.defaultStyle())
        for (x in 0 until leftColumn.x) {
            for (y in 0 until leftColumn.y) {
                screen.setTileAt(
                    Position.create(x, y), tile
                )
            }
        }
        for (x in 0..rightColumn.x) {
            for (y in 0..rightColumn.y) {
                screen.setTileAt(
                    Position.create(screenSize.x - x, y), tile
                )
            }
        }

        // left side
        // Left menu - our squad, info
        // Right menu- others we can see.

//        screen.foregroundColor = ANSITileColor.WHITE
        var i = 0
        val characters = world.characters
        characters.forEach { entity ->
            val ch = entity.appearance?.ch ?: " "
            val shoot = if (entity.creature?.shooting ?: false) "-" else " "
            val hp = entity.creature?.hp ?: "   "
            val armor = entity.creature?.armor ?: "   "
            write(screen, 0, i++, entity.name)
            write(screen, 0, i++, "${ch}${shoot} ${hp} ${armor}")
            entity.creature?.primary?.apply {
                write(screen, 0, i, gunType.name)

                write(screen, 6, i, (this.barrel?.quality?.value ?: 0).toString())
                write(screen, 6, i, (this.scope?.quality?.value ?: 0).toString())
                write(screen, 6, i, (this.magazine?.quality?.value ?: 0).toString())
                write(screen, 6, i, (this.stock?.quality?.value ?: 0).toString())
                i++
            }
            entity.creature?.secondary?.apply {
                write(screen, 0, i++, gunType.name)
            }
            i++
        }

        // get players in vision, sort by proximity to main

        // number just fired hp/max armor/max
        // Name
        // Weapon - Ammo/max
        //  Gear on weapon
        // secondary - Ammo/max
        //  Gear on weapon


        // Teams left - players left
        i = 1
        val x = screenSize.x - rightColumn.x
        write(
            screen,
            x,
            0,
            "Squads ${world.actors.filter { it?.creature?.hp?.current ?: 0 > 0 }.map { it.squad }.distinct().size}"
        )

        val charPos = characters[world.selectedChar].pos!!
        world.actors
            .filter {
                (it.pos != null) && !characters.contains(it) && getEuclideanDistance(it.pos!!, charPos) < 18
            }
            .sortedBy { getChebyshevDistance(charPos, it.pos!!) }
            .forEach { entity ->
                val ch = entity.appearance?.ch ?: " "
                val shoot = if (entity.creature?.shooting ?: false) "-" else " "
                val hp = entity.creature?.hp ?: "   "
                val armor = entity.creature?.armor ?: "   "
                write(screen, x, i++, "${ch}${shoot} ${hp} ${armor}")
                write(screen, x, i++, entity.name)
                entity.creature?.primary?.apply {
                    write(screen, x, i++, gunType.name)
                }
                entity.creature?.secondary?.apply {
                    write(screen, x, i++, gunType.name)
                }
                i++
            }

    }

    private fun write(screen: Screen, x: Int, y: Int, text: String) {
        text.take(10).forEachIndexed { index, c ->
            screen.setTileAt(
                Position.create(x + index, y),
                Tile.createCharacterTile(c, StyleSet.defaultStyle())
            )

        }

    }
}