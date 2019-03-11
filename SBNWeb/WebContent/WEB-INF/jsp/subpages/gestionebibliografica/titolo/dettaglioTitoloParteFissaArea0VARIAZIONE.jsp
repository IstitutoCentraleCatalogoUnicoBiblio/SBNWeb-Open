<!--	SBNWeb - Rifacimento ClientServer
		dettaglioTitoloParteFissaArea0DETTAGLIO
		almaviva2 Evolutiva Settembre 2014 per Gestione Area0 - solo Nature M,W e S - Inizio definizione/Gestione Nuovi campi
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


<!-- Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
 	   12) Il campo tipo testo letterario (105 o 140) dovrebbe evidenziarsi soltanto in presenza di tipo reord a/b -->
<!-- Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
		13)  Non troviamo traccia sulle mappe dell'Indicatore testo registrazione sonora (tabella INDT)
			 valido per tipo record 'i'. In Indice T125bis.b_125 v. Parametrizzazioni -->
<c:choose>
	<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'a' ||
				dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'b'}">
		<table border="0">
			<tr>
				<td width="130" class="etichetta"><bean:message
					key="label.area0.tipoTestoLetterario" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:select property="dettTitComVO.detTitoloPFissaVO.tipoTestoLetterario" style="width:55px">
					<html:optionsCollection property="listaTipoTestoLetterarioArea0" value="codice"	label="descrizioneCodice" />
				</html:select></td>
			</tr>
		</table>
	</c:when>
	<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'i'}">
		<table border="0">
			<tr>
				<td width="130" class="etichetta"><bean:message
					key="gestione.audiov.tipoTestoRegSonora" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:select property="dettTitComVO.detTitoloPFissaVO.tipoTestoRegSonora" style="width:40px">
					<html:optionsCollection property="listaTipoTestoRegSonora" value="codice" label="descrizioneCodice" />
				</html:select></td>
			</tr>
		</table>
	</c:when>
</c:choose>

<!-- Parte Uno Obbligatoria -->

<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="label.area0.formaContenuto" bundle="gestioneBibliograficaLabels" /></td>
		<td width="70"><html:select property="dettTitComVO.detTitoloPFissaVO.formaContenuto" style="width:55px">
			<html:optionsCollection property="listaFormaContenuto" value="codice"	label="descrizioneCodice" />
		</html:select></td>
		<td width="70" class="etichetta"><bean:message
			key="label.area0.tipoContenuto" bundle="gestioneBibliograficaLabels" /></td>
		<td width="180"><html:select property="dettTitComVO.detTitoloPFissaVO.tipoContenuto" style="width:55px">
			<html:optionsCollection property="listaTipoContenuto" value="codice"	label="descrizioneCodice" />
		</html:select></td>
		<td width="80" class="etichetta"><bean:message
			key="label.area0.movimento" bundle="gestioneBibliograficaLabels" /></td>
		<td width="70"><html:select property="dettTitComVO.detTitoloPFissaVO.movimento" style="width:55px">
			<html:optionsCollection property="listaMovimento" value="codice"	label="descrizioneCodice" />
		</html:select></td>

		<td><html:submit styleClass="buttonImageNewLine"
					property="methodDettaglioTit" title="Inserisci Area0">
					<bean:message key="button.insArea0"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>

	</tr>
</table>
<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="label.area0.dimensione" bundle="gestioneBibliograficaLabels" /></td>
		<td width="70"><html:select property="dettTitComVO.detTitoloPFissaVO.dimensione" style="width:55px">
			<html:optionsCollection property="listaDimensione" value="codice"	label="descrizioneCodice" />
		</html:select></td>

		<td width="70" class="etichetta"><bean:message
			key="label.area0.sensorialita" bundle="gestioneBibliograficaLabels" /></td>
		<td width="180">
		<html:select property="dettTitComVO.detTitoloPFissaVO.sensorialita1" style="width:55px">
			<html:optionsCollection property="listaSensorialita" value="codice"	label="descrizioneCodice" />
		</html:select>
		<html:select property="dettTitComVO.detTitoloPFissaVO.sensorialita2" style="width:55px">
			<html:optionsCollection property="listaSensorialita" value="codice"	label="descrizioneCodice" />
		</html:select>
		<html:select property="dettTitComVO.detTitoloPFissaVO.sensorialita3" style="width:55px">
			<html:optionsCollection property="listaSensorialita" value="codice"	label="descrizioneCodice" />
		</html:select>

		</td>
		<td width="80" class="etichetta"><bean:message
			key="label.area0.tipoMediazione" bundle="gestioneBibliograficaLabels" /></td>
		<td width="70"><html:select property="dettTitComVO.detTitoloPFissaVO.tipoMediazione" style="width:55px">
			<html:optionsCollection property="listaTipoMediazione" value="codice"	label="descrizioneCodice" />
		</html:select></td>
		<td></td>
	</tr>
