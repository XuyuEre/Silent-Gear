package net.silentchaos512.gear.client.models;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.silentchaos512.gear.SilentGear;
import net.silentchaos512.gear.api.item.ICoreArmor;
import net.silentchaos512.gear.api.parts.*;
import net.silentchaos512.gear.client.ColorHandlers;
import net.silentchaos512.gear.client.util.GearClientHelper;
import net.silentchaos512.gear.init.ModItems;
import net.silentchaos512.gear.util.GearData;
import net.silentchaos512.gear.util.GearHelper;
import net.silentchaos512.lib.util.StackHelper;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class ArmorItemModel implements IModel {

    public static final IModel MODEL = new ArmorItemModel();

    @Nullable
    private final ResourceLocation textureMain;

    public ArmorItemModel() {
        this.textureMain = null;
    }

    public ArmorItemModel(@Nullable ResourceLocation textureMain) {
        this.textureMain = textureMain;
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures) {
        ResourceLocation main = null;

        if (textures.containsKey("main"))
            main = new ResourceLocation(textures.get("main"));

        return new ArmorItemModel(main);
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData) {
        ResourceLocation main = null;

        if (customData.containsKey("main"))
            main = new ResourceLocation(customData.get("main"));

        return new ArmorItemModel(main);
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.of();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        for (String armorClass : ModItems.armorClasses.keySet()) {
            for (PartMain part : PartRegistry.getMains()) {
                ItemPartData partData = ItemPartData.instance(part);
                // Basic texture
                ResourceLocation textureMain = partData.getTexture(ItemStack.EMPTY, armorClass, PartPositions.ARMOR, 0);
                if (textureMain != null)
                    builder.add(textureMain);

                // Broken texture
                ResourceLocation textureBroken = partData.getBrokenTexture(ItemStack.EMPTY, armorClass, PartPositions.ARMOR);
                if (textureBroken != null)
                    builder.add(textureBroken);
            }
        }
        return builder.build();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);

        TRSRTransformation transform = TRSRTransformation.identity();

        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        ImmutableList.Builder<ResourceLocation> texBuilder = ImmutableList.builder();
        if (this.textureMain != null)
            texBuilder.add(this.textureMain);

        ImmutableList<ResourceLocation> textures = texBuilder.build();
        int layerCount = textures.size();
        IBakedModel model = (new ItemLayerModel(textures)).bake(state, format, bakedTextureGetter);
        builder.addAll(model.getQuads(null, null, 0));

        return new ArmorItemModel.Baked(this, createQuadsMap(model, layerCount), format, Maps.immutableEnumMap(transformMap), new HashMap<>());
    }

    private ImmutableList<ImmutableList<BakedQuad>> createQuadsMap(IBakedModel model, int layerCount) {
        List<ImmutableList.Builder<BakedQuad>> list = new ArrayList<>();
        for (int i = 0; i < layerCount; ++i)
            list.add(ImmutableList.builder());

        for (BakedQuad quad : model.getQuads(null, null, 0))
            list.get(quad.getTintIndex()).add(quad);

        ImmutableList.Builder<ImmutableList<BakedQuad>> builder = ImmutableList.builder();
        for (ImmutableList.Builder<BakedQuad> b : list)
            builder.add(b.build());

        return builder.build();
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    public static final class Loader implements ICustomModelLoader {

        public static Loader INSTANCE = new Loader();

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {
        }

        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            boolean matchesPath = ModItems.armorClasses.keySet().stream().anyMatch(s -> modelLocation.getPath().equals(s));
            return modelLocation.getNamespace().equals(SilentGear.MOD_ID) && matchesPath;
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) {
            return MODEL;
        }
    }

    private static final class OverrideHandler extends ItemOverrideList {

        static final OverrideHandler INSTANCE = new OverrideHandler();

        OverrideHandler() {
            super(ImmutableList.of());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
            if (!(stack.getItem() instanceof ICoreArmor))
                return originalModel;

            ArmorItemModel.Baked model = (ArmorItemModel.Baked) originalModel;

            ICoreArmor itemArmor = (ICoreArmor) stack.getItem();

            String key = GearData.getCachedModelKey(stack, 0);
            StackHelper.getTagCompound(stack, true).setString("debug_modelkey", key);

            if (!GearClientHelper.modelCache.containsKey(key)) {
                ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

                processTexture(stack, itemArmor.getGearClass(), PartPositions.ARMOR, itemArmor.getPrimaryPart(stack), GearHelper.isBroken(stack), builder);

                IModel parent = model.getParent().retexture(builder.build());
                Function<ResourceLocation, TextureAtlasSprite> textureGetter = location ->
                        Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                IBakedModel bakedModel = parent.bake(new SimpleModelState(model.transforms), model.getVertexFormat(), textureGetter);
                GearClientHelper.modelCache.put(key, bakedModel);
                return bakedModel;
            }

            // Color cache
            ColorHandlers.gearColorCache.put(key, new Integer[] {itemArmor.getPrimaryPart(stack).getColor(stack, 0)});

            return GearClientHelper.modelCache.get(key);
        }

        private void processTexture(ItemStack stack, String toolClass, IPartPosition position, ItemPartData part, boolean isBroken, ImmutableMap.Builder<String, String> builder) {
            if (part != null) {
                ResourceLocation texture;
                if (isBroken)
                    texture = part.getBrokenTexture(stack, toolClass, position);
                else
                    texture = part.getTexture(stack, toolClass, position, 0);

                if (texture != null)
                    builder.put(position.getModelIndex(), texture.toString());
            }
        }
    }

    public static final class Baked extends AbstractToolModel {
        public static Baked instance;

        public Baked(IModel parent, ImmutableList<ImmutableList<BakedQuad>> immutableList, VertexFormat format, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms, Map<String, IBakedModel> cache) {
            super(parent, immutableList, format, transforms, cache);
            instance = this;
        }

        @Override
        public ItemOverrideList getOverrides() {
            return OverrideHandler.INSTANCE;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return true;
        }
    }
}
