<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/periodici/gestionePeriodici.do">

		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		</div>

		<!--  biblioteca -->
		<table width="100%" border="0">
			<tr>
				<td colspan="4">
				<div class="etichetta"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /> <html:text disabled="true" styleId="testoNormale"
					property="biblioteca.cod_bib" size="5" maxlength="3"></html:text> <span> <html:submit
					title="Lista Biblioteche" styleClass="buttonImageListaSezione" disabled="false"
					property="methodPeriodici">
					<bean:message key="button.periodici.biblioteca" bundle="periodiciLabels" />
				</html:submit></span> <bs:write name="navForm" property="biblioteca.nom_biblioteca" />
				</div>
				</td>
			</tr>
		</table>

		<jsp:include page="/WEB-INF/jsp/subpages/periodici/intestazionePeriodico.jsp" flush="true" />

		<sbn:blocchi numBlocco="bloccoSelezionato"
					numNotizie="esame.blocco.totRighe" parameter="methodPeriodici"
					totBlocchi="esame.blocco.totBlocchi" elementiPerBlocco="esame.blocco.maxRighe"
					disabled="${navForm.conferma}" />

		<jsp:include page="/WEB-INF/jsp/subpages/periodici/folderConSerie.jsp" flush="true" />
		<br/>
		<div id="divFooterCommon">
			<sbn:blocchi numBlocco="bloccoSelezionato"
						numNotizie="esame.blocco.totRighe" parameter="methodPeriodici"
						totBlocchi="esame.blocco.totBlocchi" elementiPerBlocco="esame.blocco.maxRighe"
						bottom="true" disabled="${navForm.conferma}" />
		</div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<sbn:checkAttivita idControllo="KARDEX">
					<td><layout:combo bundle="periodiciLabels"
						label="button.periodici.esamina" name="navForm"
						button="button.periodici.esegui" property="idFunzioneEsamina"
						combo="comboGestioneEsamina" parameter="methodPeriodici" /></td>
				</sbn:checkAttivita>

				<td><html:submit property="methodPeriodici">
					<bean:message key="button.periodici.chiudi"
						bundle="periodiciLabels" />
				</html:submit></td>

				<sbn:checkAttivita idControllo="KARDEX">
					<td><bean:message key="periodici.esame.range.fascicoli" bundle="periodiciLabels" />&#58;</td>
					<td>
						<bean:message key="periodici.kardex.posiziona.da" bundle="periodiciLabels" />&nbsp;
						<html:text property="paramRicercaKardex.dataFrom" size="10" maxlength="10" />
					</td>
					<td>
						<bean:message key="periodici.kardex.posiziona.a" bundle="periodiciLabels" />&nbsp;
						<html:text property="paramRicercaKardex.dataTo" size="10" maxlength="10" />
					</td>
					<td><html:submit property="methodPeriodici">
						<bean:message key="button.periodici.esamina"
							bundle="periodiciLabels" />
					</html:submit></td>
					<td>
						<bean:message key="periodici.kardex.annate.filtro" bundle="periodiciLabels" />
						<html:checkbox property="paramRicercaKardex.soloAnnatePossedute" />
						<html:hidden property="paramRicercaKardex.soloAnnatePossedute" value="false" />
					</td>
				</sbn:checkAttivita>
			</tr>
		</table>
		</div>
	</sbn:navform>

</layout:page>


