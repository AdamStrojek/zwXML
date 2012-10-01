package info.strojek.adam.utils.xml;

import static org.junit.Assert.*;

import info.strojek.adam.utils.xml.formatter.zwXMLEmptyFormatter;
import info.strojek.adam.utils.xml.formatter.zwXMLFormatter;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class zwXMLStreamWriterTest {

	@Test
	public void testWriteAttribute() throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		zwXMLFormatter formatter = new zwXMLEmptyFormatter();
		
		zwXMLStreamWriter xml = new zwXMLStreamWriter(output);
		
		xml.setFormatter(formatter);
		
		xml.startDocument("1.0");
		
		xml.startElement("myName");
		xml.writeAttribute("foo", "bar");
		xml.writeAttribute("number", 0xc0fe);
		xml.endElement();
		
		xml.endDocument();
		
		String expected = "";
		String actual = output.toString();
		
		assertEquals(expected, actual);
		
		System.out.println(actual);
	}

	@Test
	public void testWriteComment() throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		zwXMLFormatter formatter = new zwXMLEmptyFormatter();
		
		zwXMLStreamWriter xml = new zwXMLStreamWriter(output);
		
		xml.setFormatter(formatter);
		
		xml.startDocument("1.0");
		
		xml.startElement("myName");
		xml.writeAttribute("ble", 123);
		
		xml.writeComment("Example comment <>");
		
		xml.endElement();
		
		xml.endDocument();
		
		String expected = "";
		String actual = output.toString();
		
		assertEquals(expected, actual);
		
		System.out.println(actual);
	}

	@Test
	public void testWriteElementStart() throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		zwXMLFormatter formatter = new zwXMLEmptyFormatter();
		
		zwXMLStreamWriter xml = new zwXMLStreamWriter(output);
		
		xml.setFormatter(formatter);
		
		xml.startDocument("1.0");
		
		xml.startElement("myName").endElement();
		
		xml.endDocument();
		
		String expected = "";
		String actual = output.toString();
		
		assertEquals(expected, actual);
		
		System.out.println(actual);
	}

}
