/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.strojek.adam.utils.xml.formatter;

/**
 *
 * @author Adam Strojek <adam@strojek.info>
 */
public interface zwXMLFormatter
{

	public String elementStart( int level );

	public String elementEnd();

	public String indentation( int level );
}
