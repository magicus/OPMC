package com.bursie.opmc;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FabricItem extends Item {

    public FabricItem(Settings settings) {
        super(settings);
    }

    private void sendProjectileEpic(World world, PlayerEntity user) {
        FireballEntity fireballEntity = new FireballEntity(world, user, 0.0f, 0.0f, 0.0f);
        fireballEntity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);

        fireballEntity.explosionPower = 1;
        fireballEntity.updatePosition(user.getX(), user.getY(), user.getZ());
        world.spawnEntity(fireballEntity);
    }

    private void sendProjectileWeak(World world, PlayerEntity user) {
        SnowballEntity snowballEntity = new SnowballEntity(world, user);
        ItemStack itemStack1 =  new ItemStack(Items.FIRE_CHARGE);
        snowballEntity.setItem(itemStack1);
        snowballEntity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);
        world.spawnEntity(snowballEntity);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
        if (!world.isClient) {

/*
            // FUNKAR OK, gör "snöbollar" som är FIRE CHARGE...
            SnowballEntity snowballEntity = new SnowballEntity(world, user);
            ItemStack itemStack1 =  new ItemStack(Items.FIRE_CHARGE);
            snowballEntity.setItem(itemStack1);
            snowballEntity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);
            world.spawnEntity(snowballEntity);
*/

/*

            EntityRendererRegistry.INSTANCE.register(null, null);
            ZapBallEntity zapBallEntity = new ZapBallEntity(ZapBallEntity.ZAP_BALL_PROJECTILE_ENTITY_TYPE, user, world); //, 0.0f, 0.0f, 0.0f);
 //           ZapBallEntity zapBallEntity = new ZapBallEntity(EntityType.FIREBALL, user, world); //, 0.0f, 0.0f, 0.0f);
            // speed, divergence
            zapBallEntity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);
            ItemStack itemStack1 =  new ItemStack(Items.SNOWBALL);
         //   zapBallEntity.extinguish();
         //   zapBallEntity.isOnFire();
            zapBallEntity.setItem(itemStack1);
            world.spawnEntity(zapBallEntity);


 */


// OK!!! BUT STOPS!
            FireballEntity fireballEntity = new FireballEntity(world, user, 0.0f, 0.0f, 0.0f);
            fireballEntity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);

            fireballEntity.explosionPower = 1;
            fireballEntity.updatePosition(user.getX(), user.getY(), user.getZ());
            world.spawnEntity(fireballEntity);



/*
       //     FireballEntity fireballEntity = new FireballEntity(world, user, 0.0f, 0.0f, 0.0f);
// OK BUT STOPS???
            ZapBallEntity fireballEntity = new ZapBallEntity(world, user, 0.0f, 0.0f, 0.0f);
            fireballEntity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);

            fireballEntity.explosionPower = 1;
         //   fireballEntity.setItem(itemStack);
            fireballEntity.updatePosition(user.getX(), user.getY(), user.getZ());
            world.spawnEntity(fireballEntity);


 */
//            FireballEntity fireballEntity = new FireballEntity(world, user);
            ZapBallEntity zapBallEntity = new ZapBallEntity(world, user);
            // speed, divergence
            zapBallEntity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);
        //    ItemStack itemStack1 =  new ItemStack(Items.FIRE_CHARGE);
     //       zapBallEntity.setItem(itemStack1);
            world.spawnEntity(zapBallEntity);


/*

            FireballEntity fireballEntity = new FireballEntity(EntityType.FIREBALL, world);
            fireballEntity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);

            fireballEntity.setItem(itemStack);
            world.spawnEntity(fireballEntity);


 */


        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));


        return TypedActionResult.success(itemStack, world.isClient());
    }
//    @Override
    public TypedActionResult<ItemStack> use2(World world, PlayerEntity playerEntity, Hand hand) {
        playerEntity.playSound(SoundEvents.BLOCK_WOOL_BREAK, 1.0F, 1.0F);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}