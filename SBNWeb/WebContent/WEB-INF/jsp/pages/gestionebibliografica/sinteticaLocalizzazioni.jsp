<!--	SBNWeb - Rifacimento ClientServer
		JSP di sintetica autori
		almaviva2 - Inizio Codifica Settembre 2006
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

	<sbn:navform action="/gestionebibliografica/sinteticaLocalizzazioni.do">
		<div id="divForm">

		<div id="divMessaggio"><sbn:errors
			bundle="gestioneBibliograficaMessages" /></div>
		<table border="0">
			<tr>
				<td class="etichetta"><bean:message key="ricerca.titoloRiferimento"
					bundle="gestioneBibliograficaLabels" />:</td>
				<td class="testoNormale"><html:text property="idOggColl" size="15"
					readonly="true"></html:text></td>
				<td class="etichetta"><html:text property="descOggColl" size="70"
					readonly="true"></html:text></td>
			</tr>
		</table>
		<hr color="#dde8f0" />

		<sbn:blocchi numBlocco="numBlocco" numNotizie="numLoc"
			parameter="methodSinteticaLoc" totBlocchi="totBlocchi"
			elementiPerBlocco="maxRighe"
			livelloRicerca="${sinteticaLocalizzazioniForm.livRic}"></sbn:blocchi>

		<c:choose>
			<c:when
				test="${sinteticaLocalizzazioniForm.utilizzoComeSif eq 'SI'}">
						<table border="0">
							<tr>
								<td  class="etichetta"><bean:message key="gestLocal.testRic" bundle="gestioneBibliograficaLabels" />
								<html:select
									property="codPoloRicercaSelez">
									<html:optionsCollection property="listaCodPolo" value="descrizione" label="descrizione" />
								</html:select>
								<td align="center"><html:submit property="methodSinteticaLoc">
									<bean:message key="button.gestLocal.filtra"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
								<td align="center"><html:submit property="methodSinteticaLoc">
									<bean:message key="button.gestLocal.ricarica"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</tr>
						</table>
			</c:when>
		</c:choose>

		<c:choose>
			<c:when
				test="${sinteticaLocalizzazioniForm.tipoProspettazione eq 'DET'}">
				<table class="sintetica">
					<tr bgcolor="#dde8f0">
						<th class="etichetta"><bean:message key="sintetica.progr"
							bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta"><bean:message key="sintetica.idSbn"
							bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta"><bean:message key="sintetica.denominazione"
							bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta"><bean:message key="sintetica.idAnag"
							bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta"><bean:message key="sintetica.tipoLoc"
							bundle="gestioneBibliograficaLabels" /></th>
					</tr>
					<logic:iterate id="item" property="listaSintetica"
						name="sinteticaLocalizzazioniForm" indexId="riga">
						<sbn:rowcolor var="color" index="riga" />
						<tr class="testoNormale" bgcolor="#FEF1E2">
							<td bgcolor="${color}"><bean-struts:write name="item"
								property="progressivo" /></td>
							<td bgcolor="${color}">
								<sbn:linkbutton index="progressivo" name="item" value="IDSbn"
								key="button.dettaglio" bundle="gestioneBibliograficaLabels"
								title="dettaglio" property="selezRadio" />
							<td bgcolor="${color}"><bean-struts:write name="item"
								property="descrBiblioteca" /></td>
							<td bgcolor="${color}"><bean-struts:write name="item"
								property="IDAnagrafe" /></td>
							<td bgcolor="${color}"><bean-struts:write name="item"
								property="tipoLoc" /></td>
						</tr>
					</logic:iterate>
				</table>
			</c:when>
			<c:when
				test="${sinteticaLocalizzazioniForm.tipoProspettazione eq 'VAR'}">
				<table class="sintetica">
					<tr bgcolor="#dde8f0">
						<th class="etichetta"><bean:message key="sintetica.progr"
							bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta"><bean:message key="sintetica.idSbn"
							bundle="gestioneBibliograficaLabels" /> - <bean:message
							key="sintetica.denominazione"
							bundle="gestioneBibliograficaLabels" /></th>
						<th class="etichetta"><bean:message key="sintetica.tipoLoc"
							bundle="gestioneBibliograficaLabels" /></th>
						<c:choose>
							<c:when
								test="${sinteticaLocalizzazioniForm.centroSistema eq 'SI'}">
								<th width="85" class="etichetta" bgcolor="#dde8f0"><html:submit
									styleClass="buttonImageNewLine" property="methodSinteticaLoc"
									title="Nuova localizzazione">
									<bean:message key="button.insLocalizzazione"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></th>
							</c:when>
						</c:choose>
					</tr>
					<logic:iterate id="item" property="listaSintetica"
						name="sinteticaLocalizzazioniForm" indexId="idxLocalizzazioni">
						<sbn:rowcolor var="color" index="idxLocalizzazioni" />
						<tr class="testoNormale" bgcolor="#FEF1E2">
							<td bgcolor="${color}">
								<sbn:anchor name="item" property="progressivo" />
								<bean-struts:write name="item" property="progressivo" />

								<!--<sbn:linkbutton index="progressivo" name="item" value="progressivo"
									key="button.dettaglio" bundle="gestioneBibliograficaLabels"
									title="dettaglio" property="selezRadio" />
									-->
							</td>

							<td bgcolor="${color}">
								<c:choose>
									<c:when	test="${item.IDAnagrafe ne ''}">


										<!-- Modifica Google3 - il comportamento del Link sulla Biblioteca nel caso di POSSESSO
										     deve essere lo stesso della pressione del tasto "confermaAggDett" -->
										<c:choose>
											<c:when	test="${sinteticaLocalizzazioniForm.tipoRichiesta ne 'GESTIONE'}">
												<sbn:linkbutton index="progressivo" name="item" value="IDSbn"
													key="button.gestLocal.confermaAggDett" bundle="gestioneBibliograficaLabels"
													title="dettaglio" property="selezRadio" submit="true"/>
													<bean-struts:write name="item" property="descrBiblioteca" />
											</c:when>
											<c:otherwise>
												<sbn:linkbutton index="progressivo" name="item" value="IDSbn"
													key="button.dettaglio" bundle="gestioneBibliograficaLabels"
													title="dettaglio" property="selezRadio" />
													<bean-struts:write name="item" property="descrBiblioteca" />
											</c:otherwise>
										</c:choose>
									</c:when>

									<c:otherwise>
										<html:select name="item" property="codBiblioteca" style="width:200px" indexed="true">
										<html:optionsCollection property="listaBiblio"
													value="codice" label="descrizioneCodice" />
										</html:select>
									</c:otherwise>
								</c:choose>
							</td>

							<!--<td bgcolor="${color}"><html:select name="item"
								property="tipoLoc" style="width:200px" indexed="true">
								<html:optionsCollection property="listaTipoLocalizzazione"
									value="descrizione" label="descrizione" />
							</html:select></td>-->

							<td bgcolor="${color}"><html:select name="item"
								property="tipoLoc" style="width:200px" indexed="true">
								<html:optionsCollection name="item" property="listaTipoLoc"
									value="descrizione" label="descrizione" />
							</html:select></td>


							<c:choose>
								<c:when
									test="${sinteticaLocalizzazioniForm.centroSistema eq 'SI'}">
									<td bgcolor="${color}"></td>
								</c:when>
							</c:choose>
						</tr>
					</logic:iterate>
				</table>
			</c:when>
			<c:when
				test="${sinteticaLocalizzazioniForm.tipoProspettazione eq 'INS'}">
				<table width="100%" border="0">
					<tr>
						<td class="etichetta"><bean:message key="sintetica.idSbn"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="etichetta"><bean:message key="sintetica.tipoLoc"
							bundle="gestioneBibliograficaLabels" /></td>
					</tr>
					<tr>
						<td class="etichetta"><html:select property="codBiblio"
							style="width:100px">
							<html:optionsCollection property="listaBiblio"
								value="descrizioneCodice" label="descrizioneCodice" />
						</html:select></td>

						<!--<td class="etichetta"><html:select property="codTipoLoc"
							style="width:200px">
							<html:optionsCollection property="listaTipoLocalizzazione"
								value="descrizione" label="descrizione" />
						</html:select></td>-->
						<td bgcolor="${color}"><html:select name="item"
								property="tipoLoc" style="width:200px" indexed="true">
								<html:optionsCollection name="item" property="listaTipoLoc"
									value="descrizione" label="descrizione" />
						</html:select></td>

					</tr>
				</table>
			</c:when>
		</c:choose>
		</div>

		<div id="divFooterCommon"><sbn:blocchi numBlocco="numBlocco"
			numNotizie="numLoc" parameter="methodSinteticaLoc"
			totBlocchi="totBlocchi" elementiPerBlocco="maxRighe" bottom="true"></sbn:blocchi>
		</div>

		<div id="divFooter">

		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodSinteticaLoc">
					<bean:message key="button.annulla"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
				<c:choose>
					<c:when
						test="${sinteticaLocalizzazioniForm.tipoProspettazione ne 'DET'}">
						<td align="center"><html:submit property="methodSinteticaLoc">
							<bean:message key="button.gestLocal.confermaAgg"
								bundle="gestioneBibliograficaLabels" />
						</html:submit></td>

						<c:choose>
							<c:when
								test="${sinteticaLocalizzazioniForm.tipoRichiesta ne 'GESTIONE'}">
								<td align="center"><html:submit property="methodSinteticaLoc">
										<bean:message key="button.gestLocal.confermaAggDett"
										bundle="gestioneBibliograficaLabels" />
								</html:submit></td>
							</c:when>
						</c:choose>
					</c:when>
				</c:choose>
			</tr>
		</table>



		</div>
	</sbn:navform>
</layout:page>
<script type="text/javascript"
	src='<c:url value="/scripts/bibliografica/dettaglioTitolo.js" />'></script>