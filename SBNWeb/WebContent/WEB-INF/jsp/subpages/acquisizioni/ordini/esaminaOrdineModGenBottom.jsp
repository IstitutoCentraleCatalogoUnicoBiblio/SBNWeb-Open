<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<sbn:checkAttivita idControllo="VALUTA">
	<tr>
		<td>
			<!-- deve essere sempre visibile ma non agli ordini di rilegatura (tck 2450) ne sugli ordini cancellati (tck 2599)-->
			<c:choose>
				<c:when
					test="${navForm.scegliTAB ne 'R' and !navForm.datiOrdine.flag_canc }">
					<html:submit styleClass="pulsanti"
						property="methodEsaminaOrdineMod">
						<bean:message key="ricerca.button.listaInventariOrdine"
							bundle="acquisizioniLabels" />
					</html:submit>

				</c:when>
			</c:choose> <c:choose>
				<c:when
					test="${navForm.scegliTAB ne 'R' and !navForm.datiOrdine.flag_canc and (!navForm.disabilitazioneSezioneAbbonamento or (navForm.disabilitazioneSezioneAbbonamento and navForm.datiOrdine.continuativo and navForm.datiOrdine.naturaOrdine eq 'S' and (navForm.datiOrdine.statoOrdine eq 'C' or navForm.datiOrdine.statoOrdine eq 'N')))}">
					<html:submit styleClass="pulsanti"
						property="methodEsaminaOrdineMod">
						<bean:message key="ricerca.button.schedoneRinnovi"
							bundle="acquisizioniLabels" />
					</html:submit>
				</c:when>
			</c:choose> <logic:equal name="navForm" property="disabilitaTutto" value="false">

				<bs:define id="bibaff">
					<bs:write name="navForm" property="biblioNONCentroSistema" />
				</bs:define>

				<bs:define id="inventari">
					<bs:write name="navForm" property="disabilitazioneBottoneInventari" />
				</bs:define>

				<c:choose>
					<c:when test="${!navForm.biblioNONCentroSistema}">
						<html:submit styleClass="pulsanti"
							property="methodEsaminaOrdineMod" disabled="${bibaff}">
							<bean:message key="ricerca.button.bibloaffil"
								bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
				</c:choose>

				<c:choose>
					<c:when test="${navForm.scegliTAB ne 'R'}">
						<c:choose>
							<c:when test="${navForm.ordineApertoAbilitaInput}">
								<sbn:checkAttivita idControllo="GESTIONE">
									<html:submit styleClass="pulsanti"
										property="methodEsaminaOrdineMod" disabled="true">
										<bean:message key="ricerca.button.inventari"
											bundle="acquisizioniLabels" />
									</html:submit>
								</sbn:checkAttivita>

							</c:when>
							<c:otherwise>
								<sbn:checkAttivita idControllo="GESTIONE">
									<html:submit styleClass="pulsanti"
										property="methodEsaminaOrdineMod" disabled="${inventari}">
										<bean:message key="ricerca.button.inventari"
											bundle="acquisizioniLabels" />
									</html:submit>
								</sbn:checkAttivita>
							</c:otherwise>
						</c:choose>
						<!--
					<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod" >
						<bean:message key="ricerca.button.listaInventariOrdine" bundle="acquisizioniLabels" />
					</html:submit>
					-->
					</c:when>
				</c:choose>

				<!--
			<logic:equal  name="navForm" property="scegliTAB" value="R">
				<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
					<bean:message key="crea.button.visualizzInventari" bundle="acquisizioniLabels" />
				</html:submit>
			</logic:equal>
			or navForm.datiOrdine.stampato
			-->

				<logic:equal name="navForm" property="scegliTAB" value="R">
					<c:choose>
						<c:when test="${navForm.ordineApertoAbilitaInput}">
							<sbn:checkAttivita idControllo="GESTIONE">
								<html:submit styleClass="pulsanti"
									property="methodEsaminaOrdineMod" disabled="true">
									<bean:message key="crea.button.associaInventari"
										bundle="acquisizioniLabels" />
								</html:submit>
							</sbn:checkAttivita>
						</c:when>
						<c:otherwise>
							<sbn:checkAttivita idControllo="GESTIONE">
								<html:submit styleClass="pulsanti"
									property="methodEsaminaOrdineMod">
									<bean:message key="crea.button.associaInventari"
										bundle="acquisizioniLabels" />
								</html:submit>
							</sbn:checkAttivita>
						</c:otherwise>
					</c:choose>
				</logic:equal>

				<c:choose>
					<c:when
						test="${!navForm.ordineApertoAbilitaInput or navForm.datiOrdine.statoOrdine eq 'N' or navForm.datiOrdine.statoOrdine eq 'C' }">
						<html:submit styleClass="pulsanti"
							property="methodEsaminaOrdineMod">
							<bean:message key="ricerca.button.operazionesuordine"
								bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
					<c:otherwise>
						<html:submit styleClass="pulsanti"
							property="methodEsaminaOrdineMod" disabled="true">
							<bean:message key="ricerca.button.operazionesuordine"
								bundle="acquisizioniLabels" />
						</html:submit>
					</c:otherwise>
				</c:choose>
				<!--
			<c:choose>
			<c:when test="${navForm.ordineApertoAbilitaInput}">
	             <sbn:checkAttivita idControllo="GESTIONE">
						<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod" disabled="true">
							<bean:message key="ricerca.button.operazionesuordine" bundle="acquisizioniLabels" />
						</html:submit>
		          </sbn:checkAttivita>
			</c:when>
			<c:otherwise>
	             <sbn:checkAttivita idControllo="GESTIONE">
				<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod" >
					<bean:message key="ricerca.button.operazionesuordine" bundle="acquisizioniLabels" />
				</html:submit>
	             </sbn:checkAttivita>

			</c:otherwise>
			</c:choose>
			-->
				<bs:define id="periodici">
					<bs:write name="navForm" property="disabilitazioneBottonePeriodici" />
				</bs:define>
				<sbn:checkAttivita idControllo="PERIODICI">
					<html:submit styleClass="pulsanti"
						property="methodEsaminaOrdineMod" disabled="${periodici}">
						<bean:message key="ricerca.button.periodici"
							bundle="acquisizioniLabels" />
					</html:submit>
				</sbn:checkAttivita>
				<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
					<bean:message key="ricerca.button.stampa"
						bundle="acquisizioniLabels" />
				</html:submit>
				<sbn:checkAttivita idControllo="SPEDISCI">
					<html:submit styleClass="pulsanti"
						property="methodEsaminaOrdineMod">
						<bean:message key="ricerca.button.spedisciOrdine"
							bundle="acquisizioniLabels" />
					</html:submit>
				</sbn:checkAttivita>

				<logic:equal name="navForm" property="scegliTAB" value="A">
					<sbn:checkAttivita idControllo="GESTIONE">
						<c:choose>
							<c:when test="${navForm.ordineApertoAbilitaInput}">
								<c:choose>
									<c:when test="${navForm.datiOrdine.gestProf}">
										<html:submit styleClass="pulsanti"
											property="methodEsaminaOrdineMod" disabled="true">
											<bean:message key="ricerca.button.fornitoriProfili"
												bundle="acquisizioniLabels" />
										</html:submit>
									</c:when>
								</c:choose>


								<html:submit styleClass="pulsanti"
									property="methodEsaminaOrdineMod" disabled="true">
									<bean:message key="ricerca.button.buonoOrdine"
										bundle="acquisizioniLabels" />
								</html:submit>

							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${navForm.datiOrdine.gestProf}">
										<html:submit styleClass="pulsanti"
											property="methodEsaminaOrdineMod">
											<bean:message key="ricerca.button.fornitoriProfili"
												bundle="acquisizioniLabels" />
										</html:submit>
									</c:when>
								</c:choose>

								<html:submit styleClass="pulsanti"
									property="methodEsaminaOrdineMod">
									<bean:message key="ricerca.button.buonoOrdine"
										bundle="acquisizioniLabels" />
								</html:submit>

							</c:otherwise>
						</c:choose>
					</sbn:checkAttivita>
				</logic:equal>
			</logic:equal>
		</td>
	</tr>
