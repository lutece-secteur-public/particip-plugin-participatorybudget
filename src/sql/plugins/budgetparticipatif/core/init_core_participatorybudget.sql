INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.extend.template.userCanVote.textblock', '<div id="divVotedocument@id@" class="vote">\r\n    <a href="jsp/site/plugins/extend/modules/rating/DoVote.jsp?idExtendableResource=@id@&extendableResourceType=document&voteValue=1"\r\n        class="budget-btn-do-vote" onclick="checkIfUserIsValid(\'@id@\',\'document\',\'1\');">\r\n        <div class="bg-jevote">\r\n              <i class="glyphicon glyphicon-ok-circle"></i> Je Vote\r\n         </div>\r\n      </a>\r\n</div>');
INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.extend.template.userCannotVote.textblock', '<div id="divVotedocument@id@" class="vote">\r\n     <div class="bg-jannule">\r\n             <span class="budget-label-jai-vote"><i class="glyphicon glyphicon-ok-circle"></i> J\'ai voté</span>\r\n             <a  class="budget-cancel-vote-button" href="jsp/site/plugins/extend/modules/rating/DoCancelVote.jsp?idExtendableResource=@id@&extendableResourceType=document"  \r\n                       onclick="doCancelVote(\'@id@\',\'document\');" >\r\n         <span class="budget-label-remove-vote"><i class=\'glyphicon glyphicon-remove-circle\'></i> J\'annule mon vote</span>\r\n         </div>\r\n       </a>\r\n</div>\r\n');
INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.extend.template.voteClosed.textblock', '');
INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.error_code_user_already_voted', '');

INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.send_account_validation_back_url', "https://budgetparticipatif.paris.fr/bp/je-vote.html");
INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.enable_validation_account', "false");

INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.error_code_user_already_voted', "<div class='btn-in-popup'><span id='modal_button_valide'><a  class='btn-block btn-modal-valid glyphicon glyphicon-ok-circle' href='jsp/site/Portal.jsp?page=mesInfos&view=myVotes'><span> Voir mes votes</span></a></span></div>");

INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.error_code_user_can_not_vote',"");
INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.error_code_user__voted_max',"");
INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.error_vote_user_arrond',"<div class='btn-in-popup'><span id='modal_title'> Vous avez voté pour des projets qui ne sont pas dans votre arrondissement de lieu de vie ou de travail enregistré dans votre profil actuel. Souhaitez-vous maintenant annuler vos votes précédant et voter sur les projets de votre nouvel arrondissement? </span></div>");
INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.error_already_voted_tout_paris',"<div class='btn-in-popup'><span id='modal_title'> Vous avez voté sur 10 projets concernant tout Paris. Souhaitez-vous maintenant voter pour les projets par arrondissement ou valider vos votes. </span></div>");
INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.error_already_voted_arrondissement',"<div class='btn-in-popup'><span id='modal_title' >Vous avez voté sur 10 projets concernant votre arrondissement. Souhaitez-vous maintenant voter pour les projets de tout Paris ou valider vos votes.  </span></div>");
INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.error_checked_arrondissement',"");
INSERT INTO core_datastore VALUES ('budgetparticipatif.site_property.error_code_user_not_signed',"");

INSERT INTO core_datastore VALUES ('budgetparticipatif.notifyUserDaemon.nbNotificationInPool', "10");
INSERT INTO core_datastore VALUES ('budgetparticipatif.notifyUserDaemon.timeToWaitBeforeSendPoolNotification', "1000");



DELETE FROM core_admin_right WHERE id_right = 'VOTE_PAR_ARRANDISSEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('VOTE_PAR_ARRANDISSEMENT','budgetparticipatif.adminFeature.VoteParArrand.name',1,'jsp/admin/plugins/participatorybudget/VoteParArrandissement.jsp','budgetparticipatif.adminFeature.VoteParArrand.description',0,'budgetparticipatif','SYSTEM',NULL,NULL,21);


--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'CAMPAGNEBP_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('CAMPAGNEBP_MANAGEMENT','participatorybudget.adminFeature.ManageCampagnebp.name',1,'jsp/admin/plugins/participatorybudget/campaign/ManageCampagnebp.jsp','participatorybudget.adminFeature.ManageCampagnebp.description',0,'campagnebp',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'CAMPAGNEBP_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('CAMPAGNEBP_MANAGEMENT',1);

