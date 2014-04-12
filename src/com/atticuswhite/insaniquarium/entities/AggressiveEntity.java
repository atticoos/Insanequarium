package com.atticuswhite.insaniquarium.entities;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.atticuswhite.insaniquarium.MainActivity;

public class AggressiveEntity extends GameEntity {
	
	protected boolean updateHandlerEnabled;
	protected GameEntity target;
	
	public AggressiveEntity(final float pX, final float pY, final TiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager){
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.updateHandlerEnabled = true;
	}
	
	public void setTarget( GameEntity target ){
		this.target = target;
		if (this.hasTarget()){
			this.unregisterUpdateHandler(this.mPhysicsHandler);
			this.updateHandlerEnabled = false;
		}
	}
	
	public boolean hasTarget(){
		return this.target != null;
	}
	
	public GameEntity getTarget(){
		return this.target;
	}
	
	public void removeTarget(){
		this.target = null;
		this.registerUpdateHandler(this.mPhysicsHandler);
		this.updateHandlerEnabled = true;
	}
	

	protected void onManagedUpdate(final float pSecondsElapsed){
		
		if (!this.hasTarget()){
			if (!this.updateHandlerEnabled){
				this.registerUpdateHandler(this.mPhysicsHandler);
				this.updateHandlerEnabled = true;
			}
		
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
		} else {

			float pX = this.getX();
			float pY = this.getY();
			
			float tX = target.getX();
			float tY = target.getY();
			
			if (pX > tX){
				pX -= 1;
			} else {
				pX += 1;
			}
			
			if (pY >= tY){
				pY -= 1;
			} else {
				pY += 1;
			}
			
			this.setPosition(pX, pY);
		}
		super.onManagedUpdate(pSecondsElapsed);
	}
}
