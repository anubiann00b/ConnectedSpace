package space.connected;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Laser {

    enum Outcome {
        DELETE, SEND, KEEP, HIT
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
            if (MathHelper.intersects(px, py, rad, x, y, x, y + velocity*4))
                return Outcome.HIT;
        }
        return Outcome.KEEP;
    }
}
