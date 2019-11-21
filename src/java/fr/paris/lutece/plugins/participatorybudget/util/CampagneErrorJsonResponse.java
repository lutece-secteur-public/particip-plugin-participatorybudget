package fr.paris.lutece.plugins.participatorybudget.util;

import fr.paris.lutece.util.json.ErrorJsonResponse;

/**
 * 
 * ParisConnectErrorJsonResponse
 *
 */
public class CampagneErrorJsonResponse extends ErrorJsonResponse {


	private static final long serialVersionUID = 5100059838966010325L;
	private String _strToken;
	
	
	public CampagneErrorJsonResponse(String strErrorCode) {
		super(strErrorCode);
	}
	
	
	public CampagneErrorJsonResponse(String strErrorCode, String strToken) {
		super(strErrorCode);
		_strToken=strToken;
}
	
	/**
	 * 
	 * @return get token in json response
	 */
	public String getToken() {
		return _strToken;
	}
	/**
	 * set new token in json response
	 * @param _strToken set token in json response
	 */
	public void setToken(String _strToken) {
		this._strToken = _strToken;
	}

	

}
