<%@page import="fr.paris.lutece.portal.web.LocalVariables" trimDirectiveWhitespaces="true"%>
<jsp:useBean id="myVotesXPage" scope="session" class="fr.paris.lutece.plugins.participatorybudget.web.vote.MyVoteXPage" />
<%
	LocalVariables.setLocal( config, request, response );
%>
<%= myVotesXPage.doVote( request ) %>