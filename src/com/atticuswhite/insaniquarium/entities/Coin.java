package com.atticuswhite.insaniquarium.entities;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.atticuswhite.insaniquarium.GameActivity;
import com.atticuswhite.insaniquarium.GameBitmapFactory;

public class Coin extends AnimatedSprite {
	private final PhysicsHandler mPhysicsHandler;
	private static final float VELOCITY = 50.0f;
	private final int value = 50;
	
	public Coin(final float pX, final float pY){
		super(pX, pY, GameBitmapFactory.getCircleFace(), GameActivity.getInstance().getVertexBufferObjectManager());
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.mPhysicsHandler.setVelocity(0, Coin.VELOCITY);
		this.registerUpdateHandler(this.mPhysicsHandler);
	}
	
	protected void onManagedUpdate(final float pSecondsElpased){
		if (this.mY + this.getWidth() > GameActivity.CAMERA_HEIGHT){
			
		}
		super.onManagedUpdate(pSecondsElpased);
	}
	
	public boolean outOfBounds(){
		return this.mY > GameActivity.CAMERA_HEIGHT;
	}
	
	public int getValue(){
		return this.value;
	}
	
}
