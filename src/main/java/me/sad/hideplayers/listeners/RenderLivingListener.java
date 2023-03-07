package me.sad.hideplayers.listeners;

import me.sad.hideplayers.HidePlayers;
import me.sad.hideplayers.utils.EntityGetter;
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
    public void onRenderEntity(RenderLivingEvent.Pre<EntityLivingBase> event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        NetHandlerPlayClient connection = minecraft.getNetHandler();
        EntityLivingBase entity = EntityGetter.getRenderEntity(event);


        if (entity instanceof EntityOtherPlayerMP && !HidePlayers.toggled) {
            if (entity.getName().matches("^[a-zA-Z0-9_]*$") && entity.getUniqueID().version() != 2 && !HidePlayers.players.contains(entity.getName().toLowerCase())) { // hypixel marks npc uuids as v2
                entity.setInvisible(true);
                event.setCanceled(true);
            }
        }
        if (entity instanceof EntityArmorStand) {
            if (!HidePlayers.armorStandCache.containsKey(entity.getPersistentID())) {
                HidePlayers.armorStandCache.put(entity.getPersistentID(), null);
                if (entity.hasCustomName() && !entity.getCustomNameTag().toLowerCase().contains(minecraft.thePlayer.getName().toLowerCase())) {
                    List<String> playerList = connection.getPlayerInfoMap().stream().map(NetworkPlayerInfo::getGameProfile).map(gameProfile -> gameProfile.getName()).collect(Collectors.toList());
                    playerList.forEach(p -> {
                        if (entity.getCustomNameTag().toLowerCase().contains(p.toLowerCase())) {
                            HidePlayers.armorStandCache.put(entity.getPersistentID(), p);
                        }
                    });
                }
            }
            String armorStandOwner = HidePlayers.armorStandCache.get(entity.getPersistentID());
            if (armorStandOwner != null && !HidePlayers.players.contains(armorStandOwner.toLowerCase()) && !HidePlayers.toggled) {
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public void afterRenderEntity(RenderLivingEvent.Post<EntityLivingBase> event) {
        EntityLivingBase entity = EntityGetter.getRenderEntity(event);
        if (entity instanceof EntityOtherPlayerMP) entity.setInvisible(false); // armorstands can do their own thing idgaf
    }
}
