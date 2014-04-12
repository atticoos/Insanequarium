package com.atticuswhite.insaniquarium;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DefaultExample extends SimpleBaseGameActivity implements IOnSceneTouchListener{
	
	static final int CAMERA_WIDTH = 800;
	static final int CAMERA_HEIGHT = 480;
	
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 0, 0.5f);

	private Scene mScene;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mBoxFaceTextureRegion;
	private TiledTextureRegion mCircleFaceTextureRegion;
	
	private PhysicsWorld mPhysicsWorld;
	
	private int mFaceCount = 0;
	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		Camera mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 128, TextureOptions.BILINEAR);
		this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png", 0, 0, 2, 1);
		this.mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 32, 2, 1);
		
		this.mBitmapTextureAtlas.load();
	}

	@Override
	protected Scene onCreateScene() {
		this.mScene = new Scene();
		this.mScene.setBackground(new Background(0.09804f, 0.6274f, 0));
		this.mScene.setOnSceneTouchListener(this);
		
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
		final Rectangle right = new Rectangle(CAMERA_WIDTH-2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
		final Rectangle shelf = new Rectangle(300, 200, 100, 2, vertexBufferObjectManager);
		
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, shelf, BodyType.StaticBody, wallFixtureDef);
		
		this.mScene.attachChild(ground);
		this.mScene.attachChild(roof);
		this.mScene.attachChild(left);
		this.mScene.attachChild(right);
		this.mScene.attachChild(shelf);
		
		this.mScene.registerUpdateHandler(this.mPhysicsWorld);
		
		return this.mScene;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (this.mPhysicsWorld != null){
			if (pSceneTouchEvent.isActionDown()){
				this.addFace(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				return true;
			}
		}
		return false;
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
		this.mScene.attachChild(face);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(face, body, true, true));
		
	}
}
