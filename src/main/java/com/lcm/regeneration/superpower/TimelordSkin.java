package com.lcm.regeneration.superpower;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.annotation.Generated;
import javax.annotation.concurrent.Immutable;
import javax.imageio.ImageIO;

import com.lcm.regeneration.Regeneration;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Immutable //technically not 100%, 'compiled' *could possibly* be modified
public final class TimelordSkin {
	public final BufferedImage compiled;
	private final GENDER sex;
	private final HAIRCOLOR hairColor;
	private final boolean hasHeterochemia, isSpecial;//, hasBeard; 
	private final int iBeard, iBrow, iEyes, iHair, iMouth, iSkin, iSpecial;
	
	public TimelordSkin() throws IOException { //TODO configurable chances
		isSpecial = Math.random() < .01D;
		//sex = Math.random() < .5D ? GENDER.MALE : GENDER.FEMALE; //TODO selecting a gender
		sex = GENDER.MALE;
		//hasBeard = sex == GENDER.MALE ? Math.random() < .3D : false;
		hasHeterochemia = Math.random() < .01D;
		
		Random r = new Random();
		//iBeard	= r.nextInt(1);
		iBeard		= 1;
		iBrow		= r.nextInt(6)+1;
		iEyes		= hasHeterochemia ? 0 : r.nextInt(5)+1;
		iHair		= r.nextInt(10)+1;
		hairColor	= HAIRCOLOR.values()[r.nextInt(HAIRCOLOR.values().length)];
		//iMouth	= r.nextInt(1);
		iMouth		= 1;
		iSkin		= r.nextInt(6)+1;
		iSpecial	= r.nextInt(1)+1;
		
		compiled = compile(this);
	}
	
	
	public enum HAIRCOLOR {
		BLACK, DARKBLONDE, DARKBROWN, GINGER, GREY, LIGHTBLONDE, LIGHTBROWN;
		@Override public String toString() { return super.toString().toLowerCase(); }
	}
	
	public enum GENDER {
		MALE, FEMALE;
		@Override public String toString() { return super.toString().toLowerCase(); }
	}
	
	
	public TimelordSkin(NBTTagCompound tag) throws IOException {
		NBTTagCompound nbt = tag.getSize() == 0 ? new TimelordSkin().asNBT() : tag;
		
		sex				=	nbt.getBoolean("isFemale") ? GENDER.FEMALE : GENDER.MALE;
		//hasBeard		=	nbt.getBoolean("hasBeard");
		hasHeterochemia	=	nbt.getBoolean("hasHeterochemia");
		isSpecial		=	nbt.getBoolean("isSpecial");
		
		iBeard		=	nbt.getInteger("iBeard");
		iBrow		=	nbt.getInteger("iBrow");
		iEyes		=	nbt.getInteger("iEyes");
		iHair		=	nbt.getInteger("iHair");
		hairColor	=	HAIRCOLOR.values()[nbt.getInteger("iHairColor")];
		iMouth		=	nbt.getInteger("iMouth");
		iSkin		=	nbt.getInteger("iSkin");
		iSpecial	=	nbt.getInteger("iSpecial");
		
		if (hasHeterochemia && iEyes != 0)
			throw new IllegalStateException("Something went wrong while (de)serializing: hasHeterochemia but iEyes != 0\n"+nbt.toString());
		if (iEyes == 0 && !hasHeterochemia)
			throw new IllegalStateException("Something went wrong while (de)serializing: !hasHeterochemia but iEyes == 0\n"+nbt.toString());
		
		compiled = compile(this);
	}
	
	/** Fallback skin */
	public TimelordSkin(boolean ignored) throws IOException {
		sex = GENDER.FEMALE;
		hairColor = HAIRCOLOR.GINGER;
		hasHeterochemia = false;
		isSpecial = true; 
		iBeard = -1;
		iBrow = -1;
		iEyes = -1;
		iHair = -1;
		iMouth = -1;
		iSkin = -1;
		iSpecial = 0;
		compiled = compile(this); //null
		if (compiled != null) throw new IllegalStateException("compiled != null with the fallback constructor");
	}
	
	public NBTTagCompound asNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("isFemale", sex == GENDER.FEMALE);
		//nbt.setBoolean("hasBeard", hasBeard);
		nbt.setBoolean("hasHeterochemia", hasHeterochemia);
		nbt.setBoolean("isSpecial", isSpecial);
		
