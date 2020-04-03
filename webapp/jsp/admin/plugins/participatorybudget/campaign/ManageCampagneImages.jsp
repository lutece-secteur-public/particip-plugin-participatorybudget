<jsp:useBean id="manageideationCampaignImage" scope="session" class="fr.paris.lutece.plugins.participatorybudget.web.campaign.CampaignImageJspBean" />
<% String strContent = manageideationCampaignImage.processController ( request , response ); %>

<%@ page errorPage="../../../ErrorPage.jsp" %>
<jsp:include page="../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../AdminFooter.jsp" %>
