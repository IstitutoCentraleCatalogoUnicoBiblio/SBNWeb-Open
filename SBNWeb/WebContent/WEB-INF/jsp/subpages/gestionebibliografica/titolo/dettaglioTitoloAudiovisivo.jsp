<!--	// almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
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
		<td>
			<c:choose>
				<c:when test="${dettaglioTitoloForm.tipoProspettazione eq 'DET' or dettaglioTitoloForm.tipoProspetSpec eq 'DET'}">

					<table border="0">
						<tr>
							<td width="220" class="etichetta"><bean:message
								key="ricerca.audiov.livAut.specificita"
								bundle="gestioneBibliograficaLabels" /></td>
							<td class="testoNormale">
								<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.livAutSpec" descrizione="descLivAutSpecAud" />
							</td>
						</tr>
					</table>



					<c:choose>
						<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'g'}">
							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="gestione.audiov.tipoVideo"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.tipoVideo" descrizione="descTipoVideo" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.lunghezza"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="100" class="testoNormale"><html:text
										property="dettTitComVO.detTitoloAudVO.lunghezza" size="10"
										readonly="true"></html:text></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.indicCol"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.indicatoreColore" descrizione="descIndicatoreColore" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.indicSuo"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.indicatoreSuono" descrizione="descIndicatoreSuono" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.supportoSuo"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.supportoSuono" descrizione="descSupportoSuono" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.larghezzaDimensioni"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.larghezzaDimensioni" descrizione="descLarghezzaDimensioni" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.formaPubblDistr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.formaPubblDistr" descrizione="descFormaPubblDistr" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.tecnicaVideoFilm"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.tecnicaVideoFilm" descrizione="descTecnicaVideoFilm" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.presentImmagMov"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.presentImmagMov" descrizione="descPresentImmagMov" />
									</td>
								</tr>
							</table>
							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.materAccompagn"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.materAccompagn1" descrizione="descMaterAccompagn1" />
									</td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.materAccompagn2" descrizione="descMaterAccompagn2" />
									</td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.materAccompagn3" descrizione="descMaterAccompagn3" />
									</td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.materAccompagn4" descrizione="descMaterAccompagn4" />
									</td>
								</tr>
							</table>
							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.pubblicVideoreg"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.pubblicVideoreg" descrizione="descPubblicVideoreg" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.presentVideoreg"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.presentVideoreg" descrizione="descPresentVideoreg" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.materialeEmulsBase"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.materialeEmulsBase" descrizione="descMaterialeEmulsBase" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.materialeSupportSec"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.materialeSupportSec" descrizione="descMaterialeSupportSec" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.standardTrasmiss"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.standardTrasmiss" descrizione="descStandardTrasmiss" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.versioneAudiovid"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.versioneAudiovid" descrizione="descVersioneAudiovid" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.elementiProd"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.elementiProd" descrizione="descElementiProd" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.specCatColoreFilm"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.specCatColoreFilm" descrizione="descSpecCatColoreFilm" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.emulsionePellic"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.emulsionePellic" descrizione="descEmulsionePellic" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.composPellic"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.composPellic" descrizione="descComposPellic" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.suonoImmagMovimento"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.suonoImmagMovimento" descrizione="descSuonoImmagMovimento" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.tipoPellicStampa"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.tipoPellicStampa" descrizione="descTipoPellicStampa" />
									</td>
								</tr>
							</table>
						</c:when>

						<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'i'
									|| dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'j'}">



							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="gestione.audiov.formaPubblicazioneDisco"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.formaPubblicazioneDisco" descrizione="descFormaPubblicazioneDisco" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="gestione.audiov.velocita"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.velocita" descrizione="descVelocita" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.tipoSuono"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.tipoSuono" descrizione="descTipoSuono" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.larghezzaScanal"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.larghezzaScanal" descrizione="descLarghezzaScanal" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.dimensioni"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.dimensioni" descrizione="descDimensioni" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.larghezzaNastro"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.larghezzaNastro" descrizione="descLarghezzaNastro" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.configurazNastro"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.configurazNastro" descrizione="descConfigurazNastro" />
									</td>
								</tr>
							</table>

							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.materTestAccompagn"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.materTestAccompagn1" descrizione="descMaterTestAccompagn1" />
									</td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.materTestAccompagn2" descrizione="descMaterTestAccompagn2" />
									</td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.materTestAccompagn3" descrizione="descMaterTestAccompagn3" />
									</td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.materTestAccompagn4" descrizione="descMaterTestAccompagn4" />
									</td>

									<!-- Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
 										   14) Nella maschera di dettaglio audiovisivo con tipo record i/j devono essere presenti 4
 										   campi materTestAccompagn e non 6
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.materTestAccompagn5" descrizione="descMaterTestAccompagn5" />
									</td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.materTestAccompagn6" descrizione="descMaterTestAccompagn6" />
									</td>
									-->
								</tr>
							</table>
							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.tecnicaRegistraz"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.tecnicaRegistraz" descrizione="descTecnicaRegistraz" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.specCarattRiprod"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.specCarattRiprod" descrizione="descSpecCarattRiprod" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.datiCodifRegistrazSonore"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.datiCodifRegistrazSonore" descrizione="descDatiCodifRegistrazSonore" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.tipoDiMateriale"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.tipoDiMateriale" descrizione="descTipoDiMateriale" />
									</td>
								</tr>

								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.tipoDiTaglio"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloAudVO.tipoDiTaglio" descrizione="descTipoDiTaglio" />
									</td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.durataRegistraz"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:text
										property="dettTitComVO.detTitoloAudVO.durataRegistraz" size="20"
										readonly="true"></html:text>(hhmmss)</td>
								</tr>
							</table>
						</c:when>
					</c:choose>


