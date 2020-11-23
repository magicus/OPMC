package com.bursie.opmc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ModInitializer, ClientModInitializer {
	private static KeyBinding keyBinding;

	public static final FabricItem FABRIC_ITEM = new FabricItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(16));

	private void chatMsg(MinecraftClient client, String msg)  {
		client.player.sendMessage(new LiteralText(msg), false);
	}

	private void onPressZ(MinecraftClient client) {
		chatMsg(client, "Z nedtryckt");
		ItemStack itemStack = client.player.getStackInHand(Hand.MAIN_HAND);
				// client.player.inventory.main.get(0);
	//	client.player.getStackInHand(Hand.MAIN_HAND);
		if (itemStack.isEmpty()) {
			chatMsg(client, "slot 0 is empty");
			client.player.playSound(SoundEvents.ENTITY_GHAST_SCREAM, 1.0F, 1.0F);
		} else {
			chatMsg(client, "slot 0 is NOT empty");
			chatMsg(client, "slot 0 is: " + itemStack.getItem().getTranslationKey());
			client.player.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 1.0F, 1.0F);
		}
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Hello Oscar and Matilda!");
		Registry.register(Registry.ITEM, new Identifier("tutorial", "fabric_item"), FABRIC_ITEM);

		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.examplemod.spook", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_Z, // The keycode of the key
				"category.examplemod.test" // The translation key of the keybinding's category.
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBinding.wasPressed()) {
				onPressZ(client);
			}
		});
	}

	@Override
	public void onInitializeClient() {

	}
}
