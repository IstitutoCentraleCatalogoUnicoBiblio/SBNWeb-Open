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
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.tipoTestoLetterario" size="5"
					readonly="true" title="${dettaglioTitoloForm.descTipoTestoLetterarioArea0}">
				</html:text></td>
			</tr>
		</table>
	</c:when>
	<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'i'}">
		<table border="0">

			<tr>
				<td width="130"  class="etichetta"><bean:message
					key="gestione.audiov.tipoTestoRegSonora" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100"><html:text
					property="dettTitComVO.detTitoloPFissaVO.tipoTestoRegSonora" size="20"
					readonly="true" title="${dettaglioTitoloForm.descTipoTestoRegSonora}"></html:text></td>
			</tr>
		</table>
	</c:when>
</c:choose>


<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="label.area0.formaContenuto" bundle="gestioneBibliograficaLabels" /></td>
		<td width="70"><html:text
			property="dettTitComVO.detTitoloPFissaVO.formaContenuto" size="5"
			readonly="true" title="${dettaglioTitoloForm.descFormaContenuto}">
		</html:text></td>
		<td width="70" class="etichetta"><bean:message
			key="label.area0.tipoContenuto" bundle="gestioneBibliograficaLabels" /></td>
		<td width="170"><html:text
			property="dettTitComVO.detTitoloPFissaVO.tipoContenuto" size="5"
			readonly="true" title="${dettaglioTitoloForm.descTipoContenuto}">
		</html:text></td>
		<td width="90" class="etichetta"><bean:message
			key="label.area0.movimento" bundle="gestioneBibliograficaLabels" /></td>
		<td width="70"><html:text
			property="dettTitComVO.detTitoloPFissaVO.movimento" size="5"
			readonly="true" title="${dettaglioTitoloForm.descMovimento}">
		</html:text></td>
	</tr>
</table>
<table border="0">
	<tr>
		<td width="100" class="etichetta"><bean:message
			key="label.area0.dimensione" bundle="gestioneBibliograficaLabels" /></td>
		<td width="70"><html:text
			property="dettTitComVO.detTitoloPFissaVO.dimensione" size="5"
			readonly="true" title="${dettaglioTitoloForm.descDimensione}">
		</html:text></td>

		<td width="70" class="etichetta"><bean:message
			key="label.area0.sensorialita" bundle="gestioneBibliograficaLabels" /></td>
		<td width="170">
			<html:text
			property="dettTitComVO.detTitoloPFissaVO.sensorialita1" size="3"
			readonly="true" title="${dettaglioTitoloForm.descSensorialita1}"></html:text>
			<html:text
			property="dettTitComVO.detTitoloPFissaVO.sensorialita2" size="3"
			readonly="true" title="${dettaglioTitoloForm.descSensorialita2}"></html:text>
			<html:text
			property="dettTitComVO.detTitoloPFissaVO.sensorialita3" size="3"
			readonly="true" title="${dettaglioTitoloForm.descSensorialita3}"></html:text>
		</td>
		<td width="90" class="etichetta"><bean:message
			key="label.area0.tipoMediazione" bundle="gestioneBibliograficaLabels" /></td>
		<td width="70"><html:text
			property="dettTitComVO.detTitoloPFissaVO.tipoMediazione" size="5"
			readonly="true" title="${dettaglioTitoloForm.descTipoMediazione}">
		</html:text></td>
	</tr>
</table>

 <!--  evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183) -->
 <!--  evolutive Schema 2.01-ottobre 2015 almaviva2 - Richiesta Novembre la 183 non è presente per natura N -->
<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura != 'N'}">
  <table border="0">
		<tr>
			<td width="100" class="etichetta"><bean:message
				key="label.area0.tipoSupporto" bundle="gestioneBibliograficaLabels" /></td>
			<td width="70"><html:text
				property="dettTitComVO.detTitoloPFissaVO.tipoSupporto" size="5"
				readonly="true" title="${dettaglioTitoloForm.descTipoSupporto}">
			</html:text></td>
		</tr>
	</table>
