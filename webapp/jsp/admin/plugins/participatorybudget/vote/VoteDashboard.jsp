<jsp:useBean id="voteDashboard " scope="session" class="fr.paris.lutece.plugins.participatorybudget.web.vote.VoteDashboardJspBean" />

<% String strContent = voteDashboard.processController ( request , response ); %>

<%@ page errorPage="../../../ErrorPage.jsp" %>
<jsp:include page="../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../AdminFooter.jsp" %>

