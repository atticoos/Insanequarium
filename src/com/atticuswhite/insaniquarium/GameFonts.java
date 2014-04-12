package com.atticuswhite.insaniquarium;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.ui.activity.BaseGameActivity;

import android.graphics.Color;

public class GameFonts {
	private Font large;
	private Font medium;
	private Font small;
	
	
	public GameFonts(BaseGameActivity activity){
		FontFactory.setAssetBasePath("font/");
		this.large = FontFactory.createFromAsset(activity.getFontManager(), activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR, activity.getAssets(), "Droid.ttf", 32, true, Color.WHITE);
		this.medium = FontFactory.createFromAsset(activity.getFontManager(), activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR, activity.getAssets(), "Droid.ttf", 22, true, Color.WHITE);
		this.small = FontFactory.createFromAsset(activity.getFontManager(), activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR, activity.getAssets(), "Droid.ttf", 12, true, Color.WHITE);
		
		this.large.load();
		this.medium.load();
		this.small.load();
	}
	
	public Font getLargeFont(){
		return this.large;
	}
	
	public Font getMediumFont(){
		return this.medium;
	}
	
	public Font getSmallFont(){
		return this.small;
	}
	
	
}
