package fr.paris.lutece.plugins.participatorybudget.service.avatar;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import fr.paris.lutece.plugins.avatarserver.business.Avatar;
import fr.paris.lutece.plugins.avatarserver.business.AvatarHome;
import fr.paris.lutece.plugins.avatarserver.service.AvatarService;
import fr.paris.lutece.plugins.avatarserver.service.HashService;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampagneUploadHandler;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class CampagneAvatarService {

	private static final String PARAMETER_AVATAR_IMAGE = "avatar_image";

	public static void createAvatar(HttpServletRequest request,String strLuteceUserName)
	{
			
		
		CampagneUploadHandler handler=SpringContextService.getBean( CampagneUploadHandler.BEAN_NAME );
		List<FileItem> listAvatar=	handler.getListUploadedFiles( PARAMETER_AVATAR_IMAGE, request.getSession(  ) );
			//save Avatar
		if(listAvatar   != null && listAvatar.size() > 0 &&  listAvatar.get(0).getSize()>0)
	        {
	        	
				Avatar avatar=new Avatar();
	        	avatar.setValue( listAvatar.get(0).get(  ) );
	        	avatar.setMimeType( listAvatar.get(0).getContentType(  ) );
	        	avatar.setEmail(strLuteceUserName);
	        	
	        	AvatarService.create(avatar);
	        }	
		handler.removeSessionFiles(request.getSession(  ).getId());
	}
	
	public static void updateAvatar(HttpServletRequest request,String strLuteceUserName)
	{
		
		CampagneUploadHandler handler=SpringContextService.getBean( CampagneUploadHandler.BEAN_NAME );
		List<FileItem> listAvatar=	handler.getListUploadedFiles( PARAMETER_AVATAR_IMAGE, request.getSession(  ) );
			//save Avatar
		if(listAvatar   != null && listAvatar.size() > 0 &&  listAvatar.get(0).getSize()>0)
	        {
	      
        	
        		String strHash = HashService.getHash( strLuteceUserName );
        		Avatar avatar = AvatarHome.findByHash( strHash );
        		if(avatar==null)
        		{
           		 avatar=new Avatar();
           		 avatar.setEmail(strLuteceUserName);
           		 avatar.setValue( listAvatar.get(0).get(  ) );
           		 avatar.setMimeType( listAvatar.get(0).getContentType(  ) );
           		
           		 AvatarService.create(avatar);
        		}
        		else
        		{
    			 avatar.setValue( listAvatar.get(0).get(  ) );
           		 avatar.setMimeType( listAvatar.get(0).getContentType(  ) );
           		 AvatarService.update(avatar);
        		}
        	    }
			handler.removeSessionFiles(request.getSession(  ).getId());
	    
		
		}

}
