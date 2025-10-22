package com.stewsters.forkknife.world

import com.stewsters.forkknife.components.AI
import com.stewsters.forkknife.components.AmmoBox
import com.stewsters.forkknife.components.Appearance
import com.stewsters.forkknife.components.Barrel
import com.stewsters.forkknife.components.BodyArmor
import com.stewsters.forkknife.components.Creature
import com.stewsters.forkknife.components.Entity
import com.stewsters.forkknife.components.Gun
import com.stewsters.forkknife.components.GunType
import com.stewsters.forkknife.components.Helmet
import com.stewsters.forkknife.components.Inventory
import com.stewsters.forkknife.components.OpponentAI
import com.stewsters.forkknife.components.PlayerAI
import com.stewsters.forkknife.components.Quality
import com.stewsters.forkknife.components.Scope
import com.stewsters.forkknife.components.Squad
import com.stewsters.forkknife.getFemaleName
import com.stewsters.forkknife.getMaleName
import com.stewsters.forkknife.math.RangedValue
import com.stewsters.forkknife.worldCenter
import com.stewsters.forkknife.worldSize
import kaiju.math.Vec2
import kaiju.math.d
import kaiju.math.getBoolean
import kaiju.math.getIntInRange
import kaiju.math.matrix2dOf
import org.hexworks.zircon.api.color.ANSITileColor

object MapGen {
    fun gen(): World {


        val world = World(
            matrix2dOf(worldSize) { x, y ->
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
            val buildingDimensions = Vec2(
                getIntInRange(4, 8),
                getIntInRange(4, 8)
            )

            val upperLeft = Vec2(
                getIntInRange(1, world.map.xSize - 1 - buildingDimensions.x),
                getIntInRange(1, world.map.ySize - 1 - buildingDimensions.y)
            )

            // can we place it there

            val top = getBoolean()
            val bottom = getBoolean()
            val left = getBoolean()
            val right = (!(top || bottom || left)) || getBoolean()

            val xCenter = upperLeft.x + buildingDimensions.x / 2
            for (x in (upperLeft.x..(upperLeft.x + buildingDimensions.x))) {
                world.map[x, upperLeft.y].type = if (top && xCenter == x) TerrainType.DOOR else TerrainType.WALL
                world.map[x, upperLeft.y + buildingDimensions.y].type =
                    if (bottom && xCenter == x) TerrainType.DOOR else TerrainType.WALL
            }

            val yCenter = upperLeft.y + buildingDimensions.y / 2
            for (y in (upperLeft.y..(upperLeft.y + buildingDimensions.y))) {
                world.map[upperLeft.x, y].type = if (left && yCenter == y) TerrainType.DOOR else TerrainType.WALL
                world.map[upperLeft.x + buildingDimensions.x, y].type =
                    if (right && yCenter == y) TerrainType.DOOR else TerrainType.WALL
            }

            val gearXY = Vec2(
                upperLeft.x + getIntInRange(1, buildingDimensions.x - 2),
                upperLeft.y + getIntInRange(1, buildingDimensions.y - 2)
            )

            world.add(buildRandomLoot(gearXY))

        }


        // drop some powerups

        repeat(20) { squadId ->


            // random xy pos, take all adjacent cells, at least one should be free
            val teamStarts = Vec2(
                getIntInRange(2, world.map.xSize - 3),
                getIntInRange(2, world.map.ySize - 3)
            )
                .inclusiveMooreNeighborhood()
                .filter { world.map.contains(it) && !world.map[it].type.blocks }
                .shuffled()

            val squad = Squad(squadId, teamStarts.first())
            val squadColor = ANSITileColor.entries[(squadId % ANSITileColor.entries.size)]

            repeat(3) { gruntNumber ->
                val appearance = if (squad.id == 0)
                    Appearance(
                        world.getPcGlyph(),
                        ANSITileColor.WHITE,
                        priority = 3
                    )
                else {
                    Appearance(
                        world.getNpcGlyph(),
                        squadColor,
                        priority = 2
                    )
                }

                val opponent = buildStartingCharacter(
                    teamStarts[gruntNumber],
                    squad,
                    if (squad.id == 0) PlayerAI() else OpponentAI(),
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
                armor = RangedValue(0)
            ),
            inventory = Inventory(mutableListOf()),
            squad = squad
        )

        // you aint got shit
        //generateStartingGear(character)

        return character
    }


    val randomTypes =
        Quality.entries.map { BodyArmor(it) } +
                Quality.entries.map { Helmet(it) } +
                Quality.entries.map { Scope(it) } +
//                Quality.values().map { Stock(it) } + // TODO: stocks dont help yet
//                Quality.values().map { Magazine(it) } + // TODO: magazines dont help yet, add reloading?
                Quality.entries.map { Barrel(it) }

    private fun buildRandomLoot(pos: Vec2): Entity {
        val gear = mutableListOf<Entity>()

        if (getBoolean()) {
            val gunType = GunType.entries.random()
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
        }
        randomTypes.random().apply {
            gear.add(
                Entity(
                    name = "",
                    item = this
                )
            )
        }


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