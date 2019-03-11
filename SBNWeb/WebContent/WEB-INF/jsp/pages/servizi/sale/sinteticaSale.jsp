<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/servizi/sale/listaSale.do">
		<div id="divForm">
		<div id="divMessaggio">
			<sbn:errors />
		</div>
		<br>
		<sbn:blocchi numBlocco="bloccoSelezionato"
			numNotizie="blocco.totRighe" parameter="methodListaDoc"
			totBlocchi="blocco.totBlocchi" elementiPerBlocco="blocco.maxRighe"
			disabled="${navForm.conferma}" />

		<table class="sintetica">
			<tr>
				<th width="3%" class="header" scope="col">
					n.
				</th>
				<th width="3%" class="header" scope="col">
					<bean:message key="servizi.sale.codiceSala" bundle="serviziLabels" />
				</th>
				<th width="50%" class="header" scope="col">
					<bean:message key="servizi.sale.descrizione" bundle="serviziLabels" />
				</th>
				<th width="5%" class="header" scope="col">
					<bean:message key="servizi.sale.numPostiTotale" bundle="serviziLabels" />
				</th>
				<th width="1%" class="header">
					<bean:message key="servizi.calendario.calendario" bundle="serviziLabels" />
				</th>
				<th width="3%" class="header" scope="col">
					<bean:message key="servizi.utenti.headerSelezionata" bundle="serviziLabels" />
				</th>
				<th width="3%" class="header" scope="col">
					<bean:message key="servizi.utenti.headerSelezionataMultipla" bundle="serviziLabels" />
				</th>
			</tr>

			<logic:iterate id="sala" property="sale" name="navForm" indexId="progr">
				<tr class="row alt-color">
					<td >
						<sbn:anchor name="sala" property="progr"/> <sbn:linkbutton
							name="sala" property="selectedSala" index="idSala"
							value="progr" key="servizi.bottone.esamina"
							bundle="serviziLabels" disabled="${navForm.conferma}"/>
					</td>
					<td >
						<bs:write name="sala" property="codSala" />
					</td>
					<td >
						<bs:write name="sala" property="descrizione" />
					</td>
					<td >
						<bs:write name="sala" property="numeroPosti" />
					</td>
					<td>
						<c:if test="${not empty sala.calendario }">
						<html:img page="/styles/images/calendar.svg"
							style="width:18px; height: 18px; display:block; margin:auto;"
							title="${sala.calendario.descrizione}"/>
					</c:if>
					</td>

					<td >
						<html:radio property="selectedSala" value="${sala.idSala}" style="text-align:center;"
									titleKey="servizi.erogazione.listaSolleciti.selezioneSingola" bundle="serviziLabels"
									disabled="${navForm.conferma}" />
					</td>
					<td >
						<html:multibox  property="selectedItems" value="${sala.idSala}" style="text-align:center;"
										titleKey="servizi.erogazione.listaSolleciti.selezioneMultipla" bundle="serviziLabels"
										disabled="${navForm.conferma}" />
					</td>
				</tr>
			</logic:iterate>
		</table>

		</div>
		<div id="divFooterCommon">
			<sbn:blocchi
			numBlocco="bloccoSelezionato" numNotizie="blocco.totRighe"
			parameter="methodListaDoc" totBlocchi="blocco.totBlocchi"
			elementiPerBlocco="blocco.maxRighe" bottom="true" disabled="${navForm.conferma}" /></div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td><sbn:bottoniera buttons="pulsanti" /></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>

