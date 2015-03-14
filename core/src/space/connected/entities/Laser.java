package space.connected.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import space.connected.ConnectedSpace;
import space.connected.util.MathHelper;

public class Laser {

    public enum Outcome {
        DELETE, SEND, KEEP, HIT
    }

    Sprite laser;
    public double x;
    public double y;
    int velocity;

    public Laser(double x, double y, boolean forward) {
        this.x = x;
        this.y = y;
        if (forward)
            laser = new Sprite(ConnectedSpace.OUTWARD_LASER);
        else
            laser = new Sprite(ConnectedSpace.INWARD_LASER);
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
        laser.setPosition((float) x - laser.getWidth() / 2, (float) y);
        laser.draw(batch);
        if (velocity < 0) {
            if (MathHelper.intersects(px, py, rad, x, y, x, y + velocity * 4))
                return Outcome.HIT;
        }
        return Outcome.KEEP;
    }
}
