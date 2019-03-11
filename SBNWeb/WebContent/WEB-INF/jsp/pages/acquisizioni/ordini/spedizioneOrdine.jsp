<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:checkAttivita idControllo="STAMPA">
		<meta http-equiv="Refresh" content="3; url='<c:url value="/fileDownload.do" />'" />
	</sbn:checkAttivita>
	<sbn:navform action="/acquisizioni/ordini/spedizioneOrdine.do">
		<div id="divMessaggio">
			<sbn:errors />
		</div>
		<div id="divForm">
			<table>
				<tr>
					<td class="etichetta"><bean:message key="ordine.label.confORD"
							bundle="acquisizioniLabels" /></td>
					<td><bean:message key="ricerca.label.soloAnno"
							bundle="acquisizioniLabels" /> <html:text styleId="testoNormale"
							property="ordine.annoOrdine" size="4" readonly="true"></html:text>
						&nbsp;&nbsp; <bean:message key="ricerca.label.nr"
							bundle="acquisizioniLabels" /> <html:text styleId="testoNormale"
							property="ordine.codOrdine" size="10" readonly="true"></html:text>
						&nbsp;&nbsp; <bean:message key="ordine.label.data"
							bundle="acquisizioniLabels" /> <html:text styleId="testoNormale"
							property="ordine.dataOrdine" size="10" readonly="true"></html:text>
						&nbsp;&nbsp; <bean:message key="ordine.label.stampato"
							bundle="acquisizioniLabels" /> <html:checkbox
							property="ordine.stampato" disabled="true"></html:checkbox>
						&nbsp;&nbsp; <bean:message key="ordine.label.stato"
							bundle="acquisizioniLabels" /> <html:select disabled="true"
							styleClass="testoNormale" property="ordine.statoOrdine"
							style="width:40px">
							<html:optionsCollection property="listaStatoOrdine" value="cd_tabellaTrim" label="ds_tabella" />
						</html:select></td>
				</tr>
				<tr>
					<td colspan="2"><hr /></td>
				</tr>
				<sbn:disableAll disabled="${navForm.conferma or navForm.ordine.spedito}">
				<tr>
					<td class="etichetta"><bean:message
							key="ordine.label.dataSpedizione" bundle="acquisizioniLabels" /></td>
					<td><html:text name="navForm"
							property="ordine.ordineCarrelloSpedizione.data" size="10"
							maxlength="10" />
						<sbn:checkAttivita idControllo="GOOGLE">
							&nbsp;&nbsp; <bean:message key="ordine.label.prgSpedizione" bundle="acquisizioniLabels" />
							<html:text name="navForm"
								property="ordine.ordineCarrelloSpedizione.prg" size="3"
								maxlength="3" />
								&nbsp;&nbsp; <bean:message
								key="ordine.label.cartName" bundle="acquisizioniLabels" /> <html:text
								disabled="true" name="navForm"
								property="ordine.ordineCarrelloSpedizione.cartName" size="30" />
						</sbn:checkAttivita>
					</td>
				</tr>
				</sbn:disableAll>
			</table>
		</div>
		<div id="divFooterCommon">
			<br /><jsp:include page="/WEB-INF/jsp/pages/gestionestampe/common/tipoStampa.jsp" />
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td><sbn:bottoniera buttons="pulsanti" /></td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>

