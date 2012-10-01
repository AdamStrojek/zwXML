/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.strojek.adam.utils.xml;

import info.strojek.adam.utils.xml.formatter.zwXMLFormatter;
import info.strojek.adam.utils.xml.formatter.zwXMLTabFormatter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Stack;

/**
 * 
 * @author Adam Strojek <adam@strojek.info>
 */
public class zwXMLStreamWriter {

	private Writer output;
	private zwXMLFormatter formatter = new zwXMLTabFormatter();
	private Stack<zwXMLStreamElement> elements = new Stack<zwXMLStreamElement>();
	private boolean documentStarted = false;
	private URI lazyDefaultNamespaceUri;
	private HashMap<String, URI> lazyNamespacesUri = new HashMap<String, URI>();

	public zwXMLStreamWriter(OutputStream output) {
		this(new OutputStreamWriter(output));
	}

	public zwXMLStreamWriter(Writer output) {
		this.output = output;
	}

	private void closeOpenedElement() throws IOException {
		if (!elements.isEmpty()) {
			if (!elements.peek().isTagClosed()) {
				output.write(">");
				output.write(formatter.elementEnd());
				elements.peek().setTagClosed(true);
			}
		}
	}

	public zwXMLStreamWriter startDocument() throws IOException {
		Charset ch = Charset.defaultCharset();
		String name = ch.name().toLowerCase();

		return startDocument("1.0", name);
	}

	public zwXMLStreamWriter startDocument(String version) throws IOException {
		if (documentStarted) {
			throw new zwXMLStreamException(
					zwXMLStreamException.ErrorType.UnexpectedElementError,
					"Document already started");
		}

		output.write("<?xml version=\"");
		output.write(version);
		output.write("\"?>");

		return this;
	}

	public zwXMLStreamWriter startDocument(String version, String encoding)
			throws IOException {
		if (documentStarted) {
			throw new zwXMLStreamException(
					zwXMLStreamException.ErrorType.UnexpectedElementError,
					"Document already started");
		}

		output.write("<?xml version=\"");
		output.write(version);

		output.write("\" encoding=\"");
		output.write(encoding);
		output.write("\"?>");

		return this;
	}

	public zwXMLStreamWriter startDocument(String version, String encoding,
			boolean standalone) throws IOException {
		if (documentStarted) {
			throw new zwXMLStreamException(
					zwXMLStreamException.ErrorType.UnexpectedElementError,
					"Document already started");
		}

		output.write("<?xml version=\"");
		output.write(version);

		output.write("\" encoding=\"");
		output.write(encoding);

		output.write("\" standalone=\"");
		output.write(standalone ? "yes" : "no");
		output.write("\"?>");

		return this;
	}

	public zwXMLStreamWriter endDocument() throws IOException {
		while (!elements.isEmpty()) {
			endElement();
		}

		output.write(System.getProperty("line.separator"));

		return this;
	}

	public zwXMLStreamWriter startElement(String qualifiedName)
			throws IOException {
		documentStarted = true;
		closeOpenedElement();

		if (!elements.isEmpty()) {
			elements.peek().setHaveChildren(true);
		}

		zwXMLStreamElement el = new zwXMLStreamElement(qualifiedName);

		if (lazyDefaultNamespaceUri != null) {
			el.setDefaultNamespaceUri(lazyDefaultNamespaceUri);
		}

		output.write(formatter.elementStart(elements.size()));

		elements.push(el);

		output.write("<");
		output.write(qualifiedName);

		if (lazyDefaultNamespaceUri != null) {
			writeDefaultNamespace(lazyDefaultNamespaceUri);
			lazyDefaultNamespaceUri = null;
		}
		return this;
	}

	public zwXMLStreamWriter endElement() throws IOException {
		if (elements.isEmpty()) {
			throw new zwXMLStreamException(
					zwXMLStreamException.ErrorType.NotWellFormedError,
					"Closed to many tags");
		}

		zwXMLStreamElement el = elements.pop();

		if (!el.isHaveChildren()) {
			if (!el.isTagClosed()) {
				output.write("/>");
			}
			// el.isTagClosed() is unnessesery
		} else {
			output.write(formatter.indentation(elements.size()));
			output.write("</");
			output.write(el.getQualifiedName());
			output.write(">");

			// el.isHaveChildren() && !el.isTagClosed() is not possible
		}

		output.write(formatter.elementEnd());

		el.setTagClosed(true);

		return this;
	}

