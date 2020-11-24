package com.bursie.opmc;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class OpmcMain implements ModInitializer, ClientModInitializer {
	private static KeyBinding keyBinding;
	public static final Identifier OPMC_ZAPPER_PACKET = new Identifier("opmc", "zapper");

	public static final FabricItem OPMC_BLUE_SPIRE_ITEM
			= new FabricItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(16));

	public static void sendProjectileEpic(World world, PlayerEntity user) {
		FireballEntity fireballEntity = new FireballEntity(world, user, 0.0f, 0.0f, 0.0f);
		fireballEntity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);

		fireballEntity.explosionPower = 10;
		fireballEntity.updatePosition(user.getX(), user.getY(), user.getZ());
		world.spawnEntity(fireballEntity);
	}

	public static void sendProjectileWeak(World world, PlayerEntity user) {
		SnowballEntity snowballEntity = new SnowballEntity(world, user);
		ItemStack itemStack1 =  new ItemStack(Items.FIRE_CHARGE);
		snowballEntity.setItem(itemStack1);
		snowballEntity.setProperties(user, user.pitch, user.yaw, 0.0F, 1.5F, 1.0F);
		world.spawnEntity(snowballEntity);
	}

	private void chatMsg(MinecraftClient client, String msg)  {
		client.player.sendMessage(new LiteralText(msg), false);
	}

	private void sendZapperPacket(int i) {
		PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
		passedData.writeByte(i);
		ClientSidePacketRegistry.INSTANCE.sendToServer(OpmcMain.OPMC_ZAPPER_PACKET, passedData);
	}

	private void onPressZ(MinecraftClient client) {
		chatMsg(client, "Z nedtryckt");
		ItemStack itemStack = client.player.getStackInHand(Hand.MAIN_HAND);
		if (itemStack.getItem().equals(OPMC_BLUE_SPIRE_ITEM)) {
			chatMsg(client, "slot 0 is MY THING");
			chatMsg(client, "slot 0 is: " + itemStack.getItem().getTranslationKey());
			client.player.playSound(SoundEvents.ENTITY_SHEEP_HURT, 1.0F, 1.0F);

			// We are in the client now, can't do anything server-sided
			sendZapperPacket(0);
		} else {
			sendZapperPacket(1);
			client.player.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 1.0F, 1.0F);
		}
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("OPMC loaded");
		Registry.register(Registry.ITEM, new Identifier("opmc", "blue_spire"), OPMC_BLUE_SPIRE_ITEM);

		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.opmc.use_spire", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_Z, // The keycode of the key
				"category.opmc.keys" // The translation key of the keybinding's category.
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBinding.wasPressed()) {
				onPressZ(client);
			}
		});

		ServerSidePacketRegistry.INSTANCE.register(OPMC_ZAPPER_PACKET, (packetContext, attachedData) -> {
			byte type = attachedData.readByte();
			packetContext.getTaskQueue().execute(() -> {
				// Execute on the main thread
				PlayerEntity user = packetContext.getPlayer();
				World world = packetContext.getPlayer().world;
				if (type == 0) {
					sendProjectileEpic(world, user);
				} else {
					sendProjectileWeak(world, user);
				}
			});
		});
	}

	@Override
	public void onInitializeClient() {
	}

	public static class FabricItem extends Item {

		public FabricItem(Settings settings) {
			super(settings);
		}

		@Override
		public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
			ItemStack itemStack = user.getStackInHand(hand);
			world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
			if (!world.isClient) {
				System.out.println("its not a client world: " + world);
				sendProjectileEpic(world, user);
			}
			user.incrementStat(Stats.USED.getOrCreateStat(this));
			return TypedActionResult.success(itemStack, world.isClient());
		}
	}
}
