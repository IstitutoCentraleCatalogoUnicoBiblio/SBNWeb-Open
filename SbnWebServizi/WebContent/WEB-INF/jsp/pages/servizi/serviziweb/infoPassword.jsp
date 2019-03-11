<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<sbn:checkAttivita idControllo="PASSWORD">
	<br/>
	<div>
		<hr/>
		<strong><bean:message key="servizi.password.conferma" bundle="serviziWebLabels" /></strong>&nbsp;
		<html:text property="infoPwd.userName" disabled="true" style="display: none;" />
		<html:password property="infoPwd.password" size="18" maxlength="30" />
		<hr/>
	</div>
	<br/>
</sbn:checkAttivita>
