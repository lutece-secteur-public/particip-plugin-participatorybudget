<jsp:useBean id="manageideationCampaignArea" scope="session" class="fr.paris.lutece.plugins.participatorybudget.web.campaign.CampaignAreaJspBean" />
<% String strContent = manageideationCampaignArea.processController ( request , response ); %>

<%@ page errorPage="../../../ErrorPage.jsp" %>
<jsp:include page="../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../AdminFooter.jsp" %>
