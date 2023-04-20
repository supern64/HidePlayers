package me.sad.hideplayers.listeners;

import com.mojang.authlib.GameProfile;
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

        List<String> playerList = connection.getPlayerInfoMap().stream().map(NetworkPlayerInfo::getGameProfile).map(GameProfile::getName).collect(Collectors.toList());
        // TODO: fix essential's EmulatedPlayerBuilder
        if (entity instanceof EntityOtherPlayerMP && !HidePlayers.toggled) {
            if (entity.getName().matches("^[a-zA-Z0-9_]{1,16}$") && entity.getUniqueID().version() != 2) { // hypixel marks npc uuids as v2
                if (HidePlayers.mode == HidePlayers.Mode.WHITELIST && !HidePlayers.players.contains(entity.getName().toLowerCase())) {
                    event.setCanceled(true);
                    minecraft.getRenderManager().setRenderShadow(false);
                } else if (HidePlayers.mode == HidePlayers.Mode.RADIUS && minecraft.thePlayer.getDistanceSqToEntity(entity) < HidePlayers.range) {
                    event.setCanceled(true);
                    minecraft.getRenderManager().setRenderShadow(false);
                }
            }
        }
        if (entity instanceof EntityArmorStand) {
            if (!HidePlayers.armorStandCache.containsKey(entity.getPersistentID())) {
                HidePlayers.armorStandCache.put(entity.getPersistentID(), null);
                if (entity.hasCustomName() && !entity.getCustomNameTag().toLowerCase().contains(minecraft.thePlayer.getName().toLowerCase())) {
                    playerList.forEach(p -> {
                        if (entity.getCustomNameTag().toLowerCase().contains(p.toLowerCase())) {
                            HidePlayers.armorStandCache.put(entity.getPersistentID(), p);
                        }
                    });
                }
            }
            String armorStandOwner = HidePlayers.armorStandCache.get(entity.getPersistentID());
            if (armorStandOwner != null && !HidePlayers.toggled) {
                if (HidePlayers.mode == HidePlayers.Mode.WHITELIST && !HidePlayers.players.contains(armorStandOwner.toLowerCase())) {
                    event.setCanceled(true);
                } else if (HidePlayers.mode == HidePlayers.Mode.RADIUS && minecraft.thePlayer.getDistanceSqToEntity(entity) < HidePlayers.range + 0.25D) {
                    event.setCanceled(true);
                }
            }

        }
    }
    @SubscribeEvent
    public void afterRenderEntity(RenderLivingEvent.Post<EntityLivingBase> event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.getRenderManager().setRenderShadow(minecraft.gameSettings.entityShadows);
    }
}
