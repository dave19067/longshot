package dc.longshot.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import dc.longshot.epf.Entity;
import dc.longshot.util.Timer;
import dc.longshot.xmladapters.TimerAdapter;

@XmlRootElement
public final class WeaponPart {

	@XmlElement
	private String entityType;
	private final List<Entity> spawns = new ArrayList<Entity>();
	@XmlElement
	private int maxSpawns;
	@XmlJavaTypeAdapter(TimerAdapter.class)
	private Timer spawnTimer;
	
	public WeaponPart() {
	}
	
	public final String getEntityType() {
		return entityType;
	}
	
	public final void addSpawn(final Entity spawn) {
		spawns.add(spawn);
	}
	
	public final boolean canSpawn() {
		return spawnTimer.isElapsed() && spawns.size() < maxSpawns;
	}
	
	public final void reset() {
		spawnTimer.reset();
	}
	
	public final void update(final float delta) {
		spawnTimer.tick(delta);
		Iterator<Entity> it = spawns.iterator();
		while (it.hasNext()) {
			Entity spawn = it.next();
			if (!spawn.isActive()) {
				it.remove();
			}
		}
	}
	
}
