package me.sad.hideplayers.mixins;

import me.sad.hideplayers.HidePlayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderManager.class)
public abstract class MixinRenderManager {
    @Inject(at=@At("HEAD"), method = "shouldRender", cancellable = true)
    public void shouldRender(Entity entityIn, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        Minecraft minecraft = Minecraft.getMinecraft();
        NetHandlerPlayClient connection = minecraft.getNetHandler();

        if (entityIn instanceof EntityOtherPlayerMP && !HidePlayers.toggled) {
            if (entityIn.getName().matches("^[a-zA-Z0-9_]*$") && entityIn.getUniqueID().version() != 2 && !HidePlayers.players.contains(entityIn.getName().toLowerCase())) { // hypixel marks npc uuids as v2
                cir.setReturnValue(false);
            }
        }
        if (entityIn instanceof EntityArmorStand) {
            if (!HidePlayers.armorStandCache.containsKey(entityIn.getPersistentID())) {
                HidePlayers.armorStandCache.put(entityIn.getPersistentID(), null);
                if (entityIn.hasCustomName() && !entityIn.getCustomNameTag().toLowerCase().contains(minecraft.thePlayer.getName().toLowerCase())) {
                    List<String> playerList = connection.getPlayerInfoMap().stream().map(NetworkPlayerInfo::getGameProfile).map(gameProfile -> gameProfile.getName()).collect(Collectors.toList());
                    playerList.forEach(p -> {
                        if (entityIn.getCustomNameTag().toLowerCase().contains(p.toLowerCase())) {
                            HidePlayers.armorStandCache.put(entityIn.getPersistentID(), p);
                        }
                    });
                }
            }
            String armorStandOwner = HidePlayers.armorStandCache.get(entityIn.getPersistentID());
            if (armorStandOwner != null && !HidePlayers.players.contains(armorStandOwner.toLowerCase()) && !HidePlayers.toggled) {
                cir.setReturnValue(false);
            }
        }
        
    }
}
