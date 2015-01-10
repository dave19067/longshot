package dc.longshot.epf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import dc.longshot.eventmanagement.EventDelegate;

/**
 * Manages a group of entities.  Provides accessing, removing, and adding entities.
 * @author David Chen
 *
 */
public final class EntityManager {
	
	private final EventDelegate<EntityAddedListener> entityAddedDelegate = new EventDelegate<EntityAddedListener>();
	private final EventDelegate<EntityRemovedListener> entityRemovedDelegate
	= new EventDelegate<EntityRemovedListener>();
	
	private final List<Entity> entities = new ArrayList<Entity>();
	private final List<Entity> entitiesToAdd = new ArrayList<Entity>();
	private final List<Entity> entitiesToRemove = new ArrayList<Entity>();
	
	public final void addEntityAddedListener(final EntityAddedListener listener) {
		entityAddedDelegate.listen(listener);
	}
	
	public final void addEntityRemovedListener(final EntityRemovedListener listener) {
		entityRemovedDelegate.listen(listener);
	}
	
	/**
	 * @return all managed and cached entities
	 */
	public final List<Entity> getAll() {
		List<Entity> entities = new ArrayList<Entity>(this.entities);
		entities.addAll(entitiesToAdd);
		return entities;
	}
	
	/**
	 * @return all managed entities
	 */
	public final List<Entity> getManaged() {
		return new ArrayList<Entity>(entities);
	}
	
	/**
	 * Adds and manages an entity on the next call to update.
	 * @param entity the entity to add and manage
	 */
	public final void add(final Entity entity) {
		entitiesToAdd.add(entity);
	}
	
	/**
	 * Adds the entities in the passed in collection on the next call to update.
	 * @param entities entities to add and manage
	 */
	public final void addAll(final Collection<Entity> entities) {
		for (Entity entity : entities) {
			add(entity);
		}
	}
	
	/**
	 * Removes an entity on the next call to update.
	 * @param entity entity to remove
	 */
	public final void remove(final Entity entity) {
		entitiesToRemove.add(entity);
	}
	
	public final void cleanup() {
		Iterator<Entity> it = entities.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			entity.cleanup();
			it.remove();
		}
	}

	/**
	 * Adds and removes entities passed into the add or remove methods.
	 */
	public final void update() {
		while (!entitiesToAdd.isEmpty()) {
			Entity entityToAdd = entitiesToAdd.remove(0);
			entityToAdd.initialize();
			entities.add(entityToAdd);
			entityAddedDelegate.notify(new EntityAddedEvent(entityToAdd));
		}
		
		while (!entitiesToRemove.isEmpty()) {
			Entity entityToRemove = entitiesToRemove.remove(0);
			if (entities.remove(entityToRemove)) {
				entityToRemove.cleanup();
				entityRemovedDelegate.notify(new EntityRemovedEvent(entityToRemove));
			}
		}
	}
	
}
