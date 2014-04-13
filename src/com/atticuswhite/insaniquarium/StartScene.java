package com.atticuswhite.insaniquarium;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import android.util.Log;

public class StartScene implements IOnSceneTouchListener, IOnAreaTouchListener {
	
	private Scene scene;
	
	public StartScene(){
		this.scene = new Scene();
		
		
		final Rectangle rect = new Rectangle(
			100,
			100,
			300,
			200,
			GameActivity.getInstance().getVertexBufferObjectManager()
		);
		rect.setColor(0,0,1);
		
		final Rectangle startButton = new Rectangle(10, 10, 140, 20, GameActivity.getInstance().getVertexBufferObjectManager()){
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY){
				Log.v("HELLO", "WORLD");
				return true;
			}
		};
		
		final Text startText = new Text(5, 5, GameActivity.getInstance().getGameFonts().getSmallFont(), "Start Game", "Start Game".length(), GameActivity.getInstance().getVertexBufferObjectManager());
		startText.setColor(0,0,0);
		startButton.attachChild(startText);
		rect.attachChild(startButton);
		
		this.scene.attachChild(rect);
		
		
		
	}
	
	
	public Scene getScene(){
		return this.scene;
	}


	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}


}
