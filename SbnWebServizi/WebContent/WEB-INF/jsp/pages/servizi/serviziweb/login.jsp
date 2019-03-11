<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>


<html:xhtml />

<head>
<title>Servizi SBN Web</title>

<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/styles/login.css" paramScope="request" paramName="url" />' />
</head>

<body>
	<div id="header"></div>
	<div id="data">
		<div>
			<l:present name="POLO_NAME" scope="session">
				<p class="IntestazioneBiblioteca" id="polo">
					<bs:write scope="session" name="POLO_NAME" />
				</p>
			</l:present>
		</div>
		<div id="divForm">
			<html:form action="/login.do" focus="userName">
				<sbn:checkAttivita idControllo="DATI_INVENTARIO">
					<table style="margin-top: 0" border="0">
						<tr>
							<td width="100px" class="etichetta" align="left"><bean:message
									key="servizi.richiestaOpacLogin" bundle="serviziWebLabels" /></td>
						</tr>
						<tr>
							<td width="100px" class="etichetta" align="left"><em><strong><bean:message
										key="servizi.utenti.headerBiblioteca" bundle="serviziWebLabels" /></strong></em> : <bs:write
								name="loginServiziWebForm" property="richiesta.denBibOpac"
								filter="true" /></td>
						</tr>
						<tr>
							<td width="100px" class="etichetta" align="left"><em><strong><bean:message
											key="servizi.autoreLogin" bundle="serviziWebLabels" /></strong></em> : <bs:write
									name="loginServiziWebForm" property="richiesta.autore"
									filter="true" /></td>
						</tr>

						<tr>
							<td width="100px" class="etichetta" align="left"><em><strong><bean:message
											key="servizi.titoloLogin" bundle="serviziWebLabels" /></strong></em> : <bs:write
									name="loginServiziWebForm" property="richiesta.titolo"
									filter="true" /> <bs:write name="loginServiziWebForm"
									property="richiesta.anno" filter="true" /></td>
						</tr>

					</table>
				</sbn:checkAttivita>
				<sbn:checkAttivita idControllo="NOT_LOGGED">
					<table style="margin-top: 0" border="2">
						<tr>
							<td width="100px" class="etichetta" align="left"><span>
									<!-- <bean:message key="servizi.welcome" bundle="serviziWebLabels" />&nbsp;
						<html:link page="/login.do?AUTOREG=Y">
							<bean:message key="button.autoregistrazione" bundle="messages" />
						</html:link>. --> <bs:write name="loginServiziWebForm"
										property="welcome" filter="false" />
							</span></td>
						</tr>
					</table>
				</sbn:checkAttivita>

				<div id="divMessaggio">
					<sbn:errors bundle="serviziWebMessages" />
				</div>

				<table style="margin-top: 10" cellspacing="0" width="100%"
					border="0">

					<sbn:checkAttivita idControllo="NOT_LOGGED">
						<tr>
							<th align="right"><label for="userName"> <bean:message
										key="servizi.utenti.utenteLogin" bundle="serviziWebLabels" />
							</label> :</th>
							<td><html:text styleId="userName" property="userName"
									size="25" maxlength="25" tabindex="true" /></td>
						</tr>

						<tr>
							<th align="right"><label for="password"> <bean:message
										key="servizi.utenti.passwordLogin" bundle="serviziWebLabels" />
							</label> :</th>
							<td><html:password styleId="password" property="password"
									size="18" maxlength="30" redisplay="false" /></td>
						</tr>
					</sbn:checkAttivita>
					<tr>
						<td align="right">
							<sbn:checkAttivita idControllo="ISCRIZIONE">
								<html:submit
									styleClass="submit;margin-top:0;background-color=beige;border-color=green"
									property="paramLogin">
									<bean:message key="servizi.bottone.opac.iscrizione"
										bundle="serviziWebLabels" />
								</html:submit>
								<html:submit
									styleClass="submit;margin-top:0;background-color=beige;border-color=green"
									property="paramLogin">
									<bean:message key="button.home" bundle="messages" />
								</html:submit>
							</sbn:checkAttivita>
							<sbn:checkAttivita idControllo="NOT_LOGGED">
								<html:submit
									styleClass="submit;margin-top:0;background-color=beige;border-color=green"
									property="paramLogin">
									<bean:message key="button.login" bundle="messages" />
								</html:submit>
							</sbn:checkAttivita>
						</td>

						<td>
						<sbn:checkAttivita idControllo="NOT_LOGGED">
						<html:submit
								styleClass="submit;margin-top:0;background-color=beige;border-color=green"
								property="paramLogin">
								<bean:message key="button.home" bundle="messages" />
							</html:submit>
						</sbn:checkAttivita>
						</td>
					</tr>

				</table>
				<sbn:checkAttivita idControllo="NOT_LOGGED">
					<table style="margin-top: 10px" cellspacing="0" width="100%"
						border="0" style="">
						<tr>
							<th><html:submit
									styleClass="submit;margin-top:0;background-color=beige;border-color=green"
									property="paramLogin">
									<bean:message key="button.cambiopassword" bundle="messages" />
								</html:submit> <html:submit
									styleClass="submit;margin-top:0;background-color=beige;border-color=green"
									property="paramLogin">
									<bean:message key="button.recuperopassword" bundle="messages" />
								</html:submit> <%--
					<html:submit styleClass="submit;margin-top:0;background-color=beige;border-color=green" property="paramLogin">
				 		<bean:message key="button.autoregistrazione" bundle="messages" />
					</html:submit>--%></th>
						</tr>
					</table>
				</sbn:checkAttivita>

				<sbn:checkAttivita idControllo="SELEZIONE_BIB_RICHIESTA_ILL">
					<table class="ill_warning">
						<tr>
							<td><h2><bean:message key="servizi.ill.non.iscritto.bib" bundle="serviziWebLabels" /></h2></td>
						</tr>
						<tr>
							<td>puoi:</td>
						</tr>
						<tr>
							<td>
								<c:if test="${loginServiziWebForm.iscrizione}">
									<ul>
										<li>
											<bean:message key="servizi.ill.iscrizione.bib.doc" bundle="serviziWebLabels" />
											<html:submit styleClass="submit;margin-top:0;background-color=beige;border-color=green" property="paramLogin">
												<bean:message key="servizi.bottone.opac.iscrizione" bundle="serviziWebLabels" />
											</html:submit>
										</li>
									</ul>
									<p>oppure:</p>
								</c:if>
								<ul>
									<li>
										<bean:message key="servizi.ill.seleziona.bib.richiedente" bundle="serviziWebLabels" />
										<html:select styleClass="testoNormale" property="bib.cod_bib">
											<html:optionsCollection property="listaBiblio" value="cod_bib" label="nom_biblioteca" />
										</html:select>
										<html:submit styleClass="submit;margin-top:0;background-color=beige;border-color=green" property="paramLogin">
											<bean:message key="servizi.bottone.scegli" bundle="serviziWebLabels" />
										</html:submit>
									</li>
								</ul>
							</td>
						</tr>
						<tr>
							<td align="center">
								<html:submit styleClass="submit;margin-top:0;background-color=beige;border-color=green" property="paramLogin">
									<bean:message key="button.home" bundle="messages" />
								</html:submit>
							</td>
						</tr>
					</table>
				</sbn:checkAttivita>
				<sbn:checkAttivita idControllo="LOGGED">
					<table>
						<tr>
							<td align="center">
								<html:submit
									styleClass="submit;margin-top:0;background-color=beige;border-color=green"
									property="paramLogin">
									<bean:message key="button.continua" bundle="messages" />
								</html:submit>
							</td>
						</tr>
					</table>
				</sbn:checkAttivita>
			</html:form>
		</div>

	</div>
</body>