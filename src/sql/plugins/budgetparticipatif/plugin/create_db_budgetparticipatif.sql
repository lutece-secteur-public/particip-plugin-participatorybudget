--
-- Structure for table budgetparticipatif_votes
--
DROP TABLE IF EXISTS budgetparticipatif_votes;
CREATE TABLE budgetparticipatif_votes (
	id_user VARCHAR(255) NOT NULL,
	id_projet int NOT NULL,
	date_vote timestamp NOT NULL,
	arrondissement int NOT NULL,
	age int NOT NULL,
	birth_date VARCHAR(255) DEFAULT NULL,
	ip_address VARCHAR(100) DEFAULT '' NOT NULL,
	title VARCHAR(255) DEFAULT '' NOT NULL,
	localisation int NOT NULL,
	thematique VARCHAR(255) DEFAULT '' NOT NULL,
	status int DEFAULT 0 NOT NULL, 
	PRIMARY KEY( id_user, id_projet )
);

--
-- Structure for table budgetparticipatif_votes
--
DROP TABLE IF EXISTS budgetparticipatif_votes_history;
CREATE TABLE budgetparticipatif_votes_history (
	id_user VARCHAR(255) NOT NULL,
	id_projet int NOT NULL,
	date_vote timestamp NOT NULL,
	arrondissement int NOT NULL,
	age int NOT NULL,
	birth_date VARCHAR(255) DEFAULT NULL,
	ip_address VARCHAR(100) DEFAULT '' NOT NULL,
	title VARCHAR(255) DEFAULT '' NOT NULL,
	localisation int NOT NULL,
	thematique VARCHAR(255) DEFAULT '' NOT NULL,
	status int NOT NULL,
	id int NOT NULL,
	status_export_stats int DEFAULT 0 NOT NULL, 
	PRIMARY KEY( id)
);

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

