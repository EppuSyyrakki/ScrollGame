package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {
    static Texture texture;
    TextureRegion[] frames;
    Animation<TextureRegion> animation;
    private float stateTime;
    private float size = 1f;

    public Explosion() {
        texture = new Texture("explosion.png");
        frames = new TextureRegion[5];

        for (int i = 0; i < 5; i++) {
            frames[i] = new TextureRegion(texture, i * 16, 0, 16, 16);
        }

        animation = new Animation<>(12 / 60, frames);
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public void dispose() {
        texture.dispose();
    }

    public float getSize() {
        return size;
    }
}
