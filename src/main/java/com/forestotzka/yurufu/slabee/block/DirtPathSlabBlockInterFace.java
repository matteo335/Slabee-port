package com.forestotzka.yurufu.slabee.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.tick.ScheduledTickView;

public interface DirtPathSlabBlockInterFace {
    BlockState getStateForNeighborUpdate(BlockState state, WorldAccess world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random);
}
