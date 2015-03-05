package dc.longshot.level;

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
import dc.longshot.epf.EntityCache;
import dc.longshot.epf.EntityManager;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.models.Alliance;
import dc.longshot.models.Level;
import dc.longshot.models.SpawningType;
import dc.longshot.parts.AlliancePart;
import dc.longshot.parts.SpawningPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;

public final class LevelController {

	private final EntityCache entityLoader;
	private final EntityManager entityManager;
	private final Level level;
	private final List<SpawnInfo> spawnInfos = new ArrayList<SpawnInfo>();
	private float time = 0;

	public LevelController(final EntityCache entityLoader, final EntityManager entityManager, final Level level) {
		this.entityLoader = entityLoader;
		this.entityManager = entityManager;
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
		return !enemiesExist && spawnInfos.isEmpty();
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
		Map<String, Integer> spawns = level.getSpawns();
		for (Entry<String, Integer> entry : spawns.entrySet()) {
			for (int i = 0; i < entry.getValue(); i++) {
				SpawnInfo spawnInfo = new SpawnInfo();
				spawnInfo.entityType = entry.getKey();
				spawnInfo.time = MathUtils.random(0, level.getSpawnDuration());
				spawnInfos.add(spawnInfo);
			}
		}
		
		Collections.sort(spawnInfos, new Comparator<SpawnInfo>() {
			@Override
			public int compare(final SpawnInfo spawnInfo1, final SpawnInfo spawnInfo2) {
				return Float.compare(spawnInfo1.time, spawnInfo2.time);
			}
		});
	}
	
	private void spawn(final SpawnInfo spawnInfo) {
		Entity spawn = entityLoader.create(spawnInfo.entityType);
		if (spawn.has(SpawningPart.class)) {
			SpawningType spawningType = spawn.get(SpawningPart.class).getSpawningType();
			switch (spawningType) {
			case ABOVE:
				placeAbove(spawn);
				break;
			case MIDAIR:
				placeInSpace(spawn);
				break;
			case DOWNWARD:
				placeAbove(spawn);
				Rectangle boundsBox = level.getBoundsBox();
				LevelUtils.setupBottomDestination(spawn, boundsBox);
				moveToOutOfBounds(spawn, boundsBox);
				break;
			case SIDE_BOUNCE:
				setupBouncer(spawn);
				break;
			default:
				throw new UnsupportedOperationException("Spawning type " + spawningType + " not supported");
			}	
		}
		else {
			throw new IllegalArgumentException("Could not spawn " + spawnInfo.entityType
					+ ".  It does not have a spawning type");
		}
		entityManager.add(spawn);
	}

	private void placeAbove(final Entity entity) {
		Rectangle boundsBox = level.getBoundsBox();
		TransformPart transform = entity.get(TransformPart.class);
		Vector2 size = transform.getStartingSize();
		float spawnX = MathUtils.random(boundsBox.x, PolygonUtils.right(boundsBox) - size.y);
		Vector2 spawnPosition = new Vector2(spawnX, PolygonUtils.top(boundsBox));
		transform.setPosition(spawnPosition);
	}

	private void setupBouncer(final Entity entity) {
		Rectangle boundsBox = level.getBoundsBox();
		TransformPart transform = entity.get(TransformPart.class);
		Vector2 size = transform.getStartingSize();
		float spawnX;
		float minVelocityX = 4;
		float maxVelocityX = 16;
		float velocityX;
		if (MathUtils.randomBoolean()) {
			spawnX = boundsBox.x - size.x;
			velocityX = MathUtils.random(minVelocityX, maxVelocityX);
		}
		else {
			spawnX = PolygonUtils.right(boundsBox);
			velocityX = -MathUtils.random(minVelocityX, maxVelocityX);
		}
		entity.get(TranslatePart.class).setVelocity(new Vector2(velocityX, 0));
		float boundsHeightRatio = 1 / 4f;
		float spawnY = MathUtils.random(boundsBox.y, PolygonUtils.top(boundsBox) * boundsHeightRatio);
		Vector2 spawnPosition = new Vector2(spawnX, spawnY);
		transform.setPosition(spawnPosition);
	}

	private void placeInSpace(final Entity entity) {
		Rectangle boundsBox = level.getBoundsBox();
		Vector2 size = entity.get(TransformPart.class).getStartingSize();
		float spawnX = MathUtils.random(boundsBox.x, PolygonUtils.right(boundsBox) - size.x);
		float boundsHeightRatio = 2 / 3f;
		float spawnY = MathUtils.random(boundsBox.y + boundsBox.height * boundsHeightRatio, 
				PolygonUtils.top(boundsBox) - size.y);
		Vector2 spawnPosition = new Vector2(spawnX, spawnY);
		TransformPart transform = entity.get(TransformPart.class);
		transform.setPosition(spawnPosition);
	}
	
	private void moveToOutOfBounds(final Entity entity, final Rectangle boundsBox) {
		// If the spawn is partially in bounds, move to just out of bounds using the negative velocity
		TransformPart transformPart = entity.get(TransformPart.class);
		float unboundedOverlapY = PolygonUtils.top(boundsBox) - transformPart.getPosition().y/* TODO: transformPart.getBoundingBox().y*/;
		TranslatePart translate = entity.get(TranslatePart.class);
		Vector2 velocity = translate.getVelocity();
		Vector2 outOfBoundsOffset = velocity.cpy().scl(unboundedOverlapY / velocity.y);
		transformPart.setPosition(transformPart.getPosition().add(outOfBoundsOffset));
	}

	private class SpawnInfo {
	
		private String entityType;
		private float time;
		
	}
	
}
