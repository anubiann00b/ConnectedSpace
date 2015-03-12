package space.connected.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Star {

    Sprite sprite;
    double x = 0;
    double y = -1;
    double z = 0;

    public Star() {
        sprite = new Sprite(new Texture("star.png"));
        y = 2*Math.random()*Gdx.graphics.getHeight();
        x = Math.random()*Gdx.graphics.getWidth();
        z = -(int)(4+Math.random()*5);
    }

    public void render(SpriteBatch batch) {
        y += z;
        if (y < 0) {
            x = Math.random()*Gdx.graphics.getWidth();
            z = -(int)(4+Math.random()*5);
            y = (Math.random()+1)*Gdx.graphics.getHeight();
        }
        sprite.setPosition((float)x, (float)y);
        sprite.draw(batch);
    }
}