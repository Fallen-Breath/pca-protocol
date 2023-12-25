package com.plusls.carpet.fakefapi;

import com.plusls.carpet.network.PcaCustomPayload;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ServerPlayNetworking
{
    public static void send(ServerPlayerEntity player, Identifier identifier, PacketByteBuf buf)
    {
        player.networkHandler.sendPacket(new CustomPayloadS2CPacket(new PcaCustomPayload(identifier, buf)));
    }
}
