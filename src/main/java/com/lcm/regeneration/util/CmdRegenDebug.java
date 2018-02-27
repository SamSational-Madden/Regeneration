package com.lcm.regeneration.util;

import com.lcm.regeneration.superpower.TimelordSuperpowerHandler;

import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
					//if (FMLCommonHandler.instance().getSide().isClient()) mockSkin();
					break;
				case 1:
					//if (FMLCommonHandler.instance().getSide().isClient()) resetSkin();
					break;
				case 2:
					TimelordSuperpowerHandler.randomize(SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordSuperpowerHandler.class));
					break;
				
				default:
					server.sendMessage(new TextComponentString("No debug action defined for " + c));
			}
		}
	}
	
}
