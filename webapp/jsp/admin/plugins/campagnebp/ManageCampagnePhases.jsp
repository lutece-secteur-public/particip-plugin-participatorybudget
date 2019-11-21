<jsp:useBean id="manageideationCampagnePhase" scope="session" class="fr.paris.lutece.plugins.campagnebp.web.CampagnePhaseJspBean" />
<% String strContent = manageideationCampagnePhase.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
