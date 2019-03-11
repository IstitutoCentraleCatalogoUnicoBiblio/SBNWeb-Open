<!--	SBNWeb - Rifacimento ClientServer
		JSP di sintetica autori
		almaviva2 - Inizio Codifica Settembre 2006
		Modifica almaviva2 15.12.2010 BUG MANTIS 4055 sinteticaAutori.jsp eliminata colonna Tipol;
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

<html:xhtml />
<layout:page>

	<sbn:navform action="/gestionebibliografica/autore/sinteticaAutori.do">
		<div id="divForm">

			<div id="divMessaggio">
				<sbn:errors bundle="gestioneBibliograficaMessages" />
			</div>

			<c:choose>
				<c:when test="${sinteticaAutoriForm.prospettaDatiOggColl eq 'SI'}">
					<table border="0">
						<tr>
							<td class="etichetta">
								<bean:message key="ricerca.titoloRiferimento"
									bundle="gestioneBibliograficaLabels" />
								:
							</td>
							<td width="20" class="testoNormale">
								<html:text property="idOggColl" size="10" readonly="true"></html:text>
							</td>
							<td width="150" class="etichetta">
								<html:text property="descOggColl" size="50" readonly="true"></html:text>
							</td>
						</tr>
					</table>
					<hr color="#dde8f0" />
				</c:when>
			</c:choose>
			<sbn:blocchi numBlocco="numBlocco" numNotizie="numAutori"
				parameter="methodSinteticaAut" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe"
				livelloRicerca="${sinteticaAutoriForm.livRicerca}"></sbn:blocchi>

			<table class="sintetica">
				<tr bgcolor="#dde8f0">
					<th class="etichetta">
						<bean:message key="sintetica.progr"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<!--<th class="etichetta">
						<bean:message key="sintetica.tipol"
							bundle="gestioneBibliograficaLabels" />-->
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.vid"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.nome"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.forma"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.tiponome"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.livelloaut"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.datazione"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta"></th>
					<th class="etichetta"></th>
				</tr>
				<logic:iterate id="item" property="listaSintetica"
					name="sinteticaAutoriForm" indexId="riga">
					<sbn:rowcolor var="color" index="riga" />
					<tr class="testoNormale" bgcolor="#FEF1E2">
						<!--<td bgcolor="${color}"><bean-struts:write name="item"
						property="progressivo" /></td>-->
						<td bgcolor="${color}">
							<sbn:anchor name="item" property="progressivo" />
							<sbn:linkbutton index="vid" name="item" value="progressivo"
								key="button.analiticaAut" bundle="gestioneBibliograficaLabels"
								title="analitica" property="selezRadio" />
						</td>
						<!--<td valign="middle" bgcolor="${color}">
						<html:img page="/images/${item.imageUrl}" /></td>-->
						<td bgcolor="${color}">

							<!--<bean-struts:write name="item" property="vid" />-->
							<sbn:linkbutton index="vid" name="item" value="vid"
								key="button.analiticaAut" bundle="gestioneBibliograficaLabels"
								title="analitica" property="selezRadio" />

						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="nome" filter="false" />
						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="forma" />
						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="tipoNome" />
						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="livelloAutorita" />
						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="datazione" />
						</td>
						<td bgcolor="${color}">
							<html:radio property="selezRadio" value="${item.keyVidNome}" />
						</td>
						<td bgcolor="${color}">
							<html:multibox property="selezCheck" value="${item.vid}" />
							<html:hidden property="selezCheck" value="" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</div>

		<div id="divFooterCommon">
			<sbn:blocchi numBlocco="numBlocco" numNotizie="numAutori"
				parameter="methodSinteticaAut" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe" bottom="true"></sbn:blocchi>
		</div>

		<div id="divFooter">
			<c:choose>
				<c:when
					test="${sinteticaAutoriForm.tipologiaTastiera eq 'ProspettazioneSimili'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.gestSimili.riAggiorna"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.gestSimili.confermaAgg"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<c:choose>
								<c:when test="${sinteticaAutoriForm.prospettazionePerLegami eq 'SI'}">
									<td align="center">
											<html:submit property="methodSinteticaAut">
											<bean:message key="button.gestLegami.lega"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
							</c:choose>

							<c:choose>
								<c:when test="${sinteticaAutoriForm.idOggColl ne ''}">
									<td align="center">
										<html:submit property="methodSinteticaAut">
											<bean:message key="button.gestSimili.fusione"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
							</c:choose>
						</tr>
					</table>
				</c:when>

				<c:when
					test="${sinteticaAutoriForm.tipologiaTastiera eq 'ProspettaSimiliPerCondividi'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.gestSimiliCondividi.catturaEFondi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.gestSimiliCondividi.tornaAnaliticaPerCondividi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

						</tr>
					</table>
				</c:when>


				<c:when
					test="${sinteticaAutoriForm.tipologiaTastiera eq 'ProspettaIdenticoPerCondividi'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.gestSimiliCondividi.catturaEFondi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>

				<c:when
					test="${sinteticaAutoriForm.tipologiaTastiera eq 'ProspettaSimiliPerCondividiNoRadice'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.gestSimiliCondividi.catturaESpostaLegame"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.analiticaTit"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.gestSimiliCondividi.tornaAnaliticaPerCondividi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

						</tr>
					</table>
				</c:when>

				<c:when
					test="${sinteticaAutoriForm.tipologiaTastiera eq 'UtilizzoComeSif'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.analiticaAut"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

							<td class="etichetta">
								<bean:message key="label.esamina" bundle="gestioneBibliograficaLabels" />
								<html:select property="esaminaAutSelez">
									<html:optionsCollection property="listaEsaminaAut"
										value="descrizione" label="descrizione" />
								</html:select>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.esamina"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>

				<c:when
					test="${sinteticaAutoriForm.tipologiaTastiera eq 'ProspettazionePerFusioneOnLine'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.gestSimiliCondividi.catturaEFondi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.analiticaAut"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.rinunciaFusione"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>



				<c:otherwise>
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.analiticaAut"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<c:choose>
								<c:when test="${sinteticaAutoriForm.livRicerca eq 'P'}">
									<td align="center">
										<html:submit property="methodSinteticaAut">
											<bean:message key="button.cercaIndice"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
								<c:when test="${sinteticaAutoriForm.livRicerca eq 'I' and sinteticaAutoriForm.creaAut eq 'SI'}">
									<td align="center">
										<html:submit property="methodSinteticaAut">
											<bean:message key="button.creaAut"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
							</c:choose>
							<c:choose>
								<c:when test="${sinteticaAutoriForm.creaAutLoc eq 'SI'}">
									<td align="center">
										<html:submit property="methodSinteticaAut">
											<bean:message key="button.creaAutLoc"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
							</c:choose>
							<td class="etichetta">
								<bean:message key="label.esamina" bundle="gestioneBibliograficaLabels" />
								<html:select property="esaminaAutSelez">
									<html:optionsCollection property="listaEsaminaAut"
										value="descrizione" label="descrizione" />
								</html:select>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaAut">
									<bean:message key="button.esamina"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<c:choose>
								<c:when
									test="${sinteticaAutoriForm.prospettazionePerLegami eq 'SI'}">
									<td align="center">
										<html:submit property="methodSinteticaAut">
											<bean:message key="button.gestLegami.lega"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
							</c:choose>


							<td align="center">
								<layout:combo bundle="gestioneBibliograficaLabels"
									label="button.gestione" name="sinteticaAutoriForm"
									button="button.confermaGestisci" property="gestisciAutSelez"
									combo="listaGestisciAut" parameter="methodSinteticaAut" />
							</td>


							<td align="right">
								<html:submit styleClass="buttonSelezTutti"
									property="methodSinteticaAut" title="Seleziona tutto">
									<bean:message key="button.selAllAutori"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
								<html:submit styleClass="buttonSelezNessuno"
									property="methodSinteticaAut" title="Deseleziona tutto">
									<bean:message key="button.deSelAllAutori"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>
</layout:page>
