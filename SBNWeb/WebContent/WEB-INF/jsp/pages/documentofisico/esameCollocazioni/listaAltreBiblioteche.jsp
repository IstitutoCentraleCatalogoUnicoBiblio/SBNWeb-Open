<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>

<html:xhtml />

<layout:page>
<sbn:errors bundle="documentoFisicoMessages" />
<sbn:navform action="/documentofisico/esameCollocazioni/listaAltreBiblioteche.do">
<html:hidden property="action"/>
<div id="content">
    <table width="100%" border="0" bgcolor="#FEF1E2">
		<tr class="etichetta" bgcolor="#dde8f0">
		    <th style="width:8%"><bean:message
				key="documentofisico.codice"
				bundle="documentoFisicoLabels" /></th>
		    <th><bean:message
				key="documentofisico.bibliotecaT"
				bundle="documentoFisicoLabels" /></th>
			<th style="width:3%">
					<div align="center"></div>
				</th>
        </tr>
		<logic:iterate id="item" property="listaBiblioteche" name="listaAltreBibliotecheForm" indexId="riga">
			<sbn:rowcolor var="color" index="riga" />
			<tr>
				<td bgcolor="${color}"><bean-struts:write
					name="item" property="codice" /></td>
				<td bgcolor="${color}"><bean-struts:write
					name="item" property="descrizione" /></td>
				<td bgcolor="${color}"><html:radio property="selectedBiblio"
					value="${riga}" /></td>
	        </tr>
		</logic:iterate>
     </table>
</div>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
          <table align="center">
             <tr>
              <td>
				<html:submit styleClass="pulsanti" property="methodListaBib">
					<bean:message key="documentofisico.bottone.scegli"
								bundle="documentoFisicoLabels" /></html:submit>
				<html:submit styleClass="pulsanti" property="methodListaBib">
					<bean:message key="documentofisico.bottone.indietro"
								bundle="documentoFisicoLabels" /></html:submit>
			 </td>
			</tr>
		</table>
</div>
</sbn:navform>

</layout:page>
