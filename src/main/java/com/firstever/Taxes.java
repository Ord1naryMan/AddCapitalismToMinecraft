package com.firstever;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Taxes implements ServerTickEvents.EndTick {

    Map<PlayerEntity, Boolean> allowJump = new HashMap<>();
    Set<Item> restrictedItems = Set.of(
        Items.DIAMOND_AXE,
        Items.DIAMOND_PICKAXE,
        Items.DIAMOND_SWORD,
        Items.DIAMOND_HELMET,
        Items.DIAMOND_LEGGINGS,
        Items.DIAMOND_BOOTS,
        Items.DIAMOND_CHESTPLATE,
        Items.DIAMOND_SHOVEL
    );

    @Override
    public void onEndTick(MinecraftServer server) {
        restrictJumping(server);
        deleteDiamondToolsAndArmor(server);
    }

    private void deleteDiamondToolsAndArmor(MinecraftServer server) {
        for (var player : server.getPlayerManager().getPlayerList()) {
            player.getInventory()
                .remove(
                    item -> restrictedItems.contains(item.getItem()),
                    1,
                    player.getInventory()
                );
        }
    }

    private void restrictJumping(MinecraftServer server) {
        for (var player : server.getPlayerManager().getPlayerList()) {
            if (!allowJump.containsKey(player)) {
                allowJump.put(player, false);
            }
            if (player.getVelocity().getY() <= 0) {
                allowJump.put(player, false);
            }

            if (player.getVelocity().getY() > 0) {
                if (allowJump.get(player)) {
                    continue;
                }
                if (
                    player.getInventory()
                        .contains(Items.DIAMOND.getDefaultStack())
                ) {

                    int slotWithDiamonds = player.getInventory()
                        .getSlotWithStack(Items.DIAMOND.getDefaultStack());

                    int prevDiamondQuantity = player.getInventory()
                            .getStack(slotWithDiamonds).getCount();

                    player.getInventory()
                        .getStack(slotWithDiamonds)
                        .setCount(prevDiamondQuantity - 1);

                    allowJump.put(player, true);
                }
            }
        }
    }
}
