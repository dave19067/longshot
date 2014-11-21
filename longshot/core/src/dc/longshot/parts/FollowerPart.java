package dc.longshot.parts;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;
import dc.longshot.geometry.PolygonUtils;

public final class FollowerPart extends Part {

	private final List<Entity> followers;
	
	public FollowerPart(final List<Entity> followers) {
		this.followers = followers;
	}
	
	public final List<Entity> getFollowers() {
		return followers;
	}
	
	@Override
	public final void initialize() {
		float offsetY = entity.get(TransformPart.class).getSize().y;
		for (Entity follower : followers) {
			Vector2 entityCenter = entity.get(TransformPart.class).getGlobalCenter();
			TransformPart followerTransformPart = follower.get(TransformPart.class);
			Vector2 followerSize = followerTransformPart.getBoundingSize();
			Vector2 followerPosition = PolygonUtils.relativeCenter(entityCenter, followerSize);
			followerPosition.add(0, offsetY);
			followerTransformPart.setPosition(followerPosition);
			offsetY += followerSize.y;
		}
	}
	
}
