<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout" %>

<html:xhtml/>

<layout:page>
<sbn:navform action="/documentofisico/serieInventariali/serieInventarialeModifica.do">
	<jsp:include page="/WEB-INF/jsp/subpages/documentofisico/serieInventariali/serieInventarialeGestione.jsp" />
</sbn:navform>
</layout:page>
