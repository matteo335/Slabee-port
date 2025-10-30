package com.forestotzka.yurufu.slabee.model;

import com.forestotzka.yurufu.slabee.block.*;
import com.forestotzka.yurufu.slabee.block.enums.VerticalSlabAxis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
//import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
//import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBlockModelPart;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBlockModels;
import net.fabricmc.fabric.api.renderer.v1.render.FabricBlockModelRenderer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.render.model.*;
//import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.registry.Registries;
import net.minecraft.resource.OverlayResourcePack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.awt.geom.AffineTransform;
import java.awt.image.renderable.RenderContext;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Environment(EnvType.CLIENT)
public class DoubleVerticalSlabBlockModel implements UnbakedModel, FabricBlockModels, FabricBlockModelRenderer {
    private final Identifier positiveId;
    private final Identifier negativeId;
    private final Block positiveSlab;
    private final Block negativeSlab;
    private final boolean isX;
    private FabricBlockModels positiveBakedModel;
    private FabricBlockModels negativeBakedModel;
    private FabricBlockModels nullBakedModel;

    private final VerticalModelRotation Y0 = new VerticalModelRotation(ModelRotation.X0_Y0.getRotation(), true);
    private final VerticalModelRotation Y90 = new VerticalModelRotation(ModelRotation.X0_Y90.getRotation(), true);
    private final VerticalModelRotation Y180 = new VerticalModelRotation(ModelRotation.X0_Y180.getRotation(), true);
    private final VerticalModelRotation Y270 = new VerticalModelRotation(ModelRotation.X0_Y270.getRotation(), true);

    public DoubleVerticalSlabBlockModel(@Nullable Block positiveSlab, @Nullable Block negativeSlab, boolean isX) {
        this.positiveSlab = positiveSlab;
        this.negativeSlab = negativeSlab;
        this.isX = isX;

        if (this.positiveSlab != null) {
            Identifier positiveId = Registries.BLOCK.getId(positiveSlab);

            if (isX) {
                this.positiveId = Identifier.of(positiveId.getNamespace(), "block/" + positiveId.getPath() + "_x");
            } else {
                this.positiveId = Identifier.of(positiveId.getNamespace(), "block/" + positiveId.getPath() + "_z");
            }
        } else {
            this.positiveId = null;
        }

        if (this.negativeSlab != null) {
            Identifier negativeId = Registries.BLOCK.getId(negativeSlab);

            if (isX) {
                this.negativeId = Identifier.of(negativeId.getNamespace(), "block/" + negativeId.getPath() + "_x");
            } else {
                this.negativeId = Identifier.of(negativeId.getNamespace(), "block/" + negativeId.getPath() + "_z");
            }
        } else {
            this.negativeId = null;
        }
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
        if (positiveId != null) {
            return positiveBakedModel.getModelParticleSprite(state, view, pos);
        } else if (negativeId != null) {
            return negativeBakedModel.getModelParticleSprite(state, view, pos);
        } else {
            return nullBakedModel.getModelParticleSprite(state, view, pos);
        }
    }

    //@Override
    public ModelTransformation getTransformation() {
        return null;
    }

    //@Override
    public OverlayResourcePack getOverrides() {
        return null;
    }

    //@Override
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
        if (this.positiveId != null) {
            UnbakedModel positiveUnbakedModel = baker.getModel(this.positiveId).getModel();
            this.positiveBakedModel = (FabricBlockModels) positiveUnbakedModel.geometry();
        }

        if (this.negativeId != null) {
            UnbakedModel negativeUnbakedModel = (UnbakedModel) baker.getModel(this.negativeId).bakeGeometry((ModelTextures) textureGetter, baker, (ModelBakeSettings) AbstractBlock.Settings.create());
            this.negativeBakedModel = (FabricBlockModels) negativeUnbakedModel;
        } else if (this.positiveId == null) {
            this.nullBakedModel = (FabricBlockModels) baker.getModel(Identifier.of("minecraft:block/stone")).bakeGeometry((ModelTextures) baker, (Baker) textureGetter, rotationContainer);
        }

