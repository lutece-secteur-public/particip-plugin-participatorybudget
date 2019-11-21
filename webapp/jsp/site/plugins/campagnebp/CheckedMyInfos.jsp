<%@page import="fr.paris.lutece.portal.web.LocalVariables" trimDirectiveWhitespaces="true"%>
<jsp:useBean id="myInfosXPage" scope="session" class="fr.paris.lutece.plugins.campagnebp.web.MyInfosXPage" />
<%
	LocalVariables.setLocal( config, request, response );
%>
<%= myInfosXPage.getCheckedMyInfosAfterLogin(request) %>