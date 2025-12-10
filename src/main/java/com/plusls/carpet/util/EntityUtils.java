package com.plusls.carpet.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EntityUtils
{
	public static Level getEntityWorld(@NotNull Entity entity)
	{
		//#if MC >= 12106
		//$$ return entity.level();
		//#else
		return entity.getCommandSenderWorld();
		//#endif
	}
}
