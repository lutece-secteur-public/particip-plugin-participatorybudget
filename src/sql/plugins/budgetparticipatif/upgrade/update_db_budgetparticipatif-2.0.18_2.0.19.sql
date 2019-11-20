DELETE 
FROM core_admin_right 
WHERE id_right = 'BIZ_STAT';

INSERT 
INTO core_admin_right (
	id_right,
	name,
	level_right,
	admin_url,
	description,
	is_updatable,
	plugin_name,
	id_feature_group,
	icon_url,
	documentation_url, 
	id_order 
) 
VALUES 
(
	'BIZ_STAT',
	'budgetparticipatif.adminFeature.bizStat.name',
	1,
	'jsp/admin/plugins/budgetparticipatif/BizStat.jsp',
	'budgetparticipatif.adminFeature.bizStat.description',
	0,
	'budgetparticipatif',
	'SYSTEM',
	NULL,
	NULL,
	21
);

