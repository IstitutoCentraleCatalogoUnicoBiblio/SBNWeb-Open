<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area Cartografia
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
				<table border="0">
					<tr>
						<td width="220" class="etichetta"><bean:message
							key="ricerca.cartog.livAut.specificita"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.livAutSpec" descrizione="descLivAutSpecCar" />
						</td>
					</tr>
				</table>


				<table border="0">
					<tr>
						<td width="220" class="etichetta"><bean:message
							key="ricerca.cartog.pubblicazioneGovernativa"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.pubblicazioneGovernativa" descrizione="descPubblicazioneGovernativa" />
						</td>
					</tr>
					<tr>
						<td  class="etichetta"><bean:message
							key="ricerca.cartog.indicatoreColore"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.indicatoreColore" descrizione="descIndicCol" />
						</td>
					</tr>

					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.proiezioneCarte"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.proiezioneCarte" descrizione="descProiezioneCarte" />
						</td>
					</tr>

					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.supportoFisico"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.supportoFisico" descrizione="descSupportoFisico" />
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.tecnicaCreazione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.tecnicaCreazione" descrizione="descTecnicaCreazione" />
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.formaRiproduzione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.formaRiproduzione" descrizione="descFormaRiproduzione" />
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.formaPubblicazione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.formaPubblicazione" descrizione="descFormaPubblicazione" />
						</td>
					</tr>

					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.forma" bundle="gestioneBibliograficaLabels" />
						</td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.forma" descrizione="descForma" />
						</td>
					</tr>

					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.indicatoreTipoScala"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.indicatoreTipoScala" descrizione="descIndicatoreTipoScala" />
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.tipoScala"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.tipoScala" descrizione="descTipoScala" />
						</td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td width="208" class="etichetta"><bean:message
							key="ricerca.cartog.scala"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><bean:message
							key="ricerca.cartog.scalaH"
							bundle="gestioneBibliograficaLabels" /><html:text
							property="dettTitComVO.detTitoloCarVO.scalaOriz" size="20"
							readonly="true" ></html:text>
							</td>

						<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>

						<td class="testoNormale"><bean:message
							key="ricerca.cartog.scalaV"
							bundle="gestioneBibliograficaLabels" /><html:text
							property="dettTitComVO.detTitoloCarVO.scalaVert" size="20"
							readonly="true" ></html:text>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td width="220" class="etichetta"><bean:message
							key="ricerca.cartog.meridianoOrig"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.meridianoOrigine" descrizione="descMeridianoOrigine" />
						</td>
					</tr>
				</table>

				<!--	Bug collaudo 5009: il formato corretto dele coordinate geografiche non è: 000° 000' 000'' ma 000° 00' 00''
				        Inserite etichetta Max nord e cosi' via per uniformare a Interfaccia diretta
				        Modificato controllo su latitudine affinche in presenza di 000° 00' 00'' cioe EQUATORE non si richieda l'emisfero (N/S) -->

				<table border="0">
					<tr>
						<td width="157"class="etichetta"><bean:message
							key="ricerca.cartog.longitudine"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">Max Ovest&nbsp;&nbsp;<html:text
							property="dettTitComVO.detTitoloCarVO.longitTipo1" size="1"
							readonly="true" ></html:text></td>
						<td>

						<html:text property="longInput1gg" size="1" maxlength="3" readonly="true"></html:text>°
						<html:text property="longInput1pp" size="1" maxlength="2" readonly="true"></html:text>'
						<html:text property="longInput1ss" size="1" maxlength="2" readonly="true"></html:text>"
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td class="testoNormale">Max Est&nbsp;&nbsp;<html:text
							property="dettTitComVO.detTitoloCarVO.longitTipo2" size="1"
							readonly="true" ></html:text></td>
						<td>

						<html:text property="longInput2gg" size="1" maxlength="3" readonly="true"></html:text>°
						<html:text property="longInput2pp" size="1" maxlength="2" readonly="true"></html:text>'
						<html:text property="longInput2ss" size="1" maxlength="2" readonly="true"></html:text>"
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.latitudine"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">Max Nord&nbsp;&nbsp;&nbsp;<html:text
							property="dettTitComVO.detTitoloCarVO.latitTipo1" size="1"
							readonly="true" ></html:text></td>
						<td>

						<html:text property="latiInput1gg" size="1" maxlength="2" readonly="true"></html:text>°
						<html:text property="latiInput1pp" size="1" maxlength="2" readonly="true"></html:text>'
						<html:text property="latiInput1ss" size="1" maxlength="2" readonly="true"></html:text>"
						</td>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td class="testoNormale">Max Sud&nbsp;<html:text
							property="dettTitComVO.detTitoloCarVO.latitTipo2" size="1"
							readonly="true" ></html:text></td>
						<td>

						<html:text property="latiInput2gg" size="1" maxlength="2" readonly="true"></html:text>°
						<html:text property="latiInput2pp" size="1" maxlength="2" readonly="true"></html:text>'
						<html:text property="latiInput2ss" size="1" maxlength="2" readonly="true"></html:text>"
						</td>
					</tr>
				</table>


				<table border="0">

					<tr>
						<td width="220"class="etichetta"><bean:message
							key="ricerca.cartog.carattereImmagine"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.carattereImmagine" descrizione="descCarattereImmagine" />
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.altitudine"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.altitudine" descrizione="descAltitudine" />
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.piattaforma"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.piattaforma" descrizione="descPiattaforma" />
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.categoriaSatellite"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloCarVO.categoriaSatellite" descrizione="descCategoriaSatellite" />
						</td>
					</tr>

				</table>
			</c:when>

			<c:otherwise>
				<table border="0">
					<tr>
						<td width="220"class="etichetta"><bean:message
							key="ricerca.cartog.livAut.specificita"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="400" class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.livAutSpec" style="width:180px">
							<html:optionsCollection property="listaLivAut" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>

					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.pubblicazioneGovernativa"	bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.pubblicazioneGovernativa"
							style="width:180px">
							<html:optionsCollection property="listaPubblicazioneGovernativa"
								value="codice"	label="descrizioneCodice" /></html:select>
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.indicatoreColore"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.indicatoreColore"
							style="width:180px">
							<html:optionsCollection property="listaIndicCol" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>

					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.proiezioneCarte"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.proiezioneCarte"
							style="width:180px">
							<html:optionsCollection property="listaProiezioneCarte"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
					</tr>

					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.supportoFisico"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.supportoFisico"
							style="width:180px">
							<html:optionsCollection property="listaSupportoFisico"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.tecnicaCreazione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.tecnicaCreazione"
							style="width:180px">
							<html:optionsCollection property="listaTecnicaCreazione"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.formaRiproduzione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.formaRiproduzione"
							style="width:180px">
							<html:optionsCollection property="listaFormaRiproduzione"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.formaPubblicazione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.formaPubblicazione"
							style="width:180px">
							<html:optionsCollection property="listaFormaPubblicazione"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
					</tr>

					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.forma" bundle="gestioneBibliograficaLabels" />
						</td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.forma" style="width:180px">
							<html:optionsCollection property="listaForma" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>

					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.indicatoreTipoScala"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.indicatoreTipoScala"
							style="width:180px">
							<html:optionsCollection property="listaIndicatoreTipoScala"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.tipoScala"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.tipoScala"
							style="width:180px">
							<html:optionsCollection property="listaTipoScala" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</table>


				<table border="0">
					<tr>
						<td width="208"class="etichetta"><bean:message
							key="ricerca.cartog.scala"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
						<bean:message key="ricerca.cartog.scalaH" bundle="gestioneBibliograficaLabels" />
						<html:text property="dettTitComVO.detTitoloCarVO.scalaOriz"
							size="17"></html:text>
						</td>

						<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>

						<td class="testoNormale">
						<bean:message key="ricerca.cartog.scalaV" bundle="gestioneBibliograficaLabels" />
						<html:text property="dettTitComVO.detTitoloCarVO.scalaVert"
							size="17"></html:text>
						</td>
					</tr>
				</table>


				<table border="0">
					<tr>
						<td width="220"class="etichetta"><bean:message
							key="ricerca.cartog.meridianoOrig"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.meridianoOrigine"
							style="width:180px">
							<html:optionsCollection property="listaMeridianoOrigine"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</table>



				<!--	Bug collaudo 5009: il formato corretto dele coordinate geografiche non è: 000° 000' 000'' ma 000° 00' 00''
						Inserite etichetta Max nord e cosi' via per uniformare a Interfaccia diretta  -->
				<table border="0">
					<tr>
						<td width="157" class="etichetta"><bean:message
							key="ricerca.cartog.longitudine"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">Max Ovest&nbsp;&nbsp;<html:select
							property="dettTitComVO.detTitoloCarVO.longitTipo1"
							style="width:40px">
							<html:optionsCollection property="listaLongitudine" value="codice"
								label="descrizioneCodice" />
						</html:select>
						</td>
						<td>

						<html:text property="longInput1gg" size="1" maxlength="3"></html:text>°
						<html:text property="longInput1pp" size="1" maxlength="2"></html:text>'
						<html:text property="longInput1ss" size="1" maxlength="2"></html:text>"
						</td>

						<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>

						<td class="testoNormale">Max Est&nbsp;&nbsp;<html:select
							property="dettTitComVO.detTitoloCarVO.longitTipo2"
							style="width:40px">
							<html:optionsCollection property="listaLongitudine" value="codice"
								label="descrizioneCodice" />
						</html:select>
						</td>
						<td>

						<html:text property="longInput2gg" size="1" maxlength="3"></html:text>°
						<html:text property="longInput2pp" size="1" maxlength="2"></html:text>'
						<html:text property="longInput2ss" size="1" maxlength="2"></html:text>"
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.latitudine"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">Max Nord&nbsp;&nbsp;&nbsp;<html:select
							property="dettTitComVO.detTitoloCarVO.latitTipo1"
							style="width:40px">
							<html:optionsCollection property="listaLatitudine" value="codice"
								label="descrizioneCodice" />
						</html:select>
						</td>
						<td>

						<html:text property="latiInput1gg" size="1" maxlength="2"></html:text>°
						<html:text property="latiInput1pp" size="1" maxlength="2"></html:text>'
						<html:text property="latiInput1ss" size="1" maxlength="2"></html:text>"
						</td>

						<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>

						<td class="testoNormale">Max Sud&nbsp;<html:select
							property="dettTitComVO.detTitoloCarVO.latitTipo2"
							style="width:40px">
							<html:optionsCollection property="listaLatitudine" value="codice"
								label="descrizioneCodice" />
						</html:select>
						</td>
						<td>

						<html:text property="latiInput2gg" size="1" maxlength="2"></html:text>°
						<html:text property="latiInput2pp" size="1" maxlength="2"></html:text>'
						<html:text property="latiInput2ss" size="1" maxlength="2"></html:text>"
						</td>
					</tr>
				</table>

				<table border="0">
					<tr>
						<td width="220"class="etichetta"><bean:message
							key="ricerca.cartog.carattereImmagine"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.carattereImmagine"
							style="width:180px">
							<html:optionsCollection property="listaCarattereImmagine"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.altitudine"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.altitudine"
							style="width:180px">
							<html:optionsCollection property="listaAltitudine" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.piattaforma"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.piattaforma"
							style="width:180px">
							<html:optionsCollection property="listaPiattaforma"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.cartog.categoriaSatellite"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloCarVO.categoriaSatellite"
							style="width:180px">
							<html:optionsCollection property="listaCategoriaSatellite"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
					</tr>

				</table>
			</c:otherwise>
		</c:choose>
</table>
