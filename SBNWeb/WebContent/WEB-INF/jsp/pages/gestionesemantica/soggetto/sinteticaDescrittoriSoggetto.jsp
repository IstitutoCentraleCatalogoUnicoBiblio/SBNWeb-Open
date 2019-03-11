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
		action="/gestionesemantica/soggetto/SinteticaDescrittoriSoggetto.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<p class="etichetta">
				Ricerca eseguita sulla base dati Locale
			</p>
			<table class="sintetica">
				<tr bgcolor="#DDE8F0">
					<th width="70%" class="etichetta" align="left" scope="col">
						<bean:message key="sintetica.descrittore"
							bundle="gestioneSemanticaLabels" />
					</th>
					<th width="15%" class="etichetta" align="center" scope="col">
						<bean:message key="sintetica.soggetti"
							bundle="gestioneSemanticaLabels" />
					</th>
					<th width="15%" class="etichetta" align="center" scope="col">
						<bean:message key="sintetica.descrittori"
							bundle="gestioneSemanticaLabels" />
					</th>
				</tr>
				<c:set var="riga" value="-1" />
				<c:if test="${SinteticaDescrittoriSoggettoForm.enableDescrittori}">
					<sbn:rowcolor var="color" index="riga" auto="true"/>
					<tr bgcolor="${color}">
						<td width="70%">
							<bean-struts:write name="SinteticaDescrittoriSoggettoForm"
								property="descrittoriSogg" />
						</td>
						<td align="center">
							<logic:messagesPresent message="true" property="button.soggetti0">
								<html:messages id="msg1" message="true"
									property="button.soggetti0" bundle="gestioneSemanticaLabels">
									<html:submit property="methodSintButtonDes0" styleClass="w4em"
										disabled="${msg1 eq '0'}">
										<bean-struts:write name="msg1" />
									</html:submit>
								</html:messages>
							</logic:messagesPresent>
						</td>
						<td align="center">
							<logic:messagesPresent message="true"
								property="button.descrittori0">
								<html:messages id="msg2" message="true"
									property="button.descrittori0" bundle="gestioneSemanticaLabels">
									<html:submit property="methodSintButtonDes4" styleClass="w4em"
										disabled="${msg2 eq '0'}">
										<bean-struts:write name="msg2" />
									</html:submit>
								</html:messages>
							</logic:messagesPresent>
						</td>

					</tr>
				</c:if>

				<c:if test="${SinteticaDescrittoriSoggettoForm.enableDescrittori1}">
					<sbn:rowcolor var="color" index="riga" auto="true"/>
					<tr bgcolor="${color}">
						<td width="70%">
							<bean-struts:write name="SinteticaDescrittoriSoggettoForm"
								property="descrittoriSogg1" />
						</td>
						<td align="center">
							<logic:messagesPresent message="true" property="button.soggetti1">
								<html:messages id="msg1" message="true"
									property="button.soggetti1" bundle="gestioneSemanticaLabels">
									<html:submit property="methodSintButtonDes1" styleClass="w4em"
										disabled="${msg1 eq '0'}">
										<bean-struts:write name="msg1" />
									</html:submit>
								</html:messages>
							</logic:messagesPresent>
						</td>
						<td align="center">
							<logic:messagesPresent message="true"
								property="button.descrittori1">
								<html:messages id="msg2" message="true"
									property="button.descrittori1" bundle="gestioneSemanticaLabels">
									<html:submit property="methodSintButtonDes5" styleClass="w4em"
										disabled="${msg2 eq '0'}">
										<bean-struts:write name="msg2" />
									</html:submit>
								</html:messages>
							</logic:messagesPresent>
						</td>

					</tr>
				</c:if>
				<c:if test="${SinteticaDescrittoriSoggettoForm.enableDescrittori2}">
					<sbn:rowcolor var="color" index="riga" auto="true"/>
					<tr bgcolor="${color}">
						<td width="70%">
							<bean-struts:write name="SinteticaDescrittoriSoggettoForm"
								property="descrittoriSogg2" />
						</td>

						<td align="center">
							<logic:messagesPresent message="true" property="button.soggetti2">
								<html:messages id="msg1" message="true"
									property="button.soggetti2" bundle="gestioneSemanticaLabels">
									<html:submit property="methodSintButtonDes2" styleClass="w4em"
										disabled="${msg1 eq '0'}">
										<bean-struts:write name="msg1" />
									</html:submit>
								</html:messages>
							</logic:messagesPresent>
						</td>
						<td align="center">
							<logic:messagesPresent message="true"
								property="button.descrittori2">
								<html:messages id="msg2" message="true"
									property="button.descrittori2" bundle="gestioneSemanticaLabels">
									<html:submit property="methodSintButtonDes6" styleClass="w4em"
										disabled="${msg2 eq '0'}">
										<bean-struts:write name="msg2" />
									</html:submit>
								</html:messages>
							</logic:messagesPresent>
						</td>

					</tr>
				</c:if>

				<c:if test="${SinteticaDescrittoriSoggettoForm.enableDescrittori3}">
					<sbn:rowcolor var="color" index="riga" auto="true"/>
					<tr bgcolor="${color}">
						<td width="70%">
							<bean-struts:write name="SinteticaDescrittoriSoggettoForm"
								property="descrittoriSogg3" />
						</td>
						<td align="center">
							<logic:messagesPresent message="true" property="button.soggetti3">
								<html:messages id="msg1" message="true"
									property="button.soggetti3" bundle="gestioneSemanticaLabels">
									<html:submit property="methodSintButtonDes3" styleClass="w4em"
										disabled="${msg1 eq '0'}">
										<bean-struts:write name="msg1" />
									</html:submit>
								</html:messages>
							</logic:messagesPresent>
						</td>
						<td align="center">
							<logic:messagesPresent message="true"
								property="button.descrittori3">
								<html:messages id="msg2" message="true"
									property="button.descrittori3" bundle="gestioneSemanticaLabels">
									<html:submit property="methodSintButtonDes7" styleClass="w4em"
										disabled="${msg2 eq '0'}">
										<bean-struts:write name="msg2" />
									</html:submit>
								</html:messages>
							</logic:messagesPresent>
						</td>

					</tr>
				</c:if>
				<sbn:rowcolor var="color" index="riga" auto="true"/>
				<tr bgcolor="${color}">
					<td class="etichetta" scope="col">
						<bean:message key="sintetica.soggettiTutti"
							bundle="gestioneSemanticaLabels" />
					</td>
					<td align="center">
						<logic:messagesPresent message="true"
							property="button.soggettiTutti">
							<html:messages id="msg1" message="true"
								property="button.soggettiTutti" bundle="gestioneSemanticaLabels">
								<html:submit property="methodSintButtonDes8" styleClass="w4em"
									disabled="${msg1 eq '0'}">
									<bean-struts:write name="msg1" />
								</html:submit>
							</html:messages>
						</logic:messagesPresent>
					</td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center">
						<html:submit property="methodSinteticaDescrittori">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
