package space.connected;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConnectedSpace extends ApplicationAdapter {


    public enum State {
        MENU, GAME, DEATH
    }

    public static Texture INWARD_LASER;
    public static Texture OUTWARD_LASER;

    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
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

    int health = 10;
    State state = State.GAME;

    Stage stage;

    public ConnectedSpace(InetAddress localAddress, InetAddress broadcastAddress) {
        this.localAddress = localAddress;
        this.broadcastAddress = broadcastAddress;
    }

    @Override
    public void create() {
        INWARD_LASER = new Texture("laser_red.png");
        OUTWARD_LASER = new Texture("laser_blue.png");

        network = new NetworkHandler(this);
        new Thread(network).start();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        player = SpriteSheet.SHIP_VARIANT_1.getAnim(16);
        player.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton button = new TextButton("Start Game", skin);
        button.setWidth(Gdx.graphics.getWidth() * 0.5f);
        button.setHeight(Gdx.graphics.getHeight()*0.5f);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("BUTTON", event.getListenerActor().toString());
                state = State.GAME;
            }
        });
        stage.addActor(button);
        //Gdx.input.setInputProcessor(stage);

        for (int i=0;i<60;i++)
            stars.add(new Star());
    }

    @Override
    public void render() {
        if (state == State.MENU) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            stage.draw();
        } else if (state == State.GAME) {
            px += (float) (10.0 * Math.cos(Math.toRadians(Gdx.input.getRoll() - 90)));
            py += (float) (10.0 * Math.sin(Math.toRadians(Gdx.input.getPitch())));

            if (cumulativeTime % 50 == 0)
                lasers.add(new Laser(px, py, true));

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();

            for (Star s : stars)
                s.render(batch);

            for (Iterator<Laser> iterator = lasers.iterator(); iterator.hasNext(); ) {
                Laser l = iterator.next();
                switch (l.render(batch, px, py + 50, 50)) {
                    case SEND:
                        network.send(l);
                    case DELETE:
                        iterator.remove();
                        break;
                    case HIT:
                        iterator.remove();
                        health--;
                }
            }

            for (Laser l : addLasers)
                lasers.add(l);
            addLasers.clear();

            batch.draw(player.getKeyFrame(cumulativeTime += 2), px - player.getKeyFrame(0).getRegionWidth() * 2, py,
                    player.getKeyFrame(0).getRegionWidth() * 4,
                    player.getKeyFrame(0).getRegionHeight() * 4);

            batch.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setAutoShapeType(true);
            float sx = Gdx.graphics.getWidth() * 0.05f;
            float sy = Gdx.graphics.getHeight() * 0.05f;
            float width = Gdx.graphics.getWidth() * 0.9f;
            float height = Gdx.graphics.getHeight() * 0.05f;
            shapeRenderer.setColor(Color.BLUE);
            shapeRenderer.rect(sx, sy, width, height);
            shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(sx, sy, width * health / 10f, height);
            shapeRenderer.end();

            if (health <= 0) {
                health = 0;
                state = State.DEATH;
            }
        } else if (state == State.DEATH) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();

            batch.end();
        }
    }
}