<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>

<html:xhtml />

<layout:page>

	<script>
function getCookie(NameOfCookie){
    if (document.cookie.length > 0) {
    begin = document.cookie.indexOf(NameOfCookie+"=");
    if (begin != -1) {
      begin += NameOfCookie.length+1;
      end = document.cookie.indexOf(";", begin);
      if (end == -1) end = document.cookie.length;
        return unescape(document.cookie.substring(begin, end));
    }
  }
  return null;
}
/*
function setCookie(NameOfCookie, value, expiredays) {
var ExpireDate = new Date ();
ExpireDate.setTime(ExpireDate.getTime() + (expiredays * 24 * 3600 * 1000));

alert ("Expiry date" + ExpireDate);

  document.cookie = NameOfCookie + "=" + escape(value) +
  ((expiredays == null) ? "" : "; expires=" + ExpireDate.toGMTString());
}

function delCookie (NameOfCookie) {
  if (getCookie(NameOfCookie)) {
    document.cookie = NameOfCookie + "=" + "; expires=Thu, 01-Jan-70 00:00:01 GMT";
  }
}
*/


function createCookie()
{
var date = new Date();
var ck = "";
var name = "";
var expires = "";
var value = "";
var filled = 0;
var timeout = 30000;
var filler = "000000000000000000";
var itemid = "";
var cdPolo = "";
var cdBib = "";
var cdSerie = "";
var inventario = "";
var agt=navigator.userAgent.toLowerCase();
if (agt.indexOf("firefox") != -1) timeout = 60000;
date.setTime(date.getTime()+timeout);
expires = "; expires="+date.toGMTString()
name = "BRFIDLS_WRITEITEM=";
ck = ck + name;
// Prendi i valori che compongono l'inventario dal form
cdPolo = document.forms[0].recInvDett_codPolo.value; // "CSW"; //document.forms[0].cdPolo.value;
cdBib =  document.forms[0].codBib.value; // '_IC'; //document.forms[0].cdBib.value;
cdSerie = document.forms[0].recInvDett_codSerie.value; // document.forms[0].cdSerie.value(); //"CAT";  //  //
inventario = document.forms[0].recInvDett_codInvent.value;  //document.forms[0].inventario.value; // "123456789"; //
// Prepara il codice inventario da scrivere
//itemid = filler.substr(0, 3 - cdPolo.length);
itemid = itemid + cdPolo + ",";
//itemid = itemid + filler.substr(0, 3 - cdBib.length);
itemid = itemid + cdBib + ",";
//itemid = itemid + filler.substr(0, 3 - cdSerie.length);
itemid = itemid + cdSerie + ",";
//itemid = itemid + filler.substr(0, 9 - inventario.length);
itemid = itemid + inventario;
value = itemid;
if (value) {
ck = ck + value;
filled = 1;
}

//if (filled)
//	ck = ck.substring(0, ck.length-1);

ck = ck + expires+"; path=/";
if (filled) {
	document.cookie = ck;
// alert('Cookie\n' + ck + '\ncreato');
}
}












