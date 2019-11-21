<jsp:useBean id="manageCampagne" scope="session" class="fr.paris.lutece.plugins.participatorybudget.web.campaign.CampagneJspBean" />
<% String strContent = manageCampagne.processController ( request , response ); %>

<%@ page errorPage="../../../ErrorPage.jsp" %>
<jsp:include page="../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../AdminFooter.jsp" %>
