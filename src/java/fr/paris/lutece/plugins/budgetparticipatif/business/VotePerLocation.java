package fr.paris.lutece.plugins.budgetparticipatif.business;

public class VotePerLocation 
{
	private int id;
	   
	private String _strLocationArd;
	private int _strNbVotes;
	
	/**
	 * 
	 * @return
	 */
	public int getId( ) 
	{
		return id;
	}
	/**
	 * 
	 * @param id
	 */
	public void setId( int id ) 
	{
		this.id = id;
	}
	/**
	 * 
	 * @return
	 */
	public String getLocationArd( ) 
	{
		return _strLocationArd;
	}
	/**
	 * 
	 * @param strLocationArd
	 */
	public void setLocationArd( String strLocationArd ) 
	{
		this._strLocationArd = strLocationArd;
	}
	/**
	 * 
	 * @return
	 */
	public int getNbVotes( ) 
	{
		return _strNbVotes;
	}
	/**
	 * 
	 * @param strNbVotes
	 */
	public void setNbVotes( int strNbVotes ) 
	{
		this._strNbVotes = strNbVotes;
	}
	
	
}
