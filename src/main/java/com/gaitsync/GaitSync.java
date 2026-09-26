package com.gaitsync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

public class GaitSync implements ModInitializer {
    public static final String MOD_ID = "gaitsync";
    public static final Logger LOGGER = LoggerFactory.getLogger("GaitSync");

    @Override
    public void onInitialize() {
        // Caly mod dziala po stronie klienta -- patrz GaitSyncClient.
    }
}
