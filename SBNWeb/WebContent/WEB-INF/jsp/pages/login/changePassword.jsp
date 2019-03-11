<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<head>
<title>SBN Web</title>

<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/styles/login.css" paramScope="request" paramName="url" />' />
</head>

<div id="header"></div>
<div id="data"><html:form action="/changePassword.do"
	focus="oldPassword">
	<table align="center">
		<tr>
			<td colspan="2" align="center"><sbn:errors /></td>
		</tr>

		<tr>
			<th align="right"><label for="password">Vecchia Password</label>:</th>
			<td><html:password styleId="password" property="oldPassword"
				size="18" maxlength="30" redisplay="false" /></td>
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
				property="methodChange">
				<bean:message key="button.newpassword" bundle="messages" />
			</html:submit> <html:reset styleClass="submit" /> <html:submit styleClass="submit"
				property="methodChange">
				<bean:message key="button.logout" bundle="messages" />
			</html:submit></td>
		</tr>
	</table>
</html:form></div>
