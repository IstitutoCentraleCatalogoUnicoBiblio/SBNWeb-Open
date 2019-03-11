<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/documentofisico/serieInventariali/serieInventarialeLista.do">
		<html:hidden property="action" />
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<table width="100%" border="0">
			<tr>
				<td colspan="4">
				<div class="etichetta"><bean:message key="documentofisico.bibliotecaT"
					bundle="documentoFisicoLabels" /> <html:text disabled="true" styleId="testoNormale"
					property="codBib" size="5" maxlength="3"></html:text> <span disabled="true"> <html:submit
					title="Lista Biblioteche" styleClass="buttonImageListaSezione" disabled="false"
					property="methodSerieLista">
					<bean:message key="documentofisico.lsBib" bundle="documentoFisicoLabels" />
				</html:submit></span><bean-struts:write name="serieInventarialeListaForm" property="descrBib" /></div>
				</td>
			</tr>
		</table>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe" totBlocchi="totBlocchi"
			elementiPerBlocco="elemPerBlocchi" parameter="methodSerieLista"></sbn:blocchi>
		<table width="100%" border="0">
			<tr bgcolor="#dde8f0">
				<th class="etichetta" scope="col"><bean:message key="documentofisico.prg"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message key="documentofisico.serie"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message key="documentofisico.progrAutom"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message
					key="documentofisico.progrPregress" bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message key="documentofisico.numPartenza"
					bundle="documentoFisicoLabels" /></th>
				<th class="etichetta" scope="col"><bean:message key="documentofisico.descrizione"
					bundle="documentoFisicoLabels" /></th>
				<th>
				<div align="center"></div>
				</th>
			</tr>
			<logic:notEmpty property="listaSerie" name="serieInventarialeListaForm">
				<logic:iterate id="item" property="listaSerie" name="serieInventarialeListaForm"
					indexId="listaIdx">
					<sbn:rowcolor var="color" index="listaIdx" />
					<tr>
						<td bgcolor="${color}" class="testoNormale">
							<sbn:anchor name="item" property="prg" />
							<bean-struts:write name="item" property="prg" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="codSerie" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="progAssInv" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="pregrAssInv" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="numMan" /></td>
						<td bgcolor="${color}" class="testoNormale"><bean-struts:write name="item"
							property="descrSerie" /></td>
						<td bgcolor="${color}" ><html:radio property="selectedSerie"
							value="${listaIdx}" /></td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
		</table>
		</div>
		<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="totRighe" totBlocchi="totBlocchi"
			elementiPerBlocco="elemPerBlocchi" parameter="methodSerieLista" bottom="true"></sbn:blocchi>
		<div id="divFooter"><!-- BOTTONIERA inserire solo i SOLO td -->
		<table align="center">
			<tr>
				<td><sbn:checkAttivita idControllo="df">
					<html:submit styleClass="pulsanti" disabled="false" property="methodSerieLista">
						<bean:message key="documentofisico.bottone.nuova" bundle="documentoFisicoLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodSerieLista"
						disabled="${serieInventarialeListaForm.noSerie}">
						<bean:message key="documentofisico.bottone.modifica" bundle="documentoFisicoLabels" />
					</html:submit>
				</sbn:checkAttivita> <html:submit styleClass="pulsanti" property="methodSerieLista"
					disabled="${serieInventarialeListaForm.noSerie}">
					<bean:message key="documentofisico.bottone.esamina" bundle="documentoFisicoLabels" />
				</html:submit> <%--<html:submit styleClass="pulsanti"
					property="methodSerieLista">
					<bean:message key="documentofisico.bottone.indietro"
						bundle="documentoFisicoLabels" />
				</html:submit>--%></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
