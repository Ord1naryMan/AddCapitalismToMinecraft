package com.firstever;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstEver implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("firstever");

	@Override
	public void onInitialize() {

		LOGGER.info("FirstEver init");

		UseItemCallback.EVENT.register(new SellItem());
		UseBlockCallback.EVENT.register(new PayForInteraction());

		ServerTickEvents.END_SERVER_TICK.register(new Taxes());
	}
}