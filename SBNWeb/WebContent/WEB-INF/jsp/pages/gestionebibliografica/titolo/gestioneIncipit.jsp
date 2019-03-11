<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione Autore
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

<html:xhtml/>
<layout:page>

	<sbn:navform action="/gestionebibliografica/titolo/gestioneIncipit.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors
			bundle="gestioneBibliograficaMessages" /></div>

		<table border="0">
			<tr>
				<td class="etichetta"><bean:message key="ricerca.titoloRiferimento"
					bundle="gestioneBibliograficaLabels" /> :</td>
				<td width="20" class="testoNormale"><html:text property="idOggColl"
					size="10" readonly="true"></html:text></td>
				<td width="150" class="etichetta"><html:text property="descOggColl"
					size="50" readonly="true"></html:text></td>
			</tr>
		</table>
		<hr color="#dde8f0" />


		<c:choose>
			<c:when test="${gestioneIncipitForm.tipoProspettazione eq 'DET'}">
				<table border="0">
					<tr>
						<td width="160" class="etichetta"><bean:message
							key="ricerca.musica.IncipitNComposizione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.numComposizione" size="10"
							readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitNMov"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.numMovimento" size="10"
							readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitNProg"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.numProgDocumento" size="10"
							readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitNomepersonaggio"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.nomePersonaggio" size="10"
							readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitVoceStrumento"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:select property="dettaglioIncipitVO.voceStrumentoSelez">
							<html:optionsCollection property="listaVoceStrumento"
								value="descrizione" label="descrizione" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitFormaMusicale"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:select property="dettaglioIncipitVO.formaMusicaleSelez">
							<html:optionsCollection property="listaFormaMusicale"
								value="descrizione" label="descrizione" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitIndicazMovimento"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.indicazMovimento" size="10"
							readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitTonalita"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:select property="dettaglioIncipitVO.tonalitaSelez">
							<html:optionsCollection property="listaTonalita"
								value="descrizione" label="descrizione" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitChiave"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.chiave" size="10" readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitAlterazioni"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.alterazioni" size="10"
							readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitMisura"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.misura" size="10" readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.ricerca.musica.IncipitCont"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.contestoMusicale" size="80"
							readonly="true"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitIncipitTestuale"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.incipitTestuale" size="10"
							readonly="true"></html:text></td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<table border="0">
					<tr>
						<td width="160" class="etichetta"><bean:message
							key="ricerca.musica.IncipitNComposizione"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.numComposizione" size="10"></html:text>
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitNMov"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.numMovimento" size="10"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitNProg"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.numProgDocumento" size="10"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitNomepersonaggio"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.nomePersonaggio" size="10"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitVoceStrumento"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:select property="voceStrumento">
							<html:optionsCollection property="listaVoceStrumento"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
						<td><html:text styleId="testoNormale"
							property="voceStrumentoNum" size="10"></html:text></td>

					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitFormaMusicale"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:select property="dettaglioIncipitVO.formaMusicaleSelez">
							<html:optionsCollection property="listaFormaMusicale"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitIndicazMovimento"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.indicazMovimento" size="10"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitTonalita"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:select property="dettaglioIncipitVO.tonalitaSelez">
							<html:optionsCollection property="listaTonalita" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitChiave"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.chiave" size="10"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitAlterazioni"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.alterazioni" size="10"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitMisura"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.misura" size="10"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitCont"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.contestoMusicale" size="80"></html:text></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.musica.IncipitIncipitTestuale"
							bundle="gestioneBibliograficaLabels" /></td>
						<td><html:text styleId="testoNormale"
							property="dettaglioIncipitVO.incipitTestuale" size="10"></html:text></td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose></div>

		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodGestIncipit">
					<bean:message key="gestIncipit.button.salva"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
				<td align="center"><html:submit property="methodGestIncipit">
					<bean:message key="gestIncipit.button.annulla"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
			</tr>
		</table>
		</div>
	</sbn:navform>
</layout:page>
