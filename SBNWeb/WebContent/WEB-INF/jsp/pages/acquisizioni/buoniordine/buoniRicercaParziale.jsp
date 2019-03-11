<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput" value="false" />
<c:choose>
	<c:when test="${navForm.disabilitaTutto}">
		<script type="text/javascript">
			<bean-struts:define id="noinput"  value="true"/>
		</script>
	</c:when>
</c:choose>


<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/buoniordine/buoniRicercaParziale.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<table width="100%" align="center">
				<tr>
					<!-- tabella corpo COLONNA + LARGA -->
					<td align="left" valign="top" width="100%">

						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td class="etichettaIntestazione" colspan="7">&nbsp;</td>
							</tr>
							<tr>
								<td width="10%" scope="col" class="etichetta" align="left">
									<bean:message key="ricerca.label.codiceBibl"
										bundle="acquisizioniLabels" />
								</td>
								<td scope="col" align="left">
									<sbn:disableAll disabled="${navForm.parametri.tipoOperazione ne 'GESTIONE'}">
										<html:text styleId="testoNormale" property="codBibl" size="3"
											readonly="true"></html:text>
										<html:submit title="elenco"
											styleClass="buttonImageListaSezione"
											property="methodBuoniRicercaParziale">
											<bean:message key="ricerca.label.bibliolist"
												bundle="acquisizioniLabels" />
										</html:submit>
									</sbn:disableAll></td>
							</tr>
							<tr>
								<td class="etichettaIntestazione" colspan="7">&nbsp;</td>
							</tr>
							<tr>
								<!--	onchange="this.form.submit();"-->
								<td scope="col" colspan="2" class="etichetta"><div
										align="left">
										<bean:message key="buono.label.numBuonoDa"
											bundle="acquisizioniLabels" />
										<html:text styleId="testoNormale" property="numBuonoDa"
											size="12" maxlength="9"></html:text>
										<bean:message key="buono.label.numBuonoA"
											bundle="acquisizioniLabels" />
										<html:text styleId="testoNormale" property="numBuonoA"
											size="12" maxlength="9"></html:text>
									</div></td>
								<td scope="col" colspan="5" class="etichetta" align="left">
									<bean:message key="buono.label.dataBuonoDa"
										bundle="acquisizioniLabels" /> <html:text
										styleId="testoNormale" property="dataBuonoDa" size="10"
										readonly="${noinput}"></html:text> <bean:message
										key="buono.label.dataBuonoA" bundle="acquisizioniLabels" /> <html:text
										styleId="testoNormale" property="dataBuonoA" size="10"
										readonly="${noinput}"></html:text> <bean:message
										key="ordine.data.formato" bundle="acquisizioniLabels" />
								</td>
							</tr>
							<tr>
								<td class="etichettaIntestazione" colspan="7">&nbsp;</td>
							</tr>
							<sbn:disableAll
								disabled="${navForm.parametri.tipoOperazione ne 'GESTIONE'}">
								<tr>
									<td class="etichettaIntestazione" colspan="7"><bean:message
											key="buono.label.ordine" bundle="acquisizioniLabels" /></td>
								</tr>
								<tr>
									<td scope="col" class="etichetta"><div align="left">
											<bean:message key="ricerca.label.tipoOrdine"
												bundle="acquisizioniLabels" />
										</div></td>
									<td scope="col" align="left"><html:select
											styleClass="testoNormale" property="tipoOrdine"
											disabled="${noinput}">
											<html:optionsCollection property="listaTipoOrdine"
												value="codice" label="descrizione" />
										</html:select></td>
									<td scope="col" colspan=2 align="left"><bean:message
											key="buono.label.anno" bundle="acquisizioniLabels" /> <html:text
											styleId="testoNormale" property="anno" size="4"
											readonly="${noinput}"></html:text></td>
									<td colspan=4 scope="col" align="left"><bean:message
											key="buono.label.numero" bundle="acquisizioniLabels" /> <html:text
											styleId="testoNormale" property="numero" size="4"
											readonly="${noinput}"></html:text> <html:submit
											styleClass="buttonImage"
											property="methodBuoniRicercaParziale" disabled="${noinput}">
											<bean:message key="ricerca.button.ordine"
												bundle="acquisizioniLabels" />
										</html:submit></td>
								</tr>
								<c:choose>
									<c:when test="${navForm.gestBil}">

										<tr>
											<td class="etichettaIntestazione" colspan="7">&nbsp;</td>
										</tr>
										<tr>
											<td class="etichettaIntestazione" colspan="7"><bean:message
													key="ordine.label.bilancio" bundle="acquisizioniLabels" /></td>
										</tr>
										<tr>
											<td scope="col" class="etichetta" align="left"><bean:message
													key="ordine.label.esercizio" bundle="acquisizioniLabels" />
											</td>
											<td scope="col" align="left"><html:text
													styleId="testoNormale" property="esercizio" size="10"
													readonly="${noinput}"></html:text></td>
											<td colspan="3" class="etichetta" scope="col"><bean:message
													key="ordine.label.capitolo" bundle="acquisizioniLabels" />
												<html:text styleId="testoNormale" property="capitolo"
													size="16" maxlength="16" readonly="${noinput}"></html:text></td>
										</tr>
									</c:when>
								</c:choose>


								<tr>
									<td class="etichettaIntestazione" colspan="7">&nbsp;</td>
								</tr>
								<tr>
									<td class="etichettaIntestazione" colspan="7"><bean:message
											key="ordine.label.fornitore" bundle="acquisizioniLabels" />
									</td>
								</tr>
								<tr>
									<td colspan="4" valign="top" align="left"><html:text
											styleId="testoNormale" property="codFornitore" size="5"
											maxlength="10" readonly="${noinput}"></html:text> <html:text
											styleId="testoNormale" property="fornitore" size="45"
											readonly="${noinput}"></html:text> <!--
						  <html:link action="/acquisizioni/fornitori/fornitoriRicercaParziale.do" ><img border="0"   alt="ricerca fornitori"  src='<c:url value="/images/lente.GIF" />'/></html:link>
	                    --> <html:submit styleClass="buttonImage"
											property="methodBuoniRicercaParziale">
											<bean:message key="ordine.label.fornitore"
												bundle="acquisizioniLabels" />
										</html:submit></td>
								</tr>
							</sbn:disableAll>
						</table>
					</td>
					<!-- FINE tabella corpo COLONNA + LARGA -->
				</tr>
			</table>
		</div>
		<div id="divFooterCommon">
			<table width="100%" border="0" style="height: 40px">
				<tr>
					<td width="80" class="etichetta"><bean:message
							key="ricerca.label.elembloccoshort" bundle="acquisizioniLabels" /></td>
					<td width="150" class="testoNormale"><html:text
							property="elemXBlocchi" size="5"></html:text></td>
					<td width="75" class="etichetta"><bean:message
							key="ricerca.label.ordinamento" bundle="acquisizioniLabels" /></td>
					<td width="150" class="testoNormale"><html:select
							property="tipoOrdinamSelez">
							<html:optionsCollection property="listaTipiOrdinam"
								value="codice" label="descrizione" />
						</html:select></td>
				</tr>
			</table>
		</div>
		<div id="divFooter">
			<table align="center" border="0" style="height: 40px">
				<tr>
					<td><sbn:checkAttivita idControllo="CERCA">
							<html:submit styleClass="pulsanti"
								property="methodBuoniRicercaParziale">
								<bean:message key="ricerca.button.cerca"
									bundle="acquisizioniLabels" />
							</html:submit>
						</sbn:checkAttivita> <c:choose>
							<c:when test="${!navForm.LSRicerca}">
								<sbn:checkAttivita idControllo="CREA">
									<html:submit styleClass="pulsanti"
										property="methodBuoniRicercaParziale">
										<bean:message key="ricerca.button.crea"
											bundle="acquisizioniLabels" />
									</html:submit>
								</sbn:checkAttivita>

							</c:when>
						</c:choose> <c:choose>
							<c:when test="${navForm.visibilitaIndietroLS}">
								<html:submit styleClass="pulsanti"
									property="methodBuoniRicercaParziale">
									<bean:message key="ricerca.button.indietro"
										bundle="acquisizioniLabels" />
								</html:submit>
							</c:when>
						</c:choose></td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
