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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.jajebr.game.engine.Constants;
import com.jajebr.game.engine.Utilities;
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

    public void draw(SpriteBatch spriteBatch, Array<Player> players) {
        spriteBatch.setProjectionMatrix(this.transform);
        spriteBatch.begin();
            if (!this.player.getCar().isActive()) {
                spriteBatch.setColor(new Color(0.2f, 0.2f, 0.2f, 0.7f));
                spriteBatch.draw(
                        Content.white,
                        this.view.x,
                        this.view.y,
                        this.view.width,
                        this.view.height
                );
            }

            if (this.player.getBeginTimer().getTimeElapsed() < Constants.COUNTDOWN) {
                String text = "" + (5 - (int) this.player.getBeginTimer().getTimeElapsed());
                if (text.equals("5") || text.equals("4")) {
                    text = "- READY -";
                }
                glyphLayout.setText(Content.normalFont, text);
                Content.normalFont.setColor(Color.WHITE);
                Content.normalFont.draw(
                        spriteBatch,
                        text,
                        this.view.x + this.view.width / 2f - glyphLayout.width / 2f,
                        this.view.y + this.view.height / 2f - glyphLayout.height / 2f
                );
            }

            spriteBatch.setColor(Color.WHITE);
            Content.normalFont.setColor(Color.WHITE);
            if (!player.getPlayerInput().getName().isEmpty()) {
                glyphLayout.setText(Content.normalFont, player.getPlayerInput().getName());
                Content.normalFont.draw(
                        spriteBatch,
                        player.getPlayerInput().getName(),
                        this.view.x + this.view.width / 2f - glyphLayout.width / 2f,
                        this.view.y + this.view.height - glyphLayout.height - 20
                );
            }

            if (this.player.isRetired()) {
                Content.normalFont.setColor(Color.GRAY);
                glyphLayout.setText(Content.normalFont, "RETIRED");
                Content.normalFont.draw(
                        spriteBatch,
                        "RETIRED",
                        this.view.x + this.view.width / 2f - glyphLayout.width / 2f,
                        this.view.y + this.view.height / 2f - glyphLayout.height / 2f
                );
            }

            if (!(this.player.finished() || this.player.isRetired())) {
                if (this.player.getLapCount() > 0) {
                    Content.normalFont.draw(
                            spriteBatch,
                            "LAP " + this.player.getLapCount() + "/" + this.player.getWorld().getTrack().getAmountOfLaps(),
                            this.view.x + 20,
                            this.view.y + 33
                    );
                }

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

                this.drawMinimap(spriteBatch, players);
            } else {
                glyphLayout.setText(Content.normalFont, "FINISHED");
                Content.normalFont.setColor(Color.WHITE);
                Content.normalFont.draw(
                        spriteBatch,
                        "FINISHED",
                        this.view.x + this.view.width / 2 - glyphLayout.width / 2,
                        this.view.y + this.view.height - 20 - glyphLayout.height
                );

                String rankString = Utilities.rankToString(this.player.getRank());
                Content.normalFont.setColor(Utilities.getRankColor(this.player.getRank()));
                glyphLayout.setText(Content.normalFont, rankString);
                Content.normalFont.draw(
                        spriteBatch,
                        rankString,
                        this.view.x + this.view.width / 2f - glyphLayout.width / 2f,
                        this.view.y + this.view.height / 2f - glyphLayout.height / 2f
                );

                String elapsedTime = "FINAL TIME: " + this.player.getElapsedTimer().toString();
                glyphLayout.setText(Content.normalFont, elapsedTime);
                Content.normalFont.draw(
                        spriteBatch,
                        elapsedTime,
                        this.view.x + this.view.width / 2 - glyphLayout.width / 2,
                        this.view.y + 33
                );
            }
        spriteBatch.end();
    }

    private void drawMinimap(SpriteBatch spriteBatch, Array<Player> players) {
        Texture pixmapTexture = player.getWorld().getTrack().getPixmapTexture();
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.draw(
                pixmapTexture,
                this.view.x + this.view.width - pixmapTexture.getWidth(),
                this.view.y + this.view.height - pixmapTexture.getHeight()
        );

        // Can't use iterator for nested loops.
        for (int i = 0; i < players.size; i++) {
            Player player = players.get(i);
            if (player == this.player) {
                spriteBatch.setColor(Color.RED);
            } else {
                spriteBatch.setColor(Color.LIME);
            }

            Vector2 minimapPosition = player.getMinimapPosition();

            spriteBatch.draw(
                    Content.white,
                    this.view.x + this.view.width - pixmapTexture.getWidth() + minimapPosition.x - 2,
                    this.view.y + this.view.height - minimapPosition.y - 2,
                    4,
                    4
            );
        }
    }

    public void resize() {
        this.view = player.getViewport().getView();
        this.transform.setToOrtho2D(this.view.x, this.view.y, this.view.width, this.view.height);
    }
}
