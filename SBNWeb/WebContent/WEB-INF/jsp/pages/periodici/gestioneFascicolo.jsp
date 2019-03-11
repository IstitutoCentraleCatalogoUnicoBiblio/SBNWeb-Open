<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/periodici/gestioneFascicolo.do">

		<div id="divForm">
			<div id="divMessaggio"><sbn:errors /></div>
			<br />
			<c:choose>
				<c:when test="${navForm.operazione eq 'ESAME' }">
					<jsp:include page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" flush="true" />
				</c:when>
				<c:otherwise>
					<jsp:include page="/WEB-INF/jsp/subpages/periodici/kardexIntestazione.jsp" flush="true" />
				</c:otherwise>
			</c:choose>
			<hr />
			<h3><bean:message key="periodici.fascicolo.dati" bundle="periodiciLabels" /></h3>
			<sbn:disableAll disabled="${navForm.conferma}">
			<sbn:disableAll checkAttivita="DESCRIZIONE_FASCICOLO">
			<table>
				<sbn:checkAttivita idControllo="BID_COLLOCAZIONE">
				<tr>
					<td>
						<bean:message key="periodici.fascicolo.titolo" bundle="periodiciLabels" />
					</td>
					<td colspan="3">
						<strong><bs:write name="navForm" property="fascicolo.bid" /></strong>&nbsp;<bs:write name="navForm" property="fascicolo.isbd" />
					</td>
				</tr>
				<tr><td colspan="4">&nbsp;</td></tr>
				</sbn:checkAttivita>
				<tr>
					<td>
						<bean:message key="periodici.kardex.tipo.per" bundle="periodiciLabels" />
					</td>
					<td>
						<html:select property="fascicolo.cd_per">
							<html:optionsCollection property="listaPeriodicita" value="cd_tabellaTrim" label="ds_tabella" />
						</html:select>
					</td>
					<sbn:disableAll checkAttivita="DATA_CONVENZIONALE">
						<td>
							<bean:message key="periodici.kardex.tipo_fasc" bundle="periodiciLabels" />&nbsp;
							<html:select property="fascicolo.cd_tipo_fasc">
								<html:optionsCollection property="listaTipoFascicolo" value="cd_tabellaTrim" label="ds_tabella" />
							</html:select>
						</td>
						<td>
							<bean:message key="periodici.kardex.data.conv.fasc" bundle="periodiciLabels" />&nbsp;
							<html:text name="navForm" property="fascicolo.dataInizioPub" maxlength="10" size="10" />&nbsp;
							<html:text name="navForm" property="fascicolo.dataFinePub" maxlength="10" size="10" />&nbsp;
							<span style="font-size: 75%;">gg/mm/aaaa</span>
						</td>
					</sbn:disableAll>
				</tr>
				<tr>
					<td>
						<bean:message key="periodici.kardex.annata" bundle="periodiciLabels" />
					</td>
					<td>
						<html:text name="navForm" property="fascicolo.annata" maxlength="10" size="10"/>
					</td>
					<td>
						<bean:message key="periodici.kardex.volume" bundle="periodiciLabels" />&nbsp;
						<html:text name="navForm" property="fascicolo.numVolume" maxlength="4" size="4"/>
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="periodici.fascicolo.numerazione" bundle="periodiciLabels" />
					</td>
					<td>
						<html:text name="navForm" property="fascicolo.iniFascicolo" maxlength="5" size="5" />&nbsp;
						<html:text name="navForm" property="fascicolo.fineFascicolo" maxlength="5" size="5" />
					</td>
					<td>
						&nbsp;
					</td>
					<td>
						<bean:message key="periodici.fascicolo.alternativo" bundle="periodiciLabels" />&nbsp;
						<html:text name="navForm" property="fascicolo.num_alter" maxlength="15" size="15" />
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="periodici.fascicolo.descrizione" bundle="periodiciLabels" />
					</td>
					<td colspan="3">
						<html:textarea property="fascicolo.descrizione" cols="80" rows="2" styleClass="expandedLabel" />
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="periodici.fascicolo.sici" bundle="periodiciLabels" />
					</td>
					<td colspan="2">
						<html:text name="navForm" property="fascicolo.sici" maxlength="80" size="80"/>
					</td>
					<td>
						<bean:message key="periodici.fascicolo.ean" bundle="periodiciLabels" />&nbsp;
						<html:text name="navForm" property="fascicolo.ean" maxlength="13" size="13"/>
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="periodici.fascicolo.note" bundle="periodiciLabels" />
					</td>
					<td colspan="3">
						<html:textarea property="fascicolo.note" cols="80" rows="2" styleClass="expandedLabel" />
					</td>
				</tr>
			</table>
			</sbn:disableAll>
			<sbn:disableAll checkAttivita="AMMINISTRAZIONE">
			<c:if test="${navForm.fascicolo.posseduto and !navForm.fascicolo.esemplare.cancellato}">
				<hr />
				<h3><bean:message key="periodici.fascicolo.esemplare.dati" bundle="periodiciLabels" /></h3>
				<table width="66%">
					<tr>
						<td colspan="4">
							<table class="sintetica" style="width: 50%; max-width:60%;">
								<tr>
									<td><bean:message key="periodici.kardex.stato" bundle="periodiciLabels" /></td>
									<td><bs:write name="navForm" property="fascicolo.descrizioneStato"/></td>
								</tr>
								<tr>
									<td><bean:message key="periodici.fascicolo.ricevuto.ord" bundle="periodiciLabels" /></td>
									<td><bs:write name="navForm" property="fascicolo.chiaveOrdine"/></td>
								</tr>
								<tr>
									<td><bean:message key="periodici.fascicolo.ricevuto.inv" bundle="periodiciLabels" /></td>
									<td>
										<bs:write name="navForm" property="fascicolo.chiaveInventario"/>
										<c:if test="${not empty navForm.fascicolo.chiaveInventario}">
											&nbsp;&#47;&nbsp;
											<%--<bean:message key="periodici.fascicolo.grp_fasc" bundle="periodiciLabels" />&nbsp;  --%>
											<html:text name="navForm" property="fascicolo.gruppoFascicolo" maxlength="4" size="4"
												titleKey="periodici.fascicolo.grp_fasc" bundle="periodiciLabels"/>
										</c:if>
									</td>
								</tr>
							</table>
							<br />
						</td>
					</tr>
					<tr>
						<td>
							<c:choose>
								<c:when test="${navForm.kardex.tipo eq 'ORDINE'}">
									<bean:message key="periodici.kardex.data.ric" bundle="periodiciLabels" />&nbsp;
								</c:when>
								<c:otherwise>
									<bean:message key="periodici.kardex.data.associa" bundle="periodiciLabels" />&nbsp;
								</c:otherwise>
							</c:choose>
							<html:text name="navForm" property="fascicolo.dataRicezione" maxlength="10" size="10" />
						</td>
						<td colspan="3">
							<bean:message key="periodici.fascicolo.esemplare.smarrito" bundle="periodiciLabels" />&nbsp;
							<html:checkbox property="fascicolo.smarrito" />
							<html:hidden property="fascicolo.smarrito" value="false" />
							<bean:message key="periodici.fascicolo.esemplare.rilegatura" bundle="periodiciLabels" />&nbsp;
							<html:checkbox property="fascicolo.rilegatura" />
							<html:hidden property="fascicolo.rilegatura" value="false" />
						</td>
					</tr>
					<tr>
						<td>
							<bean:message key="periodici.fascicolo.note.esemplare" bundle="periodiciLabels" />
						</td>
						<td colspan="3">
							<html:textarea property="fascicolo.esemplare.note" cols="80" rows="2" styleClass="expandedLabel" />
						</td>
					</tr>
				</table>
			</c:if>
			</sbn:disableAll>
			</sbn:disableAll>
			<hr />
			<c:if test="${navForm.operazione eq 'ULTERIORI_FASCICOLI' }">
				<table width="66%">
					<tr>
						<td>
							<bean:message key="periodici.fascicolo.ult.fascicoli" bundle="periodiciLabels" />&nbsp;
							<html:text name="navForm" property="ulterioriFascicoli" maxlength="2" size="4" />
						</td>
					</tr>
				</table>
			</c:if>
		</div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<td>
					<sbn:bottoniera buttons="pulsanti" />
				</td>
			</tr>
		</table>
		</div>
	</sbn:navform>

</layout:page>


