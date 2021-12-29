package dev.arthomnix.snowballdamage.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SnowballEntity.class)
abstract class SnowballEntityMixin extends ThrownItemEntity {

    // we need a constructor in order to extend ThrownItemEntity
    // gets ignored by mixin
    public SnowballEntityMixin(EntityType<? extends SnowballEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "onEntityHit",
    at = @At(target = "net/minecraft/entity/Entity.damage (Lnet/minecraft/entity/damage/DamageSource;F)Z",
    value = "INVOKE"))
    private boolean doDamageIfTag(Entity instance, DamageSource source, float amount) {
        return instance.damage(source, getDamage(amount));
    }

    private float getDamage(float originalAmount) {
        NbtCompound nbtTags = this.getItem().getOrCreateNbt();
        for (String key : nbtTags.getKeys()) {
            if (key.equals("Damage")) {
                return nbtTags.getFloat(key);
            }
        }
        return originalAmount;
    }
}
