/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package info.strojek.adam.utils.xml;

import java.text.StringCharacterIterator;

/**
 *
 * @author Adam Strojek <adam@strojek.info>
 */
public class zwXMLUtility {
	/**
	 *
	 * @see #escape(java.lang.String, boolean)
	 * @param text text that should be escaped
	 * @return escaped text
	 */
	static public String escape(String text) {
		return escape(text, false);
	}

	/**
	 *
	 * @param text text that should be escaped
	 * @param escapeWhitespaces should whitespaces be escaped
	 * @return escaped text
	 */
	static public String escape(String text, boolean escapeWhitespaces) {
		StringBuilder tmp = new StringBuilder();

		StringCharacterIterator it = new StringCharacterIterator(text);

		for (char c = it.first(); c != StringCharacterIterator.DONE; c = it.next()) {
			switch (c) {
				case '<':
					tmp.append("&lt;");
					break;
				case '>':
					tmp.append("&gt;");
					break;
				case '&':
					tmp.append("&amp;");
					break;
				case '\"':
					tmp.append("&quot;");
					break;
				default: {
					if (escapeWhitespaces && Character.isWhitespace(c)) {
						switch (c) {
							case '\n':
								tmp.append("&#10;");
								break;
							case '\r':
								tmp.append("&#13;");
								break;
							case '\t':
								tmp.append("&#9;");
								break;
							default:
								tmp.append(c);
								break;
						}
					} else {
						tmp.append(c);
					}
				}
				break;
			}
		}

		return tmp.toString();
	}
}
