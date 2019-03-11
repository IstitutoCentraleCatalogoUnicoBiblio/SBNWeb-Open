<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/offerte/sinteticaOfferte.do">
		<div id="divForm">
			<div id="divMessaggio">
				<div align="center" class="messaggioInfo">
					<sbn:errors bundle="acquisizioniMessages" />
				</div>
			</div>

			<c:set var="livelloRicerca" property="livelloRicerca">
			</c:set>

		<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaOfferte"	 ></sbn:blocchi>

		<table align="center" width="100%"
			style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
			<tr class="etichetta" bgcolor="#dde8f0">
				<td scope="col" title="Tipo documento" align="center">
				</td>

				<td scope="col" title="Tipo documento" align="center">
					<bean:message key="ricerca.label.codiceBibl"
						bundle="acquisizioniLabels" />
				</td>
				<td scope="col" height="18" width="6%" align="center">
					<bean:message key="ordine.label.offerta"
						bundle="acquisizioniLabels" />
				</td>
				<td scope="col" colspan="2" height="18" align="center">
					<bean:message key="ordine.label.fornitore"
						bundle="acquisizioniLabels" />
				</td>
				<td scope="col" height="18" width="9%" align="center">
					<bean:message key="ordine.label.tipoOfferta"
						bundle="acquisizioniLabels" />
				</td>
				<td scope="col" height="18" width="7%" align="center">
					<bean:message key="ricerca.label.paese"
						bundle="acquisizioniLabels" />
				</td>
				<td scope="col" colspan="1" height="18" width="10%"
					align="center">
					<bean:message key="ricerca.label.lingua"
						bundle="acquisizioniLabels" />
				</td>
				<td scope="col" colspan="1" height="18" width="10%"
					align="center">
					<bean:message key="buono.label.anno"
						bundle="acquisizioniLabels" />
				</td>
				<td scope="col" colspan="1" height="18" width="15%"
					align="center">
					<bean:message key="ricerca.label.codISBN"
						bundle="acquisizioniLabels" />
				</td>
				<td scope="col" width="7%" align="center"></td>
			</tr>
			<logic:iterate id="elencaOfferte" property="listaOfferte" 	name="sinteticaOfferteForm"  >
				<c:set var="color">
					<c:choose>
						<c:when test='${color == "#FFCC99"}'>
		            #FEF1E2
		        </c:when>
						<c:otherwise>
					#FFCC99
		        </c:otherwise>
					</c:choose>
				</c:set>

				<tr class="testoNormale" bgcolor="${color}">
						<td bgcolor="${color}" >
							<sbn:linkbutton index="progressivo" name="elencaOfferte"
								value="progressivo"  key="button.ok"
								bundle="acquisizioniLabels" title="esamina" property="progrForm" />
						</td>
					<td align="center">
						<bean-struts:write name="elencaOfferte" property="codBibl"  />
					</td>
					<td align="center">
						<bean-struts:write name="elencaOfferte"
							property="identificativoOfferta" />
					</td>
					<td align="center">
						<bean-struts:write name="elencaOfferte"
							property="fornitore.codice" />
					</td>
					<td align="center">
						<bean-struts:write name="elencaOfferte"
							property="fornitore.descrizione" />
					</td>
					<td align="center">
						<bean-struts:write name="elencaOfferte"
							property="tipoProvenienza.descrizione" />
					</td>
					<td align="center">
						<bean-struts:write name="elencaOfferte"
							property="paese.codice" />
					</td>
					<td align="center">
						<bean-struts:write name="elencaOfferte"
							property="lingua.codice" />
					</td>
					<td align="center">
						<bean-struts:write name="elencaOfferte" property="data" />
					</td>
					<td align="center">
						<bean-struts:write name="elencaOfferte"
							property="numeroStandard" />
					</td>

					<td>
						<html:multibox property="selectedOfferte">
							<bean-struts:write name="elencaOfferte" property="chiave" />
						</html:multibox>
					</td>
				</tr>
			</logic:iterate>
		</table>
		</div>
		<div id="divFooterCommon">
			<sbn:blocchi numNotizie="totRighe" numBlocco="bloccoSelezionato"  elementiPerBlocco="maxRighe" totBlocchi="totBlocchi"	parameter="methodSinteticaOfferte"	bottom="true" ></sbn:blocchi>
		</div>
		<div id="divFooter">
			<table align="center" border="0" style="height:40px">
				<tr>
					<td align="center">


						<c:choose>
							<c:when test="${sinteticaOfferteForm.visibilitaIndietroLS}">
								<html:submit styleClass="pulsanti"
									property="methodSinteticaOfferte">
									<bean:message key="ricerca.button.scegli"
										bundle="acquisizioniLabels" />
								</html:submit>
							</c:when>
							<c:otherwise>
								<html:submit styleClass="buttonSelezTutti" title="Seleziona tutto"
									property="methodSinteticaOfferte">
									<bean:message key="ricerca.button.selTutti"
										bundle="acquisizioniLabels" />
								</html:submit>

								<html:submit styleClass="buttonSelezNessuno" title="Deseleziona tutto"
									property="methodSinteticaOfferte">
									<bean:message key="ricerca.button.deselTutti"
										bundle="acquisizioniLabels" />
								</html:submit>

				    		</c:otherwise>

						</c:choose>
						<html:submit styleClass="pulsanti"
							property="methodSinteticaOfferte">
							<bean:message key="ricerca.button.esamina"
								bundle="acquisizioniLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti"
							property="methodSinteticaOfferte">
							<bean:message key="ricerca.button.indietro"
								bundle="acquisizioniLabels" />
						</html:submit>
						<!--
						<html:submit styleClass="pulsanti" property="methodSinteticaOfferte">
							<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
						</html:submit>

					--></td>
				</tr>
			</table>

		</div>
	</sbn:navform>
</layout:page>
