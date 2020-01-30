<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java">

	<xsl:param name="site-path" select="site-path" />

	<xsl:variable name="portlet-id" select="portlet/portlet-id" />

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
					<p>News about projects !</p>
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
			<xsl:value-of select="pb_project-theme"/>
		</div>
		
		<div>
			<xsl:attribute name="class">bordered-4px-theme-<xsl:value-of select="$theme"/></xsl:attribute>
	
			<div class="focus-title">
				<xsl:value-of select="document-title"/>
			</div>
	
			<div class="focus-link btn btn-14rem btn-black-on-white">
				<xsl:attribute name="href">jsp/site/Portal.jsp?document_id=<xsl:value-of select="document-id"/>&amp;portlet_id=158</xsl:attribute>
				discover project
			</div>
			
		</div>

	</xsl:template>

</xsl:stylesheet>
