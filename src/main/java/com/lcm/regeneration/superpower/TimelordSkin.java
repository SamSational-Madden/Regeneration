package com.lcm.regeneration.superpower;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.annotation.Generated;
import javax.annotation.concurrent.Immutable;
import javax.imageio.ImageIO;

import net.minecraft.nbt.NBTTagCompound;

@Immutable //technically not 100%, 'compiled' *could possibly* be modified
public final class TimelordSkin {
	public final BufferedImage compiled;
	private final GENDER sex;
	private final HAIRCOLOR hairColor;
	private final boolean hasBeard, hasHeterochemia, isSpecial; //TODO assert sex == MALE || (sex == FEMALE && hasBeard == false)
	private final int iBeard, iBrow, iEyes, iHair, iMouth, iSkin, iSpecial;
	
	public TimelordSkin() throws IOException { //TODO configurable chances
		isSpecial = Math.random() < .1D;
		sex = Math.random() < .5D ? GENDER.MALE : GENDER.FEMALE; //TODO selecting a gender
		hasBeard = sex == GENDER.MALE ? Math.random() < .3D : false;
		hasHeterochemia = Math.random() < .3D;
		
		Random r = new Random();
		iBeard		= r.nextInt(1);
		iBrow		= r.nextInt(7);
		iEyes		= r.nextInt(6);
		iHair		= r.nextInt(11);
		hairColor	= HAIRCOLOR.values()[r.nextInt(HAIRCOLOR.values().length)];
		iMouth		= r.nextInt(1);
		iSkin		= r.nextInt(7);
		iSpecial	= r.nextInt(2);
		
		compiled = compile();
	}
	
		
	public enum HAIRCOLOR {
		BLACK, DARKBLONDE, DARKBROWN, GINGER, GRAY, LIGHTBLONDE, LIGHTBROWN;
		@Override public String toString() { return super.toString().toLowerCase(); }
	}
	
	public enum GENDER {
		MALE, FEMALE;
		@Override public String toString() { return super.toString().toLowerCase(); }
	}
	
	
	public TimelordSkin(NBTTagCompound tag) throws IOException {
		sex				=	tag.getBoolean("isFemale") ? GENDER.FEMALE : GENDER.MALE;
		hasBeard		=	tag.getBoolean("hasBeard");
		hasHeterochemia	=	tag.getBoolean("hasHeterochemia");
		isSpecial		=	tag.getBoolean("isSpecial");
		
		iBeard		=	tag.getInteger("iBeard");
		iBrow		=	tag.getInteger("iBrow");
		iEyes		=	tag.getInteger("iEyes");
		iHair		=	tag.getInteger("iHair");
		hairColor	=	HAIRCOLOR.values()[tag.getInteger("iHairColor")];
		iMouth		=	tag.getInteger("iMouth");
		iSkin		=	tag.getInteger("iSkin");
		iSpecial	=	tag.getInteger("iSpecial");
		
		compiled = compile();
	}
	
	public NBTTagCompound asNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("isFemale", sex == GENDER.FEMALE);
		nbt.setBoolean("hasBeard", hasBeard);
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
		for (BufferedImage i : layers) if (i != null) { //TODO could probably throw a warning if sizes change
			w = Math.max(w, i.getWidth());
			h = Math.max(h, i.getHeight());
		}
		
		BufferedImage merged = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = merged.getGraphics();
		for (BufferedImage i : layers) if (i != null) g.drawImage(i, 0, 0, null);
		return merged;
	}
	
	private static BufferedImage getLayer(String layer, int index) throws IOException {
		return ImageIO.read(TimelordSkin.class.getResourceAsStream("assets/lcm-regen/skins/"+layer+"/"+index+".png"));
	}
	private static BufferedImage getLayer(String layer, Object category, int index) throws IOException {
		return ImageIO.read(TimelordSkin.class.getResourceAsStream("assets/lcm-regen/skins/"+layer+"/"+category.toString()+"/"+index+".png"));
	}
	
	
	private BufferedImage compile() throws IOException {
		if (isSpecial) {
			return ImageIO.read(getClass().getResourceAsStream("assets/lcm-regen/skins/special/"+iSpecial+".png"));
		} else {
			BufferedImage skin = getLayer("skin", sex, iSkin),
							mouth = getLayer("mouth", sex, iMouth),
							hair = getLayer("hair", sex.toString()+"/"+hairColor.toString(), iHair),
							eyes = getLayer("eyes", iEyes),
							brow = getLayer("brow", iBrow),
							beard = getLayer("beard", iBeard);
			return compileLayers(skin, mouth, hair, eyes, brow, beard);
		}
	}


	@Override @Generated("eclipse")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hairColor == null) ? 0 : hairColor.hashCode());
		result = prime * result + (hasBeard ? 1231 : 1237);
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
		if (hasBeard != other.hasBeard) return false;
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
}
