package fr.paris.lutece.plugins.campagnebp.service;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.service.security.LuteceUser;

public interface IMyInfosListener {
	

		void updateNickName(String strLuteceUserName,String strNickName);
		
		void createNickName(String strLuteceUserName,String strNickName);
		
		int canChangeArrond( LuteceUser user);

		String deleteVotes(  HttpServletRequest request );


}
