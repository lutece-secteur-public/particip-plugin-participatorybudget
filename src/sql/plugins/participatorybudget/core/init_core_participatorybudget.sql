INSERT INTO core_datastore VALUES ('participatorybudget.site_property.extend.template.userCanVote.textblock'   , '<div id="divVotedocument@id@" class="vote">\r\n    <a href="jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=@id@&extendableResourceType=document&voteValue=1"\r\n        class="budget-btn-do-vote" onclick="checkIfUserIsValid(\'@id@\',\'document\',\'1\');">\r\n        <div class="bg-jevote">\r\n              <i class="glyphicon glyphicon-ok-circle"></i> Je Vote\r\n         </div>\r\n      </a>\r\n</div>');
INSERT INTO core_datastore VALUES ('participatorybudget.site_property.extend.template.userCannotVote.textblock', '<div id="divVotedocument@id@" class="vote">\r\n     <div class="bg-jannule">\r\n             <span class="budget-label-jai-vote"><i class="glyphicon glyphicon-ok-circle"></i> J\'ai voté</span>\r\n             <a  class="budget-cancel-vote-button" href="jsp/site/plugins/extend/modules/rating/DoCancelVote.jsp?idExtendableResource=@id@&extendableResourceType=document"  \r\n                       onclick="doCancelVote(\'@id@\',\'document\');" >\r\n         <span class="budget-label-remove-vote"><i class=\'glyphicon glyphicon-remove-circle\'></i> J\'annule mon vote</span>\r\n         </div>\r\n       </a>\r\n</div>\r\n');
INSERT INTO core_datastore VALUES ('participatorybudget.site_property.extend.template.voteClosed.textblock'    , '');
INSERT INTO core_datastore VALUES ('participatorybudget.site_property.send_account_validation_back_url'        , 'https://budgetparticipatif.paris.fr/bp/je-vote.html');
INSERT INTO core_datastore VALUES ('participatorybudget.site_property.enable_validation_account'               , 'false');
INSERT INTO core_datastore VALUES ('participatorybudget.site_property.error_code_user_already_voted'           , '<div class=\'btn-in-popup\'><span id=\'modal_button_valide\'><a  class=\'btn-block btn-modal-valid glyphicon glyphicon-ok-circle\' href=\'jsp/site/Portal.jsp?page=mesInfos&view=myVotes\'><span> Voir mes votes</span></a></span></div>');
INSERT INTO core_datastore VALUES ('participatorybudget.site_property.error_code_user_can_not_vote'            , '');
INSERT INTO core_datastore VALUES ('participatorybudget.site_property.error_code_user__voted_max'              , '');
INSERT INTO core_datastore VALUES ('participatorybudget.site_property.error_checked_arrondissement'            , '');
INSERT INTO core_datastore VALUES ('participatorybudget.site_property.error_code_user_not_signed'              , '');

INSERT INTO core_datastore VALUES ('solr.app.conf.list_projects.addonBeans.0', 'participatorybudget.ProjectsRealisationSolrListAddon');
INSERT INTO core_datastore VALUES ('solr.app.conf.list_projects.fq', '(status_text:SUIVI" OR status_text:"suivi") AND type:"PB Project');
INSERT INTO core_datastore VALUES ('solr.app.conf.list_projects.mapping', 'false');
INSERT INTO core_datastore VALUES ('solr.app.conf.list_projects.template', 'skin/plugins/participatorybudget/document_list_solr_search_results.html');
INSERT INTO core_datastore VALUES ('solr.app.conf.map_projects.fq', '(status_text:SUIVI" OR status_text:"suivi") AND type:"PB Project');
INSERT INTO core_datastore VALUES ('solr.app.conf.map_projects.mapping', 'true');
INSERT INTO core_datastore VALUES ('solr.app.conf.map_projects.template', 'skin/plugins/participatorybudget/document_map_solr_search_results.html');
INSERT INTO core_datastore VALUES ('solr.app.conf.map_projets.addonBeans.0', 'participatorybudget.BudgetSolrAddon');
INSERT INTO core_datastore VALUES ('solr.app.conf.map_projets.fq', '(status_text:SOUMIS" OR status_text:"GAGNANT" OR status_text:"PERDANT") AND type:"PB Project');
INSERT INTO core_datastore VALUES ('solr.app.conf.map_projets.mapping', 'true');
INSERT INTO core_datastore VALUES ('solr.app.conf.map_projets.template', 'skin/plugins/participatorybudget/projet_mdp_map_solr_search_results.html');
INSERT INTO core_datastore VALUES ('solr.app.conf.projects_mdp_detail.addonBeans.0', 'participatorybudget.VotesSolrAddon');
INSERT INTO core_datastore VALUES ('solr.app.conf.projects_mdp_detail.fq', '(status_text:SOUMIS" OR status_text:"GAGNANT" OR status_text:"PERDANT") AND type:"PB Project');
INSERT INTO core_datastore VALUES ('solr.app.conf.projects_mdp_detail.mapping', 'false');
INSERT INTO core_datastore VALUES ('solr.app.conf.projects_mdp_detail.template', 'skin/plugins/participatorybudget/projet_mdp_details_solr_search_results.html');
INSERT INTO core_datastore VALUES ('solr.app.conf.projects_mdp.addonBeans.0', 'participatorybudget.BudgetSolrListAddon');
INSERT INTO core_datastore VALUES ('solr.app.conf.projects_mdp.fq', '(status_text:SOUMIS" OR status_text:"GAGNANT" OR status_text:"PERDANT") AND type:"PB Project');
INSERT INTO core_datastore VALUES ('solr.app.conf.projects_mdp.mapping', 'false');
INSERT INTO core_datastore VALUES ('solr.app.conf.projects_mdp.template', 'skin/plugins/participatorybudget/projet_mdp_list_solr_search_results.html');

--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'CAMPAIGN_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('CAMPAIGN_MANAGEMENT','participatorybudget.adminFeaure.ManageCampaign.name',1,'jsp/admin/plugins/participatorybudget/campaign/ManageCampaign.jsp','participatorybudget.adminFeature.ManageCampaign.description',0,'participatorybudget',NULL,NULL,NULL,4);

--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'CAMPAIGN_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('CAMPAIGN_MANAGEMENT',1);