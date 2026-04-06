package com.plusls.carpet.mixin;

import com.plusls.carpet.util.PcaContainerListener;
import com.plusls.carpet.util.PcaSimpleContainer;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * mc < 26.1 : subproject 1.20.2 (main project)        <--------
 * mc >= 26.1: subproject 26.1.1
 */
@Mixin(SimpleContainer.class)
public abstract class SimpleContainerMixin implements PcaSimpleContainer
{
	@Shadow
	public abstract void addListener(ContainerListener containerListener);

	@Override
	public void pca$addListener(PcaContainerListener listener)
	{
		this.addListener(listener::containerChanged);
	}
}
