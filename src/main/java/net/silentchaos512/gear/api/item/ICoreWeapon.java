package net.silentchaos512.gear.api.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gear.api.stats.CommonItemStats;
import net.silentchaos512.gear.api.stats.ItemStat;

import java.util.Set;

public interface ICoreWeapon extends ICoreTool {
    Set<ItemStat> RELEVANT_STATS = ImmutableSet.of(
            CommonItemStats.MELEE_DAMAGE,
            CommonItemStats.MAGIC_DAMAGE,
            CommonItemStats.ATTACK_SPEED,
            CommonItemStats.DURABILITY,
            CommonItemStats.ENCHANTABILITY,
            CommonItemStats.RARITY
    );

    @Override
    default Set<ItemStat> getRelevantStats(ItemStack stack) {
        return RELEVANT_STATS;
    }

    @Override
    default int getDamageOnHitEntity(ItemStack gear, EntityLivingBase target, EntityLivingBase attacker) {
        return 1;
    }
}
