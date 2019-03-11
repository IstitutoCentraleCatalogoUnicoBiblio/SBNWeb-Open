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

<%@ page import="it.iccu.sbn.web.actionforms.gestionebibliografica.titolo.InterrogazioneTitoloForm"%>

<!--
 GESTIONE DELLA PARTE COMUE 'CANALI + FILTRI
 -->
<table border="0">
	<tr>
		<td width="130" class="etichetta"><bean:message key="ricerca.titolo"
			bundle="gestioneBibliograficaLabels" />:</td>
		<td class="testoNormale"><html:text property="interrGener.titolo"
			size="75" maxlength="80"></html:text>
<sbn:tastiera limit="80" property="interrGener.titolo" name="interrogazioneTitoloForm"></sbn:tastiera>
			</td>
		<td class="etichetta"><bean:message key="ricerca.puntuale"
			bundle="gestioneBibliograficaLabels" />:
				<html:checkbox property="interrGener.titoloPunt" />
				<html:hidden property="interrGener.titoloPunt" value="false" />
			</td>
	</tr>
	<tr>
		<td class="etichetta"><bean:message key="ricerca.bid"
			bundle="gestioneBibliograficaLabels" />:</td>
		<td class="testoNormale"><html:text property="interrGener.bid"
			size="10" maxlength="10"></html:text></td>
		<td></td>
	</tr>
</table>

<!-- Ottobre 2014 almaviva2 - Eliminazione ENUMERATE su protocollo per il Numero Standard ed eliminazione classe SbnTipoSTD
									e modifica lunghezza massima a 25 caratteri   -->
<table border="0">
	<tr>
		<td width="130" class="etichetta"><bean:message
			key="ricerca.num.standard" bundle="gestioneBibliograficaLabels" />:
		</td>
		<td width="35" class="etichetta"><bean:message key="ricerca.tipo"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="85" class="testoNormale"><html:select
			property="interrGener.numStandardSelez" style="width:36px">
			<html:optionsCollection property="interrGener.listaTipiNumStandard"
				value="codice" label="descrizioneCodice" />
		</html:select></td>
		<td class="testoNormale"><html:text styleId="testoNormale"
			property="interrGener.numStandard1" size="15" maxlength="25"></html:text>
		- <html:text styleId="testoNormale"
			property="interrGener.numStandard2" size="15" maxlength="25"></html:text>
		</td>
	</tr>
</table>

<hr color="#dde8f0" />

<table border="0">
	<tr>
		<td width="130" class="etichetta"><bean:message key="ricerca.impronta"
			bundle="gestioneBibliograficaLabels" />:</td>
		<td class="testoNormale"><html:text
			property="interrGener.impronta1" size="10" maxlength="10"></html:text>
			<html:text property="interrGener.impronta2" size="14" maxlength="14"></html:text>
			<html:text property="interrGener.impronta3" size="8" maxlength="8"></html:text>
		</td>

		<td>
			<bean:message key="ricerca.docAntico" bundle="gestioneBibliograficaLabels" />
			<html:radio	property="interrGener.tipoMaterialeImpronta" value="improntaAntico"/>
			<bean:message key="ricerca.docMusica" bundle="gestioneBibliograficaLabels" />
			<html:radio	property="interrGener.tipoMaterialeImpronta" value="improntaMusica"/>
		</td>
	</tr>
</table>

<hr color="#dde8f0" />

<table border="0">
	<tr>
		<td width="130" class="etichetta"><bean:message key="ricerca.natura"
			bundle="gestioneBibliograficaLabels" />:</td>
		<td width="60" class="testoNormale"><html:select
			property="interrGener.naturaSelez1" style="width:36px">
			<html:optionsCollection property="interrGener.listaNature"
				value="codice" label="descrizioneCodice" />
		</html:select></td>
		<td width="60" class="testoNormale"><html:select
			property="interrGener.naturaSelez2" style="width:36px">
			<html:optionsCollection property="interrGener.listaNature"
				value="codice" label="descrizioneCodice" />
		</html:select></td>
		<td width="60" class="testoNormale"><html:select
			property="interrGener.naturaSelez3" style="width:36px">
			<html:optionsCollection property="interrGener.listaNature"
				value="codice" label="descrizioneCodice" />
		</html:select></td>
		<td width="63" class="testoNormale"><html:select
			property="interrGener.naturaSelez4" style="width:36px">
			<html:optionsCollection property="interrGener.listaNature"
				value="codice" label="descrizioneCodice" />
		</html:select></td>
		<td width="45" class="etichetta"><bean:message
			key="ricerca.sottotipolegame" bundle="gestioneBibliograficaLabels" />
		</td>
		<td width="80" class="testoNormale"><html:select
			property="interrGener.sottoNaturaDSelez" style="width:60px">
			<html:optionsCollection property="interrGener.listaSottonatureD"
				value="codice" label="descrizioneCodice" />
		</html:select></td>


		<c:if test="${interrogazioneTitoloForm.provenienza eq 'INTERFILTRATA'}">
			<td width="30" class="etichetta"><bean:message key="dettaglio.sici" bundle="gestioneBibliograficaLabels" /></td>
			<td class="testoNormale"><html:text property="interrGener.codiceSici" maxlength="80" size="25"></html:text></td>
		</c:if>
	</tr>
