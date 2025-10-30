package com.forestotzka.yurufu.slabee.model;

import com.forestotzka.yurufu.slabee.block.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
//import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
//import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBlockModels;
import net.fabricmc.fabric.api.renderer.v1.render.FabricBlockModelRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.model.*;
//import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.awt.image.renderable.RenderContext;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class TranslucentBlockModel implements UnbakedModel, FabricBlockModels, FabricBlockModelRenderer {
    private final Identifier id;
    private final Block block;
    private BakedGeometry bakedModel;
    protected FabricBlockModels nullBakedModel;

    public TranslucentBlockModel(Block block) {
        this.block = block;

        Identifier id = Registries.BLOCK.getId(this.block);

        this.id = Identifier.of(id.getNamespace(), "block/" + id.getPath());
    }
    //@Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return List.of();
    }

    //@Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    //@Override
    public boolean hasDepth() {
        return false;
    }

    //@Override
    public boolean isSideLit() {
        return false;
    }

    //@Override
    public boolean isBuiltin() {
        return false;
    }

    //@Override
    public Sprite getParticleSprite(BlockState state, BlockRenderView view, BlockPos pos) {
        if (bakedModel != null) {
            return getParticleSprite(state, view, pos);
        } else {
            return nullBakedModel.getModelParticleSprite(state, view, pos);
        }
    }

    //@Override
    /*public ModelTransformation getTransformation() {
        //return bakedModel != null; return ModelTransformation.NONE;
    }
     */

    //@Override
   /* public ModelBaker getOverrides() {
        return bakedModel != null ? bakedModel : Model();
    }
    */

    public Collection<Identifier> getModelDependencies() {
        return List.of();
    }

    //@Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

    }

    //@Override
    public boolean isVanillaAdapter() {
        return false;
    }

    //@Override
    public @Nullable FabricBlockModels bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer) {
        UnbakedModel unbakedModel = (UnbakedModel) baker.getModel(this.id).bakeGeometry((ModelTextures) baker, (Baker) textureGetter, rotationContainer);
        this.bakedModel = unbakedModel.geometry().bake((ModelTextures) baker, (Baker) textureGetter, rotationContainer, (SimpleModel) baker);

        return this;
    }

    //@Override
    public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {
        /*renderContext.pushTransform(quad -> {
            Direction face = quad.cullFace();

            return face != null && !shouldCull(face, blockRenderView, blockPos);
        });*/
        bakedModel.getAllQuads();
        renderContext.getTransform();
    }

    private boolean shouldCull(Direction face, BlockRenderView world, BlockPos pos) {
        BlockPos otherPos;
        BlockState otherState;
        Block otherBlock;

        if (face == Direction.UP) {
            otherPos = pos.up();
            otherState = world.getBlockState(otherPos);
            otherBlock = otherState.getBlock();
            if (otherBlock instanceof SlabBlock) {
                return block == ModBlockMap.slabToOriginal(otherBlock) && otherState.get(SlabBlock.TYPE) == SlabType.BOTTOM;
            } else if (otherState.isOf(ModBlocks.DOUBLE_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleSlabBlockEntity entity) {
                return block == ModBlockMap.slabToOriginal(entity.getNegativeSlabState().getBlock());
            } else if (otherState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleVerticalSlabBlockEntity entity) {
                Block b = ModBlockMap.slabToVerticalSlab(block);
                return b == entity.getPositiveSlabState().getBlock() && b == entity.getNegativeSlabState().getBlock();
            }
        } else if (face == Direction.DOWN) {
            otherPos = pos.down();
            otherState = world.getBlockState(otherPos);
            otherBlock = otherState.getBlock();
            if (otherBlock instanceof SlabBlock) {
                return block == ModBlockMap.slabToOriginal(otherBlock) && otherState.get(SlabBlock.TYPE) == SlabType.TOP;
            } else if (otherState.isOf(ModBlocks.DOUBLE_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleSlabBlockEntity entity) {
                return block == ModBlockMap.slabToOriginal(entity.getPositiveSlabState().getBlock());
            } else if (otherState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleVerticalSlabBlockEntity entity) {
                Block b = ModBlockMap.slabToVerticalSlab(block);
                return b == entity.getPositiveSlabState().getBlock() && b == entity.getNegativeSlabState().getBlock();
            }
        } else {
            if (face == Direction.EAST) {
                otherPos = pos.east();
            } else if (face == Direction.SOUTH) {
                otherPos = pos.south();
            } else if (face == Direction.WEST) {
                otherPos = pos.west();
            } else {
                otherPos = pos.north();
            }
            otherState = world.getBlockState(otherPos);
            otherBlock = otherState.getBlock();

            if (otherState.isOf(ModBlocks.DOUBLE_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleSlabBlockEntity entity) {
                Block b = ModBlockMap.originalToSlab(block);
                return b == entity.getPositiveSlabState().getBlock() && b == entity.getNegativeSlabState().getBlock();
            } else if (otherBlock instanceof VerticalSlabBlock) {
                return block == ModBlockMap.verticalSlabToOriginal(otherBlock) && otherState.get(VerticalSlabBlock.FACING) == face.getOpposite();
            } else if (otherState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleVerticalSlabBlockEntity entity) {
                if (entity.isX()) {
                    if (face == Direction.EAST) {
                        return block == ModBlockMap.verticalSlabToOriginal(entity.getNegativeSlabState().getBlock());
                    } else if (face == Direction.WEST) {
                        return block == ModBlockMap.verticalSlabToOriginal(entity.getPositiveSlabState().getBlock());
                    } else {
                        Block b = ModBlockMap.originalToVerticalSlab(block);
                        return b == entity.getPositiveSlabState().getBlock() && b == entity.getNegativeSlabState().getBlock();
                    }
                } else {
                    if (face == Direction.SOUTH) {
                        return block == ModBlockMap.verticalSlabToOriginal(entity.getNegativeSlabState().getBlock());
                    } else if (face == Direction.NORTH) {
                        return block == ModBlockMap.verticalSlabToOriginal(entity.getPositiveSlabState().getBlock());
                    } else {
                        Block b = ModBlockMap.originalToVerticalSlab(block);
                        return b == entity.getPositiveSlabState().getBlock() && b == entity.getNegativeSlabState().getBlock();
                    }
                }
            }
        }

        return ModBlockMap.slabToOriginal(block) == otherBlock;
    }
}
