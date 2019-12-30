<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">

	<xsl:param name="site-path" select="site-path" />

	<xsl:variable name="portlet-id" select="portlet/portlet-id" />

	<!-- 
	<portlet>
		<portlet-name>Les realisations a la une</portlet-name>
		<portlet-id>195</portlet-id>
		<page-id>7</page-id>
		<plugin-name>document</plugin-name>
		<display-portlet-title>0</display-portlet-title>
		<display-on-small-device>1</display-on-small-device>
		<display-on-normal-device>1</display-on-normal-device>
		<display-on-large-device>1</display-on-large-device>
		<display-on-xlarge-device>1</display-on-xlarge-device>
		<document-list-portlet>
			<document>
				<document-id>1636</document-id>
				<document-date-publication>06/07/2018</document-date-publication>
				<document-xml-content>
					<pb_project>
						<document-id>1636</document-id>
						<document-title>Sécuriser les traversées piétonnes avenue Simon Bolivar</document-title>
						<document-summary>Le projet consiste à limiter la vitesse sur l'avenue Simon Bolivar à 30 km/h et à installer des ralentisseurs type coussins berlinois, afin de réduire la pollution sonore et d'assurer un meilleur partage de l'espace public entre tous les usagers.</document-summary>
						<document-date-begin/>
						<document-date-end/>
						<document-categories/>
						<pb_project-code_projet>22</pb_project-code_projet>
						<pb_project-identifiant>2871</pb_project-identifiant>
						<pb_project-thematique>Transport et mobilité</pb_project-thematique>
						<pb_project-description>&lt;p&gt;Le projet consiste &amp;agrave; limiter la vitesse sur l'avenue Simon Bolivar &amp;agrave; 30 km/h et &amp;agrave; installer des ralentisseurs type coussins berlinois, afin de r&amp;eacute;duire la pollution sonore et d'assurer un meilleur partage de l'espace public entre tous les usagers.&lt;/p&gt;</pb_project-description>
						<pb_project-localisation>19e arrondissement</pb_project-localisation>
						<pb_project-lieu>Avenue Simon Bolivar</pb_project-lieu>
						<pb_project-url>https://idee.paris.fr/amenagement-de-lavenue-simon-bolivar</pb_project-url>
						<pb_project-budget>200000</pb_project-budget>
						<pb_project-contributeur>Projet élaboré à partir des propositions de deux Parisiens.</pb_project-contributeur>
						<pb_project-direction>DVD</pb_project-direction>
						<pb_project-image>&lt;img src="document?id=1329&amp;id_attribute=43" class="img-responsive" alt="Thématique : Transport et mobilité" title="Thématique : Transport et mobilité" height="100%" width="100%"/&gt;</pb_project-image>
						<pb_project-nombre_de_votes>791</pb_project-nombre_de_votes>
						<pb_project-statut_project>SUIVI</pb_project-statut_project>
						<pb_project-content>&lt;p style="margin: 0cm 0cm 10pt;"&gt;&lt;font face="Calibri" size="3"&gt;L’avenue Simon Bolivar au niveau du 36 ayant déjà été aménagée pour plus de sécurité, la sécurisation de l’avenue a consisté&amp;nbsp;en la mise en place d’îlots au niveau de la rue Sadi Lecointe et de la rue Lauzi, complétée par la création d’un plateau surélevé.&lt;/font&gt;&lt;/p&gt;
						</pb_project-content>
						<pb_project-pop_district/>
						<pb_project-localisation_precise>
							<geoloc>
							<lon>2.379279372158752</lon>
							<lat>48.87759102013792</lat>
							<address>Avenue Simon Bolivar, 75019 PARIS</address>
							</geoloc>
						</pb_project-localisation_precise>
						<pb_project-phase1_date_deb>01/04/2016</pb_project-phase1_date_deb>
						<pb_project-phase1_echeancier/>
						<pb_project-phase2_date_deb>01/10/2016</pb_project-phase2_date_deb>
						<pb_project-phase2_echeancier/>
						<pb_project-phase3_date_deb>01/08/2017</pb_project-phase3_date_deb>
						<pb_project-phase3_echeancier/>
						<pb_project-phase4_date_deb>10/11/2017</pb_project-phase4_date_deb>
						<pb_project-phase4_date_fin>17/11/2017</pb_project-phase4_date_fin>
						<pb_project-phase4_echeancier/>
						<pb_project-campagne>Budget Participatif 2015</pb_project-campagne>
						<pb_project-statut_eudo>Achevé</pb_project-statut_eudo>
						<pb_project-num_idea>2961</pb_project-num_idea>
						<pb_project-pseudos>Treize Accessible  (autre Collectif CQ et Associations)</pb_project-pseudos>
						<pb_project-title_idea>Mise en accessibilité des centres d'animation</pb_project-title_idea>
						<pb_project-statut_eudo>Achevé</pb_project-statut_eudo>
						<pb_project-step1>01/01/2017</pb_project-step1>
						<pb_project-step2>01/04/2017</pb_project-step2>
						<pb_project-step3>01/07/2017</pb_project-step3>
						<pb_project-step4>30/09/2017</pb_project-step4>
						<pb_project-step5>10/03/2018</pb_project-step5>
						<pb_project-handicap>non</pb_project-handicap>
					</pb_project>
				</document-xml-content>
				<document-number-comment>0</document-number-comment>
				<document-number-favorite>2</document-number-favorite>
				<document-number-follow>3</document-number-follow>
			</document>
	-->
		
	<!-- *********************************************************************************** -->
	<!-- * HEADER HEADER HEADER HEADER HEADER HEADER HEADER HEADER HEADER HEADER HEADER HE * -->
	<!-- * HEADER HEADER HEADER HEADER HEADER HEADER HEADER HEADER HEADER HEADER HEADER HE * -->
	<!-- *********************************************************************************** -->

	<xsl:template match="portlet">
	
		<xsl:variable name="device_class">
			<xsl:choose>
				<xsl:when test="string(display-on-small-device)='0'">hidden-phone</xsl:when>
				<xsl:when test="string(display-on-normal-device)='0'">hidden-tablet</xsl:when>
				<xsl:when test="string(display-on-large-device)='0'">hidden-desktop</xsl:when>
				<xsl:otherwise></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<!-- Displaying portlet only if documents available -->
		<xsl:if test="document-list-portlet/*">
		
			<div id="portlet-pgagn-focus" class="{$device_class}">
				<div class="focus-body">
					<p>Projects in the news !</p>
					<xsl:apply-templates select="document-list-portlet" />				
				</div>
			</div>
				
		</xsl:if>
		
	</xsl:template>

	<!-- *********************************************************************************** -->
	<!-- * FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE * -->
	<!-- * FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE FILE * -->
	<!-- *********************************************************************************** -->

	<xsl:template match="document-list-portlet">
		<div class="row row-eq-height">
			<xsl:apply-templates select="document" />
		</div>
	</xsl:template>

	<xsl:template match="document">
		<div class="col-md-4">
			<a class="focus-item">
				<xsl:attribute name="href">jsp/site/Portal.jsp?document_id=<xsl:value-of select="document-id"/>&amp;portlet_id=158</xsl:attribute>
				<xsl:apply-templates select="document-xml-content/pb_project" />
			</a>
		</div>
	</xsl:template>

	<xsl:template match="document/document-xml-content/pb_project">
	
		<!-- On identifie le code technique du theme pour la gestion de la couleur de fond et de l'image -->
		<xsl:variable name="theme">other</xsl:variable>

		<div class="focus-img">
			<xsl:choose>
				<xsl:when test="pb_project-image!=''">
					<xsl:value-of disable-output-escaping="yes" select="pb_project-image" />
				</xsl:when>
				<xsl:otherwise>
					<img><xsl:attribute name="src">images/local/skin/theme_<xsl:value-of select="$theme"/>.png</xsl:attribute></img>
				</xsl:otherwise>
			</xsl:choose>
		</div>
	
		<div>
			<xsl:attribute name="class">focus-theme bgcolor-theme-<xsl:value-of select="$theme"/></xsl:attribute>
			<xsl:value-of select="pb_project-thematique"/>
		</div>
		
		<div>
			<xsl:attribute name="class">bordered-4px-theme-<xsl:value-of select="$theme"/></xsl:attribute>
	
			<div class="focus-title">
				<xsl:value-of select="document-title"/>
			</div>
	
			<div class="focus-link btn btn-14rem btn-black-on-white">
				<xsl:attribute name="href">jsp/site/Portal.jsp?document_id=<xsl:value-of select="document-id"/>&amp;portlet_id=158</xsl:attribute>
				découvrir le projet
			</div>
			
		</div>

	</xsl:template>

</xsl:stylesheet>
