package dc.longshot.epf;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Used to marshal/unmarshal between the serializable {@link EntityAdapted} and {@link Entity}.
 * @author David Chen
 *
 */
public final class EntityAdapter extends XmlAdapter<EntityAdapted, Entity> {
	
	private final EntityCache entityCache;
	private final Converter[] converters;
	
	public EntityAdapter(final EntityCache entityCache, final Converter[] converters) {
		this.entityCache = entityCache;
		this.converters = converters;
	}

	@Override
	public final EntityAdapted marshal(final Entity entity) {
		EntityAdapted entityAdapted = new EntityAdapted(entity.getAll());
		return entityAdapted;
	}

	@Override
	public final Entity unmarshal(final EntityAdapted entityAdapted) {
		Entity entity = new Entity();
		for (Object part : entityAdapted.getParts()) {
			for (Converter converter : converters) {
				if (converter.canConvert(part)) {
					part = converter.convert(part);
				}
			}
			entity.attach(part);
		}
		return entity;
	}

}
