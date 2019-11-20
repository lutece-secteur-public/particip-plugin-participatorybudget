<%@page import="fr.paris.lutece.portal.web.LocalVariables" trimDirectiveWhitespaces="true"%>
<jsp:useBean id="myInfosXPage" scope="session" class="fr.paris.lutece.plugins.budgetparticipatif.web.MyVoteXPage" />
<%
	LocalVariables.setLocal( config, request, response );
%>
<%= myInfosXPage.doSendAvis( request ) %>