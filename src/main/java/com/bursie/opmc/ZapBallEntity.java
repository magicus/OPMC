package com.bursie.opmc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

@SuppressWarnings("EntityConstructor")
public class ZapBallEntity extends FireballEntity {


    public static final EntityType<ZapBallEntity> ZAP_BALL_PROJECTILE_ENTITY_TYPE ;

    protected boolean isBurning() {
        return true;
    }

    public ZapBallEntity(EntityType<? extends FireballEntity> entityType, World world) {
        super(entityType, world);
    }

    public ZapBallEntity(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ) {
        super(world, owner, velocityX, velocityY, velocityZ);
    }


    protected ZapBallEntity(EntityType<? extends FireballEntity> type, double x, double y, double z, World world) {
        this(type, world);
        this.updatePosition(x, y, z);
    }


    protected ZapBallEntity(EntityType<? extends FireballEntity> type, LivingEntity owner, World world) {
        this(type, owner.getX(), owner.getEyeY() - 0.10000000149011612D, owner.getZ(), world);
        this.setOwner(owner);
    }

    public ZapBallEntity(World world, LivingEntity owner) {
        this(ZAP_BALL_PROJECTILE_ENTITY_TYPE, owner, world);
    }

/*
    public ZapBallProjectile(World world, double x, double y, double z) {
        super(ZAP_BALL_PROJECTILE_ENTITY_TYPE, x, y, z, world);
    }
*/

/*
    public ZapBallProjectile(EntityType<? extends ZapBallProjectile> entityEntityType, World world) {
        super(entityEntityType, world);
    }
*/
    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return (EntityType) Registry.register(Registry.ENTITY_TYPE, id, type.build(id));
    }

    static {
        ZAP_BALL_PROJECTILE_ENTITY_TYPE = register("opmc:zap_ball", EntityType.Builder.<ZapBallEntity>create(ZapBallEntity::new, SpawnGroup.MISC).setDimensions(0.25F, 0.25F).maxTrackingRange(8).trackingTickInterval(20));
   }

   /*
       protected void onEntityHit(EntityHitResult entityHitResult) {
        this.setVelocity(this.getVelocity().multiply(-0.01D, -0.1D, -0.01D));
        float g = 1.0F;
        if (this.world instanceof ServerWorld && this.world.isThundering() && EnchantmentHelper.hasChanneling(this.tridentStack)) {
            BlockPos blockPos = entity.getBlockPos();
            if (this.world.isSkyVisible(blockPos)) {
                LightningEntity lightningEntity = (LightningEntity)EntityType.LIGHTNING_BOLT.create(this.world);
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                lightningEntity.setChanneler(entity2 instanceof ServerPlayerEntity ? (ServerPlayerEntity)entity2 : null);
                this.world.spawnEntity(lightningEntity);
                soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
                g = 5.0F;
            }
        }



        Entity entity = entityHitResult.getEntity();
        float f = 8.0F;
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            f += EnchantmentHelper.getAttackDamage(this.tridentStack, livingEntity.getGroup());
        }

        Entity entity2 = this.getOwner();
        DamageSource damageSource = DamageSource.trident(this, (Entity)(entity2 == null ? this : entity2));
        this.dealtDamage = true;
        SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_HIT;
        if (entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity2 = (LivingEntity)entity;
                if (entity2 instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity2, entity2);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity2);
                }

                this.onHit(livingEntity2);
            }
        }


        this.playSound(soundEvent, g, 1.0F);
    }
    */
/*

    public ZapBallProjectile(World world, LivingEntity owner) {
        super(ZAP_BALL_PROJECTILE_ENTITY_TYPE, world);
    }
    public ZapBallProjectile(EntityType<? extends FireballEntity> entityType, World world) {
        super(entityType, world);
    }


    public ZapBallProjectile(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
    }

    public ZapBallProjectile(World world, LivingEntity owner, double velocityX, double velocityY, double velocityZ) {
        super(world, owner, velocityX, velocityY, velocityZ);
    }
    */
}
