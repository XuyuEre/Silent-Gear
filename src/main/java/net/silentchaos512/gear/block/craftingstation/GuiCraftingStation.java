package net.silentchaos512.gear.block.craftingstation;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gear.api.item.ICoreItem;
import net.silentchaos512.gear.client.gui.GuiItemParts;
import net.silentchaos512.gear.client.util.TooltipFlagTC;
import net.silentchaos512.gear.init.ModBlocks;
import net.silentchaos512.gear.item.ToolHead;
import net.silentchaos512.lib.client.key.KeyTrackerSL;
import net.silentchaos512.lib.gui.TexturedButton;
import net.silentchaos512.lib.util.StringUtil;

import java.io.IOException;
import java.util.List;

public class GuiCraftingStation extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SilentGear.MOD_ID, "textures/gui/crafting_station.png");

    private final TileCraftingStation tile;
    private final ContainerCraftingStation container;

    private TexturedButton buttonShowAllParts;

    public GuiCraftingStation(TileCraftingStation tile, ContainerCraftingStation inventorySlotsIn) {
        super(inventorySlotsIn);
        this.tile = tile;
        this.container = inventorySlotsIn;

        this.xSize = 256;
        // this.ySize = 256;
    }

    @Override
    public void initGui() {
        super.initGui();

        buttonShowAllParts = new TexturedButton(TEXTURE, 100, guiLeft + 149, guiTop + 5, 236, 166, 20, 18,
                ImmutableList.of("Show Available Parts", TextFormatting.GRAY + "List can be sorted by stats"));
        buttonList.add(buttonShowAllParts);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button == buttonShowAllParts) {
            mc.displayGuiScreen(new GuiItemParts());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        if (buttonShowAllParts.isMouseOver()) {
            buttonShowAllParts.drawHover(mouseX, mouseY);
        }
    }

    private TooltipFlagTC getTooltipFlag(ItemStack stack) {
        boolean ctrl = stack.getItem() instanceof ICoreItem || stack.getItem() instanceof ToolHead;
        boolean alt = KeyTrackerSL.isAltDown();
        boolean shift = KeyTrackerSL.isShiftDown();
        return new TooltipFlagTC(ctrl, alt, shift, false);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        // Draw adjacent inventories
//        NonNullList<Pair<ItemStack, IInventory>> list = this.tile.getAdjacentInventories();
//        int itemX = 5;
//        for (Pair<ItemStack, IInventory> pair : list) {
//            AssetUtil.renderStackToGui(pair.getLeft(), itemX, -18, 1f);
//            itemX += 20;
//        }

        this.fontRenderer.drawString(SilentGear.i18n.subText(ModBlocks.craftingStation, "crafting"),
                8, 6, 0x404040);
        this.fontRenderer.drawString(SilentGear.i18n.subText(ModBlocks.craftingStation, "parts"),
                80, 6, 0x404040); // TODO: Change text color when able to use?
        this.fontRenderer.drawString(SilentGear.i18n.subText(ModBlocks.craftingStation, "storage"),
                -55, 19, 0x404040);
        this.fontRenderer.drawString(I18n.format("container.inventory"),
                8, this.ySize - 96 + 2, 0x404040);

        // Version number (remove in full release)
        String versionNumString = "Version: " + SilentGear.VERSION + "-" + SilentGear.BUILD_NUM + (SilentGear.instance.isDevBuild() ? " (dev)" : "");
        int versionNumStringWidth = fontRenderer.getStringWidth(versionNumString);
        float versionNumScale = 0.65f;
        StringUtil.renderScaledAsciiString(fontRenderer, versionNumString,
                (int) (xSize - versionNumStringWidth * versionNumScale) - 1,
                ySize + 1,
                0xCCCCCC, false, versionNumScale);

        ItemStack craftResult = this.container.craftResult.getStackInSlot(0);
        drawSlimeFace(craftResult);

        // Debug
//        if (SilentGear.instance.isDevBuild()) {
//            Slot slot = getSlotUnderMouse();
//            if (slot != null) {
//                fontRenderer.drawString("slot=" + slot.getSlotIndex(), 130, 6, Color.VALUE_WHITE);
//            }
//        }

        if (!craftResult.isEmpty()) {
            // Draw crafting result tooltip
            FontRenderer font = this.mc.fontRenderer;
            List<String> tooltip = craftResult.getTooltip(this.mc.player, getTooltipFlag(craftResult));

            int maxWidth = 0;
            for (String line : tooltip) {
                maxWidth = Math.max(maxWidth, font.getStringWidth(line));
            }
            // SilentGear.log.debug(maxWidth);

            float scale = 75f / maxWidth;
            int xPos = (int) ((this.xSize - 82) / scale);
            int yPos = (int) ((this.ySize - 160) / scale);
            // SilentGear.log.debug(xPos, yPos);

            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, 1);

            int step = (int) (scale * 10);
            for (String line : tooltip) {
                font.drawStringWithShadow(line, xPos, yPos, 0xFFFFFF);
                yPos += Math.round(step / scale);
            }

            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        // Main window
        GlStateManager.color(1, 1, 1, 1);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);

        // Internal/external inventory slots
        final int rowCount = TileCraftingStation.SIDE_INVENTORY_SIZE / 3;
        final int rowWidth = 62;
        final int totalHeight = 44 + 18 * (rowCount - 2);
        int x = xPos - 61;
        int y = yPos + (this.ySize - totalHeight) / 2;
        for (int i = 0; i < rowCount; ++i) {
            int height = i == 0 || i == rowCount - 1 ? 22 : 18;
            int ty = 194 + (i == 0 ? 0 : i == rowCount - 1 ? 40 : 22);
            this.drawTexturedModalRect(x, y, 0, ty, rowWidth, height);
            y += height;
        }

        this.drawTexturedModalRect(x, y - totalHeight - 12, 0, 177, rowWidth, 15);

        // Tooltip background
        x = xPos + 171;
        y = yPos + 4;
        drawRect(x, y, x + 80, y + 158, 0xCF000000);
    }

    private void drawSlimeFace(ItemStack craftResult) {
        mc.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.color(1, 1, 1, 1);
        final int textureY = 245;
        int textureX;
        if (craftResult.isEmpty()) {
            if (this.container.craftMatrix.isEmpty()) textureX = 223; // :|
            else textureX = 234; // :\
        } else {
            ResourceLocation name = craftResult.getItem().getRegistryName();
            if (name == null || "tconstruct".equals(name.getNamespace())) textureX = 245; // :(
            else textureX = 212; // :)
        }

        drawTexturedModalRect(this.xSize - 18, this.ySize - 17, textureX, textureY, 11, 11);
        mc.getTextureManager().bindTexture(ICONS);
    }
}
