<!--	// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro -->
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
		<td>
			<c:choose>
				<c:when test="${dettaglioTitoloForm.tipoProspettazione eq 'DET' or dettaglioTitoloForm.tipoProspetSpec eq 'DET'}">
					<table border="0">
						<tr>
							<td width="160" class="etichetta"><bean:message
								key="ricerca.elettr.livAut.specificita"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="400" class="testoNormale"><html:text
								property="dettTitComVO.detTitoloEleVO.livAutSpec" size="20"
								readonly="true"
								title="${dettaglioTitoloForm.descLivAutSpecEle}"></html:text></td>
						</tr>

						<tr>
							<td width="160" class="etichetta"><bean:message
								key="ricerca.elettr.tipoRisorsaElettronica"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="400" class="testoNormale"><html:text
								property="dettTitComVO.detTitoloEleVO.tipoRisorsaElettronica" size="20"
								readonly="true"
								title="${dettaglioTitoloForm.descTipoRisorsaElettronica}"></html:text></td>
						</tr>
						<tr>
							<td width="160" class="etichetta"><bean:message
								key="ricerca.elettr.indicazioneSpecificaMateriale"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="400" class="testoNormale"><html:text
								property="dettTitComVO.detTitoloEleVO.indicazioneSpecificaMateriale" size="20"
								readonly="true"
								title="${dettaglioTitoloForm.descIndicazioneSpecificaMateriale}"></html:text></td>
						</tr>
						<tr>
							<td width="160" class="etichetta"><bean:message
								key="ricerca.elettr.coloreElettronico"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="400" class="testoNormale"><html:text
								property="dettTitComVO.detTitoloEleVO.coloreElettronico" size="20"
								readonly="true"
								title="${dettaglioTitoloForm.descColoreElettronico}"></html:text></td>
						</tr>
						<tr>
							<td width="160" class="etichetta"><bean:message
								key="ricerca.elettr.dimensioniElettronico"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="400" class="testoNormale"><html:text
								property="dettTitComVO.detTitoloEleVO.dimensioniElettronico" size="20"
								readonly="true"
								title="${dettaglioTitoloForm.descDimensioniElettronico}"></html:text></td>
						</tr>
						<tr>
							<td width="160" class="etichetta"><bean:message
								key="ricerca.elettr.suonoElettronico"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="400" class="testoNormale"><html:text
								property="dettTitComVO.detTitoloEleVO.suonoElettronico" size="20"
								readonly="true"
								title="${dettaglioTitoloForm.descSuonoElettronico}"></html:text></td>
						</tr>

					</table>
				</c:when>
				<c:otherwise>
					<table border="0">
						<tr>
							<td width="180" class="etichetta"><bean:message
								key="ricerca.elettr.livAut.specificita"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="400" class="testoNormale"><html:select
								property="dettTitComVO.detTitoloEleVO.livAutSpec" style="width:40px">
								<html:optionsCollection property="listaLivAut" value="codice"
											label="descrizioneCodice" />
							</html:select></td>
						</tr>
						<tr>
							<td width="160" class="etichetta"><bean:message
								key="ricerca.elettr.tipoRisorsaElettronica"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="400" class="testoNormale"><html:select
								property="dettTitComVO.detTitoloEleVO.tipoRisorsaElettronica" style="width:40px">
								<html:optionsCollection property="listaTipoRisorsaElettronica" value="codice"
											label="descrizioneCodice" />
							</html:select></td>
						</tr>
						<tr>
							<td width="160" class="etichetta"><bean:message
								key="ricerca.elettr.indicazioneSpecificaMateriale"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="400" class="testoNormale"><html:select
								property="dettTitComVO.detTitoloEleVO.indicazioneSpecificaMateriale" style="width:40px">
								<html:optionsCollection property="listaIndicazioneSpecificaMateriale" value="codice"
											label="descrizioneCodice" />
							</html:select></td>
						</tr>
						<tr>
							<td width="160" class="etichetta"><bean:message
								key="ricerca.elettr.coloreElettronico"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="400" class="testoNormale"><html:select
								property="dettTitComVO.detTitoloEleVO.coloreElettronico" style="width:40px">
								<html:optionsCollection property="listaColoreElettronico" value="codice"
											label="descrizioneCodice" />
							</html:select></td>
						</tr>
						<tr>
							<td width="160" class="etichetta"><bean:message
								key="ricerca.elettr.dimensioniElettronico"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="400" class="testoNormale"><html:select
								property="dettTitComVO.detTitoloEleVO.dimensioniElettronico" style="width:40px">
								<html:optionsCollection property="listaDimensioniElettronico" value="codice"
											label="descrizioneCodice" />
							</html:select></td>
						</tr>
						<tr>
							<td width="160" class="etichetta"><bean:message
								key="ricerca.elettr.suonoElettronico"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="400" class="testoNormale"><html:select
								property="dettTitComVO.detTitoloEleVO.suonoElettronico" style="width:40px">
								<html:optionsCollection property="listaSuonoElettronico" value="codice"
											label="descrizioneCodice" />
							</html:select></td>
						</tr>
					</table>
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
