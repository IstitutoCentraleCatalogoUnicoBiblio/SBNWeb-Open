<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area Moderno/Antico
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



<table width="100%" border="0" bgcolor="#FFCC99">
	<tr>
		<td><c:choose>
			<c:when test="${dettaglioTitoloForm.tipoProspettazione eq 'DET' or dettaglioTitoloForm.tipoProspetSpec eq 'DET'}">

				<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'M' or
								dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'W' or
								dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'N'}">

						<table border="0">
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.genereRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloModAntVO.genereRappr" descrizione="descGenereRappr" />
								</td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.annoIRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloModAntVO.annoIRappr" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.periodoIRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloModAntVO.periodoIRappr" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.localitaIRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloModAntVO.localitaIRappr" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.sedeIRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloModAntVO.sedeIRappr" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.occasioneIRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloModAntVO.occasioneIRappr"
									size="50" readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.noteIRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloModAntVO.noteIRappr" size="50"
									readonly="true"></html:text></td>
							</tr>
						</table>

						<c:choose>
							<c:when
								test="${dettaglioTitoloForm.dettTitComVO.detTitoloModAntVO.presenzaPersonaggi eq 'SI'}">
								<table width="100%" border="0">
									<tr>
										<th width="220" class="etichetta"><bean:message
											key="ricerca.musica.Personaggio"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.persPersonaggio"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.persTimbro"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.persInterprete"
											bundle="gestioneBibliograficaLabels" /></th>
									</tr>

									<logic:iterate id="item"
										property="dettTitComVO.detTitoloModAntVO.listaPersonaggi"
										name="dettaglioTitoloForm">
										<tr class="testoNormale">
											<td></td>
											<td bgcolor="#FFCC99"><bean-struts:write name="item"
												property="campoUno" /></td>
											<td bgcolor="#FFCC99"><bean-struts:write name="item"
												property="campoDue" /></td>
											<td bgcolor="#FFCC99"><bean-struts:write name="item"
												property="nota" /></td>
										</tr>
									</logic:iterate>

								</table>
							</c:when>
						</c:choose>
					</c:if>

				</c:when>


				<c:otherwise>

					<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'M' or
								dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'W' or
								dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'N'}">

						<table border="0">
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.genereRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:select
									property="dettTitComVO.detTitoloModAntVO.genereRappr"
									style="width:180px">
									<html:optionsCollection property="listaGenereRappr"
										value="codice" label="descrizioneCodice" />
								</html:select></td>
							</tr>
							<!-- Modifica almaviva2 Settembre 2014 - Mantis 5638
							inseriti controlli lunghezza campi rappresentazione per bloccare il bibliotecario nella fase di Ins/var:
							maxlength anno      5 chr
							maxlength periodo  15 chr
							maxlength località 30 chr
							maxlength sede     30 chr
							 -->
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.annoIRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloModAntVO.annoIRappr" maxlength="5" size="50"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.periodoIRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloModAntVO.periodoIRappr" maxlength="15" size="50"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.localitaIRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloModAntVO.localitaIRappr" maxlength="30" size="50"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.sedeIRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloModAntVO.sedeIRappr" maxlength="30" size="50"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.occasioneIRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloModAntVO.occasioneIRappr"
									size="50"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.noteIRappr"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloModAntVO.noteIRappr" size="50"></html:text></td>
							</tr>
						</table>

						<table width="100%" border="0">
							<tr>
								<th width="220" class="etichetta"><bean:message
									key="ricerca.musica.Personaggio"
									bundle="gestioneBibliograficaLabels" /></th>
								<th class="etichetta" bgcolor="#dde8f0"><bean:message
									key="ricerca.musica.persPersonaggio"
									bundle="gestioneBibliograficaLabels" /></th>
								<th class="etichetta" bgcolor="#dde8f0"><bean:message
									key="ricerca.musica.persTimbro"
									bundle="gestioneBibliograficaLabels" /></th>
								<th class="etichetta" bgcolor="#dde8f0"><bean:message
									key="ricerca.musica.persInterprete"
									bundle="gestioneBibliograficaLabels" /></th>
								<th class="etichetta" bgcolor="#dde8f0">&nbsp; <html:submit
									styleClass="buttonImageDelLine" property="methodDettaglioTit"
									title="Cancella Personaggio">
									<bean:message key="button.canPersonaggio"
										bundle="gestioneBibliograficaLabels" />
								</html:submit> <html:submit styleClass="buttonImageNewLine"
									property="methodDettaglioTit" title="Inserisci Personaggio">
									<bean:message key="button.insPersonaggio"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></th>
							</tr>

							<logic:iterate id="itemPersonaggiModAnt"
								property="dettTitComVO.detTitoloModAntVO.listaPersonaggi"
								name="dettaglioTitoloForm" indexId="idxPersonaggi">
								<tr class="testoNormale">
									<td></td>
									<td bgcolor="#FFCC99"><html:text name="itemPersonaggiModAnt"
										property="campoUno" indexed="true" /></td>
									<td bgcolor="#FFCC99"><html:select name="itemPersonaggiModAnt"
										property="campoDue" style="width:180px" indexed="true">
										<html:optionsCollection property="listaTimbroVocale"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
									<td bgcolor="#FFCC99"><html:text name="itemPersonaggiModAnt"
										property="nota" indexed="true" /></td>
									<td bgcolor="#FFCC99"><html:radio
										property="selezRadioPersonaggio" value="${idxPersonaggi}" /></td>
								</tr>
							</logic:iterate>
						</table>

					</c:if>

				</c:otherwise>
		</c:choose>
</table>
