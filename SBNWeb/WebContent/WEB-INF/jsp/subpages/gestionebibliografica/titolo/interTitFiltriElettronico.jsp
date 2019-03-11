<!-- almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
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


<table width="100%" border="0" bgcolor="#FFCC99">
	<tr>
		<td width="130" class="etichetta"><bean:message
				key="ricerca.elettr.tipoRisorsaElettronica" bundle="gestioneBibliograficaLabels" />
		</td>
		<td width="100" class="testoNormale"><html:select
					property="interrElettronico.tipoRisorsaElettronicaSelez" style="width:100px" onchange="this.form.submit()">
					<html:optionsCollection
						property="interrElettronico.listaTipoRisorsaElettronica" value="codice"
						label="descrizioneCodice" />
				</html:select></td>
	</tr>
	<tr>
		<td width="130" class="etichetta"><bean:message
				key="ricerca.elettr.indicazioneSpecificaMateriale" bundle="gestioneBibliograficaLabels" />
		</td>
		<td width="100" class="testoNormale"><html:select
					property="interrElettronico.indicazioneSpecificaMaterialeSelez" style="width:100px" onchange="this.form.submit()">
					<html:optionsCollection
						property="interrElettronico.listaIndicazioneSpecificaMateriale" value="codice"
						label="descrizioneCodice" />
				</html:select></td>
	</tr>
</table>