</c:if>

<c:if test="${not empty dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.formaContenutoBIS }">

	<table border="0">
		<tr>
			<td width="100" class="etichetta"><bean:message
				key="label.area0.formaContenuto" bundle="gestioneBibliograficaLabels" /></td>
			<td width="70"><html:text
				property="dettTitComVO.detTitoloPFissaVO.formaContenutoBIS" size="5"
				readonly="true" title="${dettaglioTitoloForm.descFormaContenutoBIS}">
			</html:text></td>
			<td width="70" class="etichetta"><bean:message
				key="label.area0.tipoContenuto" bundle="gestioneBibliograficaLabels" /></td>
			<td width="170"><html:text
				property="dettTitComVO.detTitoloPFissaVO.tipoContenutoBIS" size="5"
				readonly="true" title="${dettaglioTitoloForm.descTipoContenutoBIS}">
			</html:text></td>
			<td width="90" class="etichetta"><bean:message
				key="label.area0.movimento" bundle="gestioneBibliograficaLabels" /></td>
			<td width="70"><html:text
				property="dettTitComVO.detTitoloPFissaVO.movimentoBIS" size="5"
				readonly="true" title="${dettaglioTitoloForm.descMovimentoBIS}">
			</html:text></td>
		</tr>
	</table>
	<table border="0">
		<tr>
			<td width="100" class="etichetta"><bean:message
				key="label.area0.dimensione" bundle="gestioneBibliograficaLabels" /></td>
			<td width="70"><html:text
				property="dettTitComVO.detTitoloPFissaVO.dimensioneBIS" size="5"
				readonly="true" title="${dettaglioTitoloForm.descDimensioneBIS}">
			</html:text></td>

			<td width="70" class="etichetta"><bean:message
				key="label.area0.sensorialita" bundle="gestioneBibliograficaLabels" /></td>
			<td width="170">
				<html:text
				property="dettTitComVO.detTitoloPFissaVO.sensorialitaBIS1" size="3"
				readonly="true" title="${dettaglioTitoloForm.descSensorialitaBIS1}"></html:text>
				<html:text
				property="dettTitComVO.detTitoloPFissaVO.sensorialitaBIS2" size="3"
				readonly="true" title="${dettaglioTitoloForm.descSensorialitaBIS2}"></html:text>
				<html:text
				property="dettTitComVO.detTitoloPFissaVO.sensorialitaBIS3" size="3"
				readonly="true" title="${dettaglioTitoloForm.descSensorialitaBIS3}"></html:text>
			</td>
			<td width="90" class="etichetta"><bean:message
				key="label.area0.tipoMediazione" bundle="gestioneBibliograficaLabels" /></td>
			<td width="70"><html:text
				property="dettTitComVO.detTitoloPFissaVO.tipoMediazioneBIS" size="5"
				readonly="true" title="${dettaglioTitoloForm.descTipoMediazioneBIS}">
			</html:text></td>
		</tr>
	</table>

  <!--  evolutive Schema 2.01-ottobre 2015 almaviva2 - Inserimento gestione nuovo campo TipoSupporto (etichetta 183) -->
 <!--  evolutive Schema 2.01-ottobre 2015 almaviva2 - Richiesta Novembre la 183 non è presente per natura N -->
<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura != 'N'}">
  <table border="0">
		<tr>
			<td width="100" class="etichetta"><bean:message
				key="label.area0.tipoSupporto" bundle="gestioneBibliograficaLabels" /></td>
			<td width="70"><html:text
				property="dettTitComVO.detTitoloPFissaVO.tipoSupportoBIS" size="5"
				readonly="true" title="${dettaglioTitoloForm.descTipoSupportoBIS}">
			</html:text></td>
		</tr>
	</table>
</c:if>


</c:if>

