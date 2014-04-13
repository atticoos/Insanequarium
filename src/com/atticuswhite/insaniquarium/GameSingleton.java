package com.atticuswhite.insaniquarium;

import org.andengine.ui.activity.SimpleBaseGameActivity;

public class GameSingleton {
	private static volatile SimpleBaseGameActivity singleton;
	
	
	public static synchronized void setInstance( SimpleBaseGameActivity instance ){
		singleton = instance;
	}
	
	public static synchronized SimpleBaseGameActivity getInstance(){
		return singleton;
	}
	
	
}
