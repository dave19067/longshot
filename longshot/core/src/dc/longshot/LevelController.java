package dc.longshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.util.RandomUtils;

public class LevelController {
	
	private EntityFactory entityFactory;
	private EntityManager entityManager;
	private Level level;
	private List<Float> spawnTimes = new ArrayList<Float>();
	private float time = 0;

	public LevelController(EntityFactory entityFactory, EntityManager entityManager, Level level) {
		this.entityFactory = entityFactory;
		this.entityManager = entityManager;
		this.level = level;
		
		for (int i = 0; i < level.getEnemySpawns(); i++) {
			spawnTimes.add(RandomUtils.nextFloat(0, level.getSpawnDuration()));
		}
		Collections.sort(spawnTimes);
	}
	
	public void update(float delta) {
		time += delta;
		Iterator<Float> it = spawnTimes.iterator();
		while (it.hasNext()) {
			Float spawnTime = it.next();
			if (spawnTime <= time) {
				spawnEnemy(entityFactory.createBomb());
				it.remove();
			}
			else {
				break;
			}
		}
	}

	private void spawnEnemy(Entity entity) {
		Vector2 levelSize = level.getSize();
		Vector2 size = entity.get(TransformPart.class).getSize();
		float spawnX = RandomUtils.nextFloat(0, levelSize.x - size.x);
		Vector2 spawnPosition = new Vector2(spawnX, levelSize.y - size.y);
		entity.get(TransformPart.class).setPosition(spawnPosition);
		float destX = RandomUtils.nextFloat(0, levelSize.x - size.x);
		Vector2 destPosition = new Vector2(destX, 0);
		Vector2 offset = destPosition.cpy().sub(spawnPosition);
		entity.get(TranslatePart.class).setVelocity(offset);
		entityManager.add(entity);
	}
	
}
