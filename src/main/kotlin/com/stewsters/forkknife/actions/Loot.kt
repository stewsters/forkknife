package com.stewsters.forkknife.actions

import com.stewsters.forkknife.components.*
import com.stewsters.forkknife.math.RangedValue
import com.stewsters.forkknife.world.World

class Loot : Action {

    override fun onPerform(world: World, entity: Entity): ActionResult {

        val creature = entity.creature ?: return Failed
        val pos = entity.pos ?: return Failed


        for (box in world.map[pos].entities) {
            if (box.creature != null)
                continue
            val inventory = box.inventory
            if (inventory != null) {

                val itemsToRemove = mutableListOf<Entity>()

                for (itemEntity in inventory.items) {
                    val item = itemEntity.item!!
                    val take = when (item) {
                        is BodyArmor ->
                            if (item.quality.value > (creature.bodyArmor?.quality?.value ?: 0)) {
                                creature.bodyArmor = item
                                creature.armor = RangedValue(item.quality.value)
                                true
                            } else false
                        is Helmet ->
                            if (item.quality.value > (creature.headArmor?.quality?.value ?: 0)) {
                                creature.headArmor = item
                                true
                            } else false
                        is Gun ->
                            if (creature.primary == null) {
                                creature.primary = item
                                true
                            } else if (creature.secondary == null) {
                                creature.secondary = item
                                true
                            } else false

                        is Scope ->
                            if (creature.primary != null && (creature.primary?.scope == null || creature.primary?.scope?.quality?.value ?: 0 < item.quality.value)) {
                                creature.primary?.scope = item
                                true
                            } else if (creature.secondary != null && (creature.secondary?.scope == null || creature.secondary?.scope?.quality?.value ?: 0 < item.quality.value)) {
                                creature.secondary?.scope = item
                                true
                            } else false

                        is Stock ->
                            if (creature.primary != null && (creature.primary?.stock == null || creature.primary?.stock?.quality?.value ?: 0 < item.quality.value)) {
                                creature.primary?.stock = item
                                true
                            } else if (creature.secondary != null && (creature.secondary?.stock == null || creature.secondary?.stock?.quality?.value ?: 0 < item.quality.value)) {
                                creature.secondary?.stock = item
                                true
                            } else false

                        is Magazine -> if (creature.primary != null && (creature.primary?.magazine == null || creature.primary?.magazine?.quality?.value ?: 0 < item.quality.value)) {
                            creature.primary?.magazine = item
                            true
                        } else if (creature.secondary != null && (creature.secondary?.magazine == null || creature.secondary?.magazine?.quality?.value ?: 0 < item.quality.value)) {
                            creature.secondary?.magazine = item
                            true
                        } else false

                        is Barrel -> if (creature.primary != null && (creature.primary?.barrel == null || creature.primary?.barrel?.quality?.value ?: 0 < item.quality.value)) {
                            creature.primary?.barrel = item
                            true
                        } else if (creature.secondary != null && creature.secondary?.barrel == null || creature.secondary?.barrel?.quality?.value ?: 0 < item.quality.value) {
                            creature.secondary?.barrel = item
                            true
                        } else false

                        is AmmoBox -> {
                            false
                        }
                        is Backpack -> {
                            false
                        }
                    }

                    if (take) {
                        itemsToRemove.add(itemEntity)
                    }

                }
                inventory.items.removeAll(itemsToRemove)

                if (inventory.items.isEmpty()) {
                    println("emptied box")
                    box.appearance?.ch = '_'
                }
            }

        }

        return Succeeded
    }

}