	public zwXMLStreamWriter writeAttribute(String qualifiedName, Object value)
			throws IOException {
		documentStarted = true;

		if (elements.isEmpty()) {
			throw new zwXMLStreamException(
					zwXMLStreamException.ErrorType.NotWellFormedError,
					"Attribute can't be written when have no elements");
		}

		if (elements.peek().isTagClosed() || elements.peek().isHaveChildren()) {
			throw new zwXMLStreamException(
					zwXMLStreamException.ErrorType.UnexpectedElementError,
					"Attribute " + qualifiedName
							+ " can't be insert because tag is closed");
		}

		if (qualifiedName.startsWith("xmlns:")) {
			throw new zwXMLStreamException(
					zwXMLStreamException.ErrorType.NotWellFormedError,
					"Attribute is not to set namespaces, use writeNamespace() instead");
		}

		output.write(" ");
		output.write(qualifiedName);
		output.write("=\"");
		output.write(zwXMLUtility.escape(value.toString(), true));
		output.write("\"");

		return this;
	}

	public zwXMLStreamWriter writeCDATA(String text) throws IOException {
		documentStarted = true;
		String tmp = new String(text);
		tmp.replace("]]>", "]]]]><![CDATA[>");

		closeOpenedElement();

		if (!elements.isEmpty()) {
			elements.peek().setHaveChildren(true);
		}

		output.write("<![CDATA[");
		output.write(tmp);
		output.write("]]>");

		return this;
	}

	public zwXMLStreamWriter writeComment(String text) throws IOException {
		documentStarted = true;
		if (text.contains("--")) {
			throw new zwXMLStreamException(
					zwXMLStreamException.ErrorType.NotWellFormedError,
					"Comments can't contains \"--\"");
		}
		if (text.endsWith("-")) {
			throw new zwXMLStreamException(
					zwXMLStreamException.ErrorType.NotWellFormedError,
					"Comments can't ends with \"-\"");
		}

		closeOpenedElement();

		if (!elements.isEmpty()) {
			elements.peek().setHaveChildren(true);
		}

		output.write("<!--");
		output.write(text);
		output.write("-->");

		return this;
	}

	public zwXMLStreamWriter writeDefaultNamespace(URI namespaceUri)
			throws IOException {
		documentStarted = true;

		if (elements.isEmpty() || elements.pop().isTagClosed()) {
			if (lazyDefaultNamespaceUri != null) {
				throw new zwXMLStreamException(
						zwXMLStreamException.ErrorType.UnexpectedElementError,
						"Can write only one default namespace per element");
			}
			lazyDefaultNamespaceUri = namespaceUri;
		} else {
			zwXMLStreamElement el = elements.pop();

			if (el.getDefaultNamespaceUri() == null) {
				el.setDefaultNamespaceUri(namespaceUri);
			} else {
				throw new zwXMLStreamException(
						zwXMLStreamException.ErrorType.UnexpectedElementError,
						"Can write only one default namespace per element");
			}

			output.write(" xmlns=\"");
			output.write(namespaceUri.toString());
			output.write("\"");
		}

		return this;
	}

	public zwXMLStreamWriter writeNamespace(URI namespaceUri, String prefix) {

		return this;
	}

	public zwXMLFormatter getFormatter() {
		return formatter;
	}

	public void setFormatter(zwXMLFormatter formatter) {
		if (documentStarted) {
			throw new zwXMLStreamException(
					zwXMLStreamException.ErrorType.UnexpectedElementError,
					"Can change formatter after document started!");
		}
		this.formatter = formatter;
	}

	private class zwXMLStreamElement {

		private String qualifiedName;
		private URI defaultNamespaceUri;
		private boolean haveChildren = false;
		private boolean tagClosed = false;
		private HashMap<String, URI> namespacesUri = new HashMap<String, URI>();

		public zwXMLStreamElement(String qualifiedName) {
			this.qualifiedName = qualifiedName;
		}

		public boolean isHaveChildren() {
			return haveChildren;
		}

		public void setHaveChildren(boolean haveChildren) {
			this.haveChildren = haveChildren;
		}

		public URI getDefaultNamespaceUri() {
			return defaultNamespaceUri;
		}

		public void setDefaultNamespaceUri(URI defaultNamespaceUri) {
			this.defaultNamespaceUri = defaultNamespaceUri;
		}

		public String getQualifiedName() {
			return qualifiedName;
		}

		public void setQualifiedName(String qualifiedName) {
			this.qualifiedName = qualifiedName;
		}

		public boolean isTagClosed() {
			return tagClosed;
		}

		public void setTagClosed(boolean tagClosed) {
			this.tagClosed = tagClosed;
		}
	}

	private class zwXMLStreamNamespace {

		private URI namespaceUri;
		private String prefix;

		public zwXMLStreamNamespace(URI namespaceUri, String prefix) {
			this.namespaceUri = namespaceUri;
			this.prefix = prefix;
		}

		public URI getNamespaceUri() {
			return namespaceUri;
		}

		public void setNamespaceUri(URI namespaceUri) {
			this.namespaceUri = namespaceUri;
		}

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
	}
}
