package com.forestotzka.yurufu.slabee.mixin;

import com.forestotzka.yurufu.slabee.block.*;
import com.forestotzka.yurufu.slabee.block.enums.DoubleSlabVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow
    public abstract BlockState getDefaultState();

    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void onShouldDrawSide(BlockState state, BlockState otherState, Direction side, CallbackInfoReturnable<Boolean> cir) {
        Block block = state.getBlock();

        if (block instanceof SlabBlock) {
            BlockState blockState = otherState;
            SlabType slabType = state.get(Properties.SLAB_TYPE);

            if (blockState.isOf(ModBlocks.DOUBLE_SLAB_BLOCK)) {
                if (slabType == SlabType.BOTTOM) {
                    if (side == Direction.UP) {
                        cir.setReturnValue(DoubleSlabUtils.isPositiveSeeThrough(blockState) && DoubleSlabVariant.fromBlock(state.getBlock()) != blockState.get(AbstractDoubleSlabBlock.POSITIVE_SLAB));
                        cir.cancel();
                    }
                } else if (slabType == SlabType.TOP) {
                    if (side == Direction.DOWN) {
                        cir.setReturnValue(DoubleSlabUtils.isNegativeSeeThrough(blockState) && DoubleSlabVariant.fromBlock(state.getBlock()) != blockState.get(AbstractDoubleSlabBlock.NEGATIVE_SLAB));
                        cir.cancel();
                    }
                }
            }

        } else if (block instanceof VerticalSlabBlock) {
            BlockState blockState = otherState;

            if (blockState.isOf(ModBlocks.DOUBLE_VERTICAL_SLAB_BLOCK)) {
                Direction d = state.get(Properties.HORIZONTAL_FACING);
                if (d.getOpposite() == side) {
                    if (d == Direction.EAST || d == Direction.SOUTH) {
                        cir.setReturnValue(DoubleSlabUtils.isNegativeSeeThrough(blockState) && DoubleSlabVariant.fromBlock(state.getBlock()) != blockState.get(AbstractDoubleSlabBlock.NEGATIVE_SLAB));
                        cir.cancel();
                    } else if (d == Direction.WEST || d == Direction.NORTH) {
                        cir.setReturnValue(DoubleSlabUtils.isPositiveSeeThrough(blockState) && DoubleSlabVariant.fromBlock(state.getBlock()) != blockState.get(AbstractDoubleSlabBlock.POSITIVE_SLAB));
                        cir.cancel();
                    }
                }
            }
        }
    }
}