</table>



<table border="0">
	<tr>
		<td width="130" class="etichetta"><bean:message
			key="ricerca.data.pubblicazione" bundle="gestioneBibliograficaLabels" />:
		</td>
		<td class="etichetta"><bean:message key="ricerca.tipo.Data"
			bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:select
			property="interrGener.tipoDataSelez" style="width:36px">
			<html:optionsCollection property="interrGener.listaTipoData"
				value="codice" label="descrizioneCodice" />
		</html:select></td>

		<td class="etichetta"><bean:message key="ricerca.dataPubblicazione1"
			bundle="gestioneBibliograficaLabels" /> <bean:message
			key="ricerca.da" bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:text property="interrGener.data1Da"
			maxlength="4" size="4"></html:text></td>
		<td class="etichetta"><bean:message key="ricerca.a"
			bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:text property="interrGener.data1A"
			maxlength="4" size="4"></html:text></td>

		<td class="etichetta"><bean:message key="ricerca.dataPubblicazione2"
			bundle="gestioneBibliograficaLabels" /> <bean:message
			key="ricerca.da" bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:text property="interrGener.data2Da"
			maxlength="4" size="4"></html:text></td>
		<td class="etichetta"><bean:message key="ricerca.a"
			bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:text styleId="testoNormale"
			property="interrGener.data2A" maxlength="4" size="4"></html:text></td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="130" class="etichetta"><bean:message key="ricerca.lingua"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="90" class="testoNormale"><html:select
			property="interrGener.linguaSelez" style="width:47px">
			<html:optionsCollection property="interrGener.listaLingue"
				value="codice" label="descrizioneCodice" />
		</html:select></td>
		<td width="38" class="etichetta"><bean:message key="ricerca.paese"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="45" class="testoNormale"><html:select
			property="interrGener.paeseSelez" style="width:39px">
			<html:optionsCollection property="interrGener.listaPaese"
				value="codice" label="descrizioneCodice" />
		</html:select></td>
	</tr>
