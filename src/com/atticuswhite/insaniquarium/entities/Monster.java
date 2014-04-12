package com.atticuswhite.insaniquarium.entities;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.atticuswhite.insaniquarium.GameFonts;
import com.atticuswhite.insaniquarium.MainActivity;

public class Monster extends AnimatedSprite {

	private final static Integer MAX_HEALTH = 5;
	private final float velocityX = 120.0f;
	private final float velocityY = 80.0f;
	private final Text healthText;
	private final PhysicsHandler mPhysicsHandler;
	
	
	private boolean updateHandlerEnabled;
	
	private Fish target;
	private Integer health = MAX_HEALTH;
	
	
	public Monster(final float pX, final float pY, GameFonts font, final TiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager){
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		this.updateHandlerEnabled = true;
		this.mPhysicsHandler.setVelocity(this.velocityX, this.velocityY);
		
		this.healthText = new Text(4, 5, font.getMediumFont(), MAX_HEALTH.toString(), MAX_HEALTH.toString().length(), pVertexBufferObjectManager);
		this.healthText.setColor(1,0,0);
		this.attachChild(this.healthText);
		
	}
	
	public void setTarget( Fish target ){
		this.target = target;
		if (this.hasTarget()){
			this.unregisterUpdateHandler(this.mPhysicsHandler);
			this.updateHandlerEnabled = false;
			this.target.animate(200);
			this.animate(200);
		} else {
			this.stopAnimation();
		}
	}
	
	public boolean hasTarget(){
		return this.target != null;
	}
	
	public void handleTouch(){
		this.health -= 1;
		this.healthText.setText(this.health.toString());
	}
	
	public boolean isDead(){
		return this.health <= 0;
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
