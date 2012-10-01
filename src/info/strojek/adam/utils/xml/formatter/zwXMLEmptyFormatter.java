/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package info.strojek.adam.utils.xml.formatter;

/**
 *
 * @author Adam Strojek <adam@strojek.info>
 */
public class zwXMLEmptyFormatter implements zwXMLFormatter {
	
	public zwXMLEmptyFormatter() {
		
	}

	public String elementStart(int level) {
		return "";
	}

	public String elementEnd() {
		return "";
	}

	public String indentation(int level) {
		return "";
	}

}
