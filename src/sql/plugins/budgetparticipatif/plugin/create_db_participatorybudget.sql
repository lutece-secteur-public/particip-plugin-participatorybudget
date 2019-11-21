--
-- Structure for table budgetparticipatif_bizstat_file
--
DROP TABLE IF EXISTS `budgetparticipatif_bizstat_file`;
CREATE TABLE IF NOT EXISTS `budgetparticipatif_bizstat_file` (
  `id_bizstat_file` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `id_admin_user` int(11) NOT NULL,
  `admin_user_access_code` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `admin_user_email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `reason` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `file_name` varchar(500) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  `error` varchar(4000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `file_value` longblob,
  PRIMARY KEY (`id_bizstat_file`)
);

--
-- Structure for table budgetparticipatif_rgpd_treatment_log
--
DROP TABLE IF EXISTS `budgetparticipatif_rgpd_treatment_log`;
CREATE TABLE IF NOT EXISTS `budgetparticipatif_rgpd_treatment_log` (
  `id_treatment_log` int(11) NOT NULL AUTO_INCREMENT,
  `treatment_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id_admin_user` int(11) NOT NULL,
  `admin_user_access_code` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `admin_user_email` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `treatment_type` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `treatment_object_name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `treatment_object_fields` varchar(1000) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_treatment_log`),
  KEY `id_treatment_log_index` (`id_treatment_log`)
);

--
-- Structure for table budgetparticipatif_user_access_vote
--
DROP TABLE IF EXISTS `budgetparticipatif_user_access_vote`;
CREATE TABLE IF NOT EXISTS `budgetparticipatif_user_access_vote` (
  `id_user` varchar(255) NOT NULL,
  `has_acces_vote` int(11) NOT NULL,
  PRIMARY KEY (`id_user`)
);

--
-- Structure for table budgetparticipatif_votes
--
DROP TABLE IF EXISTS `budgetparticipatif_votes`;
CREATE TABLE IF NOT EXISTS `budgetparticipatif_votes` (
  `id_user` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `id_projet` int(11) NOT NULL,
  `date_vote` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `arrondissement` int(11) NOT NULL,
  `age` int(11) NOT NULL,
  `birth_date` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ip_address` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `localisation` int(11) NOT NULL,
  `thematique` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_user`,`id_projet`)
);

--
-- Structure for table budgetparticipatif_votes_history
--
DROP TABLE IF EXISTS `budgetparticipatif_votes_history`;
CREATE TABLE IF NOT EXISTS `budgetparticipatif_votes_history` (
  `id_user` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `id_projet` int(11) NOT NULL,
  `date_vote` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `arrondissement` int(11) NOT NULL,
  `age` int(11) NOT NULL,
  `birth_date` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ip_address` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `localisation` int(11) NOT NULL,
  `thematique` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `status` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `status_export_stats` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `budgetparticipatif_votes_history_index_id_user` (`id_user`)
);

--
-- Structure for table budgetparticipatif_votes_per_location
--
DROP TABLE IF EXISTS `budgetparticipatif_votes_per_location`;
CREATE TABLE IF NOT EXISTS `budgetparticipatif_votes_per_location` (
  `id` int(11) NOT NULL,
  `localisation_ardt` varchar(50) NOT NULL,
  `nb_votes` int(10) NOT NULL,
  PRIMARY KEY (`id`)
);

-- *********************************************************************************************
-- * NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIF *
-- * NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIFY NOTIF *
-- *********************************************************************************************

--
-- Structure for table task_notify_documentbp_cf
--
DROP TABLE IF EXISTS task_notify_documentbp_cf;
CREATE TABLE task_notify_documentbp_cf(
  id_task INT NOT NULL,
  sender_name VARCHAR(255) DEFAULT NULL, 
  sender_email VARCHAR(255) DEFAULT NULL,
  subject VARCHAR(255) DEFAULT NULL, 
  message long VARCHAR DEFAULT NULL,
  recipients_cc VARCHAR(255) DEFAULT '' NOT NULL,
  recipients_bcc VARCHAR(255) DEFAULT '' NOT NULL,
  is_abonnes SMALLINT NOT NULL,
  PRIMARY KEY  (id_task)
);
DROP TABLE IF EXISTS ideation_campagnes_images;
DROP TABLE IF EXISTS ideation_campagnes_phases;
DROP TABLE IF EXISTS ideation_campagnes_themes;
DROP TABLE IF EXISTS ideation_campagnes;
DROP TABLE IF EXISTS ideation_phase_types;
DROP TABLE IF EXISTS ideation_moderation_types;

-- *********************************************************************************************
-- * CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN *
-- * CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN *
-- *********************************************************************************************

--
-- Structure for table ideation_campagnes
--
DROP TABLE IF EXISTS `ideation_campagnes`;
CREATE TABLE IF NOT EXISTS `ideation_campagnes` (
  `id_campagne` int(6) NOT NULL,
  `code_campagne` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `active` smallint(6) NOT NULL,
  `code_moderation_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `moderation_duration` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_campagne`),
  UNIQUE KEY `code_campagne` (`code_campagne`),
  KEY `fk_ideation_campagnes_moderation` (`code_moderation_type`),
  CONSTRAINT `fk_ideation_campagnes_moderation` FOREIGN KEY (`code_moderation_type`) REFERENCES `ideation_moderation_types` (`code_moderation_type`)
);

