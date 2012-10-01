/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.strojek.adam.utils.xml.formatter;

/**
 *
 * @author Adam Strojek <adam@strojek.info>
 */
public class zwXMLTabFormatter implements zwXMLFormatter {

	public String elementStart(int level) {
		return indentation(level);
	}

	public String elementEnd() {
		return System.getProperty("line.separator");
	}

	public String indentation(int level) {
		StringBuilder buf = new StringBuilder();

		while ((level--) > 0) {
			buf.append("\t");
		}

		return buf.toString();
	}
}
