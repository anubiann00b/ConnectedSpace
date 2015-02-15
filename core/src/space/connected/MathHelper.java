package space.connected;

import com.badlogic.gdx.Gdx;

public class MathHelper {

    static boolean intersects(double cx, double cy, double r, double ax, double ay, double bx, double by) {
        Gdx.app.log("QQ", cx + " " + cy + "\n" + ax + " " + ay + "\n" + bx + " " + by + "\n" + r + "\n" +
                Math.sqrt((ax-cx)*(ax-cx) + (ay-cy)*(ay-cy)) + "\n" +
                Math.sqrt((bx-cx)*(bx-cx) + (by-cy)*(by-cy)) + "\n\n");
        return Math.sqrt((ax-cx)*(ax-cx) + (ay-cy)*(ay-cy)) < r ||
               Math.sqrt((bx-cx)*(bx-cx) + (by-cy)*(by-cy)) < r;
    }
}
