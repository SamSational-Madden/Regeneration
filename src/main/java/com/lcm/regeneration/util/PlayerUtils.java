package com.lcm.regeneration.util;

import java.util.Map;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PlayerUtils {
	
	public static void setWalkSpeed(EntityPlayerMP p, float speed) {
		ReflectionHelper.setPrivateValue(PlayerCapabilities.class, p.capabilities, speed, 6);
	}
	
	public static NetworkPlayerInfo getNetworkPlayerInfo(AbstractClientPlayer player) {
		return ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, 0);
	}
	
	public static Map<Type, ResourceLocation> getSkinMap(AbstractClientPlayer player) {
		NetworkPlayerInfo playerInfo = getNetworkPlayerInfo(player);
		if (playerInfo == null) throw new NullPointerException("No skin-map found for player "+player.getName());
		return ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, 1);
	}
	
	public static void setPlayerTexture(AbstractClientPlayer player, ResourceLocation texture) {
		getSkinMap(player).put(Type.SKIN, texture);
		if (texture == null) ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, getNetworkPlayerInfo(player), false, 4);
	}
	
}