</sbn:checkAttivita>


<tr>
	<sbn:checkAttivita idControllo="VALUTA">
		<td><logic:equal name="navForm" property="enableScorrimento"
				value="true">

				<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
					<bean:message key="ricerca.button.scorriIndietro"
						bundle="acquisizioniLabels" />
				</html:submit>

				<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
					<bean:message key="ricerca.button.scorriAvanti"
						bundle="acquisizioniLabels" />
				</html:submit>
			</logic:equal> <logic:equal name="navForm" property="disabilitaTutto" value="false">
				<c:choose>
					<c:when test="${navForm.ordineApertoAbilitaInput}">
						<sbn:checkAttivita idControllo="GESTIONE">

							<html:submit styleClass="pulsanti"
								property="methodEsaminaOrdineMod" disabled="true">
								<bean:message key="ricerca.button.salva"
									bundle="acquisizioniLabels" />
							</html:submit>
						</sbn:checkAttivita>

					</c:when>
					<c:otherwise>
						<sbn:checkAttivita idControllo="GESTIONE">

							<html:submit styleClass="pulsanti"
								property="methodEsaminaOrdineMod">
								<bean:message key="ricerca.button.salva"
									bundle="acquisizioniLabels" />
							</html:submit>
						</sbn:checkAttivita>

					</c:otherwise>
				</c:choose>

				<sbn:checkAttivita idControllo="GESTIONE">
					<html:submit styleClass="pulsanti"
						property="methodEsaminaOrdineMod">
						<bean:message key="ricerca.button.ripristina"
							bundle="acquisizioniLabels" />
					</html:submit>
				</sbn:checkAttivita>

				<!-- 26.07.10 cambio idControllo di cancella (CANCELLAORDINE) con quello di salva (GESTIONE)-->
				<c:choose>
					<c:when test="${navForm.datiOrdine.statoOrdine eq 'N'}">
						<sbn:checkAttivita idControllo="GESTIONE">
							<html:submit styleClass="pulsanti"
								property="methodEsaminaOrdineMod">
								<bean:message key="ricerca.button.cancella"
									bundle="acquisizioniLabels" />
							</html:submit>
						</sbn:checkAttivita>

					</c:when>
					<c:otherwise>
						<sbn:checkAttivita idControllo="GESTIONE">
							<html:submit styleClass="pulsanti"
								property="methodEsaminaOrdineMod" disabled="true">
								<bean:message key="ricerca.button.cancella"
									bundle="acquisizioniLabels" />
							</html:submit>
						</sbn:checkAttivita>

					</c:otherwise>
				</c:choose>

				<!--

			<c:choose>
			<c:when test="${navForm.ordineApertoAbilitaInput}">
	             <sbn:checkAttivita idControllo="CANCELLAORDINE">
				<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod" disabled="true">
					<bean:message key="ricerca.button.cancella" bundle="acquisizioniLabels" />
				</html:submit>
	             </sbn:checkAttivita>

			</c:when>
			<c:otherwise>
	             <sbn:checkAttivita idControllo="CANCELLAORDINE">
				<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
					<bean:message key="ricerca.button.cancella" bundle="acquisizioniLabels" />
				</html:submit>
	             </sbn:checkAttivita>

			</c:otherwise>
			</c:choose>
			-->
			</logic:equal>
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
				<bean:message key="ricerca.button.indietro"
					bundle="acquisizioniLabels" />
			</html:submit></td>
	</sbn:checkAttivita>

	<sbn:checkAttivita idControllo="VALUTA" inverted="true">
		<td><html:submit styleClass="pulsanti"
				property="methodEsaminaOrdineMod">
				<bean:message key="ricerca.button.indietro"
					bundle="acquisizioniLabels" />
			</html:submit></td>
	</sbn:checkAttivita>
</tr>

