package me.sad.hideplayers;

import me.sad.hideplayers.commands.RenderPlayersCommand;
import me.sad.hideplayers.utils.ConfigUtils;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

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

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) throws IOException {
        ConfigUtils.getConfig();
        ClientCommandHandler.instance.registerCommand(new RenderPlayersCommand());
    }
}
