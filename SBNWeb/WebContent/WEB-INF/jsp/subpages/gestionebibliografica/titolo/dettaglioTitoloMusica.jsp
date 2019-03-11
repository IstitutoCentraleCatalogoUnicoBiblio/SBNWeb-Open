<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area Musica
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

<!-- Modifica almaviva2 23.11.2009 - Mantis 3362 - inserito if su natura ( su UM -A MUS- non c'è il liv.aut per la specificita) -->
				<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura ne 'A'}">
					<table border="0">
						<tr>
							<td width="220" class="etichetta"><bean:message
								key="ricerca.musica.livAut.specificita"
								bundle="gestioneBibliograficaLabels" /></td>
							<td class="testoNormale">
								<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.livAutSpec" descrizione="descLivAutSpecMus" />
							</td>
						</tr>
					</table>
				</c:if>


				<c:choose>
					<c:when
						test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'A'}">
						<table border="0">
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.titOrdinam"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.titOrdinam" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.titEstratto"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.titEstratto" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.appellativo"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.appellativo" size="50"
									readonly="true"></html:text></td>
							</tr>
						</table>

						<table border="0">
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.formaMusicale"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.formaMusic1" descrizione="descFormaMusicale1" />
								</td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.formaMusic2" descrizione="descFormaMusicale2" />
								</td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.formaMusic3" descrizione="descFormaMusicale3" />
								</td>
							</tr>


						</table>
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
								<td class="etichetta"><bean:message
									key="ricerca.musica.numOpera"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.numOpera" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.numOrdine"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.numOrdine" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.numCatTem"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.numCatTem" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.datazioni" bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.datazione" size="50"
									readonly="true"></html:text></td>
							</tr>

							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.tonalita"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.tonalita" descrizione="descTonalita" />
								</td>
							</tr>

							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.sezioni" bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.sezioni" size="50"
									readonly="true"></html:text></td>
							</tr>

						</table>

					</c:when>
					<c:when
						test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'M'}">

						<table border="0">
						<!--  Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
						 12) e 13) Il campo viene sostituito dal tipotestoletterario nella AreaDatiFissi
							<tr>
								<td width="160"  class="etichetta"><bean:message
									key="ricerca.musica.tipoTesto"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.tipoTesto" size="50"
									readonly="true"></html:text></td>
							</tr>
							 -->
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.elaborazione"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.elabor" descrizione="descElaborazione" />
								</td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message key="ricerca.musica.orgSint"
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
								<td class="etichetta"><bean:message
									key="ricerca.musica.presentazione"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.present" descrizione="descPresentazione" />
								</td>
							</tr>
						</table>

						<c:choose>
							<c:when
								test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'd'}">
								<table border="0">
									<tr>
										<td width="220" class="etichetta"><bean:message
											key="ricerca.musica.stesura"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale">
											<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.stesura" descrizione="descStesura" />
										</td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.composito"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale">
											<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.composito" descrizione="descComposito" />
										</td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.palinsesto"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale">
											<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.palinsesto" descrizione="descPalinsesto" />
										</td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.datazioni" bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.datazione" size="50"
											readonly="true"></html:text></td>
									</tr>

								<!-- Bug mantis esercizio 5660 - La gestione del campo Materia deve essere fatta con controlli su tavella codici MAMU
								 perchè non è un testo libero (Materiale Musicale manoscritto) -->
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.materia"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale">
											<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.materia" descrizione="descMateria" />
										</td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.illustrazioni"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.illustrazioni"
											size="50" readonly="true"></html:text></td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.notazioneMusicale"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.notazioneMusicale"
											size="50" readonly="true"></html:text></td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.legatura"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.legatura" size="50"
											readonly="true"></html:text></td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.conservazione"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.conservazione"
											size="50" readonly="true"></html:text></td>
									</tr>
								</table>
							</c:when>
						</c:choose>

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

						<c:choose>
							<c:when
								test="${dettaglioTitoloForm.dettTitComVO.detTitoloMusVO.presenzaIncipit eq 'SI'}">
								<table width="100%" border="0">
									<tr>
										<th width="220" class="etichetta"><bean:message
											key="ricerca.musica.Incipit"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.IncipitNMov"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.IncipitNProg"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.IncipitCont"
											bundle="gestioneBibliograficaLabels" /></th>
									</tr>

									<logic:iterate id="item"
										property="dettTitComVO.detTitoloMusVO.listaIncipit"
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

					<c:when
						test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'W'}">

						<table border="0">
						<!--  Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
						 12) e 13) Il campo viene sostituito dal tipotestoletterario nella AreaDatiFissi
							<tr>
								<td width="160"  class="etichetta"><bean:message
									key="ricerca.musica.tipoTesto"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.tipoTesto" size="50"
									readonly="true"></html:text></td>
							</tr>
							-->
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.elaborazione"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.elabor" descrizione="descElaborazione" />
								</td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message key="ricerca.musica.orgSint"
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
								<td class="etichetta"><bean:message
									key="ricerca.musica.presentazione"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.present" descrizione="descPresentazione" />
								</td>
							</tr>
						</table>

						<c:choose>
							<c:when
								test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'd'}">
								<table border="0">
									<tr>
										<td width="220" class="etichetta"><bean:message
											key="ricerca.musica.stesura"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale">
											<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.stesura" descrizione="descStesura" />
										</td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.composito"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale">
											<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.composito" descrizione="descComposito" />
										</td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.palinsesto"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale">
											<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.palinsesto" descrizione="descPalinsesto" />
										</td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.datazioni" bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.datazione" size="15"
											readonly="true"></html:text></td>
									</tr>

									<!-- Bug mantis esercizio 5660 - La gestione del campo Materia deve essere fatta con controlli su tavella codici MAMU
								 	perchè non è un testo libero (Materiale Musicale manoscritto) -->
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.materia"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale">
											<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.materia" descrizione="descMateria" />
										</td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.illustrazioni"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.illustrazioni"
											size="50" readonly="true"></html:text></td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.notazioneMusicale"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.notazioneMusicale"
											size="50" readonly="true"></html:text></td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.legatura"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.legatura" size="50"
											readonly="true"></html:text></td>
									</tr>
									<tr>
										<td class="etichetta"><bean:message
											key="ricerca.musica.conservazione"
											bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:text
											property="dettTitComVO.detTitoloMusVO.conservazione"
											size="50" readonly="true"></html:text></td>
									</tr>
								</table>
							</c:when>
						</c:choose>

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

						<c:choose>
							<c:when
								test="${dettaglioTitoloForm.dettTitComVO.detTitoloMusVO.presenzaIncipit eq 'SI'}">
								<table width="100%" border="0">
									<tr>
										<th width="220" class="etichetta"><bean:message
											key="ricerca.musica.Incipit"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.IncipitNMov"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.IncipitNProg"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.IncipitCont"
											bundle="gestioneBibliograficaLabels" /></th>
									</tr>

									<logic:iterate id="item"
										property="dettTitComVO.detTitoloMusVO.listaIncipit"
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



					<c:when
						test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'N'}">
						<table border="0">
						<!--  Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
						 12) e 13) Il campo viene sostituito dal tipotestoletterario nella AreaDatiFissi
							<tr>
								<td width="160" class="etichetta"><bean:message
									key="ricerca.musica.tipoTesto"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.tipoTesto" size="50"
									readonly="true"></html:text></td>
							</tr>
							-->
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.elaborazione"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.elabor" descrizione="descElaborazione" />
								</td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message key="ricerca.musica.orgSint"
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
								<td class="etichetta"><bean:message
									key="ricerca.musica.presentazione"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.present" descrizione="descPresentazione" />
								</td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
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

						<c:choose>
							<c:when
								test="${dettaglioTitoloForm.dettTitComVO.detTitoloMusVO.presenzaIncipit eq 'SI'}">
								<table width="100%" border="0">
									<tr>
										<th width="220" class="etichetta"><bean:message
											key="ricerca.musica.Incipit"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.IncipitNMov"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.IncipitNProg"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.IncipitCont"
											bundle="gestioneBibliograficaLabels" /></th>
									</tr>

									<logic:iterate id="item"
										property="dettTitComVO.detTitoloMusVO.listaIncipit"
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
						<table border="0">
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.illustrazioni"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.illustrazioni" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.notazioneMusicale"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.notazioneMusicale"
									size="50" readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.legatura"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.legatura" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.conservazione"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.conservazione" size="50"
									readonly="true"></html:text></td>
							</tr>

							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.presentazione"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.present" descrizione="descPresentazione" />
								</td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
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

						<c:choose>
							<c:when
								test="${dettaglioTitoloForm.dettTitComVO.detTitoloMusVO.presenzaIncipit eq 'SI'}">
								<table width="100%" border="0">
									<tr>
										<th width="220" class="etichetta"><bean:message
											key="ricerca.musica.Incipit"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.IncipitNMov"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.IncipitNProg"
											bundle="gestioneBibliograficaLabels" /></th>
										<th class="etichetta" bgcolor="#dde8f0"><bean:message
											key="ricerca.musica.IncipitCont"
											bundle="gestioneBibliograficaLabels" /></th>
									</tr>

									<logic:iterate id="item"
										property="dettTitComVO.detTitoloMusVO.listaIncipit"
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

						<table border="0">
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.formaMusicale"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.formaMusic1" descrizione="descFormaMusicale1" />
								</td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.formaMusic2" descrizione="descFormaMusicale2" />
								</td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.formaMusic3" descrizione="descFormaMusicale3" />
								</td>
							</tr>
						</table>

						<table border="0">

							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.tonalita"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.tonalita" descrizione="descTonalita" />
								</td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.datazioni" bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.datazione" size="15"
									readonly="true"></html:text></td>
							</tr>

							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.sezioni" bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.sezioni" size="15"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.numOpera"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.numOpera" size="20"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.numOrdine"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.numOrdine" size="20"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.numCatTem"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.numCatTem" size="20"
									readonly="true"></html:text></td>
							</tr>
						</table>

						<table border="0">
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.dataComp"
									bundle="gestioneBibliograficaLabels" /> <bean:message
									key="ricerca.inizio" bundle="gestioneBibliograficaLabels" /></td>
								<td width="150" class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.dataCompIni" size="10"
									readonly="true"></html:text></td>
								<td width="50" class="etichetta"><bean:message
									key="ricerca.fine" bundle="gestioneBibliograficaLabels" /></td>
								<td width="150" class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.dataCompFin" size="10"
									readonly="true"></html:text></td>
							</tr>
						</table>

						<table border="0">
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.orgSintComp"
									bundle="gestioneBibliograficaLabels" /></td>
								<td width="400" class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.orgSintComp" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.orgAnalComp"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.orgAnalComp" size="50"
									readonly="true"></html:text></td>
							</tr>

							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.titOrdinam"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.titOrdinam" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.titEstratto"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.titEstratto" size="50"
									readonly="true"></html:text></td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.appellativo"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.appellativo" size="50"
									readonly="true"></html:text></td>
							</tr>

							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.tipoTesto"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale"><html:text
									property="dettTitComVO.detTitoloMusVO.tipoTesto" size="50"
									readonly="true"></html:text></td>
							</tr>

							<tr>
								<td class="etichetta"><bean:message key="ricerca.musica.stesura"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.stesura" descrizione="descStesura" />
								</td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.composito"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.composito" descrizione="descComposito" />
								</td>
							</tr>
							<tr>
								<td class="etichetta"><bean:message
									key="ricerca.musica.palinsesto"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.palinsesto" descrizione="descPalinsesto" />
								</td>
							</tr>

							<!-- Bug mantis esercizio 5660 - La gestione del campo Materia deve essere fatta con controlli su tavella codici MAMU
								 	perchè non è un testo libero (Materiale Musicale manoscritto) -->
							<tr>
								<td class="etichetta"><bean:message key="ricerca.musica.materia"
									bundle="gestioneBibliograficaLabels" /></td>
								<td class="testoNormale">
									<layout:TextCodDesc codice="dettTitComVO.detTitoloMusVO.materia" descrizione="descMateria" />
								</td>
							</tr>
						</table>
					</c:otherwise>
				</c:choose>
				</c:when>


				<c:otherwise>
