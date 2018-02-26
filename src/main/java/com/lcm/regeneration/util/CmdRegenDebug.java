package com.lcm.regeneration.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CmdRegenDebug extends CommandBase {
	
	@Override
	public String getName() {
		return "regdebug";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "/regdebug <actionindex>";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException { //@formatter:off
		for (int i = 0; i < args.length; i++) {
			int c;
			try { c = Integer.parseInt(args[i]); }
			catch (Exception e) { throw new CommandException("Failed to execute debug action " + args[i], e); } //@formatter:on
			
			@SuppressWarnings("unused")
			EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
			
			switch (c) {
				case 0:
					if (FMLCommonHandler.instance().getSide().isClient()) mockSkin();
					break;
				case 1:
					if (FMLCommonHandler.instance().getSide().isClient()) resetSkin();
					break;
				
				default:
					server.sendMessage(new TextComponentString("No debug action defined for " + c));
			}
		}
	}
	
	private static ResourceLocation skin;
	
	private void resetSkin() {
		PlayerUtils.setPlayerTexture(Minecraft.getMinecraft().player, null);
	}
	
	private void mockSkin() {
		PlayerUtils.setPlayerTexture(Minecraft.getMinecraft().player, skin);
	}
	
	public static void initSkin() {
		try {
			//File f = new File("random.jpg");
			//BufferedImage img = ImageIO.read(f);
			
			BufferedImage img = combineImages(
				getLayer("skin"), getLayer("hair"), getLayer("eyes"), getLayer("brows") 
			);
			
			CmdRegenDebug.skin = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("skin", new DynamicTexture(img));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private static BufferedImage getLayer(String name) throws IOException {
		System.out.println(new File("skins/"+name+".png").getAbsolutePath());
		return ImageIO.read(new File("skins/"+name+".png"));
	}
	
	private static BufferedImage combineImages(BufferedImage... img) {
		int w = 0, h = 0;
		for (BufferedImage i : img) { //TODO could probably throw a warning if sizes change
			w = Math.max(w, i.getWidth());
			h = Math.max(h, i.getHeight());
		}
		
		BufferedImage merged = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = merged.getGraphics();
		for (BufferedImage i : img) g.drawImage(i, 0, 0, null);
		return merged;
	}
	
}
