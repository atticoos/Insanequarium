package com.atticuswhite.insaniquarium;

import org.andengine.opengl.texture.region.TiledTextureRegion;

public class GameBitmapFactory {

	
	public static TiledTextureRegion getBoxFace(){
		return GameActivity.getInstance().getGameBitmaps().getBoxFace();
	}
	
	public static TiledTextureRegion getCircleFace(){
		return GameActivity.getInstance().getGameBitmaps().getCircleFace();
	}
	
	public static TiledTextureRegion getTriangleFace(){
		return GameActivity.getInstance().getGameBitmaps().getTriangleFace();
	}
	
	public static TiledTextureRegion getHexagonFace(){
		return GameActivity.getInstance().getGameBitmaps().getHexagonFace();
	}
	
}
