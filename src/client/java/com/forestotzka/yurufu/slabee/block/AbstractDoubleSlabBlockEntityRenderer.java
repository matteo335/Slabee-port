package com.forestotzka.yurufu.slabee.block;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBlockModelPart;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

import java.util.List;

public abstract class AbstractDoubleSlabBlockEntityRenderer<T extends AbstractDoubleSlabBlockEntity> implements BlockEntityRenderer<T> {
    protected int cachedRenderDistance = 1;
    protected long lastUpdateTime = 0;
    protected final MinecraftClient client = MinecraftClient.getInstance();

    public AbstractDoubleSlabBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    //@Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, BlockModelPart part, int light, int overlay) {
        BlockPos pos = entity.getPos();
        BlockRenderView world = client.world;
        Random random = Random.create();

        int positiveRenderLayerType = entity.getPositiveRenderLayerType();
        if (positiveRenderLayerType <= 1) {
            renderPositive(positiveRenderLayerType, entity.getPositiveSlabState(), pos, world, matrices, vertexConsumers, part);
        }

        int negativeRenderLayerType = entity.getNegativeRenderLayerType();
        if (negativeRenderLayerType <= 1) {
            renderNegative(negativeRenderLayerType, entity.getNegativeSlabState(), pos, world, matrices, vertexConsumers, part);
        }
    }

    protected void renderPositive(int renderLayerType, BlockState slabState, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumerProvider vertexConsumers, BlockModelPart part) {
        VertexConsumer vertexConsumer = getVertexConsumer(renderLayerType, vertexConsumers);
        client.getBlockRenderManager().renderBlock(slabState, pos, world, matrices, vertexConsumer, true, (List<BlockModelPart>) part);
    }

    //@Override
    public void renderNegative(int renderLayerType, BlockState slabState, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumerProvider vertexConsumers, FabricBlockModelPart part) {
        VertexConsumer vertexConsumer = getVertexConsumer(renderLayerType, vertexConsumers);
        client.getBlockRenderManager().renderBlock(slabState, pos, world, matrices, vertexConsumer, true, (List<BlockModelPart>) part);
    }

    protected VertexConsumer getVertexConsumer(int renderLayerType, VertexConsumerProvider vertexConsumers) {
        if (renderLayerType == 1) {
            return vertexConsumers.getBuffer(RenderLayer.getCutoutMipped());
        } else {
            return vertexConsumers.getBuffer(RenderLayer.getSolid());
        }
    }

    @Override
    public int getRenderDistance() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime > 1000) {
            cachedRenderDistance = (client.options.getViewDistance().getValue() + 1) * 16;
            lastUpdateTime = currentTime;
        }
        return cachedRenderDistance;
    }

    protected abstract void renderPositive(int renderLayerType, BlockState slabState, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Random random);

    protected abstract void renderNegative(int renderLayerType, BlockState slabState, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Random random);
}
