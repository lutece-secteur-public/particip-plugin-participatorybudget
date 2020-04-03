<jsp:useBean id="manageideationCampaignPhase" scope="session" class="fr.paris.lutece.plugins.participatorybudget.web.campaign.CampaignPhaseJspBean" />
<% String strContent = manageideationCampaignPhase.processController ( request , response ); %>

<%@ page errorPage="../../../ErrorPage.jsp" %>
<jsp:include page="../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../AdminFooter.jsp" %>
