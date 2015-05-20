package dc.longshot.xmladapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ClassAdapter extends XmlAdapter<String, Class<?>> {

	@Override
	public final String marshal(final Class<?> classToMarshal) {
		return classToMarshal.getName();
	}

	@Override
	public final Class<?> unmarshal(final String className) throws ClassNotFoundException {
		return Class.forName(className);
	}
	
}
