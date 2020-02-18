package fi.tuni.tiko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public class Bullet {
    static Texture bullets = new Texture("laser-bolts.png");
    TextureRegion bullet;
    Rectangle rectangle;

    private boolean isPlayerBullet;
    private float x;
    private float y;
    private float speed = 16f;

    public Bullet (boolean isPlayer, float x, float y) {
        isPlayerBullet = isPlayer;
        this.x = x;
        this.y = y;

        if (isPlayerBullet) {
            bullet = new TextureRegion(bullets, 0f, 0f, 0.5f, 1f);
        } else {
            bullet = new TextureRegion(bullets, 0.5f, 0f, 1f,1f);
        }

        rectangle = new Rectangle(x, y, 0.25f, 0.25f);
    }

    public void update() {

        if (isPlayerBullet) {
            x += speed * Gdx.graphics.getDeltaTime();
        } else {
            x -= speed / 2 * Gdx.graphics.getDeltaTime();
        }

        rectangle.x = x;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void stop() {
        speed = 0;
        x = -10;
        y = -10;
        rectangle.height = 0;
        rectangle.width = 0;
    }

    public void dispose() {
        bullets.dispose();
    }
}
