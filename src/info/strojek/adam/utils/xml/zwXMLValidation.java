/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.strojek.adam.utils.xml;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Adam Strojek <adam@strojek.info>
 */
public final class zwXMLValidation
{
	protected static Collection<Integer> range( int from, int to )
	{
		Collection<Integer> c = new ArrayList<Integer>();

		for( int i = from; i <= to; ++i )
		{
			c.add( i );
		}

		return c;
	}

	protected static final Collection<Integer> space = new ArrayList<Integer>()
	{{
		add( 0x20 );
		add( 0x09 ); // '\t'
		add( 0x0d ); // '\r'
		add( 0x0a ); // '\n'
	}};

	/**
	 * Declaraion of NameStartChar according to XML Spec 1.0
	 * <code>NameStartChar ::= ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6]
	 *	| [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D]
	 *	| [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF]
	 *	| [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]</code>
	 * http://www.w3.org/TR/REC-xml/#NT-NameStartChar
	 */
	protected static final Collection<Integer> nameStartChar = new ArrayList<Integer>()
	{{
		add( 0x3a ); // ':'
		addAll( range( 0x41, 0x5a ) ); // 'A'..'Z'
		add( 0x5f ); // '_'
		addAll( range( 0x61, 0x7a ) ); // 'a'..'z'
		addAll( range( 0xc0, 0xd6 ) );
		addAll( range( 0xd8, 0xf6 ) );
		addAll( range( 0xf8, 0x2ff ) );
		addAll( range( 0x0370, 0x037d ) );
		addAll( range( 0x037f, 0x1fff ) );
		addAll( range( 0x200c, 0x200d ) );
		addAll( range( 0x2070, 0x218f ) );
		addAll( range( 0x2c00, 0x2fef ) );
		addAll( range( 0x3001, 0xd7ff ) );
		addAll( range( 0xf900, 0xfdcf ) );
		addAll( range( 0xfdf0, 0xfffd ) );
		addAll( range( 0x010000, 0x0effff ) );
	}};

	/**
	 * Declaration of NameChar according to XML Spec 1.0
	 * <code>NameChar ::= NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]</code>
	 * http://www.w3.org/TR/REC-xml/#NT-NameChar
	 */
	protected static final Collection<Integer> nameChar = new ArrayList<Integer>()
	{{
		 addAll( nameStartChar );
		 add( 0x2d );
		 add( 0x2e );
		 addAll( range( 0x30, 0x39 ) ); // '0'..'9'
		 add( 0xb7 );
		 addAll( range( 0x0300, 0x036f ) );
		 addAll( range( 0x203f, 0x2040 ) );
	}};
}
