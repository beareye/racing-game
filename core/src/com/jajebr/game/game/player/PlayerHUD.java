package com.jajebr.game.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.jajebr.game.game.Content;

public class PlayerHUD {
    private Player player;
    private Matrix4 transform;
    private Rectangle view;

    private GlyphLayout glyphLayout;

    public PlayerHUD(Player currentPlayer) {
        this.player = currentPlayer;
        this.transform = new Matrix4();
        this.view = this.player.getViewport().getView();
        this.glyphLayout = new GlyphLayout();

        this.resize();
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(this.transform);
        spriteBatch.begin();
            Content.normalFont.setColor(Color.WHITE);
            Content.normalFont.draw(
                    spriteBatch,
                    "LAP " + this.player.getLapCount(),
                    this.view.x + 20,
                    this.view.y + 33
            );

            String lapTimer = this.player.getLapTimer().toString();
            glyphLayout.setText(Content.normalFont, lapTimer);
            Content.normalFont.setColor(Color.GREEN);
            Content.normalFont.draw(
                    spriteBatch,
                    this.player.getLapTimer().toString(),
                    this.view.x + 20,
                    this.view.y + 33 + glyphLayout.height * 2
            );

            String speed = Math.round(this.player.getCar().getRigidBody().getLinearVelocity().len()) + " KMH";
            glyphLayout.setText(Content.normalFont, speed);
            Content.normalFont.setColor(Color.RED);
            Content.normalFont.draw(
                    spriteBatch,
                    speed,
                    this.view.x + this.view.width - glyphLayout.width - 20,
                    this.view.y + 33
            );

            Texture pixmapTexture = player.getWorld().getTrack().getPixmapTexture();
            spriteBatch.setColor(Color.WHITE);
            spriteBatch.draw(
                    pixmapTexture,
                    this.view.x +  this.view.width - pixmapTexture.getWidth(),
                    this.view.y + this.view.height - pixmapTexture.getHeight()
            );

            if (this.player.getCar().isRecentering()) {
                glyphLayout.setText(Content.normalFont, "- RECENTERING -");
                Content.normalFont.setColor(Color.SALMON);
                Content.normalFont.draw(
                        spriteBatch,
                        "- RECENTERING -",
                        this.view.x + this.view.width / 2 - glyphLayout.width / 2,
                        this.view.y + glyphLayout.height + 20
                );
            }
        spriteBatch.end();
    }

    public void resize() {
        this.view = player.getViewport().getView();
        this.transform.setToOrtho2D(this.view.x, this.view.y, this.view.width, this.view.height);
    }
}
