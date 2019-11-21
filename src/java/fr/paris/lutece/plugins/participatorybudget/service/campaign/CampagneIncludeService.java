package fr.paris.lutece.plugins.participatorybudget.service.campaign;


import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * 
 * BudgetIncludeService
 *
 */
public class CampagneIncludeService {


	
	   // Template
     private static final String TEMPLATE_MES_INFOS_INCLUDE = "skin/plugins/participatorybudget/mes_infos_include.html";
     
     // Properties
   
    
	   /**
     * 
     * @param request
     * @return
     */
    public static String getMyInfos(  HttpServletRequest request )
    {
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MES_INFOS_INCLUDE );        
        return template.getHtml(  );
    } 
    
    
    
	
}
