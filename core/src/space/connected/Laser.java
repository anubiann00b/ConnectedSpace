package space.connected;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Laser {

    enum Outcome {
        DELETE, SEND, KEEP
    }

    Sprite laser;
    double x;
    double y;
    int velocity;

    public Laser(double x, double y, boolean forward) {
        this.x = x;
        this.y = y;
        laser = new Sprite(ConnectedSpace.MY_LASER_TEXTURE);
        laser.setScale(0.5f, 1f);
        laser.flip(false, !forward);
        laser.setOriginCenter();
        velocity = 15 * (forward ? 1 : -1);
    }

    public Outcome render(SpriteBatch batch, double px, double py, double rad) {
        if (y > Gdx.graphics.getHeight())
            return Outcome.SEND;
        else if (y < 0)
            return Outcome.DELETE;
        y += velocity;
        laser.setPosition((float)x-laser.getWidth()/2, (float)y);
        laser.draw(batch);
        if (velocity < 0) {
            batch.end();
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            if (MathHelper.intersects(px, py, rad, x, y, x, y + velocity*4))
                shapeRenderer.setColor(Color.RED);
            else
                shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle((float) px, (float) py, (float) rad);
            shapeRenderer.rect((float)(x-4),(float)y,(float)4,(float)velocity*4);
            shapeRenderer.end();
            batch.begin();
        }
        return Outcome.KEEP;
    }
}
