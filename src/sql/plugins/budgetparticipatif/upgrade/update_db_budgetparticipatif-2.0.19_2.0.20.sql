DROP TABLE IF EXISTS `budgetparticipatif_rgpd_treatment_log`;

CREATE TABLE `budgetparticipatif_rgpd_treatment_log` (
	`id_treatment_log`        INT(11)       NOT NULL AUTO_INCREMENT,
	`treatment_timestamp`     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`id_admin_user`           INT(11)       NOT NULL,
	`admin_user_access_code`  VARCHAR(255)  NOT NULL COLLATE 'utf8_unicode_ci',
	`admin_user_email`        VARCHAR(255)  NOT NULL COLLATE 'utf8_unicode_ci',
	`treatment_type`          VARCHAR(30)   NOT NULL COLLATE 'utf8_unicode_ci', -- export, print, mass treatment...
	`treatment_object_name`   VARCHAR(100)  NOT NULL COLLATE 'utf8_unicode_ci', -- exported objet name (list of users, list of depositaries...)
	`treatment_object_fields` VARCHAR(1000) NOT NULL COLLATE 'utf8_unicode_ci', -- exported objet fields (firstname, lastname, address, email...)
	PRIMARY KEY (`id_treatment_log`),
	INDEX `id_treatment_log_index` (`id_treatment_log`)
)
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
;
