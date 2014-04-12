package com.atticuswhite.insaniquarium.controlarea;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class ControlTouchArea extends Rectangle{
	ControlAreaSection parent;
	public ControlTouchArea(ControlAreaSection area, float pX, float pY, float width, float height, VertexBufferObjectManager vertexBufferObjectManager){
		super(pX, pY, width, height, vertexBufferObjectManager);
		
		this.parent = area;
	}
	
	public ControlAreaSection getSection(){
		return parent;
	}
}