        return this;
    }

    public static Logger LOGGER = LogManager.getLogManager().getLogger("Slabee - Please report to Matteo35");

    //@Override
    public void emitBlockQuads(BlockRenderView blockRenderView, BlockState blockState, BlockPos blockPos, Supplier<Random> supplier, RenderContext renderContext) {
        if (this.positiveId != null) {
           // renderContext(quad -> {
                //Direction face = quad.cullFace();

                //return this.positiveSlab == null || face != null && !shouldCullPositive(face, blockRenderView, blockPos);
            LOGGER.info("DoubleVerticalSlabBlockModel emitBlockQuads this.positiveId isn't null");
            //});

            //positiveBakedModel;
            renderContext.preConcatenateTransform((AffineTransform) positiveBakedModel);
        }

        if (this.negativeId != null) {
            /*renderContext.pushTransform(quad -> {
                Direction face = quad.cullFace();

                return this.negativeSlab == null || face != null && !shouldCullNegative(face, blockRenderView, blockPos);
            });*/

            //negativeBakedModel.emitBlockQuads(blockRenderView, blockState, blockPos, supplier, renderContext);
            renderContext.getTransform();
        }
    }

    private boolean shouldCullPositive(Direction face, BlockRenderView world, BlockPos pos) {
        BlockPos otherPos;
        BlockState otherState;
        Block otherBlock;

        if ((isX && face == Direction.WEST) || (!isX && face == Direction.NORTH)) {
            return positiveSlab == negativeSlab;
        } else if (face == Direction.EAST) {
            otherPos = pos.east();
            otherState = world.getBlockState(otherPos);
            otherBlock = otherState.getBlock();

            if (otherBlock instanceof VerticalSlabBlock && positiveSlab == otherBlock) {
                Direction d = otherState.get(VerticalSlabBlock.FACING);
                return d == Direction.WEST || (!isX && d == Direction.SOUTH);
            } else if (otherState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleVerticalSlabBlockEntity entity) {
                Block bp = entity.getPositiveSlabState().getBlock();
                Block bn = entity.getNegativeSlabState().getBlock();

                if (otherState.get(DoubleVerticalSlabBlock.AXIS) == VerticalSlabAxis.X) {
                    return positiveSlab == bn;
                } else {
                    if (isX) {
                        return positiveSlab == bp && positiveSlab == bn;
                    } else {
                        return positiveSlab == bp;
                    }
                }
            }
        } else if (face == Direction.SOUTH) {
            otherPos = pos.south();
            otherState = world.getBlockState(otherPos);
            otherBlock = otherState.getBlock();

            if (otherBlock instanceof VerticalSlabBlock && positiveSlab == otherBlock) {
                Direction d = otherState.get(VerticalSlabBlock.FACING);
                return d == Direction.NORTH || (isX && d == Direction.EAST);
            } else if (otherState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleVerticalSlabBlockEntity entity) {
                Block bp = entity.getPositiveSlabState().getBlock();
                Block bn = entity.getNegativeSlabState().getBlock();

                if (otherState.get(DoubleVerticalSlabBlock.AXIS) == VerticalSlabAxis.Z) {
                    return positiveSlab == bn;
                } else {
                    if (isX) {
                        return positiveSlab == bp;
                    } else {
                        return positiveSlab == bp && positiveSlab == bn;
                    }
                }
            }
        } else if (face == Direction.WEST) {
            otherPos = pos.west();
            otherState = world.getBlockState(otherPos);
            otherBlock = otherState.getBlock();

            if (otherBlock instanceof VerticalSlabBlock && positiveSlab == otherBlock) {
                Direction d = otherState.get(VerticalSlabBlock.FACING);
                return d == Direction.EAST || d == Direction.SOUTH;
            } else if (otherState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleVerticalSlabBlockEntity entity) {
                return positiveSlab == entity.getPositiveSlabState().getBlock();
            }
        } else if (face == Direction.NORTH) {
            otherPos = pos.north();
            otherState = world.getBlockState(otherPos);
            otherBlock = otherState.getBlock();

            if (otherBlock instanceof VerticalSlabBlock && positiveSlab == otherBlock) {
                Direction d = otherState.get(VerticalSlabBlock.FACING);
                return d == Direction.EAST || d == Direction.SOUTH;
            } else if (otherState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleVerticalSlabBlockEntity entity) {
                return positiveSlab == entity.getPositiveSlabState().getBlock();
            }
        } else {
            if (face == Direction.UP) {
                otherPos = pos.up();
            } else {
                otherPos = pos.down();
            }
            otherState = world.getBlockState(otherPos);
            otherBlock = otherState.getBlock();

            if (otherBlock instanceof VerticalSlabBlock && positiveSlab == otherBlock) {
                Direction d = otherState.get(VerticalSlabBlock.FACING);
                return (isX && d == Direction.EAST) || (!isX && d == Direction.SOUTH);
            } else if (otherState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleVerticalSlabBlockEntity entity) {
                return positiveSlab == entity.getPositiveSlabState().getBlock();
            } else if (otherBlock instanceof SlabBlock && positiveSlab == ModBlockMap.slabToVerticalSlab(otherBlock)) {
                if (face == Direction.UP) {
                    return otherState.get(SlabBlock.TYPE) == SlabType.BOTTOM;
                } else {
                    return otherState.get(SlabBlock.TYPE) == SlabType.TOP;
                }
            }
        }

        if (otherState.isOf(ModBlocks.DOUBLE_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleSlabBlockEntity entity) {
            Block b = ModBlockMap.verticalSlabToSlab(positiveSlab);
            return b == entity.getPositiveSlabState().getBlock() && b == entity.getNegativeSlabState().getBlock();
        }

        return ModBlockMap.verticalSlabToOriginal(positiveSlab) == otherBlock;
    }

    private boolean shouldCullNegative(Direction face, BlockRenderView world, BlockPos pos) {
        BlockPos otherPos;
        BlockState otherState;
        Block otherBlock;

        if ((isX && face == Direction.EAST) || (!isX && face == Direction.SOUTH)) {
            return positiveSlab == negativeSlab;
        } else if (face == Direction.EAST) {
            otherPos = pos.east();
            otherState = world.getBlockState(otherPos);
            otherBlock = otherState.getBlock();

            if (otherBlock instanceof VerticalSlabBlock && negativeSlab == otherBlock) {
                Direction d = otherState.get(VerticalSlabBlock.FACING);
                return d == Direction.WEST || d == Direction.NORTH;
            } else if (otherState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleVerticalSlabBlockEntity entity) {
                return negativeSlab == entity.getNegativeSlabState().getBlock();
            }
        } else if (face == Direction.SOUTH) {
            otherPos = pos.south();
            otherState = world.getBlockState(otherPos);
            otherBlock = otherState.getBlock();

            if (otherBlock instanceof VerticalSlabBlock && negativeSlab == otherBlock) {
                Direction d = otherState.get(VerticalSlabBlock.FACING);
                return d == Direction.NORTH || d == Direction.WEST;
            } else if (otherState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleVerticalSlabBlockEntity entity) {
                return negativeSlab == entity.getNegativeSlabState().getBlock();
            }
        } else if (face == Direction.WEST) {
            otherPos = pos.west();
            otherState = world.getBlockState(otherPos);
            otherBlock = otherState.getBlock();

            if (otherBlock instanceof VerticalSlabBlock && negativeSlab == otherBlock) {
                Direction d = otherState.get(VerticalSlabBlock.FACING);
                return d == Direction.EAST || (!isX && d == Direction.NORTH);
            } else if (otherState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleVerticalSlabBlockEntity entity) {
                Block bp = entity.getPositiveSlabState().getBlock();
                Block bn = entity.getNegativeSlabState().getBlock();

                if (otherState.get(DoubleVerticalSlabBlock.AXIS) == VerticalSlabAxis.X) {
                    return negativeSlab == bp;
                } else {
                    if (isX) {
                        return negativeSlab == bp && negativeSlab == bn;
                    } else {
                        return negativeSlab == bn;
                    }
                }
            }
        } else if (face == Direction.NORTH) {
            otherPos = pos.north();
            otherState = world.getBlockState(otherPos);
            otherBlock = otherState.getBlock();

            if (otherBlock instanceof VerticalSlabBlock && negativeSlab == otherBlock) {
                Direction d = otherState.get(VerticalSlabBlock.FACING);
                return d == Direction.SOUTH || (isX && d == Direction.WEST);
            } else if (otherState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleVerticalSlabBlockEntity entity) {
                Block bp = entity.getPositiveSlabState().getBlock();
                Block bn = entity.getNegativeSlabState().getBlock();

                if (otherState.get(DoubleVerticalSlabBlock.AXIS) == VerticalSlabAxis.Z) {
                    return negativeSlab == bp;
                } else {
                    if (isX) {
                        return negativeSlab == bn;
                    } else {
                        return negativeSlab == bp && negativeSlab == bn;
                    }
                }
            }
        } else {
            if (face == Direction.UP) {
                otherPos = pos.up();
            } else {
                otherPos = pos.down();
            }
            otherState = world.getBlockState(otherPos);
            otherBlock = otherState.getBlock();

            if (otherBlock instanceof VerticalSlabBlock && negativeSlab == otherBlock) {
                Direction d = otherState.get(VerticalSlabBlock.FACING);
                return (isX && d == Direction.WEST) || (!isX && d == Direction.NORTH);
            } else if (otherState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleVerticalSlabBlockEntity entity) {
                return negativeSlab == entity.getNegativeSlabState().getBlock();
            } else if (otherBlock instanceof SlabBlock && negativeSlab == ModBlockMap.slabToVerticalSlab(otherBlock)) {
                if (face == Direction.UP) {
                    return otherState.get(SlabBlock.TYPE) == SlabType.BOTTOM;
                } else {
                    return otherState.get(SlabBlock.TYPE) == SlabType.TOP;
                }
            }
        }

        if (otherState.isOf(ModBlocks.DOUBLE_SLAB_BLOCK) && world.getBlockEntity(otherPos) instanceof DoubleSlabBlockEntity entity) {
            Block b = ModBlockMap.verticalSlabToSlab(negativeSlab);
            return b == entity.getPositiveSlabState().getBlock() && b == entity.getNegativeSlabState().getBlock();
        }

        return ModBlockMap.verticalSlabToOriginal(negativeSlab) == otherBlock;
    }
}
