package com.plusls.carpet.mixin;

import com.plusls.carpet.util.PcaContainerListener;
import com.plusls.carpet.util.PcaSimpleContainer;
import net.minecraft.world.SimpleContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * mc < 26.1 : subproject 1.20.2 (main project)
 * mc >= 26.1: subproject 26.1.1        <--------
 */
@Mixin(SimpleContainer.class)
public abstract class SimpleContainerMixin implements PcaSimpleContainer
{
	@Unique
	private List<PcaContainerListener> pca$containerListeners = new ArrayList<>();

	@Override
	public void pca$addListener(PcaContainerListener listener)
	{
		this.pca$containerListeners.add(listener);
	}

	@Inject(method = "setChanged", at = @At("HEAD"))
	private void onSetChanged(CallbackInfo ci)
	{
		for (PcaContainerListener listener : this.pca$containerListeners)
		{
			listener.containerChanged((SimpleContainer)(Object)this);
		}
	}
}
