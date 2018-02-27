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
import scala.xml.MalformedAttributeException;

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
		
		compiled = compile();
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
			throw new MalformedAttributeException("Something went wrong while (de)serializing: hasHeterochemia but iEyes != 0");
		if (iEyes == 0 && !hasHeterochemia)
			throw new MalformedAttributeException("Something went wrong while (de)serializing: !hasHeterochemia but iEyes == 0");
		compiled = compile();
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
	
	private BufferedImage compile() throws IOException {
		if (isSpecial) {
			return ImageIO.read(getInternalFileStream("skins/special/"+iSpecial+".png"));
		} else {
			BufferedImage skin = getLayer("skin", sex, iSkin),
							mouth = getLayer("mouth", sex, iMouth),
							hair = getLayer("hair", sex.toString()+"/"+hairColor.toString(), iHair),
							eyes = getLayer("eyes", iEyes),
							brow = getLayer("brow", iBrow),
							beard = getLayer("beard", iBeard);
			return compileLayers(skin, mouth, hair, eyes, brow);//, beard);
		}
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
	
}
