package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;
import dc.longshot.geometry.PolygonUtils;

public final class FollowerPart extends Part {

	private final Entity follower;
	
	public FollowerPart(final Entity follower) {
		this.follower = follower;
	}
	
	public final Entity getFollower() {
		return follower;
	}
	
	@Override
	public final void initialize() {
		TransformPart transformPart = entity.get(TransformPart.class);
		Vector2 entityCenter = entity.get(TransformPart.class).getGlobalCenter();
		TransformPart followerTransformPart = follower.get(TransformPart.class);
		Vector2 followerSize = followerTransformPart.getBoundingSize();
		Vector2 followerPosition = PolygonUtils.relativeCenter(entityCenter, followerSize);
		followerPosition.add(new Vector2(0, -transformPart.getSize().x));
		followerTransformPart.setPosition(followerPosition);
	}
	
}
