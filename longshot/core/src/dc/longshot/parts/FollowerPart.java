package dc.longshot.parts;

import java.util.List;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;

public final class FollowerPart extends Part {

	private final List<Entity> followers;
	
	public FollowerPart(final List<Entity> followers) {
		this.followers = followers;
	}
	
	public final List<Entity> getFollowers() {
		return followers;
	}
	
}
