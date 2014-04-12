package com.atticuswhite.insaniquarium.entities;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.atticuswhite.insaniquarium.MainActivity;

public class Fish extends AnimatedSprite {
	public static final int COST = 50;
	
	private final PhysicsHandler mPhysicsHandler;
	private final float velocityX = 100.0f;
	private final float velocityY = 15.0f;
	
	

	public Fish(final float pX, final float pY, final TiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager){
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		this.mPhysicsHandler.setVelocity(this.velocityX, this.velocityY);
	}
	
	public void setDirection( boolean dir){
		if (dir){
			this.mPhysicsHandler.setVelocityX(this.velocityX);
		} else {
			this.mPhysicsHandler.setVelocityX(-this.velocityX);
		}
	}
	
	protected void onManagedUpdate(final float pSecondsElapsed){
		if(this.mX < 0){
			this.mPhysicsHandler.setVelocityX(this.velocityX);
		} else if (this.mX + this.getWidth() > MainActivity.CAMERA_WIDTH){
			this.mPhysicsHandler.setVelocityX(-this.velocityX);
		}
		
		if (this.mY < MainActivity.CONTROL_HEIGHT){
			this.mPhysicsHandler.setVelocityY(this.velocityY);
		} else if (this.mY + this.getWidth() > MainActivity.CAMERA_HEIGHT){
			this.mPhysicsHandler.setVelocityY(-this.velocityY);
		}
		
		super.onManagedUpdate(pSecondsElapsed);
	}
}
