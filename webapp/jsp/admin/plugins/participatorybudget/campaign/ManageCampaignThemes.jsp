<jsp:useBean id="manageideationCampaignTheme" scope="session" class="fr.paris.lutece.plugins.participatorybudget.web.campaign.CampaignThemeJspBean" />
<% String strContent = manageideationCampaignTheme.processController ( request , response ); %>

<%@ page errorPage="../../../ErrorPage.jsp" %>
<jsp:include page="../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../AdminFooter.jsp" %>
