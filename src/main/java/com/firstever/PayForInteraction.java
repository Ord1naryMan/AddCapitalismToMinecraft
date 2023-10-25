package com.firstever;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlastFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.SmokerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class PayForInteraction implements UseBlockCallback {

    Map<Class<? extends Block>, Integer> blockInteractionCost = new HashMap<>(){{
        put(CraftingTableBlock.class, 5);
        put(FurnaceBlock.class, 10);
        put(BlastFurnaceBlock.class, 10);
        put(SmokerBlock.class, 10);
        put(EnchantingTableBlock.class, 64);
        put(AnvilBlock.class, 64);
        put(ChestBlock.class, 20);
    }};

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (player.isCreative() && player.isSpectator()) {
            return ActionResult.PASS;
        }

        Block interractedBlock = world.getBlockState(
            hitResult.getBlockPos()
        ).getBlock();

        if (blockInteractionCost.containsKey(interractedBlock.getClass())) {
            int cost = blockInteractionCost.get(interractedBlock.getClass());
            if (!player.getStackInHand(hand).isOf(Items.DIAMOND) ||
                (player.getStackInHand(hand).isOf(Items.DIAMOND) &&
                    player.getStackInHand(hand).getCount() < cost)) {
                return ActionResult.FAIL;
            }

            ItemStack newStack = player.getStackInHand(hand);
            newStack.setCount(newStack.getCount() - cost);

            player.setStackInHand(hand, newStack);

            return ActionResult.PASS;
        }

        return ActionResult.PASS;
    }
}
