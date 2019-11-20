<jsp:useBean id="bizStat " scope="session" class="fr.paris.lutece.plugins.budgetparticipatif.web.BizStatJspBean" />

<% String strContent = bizStat.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>

