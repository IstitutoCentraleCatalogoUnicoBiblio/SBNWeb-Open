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
	<sbn:navform action="/servizi/utenti/ListaServizi">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors bundle="serviziMessages" />
		</div>
		<br>
		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"
			elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
			parameter="methodListaSer"
			livelloRicerca="${ListaUtentiForm.livelloRicerca}" />
		<table class="sintetica">
			<tr bgcolor="#dde8f0">
				<th width="10%" class="etichetta" style="text-align: center;">
				<bean:message key="servizi.utenti.headerProgressivo"
					bundle="serviziLabels" /></th>
				<th width="45%" class="etichetta" style="text-align: center;">
<!--				<bean:message key="servizi.utenti.titCodice" bundle="serviziLabels" />-->
				<bean:message key="servizi.erogazione.tipoServizio" bundle="serviziLabels" />
				</th>
				<th width="45%" class="etichetta" style="text-align: center;">
				<bean:message key="servizi.utenti.titServizio"
					bundle="serviziLabels" /></th>
				<!--
				<th width="65%" class="etichetta" style="text-align: center;">
				<bean:message key="servizi.utenti.descrizioneServizio"
					bundle="serviziLabels" /></th>
				-->
				<th width="5%" class="etichetta" style="text-align: center;"><bean:message
					key="servizi.utenti.headerSelezionataMultipla"
					bundle="serviziLabels" /></th>
				</tr>
			<logic:iterate id="listaServizi" property="elencoScegliServizio"
				name="ListaServiziForm" indexId="index">
				<sbn:rowcolor var="color" index="index" />
				<tr bgcolor="${color}">
					<td class="testoNoBold" style="text-align: center;"><bean-struts:write
						name="ListaServiziForm"
						property='<%="elencoScegliServizio[" + index
										+ "].progressivo"%>' />
					</td>
					<td class="testoNoBold" style="text-align: center;"><bean-struts:write
						name="ListaServiziForm"
						property='<%="elencoScegliServizio[" + index
										+ "].componiTipoServizio"%>' />
					</td>
					<td class="testoNoBold" style="text-align: center;"><bean-struts:write
						name="ListaServiziForm"
						property='<%="elencoScegliServizio[" + index
										+ "].componi"%>' />
					</td>
					<%--
					<td class="testoNoBold"><bean-struts:write
						name="ListaServiziForm"
						property='<%="elencoScegliServizio[" + index
										+ "].descrizione"%>' />
					</td>
					--%>
					<td class="testoNoBold" style="text-align: center;"><html:checkbox
						styleId="testoNoBold" name="ListaServiziForm"
						property='<%="elencoScegliServizio[" + index
										+ "].cancella"%>'
						value="C" /></td>
				</tr>
			</logic:iterate>
		</table>
		</div>
		<div id="divFooterCommon"><sbn:blocchi numNotizie="totRighe"
			numBlocco="bloccoSelezionato" elementiPerBlocco="maxRighe"
			totBlocchi="totBlocchi" parameter="methodListaSer" bottom="true" /></div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodListaSer">
					<bean:message key="servizi.bottone.scegli" bundle="serviziLabels" />
				</html:submit> <html:submit property="methodListaSer">
					<bean:message key="servizi.bottone.annulla" bundle="serviziLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
