package space.connected;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpriteSheet {

    private Texture texture;
    private TextureRegion[] region;

    public static SpriteSheet SHIP = new SpriteSheet("ship_spritesheet.png",23,32);

    public SpriteSheet(String path, int width, int height) {
        texture = new Texture(path);
        TextureRegion[][] regions = TextureRegion.split(texture, width, height);
        region = new TextureRegion[regions.length * regions[0].length];

        for(int i = 0; i < regions.length; i++)
            System.arraycopy(regions[i], 0, region, i * regions[i].length, regions[i].length);
    }

    public Animation getAnim(float frameSpeed) {
        return new Animation(frameSpeed, region);
    }
}