</script>
	<sbn:navform
		action="/documentofisico/esameCollocazioni/esameCollocEsaminaInventario.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<input type="hidden" name="recInvDett_codMatInv"
				value="${navForm.recInvDett.codMatInv}" />
			<!--  biblioteca -->
			<table width="100%" border="0">
				<tr>
					<td colspan="3"><bean:message
							key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" />
						<input type="hidden" name="recInvDett_codPolo"
						value="${navForm.recInvDett.codPolo}" />
						<html:text readonly="true" styleId="testoNormale"
							property="codBib" size="5" maxlength="3"></html:text> <bean-struts:write
							name="navForm" property="descrBib" />
					</td>
				</tr>
			</table>
			<br>
			<c:choose>
				<c:when test="${navForm.prov eq 'collDef'}">
					<table width="100%">
						<tr>
							<td width="15%"><bean:message key="documentofisico.titoloT"
									bundle="documentoFisicoLabels" />
							</td>
							<td colspan="3"><bean-struts:write
									name="navForm"
									property="recInvDett.bid" />&nbsp;&nbsp;<bean-struts:write
									name="navForm"
									property="recInvDett.titIsbd" />
							</td>
						</tr>
						<c:choose>
							<c:when
								test="${navForm.recInvDett.keyLoc ne 0}">
								<tr>
									<td width="15%"><bean:message
											key="documentofisico.titoloColl"
											bundle="documentoFisicoLabels" />
									</td>
									<td colspan="3"><bean-struts:write
											name="navForm"
											property="recInvDett.collBidLoc" /> <bean-struts:write
											name="navForm"
											property="isbdDiCollocazione" />
									</td>
								</tr>
							</c:when>
						</c:choose>
						<tr>
							<td><bean:message key="documentofisico.inventarioT"
									bundle="documentoFisicoLabels" />
							</td>
							<td colspan="4"><input type="hidden"
								name="recInvDett_codSerie"
								value="${navForm.recInvDett.codSerie}" />
								<input type="hidden" name="recInvDett_codInvent"
								value="${navForm.recInvDett.codInvent}" />
								<html:text styleId="testoNormale" readonly="true"
									name="navForm"
									property="recInvDett.codSerie" size="10"></html:text> <html:text
									styleId="testoNormale" readonly="true"
									name="navForm"
									property="recInvDett.codInvent" size="15"></html:text>
							</td>
						</tr>
						<tr>
							<td><bean:message key="documentofisico.precisazioneVolumeT"
									bundle="documentoFisicoLabels" />
							</td>
							<td colspan="4"><html:textarea readonly="true" cols="80"
									rows="3" property="recInvDett.precInv"></html:textarea>
							</td>
						</tr>
						<c:choose>
							<c:when
								test="${navForm.recInvDett.keyLoc ne 0}">
								<tr
									style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
									<td><bean:message key="documentofisico.collocazioneT"
											bundle="documentoFisicoLabels" />
									</td>
									<td colspan="4"><html:text styleId="testoNormale"
											readonly="true" property="recInvDett.collCodSez" size="15"></html:text>
										<html:text styleId="testoNormale" readonly="true"
											property="recInvDett.collCodLoc" size="45"></html:text> <html:text
											styleId="testoNormale" readonly="true"
											property="recInvDett.collSpecLoc" size="30"></html:text> <html:text
											styleId="testoNormale" readonly="true"
											property="recInvDett.seqColl" size="25"></html:text>
									</td>
								</tr>
							</c:when>
						</c:choose>
					</table>
				</c:when>
				<c:otherwise>
					<table width="100%">
						<tr>
							<td width="15%"><bean:message key="documentofisico.titoloT"
									bundle="documentoFisicoLabels" />
							</td>
							<td colspan="3"><bean-struts:write
									name="navForm"
									property="recInvDett.bid" />&nbsp;&nbsp;<bean-struts:write
									name="navForm"
									property="recInvDett.titIsbd" />
							</td>
							<c:choose>
								<c:when
									test="${navForm.recInvDett.keyLoc ne 0}">
									<tr>
										<td width="15%"><bean:message
												key="documentofisico.titoloColl"
												bundle="documentoFisicoLabels" />
										</td>
										<td colspan="3"><bean-struts:write
												name="navForm"
												property="recInvDett.collBidLoc" /> <bean-struts:write
												name="navForm"
												property="isbdDiCollocazione" />
										</td>
									</tr>
								</c:when>
							</c:choose>
						</tr>
						<tr>
							<td><bean:message key="documentofisico.inventarioT"
									bundle="documentoFisicoLabels" />
							</td>
							<td colspan="4"><input type="hidden"
								name="recInvDett_codSerie"
								value="${navForm.recInvDett.codSerie}" />
								<input type="hidden" name="recInvDett_codInvent"
								value="${navForm.recInvDett.codInvent}" />
								<html:text styleId="testoNormale" readonly="true"
									name="navForm"
									property="recInvDett.codSerie" size="10"></html:text> <html:text
									styleId="testoNormale" readonly="true"
									name="navForm"
									property="recInvDett.codInvent" size="15"></html:text>
							</td>
						</tr>
						<tr>
							<td><bean:message key="documentofisico.precisazioneVolumeT"
									bundle="documentoFisicoLabels" />
							</td>
							<td colspan="4"><html:textarea readonly="true" cols="80"
									rows="3" property="recInvDett.precInv"></html:textarea>
							</td>
						</tr>
						<c:choose>
							<c:when
								test="${navForm.recInvDett.keyLoc ne 0}">
								<tr
									style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
									<c:choose>
										<c:when
											test="${navForm.recInvDett.keyLocOld ne 0}">
											<td><bean:message
													key="documentofisico.collocazioneTemporaneaT"
													bundle="documentoFisicoLabels" />
											</td>
										</c:when>
										<c:otherwise>
											<td><bean:message key="documentofisico.collocazioneT"
													bundle="documentoFisicoLabels" />
											</td>
										</c:otherwise>
									</c:choose>
									<td colspan="4"><html:text styleId="testoNormale"
											readonly="true" property="recInvDett.collCodSez" size="15"></html:text>
										<html:text styleId="testoNormale" readonly="true"
											property="recInvDett.collCodLoc" size="45"></html:text> <html:text
											styleId="testoNormale" readonly="true"
											property="recInvDett.collSpecLoc" size="30"></html:text> <html:text
											styleId="testoNormale" readonly="true"
											property="recInvDett.seqColl" size="25"></html:text>
									</td>
								</tr>
								<c:choose>
									<c:when
										test="${navForm.recInvDett.keyLocOld ne 0}">
										<tr
											style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
											<td><bean:message
													key="documentofisico.collocazioneDefinitivaT"
													bundle="documentoFisicoLabels" />
											</td>
											<td colspan="4"><html:text styleId="testoNormale"
													readonly="true" property="recInvDett.sezOld" size="15"></html:text>
												<html:text styleId="testoNormale" readonly="true"
													property="recInvDett.locOld" size="45"></html:text> <html:text
													styleId="testoNormale" readonly="true"
													property="recInvDett.specOld" size="30"></html:text> <html:text
													styleId="testoNormale" readonly="true"
													property="recInvDett.seqColl" size="25"></html:text>
											</td>
										</tr>
									</c:when>
								</c:choose>
							</c:when>
						</c:choose>
						<tr>
							<!--  sitAmm + dataIngresso-->
							<td width="15%"><bean:message
									key="documentofisico.situazioneAmminT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testoNormale" readonly="true"
									property="descrSitAmm" size="30"></html:text>
							</td>
							<td><bean:message key="documentofisico.dataIngressoT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testo" readonly="true"
									property="recInvDett.dataIngresso" size="30"></html:text>
							</td>
						</tr>
						<c:choose>
							<c:when
								test="${navForm.recInvDett.codSit eq '3' }">
								<tr>
									<!--  motivo scarico + data scarico-->
									<td width="15%"><bean:message
											key="documentofisico.motivoScaricoT"
											bundle="documentoFisicoLabels" />
									</td>
									<td><html:text styleId="testoNormale" readonly="true"
											property="recInvDett.motivoScaricoDescr" size="30"></html:text>
									</td>
						<c:choose>
							<c:when
								test="${navForm.recInvDett.codSit eq '3'
									 && navForm.recInvDett.codScarico eq 'T'}">
								<tr>
									<!--  verso polo + biblioteca-->
									<td width="15%"><bean:message
											key="documentofisico.versoLaBiblioT"
											bundle="documentoFisicoLabels" />
									</td>
									<td colspan="3"><html:text styleId="testoNormale"
											readonly="true" property="recInvDett.codPoloScar" size="5"></html:text>
										<html:text styleId="testo" readonly="true"
											property="recInvDett.versoBibDescr" size="70"></html:text>
									</td>
								</tr>
							</c:when>
						</c:choose>
									<td><bean:message key="documentofisico.dataScaricoT"
											bundle="documentoFisicoLabels" />
									</td>
									<td><html:text styleId="testo" readonly="true"
											property="recInvDett.dataScarico" size="30"></html:text>
									</td>
								</tr>
								<tr>
									<td><bean:message
											key="documentofisico.numeroBuonoScaricoT"
											bundle="documentoFisicoLabels" />
									</td>
									<td><html:text styleId="testoNormale" readonly="true"
											property="recInvDett.numScarico" size="30"></html:text></td>
									<td><bean:message key="documentofisico.dataDeliberaT"
											bundle="documentoFisicoLabels" />
									</td>
									<td><html:text styleId="testoNormale" readonly="true"
											property="recInvDett.dataDelibScar" size="30"></html:text></td>
								</tr>
								<tr>
									<td><bean:message key="documentofisico.testoDeliberaT"
											bundle="documentoFisicoLabels" />
									</td>
									<td colspan="4"><html:textarea readonly="true" cols="80"
											rows="3" property="recInvDett.deliberaScarico"></html:textarea>
									</td>
								</tr>
							</c:when>
						</c:choose>

						<tr>
							<!-- seconda riga tipoAcq + proven-->
							<td><bean:message key="documentofisico.tipoAcquisizioneT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testoNormale" readonly="true"
									property="descrTipoAcquisizione" size="30"></html:text>
							</td>
							<td><bean:message key="documentofisico.provenienzaT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testo" readonly="true"
									property="recInvDett.descrProven" size="30"></html:text>
							</td>
						</tr>
						<c:choose>
							<c:when
								test="${navForm.recInvDett.codBibO ne ''}">
								<tr>
									<!-- seconda riga bis ordine + fornitore-->
									<td><bean:message key="documentofisico.ordineT"
											bundle="documentoFisicoLabels" />
									</td>
									<td><html:text styleId="testoNormale" readonly="true"
											property="recInvDett.codBibO" size="3"></html:text> <html:text
											styleId="testoNormale" readonly="true"
											property="recInvDett.codTipoOrd" size="3"></html:text> <html:text
											styleId="testoNormale" readonly="true"
											property="recInvDett.annoOrd" size="3"></html:text> <html:text
											styleId="testoNormale" readonly="true"
											property="recInvDett.codOrd" size="10"></html:text>
									</td>
									<td><bean:message key="documentofisico.fornitoreT"
											bundle="documentoFisicoLabels" />
									</td>
									<td><html:text styleId="testo" readonly="true"
											property="recInvDett.descrFornitore" size="30"></html:text>
									</td>
								</tr>
								<tr>
									<!-- seconda riga ter fattura-->
									<td><bean:message key="documentofisico.numFatturaT"
											bundle="documentoFisicoLabels" /></td>
									<td><html:text styleId="testoNormale" readonly="true"
											property="recInvDett.numFattura" size="3"></html:text></td>
									<td><bean:message key="documentofisico.dataFatturaT"
											bundle="documentoFisicoLabels" /></td>
									<td><html:text styleId="testo" readonly="true"
											property="recInvDett.dataFattura" size="30"></html:text>
									</td>
								</tr>
							</c:when>
						</c:choose>
						<tr>
							<!-- terza riga  valore + prezzo-->
							<td><bean:message key="documentofisico.valoreInventarialeT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testo" readonly="true"
									property="recInvDett.valore" size="20"></html:text>
							</td>
							<td><bean:message key="documentofisico.prezzoRealeT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testo" readonly="true"
									property="recInvDett.importo" size="30"></html:text>
							</td>
						</tr>
						<tr>
							<!-- quarta riga tipoMat + statoCons -->
							<td><bean:message key="documentofisico.tipoMaterialeT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testo" readonly="true"
									property="descrMatInv" size="30"></html:text>
							</td>
							<td><bean:message key="documentofisico.tipoStatoConT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testo" readonly="true"
									property="descrStatoConser" size="30"></html:text>
							</td>
						</tr>
						<tr>
							<!-- quinta riga supportoCopia + riproducibilità-->
							<td><bean:message key="documentofisico.supportoCopiaT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testo" readonly="true"
									property="descrSupportoCopia" size="30"></html:text>
							</td>
							<td><bean:message key="documentofisico.riproducibilitaT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testo" readonly="true"
									property="descrRiproducibilta" size="30"></html:text>
							</td>
						</tr>
						<tr>
							<!-- quinta riga bis digitalizzazione + disponibilità da remoto-->
							<td><bean:message
									key="documentofisico.tipoDigitalizzazioneT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testo" readonly="true"
									property="descrTipoDigit" size="30"></html:text>
							</td>
							<td><bean:message key="documentofisico.dispDaRemotoT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testo" readonly="true"
									property="descrDispDaRemoto" size="30"></html:text>
							</td>
						</tr>
						<c:choose>
							<c:when
								test="${navForm.recInvDett.descrRifTecaDigitale ne null}">
								<tr>
									<!-- quinta riga ter teche digitali-->
									<td><bean:message key="documentofisico.rifTecaDigitaleT"
											bundle="documentoFisicoLabels" />
									</td>
									<td colspan="4"><html:text styleId="testo" readonly="true"
											property="descrTecaDigitale" size="30"></html:text>
									</td>
								</tr>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when
								test="${not empty navForm.recInvDett.idAccessoRemoto }">
								<tr>
									<!-- quinta riga quater url accesso remoto-->
									<td><bean:message
											key="documentofisico.datiPerAccessoDaRemotoT"
											bundle="documentoFisicoLabels" />
									</td>
									<td colspan="4"><html:textarea readonly="true" cols="80"
											rows="5" property="recInvDett.idAccessoRemoto"></html:textarea>
									</td>
								</tr>
							</c:when>
						</c:choose>
						<tr>
							<!-- sesta riga tipoFrui + nonDisp-->
							<td><bean:message key="documentofisico.tipoFruizioneT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testo" readonly="true"
									property="descrTipoFruizione" size="40"></html:text>
							</td>
							<td><bean:message key="documentofisico.nonDisponibilePerT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testo" readonly="true"
									property="descrNoDispo" size="30"></html:text>
							</td>
						</tr>
						<tr>
							<!-- settima data redisp-->
							<td colspan="2"></td>
							<td><bean:message key="documentofisico.finoAlT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text styleId="testo" readonly="true"
									property="recInvDett.dataRedisp" size="30"></html:text>
							</td>
							<c:choose>
								<c:when test="${navForm.periodico}">
									<tr>
										<td><bean:message key="documentofisico.annoAbbT"
												bundle="documentoFisicoLabels" />
										</td>
										<td><html:text styleId="testo" readonly="true"
												property="recInvDett.annoAbb" size="10"></html:text>
										</td>
										<td><bean:message key="documentofisico.numVolT"
												bundle="documentoFisicoLabels" />
										</td>
										<td class="testo"><html:text styleId="testo"
												readonly="true" property="recInvDett.numVol" size="10"></html:text>
										</td>
									</tr>
								</c:when>
							</c:choose>
						</tr>
						<!-- ottava note inventario-->
						<c:choose>
							<c:when
								test="${navForm.recInvDett.listaNote ne null}">
								<tr>
									<td class="etichetta" width="20%" valign="top"><bean:message
											key="documentofisico.note" bundle="documentoFisicoLabels" />
									</td>
									<td colspan="5">
										<table width="100%"
											style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
											<tr>
												<td bgcolor="#dde8f0"><bean:message
														key="documentofisico.codiceT"
														bundle="documentoFisicoLabels" />
												</td>
												<td bgcolor="#dde8f0"><bean:message
														key="documentofisico.descrizioneT"
														bundle="documentoFisicoLabels" />
												</td>
											</tr>
											<logic:iterate id="itemNota" property="listaNote"
												name="navForm" indexId="idxNota">
												<tr bgcolor="#FFCC99">
													<td bgcolor="#dde8f0" valign="top"><bean-struts:write
															name="itemNota" property="codice1" />
													</td>
													<td bgcolor="#dde8f0" valign="top"><bean-struts:write
															name="itemNota" property="descrizione1" />
													</td>
												</tr>
											</logic:iterate>
										</table></td>
								</tr>
							</c:when>
						</c:choose>
						<tr>
							<td><bean:message key="documentofisico.dataInserimentoT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><bean-struts:write
									name="navForm"
									property="recInvDett.dataIns" />
							</td>
							<td><bean:message key="documentofisico.dataAggiornamentoT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><bean-struts:write
									name="navForm"
									property="recInvDett.dataAgg" />
							</td>
						</tr>
					</table>
					<table>
						<tr>
							<td width="15%"><bean:message
									key="documentofisico.dataBollettinoNuoveAccessioniT"
									bundle="documentoFisicoLabels" />
							</td>
							<td><html:text property="recInvDett.dataInsPrimaColl"
									readonly="true" size="20"></html:text>
							</td>
						</tr>
					</table>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when
					test="${navForm.recInvDett.codCarico ne ' '}">
					<table width="100%" border="0">
						<tr>
							<td width="15%"><bean:message
									key="documentofisico.motivoCaricoT"
									bundle="documentoFisicoLabels" /></td>
							<td><html:text styleId="testoNormale" readonly="true"
									property="descrCodCarico" size="30"></html:text>
							</td>
						</tr>
						<tr>
							<td width="15%"><bean:message
									key="documentofisico.dataCaricoT"
									bundle="documentoFisicoLabels" /></td>
							<td><html:text styleId="testo" readonly="true"
									property="recInvDett.dataCarico" size="35" maxlength="12"></html:text>
							</td>
							<td><bean:message key="documentofisico.numBuonoCaricoT"
									bundle="documentoFisicoLabels" /></td>
							<td><html:text styleId="testo" readonly="true"
									property="recInvDett.numCarico" size="30" maxlength="9"></html:text>
							</td>
						</tr>
					</table>
				</c:when>
			</c:choose>

		</div>
		<div id="divFooter">
			<!-- BOTTONIERA inserire solo i SOLO td -->
			<table align="center">
				<tr>
					<td><c:choose>
							<c:when
								test="${navForm.prov eq 'posseduto'}">
								<sbn:checkAttivita idControllo="df">
									<sbn:checkAttivita idControllo="DETTAGLIO_MOVIMENTO"
										inverted="true">
										<html:submit styleClass="pulsanti" disabled="false"
											property="methodEsameCollEsInv">
											<bean:message key="documentofisico.bottone.disponibilita"
												bundle="documentoFisicoLabels" />
										</html:submit>
									</sbn:checkAttivita>
									<sbn:checkAttivita idControllo="possessori">
										<html:submit styleClass="pulsanti" disabled="false"
											property="methodEsameCollEsInv">
											<bean:message key="documentofisico.bottone.possessori"
												bundle="documentoFisicoLabels" />
										</html:submit>
									</sbn:checkAttivita>
								</sbn:checkAttivita>
								<html:submit styleClass="pulsanti" disabled="false"
									property="methodEsameCollEsInv">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit>
								<!-- <html:submit styleClass="pulsanti" disabled="false" property="methodEsameCollEsInv"
							onclick="createCookie()">
							<bean:message key="documentofisico.bottone.scriviRfid"
								bundle="documentoFisicoLabels" />
						</html:submit> -->
							</c:when>
							<c:when
								test="${navForm.prov eq 'collDef'}">
								<html:submit styleClass="pulsanti" disabled="false"
									property="methodEsameCollEsInv">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</c:when>
							<c:otherwise>
								<sbn:checkAttivita idControllo="df">
									<sbn:checkAttivita idControllo="DETTAGLIO_MOVIMENTO"
										inverted="true">
										<html:submit styleClass="pulsanti" disabled="false"
											property="methodEsameCollEsInv">
											<bean:message key="documentofisico.bottone.disponibilita"
												bundle="documentoFisicoLabels" />
										</html:submit>
									</sbn:checkAttivita>
									<c:choose>
										<c:when
											test="${navForm.prov eq 'interrogazioneEsame'}">
											<c:choose>
												<c:when
													test="${navForm.descrSitAmm eq 'collocato'}">
													<sbn:checkAttivita idControllo="etichette">
														<html:submit styleClass="pulsanti" disabled="false"
															property="methodEsameCollEsInv"
															onclick="DoTheCookieStuff();">
															<bean:message key="documentofisico.bottone.etichetta"
																bundle="documentoFisicoLabels" />
														</html:submit>
													</sbn:checkAttivita>
													<sbn:checkAttivita idControllo="MODULO_PRELIEVO">
														<html:submit styleClass="pulsanti" disabled="false"	property="${navButtons}" >
															<bean:message key="documentofisico.bottone.moduloPrelievo" bundle="documentoFisicoLabels" />
														</html:submit>
													</sbn:checkAttivita>
												</c:when>
											</c:choose>
										</c:when>
										<c:otherwise>
											<sbn:checkAttivita idControllo="etichette">
												<html:submit styleClass="pulsanti" disabled="false"
													property="methodEsameCollEsInv"
													onclick="DoTheCookieStuff();">
													<bean:message key="documentofisico.bottone.etichetta"
														bundle="documentoFisicoLabels" />
												</html:submit>
											</sbn:checkAttivita>
											<sbn:checkAttivita idControllo="MODULO_PRELIEVO">
												<html:submit styleClass="pulsanti" disabled="false"	property="${navButtons}" >
													<bean:message key="documentofisico.bottone.moduloPrelievo" bundle="documentoFisicoLabels" />
												</html:submit>
											</sbn:checkAttivita>
											<c:choose>
												<c:when
													test="${navForm.recInvDett.codSit ne '3'}">
													<html:submit styleClass="pulsanti" disabled="false"
														property="methodEsameCollEsInv">
														<bean:message key="documentofisico.bottone.modificaInv"
															bundle="documentoFisicoLabels" />
													</html:submit>
												</c:when>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</sbn:checkAttivita>
								<html:submit styleClass="pulsanti" disabled="false"
									property="methodEsameCollEsInv">
									<bean:message key="documentofisico.bottone.indietro"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
			<!--
<p align="center"><a href="#" onclick="createCookie()">Scrivi RFID +++</a></p>
<p align="center"><a href="#" onclick="delCookie('username')">Click here to RESET the cookie.</a></p>
-->
		</div>
	</sbn:navform>
</layout:page>
