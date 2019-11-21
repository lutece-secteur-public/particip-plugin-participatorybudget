package fr.paris.lutece.plugins.participatorybudget.service;


import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.extend.modules.favorite.service.IFavoriteListener;
import fr.paris.lutece.portal.service.security.LuteceUser;


public class BudgetFavoriteListener implements IFavoriteListener{

	 /**
     * {@inheritDoc}
     */
	@Override
	public void favorite(String strExtendableResourceType,
			String strIdExtendableResource, HttpServletRequest request) {
		
	}

	@Override
	public void cancelFavorite(String strExtendableResourceType,
			String strIdExtendableResource, HttpServletRequest request) {
		
	}

	@Override
	public boolean canFavorite( String strExtendableResourceType, String strIdExtendableResource, LuteceUser user )
	{
//		if ( strExtendableResourceType.equals("document") )
//		{
//    		if ( CampagnesService.IsPhaseClosed(CampagneHome.getLastCampagne().getCode(),"SOUMISSION") )
//    		{
//    			return false;
//    		}
//		}
		return true;
	}
}
