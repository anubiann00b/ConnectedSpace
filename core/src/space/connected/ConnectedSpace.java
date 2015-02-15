package space.connected;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class ConnectedSpace extends ApplicationAdapter {

	SpriteBatch batch;
    Animation player;
    float cumulativeTime = 0;
    float px = 240;
    float py = 240;
    List<Star> stars = new ArrayList<Star>();

	@Override
	public void create () {
		batch = new SpriteBatch();
        player = SpriteSheet.SHIP_VARIANT_1.getAnim(16);
        player.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        for (int i=0;i<20;i++)
            stars.add(new Star());
    }

	@Override
	public void render() {
        px += (float) (10.0*Math.cos(Math.toRadians(Gdx.input.getRoll()-90)));
        py += (float) (10.0*Math.sin(Math.toRadians(Gdx.input.getPitch())));

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

        for (Star s : stars)
            s.render(batch);
        batch.draw(player.getKeyFrame(cumulativeTime+=2), this.px, this.py, 25 * 4, 30 * 4);

        batch.end();
	}
}