</table>

 <!--  evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183) -->
 <!--  evolutive Schema 2.01-ottobre 2015 almaviva2 - Richiesta Novembre la 183 non è presente per natura N -->
<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura != 'N'}">
	<table border="0">
		<tr>
			<td width="100" class="etichetta"><bean:message
				key="label.area0.tipoSupporto" bundle="gestioneBibliograficaLabels" /></td>
			<td width="70"><html:select property="dettTitComVO.detTitoloPFissaVO.tipoSupporto" style="width:55px">
				<html:optionsCollection property="listaTipoSupporto" value="codice"	label="descrizioneCodice" />
			</html:select></td>
		</tr>
	</table>
</c:if>


<!-- Parte Due Facoltativa -->

<c:if test="${not empty dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.formaContenutoBIS
									|| dettaglioTitoloForm.presenzaArea0BIS eq 'SI'}">

	<table border="0">
		<tr>
			<td width="100" class="etichetta"><bean:message
				key="label.area0.formaContenuto" bundle="gestioneBibliograficaLabels" /></td>
			<td width="70"><html:select property="dettTitComVO.detTitoloPFissaVO.formaContenutoBIS" style="width:55px">
				<html:optionsCollection property="listaFormaContenuto" value="codice"	label="descrizioneCodice" />
			</html:select></td>
			<td width="70" class="etichetta"><bean:message
				key="label.area0.tipoContenuto" bundle="gestioneBibliograficaLabels" /></td>
			<td width="180"><html:select property="dettTitComVO.detTitoloPFissaVO.tipoContenutoBIS" style="width:55px">
				<html:optionsCollection property="listaTipoContenuto" value="codice"	label="descrizioneCodice" />
			</html:select></td>
			<td width="80" class="etichetta"><bean:message
				key="label.area0.movimento" bundle="gestioneBibliograficaLabels" /></td>
			<td width="70"><html:select property="dettTitComVO.detTitoloPFissaVO.movimentoBIS" style="width:55px">
				<html:optionsCollection property="listaMovimento" value="codice"	label="descrizioneCodice" />
			</html:select></td>

			<td><html:submit styleClass="buttonImageDelLine"
						property="methodDettaglioTit" title="Cancella Area0">
						<bean:message key="button.canArea0"
							bundle="gestioneBibliograficaLabels" />
					</html:submit></td>

		</tr>
	</table>
	<table border="0">
		<tr>
			<td width="100" class="etichetta"><bean:message
				key="label.area0.dimensione" bundle="gestioneBibliograficaLabels" /></td>
			<td width="70"><html:select property="dettTitComVO.detTitoloPFissaVO.dimensioneBIS" style="width:55px">
				<html:optionsCollection property="listaDimensione" value="codice"	label="descrizioneCodice" />
			</html:select></td>

			<td width="70" class="etichetta"><bean:message
				key="label.area0.sensorialita" bundle="gestioneBibliograficaLabels" /></td>
			<td width="180">
			<html:select property="dettTitComVO.detTitoloPFissaVO.sensorialitaBIS1" style="width:55px">
				<html:optionsCollection property="listaSensorialita" value="codice"	label="descrizioneCodice" />
			</html:select>
			<html:select property="dettTitComVO.detTitoloPFissaVO.sensorialitaBIS2" style="width:55px">
				<html:optionsCollection property="listaSensorialita" value="codice"	label="descrizioneCodice" />
			</html:select>
			<html:select property="dettTitComVO.detTitoloPFissaVO.sensorialitaBIS3" style="width:55px">
				<html:optionsCollection property="listaSensorialita" value="codice"	label="descrizioneCodice" />
			</html:select>

			</td>
			<td width="80" class="etichetta"><bean:message
				key="label.area0.tipoMediazione" bundle="gestioneBibliograficaLabels" /></td>
			<td width="70"><html:select property="dettTitComVO.detTitoloPFissaVO.tipoMediazioneBIS" style="width:55px">
				<html:optionsCollection property="listaTipoMediazione" value="codice"	label="descrizioneCodice" />
			</html:select></td>
			<td></td>
		</tr>
	</table>

 <!--  evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183) -->
  <!--  evolutive Schema 2.01-ottobre 2015 almaviva2 - Richiesta Novembre la 183 non è presente per natura N -->
<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura != 'N'}">
	<table border="0">
		<tr>
			<td width="100" class="etichetta"><bean:message
				key="label.area0.tipoSupporto" bundle="gestioneBibliograficaLabels" /></td>
			<td width="70"><html:select property="dettTitComVO.detTitoloPFissaVO.tipoSupportoBIS" style="width:55px">
				<html:optionsCollection property="listaTipoSupporto" value="codice"	label="descrizioneCodice" />
			</html:select></td>
		</tr>
	</table>
</c:if>


</c:if>