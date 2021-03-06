/*
 * Silent Gear -- VanillaGearSalvage
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.silentchaos512.gear.block.salvager;

import com.google.common.collect.ImmutableList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.silentchaos512.gear.SilentGear;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;

final class VanillaGearSalvage {
    private static final Collection<Item> items = ImmutableList.of(
            Items.DIAMOND_SWORD, Items.GOLDEN_SWORD, Items.IRON_SWORD, Items.STONE_SWORD, Items.WOODEN_SWORD,
            Items.DIAMOND_PICKAXE, Items.GOLDEN_PICKAXE, Items.IRON_PICKAXE, Items.STONE_PICKAXE, Items.WOODEN_PICKAXE,
            Items.DIAMOND_SHOVEL, Items.GOLDEN_SHOVEL, Items.IRON_SHOVEL, Items.STONE_SHOVEL, Items.WOODEN_SHOVEL,
            Items.DIAMOND_AXE, Items.GOLDEN_AXE, Items.IRON_AXE, Items.STONE_AXE, Items.WOODEN_AXE,
            Items.DIAMOND_HOE, Items.GOLDEN_HOE, Items.IRON_HOE, Items.STONE_HOE, Items.WOODEN_HOE,
            Items.LEATHER_BOOTS, Items.LEATHER_CHESTPLATE, Items.LEATHER_HELMET, Items.LEATHER_LEGGINGS,
            Items.CHAINMAIL_BOOTS, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_LEGGINGS,
            Items.IRON_BOOTS, Items.IRON_CHESTPLATE, Items.IRON_HELMET, Items.IRON_LEGGINGS,
            Items.GOLDEN_BOOTS, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_HELMET, Items.GOLDEN_LEGGINGS,
            Items.DIAMOND_BOOTS, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_HELMET, Items.DIAMOND_LEGGINGS
    );

    private VanillaGearSalvage() {}

    static boolean isVanillaGear(ItemStack stack) {
        return items.contains(stack.getItem());
    }

    static int getHeadCount(ItemStack stack) {
        Item item = stack.getItem();
        //noinspection ChainOfInstanceofChecks
        if (item instanceof ItemSpade) return 1;
        if (item instanceof ItemSword || item instanceof ItemHoe) return 2;
        if (item instanceof ItemPickaxe || item instanceof ItemAxe) return 3;
        if (item instanceof ItemArmor) {
            int multi = Objects.requireNonNull(item.getRegistryName()).getPath().startsWith("chainmail") ? 4 : 1;
            EntityEquipmentSlot type = ((ItemArmor) item).armorType;
            if (type == EntityEquipmentSlot.CHEST) return 8 * multi;
            if (type == EntityEquipmentSlot.FEET) return 4 * multi;
            if (type == EntityEquipmentSlot.HEAD) return 5 * multi;
            if (type == EntityEquipmentSlot.LEGS) return 7 * multi;
        }

        SilentGear.log.warn("Tried to salvage '{}' as vanilla gear, but could not identify item type", stack);
        return 0;
    }

    static int getRodCount(ItemStack stack) {
        //noinspection ChainOfInstanceofChecks
        if (stack.getItem() instanceof ItemArmor) return 0;
        if (stack.getItem() instanceof ItemSword) return 1;
        return 2;
    }

    @Nullable
    static Item getHeadItem(ItemStack stack) {
        String name = Objects.requireNonNull(stack.getItem().getRegistryName()).getPath();
        if (name.startsWith("diamond")) return Items.DIAMOND;
        if (name.startsWith("golden")) return Items.GOLD_INGOT;
        if (name.startsWith("iron")) return Items.IRON_INGOT;
        if (name.startsWith("stone")) return Item.getItemFromBlock(Blocks.COBBLESTONE);
        if (name.startsWith("wooden")) return Item.getItemFromBlock(Blocks.PLANKS);
        if (name.startsWith("leather")) return Items.LEATHER;
        if (name.startsWith("chainmail")) return Items.IRON_NUGGET;

        SilentGear.log.warn("Don't know salvage head part for vanilla gear '{}'", stack);
        return null;
    }
}
