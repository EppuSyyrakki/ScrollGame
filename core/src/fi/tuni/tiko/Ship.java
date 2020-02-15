package fi.tuni.tiko;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Ship {
    void update(SpriteBatch batch);
    float getX();
    float getY();
    float getWidth();
    float getHeight();
    boolean[] getCornersFree();

}
