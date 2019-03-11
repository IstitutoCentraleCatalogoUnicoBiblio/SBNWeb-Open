<!--	SBNWeb - Rifacimento ClientServer
		JSP di sintetica autori
		almaviva2 - Inizio Codifica Settembre 2006
		Modifica almaviva2 15.12.2010 BUG MANTIS 4055 sinteticaMarche.jsp cambiato titolo colonna Tipol con Logo;
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

	<sbn:navform action="/gestionebibliografica/marca/sinteticaMarche.do">

		<div id="divForm">

			<div id="divMessaggio">
				<sbn:errors bundle="gestioneBibliograficaMessages" />
			</div>

			<c:choose>
				<c:when test="${sinteticaMarcheForm.prospettaDatiOggColl eq 'SI'}">
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
			<sbn:blocchi numBlocco="numBlocco" numNotizie="numMarche"
				parameter="methodSinteticaMar" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe"
				livelloRicerca="${sinteticaMarcheForm.livRicerca}" />

			<table class="sintetica">
				<tr bgcolor="#dde8f0">
					<th class="etichetta">
						<bean:message key="sintetica.progr"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.logo"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.mid"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="ricerca.descrizione"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.livelloaut"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.motto"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th class="etichetta">
						<bean:message key="sintetica.citazione"
							bundle="gestioneBibliograficaLabels" />
					</th>
					<th width="22" class="etichetta"></th>
					<th width="22" class="etichetta"></th>
				</tr>
				<logic:iterate id="item" property="listaSintetica"
					name="sinteticaMarcheForm" indexId="riga">
					<sbn:rowcolor var="color" index="riga" />
					<tr class="testoNormale" bgcolor="#FEF1E2">
						<td bgcolor="${color}">
							<sbn:anchor name="item" property="progressivo" />
							<sbn:linkbutton index="mid" name="item" value="progressivo"
								key="button.analiticaMar" bundle="gestioneBibliograficaLabels"
								title="analitica" property="selezRadio" />
						</td>

						<td valign="middle" bgcolor="${color}">
							<html:img page="/images/${item.imageUrl}" />
						</td>
						<td bgcolor="${color}">
							<sbn:linkbutton index="mid" name="item" value="mid"
								key="button.analiticaMar" bundle="gestioneBibliograficaLabels"
								title="analitica" property="selezRadio" />


						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="nome" />
						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="livelloAutorita" />
						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="motto" />
						</td>
						<td bgcolor="${color}">
							<bean-struts:write name="item" property="citazione" />
						</td>
						<td bgcolor="${color}">
							<html:radio property="selezRadio" value="${item.keyMidNomeFinto}" />
						</td>
						<td bgcolor="${color}">
							<html:multibox property="selezCheck" value="${item.mid}" />
							<html:hidden property="selezCheck" value="" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</div>


		<div id="divFooterCommon">
			<sbn:blocchi numBlocco="numBlocco" numNotizie="numMarche"
				parameter="methodSinteticaMar" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe" bottom="true" />
		</div>


		<div id="divFooter">
			<c:choose>
				<c:when
					test="${sinteticaMarcheForm.tipologiaTastiera eq 'UtilizzoComeSif'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaMar">
									<bean:message key="button.analiticaMar"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

							<td align="center">
								<html:submit property="methodSinteticaMar">
									<bean:message key="button.immaginiMarche"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

							<td class="etichetta">
								<bean:message key="label.esamina" bundle="gestioneBibliograficaLabels" />
								<html:select property="esaminaMarSelez">
									<html:optionsCollection property="listaEsaminaMar"
										value="descrizione" label="descrizione" />
								</html:select>
							</td>

							<td align="center">
								<html:submit property="methodSinteticaMar">
									<bean:message key="button.esamina"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

							<td align="right">
								<html:submit styleClass="buttonSelezTutti"
									property="methodSinteticaMar" title="Seleziona tutto">
									<bean:message key="button.selAllMarche"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
								<html:submit styleClass="buttonSelezMarcheImmagini"
									property="methodSinteticaMar" title="Seleziona marche con Immagini">
									<bean:message key="button.selAllMarcheImmagini"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
								<html:submit styleClass="buttonSelezNessuno"
									property="methodSinteticaMar" title="Deseleziona tutto">
									<bean:message key="button.deSelAllMarche"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

						</tr>
					</table>
				</c:when>

				<c:when
					test="${sinteticaMarcheForm.tipologiaTastiera eq 'ProspettazionePerFusioneOnLine'}">
					<table align="center">
						<tr>
							<td align="center">
								<html:submit property="methodSinteticaMar">
									<bean:message key="button.gestSimiliCondividi.catturaEFondi"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaMar">
									<bean:message key="button.analiticaMar"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaMar">
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
								<html:submit property="methodSinteticaMar">
									<bean:message key="button.analiticaMar"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

							<td align="center">
								<html:submit property="methodSinteticaMar">
									<bean:message key="button.immaginiMarche"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>

							<c:choose>
								<c:when test="${sinteticaMarcheForm.livRicerca eq 'P'}">
									<td align="center">
										<html:submit property="methodSinteticaMar">
											<bean:message key="button.cercaIndice"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${interrogazioneMarcaForm.creaMar eq 'SI'}">
											<td align="center">
												<html:submit property="methodSinteticaMar">
													<bean:message key="button.creaMar"
														bundle="gestioneBibliograficaLabels" />
												</html:submit>
											</td>
										</c:when>
									</c:choose>
								</c:otherwise>
							</c:choose>

							<td class="etichetta">
								<bean:message key="label.esamina" bundle="gestioneBibliograficaLabels" />
								<html:select property="esaminaMarSelez">
									<html:optionsCollection property="listaEsaminaMar"
										value="descrizione" label="descrizione" />
								</html:select>
							</td>
							<td align="center">
								<html:submit property="methodSinteticaMar">
									<bean:message key="button.esamina"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
							<c:choose>
								<c:when
									test="${sinteticaMarcheForm.prospettazionePerLegami eq 'SI'}">
									<td align="center">
										<html:submit property="methodSinteticaMar">
											<bean:message key="button.gestLegami.lega"
												bundle="gestioneBibliograficaLabels" />
										</html:submit>
									</td>
								</c:when>
							</c:choose>

							<td align="right">
								<html:submit styleClass="buttonSelezTutti"
									property="methodSinteticaMar" title="Seleziona tutto">
									<bean:message key="button.selAllMarche"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
								<html:submit styleClass="buttonSelezMarcheImmagini"
									property="methodSinteticaMar" title="Seleziona marche con Immagini">
									<bean:message key="button.selAllMarcheImmagini"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
								<html:submit styleClass="buttonSelezNessuno"
									property="methodSinteticaMar" title="Deseleziona tutto">
									<bean:message key="button.deSelAllMarche"
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
