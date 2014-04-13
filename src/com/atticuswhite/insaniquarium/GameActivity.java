package com.atticuswhite.insaniquarium;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.andengine.engine.Engine;
import org.andengine.engine.Engine.EngineLock;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import com.atticuswhite.insaniquarium.controlarea.ControlArea;
import com.atticuswhite.insaniquarium.controlarea.ControlAreaSection;
import com.atticuswhite.insaniquarium.controlarea.ControlTouchArea;
import com.atticuswhite.insaniquarium.controlarea.ScoreManager;
import com.atticuswhite.insaniquarium.entities.Coin;
import com.atticuswhite.insaniquarium.entities.Fish;
import com.atticuswhite.insaniquarium.entities.Food;
import com.atticuswhite.insaniquarium.entities.Monster;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
/* http://stuartmct.co.uk/2012/07/16/andengine-scenes-and-scene-management/ */
public class GameActivity extends SimpleBaseGameActivity {
	
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
	public static final int CONTROL_HEIGHT = 100;
	
	private static volatile GameActivity instance = null;
	
	
	private GameState mGameState;
	private GameFonts mGameFonts;
	private GameBitmaps mGameBitmaps;
	
	
	private GameScene mGameScene;
	private StartScene mStartScene;
	
	
	
	@Override
	protected void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		instance = this;
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		Camera mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
	}

	@Override
	protected void onCreateResources() {
		this.mGameFonts = new GameFonts();
		this.mGameBitmaps = new GameBitmaps();
		this.mGameBitmaps.generateBitmapTextures();
	}
	
	public static synchronized GameActivity getInstance(){
		return instance;
	}

	@Override
	protected Scene onCreateScene() {
		this.mGameState = GameState.MENU_STATE;
		
		this.mStartScene = new StartScene();
		this.mGameScene = new GameScene();
		return this.mStartScene.getScene();
		
	}

	public void updateGameState(GameState state){
		
		switch(state) {
			case MENU_STATE:
				this.mEngine.setScene(this.mStartScene.getScene());
				break;
			case GAME_STATE:
				this.mEngine.setScene(this.mGameScene.getScene());
				break;
		}
		
	}
	
	public GameFonts getGameFonts(){
		return this.mGameFonts;
	}
	
	public GameBitmaps getGameBitmaps(){
		return this.mGameBitmaps;
	}


}
