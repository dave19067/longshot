package dc.longshot.epf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import dc.longshot.util.Cloning;
import dc.longshot.util.XmlUtils;

public final class EntityCache {

	private static final String ENTITY_EXTENSION = ".xml";
	private final XmlAdapter<EntityAdapted, Entity> ENTITY_ADAPTER = new EntityAdapter(this);
	private final String root;
	private final Class<?>[] boundClasses;
	private final Map<String, Entity> cache = new HashMap<String, Entity>();
	
	public EntityCache(final String root, final Class<?>[] boundClasses) {
		this.root = root;
		this.boundClasses = boundClasses;
	}
	
	public final Entity create(final String entityType) {
		if (!cache.containsKey(entityType)) {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(root + entityType + ENTITY_EXTENSION);
			}
			catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
			try {
				Entity entity = XmlUtils.unmarshal(inputStream, ENTITY_ADAPTER, boundClasses);
				cache.put(entityType,  entity);
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Could not create entity " + entityType, e);
			}
		}
		return Cloning.clone(cache.get(entityType));
	}
	
}