<!-- Modifica almaviva2 23.11.2009 - Mantis 3362 - inserito if su natura ( su UM -A MUS- non c'è il liv.aut per la specificita) -->
					<c:if test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura ne 'A'}">
						<table border="0">
							<tr>
								<td width="220" class="etichetta"><bean:message
									key="ricerca.musica.livAut.specificita"
									bundle="gestioneBibliograficaLabels" /></td>
								<td width="400" class="testoNormale"><html:select
									property="dettTitComVO.detTitoloMusVO.livAutSpec"
									style="width:180px">
									<html:optionsCollection property="listaLivAut" value="codice"
										label="descrizioneCodice" />
								</html:select></td>
							</tr>
						</table>
					</c:if>

					<c:choose>
						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'A'}">
							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.musica.titOrdinam"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.titOrdinam" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.titEstratto"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.titEstratto" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.appellativo"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.appellativo" size="50"></html:text></td>
								</tr>
							</table>

							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.musica.formaMusicale"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="100" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.formaMusic1"
										style="width:180px">
										<html:optionsCollection property="listaFormaMusicale"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
									<td width="100" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.formaMusic2"
										style="width:180px">
										<html:optionsCollection property="listaFormaMusicale"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
									<td width="100" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.formaMusic3"
										style="width:180px">
										<html:optionsCollection property="listaFormaMusicale"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
							</table>
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
										key="ricerca.musica.numOpera"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.numOpera" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.numOrdine"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.numOrdine" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.numCatTem"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.numCatTem" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.datazioni" bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.datazione" maxlength="10" size="50"></html:text></td>
								</tr>
								<tr>
									<td  class="etichetta"><bean:message
										key="ricerca.musica.tonalita"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="100" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.tonalita"
										style="width:180px">
										<html:optionsCollection property="listaTonalita"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.sezioni" bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.sezioni" size="50"></html:text></td>
								</tr>
							</table>

							<table align="center">
								<tr>
									<td align="center"><html:submit property="methodDettaglioTit">
										<bean:message key="button.calcolaISBD"	bundle="gestioneBibliograficaLabels" />
										</html:submit></td>
								</tr>
							</table>
						</c:when>


						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'M'}">

							<table border="0">
							<!--  Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
						 12) e 13) Il campo viene sostituito dal tipotestoletterario nella AreaDatiFissi
								<tr>
									<td width="160" class="etichetta"><bean:message
										key="ricerca.musica.tipoTesto"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="100" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.tipoTesto"
										style="width:180px">
										<html:optionsCollection property="listaTipoTestoLetterario"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
								-->
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.musica.elaborazione"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.elabor"
										style="width:180px">
										<html:optionsCollection property="listaElaborazione"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
							</table>
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
										key="gestione.musica.presentazione"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.present"
										style="width:180px">
										<html:optionsCollection property="listaPresentazione"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
							</table>

							<c:choose>
								<c:when
									test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'd'}">
									<table border="0">
										<tr>
											<td width="220" class="etichetta"><bean:message
												key="ricerca.musica.stesura"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:select
												property="dettTitComVO.detTitoloMusVO.stesura"
												style="width:180px">
												<html:optionsCollection property="listaStesura"
													value="codice" label="descrizioneCodice" />
											</html:select></td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.composito"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.composito" size="50"></html:text></td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.palinsesto"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.palinsesto" size="50"></html:text></td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.datazioni" bundle="gestioneBibliograficaLabels" /></td>
											<td width="400" class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.datazione" maxlength="10" size="50"></html:text></td>
										</tr>

										<!-- Bug mantis esercizio 5660 - La gestione del campo Materia deve essere fatta con controlli su tavella codici MAMU
								 			perchè non è un testo libero (Materiale Musicale manoscritto) -->
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.materia"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:select
												property="dettTitComVO.detTitoloMusVO.materia"
												style="width:180px">
												<html:optionsCollection property="listaMateria"
													value="codice" label="descrizioneCodice" />
											</html:select></td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.illustrazioni"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.illustrazioni"
												size="50"></html:text></td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.notazioneMusicale"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.notazioneMusicale"
												size="50"></html:text></td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.legatura"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.legatura" size="50"></html:text></td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.conservazione"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.conservazione"
												size="50"></html:text></td>
										</tr>

									</table>
								</c:when>
							</c:choose>
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

							<table width="100%" border="0">
								<tr>
									<th width="220" class="etichetta"><bean:message
										key="ricerca.musica.Incipit"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0"><bean:message
										key="ricerca.musica.IncipitNMov"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0"><bean:message
										key="ricerca.musica.IncipitNProg"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0"><bean:message
										key="ricerca.musica.IncipitCont"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0">&nbsp; <html:submit
										styleClass="buttonImageDelLine" property="methodDettaglioTit"
										title="Cancella Incipit">
										<bean:message key="button.canIncipit"
											bundle="gestioneBibliograficaLabels" />
									</html:submit> <html:submit styleClass="buttonImageNewLine"
										property="methodDettaglioTit" title="Inserisci Incipit">
										<bean:message key="button.insIncipit"
											bundle="gestioneBibliograficaLabels" />
									</html:submit> <html:submit styleClass="buttonImageModLine"
										property="methodDettaglioTit" title="Modifica Incipit">
										<bean:message key="button.modIncipit"
											bundle="gestioneBibliograficaLabels" />
									</html:submit></th>
								</tr>

								<logic:iterate id="itemIncipit"
									property="dettTitComVO.detTitoloMusVO.listaIncipit"
									name="dettaglioTitoloForm" indexId="idxIncipit">
									<tr class="testoNormale">
										<td></td>
										<td bgcolor="#FFCC99"><html:text name="itemIncipit"
											property="campoUno" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:text name="itemIncipit"
											property="campoDue" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:text name="itemIncipit"
											property="nota" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:radio property="selezRadioIncipit"
											value="${idxIncipit}" /></td>
									</tr>
								</logic:iterate>
							</table>
						</c:when>

						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'N'}">

							<table border="0">
							<!--  Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
						 12) e 13) Il campo viene sostituito dal tipotestoletterario nella AreaDatiFissi
								<tr>
									<td width="160" class="etichetta"><bean:message
										key="ricerca.musica.tipoTesto"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.tipoTesto"
										style="width:180px">
										<html:optionsCollection property="listaTipoTestoLetterario"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
								-->
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.musica.elaborazione"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.elabor"
										style="width:180px">
										<html:optionsCollection property="listaElaborazione"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
							</table>
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
										key="gestione.musica.presentazione"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.present"
										style="width:180px">
										<html:optionsCollection property="listaPresentazione"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
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
								Inseriti controlli lunghezza campi rappresentazione per bloccare il bibliotecario nella fase di Ins/var:
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

							<table width="100%" border="0">
								<tr>
									<th width="220" class="etichetta"><bean:message
										key="ricerca.musica.Incipit"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0"><bean:message
										key="ricerca.musica.IncipitNMov"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0"><bean:message
										key="ricerca.musica.IncipitNProg"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0"><bean:message
										key="ricerca.musica.IncipitCont"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0">&nbsp; <html:submit
										styleClass="buttonImageDelLine" property="methodDettaglioTit"
										title="Cancella Incipit">
										<bean:message key="button.canIncipit"
											bundle="gestioneBibliograficaLabels" />
									</html:submit> <html:submit styleClass="buttonImageNewLine"
										property="methodDettaglioTit" title="Inserisci Incipit">
										<bean:message key="button.insIncipit"
											bundle="gestioneBibliograficaLabels" />
									</html:submit> <html:submit styleClass="buttonImageModLine"
										property="methodDettaglioTit" title="Modifica Incipit">
										<bean:message key="button.modIncipit"
											bundle="gestioneBibliograficaLabels" />
									</html:submit></th>
								</tr>

								<logic:iterate id="itemIncipit"
									property="dettTitComVO.detTitoloMusVO.listaIncipit"
									name="dettaglioTitoloForm" indexId="idxIncipit">
									<tr class="testoNormale">
										<td></td>
										<td bgcolor="#FFCC99"><html:text name="itemIncipit"
											property="campoUno" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:text name="itemIncipit"
											property="campoDue" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:text name="itemIncipit"
											property="nota" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:radio property="selezRadioIncipit"
											value="${idxIncipit}" /></td>
									</tr>
								</logic:iterate>
							</table>
						</c:when>



						<c:when
							test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.natura eq 'W'}">

							<table border="0">
							<!--  Inizio Modifica gennaio 2015 - Vedi Doc test SbnWEB audiovisivi di Carla Scognamiglio
							 12) e 13) Il campo viene sostituito dal tipotestoletterario nella AreaDatiFissi
								<tr>
									<td width="160" class="etichetta"><bean:message
										key="ricerca.musica.tipoTesto"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="100" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.tipoTesto"
										style="width:180px">
										<html:optionsCollection property="listaTipoTestoLetterario"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
								-->
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.musica.elaborazione"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.elabor"
										style="width:180px">
										<html:optionsCollection property="listaElaborazione"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
							</table>
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
										key="gestione.musica.presentazione"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.present"
										style="width:180px">
										<html:optionsCollection property="listaPresentazione"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
							</table>

							<c:choose>
								<c:when
									test="${dettaglioTitoloForm.dettTitComVO.detTitoloPFissaVO.tipoRec eq 'd'}">
									<table border="0">
										<tr>
											<td width="220" class="etichetta"><bean:message
												key="ricerca.musica.stesura"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:select
												property="dettTitComVO.detTitoloMusVO.stesura"
												style="width:180px">
												<html:optionsCollection property="listaStesura"
													value="codice" label="descrizioneCodice" />
											</html:select></td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.composito"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.composito" size="50"></html:text></td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.palinsesto"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.palinsesto" size="50"></html:text></td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.datazioni" bundle="gestioneBibliograficaLabels" /></td>
											<td width="400" class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.datazione" maxlength="10" size="50"></html:text></td>
										</tr>

										<!-- Bug mantis esercizio 5660 - La gestione del campo Materia deve essere fatta con controlli su tavella codici MAMU
								 			perchè non è un testo libero (Materiale Musicale manoscritto) -->
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.materia"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:select
												property="dettTitComVO.detTitoloMusVO.materia"
												style="width:180px">
												<html:optionsCollection property="listaMateria"
													value="codice" label="descrizioneCodice" />
											</html:select></td>
										</tr>

										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.illustrazioni"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.illustrazioni"
												size="50"></html:text></td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.notazioneMusicale"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.notazioneMusicale"
												size="50"></html:text></td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.legatura"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.legatura" size="50"></html:text></td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
												key="ricerca.musica.conservazione"
												bundle="gestioneBibliograficaLabels" /></td>
											<td class="testoNormale"><html:text
												property="dettTitComVO.detTitoloMusVO.conservazione"
												size="50"></html:text></td>
										</tr>

									</table>
								</c:when>
							</c:choose>
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
								Inseriti controlli lunghezza campi rappresentazione per bloccare il bibliotecario nella fase di Ins/var:
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

							<table width="100%" border="0">
								<tr>
									<th width="220" class="etichetta"><bean:message
										key="ricerca.musica.Incipit"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0"><bean:message
										key="ricerca.musica.IncipitNMov"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0"><bean:message
										key="ricerca.musica.IncipitNProg"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0"><bean:message
										key="ricerca.musica.IncipitCont"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0">&nbsp; <html:submit
										styleClass="buttonImageDelLine" property="methodDettaglioTit"
										title="Cancella Incipit">
										<bean:message key="button.canIncipit"
											bundle="gestioneBibliograficaLabels" />
									</html:submit> <html:submit styleClass="buttonImageNewLine"
										property="methodDettaglioTit" title="Inserisci Incipit">
										<bean:message key="button.insIncipit"
											bundle="gestioneBibliograficaLabels" />
									</html:submit> <html:submit styleClass="buttonImageModLine"
										property="methodDettaglioTit" title="Modifica Incipit">
										<bean:message key="button.modIncipit"
											bundle="gestioneBibliograficaLabels" />
									</html:submit></th>
								</tr>

								<logic:iterate id="itemIncipit"
									property="dettTitComVO.detTitoloMusVO.listaIncipit"
									name="dettaglioTitoloForm" indexId="idxIncipit">
									<tr class="testoNormale">
										<td></td>
										<td bgcolor="#FFCC99"><html:text name="itemIncipit"
											property="campoUno" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:text name="itemIncipit"
											property="campoDue" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:text name="itemIncipit"
											property="nota" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:radio property="selezRadioIncipit"
											value="${idxIncipit}" /></td>
									</tr>
								</logic:iterate>
							</table>
						</c:when>

						<c:otherwise>
							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.musica.illustrazioni"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.illustrazioni" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.notazioneMusicale"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.notazioneMusicale"
										size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.legatura"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.legatura" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.conservazione"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.conservazione" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="gestione.musica.presentazione"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.present"
										style="width:180px">
										<html:optionsCollection property="listaPresentazione"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
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
								Inseriti controlli lunghezza campi rappresentazione per bloccare il bibliotecario nella fase di Ins/var:
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

							<table width="100%" border="0">
								<tr>
									<th width="220" class="etichetta"><bean:message
										key="ricerca.musica.Incipit"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0"><bean:message
										key="ricerca.musica.IncipitNMov"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0"><bean:message
										key="ricerca.musica.IncipitNProg"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0"><bean:message
										key="ricerca.musica.IncipitCont"
										bundle="gestioneBibliograficaLabels" /></th>
									<th class="etichetta" bgcolor="#dde8f0">&nbsp; <html:submit
										styleClass="buttonImageDelLine" property="methodDettaglioTit"
										title="Cancella Incipit">
										<bean:message key="button.canIncipit"
											bundle="gestioneBibliograficaLabels" />
									</html:submit> <html:submit styleClass="buttonImageNewLine"
										property="methodDettaglioTit" title="Inserisci Incipit">
										<bean:message key="button.insIncipit"
											bundle="gestioneBibliograficaLabels" />
									</html:submit> <html:submit styleClass="buttonImageModLine"
										property="methodDettaglioTit" title="Modifica Incipit">
										<bean:message key="button.modIncipit"
											bundle="gestioneBibliograficaLabels" />
									</html:submit></th>
								</tr>

								<logic:iterate id="itemIncipit"
									property="dettTitComVO.detTitoloMusVO.listaIncipit"
									name="dettaglioTitoloForm" indexId="idxIncipit">
									<tr class="testoNormale">
										<td></td>
										<td bgcolor="#FFCC99"><html:text name="itemIncipit"
											property="campoUno" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:text name="itemIncipit"
											property="campoDue" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:text name="itemIncipit"
											property="nota" indexed="true" /></td>
										<td bgcolor="#FFCC99"><html:radio property="selezRadioIncipit"
											value="${idxIncipit}" /></td>
									</tr>
								</logic:iterate>
							</table>

							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.musica.formaMusicale"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="100" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.formaMusic1"
										style="width:180px">
										<html:optionsCollection property="listaFormaMusicale"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
									<td width="100" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.formaMusic2"
										style="width:180px">
										<html:optionsCollection property="listaFormaMusicale"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
									<td width="100" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.formaMusic3"
										style="width:180px">
										<html:optionsCollection property="listaFormaMusicale"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
							</table>

							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.musica.tonalita"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="100" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.tonalita"
										style="width:180px">
										<html:optionsCollection property="listaTonalita"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>

								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.datazioni" bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.datazione" maxlength="10" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.sezioni" bundle="gestioneBibliograficaLabels" /></td>
									<td width="400" class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.sezioni" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.numOpera"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.numOpera" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.numOrdine"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.numOrdine" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.numCatTem"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.numCatTem" size="50"></html:text></td>
								</tr>
							</table>

							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.musica.dataComp"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="etichetta"><bean:message
										key="ricerca.inizio" bundle="gestioneBibliograficaLabels" /></td>
									<td>&nbsp;</td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.dataCompIni" size="10"></html:text></td>
									<td>&nbsp;&nbsp;&nbsp;</td>
									<td class="etichetta"><bean:message
										key="ricerca.fine" bundle="gestioneBibliograficaLabels" /></td>
									<td>&nbsp;</td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.dataCompFin" size="10"></html:text></td>
								</tr>
							</table>





							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.musica.orgSintComp"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.orgSintComp" size="50"></html:text>
									<html:submit styleClass="buttonImageHlpRep"
										property="methodDettaglioTit" alt="Cerca Elementi Organico">
										<bean:message key="button.orgSintComp.hlpElementiOrganico"
											bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.orgAnalComp"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.orgAnalComp" size="50"></html:text>
									<html:submit styleClass="buttonImageHlpRep"
										property="methodDettaglioTit" alt="Cerca Elementi Organico">
										<bean:message key="button.orgAnalComp.hlpElementiOrganico"
											bundle="gestioneBibliograficaLabels" />
									</html:submit></td>
								</tr>
							</table>
							<table border="0">
								<tr>
									<td width="220" class="etichetta"><bean:message
										key="ricerca.musica.titOrdinam"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.titOrdinam" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.titEstratto"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.titEstratto" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.appellativo"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.appellativo" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.tipoTesto"
										bundle="gestioneBibliograficaLabels" /></td>
									<td width="100" class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.tipoTesto"
										style="width:180px">
										<html:optionsCollection property="listaTipoTestoLetterario"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.stesura"
										bundle="gestioneBibliograficaLabels" /></td>
										<td class="testoNormale"><html:select
												property="dettTitComVO.detTitoloMusVO.stesura"
												style="width:180px">
												<html:optionsCollection property="listaStesura"
													value="codice" label="descrizioneCodice" />
										</html:select></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.composito"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.composito" size="50"></html:text></td>
								</tr>
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.palinsesto"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:text
										property="dettTitComVO.detTitoloMusVO.palinsesto" size="50"></html:text></td>
								</tr>

								<!-- Bug mantis esercizio 5660 - La gestione del campo Materia deve essere fatta con controlli su tavella codici MAMU
						 			perchè non è un testo libero (Materiale Musicale manoscritto) -->
								<tr>
									<td class="etichetta"><bean:message
										key="ricerca.musica.materia"
										bundle="gestioneBibliograficaLabels" /></td>
									<td class="testoNormale"><html:select
										property="dettTitComVO.detTitoloMusVO.materia"
										style="width:180px">
										<html:optionsCollection property="listaMateria"
											value="codice" label="descrizioneCodice" />
									</html:select></td>
								</tr>

							</table>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
		</c:choose>
</table>
