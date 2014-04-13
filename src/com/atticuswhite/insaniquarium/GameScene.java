package com.atticuswhite.insaniquarium;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.hardware.SensorManager;
import android.util.Log;

import com.atticuswhite.insaniquarium.controlarea.ControlArea;
import com.atticuswhite.insaniquarium.controlarea.ControlAreaSection;
import com.atticuswhite.insaniquarium.controlarea.ControlTouchArea;
import com.atticuswhite.insaniquarium.controlarea.ScoreManager;
import com.atticuswhite.insaniquarium.entities.Coin;
import com.atticuswhite.insaniquarium.entities.Fish;
import com.atticuswhite.insaniquarium.entities.Food;
import com.atticuswhite.insaniquarium.entities.Monster;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class GameScene implements GameEventListener, IOnSceneTouchListener, IOnAreaTouchListener {
	

	private static final int LAYER_COUNT = 4;
	private static final int LAYER_BACKGROUND = 0;
	private static final int LAYER_FISH = LAYER_BACKGROUND + 1;
	private static final int LAYER_CONTROLS = LAYER_FISH + 1;
	private static final int LAYER_SCORE = LAYER_CONTROLS + 1;
	
	private List<Coin> coins = new ArrayList<Coin>();
	private List<Fish> fishies = new ArrayList<Fish>();
	private List<Monster> monsters = new ArrayList<Monster>();
	private List<Food> munchies = new ArrayList<Food>();
	
	private final Scene scene;
	private final ControlArea mControlArea;
	private final ScoreManager mScoreManager;
	private final GameEventManager mGameEventManager;
	private final PhysicsWorld mPhysicsWorld;
	
	private long currentTime;
	private Long lastCoin;
	
	public GameScene(){
		this.scene = new Scene();
		final VertexBufferObjectManager vertexBufferObjectManager = GameActivity.getInstance().getVertexBufferObjectManager();
		
		for (int i=0; i<LAYER_COUNT; i++){
			this.scene.attachChild(new Entity());
		}
		
		this.scene.setBackground(new Background(0.09804f, 0.6274f, 0));
		this.scene.setOnSceneTouchListener(this);
		this.scene.setOnAreaTouchListener(this);
		
		this.mGameEventManager = new GameEventManager();
		this.mGameEventManager.addListener(this);
		
		this.mControlArea = new ControlArea(this.mGameEventManager);
		this.mScoreManager = new ScoreManager();
		
		this.mControlArea.attachChild(this.mScoreManager.getEntity());
		this.mControlArea.registerTouchAreas(this.scene);
		this.scene.getChildByIndex(LAYER_CONTROLS).attachChild(this.mControlArea);
		

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		
		final Rectangle ground = new Rectangle(0, GameActivity.CAMERA_HEIGHT - 2, GameActivity.CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, GameActivity.CONTROL_HEIGHT, GameActivity.CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, GameActivity.CONTROL_HEIGHT, 2, GameActivity.CAMERA_HEIGHT - GameActivity.CONTROL_HEIGHT, vertexBufferObjectManager);
		final Rectangle right = new Rectangle(GameActivity.CAMERA_WIDTH-2, GameActivity.CONTROL_HEIGHT, 2, GameActivity.CAMERA_HEIGHT - GameActivity.CONTROL_HEIGHT, vertexBufferObjectManager);
		final Rectangle shelf = new Rectangle(300, GameActivity.CONTROL_HEIGHT + 200, 100, 2, vertexBufferObjectManager);
		
		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, shelf, BodyType.StaticBody, wallFixtureDef);
		

		this.scene.getChildByIndex(LAYER_BACKGROUND).attachChild(ground);
		this.scene.getChildByIndex(LAYER_BACKGROUND).attachChild(roof);
		this.scene.getChildByIndex(LAYER_BACKGROUND).attachChild(left);
		this.scene.getChildByIndex(LAYER_BACKGROUND).attachChild(right);
		this.scene.getChildByIndex(LAYER_BACKGROUND).attachChild(shelf);
		
		this.scene.registerUpdateHandler(this.mPhysicsWorld);
		
		this.scene.registerUpdateHandler(new IUpdateHandler(){

			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				currentTime = System.currentTimeMillis();
				
				
				// COIN BOUNDARIES
				Iterator<Coin> coinIter = coins.iterator();
				while(coinIter.hasNext()){
					Coin coin = coinIter.next();
					if (coin.outOfBounds()){
						scene.getChildByIndex(LAYER_CONTROLS).detachChild(coin);
						scene.unregisterTouchArea(coin);
						coinIter.remove();
					}
				}
				
				// DROP COIN
				if (lastCoin == null || ( (currentTime / 1000) - (lastCoin / 1000)) > 5){
					lastCoin = currentTime;
					float pX = (float) ((Math.random() * (GameActivity.CAMERA_WIDTH - 10f) + 10f));
					addCoin(pX);
				}
				
				// HUNGRY MONSTERS
				Iterator<Fish> fishIter;
				for(Monster monster : monsters){
					fishIter = fishies.iterator();
					
					while(fishIter.hasNext()){
						Fish fish = fishIter.next();
						if (monster.collidesWith(fish)){
							fishIter.remove();
							scene.unregisterTouchArea(fish);
							scene.getChildByIndex(LAYER_FISH).detachChild(fish);
							monster.setTarget(null);
						}
					}
					
					if (!monster.hasTarget()){
						Fish target = null;
						Float pX = null;
						Float pY = null;
					
						for (Fish fish : fishies){
							float dX = Math.abs(monster.getX() - fish.getX());
							float dY = Math.abs(monster.getY() - fish.getY());
							
							if (pX == null || pY == null || (dX < pX && dY < pY)){
								pX = dX;
								pY = dY;
								target = fish;
							}
						}
						monster.setTarget(target);
					}
					
				}
				
				// MONSTER FATALITIES
				Iterator<Monster> monsterIter = monsters.iterator();
				while(monsterIter.hasNext()){
					Monster monster = monsterIter.next();
					if (monster.isDead()){
						monsterIter.remove();
						scene.unregisterTouchArea(monster);
						scene.getChildByIndex(LAYER_FISH).detachChild(monster);
					}
				}
				
				// FISHIES HUNGRY
				float foodRange = 100.0f;
				for(Fish fish : fishies){
					if (!fish.hasTarget()){
						for (Food food : munchies){
							
							float dX = Math.abs(fish.getX() - food.getX());
							float dY = Math.abs(fish.getY() - food.getY());
							
							if (dX < foodRange && dY < foodRange){
								fish.setTarget(food);
							}
							
						}
					} else {
						if (fish.getTarget().collidesWith(fish)){
							munchies.remove(fish.getTarget());
							scene.getChildByIndex(LAYER_FISH).detachChild(fish.getTarget());
							fish.removeTarget();
						}
					}
				}
				
				
				
			}

			@Override
			public void reset() { }
			
		});
		
	}
	


	
	public Scene getScene() {
		return this.scene;
	}


	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if (pSceneTouchEvent.isActionDown()){
			if (pTouchArea instanceof Coin){
				Log.v("COIN", "Eating coin");
				this.eatCoin((Coin) pTouchArea);
			} else if (pTouchArea instanceof ControlTouchArea){
				Log.v("ControlAreaSection", "Touched");
				((ControlAreaSection)((ControlTouchArea) pTouchArea ).getParent()).touchedArea();
			} else if (pTouchArea instanceof Fish){
				Log.v("Monster", "Creating monster");
				this.addMonster((Fish) pTouchArea);
			} else if (pTouchArea instanceof Monster){
				((Monster) pTouchArea).handleTouch();
			}
			return true;
		}
		return false;
	}


	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (this.mPhysicsWorld != null){
			if (pSceneTouchEvent.isActionDown()){
				if (this.mScoreManager.getScore() - Food.COST >= 0){
					this.addFood(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				}
				return true;
			}
		}
		return false;
	}


	@Override
	public void handleGameEvent(GameEvent event) {
		switch(event){
			case CREATE_FISH:
				Log.v("FISH", "CREATE FISH");
				if (this.mScoreManager.getScore() - Fish.COST >= 0){
					this.addFish();
				}
				break;
		}
	}
	

	private void addCoin(final float pX){
		final float pY = GameActivity.CONTROL_HEIGHT;
		final Coin coin = new Coin(pX, pY);
		this.coins.add(coin);
		this.scene.registerTouchArea(coin);
		this.scene.getChildByIndex(LAYER_FISH).attachChild(coin);
	}
	
	private void eatCoin(Coin coin){
		this.scene.unregisterTouchArea(coin);
		this.scene.getChildByIndex(LAYER_FISH).detachChild(coin);
		this.mScoreManager.updateScore(coin.getValue());
	}
	
	private void addMonster(Fish target){
		final float pX = 100f;
		final float pY = 200f;
		
		final Monster monster = new Monster(pX, pY);
		monster.setTarget(target);
		
		this.monsters.add(monster);
		this.scene.registerTouchArea(monster);
		this.scene.getChildByIndex(LAYER_FISH).attachChild(monster);
	}
	
	private void addFood(float pX, float pY){
		final Food food = new Food(pX, pY);
		munchies.add(food);
		this.scene.getChildByIndex(LAYER_FISH).attachChild(food);
		
		this.mScoreManager.updateScore( -Food.COST );
	}
	

	private void addFish(){
		final float pX = (float) Math.random() * GameActivity.CAMERA_WIDTH;
		final float pY = (float) (Math.random() * (GameActivity.CAMERA_HEIGHT - GameActivity.CONTROL_HEIGHT)) + GameActivity.CONTROL_HEIGHT;
		boolean direction = true;
		
		if (Math.random() * 2 > 1){
			direction = false;
		}
		
		Fish fish = new Fish(pX, pY);
		fish.setDirection(direction);
		this.fishies.add(fish);
		this.scene.registerTouchArea(fish);
		this.scene.getChildByIndex(LAYER_FISH).attachChild(fish);
		
		this.mScoreManager.updateScore( -Fish.COST );
	}

}
