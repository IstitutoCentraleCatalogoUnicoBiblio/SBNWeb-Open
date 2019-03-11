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

	<sbn:navform action="/gestionebibliografica/sifRicercaElementiOrganico.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" />
		</div>
		<table border="0">
			<tr>
				<td  class="etichetta"><bean:message
					key="sifElOrganico.testRic" bundle="gestioneBibliograficaLabels" />:
				<html:text styleId="testoNormale" property="testoRicerca" size="50"></html:text></td>
				<td align="center"><html:submit property="methodSifElementiOrganico">
					<bean:message key="sifElOrganico.button.cerca"
						bundle="gestioneBibliograficaLabels" />
				</html:submit>
				<td align="center"><html:submit property="methodSifElementiOrganico">
					<bean:message key="sifElOrganico.button.ricarica"
						bundle="gestioneBibliograficaLabels" />
				</html:submit>
			</tr>
		</table>

		<table width="100%" border="0">
			<tr bgcolor="#dde8f0">
				<th class="etichetta"><bean:message
					key="sifElOrganico.codice" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta"><bean:message
					key="sifElOrganico.descr" bundle="gestioneBibliograficaLabels" /></th>
				<th class="etichetta"></th>
			</tr>
			<logic:iterate id="item" property="listaElementiOrg"
				name="sifRicercaElementiOrganicoForm" indexId="riga">
				<sbn:rowcolor var="color" index="riga" />
				<tr class="testoNormale" bgcolor="#FEF1E2">
					<td bgcolor="${color}"><bean-struts:write name="item"
						property="codice" /></td>
					<td bgcolor="${color}"><bean-struts:write name="item"
						property="descrizione" /></td>
					<td bgcolor="${color}"><html:radio property="selezRadio"
						value="${item.codice}" /></td>
				</tr>
			</logic:iterate>
		</table>
		</div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodSifElementiOrganico">
					<bean:message key="sifElOrganico.button.selez"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodSifElementiOrganico">
					<bean:message key="sifElOrganico.button.annul"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
