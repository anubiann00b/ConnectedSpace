package space.connected;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectedSpace extends ApplicationAdapter {

    SpriteBatch batch;
    Animation player;
    float cumulativeTime = 0;
    float px = 240;
    float py = 240;
    List<Star> stars = new ArrayList<Star>();
    List<Laser> lasers = new ArrayList<Laser>();
    List<Laser> addLasers = new CopyOnWriteArrayList<Laser>();

    NetworkHandler network;
    InetAddress localAddress;
    InetAddress broadcastAddress;

    public ConnectedSpace(InetAddress localAddress, InetAddress broadcastAddress) {
        this.localAddress = localAddress;
        this.broadcastAddress = broadcastAddress;
    }

    @Override
    public void create() {
        network = new NetworkHandler(this);
        new Thread(network).start();
        batch = new SpriteBatch();
        player = SpriteSheet.SHIP_VARIANT_1.getAnim(16);
        player.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        for (int i=0;i<60;i++)
            stars.add(new Star());
    }

    @Override
    public void render() {
        px += (float) (10.0*Math.cos(Math.toRadians(Gdx.input.getRoll()-90)));
        py += (float) (10.0*Math.sin(Math.toRadians(Gdx.input.getPitch())));

        if (cumulativeTime%50 == 0)
            lasers.add(new Laser(px, py, true));

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        for (Star s : stars)
            s.render(batch);
        for (Iterator<Laser> iterator = lasers.iterator(); iterator.hasNext();) {
            Laser l = iterator.next();
            switch(l.render(batch)) {
                case SEND:
                    network.send(l);
                case DELETE:
                    iterator.remove();
            }
        }
        for (Laser l : addLasers)
            lasers.add(l);
        addLasers.clear();

        batch.draw(player.getKeyFrame(cumulativeTime+=2), px-player.getKeyFrame(0).getRegionWidth()*2, py,
                player.getKeyFrame(0).getRegionWidth()*4,
                player.getKeyFrame(0).getRegionHeight()*4);

        batch.end();
    }
}