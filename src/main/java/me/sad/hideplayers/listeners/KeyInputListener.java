package me.sad.hideplayers.listeners;

import me.sad.hideplayers.HidePlayers;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputListener {
    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (HidePlayers.toggleKey.isPressed()) {
            HidePlayers.toggleRenderer(Minecraft.getMinecraft().thePlayer);
        }
    }
}
