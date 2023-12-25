package com.plusls.carpet.network;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PcaCustomPayload(Identifier id, PacketByteBuf buf) implements CustomPayload
{
	public PcaCustomPayload(Identifier id, PacketByteBuf buf)
	{
		this.id = id;
		this.buf = new PacketByteBuf(Unpooled.buffer());
		this.buf.writeBytes(buf);
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeBytes(this.buf);
	}
}
