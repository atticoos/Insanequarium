package com.atticuswhite.insaniquarium.controlarea;

import org.andengine.entity.Entity;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.atticuswhite.insaniquarium.GameFonts;
import com.atticuswhite.insaniquarium.MainActivity;

public class ScoreManager {
	private final VertexBufferObjectManager vertexBufferObjectManager;
	private final Text scoreText;
	private Entity entity;
	private int score = 0;
	
	public ScoreManager(GameFonts font, final VertexBufferObjectManager vertexBufferObjectManager){
		this.vertexBufferObjectManager = vertexBufferObjectManager;
		this.entity = new Entity();
		this.scoreText = new Text(MainActivity.CAMERA_WIDTH - 100, 5, font.getLargeFont(), "$0", "$XXX".length(), vertexBufferObjectManager);;
		
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
