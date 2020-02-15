package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {
    Texture texture = new Texture("explosion.png");
    TextureRegion[] frames = new TextureRegion[5];
    Animation<TextureRegion> animation;

    private float size = 0.75f;

    public Explosion() {

        for (int i = 0; i < 5; i++) {
            frames[i] = new TextureRegion(texture,
                    i * 0.2f, 0f, 0.2f + i * 0.2f, 1f);
        }

        animation = new Animation<>(12f / 60f , frames);
    }

    public void dispose() {
        texture.dispose();
    }

    public float getSize() {
        return size;
    }
}