--
-- Structure for table ideation_campagnes_images
--
DROP TABLE IF EXISTS `ideation_campagnes_images`;
CREATE TABLE IF NOT EXISTS `ideation_campagnes_images` (
  `id_campagne_image` int(6) NOT NULL,
  `code_campagne` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `id_file` int(11) NOT NULL,
  PRIMARY KEY (`id_campagne_image`),
  KEY `code_campagne` (`code_campagne`,`id_file`),
  KEY `id_file` (`id_file`),
  CONSTRAINT `fk_ideation_campagnes_images_campagne` FOREIGN KEY (`code_campagne`) REFERENCES `ideation_campagnes` (`code_campagne`),
  CONSTRAINT `fk_ideation_campagnes_images_file` FOREIGN KEY (`id_file`) REFERENCES `core_file` (`id_file`)
);

--
-- Structure for table ideation_campagnes_phases
--
DROP TABLE IF EXISTS `ideation_campagnes_phases`;
CREATE TABLE IF NOT EXISTS `ideation_campagnes_phases` (
  `id_campagne_phase` int(6) NOT NULL,
  `code_phase_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `code_campagne` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `start` datetime NOT NULL,
  `end` datetime NOT NULL,
  PRIMARY KEY (`id_campagne_phase`),
  KEY `code_campagne` (`code_campagne`,`code_phase_type`),
  KEY `fk_ideation_campagnes_phases_phase` (`code_phase_type`),
  CONSTRAINT `fk_ideation_campagnes_phases_campagne` FOREIGN KEY (`code_campagne`) REFERENCES `ideation_campagnes` (`code_campagne`),
  CONSTRAINT `fk_ideation_campagnes_phases_phase` FOREIGN KEY (`code_phase_type`) REFERENCES `ideation_phase_types` (`code_phase_type`)
);

--
-- Structure for table ideation_campagnes_themes
--
DROP TABLE IF EXISTS `ideation_campagnes_themes`;
CREATE TABLE IF NOT EXISTS `ideation_campagnes_themes` (
  `id_campagne_theme` int(6) NOT NULL,
  `code_campagne` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `code_theme` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `active` smallint(6) NOT NULL,
  `image_file` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_campagne_theme`),
  UNIQUE KEY `code_campagne` (`code_campagne`,`code_theme`),
  CONSTRAINT `ideation_campagnes_themes_campagne` FOREIGN KEY (`code_campagne`) REFERENCES `ideation_campagnes` (`code_campagne`)
);

--
-- Structure for table ideation_depositaire_complement_types
--
DROP TABLE IF EXISTS `ideation_depositaire_complement_types`;
CREATE TABLE IF NOT EXISTS `ideation_depositaire_complement_types` (
  `id_depositaire_complement_type` int(6) NOT NULL,
  `code_depositaire_complement_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_depositaire_complement_type`),
  UNIQUE KEY `code_depositaire_complement_type` (`code_depositaire_complement_type`)
);

--
-- Structure for table ideation_depositaire_types
--
DROP TABLE IF EXISTS `ideation_depositaire_types`;
CREATE TABLE IF NOT EXISTS `ideation_depositaire_types` (
  `id_depositaire_type` int(6) NOT NULL,
  `code_depositaire_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `code_complement_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_depositaire_type`),
  UNIQUE KEY `code_depositaire_type` (`code_depositaire_type`),
  KEY `fk_ideation_depositaire_types_complement` (`code_complement_type`),
  CONSTRAINT `fk_ideation_depositaire_types_complement` FOREIGN KEY (`code_complement_type`) REFERENCES `ideation_depositaire_complement_types` (`code_depositaire_complement_type`)
);

--
-- Structure for table ideation_depositaire_types_values
--
DROP TABLE IF EXISTS `ideation_depositaire_types_values`;
CREATE TABLE IF NOT EXISTS `ideation_depositaire_types_values` (
  `id_depositaire_type_value` int(6) NOT NULL,
  `code_depositaire_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `code` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_depositaire_type_value`),
  UNIQUE KEY `code_depositaire_type` (`code_depositaire_type`,`code`),
  CONSTRAINT `fk_ideation_depositaire_type_values_depositaire` FOREIGN KEY (`code_depositaire_type`) REFERENCES `ideation_depositaire_types` (`code_depositaire_type`)
);

--
-- Structure for table ideation_moderation_types
--
DROP TABLE IF EXISTS `ideation_moderation_types`;
CREATE TABLE IF NOT EXISTS `ideation_moderation_types` (
  `id_moderation_type` int(6) NOT NULL,
  `code_moderation_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_moderation_type`),
  UNIQUE KEY `code_moderation_type` (`code_moderation_type`)
);

--
-- Structure for table ideation_phase_types
--
DROP TABLE IF EXISTS `ideation_phase_types`;
CREATE TABLE IF NOT EXISTS `ideation_phase_types` (
  `id_phase_type` int(6) NOT NULL,
  `code_phase_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_phase_type`),
  UNIQUE KEY `code_phase_type` (`code_phase_type`)
);
