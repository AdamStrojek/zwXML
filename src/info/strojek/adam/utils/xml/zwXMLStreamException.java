/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package info.strojek.adam.utils.xml;

/**
 *
 * @author Adam Strojek <adam@strojek.info>
 */
public class zwXMLStreamException extends RuntimeException
{

	/**
	 * Generated
	 */
	private static final long serialVersionUID = -2642628790586678785L;

	public enum ErrorType
	{
		NoError("No error"),
		CustomError("Custom error"),
		NotWellFormedError("Not well formed error"),
		PrematureEndOfDocumentError("Premature end of document error"),
		UnexpectedElementError("Unexpected element");

		protected String message;

		private ErrorType( String message )
		{
			this.message=message;
		}

		public String getMessage()
		{
			return message;
		}
	}
	
	protected ErrorType type;

	public zwXMLStreamException( ErrorType type, String message )
	{
		super( message );

		this.type = type;
	}

	public zwXMLStreamException( ErrorType type, String message, Throwable cause )
	{
		super( message, cause );
		this.type = type;
	}

	public ErrorType getType()
	{
		return type;
	}

	@Override
	public String getMessage()
	{
		return type.getMessage() + ": " + super.getMessage();
	}


}
