package com.atticuswhite.insaniquarium.entities;


import com.atticuswhite.insaniquarium.GameBitmapFactory;

public class Food extends GameEntity {
	
	public static final int COST = 5;

	public Food(final float pX, final float pY){
		super(pX, pY, GameBitmapFactory.getHexagonFace());
		this.velocityX = 0;
		this.velocityY = 40.0f;
		this.mPhysicsHandler.setVelocity(this.velocityX, this.velocityY);
		
	}
	
	
}
