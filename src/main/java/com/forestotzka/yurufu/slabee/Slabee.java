package com.forestotzka.yurufu.slabee;

import com.forestotzka.yurufu.slabee.block.ModBlockEntities;
import com.forestotzka.yurufu.slabee.block.ModBlocks;
import com.forestotzka.yurufu.slabee.handler.ModHandlers;
import com.forestotzka.yurufu.slabee.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Slabee implements ModInitializer {
    public static final String MOD_ID = "slabee";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Loading a unofficial and experimental port of Slabee made by Mattéo35");
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModCreativeTabs.registerItemGroups();
        ModBlockEntities.registerModBlockEntities();
        ModHandlers.register();
        ModConfig.load();
    }
}
