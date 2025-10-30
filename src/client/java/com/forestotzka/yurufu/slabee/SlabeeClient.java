package com.forestotzka.yurufu.slabee;

import com.forestotzka.yurufu.slabee.block.ModBlockEntities;
import com.forestotzka.yurufu.slabee.block.ModBlocks;
import com.forestotzka.yurufu.slabee.block.DoubleSlabBlockEntityRenderer;
import com.forestotzka.yurufu.slabee.block.DoubleVerticalSlabBlockEntityRenderer;
//import com.forestotzka.yurufu.slabee.handler.ModClientHandlers;
import com.forestotzka.yurufu.slabee.listener.ModClientListeners;
import com.forestotzka.yurufu.slabee.model.ModModelLoaders;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
//import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.biome.GrassColors;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class SlabeeClient implements ClientModInitializer {
    private static final List<Block> LEAF_SLABS = Arrays.asList(
            ModBlocks.OAK_LEAF_SLAB,
            ModBlocks.SPRUCE_LEAF_SLAB,
            ModBlocks.BIRCH_LEAF_SLAB,
            ModBlocks.JUNGLE_LEAF_SLAB,
            ModBlocks.ACACIA_LEAF_SLAB,
            ModBlocks.DARK_OAK_LEAF_SLAB,
            ModBlocks.MANGROVE_LEAF_SLAB,
            ModBlocks.CHERRY_LEAF_SLAB,
            ModBlocks.AZALEA_LEAF_SLAB,
            ModBlocks.FLOWERING_AZALEA_LEAF_SLAB,

            ModBlocks.OAK_LEAF_VERTICAL_SLAB,
            ModBlocks.SPRUCE_LEAF_VERTICAL_SLAB,
            ModBlocks.BIRCH_LEAF_VERTICAL_SLAB,
            ModBlocks.JUNGLE_LEAF_VERTICAL_SLAB,
            ModBlocks.ACACIA_LEAF_VERTICAL_SLAB,
            ModBlocks.DARK_OAK_LEAF_VERTICAL_SLAB,
            ModBlocks.MANGROVE_LEAF_VERTICAL_SLAB,
            ModBlocks.CHERRY_LEAF_VERTICAL_SLAB,
            ModBlocks.AZALEA_LEAF_VERTICAL_SLAB,
            ModBlocks.FLOWERING_AZALEA_LEAF_VERTICAL_SLAB
            );
    private static final List<Block> COLORED_GLASS_SLABS = Arrays.asList(
            ModBlocks.TINTED_GLASS_SLAB,
            ModBlocks.WHITE_STAINED_GLASS_SLAB,
            ModBlocks.LIGHT_GRAY_STAINED_GLASS_SLAB,
            ModBlocks.GRAY_STAINED_GLASS_SLAB,
            ModBlocks.BLACK_STAINED_GLASS_SLAB,
            ModBlocks.BROWN_STAINED_GLASS_SLAB,
            ModBlocks.RED_STAINED_GLASS_SLAB,
            ModBlocks.ORANGE_STAINED_GLASS_SLAB,
            ModBlocks.YELLOW_STAINED_GLASS_SLAB,
            ModBlocks.LIME_STAINED_GLASS_SLAB,
            ModBlocks.GREEN_STAINED_GLASS_SLAB,
            ModBlocks.CYAN_STAINED_GLASS_SLAB,
            ModBlocks.LIGHT_BLUE_STAINED_GLASS_SLAB,
            ModBlocks.BLUE_STAINED_GLASS_SLAB,
            ModBlocks.PURPLE_STAINED_GLASS_SLAB,
            ModBlocks.MAGENTA_STAINED_GLASS_SLAB,
            ModBlocks.PINK_STAINED_GLASS_SLAB,

            ModBlocks.TINTED_GLASS_VERTICAL_SLAB,
            ModBlocks.WHITE_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.LIGHT_GRAY_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.GRAY_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.BLACK_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.BROWN_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.RED_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.ORANGE_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.YELLOW_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.LIME_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.GREEN_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.CYAN_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.LIGHT_BLUE_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.BLUE_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.PURPLE_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.MAGENTA_STAINED_GLASS_VERTICAL_SLAB,
            ModBlocks.PINK_STAINED_GLASS_VERTICAL_SLAB
    );

    @Override
    public void onInitializeClient() {
        ModClientListeners.register();
        //ModClientHandlers.register();
        ModModelLoaders.register();

        //WILL NEED TO BE FIXED IN THE FUTURE
        /*LEAF_SLABS.forEach(block -> {
            ColorProviderRegistry.BLOCK.register(this::getLeafSlabColor, block);

            ColorProviderRegistry.BLOCK.register(((stack, tintIndex) -> {
                if (stack == ModBlocks.MANGROVE_LEAF_SLAB.asItem() || stack == ModBlocks.MANGROVE_LEAF_VERTICAL_SLAB.asItem()) {
                    return FoliageColors.MANGROVE;
                } else if (
                        stack == (ModBlocks.OAK_LEAF_SLAB.asItem()) ||stack == (ModBlocks.SPRUCE_LEAF_SLAB.asItem()) ||stack == (ModBlocks.BIRCH_LEAF_SLAB.asItem()) || stack == (ModBlocks.JUNGLE_LEAF_SLAB.asItem()) || stack == (ModBlocks.ACACIA_LEAF_SLAB.asItem()) || stack == (ModBlocks.DARK_OAK_LEAF_SLAB.asItem()) ||
                        stack == ModBlocks.OAK_LEAF_VERTICAL_SLAB.asItem() || stack == ModBlocks.SPRUCE_LEAF_VERTICAL_SLAB.asItem() ||stack == (ModBlocks.BIRCH_LEAF_VERTICAL_SLAB.asItem()) ||stack == (ModBlocks.JUNGLE_LEAF_VERTICAL_SLAB.asItem()) || stack == (ModBlocks.ACACIA_LEAF_VERTICAL_SLAB.asItem()) || stack == (ModBlocks.DARK_OAK_LEAF_VERTICAL_SLAB.asItem())
                ) {
                    BlockState state = Block.getBlockFromItem(Item.byRawId(1)).getDefaultState(); //Probably not going to work
                    return getLeafSlabColor(state, null, null, tintIndex);
                } else {
                    return 0xFFFFFF;
                }
            }), block.asItem());

            BlockRenderLayerMap.putBlock(block, BlockRenderLayer.CUTOUT);
        });

         */

      /*  BlockRenderLayerMap.putBlock(ModBlocks.GLASS_SLAB, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.GLASS_VERTICAL_SLAB, BlockRenderLayer.CUTOUT);
        COLORED_GLASS_SLABS.forEach(block -> BlockRenderLayerMap.putBlock(block, BlockRenderLayer.CUTOUT));

        ColorProviderRegistry.BLOCK.register(this::getGrassSlabColor, ModBlocks.GRASS_SLAB, ModBlocks.GRASS_VERTICAL_SLAB);
        ColorProviderRegistry.BLOCK.register((stack, world, pos, tintIndex) -> {
            BlockState state = stack.getBlock().getDefaultState();
            return getGrassSlabColor(state, null, null, 1);
        }, ModBlocks.GRASS_SLAB, ModBlocks.GRASS_VERTICAL_SLAB);

        BlockRenderLayerMap.putBlock(ModBlocks.GRASS_SLAB, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.GRASS_VERTICAL_SLAB, BlockRenderLayer.CUTOUT);

        BlockRenderLayerMap.putBlock(ModBlocks.MANGROVE_ROOT_SLAB, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.MANGROVE_ROOT_VERTICAL_SLAB, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.DIRT_PATH_SLAB, BlockRenderLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.DIRT_PATH_VERTICAL_SLAB, BlockRenderLayer.CUTOUT);

        BlockRenderLayerMap.putBlock(ModBlocks.DOUBLE_SLAB_BLOCK, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK, BlockRenderLayer.TRANSLUCENT);

        BlockEntityRendererFactories.register(ModBlockEntities.DOUBLE_SLAB_BLOCK_ENTITY, DoubleSlabBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.DOUBLE_VERTICAL_SLAB_BLOCK_ENTITY, DoubleVerticalSlabBlockEntityRenderer::new);
    }

    private int getLeafSlabColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        if (state.isOf(ModBlocks.SPRUCE_LEAF_SLAB) || state.isOf(ModBlocks.SPRUCE_LEAF_VERTICAL_SLAB)) {
            return FoliageColors.SPRUCE;
        } else if (state.isOf(ModBlocks.BIRCH_LEAF_SLAB) || state.isOf(ModBlocks.BIRCH_LEAF_VERTICAL_SLAB)) {
            return FoliageColors.BIRCH;
        } else if (world == null || pos == null) {
            return FoliageColors.DEFAULT;
        } else if (
                state.isOf(ModBlocks.OAK_LEAF_SLAB) || state.isOf(ModBlocks.JUNGLE_LEAF_SLAB) || state.isOf(ModBlocks.ACACIA_LEAF_SLAB) || state.isOf(ModBlocks.DARK_OAK_LEAF_SLAB) || state.isOf(ModBlocks.MANGROVE_LEAF_SLAB) ||
                state.isOf(ModBlocks.OAK_LEAF_VERTICAL_SLAB) || state.isOf(ModBlocks.JUNGLE_LEAF_VERTICAL_SLAB) || state.isOf(ModBlocks.ACACIA_LEAF_VERTICAL_SLAB) || state.isOf(ModBlocks.DARK_OAK_LEAF_VERTICAL_SLAB) || state.isOf(ModBlocks.MANGROVE_LEAF_VERTICAL_SLAB)
        ) {
            return BiomeColors.getFoliageColor(world, pos);
        } else {
            return 0xFFFFFF;
        }
    }

    private int getGrassSlabColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        if (world != null && pos != null) {
            return BiomeColors.getGrassColor(world, pos);
        } else {
            return GrassColors.getDefaultColor();
        }
        */
    }
}
