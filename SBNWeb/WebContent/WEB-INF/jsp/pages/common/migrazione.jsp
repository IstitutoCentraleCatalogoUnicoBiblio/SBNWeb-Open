<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
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
			<html:form action="/migrazione.do" focus="userName"
				enctype="multipart/form-data">
				<table>
					<tr>
						<td>
							<h3>Migrazione catene di rinnovi da C/S a SBN Web</h3>
						</td>
						<td><html:submit property="methodMigrazione">
								<bean:message key="button.mig.rinnovi" bundle="messages" />
							</html:submit></td>
					</tr>
					<tr>
						<td>
							<h3>Ricalcolo massivo chiavi ordinamento authority soggetti
								(FIR)</h3>
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

					<tr>
						<td>
							<h3>Fix normalizzazione range segnature</h3>
						</td>
						<td><html:submit property="methodMigrazione">
								<bean:message key="button.mig.segnature" bundle="messages" />
							</html:submit></td>
					</tr>
					<tr>
						<td>
							<h3>Fix normalizzazione Documenti non SBN</h3>
						</td>
						<td><html:submit property="methodMigrazione">
								<bean:message key="button.mig.segnature.docnosbn"
									bundle="messages" />
							</html:submit></td>
					</tr>

					<tr>
						<td><h3>Batch Interrogazione massiva</h3>
							<html:file property="uploadImmagine" /> <html:submit
								property="methodMigrazione">
								<bean:message key="button.mig.carFileInterrMassiva"
									bundle="messages" />
							</html:submit></td>

						<td><html:submit property="methodMigrazione">
								<bean:message key="button.mig.prenBatchInterrMassiva"
									bundle="messages" />
							</html:submit></td>
					</tr>

					<!--
						<tr>
							<td>
								<h3>Test query collocazione (paginazione asincrona)</h3>
							</td>
							<td align="center"	>
								<html:submit property="methodMigrazione">
									<bean:message key="button.mig.pagination" bundle="messages" />
								</html:submit>
							</td>
						</tr>

					-->
					<tr>
						<td><h3>reload configurazione</h3></td>
						<td>
							<html:submit property="methodMigrazione">
								<bean:message key="button.pad.execute" bundle="messages" />
							</html:submit>
						</td>
					</tr>
					<tr>
						<td><h3>avvio forzato batch</h3></td>
						<td>
							<html:text property="idBatch" size="5" maxlength="5" />
							<html:submit property="methodMigrazione">
								<bean:message key="button.mig.force.batch.start" bundle="messages" />
							</html:submit>
						</td>
					</tr>
					<tr>
						<td><h3>stop forzato batch</h3></td>
						<td>
							<html:text property="idBatchStop" size="5" maxlength="5" />
							<html:submit property="methodMigrazione">
								<bean:message key="button.mig.force.batch.stop" bundle="messages" />
							</html:submit>
						</td>
					</tr>
					<tr>
						<td><h3>test mail server</h3></td>
						<td>
							<html:text property="email" size="25" />
							<html:submit property="methodMigrazione">
								<bean:message key="button.mig.test.mail" bundle="messages" />
							</html:submit>
							<html:submit property="methodMigrazione">
								<bean:message key="button.mig.test.sms" bundle="messages" />
							</html:submit>
						</td>
					</tr>
					<tr>
						<td><h3>Salvataggio file allineamento su server locale</h3></td>
						<td>
							<html:text property="idBatchAllinea" size="9" maxlength="9" />
							<html:submit property="methodMigrazione">
								<bean:message key="button.mig.salva.file.allinea" bundle="messages" />
							</html:submit>
						</td>
					</tr>
					<tr>
						<td><h3>Allineamento richieste al Server ILL</h3></td>
						<td>
							<html:submit property="methodMigrazione">
								<bean:message key="button.mig.allinea.ill" bundle="messages" />
							</html:submit>
						</td>
					</tr>
					<tr>
						<td><h3>Invio XML ILL</h3></td>
						<td>
							<p>Isil:<html:text property="isil" size="9" maxlength="9" /></p>
							<p>xml:<html:textarea property="ill_xml" rows="5" cols="80" /></p>
							<html:submit property="methodMigrazione">
								<bean:message key="button.mig.invia.ill.xml" bundle="messages" />
							</html:submit>
						</td>
					</tr>
				</table>


			</html:form>
		</div>
	</div>
</body>