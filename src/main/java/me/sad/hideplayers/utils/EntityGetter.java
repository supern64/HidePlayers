package me.sad.hideplayers.utils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;

public class EntityGetter {
    public static EntityLivingBase getRenderEntity(RenderLivingEvent<EntityLivingBase> event) {
        return event.entity;
    }
}
