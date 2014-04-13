package com.atticuswhite.insaniquarium;

import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

public class GameBitmaps {
	
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mBoxFaceTextureRegion;
	private TiledTextureRegion mCircleFaceTextureRegion;
	private TiledTextureRegion mTriangleFaceTextureRegion;
	private TiledTextureRegion mHexagonFaceTextureRegion;
	
	
	public GameBitmaps(){
		
	}
	
	
	public void generateBitmapTextures(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(GameActivity.getInstance().getTextureManager(), 64, 128, TextureOptions.BILINEAR);
		this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, GameActivity.getInstance(), "face_box_tiled.png", 0, 0, 2, 1);
		this.mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, GameActivity.getInstance(), "face_circle_tiled.png", 0, 32, 2, 1);
		this.mTriangleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, GameActivity.getInstance(), "face_triangle_tiled.png", 0, 64, 2, 1); // 64x32
		this.mHexagonFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, GameActivity.getInstance(), "face_hexagon_tiled.png", 0, 96, 2, 1); // 64x32
		
		this.mBitmapTextureAtlas.load();
	}
	
	
	public TiledTextureRegion getBoxFace(){
		return this.mBoxFaceTextureRegion;
	}
	
	public TiledTextureRegion getCircleFace(){
		return this.mCircleFaceTextureRegion;
	}
	
	public TiledTextureRegion getTriangleFace(){
		return this.mTriangleFaceTextureRegion;
	}
	
	public TiledTextureRegion getHexagonFace(){
		return this.mHexagonFaceTextureRegion;
	}
	
	
	
	
	
}
