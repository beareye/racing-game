package com.jajebr.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.bullet.Bullet;
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
		Bullet.init();

		dtTimer = 0.0f;
		Director.setCurrentScreen(new LoadingScreen());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		// Clear color and depth buffer (3D)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		this.checkKeys();

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

	/**
	 * Checks keys for any top-level operations (e.g. fullscreen)
	 */
	public void checkKeys() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
			Director.swapFullscreen();
			this.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
