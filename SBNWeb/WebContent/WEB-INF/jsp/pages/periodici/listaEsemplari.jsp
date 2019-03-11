<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/periodici/listaEsemplari.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<br />
		<jsp:include page="/WEB-INF/jsp/subpages/gestionesemantica/oggettoRiferimento.jsp" flush="true" />
		<hr />
		<sbn:blocchi numBlocco="bloccoSelezionato"
					numNotizie="kardex.blocco.totRighe" parameter="methodEsempl"
					totBlocchi="kardex.blocco.totBlocchi" elementiPerBlocco="kardex.blocco.maxRighe"
					disabled="${navForm.conferma}" />

		<table class="sintetica">
			<tr>
				<c:if test="${navForm.kardex.intestazione.fascicolo.esemplariInPolo}">
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="periodici.esame.esempl.bib" bundle="periodiciLabels" />
					</th>
				</c:if>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="periodici.kardex.stato" bundle="periodiciLabels" />
				</th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="periodici.esame.collocazione" bundle="periodiciLabels" />
				</th>
				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="periodici.kardex.data.ric" bundle="periodiciLabels" />&nbsp;
				</th>

				<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
					<bean:message key="periodici.fascicolo.note.esemplare" bundle="periodiciLabels" />
				</th>
			</tr>
			<logic:iterate id="item" property="fascicoli" name="navForm" indexId="idx">
				<sbn:rowcolor var="color" index="idx" />
				<tr bgcolor="${color}">
					<c:if test="${navForm.kardex.intestazione.fascicolo.esemplariInPolo}">
						<td><bs:write name="item" property="esemplare.codBib" /></td>
					</c:if>
					<td>
						<bs:write name="item" property="descrizioneStato"/>
						&nbsp;&#40;<bs:write name="item" property="dettaglioRicezione" />&#41;
					</td>
					<td><bs:write name="item" property="chiaveCollocazione" /></td>
					<td>
						<bs:write name="item" property="dataRicezione" format="dd/MM/yyyy" />
					</td>
					<td><bs:write name="item" property="esemplare.note"/></td>
				</tr>
			</logic:iterate>
		</table>

		</div>
		<div id="divFooterCommon">
			<sbn:blocchi numBlocco="bloccoSelezionato"
					numNotizie="kardex.blocco.totRighe" parameter="methodEsempl"
					totBlocchi="kardex.blocco.totBlocchi" elementiPerBlocco="kardex.blocco.maxRighe"
					bottom="true" disabled="${navForm.conferma}" />
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


