<jsp:useBean id="voteParArrd" scope="session" class="fr.paris.lutece.plugins.participatorybudget.web.vote.VoteParArrandJspBean" />
<% String strContent = voteParArrd.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>

