package com.atticuswhite.insaniquarium.entities;

import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.atticuswhite.insaniquarium.GameBitmapFactory;
import com.atticuswhite.insaniquarium.GameFonts;
import com.atticuswhite.insaniquarium.GameActivity;

public class Monster extends AggressiveEntity {

	private final static Integer MAX_HEALTH = 5;
	private final Text healthText;
	private Integer health = MAX_HEALTH;
	
	
	public Monster(final float pX, final float pY){
		super(pX, pY, GameBitmapFactory.getTriangleFace());
		GameFonts font = GameActivity.getInstance().getGameFonts();
		this.velocityX = 120.f;
		this.velocityY = 80.0f;
		this.mPhysicsHandler.setVelocity(this.velocityX, this.velocityY);
		this.healthText = new Text(4, 5, font.getMediumFont(), MAX_HEALTH.toString(), MAX_HEALTH.toString().length(), GameActivity.getInstance().getVertexBufferObjectManager());
		this.healthText.setColor(1,0,0);
		this.attachChild(this.healthText);
		
	}
	
	public void setTarget( GameEntity target ){
		super.setTarget( target );
		if (this.hasTarget()){
			this.target.animate(200);
			this.animate(200);
		} else {
			this.stopAnimation();
		}
	}
	
	
	public void handleTouch(){
		this.health -= 1;
		this.healthText.setText(this.health.toString());
	}
	
	public boolean isDead(){
		return this.health <= 0;
	}
}
