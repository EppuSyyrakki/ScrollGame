package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Enemy implements Ship {
    private float speedX;
    private float speedY;
    private float x;
    private float y;
    private float width = 0.5f;
    private float height = 1f;
    private float stateTime = 0.0f;
    private boolean gameRunning = false;
    private boolean[] cornersFree = {true, true, true, true};
    private boolean isAlive = true;
    private TextureRegion currentFrame;
    private Explosion explosion = new Explosion();

    Rectangle rectangle;
    Texture moveTexture = new Texture("enemy-medium.png");
    TextureRegion[] moveFrames = new TextureRegion[2];
    Animation<TextureRegion> move;

    public Enemy (float x, float y) {
        this.x = x;
        this.y = y;
        rectangle = new Rectangle(this.x, this.y, width, height);
        moveFrames[0] = new TextureRegion(moveTexture, 0,0, 16, 32);
        moveFrames[1] = new TextureRegion(moveTexture, 16, 0, 16, 32);
        move = new Animation<>(6 / 60f, moveFrames);
        currentFrame = move.getKeyFrame(stateTime);
    }
    @Override
    public void update(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime();
        stateTime += delta;

        if (isAlive) {
            // x += speedX * delta;
            // rectangle.x = x;
        }

        currentFrame = move.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y);
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public boolean[] getCornersFree() {
        return cornersFree;
    }
}
