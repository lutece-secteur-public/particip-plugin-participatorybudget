<jsp:useBean id="manageideationCampagneTheme" scope="session" class="fr.paris.lutece.plugins.campagnebp.web.CampagneThemeJspBean" />
<% String strContent = manageideationCampagneTheme.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
