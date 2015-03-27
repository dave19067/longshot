package dc.longshot.level;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityCache;
import dc.longshot.game.EntityUtils;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.WaypointsPart;
import dc.longshot.parts.WeaponPart;

public final class LevelUtils {

	private LevelUtils() {
	}
	
	public static final float getPathDistance(final Entity entity) {
		WaypointsPart waypointsPart = entity.get(WaypointsPart.class);
		TransformPart transformPart = entity.get(TransformPart.class);
		List<Vector2> waypoints = waypointsPart.getWaypoints();
		List<Vector2> path = new ArrayList<Vector2>();
		path.add(transformPart.getCenter());
		path.addAll(waypoints);
		float distance = 0;
		for (int i = 1; i < path.size(); i++) {
			distance += VectorUtils.offset(path.get(i - 1), path.get(i)).len();
		}
		return distance;
	}
	
	public static final Entity createWeaponSpawn(final Entity entity, final EntityCache entityCache) {
		WeaponPart weaponPart = entity.get(WeaponPart.class);
		weaponPart.reset();
		Entity spawn = entityCache.create(weaponPart.getEntityType());
		TransformPart spawnTransform = spawn.get(TransformPart.class);
		TransformPart transform = entity.get(TransformPart.class);
		spawnTransform.setPosition(PolygonUtils.relativeCenter(transform.getCenter(), spawnTransform.getSize()));
		weaponPart.addSpawn(spawn);
		return spawn;
	}
	
	public static final void setupBottomDestination(final Entity entity, final Rectangle boundsBox) {
		TransformPart transformPart = entity.get(TransformPart.class);
		
		// Get the destination, which is a random point on the ground
		float destX = MathUtils.random(boundsBox.x, PolygonUtils.right(boundsBox) - transformPart.getSize().x);
		Vector2 destPosition = new Vector2(destX, 0);
		
		// Find the direction to get from the entity spawn position to the destination
		Vector2 offset = VectorUtils.offset(transformPart.getPosition(), destPosition);
		EntityUtils.setDirection(entity, offset);
	}
	
}
