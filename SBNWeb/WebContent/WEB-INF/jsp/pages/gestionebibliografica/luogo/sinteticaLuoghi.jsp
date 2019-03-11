<!--	SBNWeb - Rifacimento ClientServer
		JSP di sintetica autori
		almaviva2 - Inizio Codifica Settembre 2006
		Modifica almaviva2 15.12.2010 BUG MANTIS 4055 sinteticaLuoghi.jsp eliminata colonna Tipol;
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

	<sbn:navform action="/gestionebibliografica/luogo/sinteticaLuoghi.do">
		<div id="divForm">

			<div id="divMessaggio">
				<sbn:errors bundle="gestioneBibliograficaMessages" />
			</div>

			<c:choose>
				<c:when test="${sinteticaLuoghiForm.prospettaDatiOggColl eq 'SI'}">
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
			<sbn:blocchi numBlocco="numBlocco" numNotizie="numLuoghi"
				parameter="methodSinteticaLuo" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe"
				livelloRicerca="${sinteticaLuoghiForm.livRicerca}"></sbn:blocchi>

			<table class="sintetica">
				<tr bgcolor="#dde8f0">
					<th class="etichetta">
						<bean:message key="sintetica.progr"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<!--<th class="etichetta">
						<bean:message key="sintetica.tipol"
							bundle="gestioneBibliograficaLabels" />
					</th>-->
					<th class="etichetta">
						<bean:message key="sintetica.lid"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.denominazione"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.forma"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.livelloaut"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th width="45" class="etichetta"></th>
				</tr>
				<logic:iterate id="item" property="listaSintetica"
					name="sinteticaLuoghiForm" indexId="riga">
					<sbn:rowcolor var="color" index="riga" />
					<tr class="testoNormale" bgcolor="#FEF1E2">
						<!--<td bgcolor="${color}"><bean-struts:write name="item"
						property="progressivo" /></td>-->
						<td bgcolor="${color}">
							<sbn:anchor name="item" property="progressivo" />
							<sbn:linkbutton index="lid" name="item" value="progressivo"
								key="button.analiticaLuo" bundle="gestioneBibliograficaLabels"
								title="analitica" property="selezRadio" />
						</td>

						<!--<td valign="middle" bgcolor="${color}">
						<html:img page="/images/${item.imageUrl}" /></td>-->
						<td bgcolor="${color}">

							<!--<bean-struts:write name="item" property="lid" />-->
							<sbn:linkbutton index="lid" name="item" value="lid"
								key="button.analiticaLuo" bundle="gestioneBibliograficaLabels"
								title="analitica" property="selezRadio" />


						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="denominazione"
								filter="false" />
						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="forma" />
						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="livelloAutorita" />
						</td>
						<td bgcolor="${color}">
							<html:radio property="selezRadio" value="${item.lid}" />
							<html:multibox property="selezCheck" value="${item.lid}" />
							<html:hidden property="selezCheck" value="" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</div>

		<div id="divFooterCommon">
			<sbn:blocchi numBlocco="numBlocco" numNotizie="numLuoghi"
				parameter="methodSinteticaLuo" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe" bottom="true" />
		</div>

		<div id="divFooter">
			<c:choose>
				<c:when
					test="${sinteticaLuoghiForm.tipologiaTastiera eq 'UtilizzoComeSif'}">

					<td align="center">
						<html:submit property="methodSinteticaLuo">
							<bean:message key="button.analiticaLuo"
								bundle="gestioneBibliograficaLabels" />
						</html:submit>
					</td>

					<td class="etichetta">
						<bean:message key="label.esamina" bundle="gestioneBibliograficaLabels" />
						<html:select property="esaminaLuoSelez">
							<html:optionsCollection property="listaEsaminaLuo"
								value="descrizione" label="descrizione" />
						</html:select>
					</td>
					<td align="center">
						<html:submit property="methodSinteticaLuo">
							<bean:message key="button.esamina"
								bundle="gestioneBibliograficaLabels" />
						</html:submit>
					</td>
				</c:when>
				<c:when
					test="${sinteticaLuoghiForm.tipologiaTastiera eq 'ProspettazionePerFusioneOnLine'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaLuo">
									<bean:message key="button.gestSimiliCondividi.catturaEFondi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaLuo">
									<bean:message key="button.analiticaLuo"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaLuo">
									<bean:message key="button.rinunciaFusione"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</tr>
					</table>
				</c:when>

				<c:otherwise>
					<c:choose>
						<c:when test="${sinteticaLuoghiForm.prospettazioneSimili eq 'SI'}">
							<table align="center">
								<tr>
									<td align="center">
										<html:submit property="methodSinteticaLuo">
											<bean:message key="button.gestSimili.riAggiorna"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
									<td align="center">
										<html:submit property="methodSinteticaLuo">
											<bean:message key="button.gestSimili.confermaAgg"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>

									<c:choose>
										<c:when test="${sinteticaLuoghiForm.prospettazionePerLegami eq 'SI'}">
											<td align="center">
													<html:submit property="methodSinteticaLuo">
													<bean:message key="button.gestLegami.lega"
														bundle="gestioneBibliograficaLabels" />
												</html:submit>
											</td>
										</c:when>
									</c:choose>

									<c:choose>
										<c:when test="${sinteticaLuoghiForm.idOggColl ne ''}">
											<td align="center">
												<html:submit property="methodSinteticaLuo">
													<bean:message key="button.gestSimili.fusione"
														bundle="gestioneBibliograficaLabels" />
												</html:submit>
											</td>
										</c:when>
									</c:choose>


								</tr>
							</table>

						</c:when>
						<c:otherwise>

							<table align="center">
								<tr>

									<td align="center">
										<html:submit property="methodSinteticaLuo">
											<bean:message key="button.analiticaLuo"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>

									<c:choose>
										<c:when test="${sinteticaLuoghiForm.livRicerca eq 'P'}">
											<td align="center">
												<html:submit property="methodSinteticaLuo">
													<bean:message key="button.cercaIndice"
														bundle="gestioneBibliograficaLabels" />
												</html:submit>
											</td>
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${sinteticaLuoghiForm.creaLuo eq 'SI'}">
													<td align="center">
														<html:submit property="methodSinteticaLuo">
															<bean:message key="button.creaLuo"
																bundle="gestioneBibliograficaLabels" />
														</html:submit>
													</td>
												</c:when>
											</c:choose>
										</c:otherwise>
									</c:choose>

									<c:choose>
										<c:when test="${sinteticaLuoghiForm.creaLuo eq 'SI'}">
											<td align="center">
											<!-- la gestione del luogo SOLO LOCALE VIENE ELIMINATA
												<html:submit property="methodSinteticaLuo">
												<bean:message key="button.creaLuoLoc" bundle="gestioneBibliograficaLabels" />
												</html:submit>
											-->
											</td>
										</c:when>
									</c:choose>
									<td class="etichetta">
										<bean:message key="label.esamina" bundle="gestioneBibliograficaLabels" />
										<html:select property="esaminaLuoSelez">
											<html:optionsCollection property="listaEsaminaLuo"
												value="descrizione" label="descrizione" />
										</html:select>
									</td>
									<td align="center">
										<html:submit property="methodSinteticaLuo">
											<bean:message key="button.esamina"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
									<c:choose>
										<c:when
											test="${sinteticaLuoghiForm.prospettazionePerLegami eq 'SI'}">
											<td align="center">
												<html:submit property="methodSinteticaLuo">
													<bean:message key="button.gestLegami.lega"
														bundle="gestioneBibliograficaLabels" />
												</html:submit>
											</td>
										</c:when>
									</c:choose>

									<td align="right">
										<html:submit styleClass="buttonSelezTutti"
											property="methodSinteticaLuo" title="Seleziona tutto">
											<bean:message key="button.selAllLuoghi"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
										<html:submit styleClass="buttonSelezNessuno"
											property="methodSinteticaLuo" title="Deseleziona tutto">
											<bean:message key="button.deSelAllLuoghi"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>

								</tr>
							</table>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</div>
	</sbn:navform>
</layout:page>
