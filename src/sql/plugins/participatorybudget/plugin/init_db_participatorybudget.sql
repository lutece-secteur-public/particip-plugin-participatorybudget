INSERT INTO participatorybudget_campaign_moderation_type (id_moderation_type, code_moderation_type, libelle) VALUES
	(1, 'NONE', 'No moderation'),
	(2, 'PUBLISH', 'Immediate publication'),
	(3, 'WAIT', 'Delayed publication');

INSERT INTO participatorybudget_campaign (id_campaign, code_campaign, title, description, active, code_moderation_type, moderation_duration) VALUES
	(1, 'A', 'Participatory Budget 2019', 'Participatory Budget 2019', 1, 'NONE', 0);

INSERT INTO participatorybudget_campaign_phase_type (id_phase_type, code_phase_type, label, order_num) VALUES
	(1, 'PRE_IDEATION', 'Before ideation phase', 1),
	(2, 'IDEATION', 'Ideation phase open', 2),
	(3, 'POST_IDEATION', 'After ideation phase', 3),
	(4, 'CO_CONSTRUCTION', 'Co-construction phase', 4),
	(5, 'PRE_SUBMIT', 'Before submission phase', 5),
	(6, 'SUBMIT', 'View projects to be voted', 6),
	(7, 'PRE_VOTE', 'Before vote phase', 7),
	(8, 'VOTE', 'Vote phase open', 8),
	(9, 'POST_VOTE', 'After vote phase', 9),
	(10, 'PRE_RESULT', 'Before result phase', 10),
	(11, 'RESULT', 'Announce vote results', 11);

INSERT INTO participatorybudget_campaign_phase (id_campaign_phase, code_phase_type, code_campaign, start, end) VALUES
	(1, 'PRE_IDEATION', 'A', '2019-08-09 00:00:00', '2019-08-22 23:59:59'),
	(2, 'IDEATION', 'A', '2019-08-23 00:00:00', '2019-12-05 23:59:59'),
	(3, 'POST_IDEATION', 'A', '2019-09-06 00:00:00', '2019-09-19 23:59:59'),
	(4, 'CO_CONSTRUCTION', 'A', '2019-09-20 00:00:00', '2019-10-03 23:59:59'),
	(5, 'PRE_SUBMIT', 'A', '2019-10-04 00:00:00', '2019-10-17 23:59:59'),
	(6, 'SUBMIT', 'A', '2019-10-18 00:00:00', '2019-10-31 23:59:59'),
	(7, 'PRE_VOTE', 'A', '2019-11-01 00:00:00', '2019-11-14 23:59:59'),
	(8, 'VOTE', 'A', '2019-11-15 00:00:00', '2019-11-28 23:59:59'),
	(9, 'POST_VOTE', 'A', '2019-11-29 00:00:00', '2019-12-12 23:59:59'),
	(10, 'PRE_RESULT', 'A', '2019-12-13 00:00:00', '2019-12-26 23:59:59'),
	(11, 'RESULT', 'A', '2019-12-27 00:00:00', '2020-01-09 23:59:59');

INSERT INTO participatorybudget_campaign_theme (id_campaign_theme, code_campaign, code_theme, title, description, active, image_file) VALUES
	(1, 'A', 'ENVIRONMENT', 'Environment issues', 'Environment issues', 1, NULL),
	(2, 'A', 'SOCIAL', 'Social issues', 'Social issues', 1, NULL),
	(3, 'A', 'SPORT', 'Sport', 'Sport', 1, NULL),
	(4, 'A', 'GENERAL', 'General', 'General', 1, NULL);

INSERT INTO participatorybudget_votes_per_location (id, location_ardt, nb_votes) VALUES
	(1, 'whole_city', 2),
	(2, 'area_1', 3),
	(3, 'area_2', 0),
	(4, 'area_3', 1),
	(5, 'area_4', 0),
	(6, 'area_5', 1);

INSERT INTO participatorybudget_campaign_area VALUES 
	(0,'A','whole city',1,'whole','3'),
	(1,'A','area 1',1,'localized','1'),
	(2,'A','area 2',1,'localized','1'),
	(3,'A','area 3',1,'localized','1'),
	(4,'A','area 4',1,'localized','2'),
	(5,'A','area 5',1,'localized','1');

INSERT INTO task_notify_documentbp_cf (id_task, sender_name, sender_email, subject, message, recipients_cc, recipients_bcc, is_abonnes) VALUES
	(36, 'Ville de Paris - budget participatif', 'budget.participatif@paris.fr', 'Des nouvelles des projets gagnants !', '<p>Bonjour,</p>\r\n<p>Nous avons le plaisir de vous informer que la fiche du projet &laquo; ${titre_projet!} &raquo; auquel vous &ecirc;tes associ&eacute;&middot;e,&nbsp;a &eacute;t&eacute; mise &agrave; jour :</p>\r\n<blockquote><span style="color: #0000ff;">${message_notification!}</span></blockquote>\r\n<p>Vous pouvez consulter la fiche du projet en cliquant sur le lien suivant, pour voir notamment le d&eacute;tail du planning de mise en &oelig;uvre :</p>\r\n<blockquote><a href="${url_fiche!\'\'}">fiche du projet "${titre_projet!}"</a></blockquote>\r\n<p>Pour toute question, vous pouvez consulter <span style="text-decoration: underline;"><span style="color: #0000ff;"><a style="color: #0000ff; text-decoration: underline;" href="https://budgetparticipatif.paris.fr/bp/jsp/site/Portal.jsp?page=helpdesk&amp;faq_id=3" target="_blank">la foire aux questions du budget participatif</a></span></span> ou nous &eacute;crire &agrave; <span style="text-decoration: underline;"><span style="color: #0000ff;"><a style="color: #0000ff; text-decoration: underline;" href="mailto:budgetparticipatif@paris.fr" target="_blank">budgetparticipatif@paris.fr</a></span></span></p>\r\n<p>L&rsquo;&eacute;quipe du budget participatif de la Ville de Paris</p>\r\n<p><img src="https://budgetparticipatif.paris.fr/bp/document?id=5278&amp;id_attribute=43" alt="mdp" width="103" height="123" /></p>\r\n<p>#budgetparticipatif</p>\r\n<p>Nous suivre sur <span style="text-decoration: underline;"><span style="color: #0000ff;"><a style="color: #0000ff; text-decoration: underline;" href="https://www.facebook.com/groups/NotreBudget/" target="_blank">Facebook</a></span></span> et sur <span style="text-decoration: underline;"><span style="color: #0000ff;"><a style="color: #0000ff; text-decoration: underline;" href="https://twitter.com/Paris" target="_blank">Twitter</a></span></span></p>\r\n<p>Pour param&eacute;trer les notifications que vous recevez, rendez-vous dans la rubrique &laquo; Mes notifications &raquo; de votre compte sur budgetparticipatif.paris</p>', 'ddct-mpc-site-bp@paris.fr', '', 1);

