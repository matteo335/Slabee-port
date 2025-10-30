package com.forestotzka.yurufu.slabee.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

@Environment(EnvType.CLIENT)
public class DoubleSlabBlockEntityRenderer extends AbstractDoubleSlabBlockEntityRenderer<DoubleSlabBlockEntity> {
    public DoubleSlabBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected void renderPositive(int renderLayerType, BlockState slabState, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Random random) {
        if (slabState.getBlock() instanceof SnowySlabBlock) {
            slabState = slabState.with(SnowySlabBlock.SNOWY, world.getBlockState(pos.up()).isIn(BlockTags.SNOW));
        }

        //super.renderPositive(renderLayerType, slabState, pos, world, matrices, vertexConsumers, random);
    }

    @Override
    protected void renderNegative(int renderLayerType, BlockState slabState, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Random random) {

    }

    @Override
    public void render(DoubleSlabBlockEntity entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos) {

    }

    @Override
    public boolean rendersOutsideBoundingBox() {
        return super.rendersOutsideBoundingBox();
    }

    @Override
    public boolean isInRenderDistance(DoubleSlabBlockEntity blockEntity, Vec3d pos) {
        return super.isInRenderDistance(blockEntity, pos);
    }
}
