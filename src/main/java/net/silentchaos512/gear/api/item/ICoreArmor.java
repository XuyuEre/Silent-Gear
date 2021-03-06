package net.silentchaos512.gear.api.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gear.api.parts.ItemPartData;
import net.silentchaos512.gear.api.stats.CommonItemStats;
import net.silentchaos512.gear.api.stats.ItemStat;

import java.util.Set;

public interface ICoreArmor extends ICoreItem {
    Set<ItemStat> RELEVANT_STATS = ImmutableSet.of(
            CommonItemStats.ARMOR,
            CommonItemStats.MAGIC_ARMOR,
            CommonItemStats.ARMOR_TOUGHNESS,
            CommonItemStats.DURABILITY,
            CommonItemStats.ENCHANTABILITY,
            CommonItemStats.RARITY
    );

    @Override
    default Set<ItemStat> getRelevantStats(ItemStack stack) {
        return RELEVANT_STATS;
    }

    @Override
    default ItemPartData[] getRenderParts(ItemStack stack) {
        return new ItemPartData[] {getPrimaryPart(stack)};
    }
}
