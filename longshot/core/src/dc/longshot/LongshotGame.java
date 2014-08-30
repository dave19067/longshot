package dc.longshot;

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
import dc.longshot.parts.BouncePart;
import dc.longshot.parts.BoundsPart;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.HealthPart;
import dc.longshot.parts.SpawnerPart;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.util.EventManager;
import dc.longshot.util.RandomUtils;
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
	private CollisionManager collisionManager = new CollisionManager(eventManager);
	
	private Entity shooter;
	
	@Override
	public void create() {
		camera = new OrthographicCamera(levelSize.x * DRAW_SCALE, levelSize.y * DRAW_SCALE);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		spriteBatch = new SpriteBatch();
		
		loadSprites();
		
		shooter = entityFactory.createShooter(new Vector2(0, 0));
		entityManager.add(shooter);
		
		// TODO: temporary
		for (int i = 0; i < 5; i++)
			spawnEnemy(entityFactory.createBomb());
	}

	@Override
	public void render() {
		float delta = Gdx.graphics.getDeltaTime();
		
		handleInput(delta);
		entityManager.update();
		camera.update();
		
		collisionManager.checkCollisions(entityManager.getAll());
		
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
				
				entity.get(TransformPart.class).setPosition(newPosition);
			}
			
			// Bounce entity off walls
			if (entity.has(TransformPart.class) && entity.has(TranslatePart.class) && entity.has(BouncePart.class)) {
				Rectangle boundingBox = entity.get(TransformPart.class).getBoundingBox();
				Vector2 velocity = entity.get(TranslatePart.class).getVelocity();
				Vector2 newVelocity = velocity.cpy();

				if (boundingBox.x + boundingBox.width >= levelSize.x) {
					// right bounds
					if (velocity.x > 0)
					{
						newVelocity.x *= -1;
					}
				}
				if (boundingBox.x <= 0) {
					// left bounds
					if (velocity.x < 0) {
						newVelocity.x *= -1;
					}
				}
				
				entity.get(TranslatePart.class).setVelocity(newVelocity);
			}
			
			// Go through collisions
			for (Entity other : collisionManager.getCollisions(entity)) {
				if (other.has(CollisionTypePart.class)) {
					if (entity.has(DamageOnCollisionPart.class) && other.has(HealthPart.class)) {
						DamageOnCollisionPart damageOnCollisionPart = entity.get(DamageOnCollisionPart.class);
						if (damageOnCollisionPart.getCollisionTypes().contains(
								other.get(CollisionTypePart.class).getCollisionType())) {
							other.get(HealthPart.class).subtract(damageOnCollisionPart.getDamage());
						}
					}
				}
			}
			
			// Remove entities with no health
			if (entity.has(HealthPart.class) && !entity.get(HealthPart.class).isAlive()) {
				entityManager.remove(entity);
			}
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
	
	private void spawnEnemy(Entity entity) {
		float spawnX = RandomUtils.nextFloat(0, levelSize.x - entity.get(TransformPart.class).getSize().x);
		Vector2 spawnPosition = new Vector2(spawnX, levelSize.y);
		entity.get(TransformPart.class).setPosition(spawnPosition);
		float destX = RandomUtils.nextFloat(0, levelSize.x - entity.get(TransformPart.class).getSize().x);
		Vector2 destPosition = new Vector2(destX, 0);
		Vector2 offset = destPosition.cpy().sub(spawnPosition);
		entity.get(TranslatePart.class).setVelocity(offset);
		entityManager.add(entity);
	}
	
	private Vector2 getScreenToWorldCoords(int screenX, int screenY) {
		Vector3 worldCoords3 = new Vector3(screenX, screenY, 0);
		camera.unproject(worldCoords3);
		worldCoords3.scl(1 / DRAW_SCALE);
		Vector2 worldCoords = new Vector2(worldCoords3.x, worldCoords3.y);
		return worldCoords;
	}
	
}
