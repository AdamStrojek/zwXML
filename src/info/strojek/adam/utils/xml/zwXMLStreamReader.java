/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.strojek.adam.utils.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author Adam Strojek <adam@strojek.info>
 */
public class zwXMLStreamReader
{

	public enum TokenType
	{

		NoToken,
		Error,
		DocumentStart,
		DocumentEnd,
		ElementStart,
		ElementEnd,
		Characters,
		Comment,
		DTD,
		EntityReference,
		ProcessingInstruction
	}
	protected enum State
	{
		Tag,
		Attribute
	}
	protected State state;

	// Input
	protected InputStream input;
	protected String charsetName;
	protected boolean ended = false;
	protected long lineNumber = 0;
	protected long columnNumber = 0;
	// Token
	protected TokenType token = TokenType.NoToken;
	// Tag
	protected String name;
	protected String prefix;
	// Attributes

	// Namespaces
	protected Stack<URI> defaultNamespaceURI = new Stack<URI>();
	protected Stack<HashMap<String, URI>> namespacesURIStack = new Stack<HashMap<String, URI>>();

	//Validation

	public zwXMLStreamReader( InputStream input )
	{
		this( input, "utf-8" );
	}

	public zwXMLStreamReader( InputStream input, String charsetName )
	{
		this.input = input;
		this.charsetName = charsetName;
	}

	public zwXMLStreamReader( String input )
	{
		this(input.getBytes());
	}

	public zwXMLStreamReader( byte[] input )
	{
		this.input = new ByteArrayInputStream( input );
	}

	public TokenType readNext()
	{
		// If previous token was TokenType.ElementEnd this mean that we
		// must pop stacks.
		if( token == TokenType.ElementEnd )
		{
			defaultNamespaceURI.pop();
			namespacesURIStack.pop();
		}

		// Must read content to know what is next token
		try
		{
			int ch;

			while( (ch = input.read()) != -1 )
			{
				if(ch == '<')
				{
					state = State.Tag;
					name = "";
				}

				switch(state) {
					case Tag:
						name += (char)ch;

						break;
					case Attribute:
						break;
				}
			}
		}
		catch( IOException ex )
		{
			throw new zwXMLStreamException( zwXMLStreamException.ErrorType.PrematureEndOfDocumentError, ex.getMessage(), ex );
		}

		return TokenType.NoToken;
	}

	public final String getName()
	{
		return name;
	}

	public URI getNamespaceURI()
	{
		if( prefix.isEmpty() )
		{
			if( !defaultNamespaceURI.empty() )
			{
				return defaultNamespaceURI.peek();
			}
		}
		else
		{
			for( HashMap<String, URI> namespacesURI : namespacesURIStack )
			{
				if( namespacesURI.containsKey( prefix ) )
				{
					return namespacesURI.get( prefix );
				}
			}

		}

		try
		{
			return new URI( "" );
		}
		finally
		{
			return null;
		}
	}

	public String getPrefix()
	{
		return prefix;
	}

	public String getQualifiedName()
	{
		return prefix + ":" + name;
	}
}
