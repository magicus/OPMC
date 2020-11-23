package com.bursie.opmc;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.options.StickyKeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;

public class KeyBindingsTest implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBinding binding1 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_1", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "key.category.first.test"));
        KeyBinding binding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fabric-key-binding-api-v1-testmod.test_keybinding_2", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, "key.category.second.test"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (binding1.wasPressed()) {
                client.player.sendMessage(new LiteralText("Har tryckt Z"), false);
            }

            while (binding2.wasPressed()) {
                client.player.sendMessage(new LiteralText("Har tryckt X"), false);
            }
        });
    }
}