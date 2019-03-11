<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione Autore
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

<html:xhtml/>
<layout:page>

	<sbn:navform action="/gestionebibliografica/sifRicercaRepertori.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" />
		</div>
		<table border="0">
			<tr>
				<td  class="etichetta"><bean:message
					key="sifRepertori.testRic" bundle="gestioneBibliograficaLabels" />:
				<html:text styleId="testoNormale" property="testoRicerca" size="50"></html:text></td>
				<td  class="etichetta"><bean:message
					key="sifRepertori.tipo" bundle="gestioneBibliograficaLabels" /></td>
				<td  class="testoNormale"><html:select
					property="tipoRicercaSelez">
					<html:optionsCollection property="listaTipoRicerca"
						value="descrizione" label="descrizione" />
				</html:select></td>
				<td align="center"><html:submit property="methodSifRepertoriTit">
					<bean:message key="sifRepertori.button.cerca"
						bundle="gestioneBibliograficaLabels" />
				</html:submit>
				<td align="center"><html:submit property="methodSifRepertoriTit">
					<bean:message key="sifRepertori.button.ricarica"
						bundle="gestioneBibliograficaLabels" />
				</html:submit>
			</tr>
		</table>

		<table width="100%" border="0">
			<tr bgcolor="#dde8f0">
				<th class="etichetta"><bean:message
					key="sifRepertori.tipo" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta"><bean:message
					key="sifRepertori.desc" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta"><bean:message
					key="sifRepertori.sigl" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta"></th>
			</tr>
			<logic:iterate id="item" property="listaRepertori"
				name="sifRicercaRepertoriForm" indexId="riga">
				<sbn:rowcolor var="color" index="riga" />
				<tr class="testoNormale" bgcolor="#FEF1E2">
					<td bgcolor="${color}"><bean-struts:write name="item"
						property="tipo" /></td>
					<td bgcolor="${color}"><bean-struts:write name="item"
						property="desc" /></td>
					<td bgcolor="${color}"><bean-struts:write name="item"
						property="sigl" /></td>
					<td bgcolor="${color}"><html:radio property="selezRadio"
						value="${item.sigl}" /></td>
				</tr>
			</logic:iterate>
		</table>
		</div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodSifRepertoriTit">
					<bean:message key="sifRepertori.button.selez"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodSifRepertoriTit">
					<bean:message key="sifRepertori.button.annul"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodSifRepertoriTit">
					<bean:message key="sifRepertori.button.stampa"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