</table>
<table border="0">
	<tr>
		<td width="130" class="etichetta"><bean:message key="ricerca.luogo"
			bundle="gestioneBibliograficaLabels" /></td>

			<!-- Inizio modifica almaviva2 BUG MANTIS 3883 27.09.2010
			 per gestire correttamente l'inibizione dell'uso del filtro per luogo (o per marca),
			 dopo aver selezionato Esamina titoli collegati con filtro, se si è partiti da un record di authority dello stesso tipo.
			 TitoliCollegatiInvoke.oggDiRicerca = 4  TITOLI_COLLEGATI_A_LUOGO		-->

			<c:choose>
				<c:when test="${interrogazioneTitoloForm.provenienza eq 'INTERFILTRATA' and interrogazioneTitoloForm.oggettoDiRicerca eq '4'}">
					<td class="testoNormale"><html:text property="interrGener.luogoPubbl" size="75" maxlength="80" disabled="true"></html:text>
					<sbn:tastiera limit="80" property="interrGener.luogoPubbl" name="interrogazioneTitoloForm"></sbn:tastiera>
					</td>
				</c:when>
				<c:otherwise>
					<td class="testoNormale"><html:text property="interrGener.luogoPubbl" size="75" maxlength="80"></html:text>
					<sbn:tastiera limit="80" property="interrGener.luogoPubbl" name="interrogazioneTitoloForm"></sbn:tastiera>
					</td>
				</c:otherwise>
			</c:choose>
			<!-- Fine modifica almaviva2 BUG MANTIS 3883 27.09.2010	-->


		<td class="etichetta"><bean:message key="ricerca.puntuale"
			bundle="gestioneBibliograficaLabels" />:
				<html:checkbox property="interrGener.luogoPubblPunt" />
				<html:hidden property="interrGener.luogoPubblPunt" value="false" />
		</td>
	</tr>


	<tr>

		<td class="etichetta"><bean:message key="ricerca.paroleEditore"
			bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:text
			property="interrGener.paroleEditore" size="75" maxlength="80"></html:text>
		<sbn:tastiera limit="80" property="interrGener.paroleEditore" name="interrogazioneTitoloForm"></sbn:tastiera></td>
	</tr>


	<tr>
		<td class="etichetta"><bean:message key="ricerca.nomeCollegato"
			bundle="gestioneBibliograficaLabels" /></td>
		<td class="testoNormale"><html:text
			property="interrGener.nomeCollegato" size="75" maxlength="80"></html:text>
		<sbn:tastiera limit="80" property="interrGener.nomeCollegato" name="interrogazioneTitoloForm"></sbn:tastiera></td>
		<td class="etichetta"><bean:message key="ricerca.puntuale"
			bundle="gestioneBibliograficaLabels" />:
				<html:checkbox property="interrGener.nomeCollegatoPunt" />
				<html:hidden property="interrGener.nomeCollegatoPunt" value="false"/>
			</td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="130" class="etichetta"><bean:message
			key="ricerca.responsabilita" bundle="gestioneBibliograficaLabels" /></td>
		<td width="60" class="testoNormale"><html:select
			property="interrGener.responsabilitaSelez" style="width:35px">
			<html:optionsCollection property="interrGener.listaResponsabilita"
				value="codice" label="descrizioneCodice" />
		</html:select></td>
		<td width="60" class="etichetta"><bean:message key="ricerca.relazione"
			bundle="gestioneBibliograficaLabels" /></td>
		<td width="60" class="testoNormale"><html:select
			property="interrGener.relazioniSelez" style="width:47px">
			<html:optionsCollection property="interrGener.listaRelazioni"
				value="codice" label="descrizioneCodice" />
		</html:select></td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="130" class="etichetta"><bean:message
			key="ricerca.tipoRecord" bundle="gestioneBibliograficaLabels" /></td>
		<td width="60" class="testoNormale"><html:select
			property="interrGener.tipiRecordSelez" style="width:35px">
			<html:optionsCollection property="interrGener.listaTipiRecord"
				value="codice" label="descrizioneCodice" />
		</html:select></td>

		<td width="60" class="etichetta"><bean:message
			key="ricerca.specificita" bundle="gestioneBibliograficaLabels" /></td>
		<td width="70" class="testoNormale"><html:select
			property="interrGener.specificitaSelez" style="width:70px">
			<html:optionsCollection property="interrGener.listaSpecificita"
				value="codice" label="descrizione" />
		</html:select></td>

		<td width="80" class="etichetta"><bean:message key="ricerca.libretto"
			bundle="gestioneBibliograficaLabels" />
				<html:checkbox property="interrGener.libretto" />
				<html:hidden property="interrGener.libretto" value="false" />
			</td>

		<td width="80" class="etichetta"><bean:message key="ricerca.tabAntico"
			bundle="gestioneBibliograficaLabels" />
				<html:checkbox	property="interrGener.matAntico" />
				<html:hidden	property="interrGener.matAntico" value="false" />

			</td>

		<td class="etichetta"><html:submit property="methodInterrogTit">
			<bean:message key="ricerca.button.campiSpecifici"
				bundle="gestioneBibliograficaLabels" />
		</html:submit></td>
	</tr>
</table>

<table border="0">
	<tr>
		<td width="130" class="etichetta"><bean:message key="ricerca.genere"
			bundle="gestioneBibliograficaLabels" />:</td>
		<td width="60" class="testoNormale"><html:select
			property="interrGener.genereSelez1" style="width:36px">
			<html:optionsCollection property="interrGener.listaGeneri"
				value="codice" label="descrizioneCodice" />
		</html:select></td>
		<td width="60" class="testoNormale"><html:select
			property="interrGener.genereSelez2" style="width:36px">
			<html:optionsCollection property="interrGener.listaGeneri"
				value="codice" label="descrizioneCodice" />
		</html:select></td>
		<td width="60" class="testoNormale"><html:select
			property="interrGener.genereSelez3" style="width:36px">
			<html:optionsCollection property="interrGener.listaGeneri"
				value="codice" label="descrizioneCodice" />
		</html:select></td>
		<td width="63" class="testoNormale"><html:select
			property="interrGener.genereSelez4" style="width:36px">
			<html:optionsCollection property="interrGener.listaGeneri"
				value="codice" label="descrizioneCodice" />
		</html:select></td>
	</tr>
</table>





