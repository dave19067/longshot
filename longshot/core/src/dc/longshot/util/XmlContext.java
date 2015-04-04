package dc.longshot.util;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.badlogic.gdx.files.FileHandle;

public final class XmlContext {
	
	private final JAXBContext jaxbContext;
	
	public XmlContext(final Class<?>[] boundClasses) {
		try {
			jaxbContext = JAXBContext.newInstance(boundClasses);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Could not construct context", e);
		}
	}
	
	public final <T> T unmarshal(final FileHandle fileHandle) {
		return unmarshal(fileHandle.read());
	}
	
	public <TAdapted, T> T unmarshal(final InputStream inputStream, final XmlAdapter<TAdapted, T> xmlAdapter) {
		TAdapted adaptedObject = unmarshal(inputStream);
		try {
			return xmlAdapter.unmarshal(adaptedObject);
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Could not unmarshal stream", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public final <T> T unmarshal(final InputStream inputStream) {
		try {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return (T)unmarshaller.unmarshal(inputStream);
		}
		catch (JAXBException e) {
			throw new IllegalArgumentException("Could not unmarshal stream", e);
		}
	}
	
	public final <T> void marshal(final T object, final FileHandle fileHandle) {
		OutputStream outputStream = fileHandle.write(false);
		marshal(object, outputStream);
	}
	
	public final <T> void marshal(final T object, final OutputStream outputStream) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(object, outputStream);
		}
		catch (JAXBException e) {
			throw new IllegalArgumentException("Could not marshal " + object.toString(), e);
		}
	}
	
}
