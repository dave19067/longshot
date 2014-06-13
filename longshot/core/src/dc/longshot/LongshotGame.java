package dc.longshot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
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
import dc.longshot.parts.BoundsPart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.SpawnerPart;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.util.EventManager;
import dc.longshot.util.VectorUtils;

public class LongshotGame extends ApplicationAdapter {
	
	public static final float DRAW_SCALE = 64;
	private Vector2 levelSize = new Vector2(40, 30);
	
	private Camera camera;
	private SpriteBatch spriteBatch;
	
	private EventManager eventManager = new EventManager();
	private EntityManager entityManager = new EntityManager(eventManager);
	private SpriteCache<SpriteKey> spriteCache = new SpriteCache<SpriteKey>();
	private EntityFactory entityFactory = new EntityFactory(spriteCache);
	private Entity shooter;
	
	@Override
	public void create() {
		camera = new OrthographicCamera(levelSize.x * DRAW_SCALE, levelSize.y * DRAW_SCALE);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		spriteBatch = new SpriteBatch();
		
		loadSprites();
		shooter = entityFactory.createShooter(new Vector2(0, 0));
		entityManager.add(shooter);
	}

	@Override
	public void render() {
		float delta = Gdx.graphics.getDeltaTime();
		
		handleInput(delta);
		entityManager.update();
		camera.update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		for (Entity entity : entityManager.getAll()) {
			entity.update(delta);
			
			// Restrict entity in bounds
			if (entity.has(TransformPart.class) && entity.has(BoundsPart.class)) {
				Vector2 size = entity.get(TransformPart.class).getSize();
				Vector2 position = entity.get(TransformPart.class).getPosition();
				Vector2 newPosition = position.cpy();
				
				if (position.x < 0) {
					newPosition.x = 0;
				}
				if (position.x + size.x > levelSize.x) {
					newPosition.x = levelSize.x - size.x;
				}
				if (position.y < 0) {
					newPosition.y = 0;
				}
				if (position.y + size.y > levelSize.y) {
					newPosition.y = levelSize.y - size.y;
				}
				
				entity.get(TransformPart.class).setPosition(newPosition);
			}
			
			// Draw entity
			if (entity.has(TransformPart.class) && entity.has(DrawablePart.class)) {
				Vector2 size = entity.get(TransformPart.class).getSize().scl(DRAW_SCALE);
				Vector2 position = entity.get(TransformPart.class).getPosition().scl(DRAW_SCALE);
				spriteBatch.draw(entity.get(DrawablePart.class).getTextureRegion(), 
						position.x, position.y, size.x, size.y);
			}
		}
		spriteBatch.end();
	}
	
	private void loadSprites() {
		spriteCache.add(SpriteKey.SHOOTER, "images/shooter.png");
		spriteCache.add(SpriteKey.BULLET, "images/bullet.png");
	}
	
	private void handleInput(float delta) {
		Vector2 moveDirection = new Vector2(0, 0);
		
		if (Gdx.input.isKeyPressed(Keys.A)) {
			moveDirection.x -= 1;
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			moveDirection.x += 1;
		}
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			SpawnerPart spawnerPart = shooter.get(SpawnerPart.class);
			if (spawnerPart.canSpawn()) {
				Vector2 mouseWorldCoords = getScreenToWorldCoords(Gdx.input.getX(), Gdx.input.getY());
				Entity bullet = spawnerPart.createSpawn();
				Vector2 shooterCenter = shooter.get(TransformPart.class).getCenter();
				// TODO: Center in center
				bullet.get(TransformPart.class).setPosition(shooterCenter);
				Vector2 offset = mouseWorldCoords.cpy().sub(shooterCenter);
				bullet.get(TranslatePart.class).setVelocity(offset);
				entityManager.add(bullet);
			}
		}
		
		if (moveDirection.len() > 0) {
			Vector2 velocity = VectorUtils.lengthen(moveDirection, shooter.get(SpeedPart.class).getSpeed() * delta);
			Vector2 newPosition = shooter.get(TransformPart.class).getPosition().add(velocity);
			shooter.get(TransformPart.class).setPosition(newPosition);
		}
	}
	
	private Vector2 getScreenToWorldCoords(int screenX, int screenY) {
		Vector3 worldCoords3 = new Vector3(screenX, screenY, 0);
		camera.unproject(worldCoords3);
		worldCoords3.scl(1 / DRAW_SCALE);
		Vector2 worldCoords = new Vector2(worldCoords3.x, worldCoords3.y);
		return worldCoords;
	}
	
}
