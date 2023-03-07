package me.sad.hideplayers.listeners;

import me.sad.hideplayers.HidePlayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.stream.Collectors;

public class RenderLivingListener {
    @SubscribeEvent
    public void onRenderPlayer(RenderLivingEvent.Pre<EntityLivingBase> event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        NetHandlerPlayClient connection = minecraft.getNetHandler();

        if (event.entity instanceof EntityOtherPlayerMP && !HidePlayers.toggled) {
            if (event.entity.getName().matches("^[a-zA-Z0-9_]*$") && event.entity.getUniqueID().version() != 2 && !HidePlayers.players.contains(event.entity.getName().toLowerCase())) { // hypixel marks npc uuids as v2
                event.setCanceled(true);
            }
        }
        if (event.entity instanceof EntityArmorStand) {
            if (!HidePlayers.armorStandCache.containsKey(event.entity.getPersistentID())) {
                HidePlayers.armorStandCache.put(event.entity.getPersistentID(), null);
                if (event.entity.hasCustomName() && !event.entity.getCustomNameTag().toLowerCase().contains(minecraft.thePlayer.getName().toLowerCase())) {
                    List<String> playerList = connection.getPlayerInfoMap().stream().map(NetworkPlayerInfo::getGameProfile).map(gameProfile -> gameProfile.getName()).collect(Collectors.toList());
                    playerList.forEach(p -> {
                        if (event.entity.getCustomNameTag().toLowerCase().contains(p.toLowerCase())) {
                            HidePlayers.armorStandCache.put(event.entity.getPersistentID(), p);
                        }
                    });
                }
            }
            String armorStandOwner = HidePlayers.armorStandCache.get(event.entity.getPersistentID());
            if (armorStandOwner != null && !HidePlayers.players.contains(armorStandOwner.toLowerCase()) && !HidePlayers.toggled) {
                event.setCanceled(true);
            }
        }
    }
}
