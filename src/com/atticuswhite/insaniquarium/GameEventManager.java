package com.atticuswhite.insaniquarium;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameEventManager {
	private GameEvent event;
	private List<GameEventListener> listeners = new ArrayList<GameEventListener>();
	
	public synchronized void createFish(){
		this.event = GameEvent.CREATE_FISH;
		triggerEvent();
	}
	
	public synchronized void addListener( GameEventListener listener ){
		this.listeners.add(listener);
	}
	
	public synchronized void removeListener( GameEventListener listener ){
		this.listeners.remove(listener);
	}
	
	public synchronized void triggerEvent(){
		Iterator<GameEventListener> iterator = listeners.iterator();
		while (iterator.hasNext()){
			iterator.next().handleGameEvent(event);
		}
	}
}
