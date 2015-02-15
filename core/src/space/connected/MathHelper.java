package space.connected;

public class MathHelper {

    static boolean intersects(double cx, double cy, double r, double ax, double ay, double bx, double by) {
        // proj a = (a*bh)bh
        // proj AC onto AB to get D, compare D to R
        double magB = Math.sqrt(bx*bx+by*by);
        double bUnitX = bx / magB;
        double bUnitY = by / magB;
        double aDotBUnit = ax*bUnitX + ay*bUnitY;
        double dx = aDotBUnit*bUnitX;
        double dy = aDotBUnit*bUnitY;

//        if (Math.sqrt((ax+dx)*(ax+dx) + (ay+dy)*(ay+dy)) > 50)
//            return false;

        double dc = Math.sqrt((dx+cx)*(dx+cx) + (dy+cy)*(dy+cy));
        return dc <= r;
    }
}
