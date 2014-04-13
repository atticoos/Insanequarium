package com.atticuswhite.insaniquarium.entities;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.atticuswhite.insaniquarium.GameActivity;

public class GameEntity extends AnimatedSprite {
	
	protected final PhysicsHandler mPhysicsHandler;
	protected float velocityX;
	protected float velocityY;
	
	public GameEntity(final float pX, final float pY, final TiledTextureRegion pTextureRegion){
		super(pX, pY, pTextureRegion, GameActivity.getInstance().getVertexBufferObjectManager());
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		this.mPhysicsHandler.setVelocity(this.velocityX, this.velocityY);
	}
	
}
