package com.stewsters.forkknife.world

import com.stewsters.forkknife.components.Entity
import com.stewsters.forkknife.halfPlayAreaSize
import com.stewsters.forkknife.leftColumn
import com.stewsters.forkknife.worldCenter
import kaiju.math.Matrix2d
import kaiju.math.Vec2
import kaiju.math.getEuclideanDistance
import kaiju.math.max
import org.hexworks.zircon.api.data.Position

class World(
//    val size: Vec3,
    val map: Matrix2d<Cell>,
    var cameraCenter: (world: World) -> Vec2
) {

    val characters: MutableList<Entity> = mutableListOf()
    var selectedChar = 0

    val actors = mutableListOf<Entity>() // with ai

    fun add(character: Entity) {
        val pos = character.pos
        if (pos != null) {
            map[pos].entities.add(character)
        }
        if (character.ai != null) {
            actors.add(character)
        }
    }

    fun remove(character: Entity) {
        val pos = character.pos
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

        return Vec2[x, y]
    }

    fun screenToMap(position: Position): Vec2 = screenToMap(position.x, position.y)

    fun screenToMap(x: Int, y: Int): Vec2 {
        // gets the world coordinates from a click
        val center = cameraCenter(this)

        val xW = x + center.x - halfPlayAreaSize.x - leftColumn.x
        val yW = y + center.y - halfPlayAreaSize.y

        return Vec2[xW, yW]
    }

    var turn = 0
    fun passTime(world: World) {
        // each person does what they do.  Then AI do what they do
        println(turn++)
        world.actors.forEach { it.ai?.act(it, world) }
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


    var center = worldCenter
    var radius = max(worldCenter.x, worldCenter.y) * 1.414

    fun isOutside(pos: Vec2): Boolean = getEuclideanDistance(center, pos) > radius

}

