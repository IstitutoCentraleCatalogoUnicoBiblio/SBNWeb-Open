<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bs:define id="noinput" value="false" />
<c:choose>
	<c:when test="${statoElaborazioniDifferiteForm.disabilitaTutto}">
		<script type="text/javascript">
		<bs:define id="noinput"  value="true"/>
      </script>
	</c:when>
</c:choose>


<html:xhtml />
<layout:page>
	<sbn:navform
		action="/elaborazioniDifferite/sinteticaElaborazioniDifferite.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<br/>
			<sbn:blocchi numNotizie="totRighe" numBlocco="numBlocco"
				elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
				parameter="methodMap_sinteticaElaborazioniDifferite" />

		<table class="sintetica">

			<tr bgcolor="#dde8f0">
				<th class="etichetta" scope="col" align="center">&nbsp;</th>
				<th class="etichetta" scope="col" align="center"><bean:message
					key="label.sintetica.procedura"
					bundle="elaborazioniDifferiteLabels" /></th>

				<th class="etichetta" style="width: 5%;" scope="col" align="center">
				<bean:message key="label.sintetica.nomeCoda"
					bundle="elaborazioniDifferiteLabels" /></th>
				<th class="etichetta" style="width: 5%;" scope="col" align="center">
				<bean:message key="label.sintetica.idRichiesta"
					bundle="elaborazioniDifferiteLabels" /></th>
				<th class="etichetta" style="width: 5%;" scope="col" align="center">
				<bean:message key="label.sintetica.dataRichiesta"
					bundle="elaborazioniDifferiteLabels" /></th>
				<th class="etichetta" scope="col" align="center"><bean:message
					key="label.sintetica.biblioteca"
					bundle="elaborazioniDifferiteLabels" /></th>
				<th class="etichetta" style="width: 15%;" scope="col" align="center">
				<bean:message key="label.sintetica.stato"
					bundle="elaborazioniDifferiteLabels" /></th>
				<th class="etichetta" style="width: 30%;" scope="col" align="center">
				<bean:message key="label.sintetica.scarica"
					bundle="elaborazioniDifferiteLabels" /></th>
				<th class="etichetta" scope="col" align="center" style="width: 3%;"></th>
			</tr>

			<logic:iterate id="item" property="listaRichieste"
				name="sinteticaElaborazioniDifferiteForm" indexId="riga">

				<sbn:rowcolor var="color" index="riga" />

				<tr class="testoNormale" bgcolor="${color}">
					<td bgcolor="${color}" >
						<sbn:anchor name="item" property="progressivo"/>
						<sbn:linkbutton	index="idRichiesta" name="item" value="progressivo"
							key="button.esamina" bundle="elaborazioniDifferiteLabels"
							title="Esamina" property="richiesteSelez" />
					</td>

					<td align="left"><bs:write name="item"
						property="procedura" />&nbsp;-&nbsp;
						<bs:write name="item"
						property="descrizioneProcedura" /></td>
					<td align="center"><bs:write name="item"
						property="nomeCoda" /></td>
					<td align="center"><bs:write name="item"
						property="idRichiesta" /></td>
					<td align="center"><bs:write name="item"
						property="dataRichiesta" /></td>
					<td align="center"><bs:write name="item"
						property="biblioteca" /></td>
					<td align="center"><bs:write name="item"
						property="stato" /></td>

					<td align="center"><logic:notEmpty name="item"
						property="listaDownload">
						<bs:size id="listaDownloadSize" name="item"
							property="listaDownload" />

						<logic:iterate id="elencaDownload" property="listaDownload"
							name="item" indexId="fileIdx">

							<%--<html:link href="${elencaDownload.linkToDownload}"> --%>
							<html:link page="/downloadBatch.do?FILEID=${elencaDownload.base64Link}">
								<bs:write name="elencaDownload"
									property="nomeFileVisualizzato" />
							</html:link>
							<c:if test="${fileIdx ne (listaDownloadSize - 1) }">,&nbsp;</c:if>
						</logic:iterate>
					</logic:notEmpty></td>



					<td><html:multibox property="richiesteSelez">
						<bs:write name="item" property="idRichiesta" />
					</html:multibox></td>


				</tr>
			</logic:iterate>


		</table>
			<sbn:blocchi numNotizie="totRighe" numBlocco="numBlocco"
				elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"
				parameter="methodMap_sinteticaElaborazioniDifferite" bottom="true" />
		</div>


		<div id="divFooter">
		<table align="center" border="0" style="height: 40px">
			<tr>
				<td align="center"><html:submit styleClass="pulsanti"
					property="methodMap_sinteticaElaborazioniDifferite">
					<bean:message key="button.aggiorna"
						bundle="elaborazioniDifferiteLabels" />
				</html:submit>&nbsp;<html:submit styleClass="pulsanti"
					property="methodMap_sinteticaElaborazioniDifferite">
					<bean:message key="button.esamina"
						bundle="elaborazioniDifferiteLabels" />
				</html:submit>&nbsp;<html:submit styleClass="pulsanti"
					property="methodMap_sinteticaElaborazioniDifferite">
					<bean:message key="button.indietro"
						bundle="elaborazioniDifferiteLabels" />
				</html:submit>
				<html:submit styleClass="buttonSelezTutti"
					property="methodMap_sinteticaElaborazioniDifferite" title="Seleziona tutto">
					<bean:message key="button.selAllTitoli"
						bundle="gestioneBibliograficaLabels" />
				</html:submit>
				<html:submit styleClass="buttonSelezNessuno"
					property="methodMap_sinteticaElaborazioniDifferite" title="Deseleziona tutto">
					<bean:message key="button.deSelAllTitoli"
						bundle="gestioneBibliograficaLabels" />
				</html:submit>
				</td>
			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>
