package com.stewsters.forkknife.world

import com.stewsters.forkknife.components.Entity
import com.stewsters.forkknife.halfPlayAreaSize
import com.stewsters.forkknife.leftColumn
import com.stewsters.forkknife.math.Bresenham2d
import com.stewsters.forkknife.math.LosEvaluator
import com.stewsters.forkknife.worldCenter
import kaiju.math.*

import org.hexworks.zircon.api.data.Position
import kotlin.math.max
import kotlin.streams.toList

class World(
//    val size: Vec3,
    val map: Matrix2d<Cell>,
    var cameraCenter: (world: World) -> Vec2
) {
    val losEntity = LosEvaluator(this)

    val characters = mutableListOf<Entity>()
    var selectedChar = 0

    fun getSelectedCharacter(): Entity {
        return characters[selectedChar]
    }

    val ringCenter = Vec2(
            getIntInRange(map.xSize / 4, 3 * map.xSize / 4),
            getIntInRange(map.ySize / 4, 3 * map.ySize / 4)
    )

    var radius = max(worldCenter.x, worldCenter.y) * 2

    fun isOutsideRing(pos: Vec2): Boolean = getEuclideanDistance(ringCenter, pos) > radius

//    val entities = mutableListOf<Entity>()
    val actors = mutableListOf<Entity>() // with ai

    fun add(character: Entity) {
        val pos = character.pos
//        entities.add(character)
        if (pos != null) {
            map[pos].entities.add(character)
        }
        if (character.ai != null) {
            actors.add(character)
        }
    }

    fun remove(character: Entity) {
        val pos = character.pos
//        entities.remove(character)
        if (pos != null) {
            map[pos].entities.remove(character)
        }
        if (character.ai != null) {
            actors.add(character)
        }
    }

    fun move(character: Entity, dest: Vec2) {
        val pos = character.pos
        if (pos != null) {
            map[pos].entities.remove(character)
            character.pos = dest
            map[dest].entities.add(character)
        }

    }

    // TODO: this
    // gets position relative to player
    fun screenToPlayer(position: Position): Vec2 {
        // gets the world coordinates from a click
        val center = cameraCenter(this)

        val x = position.x - center.x
        val y = position.y - center.y

        return Vec2(x, y)
    }

    fun screenToMap(position: Position): Vec2 = screenToMap(position.x, position.y)

    fun screenToMap(x: Int, y: Int): Vec2 {
        // gets the world coordinates from a click
        val center = cameraCenter(this)

        val xW = x + center.x - halfPlayAreaSize.x - leftColumn.x
        val yW = y + center.y - halfPlayAreaSize.y

        return Vec2(xW, yW)
    }

    var turn = 0
    fun passTime(world: World) {
        // each person does what they do.  Then AI do what they do
        println(turn++)

        if ((turn / 20) % 3 == 0)
            radius -= 1 // todo, close, then hold
        // If players are dead, stop


        // if all not players are dead, stop

        world.actors.forEach {

            if (it.creature?.hp?.current ?: 0 > 0) {
                val ai = it.ai ?: return
                val action = ai.getNextAction(it, world)
                action.onPerform(world, it)
            }

            if (isOutsideRing(it.pos!!)) {
                it.creature?.takeDamage(it, 1)
            }
        }
    }

    var pcId = 0
    val pcGlyphs = ('1'..'9').toList()

    fun getPcGlyph(): Char {
        val char = pcGlyphs[pcId++]
        if (pcId >= pcGlyphs.size)
            pcId = 0
        return char
    }


    var npcId = 0
    val npcGlyphs = (('a'..'z').toList() + ('A'..'Z').toList()).shuffled()

    fun getNpcGlyph(): Char {
        val char = npcGlyphs[npcId++]
        if (npcId >= npcGlyphs.size)
            npcId = 0
        return char
    }

    fun visibleThings(entity: Entity, range: Int): List<Entity> {
        return actors.asSequence()
            .filter { getEuclideanDistance(entity.pos!!, it.pos!!) <= range }
            .filter { Bresenham2d.los(entity.pos!!, it.pos!!, losEntity) }
            .toList()
    }

    fun closestVisibleEnemyInRange(entity: Entity, range: Int): Entity? {
        val pos = entity.pos!!

        return actors.asSequence()
            .filter { it.squad != entity.squad && it.creature?.hp?.current ?: 0 > 0 }
            .filter { getEuclideanDistance(pos, it.pos!!) <= range }
            .filter { Bresenham2d.los(pos, it.pos!!, losEntity) }
            .minByOrNull { getEuclideanDistance(pos, it.pos!!) }
    }

    fun distanceToRing(entity: Entity): Int {
        return radius - getChebyshevDistance(entity.pos!!, ringCenter)
    }

//    fun getRandomPackageLocation(): Vec2 {
//        return entities.filter { it.isLootable() }.random().pos ?: ringCenter
//    }

}

