<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="page1" %>
<page1:page>
	<display:table  name="sessionScope.showCodeModel.message" export="false" pagesize="10" requestURI="/SbnMarcWeb/jms/showCode.do?methodShow=next" requestURIcontext="false">
	    <display:column property="JMSMessageID" title="ID" href="/SbnMarcWeb/jms/showCode.do?methodShow=esamina" paramId="ID"/>
	    <display:column property="JMSCorrelationID" title="Blocco" />
	    <display:setProperty name="paging.banner.full">
      <![CDATA[
		<span class="pagelinks">
        [<a href="{1}">First</a>/<a href="{2}">Back</a>]
        {0}
        [<a href="{3}">Forw</a>/<a href="{4}">Last</a>]
      ]]>
    </display:setProperty>
  	</display:table>
	<html:link action="/jms/showCode?methodShow=elimina">elimina</html:link>
</page1:page>

