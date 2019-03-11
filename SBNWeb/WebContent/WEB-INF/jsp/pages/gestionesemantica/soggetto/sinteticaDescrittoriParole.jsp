<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<sbn:navform
		action="/gestionesemantica/soggetto/SinteticaDescrittoriParole.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<table width="100%" border="0">
				<tr>
					<td class="etichetta">
						Ricerca eseguita sulla base dati Locale
					</td>
				</tr>
			</table>
			<c:set var="riga" value="-1" />
			<table class="sintetica">
				<tr bgcolor="#dde8f0">
					<th width="85%" class="etichetta" align="left" scope="col">
						<bean:message key="sintetica.parola"
							bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" align="center" scope="col">
						<bean:message key="sintetica.descrittori"
							bundle="gestioneSemanticaLabels" />
					</th>
				</tr>
				<c:if test="${SinteticaDescrittoriParoleForm.enableParole}">
					<sbn:rowcolor var="color" index="riga" auto="true" />
					<tr bgcolor="${color}">
						<td width="85%">
							<bean-struts:write name="SinteticaDescrittoriParoleForm"
								property="parole" />
						</td>


						<td align="center">
							<logic:messagesPresent message="true" property="button.parola0">
								<html:messages id="msg1" message="true"
									property="button.parola0" bundle="gestioneSemanticaLabels">
									<html:submit property="methodSintButtonParole0"
										disabled="${msg1 eq '0'}">
										<bean-struts:write name="msg1" />
									</html:submit>
								</html:messages>
							</logic:messagesPresent>
						</td>

					</tr>
				</c:if>
				<c:if test="${SinteticaDescrittoriParoleForm.enableParole1}">
					<sbn:rowcolor var="color" index="riga" auto="true" />
					<tr bgcolor="${color}">
						<td width="85%">
							<bean-struts:write name="SinteticaDescrittoriParoleForm"
								property="parole1" />
						</td>

						<td align="center">
							<logic:messagesPresent message="true" property="button.parola1">
								<html:messages id="msg1" message="true"
									property="button.parola1" bundle="gestioneSemanticaLabels">
									<html:submit property="methodSintButtonParole1"
										disabled="${msg1 eq '0'}">
										<bean-struts:write name="msg1" />
									</html:submit>
								</html:messages>
							</logic:messagesPresent>
						</td>

					</tr>
				</c:if>
				<c:if test="${SinteticaDescrittoriParoleForm.enableParole2}">
					<sbn:rowcolor var="color" index="riga" auto="true" />
					<tr bgcolor="${color}">
						<td width="85%">
							<bean-struts:write name="SinteticaDescrittoriParoleForm"
								property="parole2" />
						</td>

						<td align="center">
							<logic:messagesPresent message="true" property="button.parola2">
								<html:messages id="msg1" message="true"
									property="button.parola2" bundle="gestioneSemanticaLabels">
									<html:submit property="methodSintButtonParole2"
										disabled="${msg1 eq '0'}">
										<bean-struts:write name="msg1" />
									</html:submit>
								</html:messages>
							</logic:messagesPresent>
						</td>

					</tr>
				</c:if>
				<c:if test="${SinteticaDescrittoriParoleForm.enableParole3}">
					<sbn:rowcolor var="color" index="riga" auto="true" />
					<tr bgcolor="${color}">
						<td width="85%">
							<bean-struts:write name="SinteticaDescrittoriParoleForm"
								property="parole3" />
						</td>

						<td align="center">
							<logic:messagesPresent message="true" property="button.parola3">
								<html:messages id="msg1" message="true"
									property="button.parola3" bundle="gestioneSemanticaLabels">
									<html:submit property="methodSintButtonParole3"
										disabled="${msg1 eq '0'}">
										<bean-struts:write name="msg1" />
									</html:submit>
								</html:messages>
							</logic:messagesPresent>
						</td>

					</tr>
				</c:if>
				<sbn:rowcolor var="color" index="riga" auto="true" />
				<tr bgcolor="${color}">
					<td class="etichetta" scope="col">
						<bean:message key="sintetica.paroleTutte"
							bundle="gestioneSemanticaLabels" />
					</td>
					<td align="center">
						<logic:messagesPresent message="true"
							property="button.paroleTutte">
							<html:messages id="msg1" message="true"
								property="button.paroleTutte" bundle="gestioneSemanticaLabels">
								<html:submit property="methodSintButtonParole4"
									disabled="${msg1 eq '0'}">
									<bean-struts:write name="msg1" />
								</html:submit>
							</html:messages>
						</logic:messagesPresent>
					</td>
				</tr>
			</table>
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center">
						<html:submit property="methodSinteticaParole">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
