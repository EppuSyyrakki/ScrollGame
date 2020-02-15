package fi.tuni.tiko;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {
    private Texture texture;
    private TextureRegion[] frames;
    Animation<TextureRegion> animation;

    public Explosion() {
        texture = new Texture("explosion.png");
        frames = new TextureRegion[5];

        for (int i = 0; i < 5; i++) {
            frames[i] = new TextureRegion(texture,
                    i * 0.2f, 0f, 0.2f + i * 0.2f, 1f);
        }
        animation = new Animation<>(12f / 60f , frames);
    }

    public void dispose() {
        texture.dispose();
    }
}
