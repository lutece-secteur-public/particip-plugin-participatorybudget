INSERT INTO `budgetparticipatif_votes_per_location` (`id`, `localisation_ardt`, `nb_votes`) VALUES
	( 1, '75001'   , 2 ),
	( 1, '75002'   , 2 ),
	( 3, 'All-City', 5 )
;

INSERT INTO `ideation_campagnes` (`id_campagne`, `code_campagne`, `title`, `description`, `active`, `code_moderation_type`, `moderation_duration`) VALUES
	( 1, 'A', 'Participatory budgeting', 'Participatory budgeting', 1, 'NONE', NULL )
;

INSERT INTO `ideation_campagnes_phases` (`id_campagne_phase`, `code_phase_type`, `code_campagne`, `start`, `end`) VALUES
	(  1, 'PRE_IDEATION'   , 'A', '2019-08-09 00:00:00', '2019-08-22 23:59:59' ),
	(  2, 'IDEATION'       , 'A', '2019-08-23 00:00:00', '2019-09-05 23:59:59' ),
	(  3, 'POST_IDEATION'  , 'A', '2019-09-06 00:00:00', '2019-09-19 23:59:59' ),
	(  4, 'CO_CONSTRUCTION', 'A', '2019-09-20 00:00:00', '2019-10-03 23:59:59' ),
	(  5, 'PRE_SUBMIT'     , 'A', '2019-10-04 00:00:00', '2019-10-17 23:59:59' ),
	(  6, 'SUBMIT'         , 'A', '2019-10-18 00:00:00', '2019-10-31 23:59:59' ),
	(  7, 'PRE_VOTE'       , 'A', '2019-11-01 00:00:00', '2019-11-14 23:59:59' ),
	(  8, 'VOTE'           , 'A', '2019-11-15 00:00:00', '2019-11-28 23:59:59' ),
	(  9, 'POST_VOTE'      , 'A', '2019-11-29 00:00:00', '2019-12-12 23:59:59' ),
	( 10, 'PRE_RESULT'     , 'A', '2019-12-13 00:00:00', '2019-12-26 23:59:59' ),
	( 11, 'RESULT'         , 'A', '2019-12-27 00:00:00', '2020-01-09 23:59:59' )
;

INSERT INTO `ideation_campagnes_themes` (`id_campagne_theme`, `code_campagne`, `code_theme`, `title`, `description`, `active`, `image_file`) VALUES
	(  1, 'A', 'CADRE'        , 'Cadre de vie'                    , 'Cadre de vie'                    , 1, NULL ),
	(  2, 'A', 'CULTURE'      , 'Culture et patrimoine'           , 'Culture et patrimoine'           , 1, NULL ),
	(  3, 'A', 'ECO'          , 'Économie, emploi et attractivité', 'Économie, emploi et attractivité', 1, NULL ),
	(  4, 'A', 'EDU'          , 'Education et jeunesse'           , 'Education et jeunesse'           , 1, NULL ),
	(  5, 'A', 'ENVIRONNEMENT', 'Environnement'                   , 'Environnement'                   , 1, NULL ),
	(  6, 'A', 'NUMERIQUE'    , 'Ville intelligente et numérique' , 'Ville intelligente et numérique' , 1, NULL ),
	(  7, 'A', 'PROPRETE'     , 'Propreté'                        , 'Propreté'                        , 1, NULL ),
	(  8, 'A', 'SANTE'        , 'Santé'                           , 'Santé'                           , 1, NULL ),
	(  9, 'A', 'SECURITE'     , 'Prévention et sécurité'          , 'Prévention et sécurité'          , 1, NULL ),
	( 10, 'A', 'SOLIDARITE'   , 'Solidarité et cohésion sociale'  , 'Solidarité et cohésion sociale'  , 1, NULL ),
	( 11, 'A', 'SPORT'        , 'Sport'                           , 'Sport'                           , 1, NULL ),
	( 12, 'A', 'TRANSPORT'    , 'Transport et mobilité'           , 'Transport et mobilité'           , 1, NULL )
;

INSERT INTO `ideation_depositaire_complement_types` (`id_depositaire_complement_type`, `code_depositaire_complement_type`, `libelle`) VALUES
	( 1, 'NONE', 'Pas de complement' ),
	( 2, 'LIST', 'List de valeurs'   ),
	( 3, 'FREE', 'Saisie libre'      )
;

INSERT INTO `ideation_depositaire_types` (`id_depositaire_type`, `code_depositaire_type`, `libelle`, `code_complement_type`) VALUES
	( 1, 'PARTICULIER', 'Particulier'     , 'NONE' ),
	( 2, 'ASSO'       , 'Association'     , 'FREE' ),
	( 3, 'COUNCIL'    , 'District council', 'LIST' ),
	( 4, 'AUTRE'      , 'Autre'           , 'FREE' )
;

INSERT INTO `ideation_phase_types` (`id_phase_type`, `code_phase_type`, `libelle`) VALUES
	(  1, 'PRE_IDEATION'   , 'Pré idéation'                                 ),
	(  2, 'IDEATION'       , 'Idéation'                                     ),
	(  3, 'POST_IDEATION'  , 'Post idéation'                                ),
	(  4, 'CO_CONSTRUCTION', 'Co-construction'                              ),
	(  5, 'PRE_SUBMIT'     , 'Avant publication des projets soumis au vote' ),
	(  6, 'SUBMIT'         , 'Publication des projets soumis au vote'       ),
	(  7, 'PRE_VOTE'       , 'Avant vote'                                   ),
	(  8, 'VOTE'           , 'Vote'                                         ),
	(  9, 'POST_VOTE'      , 'Après vote'                                   ),
	( 10, 'PRE_RESULT'     , 'Avant annonce des projets gagnants'           ),
	( 11, 'RESULT'         , 'Annonce des projets gagnants'                 )
;

INSERT INTO `ideation_depositaire_types_values` (`id_depositaire_type_value`, `code_depositaire_type`, `code`, `libelle`) VALUES
	( 1, 'COUNCIL', '01VENDO', '1er - Vendome'      ),
	( 2, 'COUNCIL', '01PALAI', '1er - Palais Royal' )
;

INSERT INTO `ideation_moderation_types` (`id_moderation_type`, `code_moderation_type`, `libelle`) VALUES
	( 1, 'NONE'   , 'Pas de modération'                     ),
	( 2, 'PUBLISH', 'Idée publiée en grisée jusqu au délai' ),
	( 3, 'WAIT'   , 'Idée non publiée jusqu au délai'       )
;