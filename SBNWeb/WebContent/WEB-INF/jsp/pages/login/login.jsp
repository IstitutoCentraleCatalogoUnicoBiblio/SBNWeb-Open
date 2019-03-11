<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<head>
	<title>SBN Web</title>

	<link rel="stylesheet" type="text/css"
		href='<html:rewrite page="/styles/login.css" paramScope="request" paramName="url" />' />
</head>

<body>
<div id="header"></div>
<div id="data">
<div><logic:present name="POLO_NAME" scope="session">
	<p class="IntestazioneBiblioteca" id="polo"><bs:write scope="session" name="POLO_NAME" /></p>
</logic:present></div>
 <div id="divForm">
	<html:form action="/login.do" focus="userName">
		<table>
			<tr>
				<td colspan="2" align="center">
					<sbn:errors />
				</td>
			</tr>

			<tr>
				<th align="right">
					<label for="userName">
						Utente
					</label>
					:
				</th>
				<td>
					<html:text styleId="userName" property="userName" size="18"
						maxlength="6" />
				</td>
			</tr>

			<tr>
				<th align="right">
					<label for="password">
						Password
					</label>
					:
				</th>
				<td>
					<html:password styleId="password" property="password" size="18"
						maxlength="30" redisplay="false" />
				</td>
			</tr>
			<tr>
				<td>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
					<html:submit styleClass="submit;margin-top:0;background-color=beige;border-color=green" property="methodLogin">
						<bean:message key="button.login" bundle="messages" />
					</html:submit>
					<html:reset styleClass="submit;margin-top:0;background-color=beige;border-color=green" />
				</td>
			</tr>
		</table>
	</html:form>
	<div class="build">
		<table border="0" class="build">
			<logic:present name="SBNMARC_BUILD_TIME" scope="session">
				<tr>
					<td>&nbsp;sbnmarc:</td><td><bs:write scope="session" name="SBNMARC_BUILD_TIME" /></td>
				</tr>
			</logic:present>
			<logic:present name="BUILD_TIME" scope="session">
				<tr>
					<td>&nbsp;sbnweb:</td><td><bs:write scope="session" name="BUILD_TIME" /></td>
				</tr>
		</logic:present>
		</table>
	</div>
</div>
</div>
</body>