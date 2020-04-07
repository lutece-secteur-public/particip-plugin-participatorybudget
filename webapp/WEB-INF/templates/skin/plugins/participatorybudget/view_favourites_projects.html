
<h1 class="visuallyhidden">Mes projets favoris et auxquels je me suis associé</h1>

<#-- *********************************************************************************** -->
<#-- * TABLES TABLES TABLES TABLES TABLES TABLES TABLES TABLES TABLES TABLES TABLES TA * -->
<#-- * TABLES TABLES TABLES TABLES TABLES TABLES TABLES TABLES TABLES TABLES TABLES TA * -->
<#-- *********************************************************************************** -->

<div id="my-favs-container" class="container bg-color6">

	<@messages infos=infos />
	<@messages errors=errors />

	<#assign current_campaign = (campaignService.getLastCampaign().getTitle())?remove_beginning("Budget Participatif ")>

	<div id="favorite">
	
		<#-- img src="images/local/skin/page-icon-prop.png" class="pull-left title-img" -->
		<h2>Ma sélection pour le vote ${current_campaign!''}</h2>
		
		<@projectTable projects=favouriteProjects extend='favorite' edition=current_campaign tip='Ce tableau vous aidera à retrouver les projets pour lesquels vous souhaiteriez voter cette année !' class='favorite-current-campaign'/>
		
		<#-- img src="images/local/skin/page-icon-prop.png" class="pull-left title-img" -->
		<h2>Mes projets favoris (toutes campaigns)</h2>
		
		<@projectTable projects=favouriteProjects extend='favorite' />
		
		<#-- img src="images/local/skin/page-icon-prop.png" class="pull-left title-img" -->
		<h2>Les projets auxquels je me suis associé</h2>
		
		<@projectTable projects=followerProjects extend='follow' />
		
	</div>
</div>

<#-- *********************************************************************************** -->
<#-- * DATATABLES   DATATABLES   DATATABLES   DATATABLES   DATATABLES   DATATABLES   D * -->
<#-- * DATATABLES   DATATABLES   DATATABLES   DATATABLES   DATATABLES   DATATABLES   D * -->
<#-- *********************************************************************************** -->

<#-- dataTable sort initializing -->
<script type="text/javascript">
	$(window).load(function() {
		$('.table').DataTable( {
			"paging":      false,
			"info":        false,
			"order":       [[ 1, "desc" ]],
			"searching":   false,
			"columnDefs": [
				{ "orderable": false, "targets": 3 }
			],
			"language": {
				zeroRecords:    "Aucun &eacute;l&eacute;ment &agrave; afficher",
		        emptyTable:     "Vous n'avez pas encore mis de projet ${current_campaign!''} en favori !"
			}
		} );
	} );
</script>
		
<#-- *********************************************************************************** -->
<#-- * MACRO MACRO MACRO MACRO MACRO MACRO MACRO MACRO MACRO MACRO MACRO MACRO MACRO M * -->
<#-- * MACRO MACRO MACRO MACRO MACRO MACRO MACRO MACRO MACRO MACRO MACRO MACRO MACRO M * -->
<#-- *********************************************************************************** -->
	
<#macro projectTable projects extend edition='all' tip='Astuce : vous pouvez trier en cliquant sur les titres des colonnes. Maintenez la touche  \'MAJ\' appuyée pour sélectionner plusieurs colonnes de tri !' class=''>

	<div class="row ${class!''}">
	
		<#if !projects?has_content >
			<div class="col-xs-12">
				<p class="empty-list">Vous n'avez pas de projet dans cette liste.</p>
			</div>
		<#else>

			<div class="col-xs-12 col-sm-12">
				<p class="tips hidden-xs">${tip}</p>
				<table class="table table-hover">
				
					<#-- Titre du tableau -->
					<thead>
						<tr>
							<th class="         col-sm-6 col-md-7 ellip hidden-xs "><span>Titre</span></th>
							<th class="         col-sm-6 col-md-7 ellip visible-xs"><span>Titre</span></th>
							<#if edition == 'all'><th class="         col-sm-2          ellip hidden-xs "><span>Année</span></th></#if>
							<th class="         col-sm-2          ellip hidden-xs "><span>Location</span></th>
							<th class="col-xs-1 col-sm-2 col-xs-1 ellip           "><span class="hidden-xs">Supprimer</span></th>
						</tr>
					</thead>
				
					<#-- Lignes du tableau -->
					<tbody>
						<#list projects?sort_by("title") as document>
						
							<#-- Récupération de quelques attributs du document -->
							<#list document.attributes as attribute>
								<#if     attribute.code = 'location'><#assign  location_projet = attribute.textValue>
								<#elseif attribute.code = 'theme'>  <#assign  theme_projet   = attribute.textValue>
								<#elseif attribute.code = 'campaign'>    <#assign  campaign_projet     = (attribute.textValue)?remove_beginning("Budget Participatif ")>
								</#if>
							</#list>

							<#--								
								<#assign code_theme = "autre">
								<#if theme_projet??>
									<#if theme_projet = "Cadre de vie"><#assign code_theme = "cadre_vie">
									<#elseif theme_projet = "Culture" || theme_projet = "Culture et patrimoine"><#assign code_theme = "culture">
									<#elseif theme_projet = "Economie et emploi"><#assign code_theme = "economie_emploi">
									<#elseif theme_projet = "Education et jeunesse"><#assign code_theme = "education">
									<#elseif theme_projet = "Environnement" || theme_projet = "Nature en ville"><#assign code_theme = "environnement">
									<#elseif theme_projet = "Logement et habitat"><#assign code_theme = "logement">
									<#elseif theme_projet = "Participation citoyenne"><#assign code_theme = "participation_citoyenne">
									<#elseif theme_projet = "Prévention et sécurité"><#assign code_theme = "prevention_securite">
									<#elseif theme_projet = "Propreté"><#assign code_theme = "proprete">
									<#elseif theme_projet = "Solidarité" || theme_projet = "Vivre ensemble" || theme_projet = "Solidarité et cohésion sociale"><#assign code_theme = "solidarite">
									<#elseif theme_projet = "Sport"><#assign code_theme = "sport">
									<#elseif theme_projet = "Transport et mobilité"><#assign code_theme = "transport">
									<#elseif theme_projet = "Ville intelligente et numérique"><#assign code_theme = "ville_numerique">
									</#if>
								</#if>
							-->

							<#if (edition == 'all') || (edition == campaign_projet) >
	
								<tr>
									<td class="ellip hidden-xs "><a class="project-link" href="jsp/site/Portal.jsp?document_id=${document.id}&portlet_id=158">${document.title}</a></td>
									<td class="ellip visible-xs">BP ${campaign_projet!"?"} - <a class="project-link" href="jsp/site/Portal.jsp?document_id=${document.id}&portlet_id=158">${document.title}</a></td>
									<#if edition == 'all'><td class="ellip hidden-xs "><span>${campaign_projet!'?'}</span></td></#if>
									<td class="ellip hidden-xs "><span>${location_projet?replace(" arrondissement", "")}</span></td>
									<td class="ellip           ">@Extender[${document.id},document,${extend},{show:"tinylink"}]@</td>
								</tr>
							</#if>	
						</#list>
					</tbody>
				</table>
			</div>
		</#if>
	</div>
	
</#macro>