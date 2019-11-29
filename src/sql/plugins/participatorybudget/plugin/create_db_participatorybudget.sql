-- --------------------------------------------------------
-- Hôte :                        127.0.0.1
-- Version du serveur:           5.7.15-log - MySQL Community Server (GPL)
-- SE du serveur:                Win64
-- HeidiSQL Version:             9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Export de la structure de table pb. participatorybudget_bizstat_file
DROP TABLE IF EXISTS `participatorybudget_bizstat_file`;
CREATE TABLE IF NOT EXISTS `participatorybudget_bizstat_file` (
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
  `file_value` longblob
  ,
  PRIMARY KEY (`id_bizstat_file`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- L'exportation de données n'était pas sélectionnée.


-- Export de la structure de table pb. participatorybudget_campaign
DROP TABLE IF EXISTS `participatorybudget_campaign`;
CREATE TABLE IF NOT EXISTS `participatorybudget_campaign` (
  `id_campagne` int(6) NOT NULL,
  `code_campagne` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `active` smallint(6) NOT NULL,
  `code_moderation_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `moderation_duration` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_campagne`),
  UNIQUE KEY `code_campagne` (`code_campagne`),
  KEY `fk_participatorybudget_campaign_moderation` (`code_moderation_type`),
  CONSTRAINT `fk_participatorybudget_campaign_moderation` FOREIGN KEY (`code_moderation_type`) REFERENCES `participatorybudget_campaign_moderation_type` (`code_moderation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- L'exportation de données n'était pas sélectionnée.


-- Export de la structure de table pb. participatorybudget_campaign_image
DROP TABLE IF EXISTS `participatorybudget_campaign_image`;
CREATE TABLE IF NOT EXISTS `participatorybudget_campaign_image` (
  `id_campagne_image` int(6) NOT NULL,
  `code_campagne` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `id_file` int(11) NOT NULL,
  PRIMARY KEY (`id_campagne_image`),
  KEY `code_campagne` (`code_campagne`,`id_file`),
  KEY `id_file` (`id_file`),
  CONSTRAINT `fk_participatorybudget_campaign_images_campagne` FOREIGN KEY (`code_campagne`) REFERENCES `participatorybudget_campaign` (`code_campagne`),
  CONSTRAINT `fk_participatorybudget_campaign_images_file` FOREIGN KEY (`id_file`) REFERENCES `core_file` (`id_file`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- L'exportation de données n'était pas sélectionnée.


-- Export de la structure de table pb. participatorybudget_campaign_moderation_type
DROP TABLE IF EXISTS `participatorybudget_campaign_moderation_type`;
CREATE TABLE IF NOT EXISTS `participatorybudget_campaign_moderation_type` (
  `id_moderation_type` int(6) NOT NULL,
  `code_moderation_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_moderation_type`),
  UNIQUE KEY `code_moderation_type` (`code_moderation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- L'exportation de données n'était pas sélectionnée.


-- Export de la structure de table pb. participatorybudget_campaign_phase
DROP TABLE IF EXISTS `participatorybudget_campaign_phase`;
CREATE TABLE IF NOT EXISTS `participatorybudget_campaign_phase` (
  `id_campagne_phase` int(6) NOT NULL,
  `code_phase_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `code_campagne` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `start` datetime NOT NULL,
  `end` datetime NOT NULL,
  PRIMARY KEY (`id_campagne_phase`),
  KEY `code_campagne` (`code_campagne`,`code_phase_type`),
  KEY `fk_participatorybudget_campaign_phases_phase` (`code_phase_type`),
  CONSTRAINT `fk_participatorybudget_campaign_phases_campagne` FOREIGN KEY (`code_campagne`) REFERENCES `participatorybudget_campaign` (`code_campagne`),
  CONSTRAINT `fk_participatorybudget_campaign_phases_phase` FOREIGN KEY (`code_phase_type`) REFERENCES `participatorybudget_campaign_phase_type` (`code_phase_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- L'exportation de données n'était pas sélectionnée.


-- Export de la structure de table pb. participatorybudget_campaign_phase_type
DROP TABLE IF EXISTS `participatorybudget_campaign_phase_type`;
CREATE TABLE IF NOT EXISTS `participatorybudget_campaign_phase_type` (
  `id_phase_type` int(6) NOT NULL,
  `code_phase_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_phase_type`),
  UNIQUE KEY `code_phase_type` (`code_phase_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- L'exportation de données n'était pas sélectionnée.


-- Export de la structure de table pb. participatorybudget_campaign_theme
DROP TABLE IF EXISTS `participatorybudget_campaign_theme`;
CREATE TABLE IF NOT EXISTS `participatorybudget_campaign_theme` (
  `id_campagne_theme` int(6) NOT NULL,
  `code_campagne` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `code_theme` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `active` smallint(6) NOT NULL,
  `image_file` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_campagne_theme`),
  UNIQUE KEY `code_campagne` (`code_campagne`,`code_theme`),
  CONSTRAINT `participatorybudget_campaign_themes_campagne` FOREIGN KEY (`code_campagne`) REFERENCES `participatorybudget_campaign` (`code_campagne`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- L'exportation de données n'était pas sélectionnée.


-- Export de la structure de table pb. participatorybudget_rgpd_treatment_log
DROP TABLE IF EXISTS `participatorybudget_rgpd_treatment_log`;
CREATE TABLE IF NOT EXISTS `participatorybudget_rgpd_treatment_log` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- L'exportation de données n'était pas sélectionnée.


-- Export de la structure de table pb. participatorybudget_user_access_vote
DROP TABLE IF EXISTS `participatorybudget_user_access_vote`;
CREATE TABLE IF NOT EXISTS `participatorybudget_user_access_vote` (
  `id_user` varchar(255) NOT NULL,
  `has_acces_vote` int(11) NOT NULL,
  PRIMARY KEY (`id_user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- L'exportation de données n'était pas sélectionnée.


-- Export de la structure de table pb. participatorybudget_votes
DROP TABLE IF EXISTS `participatorybudget_votes`;
CREATE TABLE IF NOT EXISTS `participatorybudget_votes` (
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
  `mobile_phone` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_user`,`id_projet`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- L'exportation de données n'était pas sélectionnée.


-- Export de la structure de table pb. participatorybudget_votes_history
DROP TABLE IF EXISTS `participatorybudget_votes_history`;
CREATE TABLE IF NOT EXISTS `participatorybudget_votes_history` (
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
  KEY `participatorybudget_votes_history_index_id_user` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- L'exportation de données n'était pas sélectionnée.


-- Export de la structure de table pb. participatorybudget_votes_per_location
DROP TABLE IF EXISTS `participatorybudget_votes_per_location`;
CREATE TABLE IF NOT EXISTS `participatorybudget_votes_per_location` (
  `id` int(11) NOT NULL,
  `localisation_ardt` varchar(50) NOT NULL,
  `nb_votes` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- Export de la structure de table pb. task_notify_documentbp_cf
DROP TABLE IF EXISTS `task_notify_documentbp_cf`;
CREATE TABLE IF NOT EXISTS `task_notify_documentbp_cf` (
  `id_task` int(11) NOT NULL,
  `sender_name` varchar(255) DEFAULT NULL,
  `sender_email` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `message` mediumtext,
  `recipients_cc` varchar(255) NOT NULL DEFAULT '',
  `recipients_bcc` varchar(255) NOT NULL DEFAULT '',
  `is_abonnes` smallint(6) NOT NULL,
  PRIMARY KEY (`id_task`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- L'exportation de données n'était pas sélectionnée.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
