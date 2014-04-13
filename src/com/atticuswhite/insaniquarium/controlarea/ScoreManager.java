package com.atticuswhite.insaniquarium.controlarea;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.atticuswhite.insaniquarium.GameFonts;
import com.atticuswhite.insaniquarium.GameActivity;

public class ScoreManager {
	private final Text scoreText;
	private int score = 0;
	
	public ScoreManager(){
		this.scoreText = new Text(GameActivity.CAMERA_WIDTH - 100, 5, GameActivity.getInstance().getGameFonts().getLargeFont(), "$0", "$XXX".length(), GameActivity.getInstance().getVertexBufferObjectManager());
	}
	
	public void updateScore(int score){
		this.score += score;
		this.scoreText.setText("$" + this.score);
	}
	
	public int getScore(){
		return this.score;
	}
	
	public Entity getEntity(){
		return this.scoreText;
	}
}
