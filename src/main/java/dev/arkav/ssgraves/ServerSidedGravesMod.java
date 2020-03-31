package dev.arkav.ssgraves;

import dev.arkav.ssgraves.events.PlayerDropDeathCallback;
import dev.arkav.ssgraves.mixinapi.IMixinSkullBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ServerSidedGravesMod implements ModInitializer, AttackBlockCallback, PlayerDropDeathCallback {
    @Override
    public void onInitialize() {
        AttackBlockCallback.EVENT.register(this);
        PlayerDropDeathCallback.EVENT.register(this);
    }

    @Override
    public ActionResult interact(PlayerEntity playerEntity, World world, Hand hand, BlockPos blockPos, Direction direction) {
        BlockEntity be = world.getBlockEntity(blockPos);
        if (be instanceof SkullBlockEntity) {
            IMixinSkullBlockEntity grave = (IMixinSkullBlockEntity) be;
            if (grave.getGraveOwner().getId().equals(playerEntity.getUuid())) {
                if (grave.getInventory() != null && !grave.getInventory().isEmpty()) {
                    playerEntity.inventory.deserialize(grave.getInventory());
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                    playerEntity.sendMessage(new LiteralText("Your grave vanishes into thin air!"));
                    playerEntity.playSound(SoundEvents.BLOCK_WOODEN_DOOR_OPEN, 1.0f, 1.0f);
                }
            } else {
                playerEntity.sendMessage(new LiteralText(String.format("You cannot claim %s's grave!", grave.getGraveOwner().getName())));
                playerEntity.playSound(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 1.0f, 1.0f);
                return ActionResult.FAIL;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public ActionResult drop(PlayerEntity entity, World world) {
        assert entity != null;
        BlockState state = Blocks.PLAYER_HEAD.getDefaultState();
        world.setBlockState(entity.getBlockPos(), state);
        IMixinSkullBlockEntity grave = (IMixinSkullBlockEntity) world.getBlockEntity(entity.getBlockPos());
        assert grave != null;
        grave.setGraveOwner(entity);
        entity.sendMessage(new LiteralText("Your grave can be found at " + entity.getBlockPos().toString()));
        return ActionResult.FAIL;
    }
}
