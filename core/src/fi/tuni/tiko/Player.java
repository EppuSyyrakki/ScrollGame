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
    private boolean[] cornersFree = {true, true, true, true};
    private boolean isAlive = true;
    private TextureRegion currentFrame;
    private Explosion explosion = new Explosion();
    private ArrayList<Bullet> bullets = new ArrayList<>();

    Rectangle rectangle = new Rectangle(x, y, 1f , 0.5f);
    private Texture allFrames = new Texture("ship.png");
    private TextureRegion[][] splitFrames = TextureRegion.split(allFrames, 24,16);
    private TextureRegion[] forwardFrames = new TextureRegion[] {splitFrames[2][0], splitFrames[2][1]};
    private TextureRegion[] upFrames = new TextureRegion[] {splitFrames[0][0], splitFrames[0][1]};
    private TextureRegion[] downFrames = new TextureRegion[] {splitFrames[4][0], splitFrames[4][1]};

    private Animation<TextureRegion> forward = new Animation<>(6 / 60f, forwardFrames);
    private Animation<TextureRegion> up = new Animation<>(6 / 60f, upFrames);
    private Animation<TextureRegion> down = new Animation<>(6 / 60f, downFrames);

    public Player() {
        x = 2f;
        y = 5.1f;
        stateTime = 0.0f;
        width = 1f;
        height = 0.5f;
    }

    public void update(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime();
        stateTime += delta;

        for (boolean cornerFree : cornersFree) {

            if (!cornerFree) {
                isAlive = false;
            }
        }

        if (gameRunning) {

            if (isAlive) {

                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    bullets.add(new Bullet(true, x + 1, y));
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
            } else {
                destroy();
            }
        } else {
            currentFrame = forward.getKeyFrame(stateTime, true);

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                speedX = 4f;
                speedY = 4f;
                gameRunning = true;
            }
        }

        batch.draw(currentFrame, x, y, width, height);
        updateBulletList(batch);
    }

    public void destroy() {
        speedX = 0f;
        speedY = 0f;
        height = 1f;
        currentFrame = explosion.animation.getKeyFrame(stateTime, true);

        if (explosion.animation.isAnimationFinished(stateTime)) {

            Gdx.app.log("player", "destroyed");
        }
    }

    private void updateBulletList(SpriteBatch batch) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet tmp = bullets.get(i);
            tmp.update();

            if (tmp.getX() < this.x + 14f) {
                batch.draw(tmp.bullet, tmp.getX(), tmp.getY(), 1f, 0.5f);
                bullets.set(i, tmp);
            } else {
                bullets.remove(i);
                bullets.trimToSize();
            }
        }
    }

    public boolean checkBulletHits(Enemy enemy) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet tmp = bullets.get(i);

            if (tmp.rectangle.overlaps(enemy.rectangle)) {
                return true;
            }
        }

        return false;
    }

    public void dispose() {
        allFrames.dispose();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setCornersFree(boolean c[]) {
        cornersFree[0] = c[0];
        cornersFree[1] = c[1];
        cornersFree[2] = c[2];
        cornersFree[3] = c[3];
    }

    public boolean[] getCornersFree() {
        return cornersFree;
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
