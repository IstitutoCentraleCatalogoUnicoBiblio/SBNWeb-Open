<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<head>
<title>SBN Web</title>

<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/styles/login.css" paramScope="request" paramName="url" />' />
</head>

<body>
	<div id="header"></div>
	<div id="data">
		<div>
			<logic:present name="POLO_NAME" scope="session">
				<p class="IntestazioneBiblioteca" id="polo">
					<bs:write scope="session" name="POLO_NAME" />
				</p>
			</logic:present>
		</div>
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<html:form action="/migrazioneSoggetti.do" focus="userName"
				enctype="multipart/form-data">
				<table>
					<tr>
						<td>
							<h3>Ricalcolo massivo chiavi ordinamento authority soggetti</h3>
						</td>
						<td>
							<table style="border: 1px; border-style: dotted;">
								<tr>
									<td colspan="2">cid&nbsp;&gt;=&nbsp;<html:text
											property="parametri.fromCid" size="10" maxlength="10" />
										cid&nbsp;&lt;=&nbsp;<html:text property="parametri.toCid"
											size="10" maxlength="10" />
									</td>
								</tr>
								<tr>
									<td align="center"><html:file property="fileSoggetti"
											size="30" /></td>
									<td class="etichetta"><html:submit
											property="methodMigrazione">
											<bean:message key="button.caricafile" bundle="importaLabels" />
										</html:submit></td>
								</tr>
								<tr>
									<td align="center" colspan="2"><html:submit
											property="methodMigrazione">
											<bean:message key="button.mig.soggetti" bundle="messages" />
										</html:submit></td>
								</tr>
							</table>
					</tr>
				</table>
			</html:form>
		</div>
	</div>
</body>