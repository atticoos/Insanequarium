package com.atticuswhite.insaniquarium.entities;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.atticuswhite.insaniquarium.GameActivity;
import com.atticuswhite.insaniquarium.GameBitmapFactory;

public class Fish extends AggressiveEntity {
	public static final int COST = 50;
	
	

	public Fish(final float pX, final float pY){
		super(pX, pY, GameBitmapFactory.getBoxFace());
		this.velocityX = 100.0f;
		this.velocityY = 15.0f;
		this.mPhysicsHandler.setVelocity(this.velocityX, this.velocityY);
	}
	
	
	public void setDirection( boolean dir){
		if (dir){
			this.mPhysicsHandler.setVelocityX(this.velocityX);
		} else {
			this.mPhysicsHandler.setVelocityX(-this.velocityX);
		}
	}
	
}
