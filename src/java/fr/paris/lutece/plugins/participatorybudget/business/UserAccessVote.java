package fr.paris.lutece.plugins.participatorybudget.business;

public class UserAccessVote 
{
	String _strId ;
	boolean _bHasAccessVote ;
	
	/**
	 * GET id user
	 * @return id user
	 */
	public String getId( )
	{
		return _strId;
	}
	/**
	 * Set Id user
	 * @param _strId the user Id
	 */
	public void setId( String _strId ) 
	{
		this._strId = _strId;
	}
	/**
	 * Get if user has access vote
	 * @return
	 */
	public boolean isHasAccessVote( ) 
	{
		return _bHasAccessVote;
	}
	/**
	 * Set user access vote
	 * @param bHasAccessVote
	 */
	public void setHasAccessVote( boolean bHasAccessVote )
	{
		_bHasAccessVote = bHasAccessVote;
	}
	
	
}
