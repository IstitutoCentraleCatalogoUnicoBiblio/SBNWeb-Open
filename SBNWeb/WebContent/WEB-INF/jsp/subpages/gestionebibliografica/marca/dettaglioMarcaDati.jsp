<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area inferiore con i criteri di ricerca
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

<c:choose>
	<c:when test="${dettaglioMarcaForm.tipoProspettazione eq 'DET'}">
		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message
					key="sintetica.livelloaut" bundle="gestioneBibliograficaLabels" />
				</td>

				<td width="100" class="testoNormale"><html:text	property="dettMarcaVO.livAut" size="5" readonly="true"
					title="${dettaglioMarcaForm.descLivAut}">
				</html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message
					key="ricerca.citazionestand" bundle="gestioneBibliograficaLabels" />
				</td>
				<td width="150" class="testoNormale"><html:text
					property="dettMarcaVO.campoCodiceRep1Old" size="5" readonly="true"></html:text>
				<html:text property="dettMarcaVO.campoProgressivoRep1Old" size="5"
					readonly="true"></html:text></td>
				<td width="150" class="testoNormale"><html:text
					property="dettMarcaVO.campoCodiceRep2Old" size="5" readonly="true"></html:text>
				<html:text property="dettMarcaVO.campoProgressivoRep2Old" size="5"
					readonly="true"></html:text></td>
				<td width="150" class="testoNormale"><html:text
					property="dettMarcaVO.campoCodiceRep3Old" size="5" readonly="true"></html:text>
				<html:text property="dettMarcaVO.campoProgressivoRep3Old" size="5"
					readonly="true"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message key="ricerca.mid" bundle="gestioneBibliograficaLabels" /></td>
				<td width="70" class="testoNormale"><html:text property="dettMarcaVO.mid" size="10" readonly="true"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message
					key="ricerca.descrizione" bundle="gestioneBibliograficaLabels" />
				</td>
				<td width="400"><html:textarea
					property="dettMarcaVO.desc" rows="3" cols="70" readonly="true"></html:textarea>
				</td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message key="ricerca.motto" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text	property="dettMarcaVO.motto" readonly="true" size="100"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message
					key="ricerca.parolechiave" bundle="gestioneBibliograficaLabels" />
				</td>
				<td width="20" class="testoNormale"><html:text
					property="dettMarcaVO.parChiave1" size="15" readonly="true"></html:text>
				</td>
				<td width="20" class="testoNormale"><html:text
					property="dettMarcaVO.parChiave2" size="15" readonly="true"></html:text>
				</td>
				<td width="20" class="testoNormale"><html:text
					property="dettMarcaVO.parChiave3" size="15" readonly="true"></html:text>
				</td>
				<td width="20" class="testoNormale"><html:text
					property="dettMarcaVO.parChiave4" size="15" readonly="true"></html:text>
				</td>
				<td width="20" class="testoNormale"><html:text
					property="dettMarcaVO.parChiave5" size="15" readonly="true"></html:text>
				</td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message key="ricerca.nota"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="400" class="testoNormale"><html:text	property="dettMarcaVO.nota" readonly="true" size="100"></html:text></td>
			</tr>
		</table>
	</c:when>
	<c:otherwise>
		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message
					key="sintetica.livelloaut" bundle="gestioneBibliograficaLabels" />
				</td>
				<td width="100">
				<html:select property="dettMarcaVO.livAut" style="width:40px">
				<html:optionsCollection property="listaLivAut" value="codice" label="descrizioneCodice" />
						</html:select>
				</td>
			</tr>
		</table>

<!--	BUG MANTIS 3364 - almaviva2 24.11.2009 - Si porta la citazione a riga nuova per everne la massima lunghezza -->
		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message key="ricerca.citazionestand" bundle="gestioneBibliograficaLabels" />
				</td>
				<td width="150" class="testoNormale"><html:select property="campoCodiceRep1Mod" style="width:90px">
					<html:optionsCollection property="listaRepertori" value="codice" label="descrizioneCodice" style="width:700px"/>
				</html:select> <html:text property="campoProgressivoRep1Mod" size="5"></html:text></td>
			</tr>
			<tr>
				<td width="60" class="etichetta">
				</td>
				<td width="150" class="testoNormale"><html:select property="campoCodiceRep2Mod" style="width:90px">
					<html:optionsCollection property="listaRepertori" value="codice" label="descrizioneCodice" style="width:700px"/>
				</html:select> <html:text property="campoProgressivoRep2Mod" size="5"></html:text></td>
			</tr>
			<tr>
				<td width="60" class="etichetta">
				</td>
				<td width="150" class="testoNormale"><html:select property="campoCodiceRep3Mod" style="width:90px">
					<html:optionsCollection property="listaRepertori" value="codice" label="descrizioneCodice" style="width:700px"/>
				</html:select> <html:text property="campoProgressivoRep3Mod" size="5"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message key="ricerca.mid" bundle="gestioneBibliograficaLabels" /></td>
				<td width="70" class="testoNormale"><html:text property="dettMarcaVO.mid" size="10" readonly="true"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message
					key="ricerca.descrizione" bundle="gestioneBibliograficaLabels" />
				</td>
				<td width="400"><html:textarea property="dettMarcaVO.desc" rows="3" cols="70"></html:textarea></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message key="ricerca.motto" bundle="gestioneBibliograficaLabels" /></td>
				<td width="100" class="testoNormale"><html:text	property="dettMarcaVO.motto" size="100" ></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message
					key="ricerca.parolechiave" bundle="gestioneBibliograficaLabels" />
				</td>
				<td width="20" class="testoNormale"><html:text
					property="dettMarcaVO.parChiave1" size="15"></html:text></td>
				<td width="20" class="testoNormale"><html:text
					property="dettMarcaVO.parChiave2" size="15"></html:text></td>
				<td width="20" class="testoNormale"><html:text
					property="dettMarcaVO.parChiave3" size="15"></html:text></td>
				<td width="20" class="testoNormale"><html:text
					property="dettMarcaVO.parChiave4" size="15"></html:text></td>
				<td width="20" class="testoNormale"><html:text
					property="dettMarcaVO.parChiave5" size="15"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="60" class="etichetta"><bean:message key="ricerca.nota" bundle="gestioneBibliograficaLabels" /></td>
				<td width="400" class="testoNormale"><html:text	property="dettMarcaVO.nota" size="100"></html:text></td>
			</tr>
		</table>
	</c:otherwise>
</c:choose>

<table border="0">
	<tr>
		<td width="80" class="etichetta"><bean:message
			key="ricerca.dataInserimento" bundle="gestioneBibliograficaLabels" />
		</td>
		<td width="80" class="testoNormale"><html:text
			property="dettMarcaVO.dataIns" size="10" readonly="true"></html:text>
		</td>

		<td width="120" class="etichetta"><bean:message
			key="ricerca.dataUltimoAgg" bundle="gestioneBibliograficaLabels" />
		</td>
		<td width="80" class="testoNormale"><html:text
			property="dettMarcaVO.dataAgg" size="10" readonly="true"></html:text>
		</td>
	</tr>
</table>

<c:if test="${dettaglioMarcaForm.tipoProspettazione ne 'DET'}">
	<table border="0">
		<tr>
			<td class="etichetta">
				Carica immagine:&nbsp;
				<html:file property="uploadImmagine" />
			</td>
			<td class="etichetta">
				<html:submit property="methodDettaglioMar">
					<bean:message key="button.caricaImmagine" bundle="gestioneBibliograficaLabels" />
				</html:submit>
			</td>

		</tr>
	</table>
</c:if>

