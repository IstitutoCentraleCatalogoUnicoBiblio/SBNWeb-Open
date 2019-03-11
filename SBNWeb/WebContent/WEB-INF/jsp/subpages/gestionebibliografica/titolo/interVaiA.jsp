<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area superiore con canali e filtri
		almaviva2 - Inizio Codifica Agosto 2006
-->

<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>



<noscript>
<table border="0">
	<tr>
		<td width="350"></td>
		<td width="100" class="etichetta">
		<html:submit property="methodAnalitTitolo">
			<bean:message key="ricerca.button.aggiornaCanali"
				bundle="gestioneBibliograficaLabels" />
		</html:submit>
		</td>
	</tr>
</table>
</noscript>



<table width="100%" border="0">
	<tr>
		<td width="05%"></td>
		<td width="30%" class="etichetta"><bean:message key="vaiA.procedura"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="55%" class="etichetta"><bean:message key="vaiA.funzione"
			bundle="gestioneBibliograficaLabels" /></td>
	</tr>
	<c:if test="${!empty analiticaTitoloForm.interrogazioneVaiAForm.listaVaiAAcquis}">
		<tr>
			<td width="05%"></td>
			<td width="30%" class="etichetta"><bean:message
				key="vaiA.acquisizioni" bundle="gestioneBibliograficaLabels" />:</td>
			<td width="55%" class="testoNormale"><html:select
				property="interrogazioneVaiAForm.vaiAAcquisSelez">
				<html:optionsCollection
					property="interrogazioneVaiAForm.listaVaiAAcquis"
					value="codice" label="descrizione" />
			</html:select></td>
		</tr>
	</c:if>
	<c:if test="${!empty analiticaTitoloForm.interrogazioneVaiAForm.listaVaiACatalSemant}">
	<tr>
		<td width="05%"></td>
		<td width="30%" class="etichetta"><bean:message key="vaiA.catalSemant"
			bundle="gestioneBibliograficaLabels" />:</td>
		<td width="55%" class="testoNormale"><html:select
			property="interrogazioneVaiAForm.vaiACatalSemantSelez">
			<html:optionsCollection
				property="interrogazioneVaiAForm.listaVaiACatalSemant"
				value="codice" label="descrizione" />
		</html:select></td>
	</tr>
	</c:if>
	<c:if test="${!empty analiticaTitoloForm.interrogazioneVaiAForm.listaVaiAGestDocFis}">
		<tr>
			<td width="05%"></td>
			<td width="30%" class="etichetta"><bean:message key="vaiA.gestDocFis"
				bundle="gestioneBibliograficaLabels" />:</td>
			<td width="55%" class="testoNormale"><html:select
				property="interrogazioneVaiAForm.vaiAGestDocFisSelez">
				<html:optionsCollection
					property="interrogazioneVaiAForm.listaVaiAGestDocFis"
					value="codice" label="descrizione" />
			</html:select></td>
		</tr>
	</c:if>


	<!--	// Modifica almaviva2 04.08.2010 - Nuova voce per Gestione Periodici -->
	<c:if test="${!empty analiticaTitoloForm.interrogazioneVaiAForm.listaVaiAGestPeriodici}">
		<tr>
			<td width="05%"></td>
			<td width="30%" class="etichetta"><bean:message
				key="vaiA.gestPeriodici" bundle="gestioneBibliograficaLabels" />:</td>
			<td width="55%" class="testoNormale"><html:select
				property="interrogazioneVaiAForm.vaiAGestPeriodiciSelez">
				<html:optionsCollection
					property="interrogazioneVaiAForm.listaVaiAGestPeriodici"
					value="codice" label="descrizione" />
			</html:select></td>
		</tr>
	</c:if>


	<c:if test="${!empty analiticaTitoloForm.interrogazioneVaiAForm.listaVaiAGestBibliog}">
	<tr>
		<td width="05%%"></td>
		<td width="30%" class="etichetta"><bean:message key="vaiA.gestBibliog"
			bundle="gestioneBibliograficaLabels" />:</td>
		<td width="55%" class="testoNormale"><html:select
			property="interrogazioneVaiAForm.vaiAGestBibliogSelez">
			<html:optionsCollection
				property="interrogazioneVaiAForm.listaVaiAGestBibliog"
				value="codice" label="descrizione" />
		</html:select></td>
	</tr>
	</c:if>

	<c:if test="${!empty analiticaTitoloForm.interrogazioneVaiAForm.listaVaiACatalUnimarc}">
		<tr>
			<td width="05%%"></td>
			<td width="40%" class="etichetta"><bean:message
				key="vaiA.altreFunzioni" bundle="gestioneBibliograficaLabels" />:</td>
			<td width="55%" class="testoNormale"><html:select
				property="interrogazioneVaiAForm.vaiACatalUnimarcSelez">
				<html:optionsCollection
					property="interrogazioneVaiAForm.listaVaiACatalUnimarc"
					value="codice" label="descrizione" />
			</html:select></td>
		</tr>
	</c:if>

		<c:if test="${!empty analiticaTitoloForm.interrogazioneVaiAForm.listaVaiAGestPossessori}">
		<tr>
			<td width="05%"></td>
			<td width="30%" class="etichetta"><bean:message key="vaiA.gestPossessori"
				bundle="gestioneBibliograficaLabels" />:</td>
			<td width="55%" class="testoNormale"><html:select
				property="interrogazioneVaiAForm.vaiAGestPossessoriSelez">
				<html:optionsCollection
					property="interrogazioneVaiAForm.listaVaiAGestPossessori"
					value="codice" label="descrizione" />
			</html:select></td>
		</tr>
	</c:if>


</table>
