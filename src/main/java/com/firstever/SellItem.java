package com.firstever;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class SellItem implements UseItemCallback {

    Map<Item, Float> quantForOneDiamond = new HashMap<>(){{
        put(Items.DIRT, 64f);
        put(Items.COBBLESTONE, 64f);
        put(Items.DIAMOND_ORE, 1f/64);
        put(Items.IRON_INGOT, 1f);
    }};

    @Override
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        ItemStack item = player.getStackInHand(hand);

        if (player.isCreative() && player.isSpectator()) {
            return TypedActionResult.pass(item);
        }

        if (item.isOf(Items.DIAMOND_BLOCK) && item.getCount() == 64) {
            item.setCount(0);
            player.giveItemStack(new ItemStack(Items.BEDROCK));
            return TypedActionResult.success(item);
        }

        if (quantForOneDiamond.containsKey(item.getItem())) {
            int count = item.getCount();
            int diamondsToGive = (int) Math.floor(
                (double) count / quantForOneDiamond.get(item.getItem())
            );
            if (diamondsToGive < 1) {
                return TypedActionResult.pass(item);
            }
            item.setCount(0);
            player.giveItemStack(new ItemStack(Items.DIAMOND, diamondsToGive));
            return TypedActionResult.success(item);
        }
        return TypedActionResult.pass(item);
    }
}
