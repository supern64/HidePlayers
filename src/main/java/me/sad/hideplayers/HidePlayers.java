package me.sad.hideplayers;

import me.sad.hideplayers.commands.RenderPlayersCommand;
import me.sad.hideplayers.listeners.KeyInputListener;
import me.sad.hideplayers.listeners.RenderLivingListener;
import me.sad.hideplayers.utils.ConfigUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod(modid = HidePlayers.MOD_ID, version = "2.0.0")
public class HidePlayers {
    public static String prefix = "\u00a75HidePlayers \u00a7c- \u00a7r";
    public static final String MOD_ID = "hideplayers";
    public static List<String> players = new ArrayList<>();
    public static Map<UUID, String> armorStandCache = new HashMap<>();
    public static boolean toggled = true;
    public static KeyBinding toggleKey;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) throws IOException {
        ConfigUtils.getConfig();
        toggleKey = new KeyBinding("Toggle visibility", Keyboard.KEY_NONE, "Hide Players");
        ClientRegistry.registerKeyBinding(toggleKey);
        MinecraftForge.EVENT_BUS.register(new RenderLivingListener());
        MinecraftForge.EVENT_BUS.register(new KeyInputListener());
        ClientCommandHandler.instance.registerCommand(new RenderPlayersCommand());
    }
    public static void toggleRenderer(ICommandSender sender) {
        HidePlayers.toggled = !HidePlayers.toggled;
        if (HidePlayers.toggled) {
            sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + "Toggled rendering players \u00a7aON\u00a7r!"));
        } else {
            sender.addChatMessage(new ChatComponentText(HidePlayers.prefix + "Toggled rendering players \u00a7cOFF\u00a7r!"));
        }
        try {
            ConfigUtils.writeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
