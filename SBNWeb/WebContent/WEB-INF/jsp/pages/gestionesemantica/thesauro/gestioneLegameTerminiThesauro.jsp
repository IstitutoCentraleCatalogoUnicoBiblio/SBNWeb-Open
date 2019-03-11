<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform
		action="/gestionesemantica/thesauro/GestioneLegameTermini.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<!-- Intestazione -->
			<BR>
			<table width="100%" border="0">
				<tr>
					<td class="etichetta">
						Base dati in aggiornamento Locale
					</td>
				</tr>
			</table>
			<table class="sintetica">
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						&nbsp;
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="sintetica.did" bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="gestionesemantica.thesauro.thesauro"
							bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="sintetica.headerStato"
							bundle="gestioneSemanticaLabels" />
					</th>

					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="gestione.formaNome"
							bundle="gestioneSemanticaLabels" />
					</th>

					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="sintetica.termine"
							bundle="gestioneSemanticaLabels" />
					</th>

				</tr>
				<bean-struts:define id="color" value="#FEF1E2" />
				<logic:iterate id="listaTermini" property="termini"
					name="GestioneLegameTerminiForm" indexId="progr">
					<sbn:rowcolor var="color" index="progr" />
					<tr bgcolor="${color}">
						<td class="testoBold" align="right">
							<c:choose>
								<c:when test="${progr eq 0}">
									<bean:message key="gestionesemantica.idPartenza" bundle="gestioneSemanticaLabels" />
								</c:when>
								<c:otherwise>
									<bean:message key="gestionesemantica.idArrivo" bundle="gestioneSemanticaLabels" />
								</c:otherwise>
							</c:choose>
						</td>
						<td >
							<bean-struts:write name="listaTermini" property="did" />
						</td>
						<td >
							<bean-struts:write name="listaTermini" property="codThesauro" />
						</td>
						<td >
							<bean-struts:write name="listaTermini" property="livAut" />
						</td>
						<td >
							<bean-struts:write name="listaTermini" property="formaNome" />
						</td>
						<td >
							<bean-struts:write name="listaTermini" property="testo" />
						</td>
					</tr>
				</logic:iterate>
			</table>
			<br>
			<table>
				<tr>
					<td class="etichetta" scope="col">
						<table class="myTable" cellpadding="0" cellspacing="0"
							width="100%">
							<tr>
								<td class="myHeader">
									<bean:message key="inserisci.note"
										bundle="gestioneSemanticaLabels" />
								</td>
							</tr>
							<tr>
								<td>
									<html:textarea styleId="testoNormale"
										property="datiLegame.noteLegame" cols="90" rows="6"
										disabled="${GestioneLegameTerminiForm.modalita eq 'CANCELLA'}"/>
									<sbn:tastiera limit="240" name="GestioneLegameTerminiForm"
										property="datiLegame.noteLegame"
										visible="${GestioneLegameTerminiForm.modalita ne 'CANCELLA'}" />
								</td>
							</tr>
						</table>
				</tr>
				<tr>
					<td class="etichetta" scope="col">
						<table class="myTable" cellpadding="0" cellspacing="0"
							width="100%">
							<tr>
								<td class="myHeader">
									<bean:message key="inserisci.tipoLegame"
										bundle="gestioneSemanticaLabels" />
								</td>
							</tr>
							<tr>
								<td>
									<html:select styleClass="testoNormale" property="tipoLegameJSP"
									disabled="${GestioneLegameTerminiForm.modalita eq 'CANCELLA'}">
										<html:optionsCollection property="listaTipiLegame"
											value="codice" label="codice" />
									</html:select>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td>
						<sbn:bottoniera buttons="listaPulsanti" />
					</td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
