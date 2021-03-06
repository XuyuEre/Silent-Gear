package net.silentchaos512.gear.api.parts;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gear.api.item.ICoreItem;
import net.silentchaos512.gear.api.item.ICoreTool;

import javax.annotation.Nonnull;
import java.util.List;

public final class PartGrip extends ItemPart implements IUpgradePart {
    public PartGrip(ResourceLocation name, PartOrigins origin) {
        super(name, origin);
    }

    @Override
    public PartType getType() {
        return PartType.GRIP;
    }

    @Override
    public void addInformation(ItemPartData part, ItemStack gear, World world, @Nonnull List<String> tooltip, boolean advanced) {
        // Nothing
    }

    @Override
    public String getTypeName() {
        return "grip";
    }

    @Override
    public boolean isValidFor(ICoreItem gearItem) {
        return gearItem instanceof ICoreTool;
    }

    @Override
    public boolean replacesExisting() {
        return true;
    }

    @Override
    public IPartPosition getPartPosition() {
        return PartPositions.GRIP;
    }
}
