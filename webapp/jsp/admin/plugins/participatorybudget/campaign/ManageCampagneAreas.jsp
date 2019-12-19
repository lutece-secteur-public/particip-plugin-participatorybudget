<jsp:useBean id="manageideationCampagneArea" scope="session" class="fr.paris.lutece.plugins.participatorybudget.web.campaign.CampagneAreaJspBean" />
<% String strContent = manageideationCampagneArea.processController ( request , response ); %>

<%@ page errorPage="../../../ErrorPage.jsp" %>
<jsp:include page="../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../AdminFooter.jsp" %>
