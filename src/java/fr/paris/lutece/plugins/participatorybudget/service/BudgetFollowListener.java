package fr.paris.lutece.plugins.participatorybudget.service;


import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.extend.modules.follow.service.IFollowListener;
import fr.paris.lutece.portal.service.security.LuteceUser;


public class BudgetFollowListener implements IFollowListener{

	 /**
     * {@inheritDoc}
     */
	@Override
	public void follow(String strExtendableResourceType,
			String strIdExtendableResource, HttpServletRequest request) {
		
	}

	@Override
	public void cancelFollow(String strExtendableResourceType,
			String strIdExtendableResource, HttpServletRequest request) {
		
	}

	@Override
	public boolean canFollow( String strExtendableResourceType, String strIdExtendableResource, LuteceUser user )
	{
//		if ( strExtendableResourceType.equals( RatingAddOnService.PROPERTY_RESOURCE_TYPE ) )
//		{
//    		if ( CampagnesService.IsPhaseClosed(CampagneHome.getLastCampagne().getCode(),"SOUMISSION") )
//    		{
//    			return false;
//    		}
//		}
		return true;
	}
}
