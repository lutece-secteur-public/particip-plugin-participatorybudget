INSERT INTO `core_datastore` VALUES ('solr.app.conf.projects_mdp_detail.addonBeans.0','budgetparticipatif.VotesSolrAddon');
INSERT INTO `core_datastore` VALUES ('solr.app.conf.projects_mdp_detail.fq','type:Projet 2015\" AND statut_project_text:\"SOUMIS');
INSERT INTO `core_datastore` VALUES ('solr.app.conf.projects_mdp_detail.template','skin/plugins/budgetparticipatif/projet_mdp_details_solr_search_results.html');

--
-- Structure for table budgetparticipatif_votes_per_location
--
DROP TABLE IF EXISTS budgetparticipatif_votes_per_location;
CREATE TABLE budgetparticipatif_votes_per_location (
    id int NOT NULL,
    localisation_ardt varchar(50) NOT NULL ,
    nb_votes int(10) NOT NULL,
    PRIMARY KEY( id)
);

--
-- Structure for table budgetparticipatif_user_access_vote
--
DROP TABLE IF EXISTS budgetparticipatif_user_access_vote;
CREATE TABLE budgetparticipatif_user_access_vote (
    id_user VARCHAR(255) NOT NULL,
    has_acces_vote int NOT NULL ,
    PRIMARY KEY( id_user )
);
ALTER TABLE budgetparticipatif_votes ADD COLUMN status int NOT NULL DEFAULT 0 ;

ALTER TABLE budgetparticipatif_votes_history ADD COLUMN status_export_stats int DEFAULT 0 NOT NULL;