<jsp:useBean id="manageCampaign" scope="session" class="fr.paris.lutece.plugins.participatorybudget.web.campaign.CampaignJspBean" />
<% String strContent = manageCampaign.processController ( request , response ); %>

<%@ page errorPage="../../../ErrorPage.jsp" %>
<jsp:include page="../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../AdminFooter.jsp" %>
