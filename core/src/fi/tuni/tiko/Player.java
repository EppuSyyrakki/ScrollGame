package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;


public class Player implements Ship {
    private float speedX;
    private float speedY;
    private float x;
    private float y;
    private float width;
    private float height;
    private float stateTime;
    private boolean gameRunning = false;
    private boolean[] corners = new boolean[4];
    private boolean isAlive = true;
    private ArrayList<Bullet> bullets = new ArrayList();
    private Explosion explosion = new Explosion();

    Rectangle rectangle;
    Texture allFrames;
    TextureRegion currentFrame;
    TextureRegion[] forwardFrames;
    TextureRegion[] upFrames;
    TextureRegion[] downFrames;
    TextureRegion[][] splitFrames;
    Animation<TextureRegion> forward;
    Animation<TextureRegion> up;
    Animation<TextureRegion> down;

    public Player() {
        allFrames = new Texture("ship.png");
        splitFrames = TextureRegion.split(allFrames, 24,16);
        forwardFrames = new TextureRegion[] {splitFrames[2][0], splitFrames[2][1]};
        upFrames = new TextureRegion[] {splitFrames[0][0], splitFrames[0][1]};
        downFrames = new TextureRegion[] {splitFrames[4][0], splitFrames[4][1]};
        forward = new Animation<>(10 / 60f, forwardFrames);
        up = new Animation<>(12 / 60f, upFrames);
        down = new Animation<>(12 / 60f, downFrames);
        rectangle = new Rectangle(x, y, 1f , 0.5f);
        x = 2f;
        y = 5.1f;
        speedY = 5f;
        stateTime = 0.0f;
        width = 1f;
        height = 0.5f;
    }

    public void update(SpriteBatch batch, float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && gameRunning) {
            bullets.add(new Bullet(true, x + 1, y));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !gameRunning) {
            speedX = 3f;
            gameRunning = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += speedY * delta;
            currentFrame = up.getKeyFrame(stateTime, true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= speedY * delta;
            currentFrame = down.getKeyFrame(stateTime, true);
        } else {
            currentFrame = forward.getKeyFrame(stateTime, true);
        }

        x += speedX * delta;
        rectangle.y = y;
        rectangle.x = x;
        batch.draw(currentFrame, x, y, width, height);
        drawBullets(batch);
    }

    private void drawBullets(SpriteBatch batch) {

        for (int i = bullets.size() - 1; i > 0; i--) {
            Bullet tmp = bullets.get(i);
            tmp.update();

            if (tmp.getX() > this.x + 14f) {
                bullets.remove(i);
            } else {
                batch.draw(tmp.bullet, tmp.getX(), tmp.getY(), 1f, 0.5f);
                bullets.set(i, tmp);
            }
        }
        bullets.trimToSize();
    }

    public void dispose() {
        allFrames.dispose();
        explosion.dispose();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setCorners(boolean c[]) {
        corners[0] = c[0];
        corners[1] = c[1];
        corners[2] = c[2];
        corners[3] = c[3];
    }

    public boolean[] getCorners() {
        return corners;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }
}
