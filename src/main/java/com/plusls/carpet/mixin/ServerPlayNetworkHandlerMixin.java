package com.plusls.carpet.mixin;

import com.plusls.carpet.fakefapi.PacketSender;
import com.plusls.carpet.network.PcaCustomPayload;
import com.plusls.carpet.network.PcaSyncProtocol;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin
{
    @Shadow @Final protected MinecraftServer server;

    @Inject(method = "onCustomPayload", at = @At("HEAD"))
    private void pcaProtocol(CustomPayloadC2SPacket packet, CallbackInfo ci)
    {
        var self = (ServerCommonNetworkHandler)(Object)this;
        if (packet.payload() instanceof PcaCustomPayload payload && self instanceof ServerPlayNetworkHandler handler)
        {
            Identifier identifier = payload.id();
            MinecraftServer server = this.server;
            ServerPlayerEntity player = handler.getPlayer();
            PacketByteBuf buf = payload.buf();
            PacketSender sender = new PacketSender();

            if (identifier.equals(PcaSyncProtocol.SYNC_BLOCK_ENTITY))
            {
                PcaSyncProtocol.syncBlockEntityHandler(server, player, handler, buf, sender);
            }
            if (identifier.equals(PcaSyncProtocol.SYNC_ENTITY))
            {
                PcaSyncProtocol.syncEntityHandler(server, player, handler, buf, sender);
            }
            if (identifier.equals(PcaSyncProtocol.CANCEL_SYNC_BLOCK_ENTITY))
            {
                PcaSyncProtocol.cancelSyncBlockEntityHandler(server, player, handler, buf, sender);
            }
            if (identifier.equals(PcaSyncProtocol.CANCEL_SYNC_ENTITY))
            {
                PcaSyncProtocol.cancelSyncEntityHandler(server, player, handler, buf, sender);
            }
        }
    }

    // fabric api ServerPlayConnectionEvents.DISCONNECT
    @Inject(method = "onDisconnected", at = @At("HEAD"))
    private void handleDisconnection(CallbackInfo ci)
    {
        var self = (ServerCommonNetworkHandler)(Object)this;
        if (self instanceof ServerPlayNetworkHandler handler)
        {
            PcaSyncProtocol.onDisconnect(handler, this.server);
        }
    }
}