<!-- Nel caso di Materiale Audiovisivo si devono controllare prima le autorizzazioni per il Materiale in oggetto
	 se è presente si prosegue; se assente si controlla se l'utente è abilitato almeno alla parte musicale
	 perchè in questo caso si dovrà abilitare alla modifica solo quella parte; -->
					<c:choose>
						<c:when test="${dettaglioTitoloForm.tipoProspetSpecSoloMus eq 'DET'}">
							<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'g'
											 ||	dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'j'}">
								<table border="0">
									<tr>
										<td width="220" class="etichetta"><bean:message key="ricerca.musica.orgSint"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.orgSint" size="50"
											readonly="true"></html:text></td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message key="ricerca.musica.orgAnal"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.orgAnal" size="50"
											readonly="true"></html:text></td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message	key="ricerca.musica.elaborazione"
											bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.tipoElabor" descrizione="descTipoElaborazione" />
									</td>
									</tr>
								</table>
							</c:if>

							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.musica.genereRappr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale">
										<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.genereRappr" descrizione="descGenereRappr" />
									</td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.annoIRappr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.annoIRappr" size="50"
										readonly="true"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.periodoIRappr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.periodoIRappr" size="50"
										readonly="true"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.localitaIRappr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.localitaIRappr" size="50"
										readonly="true"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.sedeIRappr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.sedeIRappr" size="50"
										readonly="true"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.occasioneIRappr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.occasioneIRappr"
										size="50" readonly="true"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.noteIRappr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.noteIRappr" size="50"
										readonly="true"></html:text></td>
								</tr>
							</table>

							<c:choose>
								<c:when
									test="${dettaglioTitoloForm.dettTitComVO.detTitoloMusVO.presenzaPersonaggi eq 'SI'}">
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
											property="dettTitComVO.detTitoloMusVO.listaPersonaggi"
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
						</c:when>
						<c:otherwise>

							<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'g'
									 ||	dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'j'}">

								<table border="0">
									<tr>
										<td width="160" class="etichetta"><bean:message
											key="ricerca.musica.orgSint"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.orgSint" size="50"></html:text>
										<html:submit styleClass="buttonImageHlpRep"
											property="methodDettaglioTit" alt="Cerca Elementi Organico">
											<bean:message key="button.orgSint.hlpElementiOrganico"
												bundle="gestioneBibliograficaLabels" />
										</html:submit></td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.orgAnal"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.orgAnal" size="50"></html:text>
										<html:submit styleClass="buttonImageHlpRep"
											property="methodDettaglioTit" alt="Cerca Elementi Organico">
											<bean:message key="button.orgAnal.hlpElementiOrganico"
												bundle="gestioneBibliograficaLabels" />
										</html:submit></td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.elaborazione"
											bundle="gestioneBibliograficaLabels" /></td>
										<td width="400" class="testoNormale"><html:select
											property="dettTitComVO.detTitoloMusVO.elabor"
											style="width:180px"><html:optionsCollection property="listaElaborazione"
												value="codice" label="descrizioneCodice" />
										</html:select></td>
									</tr>
								</table>
							</c:if>
							<table border="0">
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.genereRappr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.genereRappr"
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
										property="dettTitComVO.detTitoloMusVO.annoIRappr" maxlength="5" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.periodoIRappr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.periodoIRappr" maxlength="15" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.localitaIRappr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.localitaIRappr" maxlength="30" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.sedeIRappr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.sedeIRappr" maxlength="30" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.occasioneIRappr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.occasioneIRappr"
										size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.noteIRappr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.noteIRappr" size="50"></html:text></td>
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

								<logic:iterate id="itemPersonaggi"
									property="dettTitComVO.detTitoloMusVO.listaPersonaggi"
									name="dettaglioTitoloForm" indexId="idxPersonaggi">
									<tr class="testoNormale">
										<td></td>
										<td bgcolor="#FFCC99"><html:text name="itemPersonaggi"
											property="campoUno" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:select name="itemPersonaggi"
											property="campoDue" style="width:180px" indexed="true">
											<html:optionsCollection property="listaTimbroVocale"
												value="codice" label="descrizioneCodice" />
										</html:select></td>
										<td bgcolor="#FFCC99"><html:text name="itemPersonaggi"
											property="nota" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:radio
											property="selezRadioPersonaggio" value="${idxPersonaggi}" /></td>
									</tr>
								</logic:iterate>
							</table>
						</c:otherwise>
					</c:choose>

				</c:when>
				<c:otherwise>
					<table border="0">
						<tr>
							<td width="220" class="etichetta"><bean:message
								key="ricerca.audiov.livAut.specificita"
								bundle="gestioneBibliograficaLabels" /></td>
							<td width="400" class="testoNormale"><html:select
								property="dettTitComVO.detTitoloAudVO.livAutSpec" style="width:180px">
								<html:optionsCollection property="listaLivAut" value="codice" label="descrizioneCodice" />
							</html:select></td>
						</tr>
					</table>

					<c:choose>
						<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'g'}">

							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="gestione.audiov.tipoVideo"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.tipoVideo" style="width:180px">
										<html:optionsCollection property="listaTipoVideo" value="codice" label="descrizioneCodice" /></html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.lunghezza"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="100" class="testoNormale"><html:text
										property="dettTitComVO.detTitoloAudVO.lunghezza" maxlength="3" size="10"></html:text></td>
									</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.indicCol"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.indicatoreColore" style="width:180px">
										<html:optionsCollection property="listaIndicatoreColore" value="codice" label="descrizioneCodice" /></html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.indicSuo"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.indicatoreSuono" style="width:180px">
										<html:optionsCollection property="listaIndicatoreSuono" value="codice" label="descrizioneCodice" /></html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.supportoSuo"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.supportoSuono" style="width:180px">
										<html:optionsCollection property="listaSupportoSuono" value="codice" label="descrizioneCodice" /></html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.larghezzaDimensioni"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.larghezzaDimensioni" style="width:180px">
										<html:optionsCollection property="listaLarghezzaDimensioni" value="codice" label="descrizioneCodice" /></html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.formaPubblDistr"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.formaPubblDistr" style="width:180px">
										<html:optionsCollection property="listaFormaPubblDistr" value="codice" label="descrizioneCodice" /></html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.tecnicaVideoFilm"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.tecnicaVideoFilm" style="width:180px">
										<html:optionsCollection property="listaTecnicaVideoFilm" value="codice" label="descrizioneCodice" /></html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.presentImmagMov"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.presentImmagMov" style="width:180px">
										<html:optionsCollection property="listaPresentImmagMov" value="codice" label="descrizioneCodice" /></html:select></td>
								</tr>
							</table>
							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.materAccompagn"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale" width="98"><html:select
										property="dettTitComVO.detTitoloAudVO.materAccompagn1" style="width:180px">
										<html:optionsCollection property="listaMaterAccompagn" value="codice" label="descrizioneCodice" />
										</html:select></td>
									<td class="testoNormale" width="98"><html:select
										property="dettTitComVO.detTitoloAudVO.materAccompagn2" style="width:180px">
										<html:optionsCollection property="listaMaterAccompagn" value="codice" label="descrizioneCodice" />
										</html:select></td>
									<td class="testoNormale" width="98"><html:select
										property="dettTitComVO.detTitoloAudVO.materAccompagn3" style="width:180px">
										<html:optionsCollection property="listaMaterAccompagn" value="codice" label="descrizioneCodice" />
										</html:select></td>
									<td class="testoNormale" width="98"><html:select
										property="dettTitComVO.detTitoloAudVO.materAccompagn4" style="width:180px">
										<html:optionsCollection property="listaMaterAccompagn" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
							</table>

							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.pubblicVideoreg"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.pubblicVideoreg" style="width:180px">
										<html:optionsCollection property="listaPubblicVideoreg" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.presentVideoreg"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.presentVideoreg" style="width:180px">
										<html:optionsCollection property="listaPresentVideoreg" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.materialeEmulsBase"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.materialeEmulsBase" style="width:180px">
										<html:optionsCollection property="listaMaterialeEmulsBase" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.materialeSupportSec"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.materialeSupportSec" style="width:180px">
										<html:optionsCollection property="listaMaterialeSupportSec" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.standardTrasmiss"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.standardTrasmiss" style="width:180px">
										<html:optionsCollection property="listaStandardTrasmiss" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.versioneAudiovid"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.versioneAudiovid" style="width:180px">
										<html:optionsCollection property="listaVersioneAudiovid" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.elementiProd"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.elementiProd" style="width:180px">
										<html:optionsCollection property="listaElementiProd" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.specCatColoreFilm"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.specCatColoreFilm" style="width:180px">
										<html:optionsCollection property="listaSpecCatColoreFilm" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.emulsionePellic"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.emulsionePellic" style="width:180px">
										<html:optionsCollection property="listaEmulsionePellic" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.composPellic"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.composPellic" style="width:180px">
										<html:optionsCollection property="listaComposPellic" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.suonoImmagMovimento"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.suonoImmagMovimento" style="width:180px">
										<html:optionsCollection property="listaSuonoImmagMovimento" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.tipoPellicStampa"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.tipoPellicStampa" style="width:180px">
										<html:optionsCollection property="listaTipoPellicStampa" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
							</table>
						</c:when>
						<c:when	test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'i'
									|| dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'j'}">


							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="gestione.audiov.formaPubblicazioneDisco"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.formaPubblicazioneDisco" style="width:180px">
										<html:optionsCollection property="listaFormaPubblicazioneDisco" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="gestione.audiov.velocita"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.velocita" style="width:180px">
										<html:optionsCollection property="listaVelocita" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.tipoSuono"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.tipoSuono" style="width:180px">
										<html:optionsCollection property="listaTipoSuono" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.larghezzaScanal"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.larghezzaScanal" style="width:180px">
										<html:optionsCollection property="listaLarghezzaScanal" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.dimensioni"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.dimensioni" style="width:180px">
										<html:optionsCollection property="listaDimensioni" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.larghezzaNastro"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.larghezzaNastro" style="width:180px">
										<html:optionsCollection property="listaLarghezzaNastro" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.configurazNastro"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.configurazNastro" style="width:180px">
										<html:optionsCollection property="listaConfigurazNastro" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
							</table>

							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.materTestAccompagn"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale" width="98"><html:select
										property="dettTitComVO.detTitoloAudVO.materTestAccompagn1" style="width:180px">
										<html:optionsCollection property="listaMaterTestAccompagn" value="codice" label="descrizioneCodice" />
										</html:select></td>
									<td class="testoNormale" width="98"><html:select
										property="dettTitComVO.detTitoloAudVO.materTestAccompagn2" style="width:180px">
										<html:optionsCollection property="listaMaterTestAccompagn" value="codice" label="descrizioneCodice" />
										</html:select></td>
									<td class="testoNormale" width="98"><html:select
										property="dettTitComVO.detTitoloAudVO.materTestAccompagn3" style="width:180px">
										<html:optionsCollection property="listaMaterTestAccompagn" value="codice" label="descrizioneCodice" />
										</html:select></td>
									<td class="testoNormale" width="98"><html:select
										property="dettTitComVO.detTitoloAudVO.materTestAccompagn4" style="width:180px">
										<html:optionsCollection property="listaMaterTestAccompagn" value="codice" label="descrizioneCodice" />
										</html:select></td>

									<!-- Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
 										   14) Nella maschera di dettaglio audiovisivo con tipo record i/j devono essere presenti 4
 										   campi materTestAccompagn e non 6
									<td class="testoNormale" width="98"><html:select
										property="dettTitComVO.detTitoloAudVO.materTestAccompagn5" style="width:180px">
										<html:optionsCollection property="listaMaterTestAccompagn" value="codice" label="descrizioneCodice" />
										</html:select></td>
									<td class="testoNormale" width="98"><html:select
										property="dettTitComVO.detTitoloAudVO.materTestAccompagn6" style="width:180px">
										<html:optionsCollection property="listaMaterTestAccompagn" value="codice" label="descrizioneCodice" />
										</html:select></td>
									-->
								</tr>
							</table>


							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.tecnicaRegistraz"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.tecnicaRegistraz" style="width:180px">
										<html:optionsCollection property="listaTecnicaRegistraz" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.specCarattRiprod"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.specCarattRiprod" style="width:180px">
										<html:optionsCollection property="listaSpecCarattRiprod" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.datiCodifRegistrazSonore"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.datiCodifRegistrazSonore" style="width:180px">
										<html:optionsCollection property="listaDatiCodifRegistrazSonore" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.tipoDiMateriale"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.tipoDiMateriale" style="width:180px">
										<html:optionsCollection property="listaTipoDiMateriale" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.tipoDiTaglio"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloAudVO.tipoDiTaglio" style="width:180px">
										<html:optionsCollection property="listaTipoDiTaglio" value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.audiov.durataRegistraz"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="100" class="testoNormale"><html:text
										property="dettTitComVO.detTitoloAudVO.durataRegistraz" size="10"></html:text>(hhmmss)</td>

								</tr>
							</table>
						</c:when>
					</c:choose>


					<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'g'
							 ||	dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'j'}">

						<table border="0">
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.orgSint"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.orgSint" size="50"></html:text>
								<html:submit styleClass="buttonImageHlpRep"
									property="methodDettaglioTit" alt="Cerca Elementi Organico">
									<bean:message key="button.orgSint.hlpElementiOrganico"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.orgAnal"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.orgAnal" size="50"></html:text>
								<html:submit styleClass="buttonImageHlpRep"
									property="methodDettaglioTit" alt="Cerca Elementi Organico">
									<bean:message key="button.orgAnal.hlpElementiOrganico"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
							</tr>
						</table>
						<table border="0">
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.elaborazione"
									bundle="gestioneBibliograficaLabels" /></td>
								<td width="400" class="testoNormale"><html:select
									property="dettTitComVO.detTitoloMusVO.elabor"
									style="width:180px"><html:optionsCollection property="listaElaborazione"
										value="codice" label="descrizioneCodice" />
								</html:select></td>
							</tr>
						</table>
					</c:if>
					<table border="0">
						<tr>
							<td width="220" class="etichetta"><bean:message
								key="ricerca.musica.genereRappr"
								bundle="gestioneBibliograficaLabels" /></td>
							<td class="testoNormale"><html:select
								property="dettTitComVO.detTitoloMusVO.genereRappr"
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
								property="dettTitComVO.detTitoloMusVO.annoIRappr" maxlength="5" size="50"></html:text></td>
						</tr>
						<tr>
							<td class="etichetta"><bean:message
								key="ricerca.musica.periodoIRappr"
								bundle="gestioneBibliograficaLabels" /></td>
							<td class="testoNormale"><html:text
								property="dettTitComVO.detTitoloMusVO.periodoIRappr" maxlength="15" size="50"></html:text></td>
						</tr>
						<tr>
							<td class="etichetta"><bean:message
								key="ricerca.musica.localitaIRappr"
								bundle="gestioneBibliograficaLabels" /></td>
							<td class="testoNormale"><html:text
								property="dettTitComVO.detTitoloMusVO.localitaIRappr" maxlength="30" size="50"></html:text></td>
						</tr>
						<tr>
							<td class="etichetta"><bean:message
								key="ricerca.musica.sedeIRappr"
								bundle="gestioneBibliograficaLabels" /></td>
							<td class="testoNormale"><html:text
								property="dettTitComVO.detTitoloMusVO.sedeIRappr" maxlength="30" size="50"></html:text></td>
						</tr>
						<tr>
							<td class="etichetta"><bean:message
								key="ricerca.musica.occasioneIRappr"
								bundle="gestioneBibliograficaLabels" /></td>
							<td class="testoNormale"><html:text
								property="dettTitComVO.detTitoloMusVO.occasioneIRappr"
								size="50"></html:text></td>
						</tr>
						<tr>
							<td class="etichetta"><bean:message
								key="ricerca.musica.noteIRappr"
								bundle="gestioneBibliograficaLabels" /></td>
							<td class="testoNormale"><html:text
								property="dettTitComVO.detTitoloMusVO.noteIRappr" size="50"></html:text></td>
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

						<logic:iterate id="itemPersonaggi"
							property="dettTitComVO.detTitoloMusVO.listaPersonaggi"
							name="dettaglioTitoloForm" indexId="idxPersonaggi">
							<tr class="testoNormale">
								<td></td>
								<td bgcolor="#FFCC99"><html:text name="itemPersonaggi"
									property="campoUno" indexed="true" /></td>
								<td bgcolor="#FFCC99"><html:select name="itemPersonaggi"
									property="campoDue" style="width:180px" indexed="true">
									<html:optionsCollection property="listaTimbroVocale"
										value="codice" label="descrizioneCodice" />
								</html:select></td>
								<td bgcolor="#FFCC99"><html:text name="itemPersonaggi"
									property="nota" indexed="true" /></td>
								<td bgcolor="#FFCC99"><html:radio
									property="selezRadioPersonaggio" value="${idxPersonaggi}" /></td>
							</tr>
						</logic:iterate>
					</table>

				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
