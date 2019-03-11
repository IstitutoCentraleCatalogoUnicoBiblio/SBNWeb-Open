<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area inferiore con i criteri di ricerca
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

<%@ page import="it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.InterrogazioneTitoloForm"%>


<!--
GESTIONE PARAMETRI GENERALI
 * Elem. blocco
 * Ordinamento
 * Formato lista
 * Liv. di ricerca
 -->

<!-- Intervento interno 26.11.2009 - almaviva2 - per gestire il filtre sulle Biblioteche
		Per area servizi si filtrano le biblio di area metropolitana
		per tutte le altre aree si può scegliere solo di filtrare per la biblio di navigazione -->

<table border="0">
	<tr>


		<c:choose>
			<c:when test="${navForm.provenienza eq 'SERVIZI' or navForm.provenienza eq 'MOVIMENTI_UTENTE'}">
				<td colspan="6">
					<bean:message key="label.filtroBiblioMetropolitane" bundle="gestioneBibliograficaLabels" />&nbsp;
								<html:text property="elencoBiblioMetropolitane" disabled="true" />&nbsp;
								<html:submit styleClass="buttonImage" property="methodInterrogTit">
								<bean:message key="button.cercabiblioteche" bundle="gestioneBibliograficaLabels" />
								</html:submit>
				</td>
			</c:when>
			<c:otherwise>
				<td class="etichetta"><bean:message key="ricerca.filtroPerBiblio"	bundle="gestioneBibliograficaLabels" /></td>
				<td width="80" >
					<html:checkbox property="interrGener.ricFiltrataPerBib"></html:checkbox>
					<html:hidden property="interrGener.ricFiltrataPerBib" value="false" />
				</td>
			</c:otherwise>
		</c:choose>



		<td width="80" class="etichetta"><bean:message
			key="ricerca.elementiBlocco" bundle="gestioneBibliograficaLabels" /></td>
		<td width="80" class="testoNormale"><html:text
			property="interrGener.elemXBlocchi" size="5"></html:text></td>
		<td width="75" class="etichetta"><bean:message
			key="ricerca.ordinamento" bundle="gestioneBibliograficaLabels" /></td>
		<td width="160" class="testoNormale"><html:select
			property="interrGener.tipoOrdinamSelez" style="width:130px">
			<html:optionsCollection property="interrGener.listaTipiOrdinam"
				value="codice" label="descrizione" />
		</html:select></td>
		<td width="80" class="etichetta"><bean:message
			key="ricerca.formatoLista" bundle="gestioneBibliograficaLabels" /></td>
		<td width="150" class="testoNormale"><html:select
			property="interrGener.formatoListaSelez" style="width:70px">
			<html:optionsCollection property="interrGener.listaFormatoLista"
				value="codice" label="descrizione" />
		</html:select></td>
	</tr>
</table>


<table align="center" border="0">
	<tr>
		<td width="85" class="etichetta"><bean:message
			key="ricerca.livelloRicerca" bundle="gestioneBibliograficaLabels" />
		</td>

		<c:choose>
			<c:when test="${interrogazioneTitoloForm.presenzaRicLocale eq 'NO'}">
				<td width="30" class="etichetta"><bean:message key="ricerca.locale"	bundle="gestioneBibliograficaLabels" /></td>
				<td width="30"><html:checkbox property="interrGener.ricLocale" disabled="true"></html:checkbox>
				</td>
			</c:when>
			<c:otherwise>
				<td width="30" class="etichetta"><bean:message key="ricerca.locale"	bundle="gestioneBibliograficaLabels" /></td>
				<td width="30">
					<html:checkbox property="interrGener.ricLocale" />
					<html:hidden property="interrGener.ricLocale" value="false" />
				</td>
			</c:otherwise>
		</c:choose>

		<c:choose>
			<c:when test="${interrogazioneTitoloForm.provenienza eq 'SERVIZI'}"></c:when>
			<c:when test="${interrogazioneTitoloForm.provenienza eq 'MOVIMENTI_UTENTE'}"></c:when>
			<c:when test="${interrogazioneTitoloForm.provenienza eq 'ACQUISIZIONI'}"></c:when>
			<c:otherwise>
				<td width="30" class="etichetta"><bean:message key="ricerca.indice"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="20">
					<html:checkbox property="interrGener.ricIndice" />
					<html:hidden property="interrGener.ricIndice" value="false" />
				</td>
			</c:otherwise>
		</c:choose>
	</tr>
</table>

