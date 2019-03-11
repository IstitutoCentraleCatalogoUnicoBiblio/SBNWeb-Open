<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>
	<sbn:navform action="/gestionesemantica/soggetto/ModLegameTraDescr.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
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
						<bean:message key="crea.soggettario"
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
				<bs:define id="color" value="#FEF1E2" />
				<logic:iterate id="item" property="descrittori"
					name="navForm" indexId="progr">
					<sbn:rowcolor var="color" index="progr" />
					<tr bgcolor="${color}">
						<td class="testoBold" align="right">
							<c:choose>
								<c:when test="${progr eq 0}">
									<bean:message key="gestionesemantica.idPartenza"
										bundle="gestioneSemanticaLabels" />
								</c:when>
								<c:otherwise>
									<bean:message key="gestionesemantica.idArrivo"
										bundle="gestioneSemanticaLabels" />
								</c:otherwise>
							</c:choose>
						</td>
						<td >
							<bs:write name="item" property="did" />
						</td>
						<td >
							<bs:write name="item" property="campoSoggettario" />
							<c:if test="${item.gestisceEdizione}">
								&nbsp;<bs:write name="item" property="edizioneSoggettario"/>
							</c:if>
						</td>
						<td >
							<bs:write name="item" property="livAut" />
						</td>
						<td >
							<bs:write name="item" property="formaNome" />
						</td>
						<td >
							<bs:write name="item" property="testo" />
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
									<html:textarea styleId="testoNormale" property="note" cols="90"
										rows="6"></html:textarea>
									<sbn:tastiera limit="240" name="navForm"
										property="note" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="etichetta" scope="col">
						<table class="myTable" cellpadding="0" cellspacing="0"
							width="100%">
							<tr>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td class="myHeader">
									<bean:message key="inserisci.tipoLegame"
										bundle="gestioneSemanticaLabels" />
								</td>
							</tr>
							<tr>
								<td>
									<html:select styleClass="testoNormale" property="codTipoLegame">
										<html:optionsCollection property="listaTipoLegame"
											value="cd_tabellaTrim" label="cd_tabellaTrim" />
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
					<td align="center">
						<html:submit property="methodModTraDes">
							<bean:message key="button.ok" bundle="gestioneSemanticaLabels" />
						</html:submit>
					</td>
					<td align="center">
						<html:submit property="methodModTraDes">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
