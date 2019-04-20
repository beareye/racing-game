package com.jajebr.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.jajebr.game.engine.Constants;
import com.jajebr.game.engine.Director;
import com.jajebr.game.engine.screen.Screen;
import com.jajebr.game.game.Content;
import com.jajebr.game.game.screen.LoadingScreen;
import com.jajebr.game.game.screen.MainGameScreen;

public class RacingGame extends ApplicationAdapter {
	private float dtTimer;

	// Don't use constructor when initiating, use create for ApplicationAdapter classes
	@Override
	public void create () {
		dtTimer = 0.0f;
		Director.setCurrentScreen(new LoadingScreen());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 1, 1, 1);
		// Clear color and depth buffer (3D)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (Director.getCurrentScreen() != null) {
			Screen screen = Director.getCurrentScreen();
			// Game loop
			dtTimer += Gdx.graphics.getDeltaTime();
			while (dtTimer >= Constants.SPF) {
				dtTimer -= Constants.SPF;

				screen.update(Constants.SPF);
			}
			// Interpolation factor
			float alpha = dtTimer / Constants.SPF;
			screen.draw(alpha);
		}
	}

	@Override
	public void resize(int w, int h) {
		if (Director.getCurrentScreen() != null) {
			Director.getCurrentScreen().resize(w, h);
		}
	}
	
	@Override
	public void dispose () {
		if (Director.getCurrentScreen() != null) {
			Director.getCurrentScreen().dispose();
		}
		Content.dispose();
	}
}
