package com.stewsters.forkknife.world

import com.stewsters.forkknife.components.*
import com.stewsters.forkknife.getFemaleName
import com.stewsters.forkknife.getMaleName
import com.stewsters.forkknife.math.RangedValue
import com.stewsters.forkknife.worldCenter
import com.stewsters.forkknife.worldSize
import kaiju.math.*
import org.hexworks.zircon.api.color.ANSITileColor

object MapGen {
    fun gen(): World {


        val world = World(
            Matrix2d(worldSize) { x, y ->
                val type =
                    if (x == 0 || y == 0 || x == worldSize.x - 1 || y == worldSize.y - 1)
                        TerrainType.WALL
                    else
                        TerrainType.GROUND
                Cell(type, mutableListOf())
            },
            {
                it.getSelectedCharacter().pos ?: worldCenter
            }
        )

        // drop some buildings on that world

        repeat(80) {
            val buildingDimentions = Vec2[
                    getIntInRange(4, 8),
                    getIntInRange(4, 8)
            ]

            val upperLeft = Vec2[
                    getIntInRange(1, world.map.xSize - 1 - buildingDimentions.x),
                    getIntInRange(1, world.map.ySize - 1 - buildingDimentions.y)
            ]

            // can we place it there

            val top = getBoolean()
            val bottom = getBoolean()
            val left = getBoolean()
            val right = (!(top || bottom || left)) || getBoolean()

            val xCenter = upperLeft.x + buildingDimentions.x / 2
            for (x in (upperLeft.x..(upperLeft.x + buildingDimentions.x))) {
                world.map[x, upperLeft.y].type = if (top && xCenter == x) TerrainType.DOOR else TerrainType.WALL
                world.map[x, upperLeft.y + buildingDimentions.y].type =
                    if (bottom && xCenter == x) TerrainType.DOOR else TerrainType.WALL
            }

            val yCenter = upperLeft.y + buildingDimentions.y / 2
            for (y in (upperLeft.y..(upperLeft.y + buildingDimentions.y))) {
                world.map[upperLeft.x, y].type = if (left && yCenter == y) TerrainType.DOOR else TerrainType.WALL
                world.map[upperLeft.x + buildingDimentions.x, y].type =
                    if (right && yCenter == y) TerrainType.DOOR else TerrainType.WALL
            }

            val gearXY = Vec2[
                    upperLeft.x + getIntInRange(1, buildingDimentions.x - 2),
                    upperLeft.y + getIntInRange(1, buildingDimentions.y - 2)
            ]

            world.add(buildRandomLoot(gearXY))

        }


        // drop some powerups

        repeat(10) { squadId ->

            val squad = Squad(squadId)

            // random xy pos
            val teamStarts = Vec2(
                getIntInRange(2, world.map.xSize - 3),
                getIntInRange(2, world.map.ySize - 3)
            )
                .inclusiveVonNeumanNeighborhood()
                .filter { world.map.contains(it) && !world.map[it].type.blocks }
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
                    world.characters.add(opponent)
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


    private fun buildRandomLoot(pos: Vec2): Entity {
        val gear = mutableListOf<Entity>()


        val gunType = GunType.values().random()
        gear.add(
            Entity(
                name = gunType.name,
                item = Gun(
                    gunType = gunType
                )
            )
        )
        gear.add(
            Entity(
                name = gunType.ammoType.name,
                item = AmmoBox(
                    ammoType = gunType.ammoType,
                    quantity = d(30)
                )
            )
        )


        return Entity(
            name = "Loot Box",
            appearance = Appearance('?', ANSITileColor.WHITE, ANSITileColor.YELLOW),
            pos = pos,
            inventory = Inventory(
                gear
            )
        )
    }
}