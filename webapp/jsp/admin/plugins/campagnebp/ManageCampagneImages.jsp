<jsp:useBean id="manageideationCampagneImage" scope="session" class="fr.paris.lutece.plugins.campagnebp.web.CampagneImageJspBean" />
<% String strContent = manageideationCampagneImage.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
