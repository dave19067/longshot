package dc.longshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.models.CollisionType;
import dc.longshot.models.EntityType;
import dc.longshot.models.Level;
import dc.longshot.models.SpawnInfo;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;

public class LevelController {
	
	private EntityManager entityManager;
	private EntityFactory entityFactory;
	private Level level;
	private List<SpawnInfo> spawnInfos = new ArrayList<SpawnInfo>();
	private float time = 0;

	public LevelController(EntityManager entityManager, EntityFactory entityFactory, Level level) {
		this.entityManager = entityManager;
		this.entityFactory = entityFactory;
		this.level = level;
		
		Map<EntityType, Integer> spawns = level.getSpawns();
		for (Entry<EntityType, Integer> entry : spawns.entrySet()) {
			for (int i = 0; i < entry.getValue(); i++) {
				SpawnInfo spawnInfo = new SpawnInfo(entry.getKey(), MathUtils.random(0, level.getSpawnDuration()));
				spawnInfos.add(spawnInfo);
			}
		}
		
		Collections.sort(spawnInfos, new Comparator<SpawnInfo>() {
			@Override
			public int compare(SpawnInfo spawnInfo1, SpawnInfo spawnInfo2) {
				return Float.compare(spawnInfo1.getSpawnTime(), spawnInfo2.getSpawnTime());
			}
		});
	}
	
	public void update(float delta) {
		time += delta;
		Iterator<SpawnInfo> it = spawnInfos.iterator();
		
		for (int i = 0; i < spawnInfos.size(); i++) {
			SpawnInfo spawnInfo = it.next();
			if (spawnInfo.getSpawnTime() <= time) {
				Entity spawn = entityFactory.create(spawnInfo.getEntityType());
				spawnEnemy(spawn);
				it.remove();
			}
			else {
				break;
			}
		}
	}
	
	public boolean isComplete() {
		boolean enemiesExist = false;
		
		for (Entity entity : entityManager.getAll()) {
			if (entity.has(CollisionTypePart.class) && entity.get(CollisionTypePart.class)
					.getCollisionType() == CollisionType.ENEMY) {
				enemiesExist = true;
				break;
			}
		}
		
		return !enemiesExist && spawnInfos.size() <= 0;
	}

	private void spawnEnemy(Entity entity) {
		Vector2 levelSize = level.getSize();
		
		// Get the spawn position, which is a random point from the skyline
		Vector2 size = entity.get(TransformPart.class).getSize();
		float spawnX = MathUtils.random(0, levelSize.x - size.y);
		Vector2 spawnPosition = new Vector2(spawnX, levelSize.y);
		TransformPart transform = entity.get(TransformPart.class);
		transform.setPosition(spawnPosition);
		
		// Get the destination, which is a random point on the ground
		float destX = MathUtils.random(0, levelSize.x - size.y);
		Vector2 destPosition = new Vector2(destX, 0);
		
		// Find the direction to get from the entity spawn position to the destination
		Vector2 offset = destPosition.cpy().sub(spawnPosition);
		TranslatePart translate = entity.get(TranslatePart.class);
		translate.setVelocity(offset);
		
		// If the enemy is partially in bounds, move to just out of bounds using the negative velocity
		float unboundedOverlapY = levelSize.y - transform.getBoundingBox().y;
		Vector2 velocity = translate.getVelocity();
		Vector2 outOfBoundsOffset = velocity.cpy().scl(unboundedOverlapY / velocity.y);
		transform.setPosition(spawnPosition.cpy().add(outOfBoundsOffset));
		
		entityManager.add(entity);
	}
	
}
