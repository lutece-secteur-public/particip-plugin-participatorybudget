<jsp:useBean id="manageCampaignChrono" scope="session" class="fr.paris.lutece.plugins.participatorybudget.web.campaign.CampaignChronoJspBean" />
<% String strContent = manageCampaignChrono.processController ( request , response ); %>

<%@ page errorPage="../../../ErrorPage.jsp" %>
<jsp:include page="../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../AdminFooter.jsp" %>
