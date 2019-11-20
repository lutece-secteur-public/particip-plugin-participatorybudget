REPLACE INTO core_datastore (entity_key, entity_value) 
VALUES ('budgetparticipatif.site_property.vote_confirmation.subject', 'Budget Participatif - Confirmation de votre vote'),
('budgetparticipatif.site_property.vote_confirmation.template.htmlblock', '[#ftl]
<p>Bonjour et merci de votre int&eacute;r&ecirc;t pour le Budget Participatif,</p>
<p>Nous avons bien enregistr&eacute; votre vote pour ${vote_projects_list?size} projet(s).</p>
<p>Vote effectu&eacute; le ${validate_vote_date!}.</p>
<p><b>Donnez de la voix &agrave; vos projets !</b> Faites voter votre entourage, parlez-en autour de vous et sur les r&eacute;seaux sociaux !</p>
<p>L&rsquo;Equipe Budget Participatif de la Ville de Paris</p>'),
('budgetparticipatif.site_property.vote_confirmation.sender', 'ddct-mpc-site-bp@paris.fr'),
('budgetparticipatif.site_property.vote_confirmation.cc', 'ddct-mpc-site-bp@paris.fr');