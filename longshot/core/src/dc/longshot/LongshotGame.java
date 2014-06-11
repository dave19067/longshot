package dc.longshot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.SpriteKey;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.TransformPart;
import dc.longshot.util.EventManager;

public class LongshotGame extends ApplicationAdapter {
	
	public static final float DRAW_SCALE = 64;
	private Vector2 levelSize = new Vector2(40, 30);
	
	private EventManager eventManager = new EventManager();
	private EntityManager entityManager = new EntityManager(eventManager);
	private SpriteCache<SpriteKey> spriteCache = new SpriteCache<SpriteKey>();
	private EntityFactory entityFactory = new EntityFactory(spriteCache);
	private Camera camera;
	private SpriteBatch spriteBatch;
	
	@Override
	public void create () {
		camera = new OrthographicCamera(levelSize.x * DRAW_SCALE, levelSize.y * DRAW_SCALE);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		spriteBatch = new SpriteBatch();
		
		loadSprites();
		Entity shooter = entityFactory.createShooter(new Vector2(0, 0));
		entityManager.add(shooter);
	}

	@Override
	public void render () {
		entityManager.update();
		camera.update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		for (Entity entity : entityManager.getAll()) {
			if (entity.has(TransformPart.class) && entity.has(DrawablePart.class)) {
//				Vector2 size = getSize().scl(DRAW_SCALE);
				Vector2 position = entity.get(TransformPart.class).getPosition().scl(DRAW_SCALE);
				spriteBatch.draw(entity.get(DrawablePart.class).getTextureRegion(), position.x, position.y, 64, 32);
			}
		}
		spriteBatch.end();
	}
	
	private void loadSprites() {
		spriteCache.add(SpriteKey.SHOOTER, "images/shooter.png");
	}
	
}
