package com.plusls.carpet.mixin;

import com.plusls.carpet.fakefapi.PacketSender;
import com.plusls.carpet.network.PcaSyncProtocol;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin
{
    @Shadow @Final private MinecraftServer server;

    // fabric api ServerPlayConnectionEvents.JOIN
    @ModifyVariable(method = "onPlayerConnect", at = @At("TAIL"), argsOnly = true)
    private ServerPlayerEntity handleDisconnection(ServerPlayerEntity player)
    {
        PcaSyncProtocol.onJoin(player.networkHandler, new PacketSender(), this.server);
        return player;
    }
}
