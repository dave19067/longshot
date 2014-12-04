package box2dlights.cloners;

import java.lang.reflect.Field;
import java.util.Map;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.rits.cloning.Cloner;
import com.rits.cloning.IFastCloner;

public final class PointLightCloner implements IFastCloner {

	@Override
	public Object clone(Object object, Cloner cloner, Map<Object, Object> map) throws IllegalAccessException {
		PointLight pointLight = (PointLight)object;
		try {
			// HACK: Getting hidden fields using reflection
			RayHandler rayHandler = getFieldValue(pointLight, "rayHandler");
			int rays = getFieldValue(pointLight, "rayNum");
			return new PointLight(rayHandler, rays, pointLight.getColor(), pointLight.getDistance(), pointLight.getX(), 
					pointLight.getY());
		} 
		catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getFieldValue(Object object, String fieldName) throws NoSuchFieldException, SecurityException, 
		IllegalArgumentException, IllegalAccessException {
		Field field = getField(object.getClass(), fieldName);
		field.setAccessible(true);
		return (T)field.get(object);
	}
	
	private Field getField(Class<?> c, String fieldName) throws NoSuchFieldException {
		try {
			return c.getDeclaredField(fieldName);
		}
		catch (NoSuchFieldException e) {
			Class<?> superClass = c.getSuperclass();
			if (superClass == null) {
				throw e;
			}
			else {
				return getField(superClass, fieldName);
			}
		}
	}

}
