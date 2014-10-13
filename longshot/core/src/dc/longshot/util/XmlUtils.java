package dc.longshot.util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public final class XmlUtils {
	
	/**
	 * Reads an xml file to an object.
	 * @param path path to the xml file
	 * @param boundClasses used to correctly unmarshal base types to derived types
	 * @return object of type T
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T unmarshal(final String path, final Class<?>[] boundClasses) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(boundClasses);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			File file = new File(path);
			T object = (T)unmarshaller.unmarshal(file);
			return object;
		}
		catch (JAXBException e) {
			throw new IllegalArgumentException("Could not unmarshal " + path, e);
		}
	}
	
	/**
	 * Writes an object to an xml file.
	 * @param object object to write
	 * @param path path of the xml file to write to
	 * @param boundClasses used to correctly marshal base types to derived types
	 */
	public static final <T> void marshal(final T object, final String path, final Class<?>[] boundClasses) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(boundClasses);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			File file = new File(path);
			marshaller.marshal(object, file);
		}
		catch (JAXBException e) {
			throw new IllegalArgumentException("Could not marshal " + object.toString(), e);
		}
	}
	
}
