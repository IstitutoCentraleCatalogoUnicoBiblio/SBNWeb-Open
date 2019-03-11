<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<html:xhtml />
<layout:page>
	<sbn:navform action="/elaborazioniDifferite/invioElaborazioniDifferite.do">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors />
		</div>
		<br/>

			<c:if test="${invioElaborazioniDifferiteForm.tipoVisualizzazione eq 'AREA'}">
				<logic:iterate id="area" property="config.aree"	name="invioElaborazioniDifferiteForm" indexId="idx">
					<sbn:checkAttivita idControllo="AREA-${idx}">
						<table border="0" cellspacing="15">
							<tr>
								<td width="300px" class="testMsg" style="font-weight: bold; font-size: 15px">
									<bean:message key="${area.id}" bundle="elaborazioniDifferiteLabels" />
								</td>
								<td><html:submit styleClass="pulsanti"
										property="${sbn:getUniqueButtonName(invioElaborazioniDifferiteForm.SUBMIT_TESTO_PULSANTE, idx)}">&gt;
									</html:submit>
								</td>
							</tr>
						</table>
					</sbn:checkAttivita>
				</logic:iterate>
			</c:if>

			<c:if test="${invioElaborazioniDifferiteForm.tipoVisualizzazione eq 'ATTIVITA'}">
				<span style="font-weight: bold; font-size: 15px">
					<bean:message key="${invioElaborazioniDifferiteForm.area.id}" bundle="elaborazioniDifferiteLabels" />
				</span>
				<logic:iterate id="item" property="area.attivita" name="invioElaborazioniDifferiteForm" indexId="idx">
					<sbn:checkAttivita idControllo="${item.codAttivita}">
						<table border="0" cellspacing="15">
							<tr>
								<td class="etichetta" width="300">
									<c:choose>
										<c:when test="${item.statico}">
											<bean:message key="${item.descrizione}" bundle="elaborazioniDifferiteLabels" />
										</c:when>
										<c:otherwise>
											<bs:write name="item" property="descrizione" />
										</c:otherwise>
									</c:choose>
								</td>
								<td><html:submit styleClass="pulsanti"
									property="${sbn:getUniqueButtonName(invioElaborazioniDifferiteForm.SUBMIT_TESTO_PULSANTE, idx)}">&gt;
								</html:submit></td>
							</tr>
						</table>
					</sbn:checkAttivita>
				</logic:iterate>

				<div id="divFooter">
					<table align="center" border="0" style="height: 40px">
						<tr>
							<td align="center">
								<html:submit styleClass="pulsanti" property="invioElabDiffParam">
									<bean:message key="button.indietro"	bundle="elaborazioniDifferiteLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</div>
			</c:if>
		</div>
	</sbn:navform>
</layout:page>
