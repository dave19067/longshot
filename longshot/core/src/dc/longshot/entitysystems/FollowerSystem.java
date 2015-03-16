package dc.longshot.entitysystems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityCache;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.FollowerPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.WaypointsPart;

public final class FollowerSystem extends EntitySystem {

	private final EntityCache entityCache;
	
	public FollowerSystem(final EntityCache entityCache) {
		this.entityCache = entityCache;
	}
	
	@Override
	public void initialize(final Entity entity) {
		if (entity.hasActive(FollowerPart.class)) {
			FollowerPart followerPart = entity.get(FollowerPart.class);
			List<Entity> followers = new ArrayList<Entity>();
			for (String followerTypeName : followerPart.getFollowerTypeNames()) {
				followers.add(entityCache.create(followerTypeName));
			}
			followerPart.setFollowers(followers);
			float followerSizeY = followers.get(0).get(TransformPart.class).getSize().y;
			float offsetY = followerSizeY / 2 + entity.get(TransformPart.class).getSize().y / 2;
			for (Entity follower : followers) {
				Vector2 entityCenter = entity.get(TransformPart.class).getCenter();
				TransformPart followerTransformPart = follower.get(TransformPart.class);
				Vector2 followerSize = followerTransformPart.getSize();
				Vector2 followerPosition = PolygonUtils.relativeCenter(entityCenter, followerSize);
				followerPosition.add(0, offsetY);
				followerTransformPart.setPosition(followerPosition);
				float endBuffer = VectorUtils.offset(entityCenter, followerPosition /* TODO: followerTransformPart.getCenter()*/)
						.dst(new Vector2());
				follower.get(WaypointsPart.class).setEndBuffer(endBuffer);
				offsetY += followerSize.y;
			}
		}
	}

	@Override
	public void update(final float delta, final Entity entity) {
		if (entity.hasActive(FollowerPart.class)) {
			for (Entity follower : entity.get(FollowerPart.class).getFollowers()) {
				Vector2 waypoint = entity.get(TransformPart.class).getCenter();
				follower.get(WaypointsPart.class).addWaypoint(waypoint);
			}
		}
	}

}