		nbt.setInteger("iBeard", iBeard);
		nbt.setInteger("iBrow", iBrow);
		nbt.setInteger("iEyes", iEyes);
		nbt.setInteger("iHair", iHair);
		nbt.setInteger("iHairColor", hairColor.ordinal());
		nbt.setInteger("iMouth", iMouth);
		nbt.setInteger("iSkin", iSkin);
		nbt.setInteger("iSpecial", iSpecial);
		return nbt;
	}
	
	
	private static BufferedImage compileLayers(BufferedImage... layers) {
		if (layers.length == 0) return null; //this may be bad but I'm lazy
		
		int w = 0, h = 0;
		for (BufferedImage i : layers) if (i != null) {
			if ((w != 0 && h != 0) && (i.getWidth() != w || i.getHeight() != h)) System.out.println("WARNING: SKIN LAYERS ARE NOT A UNIFORM SIZE");
			w = Math.max(w, i.getWidth());
			h = Math.max(h, i.getHeight());
		}
		
		BufferedImage merged = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = merged.getGraphics();
		for (BufferedImage i : layers) if (i != null) g.drawImage(i, 0, 0, null);
		return merged;
	}
	
	private static InputStream getInternalFileStream(String name) throws IOException {
		return Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(Regeneration.MODID+":"+name)).getInputStream();
	}
	
	private static BufferedImage getLayer(String layer, int index) throws IOException {
		return ImageIO.read(getInternalFileStream("skins/"+layer+"/"+index+".png"));
	}
	private static BufferedImage getLayer(String layer, Object category, int index) throws IOException {
		return ImageIO.read(getInternalFileStream("skins/"+layer+"/"+category.toString()+"/"+index+".png"));
	}
	
	@SideOnly(Side.CLIENT)
	public static BufferedImage compile(TimelordSkin skin) throws IOException {
		if (skin.isSpecial) {
			if (skin.iSpecial == 0) return null;
			return ImageIO.read(getInternalFileStream("skins/special/"+skin.iSpecial+".png"));
		} else {
			BufferedImage lSkin = getLayer("skin", skin.sex, skin.iSkin),
							mouth = getLayer("mouth", skin.sex, skin.iMouth),
							hair = getLayer("hair", skin.sex.toString()+"/"+skin.hairColor.toString(), skin.iHair),
							eyes = getLayer("eyes", skin.iEyes),
							brow = getLayer("brow", skin.iBrow),
							beard = getLayer("beard", skin.iBeard);
			return compileLayers(lSkin, mouth, hair, eyes, brow);//, beard);
		}
	}
	
	/** Copy the passed TimelordSkin and compile it */
	@SideOnly(Side.CLIENT)
	public TimelordSkin(TimelordSkin skin) throws IOException {
		sex				=	skin.sex;
		//hasBeard		=	skin.hasBeard;
		hasHeterochemia	=	skin.hasHeterochemia;
		isSpecial		=	skin.isSpecial;
		
		iBeard		=	skin.iBeard;
		iBrow		=	skin.iBrow;
		iEyes		=	skin.iEyes;
		iHair		=	skin.iHair;
		hairColor	=	skin.hairColor;
		iMouth		=	skin.iMouth;
		iSkin		=	skin.iSkin;
		iSpecial	=	skin.iSpecial;
		
		compiled = compile(this);
	}

	
	@Override @Generated("eclipse")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hairColor == null) ? 0 : hairColor.hashCode());
		result = prime * result + (hasHeterochemia ? 1231 : 1237);
		result = prime * result + iBeard;
		result = prime * result + iBrow;
		result = prime * result + iEyes;
		result = prime * result + iHair;
		result = prime * result + iMouth;
		result = prime * result + iSkin;
		result = prime * result + iSpecial;
		result = prime * result + (isSpecial ? 1231 : 1237);
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		return result;
	}

	@Override @Generated("eclipse")
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof TimelordSkin)) return false;
		TimelordSkin other = (TimelordSkin) obj;
		if (hairColor != other.hairColor) return false;
		if (hasHeterochemia != other.hasHeterochemia) return false;
		if (iBeard != other.iBeard) return false;
		if (iBrow != other.iBrow) return false;
		if (iEyes != other.iEyes) return false;
		if (iHair != other.iHair) return false;
		if (iMouth != other.iMouth) return false;
		if (iSkin != other.iSkin) return false;
		if (iSpecial != other.iSpecial) return false;
		if (isSpecial != other.isSpecial) return false;
		if (sex != other.sex) return false;
		return true;
	}

	@Override @Generated("eclipse")
	public String toString() {
		return "{sex=" + sex + ", hairColor=" + hairColor + ", hasHeterochemia=" + hasHeterochemia + ", isSpecial=" + isSpecial + ", iBeard=" + iBeard + ", iBrow=" + iBrow + ", iEyes=" + iEyes + ", iHair=" + iHair + ", iMouth=" + iMouth + ", iSkin=" + iSkin + ", iSpecial=" + iSpecial + "}";
	}
	
}
