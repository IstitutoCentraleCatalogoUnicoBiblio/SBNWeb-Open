<!--	SBNWeb - Rifacimento ClientServer
		dettaglioTitoloParteFissaVARIAZIONEnatA
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


	<table border="0">
		<tr>
			<td width="100" class="etichetta"><bean:message
				key="ricerca.livelloAutorita"
				bundle="gestioneBibliograficaLabels" /></td>
			<td class="testoNormale"><html:select
				property="dettTitComVO.detTitoloPFissaVO.livAut"
				style="width:40px">
				<html:optionsCollection property="listaLivAut" value="codice"
					label="descrizioneCodice" />
			</html:select></td>
		</tr>
		<tr>
			<td class="etichetta"><bean:message
				key="catalogazione.areaT.titolo"
				bundle="gestioneBibliograficaLabels" />
			</td>
			<td><html:textarea
				property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo" cols="70" rows="5"></html:textarea>
				<sbn:tastiera limit="1200" property="dettTitComVO.detTitoloPFissaVO.areaTitTitolo" name="dettaglioTitoloForm"></sbn:tastiera>
			</td>
		</tr>
	</table>


