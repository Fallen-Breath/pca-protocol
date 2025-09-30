package com.plusls.carpet.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EntityUtils
{
	public static World getEntityWorld(@NotNull Entity entity)
	{
		//#if 1.21.6 <= MC && MC < 1.21.9
		//$$ return entity.getWorld();
		//#else
		return entity.getEntityWorld();
		//#endif
	}
}
