package space.connected.util;

public class MathHelper {

    public static boolean intersects(double cx, double cy, double r, double ax, double ay, double bx, double by) {
        return Math.sqrt((ax-cx)*(ax-cx) + (ay-cy)*(ay-cy)) < r ||
               Math.sqrt((bx-cx)*(bx-cx) + (by-cy)*(by-cy)) < r;
    }
}
