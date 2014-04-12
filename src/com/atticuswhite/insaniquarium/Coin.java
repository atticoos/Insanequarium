package com.atticuswhite.insaniquarium;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Coin extends AnimatedSprite {
	private final PhysicsHandler mPhysicsHandler;
	private static final float VELOCITY = 50.0f;
	private final int value = 50;
	
	public Coin(final float pX, final float pY, final TiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager){
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		this.mPhysicsHandler.setVelocity(0, Coin.VELOCITY);
	}
	
	protected void onManagedUpdate(final float pSecondsElpased){
		if (this.mY + this.getWidth() > MainActivity.CAMERA_HEIGHT){
			
		}
		super.onManagedUpdate(pSecondsElpased);
	}
	
	public boolean outOfBounds(){
		return this.mY > MainActivity.CAMERA_HEIGHT;
	}
	
	public int getValue(){
		return this.value;
	}
	
}


/*
private static class Fish extends AnimatedSprite{
private final PhysicsHandler mPhysicsHandler;
private final float velocity = 100.0f;

public Fish(final float pX, final float pY, final TiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager){
	super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	this.mPhysicsHandler = new PhysicsHandler(this);
	this.registerUpdateHandler(this.mPhysicsHandler);
	this.mPhysicsHandler.setVelocity(this.velocity, this.velocity);
}


protected void onManagedUpdate(final float pSecondsElapsed){
	if(this.mX < 0){
		this.mPhysicsHandler.setVelocityX(this.velocity);
	} else if (this.mX + this.getWidth() > MainActivity.CAMERA_WIDTH){
		this.mPhysicsHandler.setVelocityX(-this.velocity);
	}
	
	if (this.mY < 0){
		this.mPhysicsHandler.setVelocityY(this.velocity);
	} else if (this.mY + this.getWidth() > MainActivity.CAMERA_HEIGHT){
		this.mPhysicsHandler.setVelocityY(-this.velocity);
	}
	
	super.onManagedUpdate(pSecondsElapsed);
}
*/