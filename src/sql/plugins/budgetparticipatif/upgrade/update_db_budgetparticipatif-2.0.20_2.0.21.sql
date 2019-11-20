DROP TABLE IF EXISTS `budgetparticipatif_bizstat_file`;

CREATE TABLE `budgetparticipatif_bizstat_file` (
	`id_bizstat_file`		 INT(11)       NOT NULL AUTO_INCREMENT,
	`status`          		 VARCHAR(50)   NOT NULL                           COLLATE 'utf8_unicode_ci',
	`id_admin_user`          INT(11)       NOT NULL,
	`admin_user_access_code` VARCHAR(255)  NOT NULL                           COLLATE 'utf8_unicode_ci',
	`admin_user_email`       VARCHAR(255)  NOT NULL                           COLLATE 'utf8_unicode_ci',
	`reason`                 VARCHAR(255)  NOT NULL                           COLLATE 'utf8_unicode_ci',
	`file_name`       		 VARCHAR(500)  NOT NULL                           COLLATE 'utf8_unicode_ci',
	`description`     		 VARCHAR(1000) NOT NULL                           COLLATE 'utf8_unicode_ci',
	`error`           		 VARCHAR(4000)     NULL DEFAULT NULL              COLLATE 'utf8_unicode_ci',
	`creation_date`   		 TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`file_value`      		 LONGBLOB          NULL,
	PRIMARY KEY (`id_bizstat_file`)
)
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
;
