package fr.paris.lutece.plugins.budgetparticipatif.business.rgpd;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

public class RGPDTreatmentLogDAO implements IRGPDTreatmentLogDAO
{
	// Constants
    private static final String SQL_QUERY_INSERT = "INSERT INTO budgetparticipatif_rgpd_treatment_log ( id_admin_user, admin_user_access_code, admin_user_email, treatment_type, treatment_object_name, treatment_object_fields ) VALUES ( ?, ?, ?, ?, ?, ? ) ";
    
	@Override
	public void insert( AdminUser user, String strTreatmentTypeStr, String strTreatmentObjectName, String strTreatmentObjectFields, Plugin plugin ) 
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
		
		daoUtil.setInt    ( 1, user.getUserId()         );
		daoUtil.setString ( 2, user.getAccessCode()     );
		daoUtil.setString ( 3, user.getEmail()          );
		daoUtil.setString ( 4, strTreatmentTypeStr      );
		daoUtil.setString ( 5, strTreatmentObjectName   );
		daoUtil.setString ( 6, strTreatmentObjectFields );
	      	        
	    daoUtil.executeUpdate(  );
	    daoUtil.free(  );
		
	}

}
