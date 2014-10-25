package dc.longshot.util;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public final class XmlUtils {
	
	private XmlUtils() {
	}
	
	/**
	 * Reads an xml file to an object.
	 * @param path path to the xml file
	 * @param boundClasses used to correctly unmarshal base types to derived types
	 * @return object of type T
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T unmarshal(final InputStream inputStream, final Class<?>[] boundClasses) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(boundClasses);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			T object = (T)unmarshaller.unmarshal(inputStream);
			return object;
		}
		catch (JAXBException e) {
			throw new IllegalArgumentException("Could not unmarshal " + inputStream.toString(), e);
		}
	}
	
	/**
	 * Writes an object to an xml file.
	 * @param object object to write
	 * @param path path of the xml file to write to
	 * @param boundClasses used to correctly marshal base types to derived types
	 */
	public static final <T> void marshal(final T object, final OutputStream outputStream, 
			final Class<?>[] boundClasses) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(boundClasses);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(object, outputStream);
		}
		catch (JAXBException e) {
			throw new IllegalArgumentException("Could not marshal " + object.toString(), e);
		}
	}
	
}
