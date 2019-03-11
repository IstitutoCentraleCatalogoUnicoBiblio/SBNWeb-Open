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
		action="/elaborazioniDifferite/dettaglioElaborazioniDifferite.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<br/>
		<table class="sintetica">

			<logic:iterate id="elencaRichieste" property="listaRichieste"
				name="dettaglioElaborazioniDifferiteForm" indexId="idx">

				<sbn:rowcolor var="color" index="idx" />

				<tr bgcolor="#dde8f0">
					<th class="etichetta">Descrizione</th>
					<th class="etichetta">Valore</th>
				</tr>
				<tr bgcolor="${color}">
					<td class="etichetta">#</td>
					<td><bs:write name="elencaRichieste"
						property="progressivo" /></td>
				</tr>
				<tr bgcolor="${color}">
					<td class="etichetta"><bean:message
						key="label.sintetica.procedura"
						bundle="elaborazioniDifferiteLabels" /></td>
					<td><bs:write name="elencaRichieste"
						property="procedura" />&nbsp;-&nbsp;<bs:write name="elencaRichieste"
						property="descrizioneProcedura" />
					</td>
				</tr>
				<tr bgcolor="${color}">
					<td class="etichetta"><bean:message
						key="label.sintetica.nomeCoda"
						bundle="elaborazioniDifferiteLabels" /></td>
					<td><bs:write name="elencaRichieste"
						property="nomeCoda" /></td>
				</tr>
				<tr bgcolor="${color}">
					<td class="etichetta"><bean:message
						key="label.sintetica.idRichiesta"
						bundle="elaborazioniDifferiteLabels" /></td>
					<td><bs:write name="elencaRichieste"
						property="idRichiesta" /></td>
				</tr>
				<tr bgcolor="${color}">
					<td class="etichetta"><bean:message
						key="label.sintetica.dataRichiesta"
						bundle="elaborazioniDifferiteLabels" /></td>
					<td><bs:write name="elencaRichieste"
						property="dataRichiesta" /></td>
				</tr>
				<tr bgcolor="${color}">
					<td class="etichetta"><bean:message
						key="label.sintetica.dataInizioEsecuzione"
						bundle="elaborazioniDifferiteLabels" /></td>
					<td><bs:write name="elencaRichieste"
						property="dataInizioEsecuzione" /></td>
				</tr>
				<tr bgcolor="${color}">
					<td class="etichetta"><bean:message
						key="label.dettaglio.dataFineEsecuzione"
						bundle="elaborazioniDifferiteLabels" /></td>
					<td><bs:write name="elencaRichieste"
						property="dataFineEsecuzione" /></td>
				</tr>
				<tr bgcolor="${color}">
					<td class="etichetta"><bean:message
						key="label.sintetica.biblioteca"
						bundle="elaborazioniDifferiteLabels" /></td>
					<td><bs:write name="elencaRichieste"
						property="biblioteca" />&nbsp;-&nbsp;<bs:write name="elencaRichieste"
						property="descrizioneBiblioteca" /></td>
				</tr>
				<tr bgcolor="${color}">
					<td class="etichetta"><bean:message
						key="label.sintetica.richiedente"
						bundle="elaborazioniDifferiteLabels" /></td>
					<td><bs:write name="elencaRichieste"
						property="cognomeNome" /></td>
				</tr>
				<%--<logic:notEmpty name="elencaRichieste" property="parametri">
					<tr bgcolor="${color}">
						<td class="etichetta"><bean:message
							key="label.dettaglio.valoriDiRicerca"
							bundle="elaborazioniDifferiteLabels" /></td>
						<td><bs:write name="elencaRichieste"
							property="parametri" /></td>
					</tr>
				</logic:notEmpty>--%>

				<tr bgcolor="${color}">
					<td class="etichetta"><bean:message
						key="label.sintetica.stato" bundle="elaborazioniDifferiteLabels" />
					</td>
					<td><bs:write name="elencaRichieste"
						property="stato" /></td>
				</tr>
				<tr bgcolor="${color}">
					<td class="etichetta"><bean:message
						key="label.sintetica.scarica" bundle="elaborazioniDifferiteLabels" />
					</td>

					<td align="left"><bs:size id="listaDownloadSize"
						name="elencaRichieste" property="listaDownload" /> <logic:iterate
						id="itemDownload" property="listaDownload"
						name="elencaRichieste" indexId="fileIdx">
						<c:choose>
							<c:when test="${!dettaglioElaborazioniDifferiteForm.conferma}">
								<html:link
									page="/downloadBatch.do?FILEID=${itemDownload.base64Link}">
									<bs:write name="itemDownload"
										property="nomeFileVisualizzato" />
								</html:link>
								&#40;<bs:write name="itemDownload" property="readableSize" />&#41;
								<c:if test="${fileIdx ne (listaDownloadSize - 1) }">,&nbsp;</c:if>
							</c:when>
							<c:otherwise>
								<bs:write name="itemDownload"
									property="nomeFileVisualizzato" />
								&#40;<bs:write name="itemDownload" property="readableSize" />&#41;
								<c:if test="${fileIdx ne (listaDownloadSize - 1) }">,&nbsp;</c:if>
							</c:otherwise>
						</c:choose>

					</logic:iterate></td>

				</tr>

				<tr bgcolor="${color}">
					<td class="etichetta"><bean:message
						key="label.dettaglio.elimina" bundle="elaborazioniDifferiteLabels" />
					</td>
					<td><html:multibox property="richiesteSelez" disabled="${dettaglioElaborazioniDifferiteForm.conferma}">
						<bs:write name="elencaRichieste" property="idRichiesta" />
					</html:multibox></td>
				</tr>

			</logic:iterate>
		</table>

		</div>


		<div id="divFooter">
		<table align="center" border="0" style="height: 40px">
			<tr>
				<td align="center">
					<sbn:bottoniera buttons="pulsanti" />
					<html:submit styleClass="buttonSelezTutti"
					property="methodMap_dettaglioElaborazioniDifferite" title="Seleziona tutto">
					<bean:message key="button.selAllTitoli"
						bundle="gestioneBibliograficaLabels" />
				</html:submit>
				<html:submit styleClass="buttonSelezNessuno"
					property="methodMap_dettaglioElaborazioniDifferite" title="Deseleziona tutto">
					<bean:message key="button.deSelAllTitoli"
						bundle="gestioneBibliograficaLabels" />
				</html:submit>
				</td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
