package dc.longshot.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import dc.longshot.epf.Entity;

@XmlRootElement
public final class FollowerPart {

	@XmlElementWrapper
	private List<String> followerTypeNames;
	private List<Entity> followers;
	
	public FollowerPart() {
	}
	
	public final List<String> getFollowerTypeNames() {
		return followerTypeNames;
	}
	
	public final List<Entity> getFollowers() {
		return followers;
	}
	
	public final void setFollowers(final List<Entity> followers) {
		this.followers = followers;
	}
	
}
