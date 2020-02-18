package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class Enemy implements Ship {
    private float speedX;
    private float x;
    private float y;
    private float width = 0.5f;
    private float height = 1f;
    private float stateTime = 0.0f;
    private boolean[] cornersFree = {true, true, true, true};
    private boolean isAlive = true;
    private TextureRegion currentFrame;
    private Explosion explosion = new Explosion();
    private ArrayList<Enemy> list = new ArrayList<>();

    Rectangle rectangle;
    private Texture moveTexture = new Texture("enemy-medium.png");
    private TextureRegion[] moveFrames = new TextureRegion[2];
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

    public void start() {
        speedX = 3f;
    }

    public void destroy() {
        move = explosion.animation;
        speedX = 0f;
        width = 1f;
        rectangle.width = 0;
        rectangle.height = 0;
        isAlive = false;
    }

    @Override
    public void update(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime();
        stateTime += delta;

        x -= speedX * delta;

        currentFrame = move.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y, width, height);
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

    public void dispose() {
        moveTexture.dispose();
    }

    public boolean getIsAlive() {
        return isAlive;
    }
}
