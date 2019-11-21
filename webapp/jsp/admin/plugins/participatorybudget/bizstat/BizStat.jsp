<jsp:useBean id="bizStat " scope="session" class="fr.paris.lutece.plugins.participatorybudget.web.bizstat.BizStatJspBean" />

<% String strContent = bizStat.processController ( request , response ); %>

<%@ page errorPage="../../../ErrorPage.jsp" %>
<jsp:include page="../../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../../AdminFooter.jsp" %>

