package com.plusls.carpet.mixin;

import com.google.common.collect.ImmutableMap;
import com.plusls.carpet.network.PcaCustomPayload;
import com.plusls.carpet.network.PcaSyncProtocol;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(CustomPayloadC2SPacket.class)
public abstract class CustomPayloadC2SPacketMixin
{
	@Shadow @Final @Mutable
	private static Map<Identifier, PacketByteBuf.PacketReader<? extends CustomPayload>> ID_TO_READER;

	static
	{
		var builder = ImmutableMap.<Identifier, PacketByteBuf.PacketReader<? extends CustomPayload>>builder();
		builder.putAll(ID_TO_READER);
		PcaSyncProtocol.ALL_PACKET_IDS.forEach(id -> builder.put(id, buf -> new PcaCustomPayload(id, buf)));
		ID_TO_READER = builder.build();
	}
}
