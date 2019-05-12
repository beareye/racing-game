package com.jajebr.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.jajebr.game.RacingGame;
import com.jajebr.game.engine.Director;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		// Enable low detail mode just for mobile
		Director.LOW_DETAIL = true;
		initialize(new RacingGame(), config);
	}
}
