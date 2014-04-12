package com.atticuswhite.insaniquarium.controlarea;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.atticuswhite.insaniquarium.GameEventManager;

public class ControlAreaSection extends Entity {
	static final float WIDTH = 100.0f;
	static final float HEIGHT = 65.0f;
	static final float TOP = 10.0f;
	
	
	private final VertexBufferObjectManager mVertexBufferObjectManager;
	private final Font font;
	private final String name;
	private final GameEventManager mGameEventManager;
	
	private final ControlTouchArea background;
	private final Text title;
	
	
	public ControlAreaSection (String name, float pX, GameEventManager gameEventManager, Font font, VertexBufferObjectManager mVertexBufferObjectManager){
		this.mVertexBufferObjectManager = mVertexBufferObjectManager;
		this.font = font;
		this.name = name;
		this.mGameEventManager = gameEventManager;
		
		
		this.background = new ControlTouchArea(this, 0, 0, WIDTH, HEIGHT, mVertexBufferObjectManager);
		this.title = new Text(10, 5, font, name, name.length(), mVertexBufferObjectManager);
		this.title.setColor(0,0,0);
		
		
		
		
		
		this.attachChild(background);
		this.attachChild(title);
	}
	
	public ControlTouchArea getTouchArea(){
		return background;
	}
	
	public void touchedArea(){
		this.mGameEventManager.createFish();
	}

}
