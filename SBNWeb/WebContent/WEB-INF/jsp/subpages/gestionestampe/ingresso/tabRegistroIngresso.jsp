<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<table width="100%" align="center">
	<table width="100%" align="left" border="0" class="etichetta">
		<tr>
		<jsp:include page="/WEB-INF/jsp/subpages/common/documentofisico/selRangeInv.jsp" /></div>
		</tr>
		<%--<tr>
			<td>&nbsp;&nbsp;<bean:message
				key="regingresso.label.dataDa" bundle="gestioneStampeLabels" />&nbsp;<html:text
				styleId="testoNormale" property="regingroDataDa" size="10"
				maxlength="10"></html:text></td>
			<td colspan="3">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message
				key="regingresso.label.dataA" bundle="gestioneStampeLabels" />&nbsp;&nbsp;<html:text
				styleId="testoNormale" property="regingroDataA" size="10"
				maxlength="10"></html:text></td>
		</tr>--%>
	</table>
	<%--
	<table width="100%" align="left" border="0" class="etichetta">
		<tr>
			<td><label for="dataInventario"><bean:message
				key="regingresso.label.dataInventario" bundle="gestioneStampeLabels" />
			</label><html:checkbox styleId="chkDataInventario"
				property="chkDataInventario" /></html:checkbox><html:hidden
				property="chkDataInventario" value="false" /></td>
			<td><label for="codInventario"><bean:message
				key="regingresso.label.codInventario" bundle="gestioneStampeLabels" />
			</label><html:checkbox styleId="chkCodInventario" property="chkCodInventario" /></html:checkbox><html:hidden
				property="chkCodInventario" value="false" /></td>
			<td><label for="codFornitore"><bean:message
				key="regingresso.label.fornitore" bundle="gestioneStampeLabels" />
			</label><html:checkbox styleId="chkCodFornitore" property="chkCodFornitore" /></html:checkbox><html:hidden
				property="chkCodFornitore" value="false" /></td>
		</tr>
		<tr>
			<td><label for="tipoOrdine"><bean:message
				key="regingresso.label.tipoOrdine" bundle="gestioneStampeLabels" />
			</label><html:checkbox styleId="chkTipoOrdine" property="chkTipoOrdine" /></html:checkbox><html:hidden
				property="chkTipoOrdine" value="false" /></td>
			<td><label for="titolo"><bean:message
				key="regingresso.label.titolo" bundle="gestioneStampeLabels" /> </label><html:checkbox
				styleId="chkTitolo" property="chkTitolo" /></html:checkbox><html:hidden
				property="chkTitolo" value="false" /></td>
			<td><label for="codMateriale"><bean:message
				key="regingresso.label.codMateriale" bundle="gestioneStampeLabels" />
			</label><html:checkbox styleId="chkCodMateriale" property="chkCodMateriale" /></html:checkbox><html:hidden
				property="chkCodMateriale" value="false" /></td>
		</tr>
		<tr>
			<td><label for="valore"><bean:message
				key="regingresso.label.valore" bundle="gestioneStampeLabels" /> </label><html:checkbox
				styleId="chkValore" property="chkValore" /></html:checkbox><html:hidden
				property="chkValore" value="false" /></td>
			<td><label for="precisazioni"><bean:message
				key="regingresso.label.precisazioni" bundle="gestioneStampeLabels" />
			</label><html:checkbox styleId="chkPrecisazioni" property="chkPrecisazioni" /></html:checkbox><html:hidden
				property="chkPrecisazioni" value="false" /></td>
			<td><label for="importo"><bean:message
				key="regingresso.label.importo" bundle="gestioneStampeLabels" /> </label><html:checkbox
				styleId="chkImporto" property="chkImporto" /></html:checkbox><html:hidden
				property="chkImporto" value="false" /></td>
		</tr>
		<tr>
			<td><label for="bid"><bean:message
				key="regingresso.label.bid" bundle="gestioneStampeLabels" /> </label><html:checkbox
				styleId="chkBid" property="chkBid" /></html:checkbox><html:hidden property="chkBid"
				value="false" /></td>
			<td><label for="nrFattura"><bean:message
				key="regingresso.label.nrFattura" bundle="gestioneStampeLabels" />
			</label><html:checkbox styleId="chkNrFattura" property="chkNrFattura" /></html:checkbox><html:hidden
				property="chkNrFattura" value="false" /></td>
			<td><label for="dataFattura"><bean:message
				key="regingresso.label.dataFattura" bundle="gestioneStampeLabels" />
			</label><html:checkbox styleId="chkDataFattura" property="chkDataFattura" /></html:checkbox><html:hidden
				property="chkDataFattura" value="false" /></td>
		</tr>
		<tr>
			<td><label for="collocazione"><bean:message
				key="regingresso.label.collocazione" bundle="gestioneStampeLabels" />
			</label><html:checkbox styleId="chkCollocazione" property="chkCollocazione" /></html:checkbox><html:hidden
				property="chkCollocazione" value="false" /></td>
		</tr>
	</table>
</table>
<HR>
<table width="100%" align="center" border="0">
	<tr>
		<td><html:submit styleClass="pulsanti"
			property="methodStampaRegistroIngresso">
			<bean:message key="button.selezionaTutti"
				bundle="gestioneStampeLabels" />
		</html:submit></td>
		<td><html:submit styleClass="pulsanti"
			property="methodStampaRegistroIngresso">
			<bean:message key="button.invertiSelezione"
				bundle="gestioneStampeLabels" />
		</html:submit></td>
		<td><html:submit styleClass="pulsanti"
			property="methodStampaRegistroIngresso">
			<bean:message key="button.deselezionaTutti"
				bundle="gestioneStampeLabels" />
		</html:submit></td>
	</tr>
</table>

<bean-struts:size id="comboSize" name="stampaRegistroIngressoForm"
	property="elencoModelli" />
<logic:greaterEqual name="comboSize" value="2">
	<!--Selezione Modello Via Combo-->
	<HR>
	<table width="100%" border="0">
		<tr>
			<td width="15%" scope="col">
			<div align="left" class="etichetta"><bean:message
				key="biblioteche.label.modello" bundle="gestioneStampeLabels" /></div>
			</td>
			<td colspan="5" valign="top" scope="col" align="left"><html:select
				styleClass="testoNormale" property="tipoModello">
				<html:optionsCollection property="elencoModelli" value="jrxml"
					label="descrizione" />
			</html:select></td>
		</tr>
	</table>
</logic:greaterEqual>
<logic:lessThan name="comboSize" value="2">
	<!--Selezione Modello Hidden-->
	<table width="100%" border="0">
		<tr>
			<td width="15%" scope="col">
			<div align="left" class="etichetta">&nbsp;</div>
			</td>
			<td colspan="5" valign="top" scope="col" align="left">&nbsp; <html:hidden
				property="tipoModello"
				value="${stampaRegistroIngressoForm.elencoModelli[0].jrxml}" /></td>
		</tr>
	</table>
</logic:lessThan>
--%>
