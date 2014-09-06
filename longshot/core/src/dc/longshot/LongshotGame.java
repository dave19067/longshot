package dc.longshot;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.SpriteKey;
import dc.longshot.models.Bound;
import dc.longshot.models.CollisionType;
import dc.longshot.parts.BouncePart;
import dc.longshot.parts.BoundsDiePart;
import dc.longshot.parts.BoundsPart;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.HealthPart;
import dc.longshot.parts.SpawnerPart;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TimedDeathPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.util.EventManager;
import dc.longshot.util.VectorUtils;

public class LongshotGame extends ApplicationAdapter {
	
	public static final float DRAW_SCALE = 64;
	
	private Camera camera;
	private SpriteBatch spriteBatch;
	
	private EventManager eventManager = new EventManager();
	private EntityManager entityManager = new EntityManager(eventManager);
	private SpriteCache<SpriteKey> spriteCache = new SpriteCache<SpriteKey>();
	private EntityFactory entityFactory = new EntityFactory(spriteCache);
	private CollisionManager collisionManager = new CollisionManager(eventManager);
	private LevelController levelController;
	
	private int health = 3;
	
	private Level level;
	private Entity shooter;
	
	@Override
	public void create() {
		level = new Level(new Vector2(40, 30), 60, 30);
		Vector2 levelSize = level.getSize();
		camera = new OrthographicCamera(levelSize.x * DRAW_SCALE, levelSize.y * DRAW_SCALE);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		spriteBatch = new SpriteBatch();
		levelController = new LevelController(entityFactory, entityManager, level);
		
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
		collisionManager.checkCollisions(entityManager.getAll());
		levelController.update(delta);
		
		for (Entity entity : entityManager.getAll()) {
			entity.update(delta);
			
			List<Bound> bounds = new ArrayList<Bound>();
			if (entity.has(TransformPart.class)) {
				bounds = checkOutOfBounds(entity.get(TransformPart.class).getBoundingBox());
			}
			
			// Bounce entity off walls
			if (entity.has(TransformPart.class) && entity.has(TranslatePart.class) && entity.has(BouncePart.class)) {
				Vector2 velocity = entity.get(TranslatePart.class).getVelocity();
				Vector2 newVelocity = velocity.cpy();

				if (bounds.contains(Bound.RIGHT)) {
					if (velocity.x > 0)
					{
						newVelocity.x *= -1;
					}
				}
				if (bounds.contains(Bound.LEFT)) {
					// left bounds
					if (velocity.x < 0) {
						newVelocity.x *= -1;
					}
				}
				
				entity.get(TranslatePart.class).setVelocity(newVelocity);
			}
			
			// Restrict entity in bounds
			if (entity.has(TransformPart.class) && entity.has(BoundsPart.class)) {
				Vector2 size = entity.get(TransformPart.class).getSize();
				Vector2 position = entity.get(TransformPart.class).getPosition();
				Vector2 newPosition = position.cpy();
				
				if (bounds.contains(Bound.LEFT)) {
					newPosition.x = 0;
				}
				if (bounds.contains(Bound.RIGHT)) {
					newPosition.x = level.getSize().x - size.x;
				}
				
				entity.get(TransformPart.class).setPosition(newPosition);
				bounds = checkOutOfBounds(entity.get(TransformPart.class).getBoundingBox());
			}
			
			// Go through collisions
			for (Entity other : collisionManager.getCollisions(entity)) {
				if (other.isActive() && other.has(CollisionTypePart.class)) {
					if (entity.has(DamageOnCollisionPart.class) && other.has(HealthPart.class)) {
						// Damage the other entity
						DamageOnCollisionPart damageOnCollisionPart = entity.get(DamageOnCollisionPart.class);
						if (damageOnCollisionPart.getCollisionTypes().contains(
								other.get(CollisionTypePart.class).getCollisionType())) {
							other.get(HealthPart.class).subtract(damageOnCollisionPart.getDamage());
						}
					}
				}
			}
			
			if (entity.has(CollisionTypePart.class))
			{
				if (entity.get(CollisionTypePart.class).getCollisionType() == CollisionType.ENEMY
						&& bounds.contains(Bound.BOTTOM))
				{
					health--;
				}
			}
			
			// Remove if no health
			if (entity.has(HealthPart.class) && !entity.get(HealthPart.class).isAlive()) {
				entityManager.remove(entity);
			}
			// Remove if out of bounds
			if (entity.has(BoundsDiePart.class)) {
				if (bounds.size() > 0) {
					entityManager.remove(entity);
				}
			}
			// Remove if timed death
			if (entity.has(TimedDeathPart.class) && entity.get(TimedDeathPart.class).isDead()) {
				entityManager.remove(entity);
			}
		}
		
		if (health <= 0)
		{
			Gdx.app.exit();
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		for (Entity entity : entityManager.getAll()) {
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
				Vector2 bulletPosition = VectorUtils.relativeCenter(shooterCenter, 
						bullet.get(TransformPart.class).getSize());
				bullet.get(TransformPart.class).setPosition(bulletPosition);
				Vector2 offset = mouseWorldCoords.cpy().sub(bulletPosition);
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
	
	private List<Bound> checkOutOfBounds(Rectangle collisionBox) {
		List<Bound> bounds = new ArrayList<Bound>();
		Vector2 levelSize = level.getSize();
		
		if (collisionBox.x < 0) {
			bounds.add(Bound.LEFT);
		}
		else if (collisionBox.x + collisionBox.width > levelSize.x) {
			bounds.add(Bound.RIGHT);
		}
		if (collisionBox.y < 0) {
			bounds.add(Bound.BOTTOM);
		}
		else if (collisionBox.y + collisionBox.height > levelSize.y) {
			bounds.add(Bound.TOP);
		}
		
		return bounds;
	}
	
	private Vector2 getScreenToWorldCoords(int screenX, int screenY) {
		Vector3 worldCoords3 = new Vector3(screenX, screenY, 0);
		camera.unproject(worldCoords3);
		worldCoords3.scl(1 / DRAW_SCALE);
		Vector2 worldCoords = new Vector2(worldCoords3.x, worldCoords3.y);
		return worldCoords;
	}
	
}
