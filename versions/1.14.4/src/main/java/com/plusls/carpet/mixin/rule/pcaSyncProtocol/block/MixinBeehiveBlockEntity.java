package com.plusls.carpet.mixin.rule.pcaSyncProtocol.block;

import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

// impl in mc >= 1.15
@Mixin(BlockEntity.class)
public abstract class MixinBeehiveBlockEntity {
}
