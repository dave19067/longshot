package dc.longshot.util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlUtils {
	
	/**
	 * Reads an xml file to an object.
	 * @param path path to the xml file
	 * @param boundClasses used to correctly unmarshal base types to derived types
	 * @return object of type T
	 */
	@SuppressWarnings("unchecked")
	public static <T> T read(String path, Class<?>[] boundClasses) {
		T object = null;
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(boundClasses);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			File file = new File(path);
			object = (T)unmarshaller.unmarshal(file);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return object;
	}
	
	/**
	 * Writes an object to an xml file.
	 * @param object object to write
	 * @param path path of the xml file to write to
	 * @param boundClasses used to correctly marshal base types to derived types
	 */
	public static <T> void write(T object, String path, Class<?>[] boundClasses) {
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(boundClasses);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			File file = new File(path);
			marshaller.marshal(object, file);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
