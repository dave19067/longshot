package dc.longshot.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.models.Alliance;
import dc.longshot.models.EntityType;
import dc.longshot.models.Level;
import dc.longshot.parts.AlliancePart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;

public final class LevelController {
	
	private final EntityManager entityManager;
	private final EntityFactory entityFactory;
	private final Level level;
	private final List<SpawnInfo> spawnInfos = new ArrayList<SpawnInfo>();
	private float time = 0;

	public LevelController(final EntityManager entityManager, final EntityFactory entityFactory, final Level level) {
		this.entityManager = entityManager;
		this.entityFactory = entityFactory;
		this.level = level;
		
		generateSpawnInfos();
	}
	
	public final boolean isComplete() {
		boolean enemiesExist = false;
		for (Entity entity : entityManager.getAll()) {
			if (entity.hasActive(AlliancePart.class) && entity.get(AlliancePart.class).getAlliance() == Alliance.ENEMY) {
				enemiesExist = true;
				break;
			}
		}
		return !enemiesExist && spawnInfos.size() <= 0;
	}
	
	public final void update(final float delta) {
		time += delta;
		Iterator<SpawnInfo> it = spawnInfos.iterator();
		
		while (it.hasNext()) {
			SpawnInfo spawnInfo = it.next();
			if (spawnInfo.time <= time) {
				spawn(spawnInfo);
				it.remove();
			}
			else {
				break;
			}
		}
	}
	
	private void generateSpawnInfos() {
		Map<EntityType, Integer> spawns = level.getSpawns();
		for (Entry<EntityType, Integer> entry : spawns.entrySet()) {
			for (int i = 0; i < entry.getValue(); i++) {
				SpawnInfo spawnInfo = new SpawnInfo();
				spawnInfo.entityType = entry.getKey();
				spawnInfo.time = MathUtils.random(0, level.getSpawnDuration());
				spawnInfos.add(spawnInfo);
			}
		}
		
		Collections.sort(spawnInfos, new Comparator<SpawnInfo>() {
			@Override
			public int compare(SpawnInfo spawnInfo1, SpawnInfo spawnInfo2) {
				return Float.compare(spawnInfo1.time, spawnInfo2.time);
			}
		});
	}
	
	private void spawn(SpawnInfo spawnInfo) {
		Entity spawn = entityFactory.create(spawnInfo.entityType);
		switch (spawnInfo.entityType) {
		case MISSILE:
		case NUKE:
			placeAbove(spawn);
			setupDestination(spawn);
			break;
		case CATERPILLAR:
			placeAbove(spawn);
			break;
		case UFO:
			placeInSpace(spawn);
			break;
		}
		entityManager.add(spawn);
	}

	private void placeAbove(final Entity entity) {
		Rectangle levelBoundsBox = level.getBoundsBox();
		
		// Get the spawn position, which is a random point from the skyline
		TransformPart transform = entity.get(TransformPart.class);
		Vector2 size = transform.getSize();
		float spawnX = MathUtils.random(levelBoundsBox.x, PolygonUtils.right(levelBoundsBox) - size.y);
		Vector2 spawnPosition = new Vector2(spawnX, PolygonUtils.top(levelBoundsBox));
		transform.setPosition(spawnPosition);
	}
	
	private void setupDestination(Entity entity) {
		Rectangle levelBoundsBox = level.getBoundsBox();
		TransformPart transformPart = entity.get(TransformPart.class);
		
		// Get the destination, which is a random point on the ground
		float destX = MathUtils.random(levelBoundsBox.x, PolygonUtils.right(levelBoundsBox) - transformPart.getSize().x);
		Vector2 destPosition = new Vector2(destX, 0);
		
		// Find the direction to get from the entity spawn position to the destination
		Vector2 position = transformPart.getPosition();
		Vector2 offset = destPosition.cpy().sub(position);
		TranslatePart translate = entity.get(TranslatePart.class);
		translate.setVelocity(offset);
		
		// If the spawn is partially in bounds, move to just out of bounds using the negative velocity
		float unboundedOverlapY = PolygonUtils.top(levelBoundsBox) - transformPart.getBoundingBox().y;
		Vector2 velocity = translate.getVelocity();
		Vector2 outOfBoundsOffset = velocity.cpy().scl(unboundedOverlapY / velocity.y);
		transformPart.setPosition(position.cpy().add(outOfBoundsOffset));
	}

	private void placeInSpace(final Entity entity) {
		// Get the spawn position, which is a random point above the halfline
		Rectangle boundsBox = level.getBoundsBox();
		Vector2 size = entity.get(TransformPart.class).getSize();
		float spawnX = MathUtils.random(boundsBox.x, PolygonUtils.right(boundsBox) - size.x);
		float spawnY = MathUtils.random(boundsBox.y + 2 / 3f * boundsBox.height, 
				PolygonUtils.top(boundsBox) - size.y);
		Vector2 spawnPosition = new Vector2(spawnX, spawnY);
		TransformPart transform = entity.get(TransformPart.class);
		transform.setPosition(spawnPosition);
	}

	private class SpawnInfo {
	
		private EntityType entityType;
		private float time;
		
	}
	
}
