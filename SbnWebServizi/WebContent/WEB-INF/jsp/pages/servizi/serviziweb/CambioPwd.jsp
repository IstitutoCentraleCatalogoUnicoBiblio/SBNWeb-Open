<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<head>
<title>Servizi SBN Web</title>

<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/styles/login.css" paramScope="request" paramName="url" />' />
</head>
<div id="header"></div>

<div id="data"><c:out value="${cambioPwdServiziWebForm.ambiente}">
</c:out> - <bean:message key="servizi.utenti.utenteConn"
	bundle="serviziWebLabels" /> <c:out
	value="${cambioPwdServiziWebForm.utenteCon}">
</c:out>


<div id="divMessaggio">
	<sbn:errors bundle="serviziWebMessages" />
</div>

<hr>

<div id="divForm"><html:form action="/serviziweb/cambioPwd.do" focus="oldPassword">
	<strong><bean:message key="servizi.utenti.cambioPwd" bundle="serviziWebLabels" /></strong>
	<hr/>
	<span>
		<bean:message key="servizi.password.rules" bundle="serviziWebLabels" />
	</span>
	<table>
		<tr>
			<th align="right"><label for="password">Vecchia Password</label>:</th>
			<td><html:password styleId="password" property="oldPassword"
				size="18" maxlength="30" redisplay="false" /></td>
		</tr>
		<tr>
		</tr>
		<tr>
			<th align="right"><label for="password">Nuova Password</label>:</th>
			<td><html:password styleId="password" property="newPassword"
				size="18" maxlength="30" redisplay="false" /></td>
		</tr>
		<tr>
			<th align="right"><label for="password">Conferma
			Password</label>:</th>
			<td><html:password styleId="password" property="rePassword"
				size="18" maxlength="30" redisplay="false" /></td>
		</tr>
		<tr>
			<td align="center" colspan="2"><html:submit styleClass="submit"
				property="paramCambioPwd">
				<bean:message key="button.newpassword" bundle="serviziWebLabels" />
			</html:submit> <html:submit styleClass="submit" property="paramCambioPwd">
				<bean:message key="button.logout" bundle="serviziWebLabels" />
			</html:submit></td>
		</tr>
	</table>
</html:form></div>
</div>
