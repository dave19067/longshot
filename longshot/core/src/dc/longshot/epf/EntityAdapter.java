package dc.longshot.epf;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Used to marshal/unmarshal between the serializable {@link EntityAdapted} and {@link Entity}.
 * @author David Chen
 *
 */
public final class EntityAdapter extends XmlAdapter<EntityAdapted, Entity> {
	
	private final EntityCache entityCache;
	
	public EntityAdapter(final EntityCache entityCache) {
		this.entityCache = entityCache;
	}

	@Override
	public final EntityAdapted marshal(final Entity entity) {
		EntityAdapted entityAdapted = new EntityAdapted(entity.getAll());
		return entityAdapted;
	}

	@Override
	public final Entity unmarshal(final EntityAdapted entityAdapted) {
		Entity entity;
		String parentEntityType = entityAdapted.getParentEntityType();
		if (parentEntityType != null) {
			entity = entityCache.create(parentEntityType);
		}
		else {
			entity = new Entity();
		}
		for (Object part : entityAdapted.getParts()) {
			if (entity.has(part.getClass())) {
				entity.detach(part.getClass());
			}
			entity.attach(part);
		}
		return entity;
	}

}
