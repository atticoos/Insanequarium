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

public class MainActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener, IOnAreaTouchListener, GameEventListener{
	
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
	public static final int CONTROL_HEIGHT = 100;
	
	private static final int LAYER_COUNT = 4;
	private static final int LAYER_BACKGROUND = 0;
	private static final int LAYER_FISH = LAYER_BACKGROUND + 1;
	private static final int LAYER_CONTROLS = LAYER_FISH + 1;
	private static final int LAYER_SCORE = LAYER_CONTROLS + 1;
	
	
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0, 0.5f);

	private Scene mScene;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mBoxFaceTextureRegion;
	private TiledTextureRegion mCircleFaceTextureRegion;
	
	
	private List<AnimatedSprite> spriteGroup = new ArrayList<AnimatedSprite>();
	private List<Coin> coins = new ArrayList<Coin>();
	private List<Fish> fishies = new ArrayList<Fish>();
	
	private PhysicsWorld mPhysicsWorld;
	
	private int mFaceCount = 0;
	private long startTime;
	private long currentTime;
	private Long lastCoin;
	
	private Font mFont;
	private ControlArea mControlArea;
	private ScoreManager mScoreManager;
	private GameEventManager mGameEventManager;
	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		Camera mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
	}

	@Override
	protected void onCreateResources() {
		
		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 512, 512, TextureOptions.BILINEAR, this.getAssets(), "Droid.ttf", 32, true, Color.WHITE);
		this.mFont.load();
		
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 128, TextureOptions.BILINEAR);
		this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png", 0, 0, 2, 1);
		this.mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 32, 2, 1);
		
		this.mBitmapTextureAtlas.load();
	}

	@Override
	protected Scene onCreateScene() {
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		this.startTime = System.currentTimeMillis();
		this.mScene = new Scene();
		
		
		
		for (int i=0; i<LAYER_COUNT; i++){
			this.mScene.attachChild(new Entity());
		}
		
		
		this.mScene.setBackground(new Background(0.09804f, 0.6274f, 0));
		this.mScene.setOnSceneTouchListener(this);
		this.mScene.setOnAreaTouchListener(this);
		
		this.mGameEventManager = new GameEventManager();
		this.mGameEventManager.addListener(this);
		
		/* Control Area */
		this.mControlArea = new ControlArea(this.mGameEventManager, this.mFont, this.getVertexBufferObjectManager());
		
		this.mScoreManager = new ScoreManager(this.mFont, this.getVertexBufferObjectManager());
		this.mControlArea.attachChild(this.mScoreManager.getEntity());
		this.mControlArea.registerTouchAreas(this.mScene);
		this.mScene.getChildByIndex(LAYER_CONTROLS).attachChild(this.mControlArea);
		
		
		
		
		
		
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		
		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, CONTROL_HEIGHT, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, CONTROL_HEIGHT, 2, CAMERA_HEIGHT - CONTROL_HEIGHT, vertexBufferObjectManager);
		final Rectangle right = new Rectangle(CAMERA_WIDTH-2, CONTROL_HEIGHT, 2, CAMERA_HEIGHT - CONTROL_HEIGHT, vertexBufferObjectManager);
		final Rectangle shelf = new Rectangle(300, CONTROL_HEIGHT + 200, 100, 2, vertexBufferObjectManager);
		
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, shelf, BodyType.StaticBody, wallFixtureDef);
		
		this.mScene.getChildByIndex(LAYER_BACKGROUND).attachChild(ground);
		this.mScene.getChildByIndex(LAYER_BACKGROUND).attachChild(roof);
		this.mScene.getChildByIndex(LAYER_BACKGROUND).attachChild(left);
		this.mScene.getChildByIndex(LAYER_BACKGROUND).attachChild(right);
		this.mScene.getChildByIndex(LAYER_BACKGROUND).attachChild(shelf);
		
		this.mScene.registerUpdateHandler(this.mPhysicsWorld);
		
		
		
		//final Fish fish = new Fish(100,100, this.mCircleFaceTextureRegion, this.getVertexBufferObjectManager());
		//this.mScene.attachChild(fish);
		
		this.mScene.registerUpdateHandler(new IUpdateHandler(){
			final EngineLock engineLock = mEngine.getEngineLock();
			boolean removing = false;
			
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				currentTime = System.currentTimeMillis();
				
				
				
				Iterator<Coin> coinIter = coins.iterator();
				while(coinIter.hasNext()){
					Coin coin = coinIter.next();
					if (coin.outOfBounds()){
						Log.v("COIN", "Out of bounds, removing");
						mScene.getChildByIndex(LAYER_CONTROLS).detachChild(coin);
						coinIter.remove();
					}
				}
				
				if (lastCoin == null || ( (currentTime / 1000) - (lastCoin / 1000)) > 5){
					lastCoin = currentTime;
					float pX = (float) ((Math.random() * (CAMERA_WIDTH - 10f) + 10f));
					addCoin(pX);
				}
				
				
				/*
				if (fish.collidesWith(ground)){
					ground.setColor(1f, 0f, 0f);
				} else {
					//ground.setColor(0, 1, 0f);
				}
				*/
			}

			@Override
			public void reset() { }
			
		});
		
		return this.mScene;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (this.mPhysicsWorld != null){
			if (pSceneTouchEvent.isActionDown()){
				//this.addFace(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				//this.addCoin(pSceneTouchEvent.getX());
				return true;
			}
		}
		return false;
	}


	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if (pSceneTouchEvent.isActionDown()){
			if (pTouchArea instanceof Coin){
				Log.v("COIN", "Eating coin");
				this.eatCoin((Coin) pTouchArea);
			} else if (pTouchArea instanceof ControlTouchArea){
				Log.v("ControlAreaSection", "Touched");
				((ControlAreaSection)((ControlTouchArea) pTouchArea ).getParent()).touchedArea();
			}
			//this.removeFace((AnimatedSprite) pTouchArea);
			return true;
		}
		return false;
	}
	
	
	private void addCoin(final float pX){
		final float pY = CONTROL_HEIGHT;
		final Coin coin = new Coin(pX, pY, this.mCircleFaceTextureRegion, this.getVertexBufferObjectManager());
		this.coins.add(coin);
		this.mScene.registerTouchArea(coin);
		this.mScene.getChildByIndex(LAYER_CONTROLS).attachChild(coin);
	}
	
	private void eatCoin(Coin coin){
		this.mScene.unregisterTouchArea(coin);
		this.mScene.getChildByIndex(LAYER_CONTROLS).detachChild(coin);
		this.mScoreManager.updateScore(coin.getValue());
	}
	
	


	@Override
	public void handleGameEvent(GameEvent event) {
		switch(event){
			case CREATE_FISH:
				Log.v("FISH", "CREATE FISH");
				if (this.mScoreManager.getScore() - Fish.COST > 0){
					this.addFish();
				}
				break;
		}
	}
	
	
	
	private void addFish(){
		final float pX = (float) Math.random() * CAMERA_WIDTH;
		final float pY = (float) (Math.random() * (CAMERA_HEIGHT - CONTROL_HEIGHT)) + CONTROL_HEIGHT;
		boolean direction = true;
		
		if (Math.random() * 2 > 1){
			direction = false;
		}
		
		Fish fish = new Fish(pX, pY, this.mBoxFaceTextureRegion, this.getVertexBufferObjectManager());
		fish.setDirection(direction);
		fish.animate(200);
		this.fishies.add(fish);
		this.mScene.registerTouchArea(fish);
		this.mScene.attachChild(fish);
		
		this.mScoreManager.updateScore( -Fish.COST );
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void addFace(final float pX, final float pY){
		this.mFaceCount++;
		final AnimatedSprite face;
		final Body body;
		
		if (mFaceCount % 2 == 0){
			face = new AnimatedSprite(pX, pY, this.mBoxFaceTextureRegion, this.getVertexBufferObjectManager());
			body = PhysicsFactory.createBoxBody(this.mPhysicsWorld, face, BodyType.DynamicBody, FIXTURE_DEF);
		} else {
			face = new AnimatedSprite(pX, pY, this.mCircleFaceTextureRegion, this.getVertexBufferObjectManager());
			body = PhysicsFactory.createCircleBody(this.mPhysicsWorld, face, BodyType.DynamicBody, FIXTURE_DEF);
		}
		face.animate(200);
		this.mScene.registerTouchArea(face);
		this.mScene.attachChild(face);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body, true, true));
		
		this.spriteGroup.add(face);
	}
	
	private void removeFace(final AnimatedSprite face){
		final PhysicsConnector facePhysicsConnector = this.mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(face);
		
		this.mPhysicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
		this.mPhysicsWorld.destroyBody(facePhysicsConnector.getBody());
		
		this.mScene.unregisterTouchArea(face);
		this.mScene.detachChild(face);
		
		System.gc();
	}


}
