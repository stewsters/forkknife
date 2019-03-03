package com.stewsters.forkknife.world

import com.stewsters.forkknife.*
import com.stewsters.forkknife.components.*
import com.stewsters.forkknife.math.RangedValue
import kaiju.math.Matrix2d
import kaiju.math.Vec2
import kaiju.math.getBoolean
import kaiju.math.getIntInRange
import org.hexworks.zircon.api.color.ANSITileColor

object MapGen {
    fun gen(): World {
        val world = World(
            Matrix2d(worldSize) { x, y ->
                val type =
                    if (x == 0 || y == 0) TerrainType.WALL else TerrainType.GROUND
                Cell(type, mutableListOf())
            },
            {
                characters[selectedChar].pos ?: worldCenter
            }
        )

        repeat(10) { squadId ->

            val squad = Squad(squadId)

            // random xy pos
            val teamStarts = Vec2(
                getIntInRange(0, world.map.xSize - 1),
                getIntInRange(0, world.map.ySize - 1)
            )
                .inclusiveVonNeumanNeighborhood()
                .filter { world.map.contains(it) }
                .shuffled()

            repeat(3) {
                val appearance = if (squad.id == 0)
                    Appearance(
                        world.getPcGlyph(),
                        ANSITileColor.WHITE
                    )
                else {
                    Appearance(
                        world.getNpcGlyph(),
                        ANSITileColor.RED
                    )
                }

                val opponent = buildStartingCharacter(
                    teamStarts[it],
                    squad,
                    if (squad.id == 0) PlayerAI() else OpponentAI,
                    appearance
                )
                world.add(opponent)
                if (squad.id == 0)
                    characters.add(opponent)
            }
        }


        return world
    }

    private fun buildStartingCharacter(pos: Vec2, squad: Squad, ai: AI, appearance: Appearance): Entity {
        val character = Entity(
            name = if (getBoolean()) getMaleName() else getFemaleName(),
            pos = pos,
            ai = ai,
            appearance = appearance,
            creature = Creature(
                hp = RangedValue(4),
                speed = 8,
                armor = RangedValue(0)
            ),
            inventory = Inventory(mutableListOf()),
            squad = squad
        )

        // you aint got shit
        //generateStartingGear(character)

        return character
    }

}