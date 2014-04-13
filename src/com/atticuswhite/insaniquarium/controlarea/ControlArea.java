package com.atticuswhite.insaniquarium.controlarea;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.atticuswhite.insaniquarium.GameEventManager;
import com.atticuswhite.insaniquarium.GameFonts;
import com.atticuswhite.insaniquarium.GameActivity;

public class ControlArea extends Entity {
	final private ControlAreaSection fishSection;
	final private ControlAreaSection scoreSection;
	
	
	public ControlArea(GameEventManager gameEventManager){
		VertexBufferObjectManager mVertexBufferObjectManager = GameActivity.getInstance().getVertexBufferObjectManager();
		GameFonts font = GameActivity.getInstance().getGameFonts();
		
		
		final Rectangle scoreBackground = new Rectangle(0, 0, GameActivity.CAMERA_WIDTH, 100, mVertexBufferObjectManager);
		scoreBackground.setColor(0, 0, 0);
		
		this.attachChild(scoreBackground);
		
		fishSection = new ControlAreaSection("Fish", 10, gameEventManager);
		scoreSection = null; //new ControlAreaSection("Score", mVertexBufferObjectManager);
		
		
		
		this.attachChild(fishSection);
	}
	
	
	
	public void registerTouchAreas(Scene scene){
		scene.registerTouchArea(this.fishSection.getTouchArea());
	}
	
	
}
