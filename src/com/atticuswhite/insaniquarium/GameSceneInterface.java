package com.atticuswhite.insaniquarium;

import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public interface GameSceneInterface {
	public boolean handleTouchEvent(TouchEvent pSceneTouchEvent);
	public boolean handleAreaTouchEvent(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY);
	public Scene getScene();
}
