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

<%@ page import="it.iccu.sbn.web.actionforms.gestionebibliografica.autore.InterrogazioneAutoreForm" %>

<html:xhtml/>
<layout:page>
	<sbn:navform
		action="/gestionebibliografica/autore/interrogazioneAutore.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors
			bundle="gestioneBibliograficaMessages" /></div>
		<c:choose>
			<c:when test="${interrogazioneAutoreForm.provenienza eq 'NEWLEGAME'}">
				<table border="0">
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.titoloRiferimento"
							bundle="gestioneBibliograficaLabels" />:</td>
						<td width="20" class="testoNormale"><html:text
							property="areaDatiLegameTitoloVO.bidPartenza" size="10"
							readonly="true"></html:text></td>
						<td width="150" class="etichetta"><html:text
							property="areaDatiLegameTitoloVO.descPartenza" size="50"
							readonly="true"></html:text></td>
					</tr>
				</table>
				<hr color="#dde8f0" />
			</c:when>
		</c:choose>

		<table border="0">
			<tr>
				<td width="90" class="etichetta"><bean:message key="ricerca.nome"
					bundle="gestioneBibliograficaLabels" />:</td>
				<td class="testoNormale"><html:text property="interrGener.nome"
					size="70" maxlength="80"></html:text>
					<sbn:tastiera limit="80" property="interrGener.nome" name="interrogazioneAutoreForm"></sbn:tastiera>
					</td>
				<td class="etichetta"><bean:message key="ricerca.inizio"
					bundle="gestioneBibliograficaLabels" /> <html:radio
					property="interrGener.tipoRicerca" value="inizio" /> <bean:message
					key="ricerca.intero" bundle="gestioneBibliograficaLabels" /> <html:radio
					property="interrGener.tipoRicerca" value="intero" /> <bean:message
					key="ricerca.parole" bundle="gestioneBibliograficaLabels" /> <html:radio
					property="interrGener.tipoRicerca" value="parole" /></td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="90" class="etichetta"><bean:message key="ricerca.vid"
					bundle="gestioneBibliograficaLabels" />:</td>
				<td width="60" class="testoNormale"><html:text
					property="interrGener.vid" size="10" maxlength="10"></html:text></td>

				<!--
				// Febbraio 2016: gestione ISNI (International standard number identifier)
				ATTENZIONE su CercaAutore e su interrogazioneAutore.jsp viene asteriscata la ricerca per ISADN
				ma non sostituita da quella per ISNI: in attesa dell'Indice
				<td width="50" class="etichetta"><bean:message key="ricerca.isadn"
					bundle="gestioneBibliograficaLabels" />:</td>
				<td width="40" class="testoNormale"><html:text
					property="interrGener.isadn" size="20" maxlength="30"></html:text></td>
				-->

			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="90" class="etichetta"><bean:message
					key="ricerca.tiponome" bundle="gestioneBibliograficaLabels" />:</td>
				<td width="100"><bean:message key="ricerca.tuttiNomi"
					bundle="gestioneBibliograficaLabels" /> <html:radio
					property="tipoAutore" value="tuttiNomi" onchange="this.form.submit()"/></td>
				<td width="180"><bean:message key="ricerca.nomePersonale"
					bundle="gestioneBibliograficaLabels" /> <html:radio
					property="tipoAutore" value="autorePersonale" onchange="this.form.submit()"/></td>
				<td><bean:message key="ricerca.nomeCollettivo"
					bundle="gestioneBibliograficaLabels" /> <html:radio
					property="tipoAutore" value="autoreCollettivo" onchange="this.form.submit()"/></td>
			</tr>

			<tr>
				<td width="90"></td>
				<td width="100" class="etichetta">
				<td width="180" class="etichetta"><label for="tiponomeA"><bean:message
					key="ricerca.tiponomeA" bundle="gestioneBibliograficaLabels" /> </label>
				<html:checkbox styleId="chkTipoNomeA"
					property="interrGener.chkTipoNomeA" /> <label for="tiponomeB"><bean:message
					key="ricerca.tiponomeB" bundle="gestioneBibliograficaLabels" /> </label>
				<html:checkbox styleId="chkTipoNomeB"
					property="interrGener.chkTipoNomeB" /> <label for="tiponomeC"><bean:message
					key="ricerca.tiponomeC" bundle="gestioneBibliograficaLabels" /> </label>
				<html:checkbox styleId="chkTipoNomeC"
					property="interrGener.chkTipoNomeC" /> <label for="tiponomeD"><bean:message
					key="ricerca.tiponomeD" bundle="gestioneBibliograficaLabels" /> </label>
				<html:checkbox styleId="chkTipoNomeD" property="interrGener.chkTipoNomeD" />
				</td>
				<td><label for="tiponomeE"><bean:message
					key="ricerca.tiponomeE" bundle="gestioneBibliograficaLabels" /> </label>
				<html:checkbox styleId="chkTipoNomeE"
					property="interrGener.chkTipoNomeE" /> <label for="tiponomeR"><bean:message
					key="ricerca.tiponomeR" bundle="gestioneBibliograficaLabels" /> </label>
				<html:checkbox styleId="chkTipoNomeR"
					property="interrGener.chkTipoNomeR" /> <label for="tiponomeG"><bean:message
					key="ricerca.tiponomeG" bundle="gestioneBibliograficaLabels" /> </label>
				<html:checkbox styleId="chkTipoNomeG"
					property="interrGener.chkTipoNomeG" /></td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="90"><bean:message key="ricerca.forma"
					bundle="gestioneBibliograficaLabels" />:</td>
				<td>
				<bean:message key="ricerca.tuttiNomi" bundle="gestioneBibliograficaLabels" /><html:radio property="interrGener.formaAutore" value="tutti" />
				<bean:message key="ricerca.autore" bundle="gestioneBibliograficaLabels" /><html:radio property="interrGener.formaAutore" value="autore" />
				<bean:message key="ricerca.rinvio" bundle="gestioneBibliograficaLabels" /><html:radio property="interrGener.formaAutore" value="rinvio" />
				</td>
			</tr>
		</table>





		<table border="0">
			<tr>
				<td width="90" class="etichetta"><bean:message key="ricerca.paese"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="30" class="testoNormale"><html:select
					property="interrGener.paeseSelez" style="width:70px">
					<html:optionsCollection property="interrGener.listaPaese"
						value="codice" label="descrizioneCodice" />
				</html:select></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="105" class="etichetta"><bean:message
					key="ricerca.annonascita" bundle="gestioneBibliograficaLabels" />
				<bean:message key="ricerca.da" bundle="gestioneBibliograficaLabels" />
				</td>
				<td width="30" class="testoNormale"><html:text
					property="interrGener.dataAnnoNascitaDa" size="4"></html:text></td>
				<td width="5" class="etichetta"><bean:message key="ricerca.a"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="50" class="testoNormale"><html:text
					property="interrGener.dataAnnoNascitaA" size="4"></html:text></td>
				<td width="20" class="testoNormale"></td>
				<td width="105" class="etichetta"><bean:message
					key="ricerca.annomorte" bundle="gestioneBibliograficaLabels" /> <bean:message
					key="ricerca.da" bundle="gestioneBibliograficaLabels" /></td>
				<td width="5" class="testoNormale"><html:text
					property="interrGener.dataAnnoMorteDa" size="4"></html:text></td>
				<td width="5" class="etichetta"><bean:message key="ricerca.a"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="20" class="testoNormale"><html:text
					styleId="testoNormale" property="interrGener.dataAnnoMorteA"
					size="4"></html:text></td>
			</tr>
		</table>
		</div>
		<div id="divFooterCommon">
		<table border="0">
			<tr>
				<td width="80" class="etichetta"><bean:message
					key="ricerca.elementiBlocco" bundle="gestioneBibliograficaLabels" /></td>
				<td width="150" class="testoNormale"><html:text
					property="interrGener.elemXBlocchi" size="5"></html:text></td>
				<td width="75" class="etichetta"><bean:message
					key="ricerca.ordinamento" bundle="gestioneBibliograficaLabels" /></td>
				<td width="150" class="testoNormale"><html:select
					property="interrGener.tipoOrdinamSelez" style="width:150px">
					<html:optionsCollection property="interrGener.listaTipiOrdinam"
						value="codice" label="descrizione" />
				</html:select></td>
			</tr>
		</table>


		<table align="center" border="0">
			<tr>
				<td width="85" class="etichetta"><bean:message
					key="ricerca.livelloRicerca" bundle="gestioneBibliograficaLabels" />
				</td>

				<c:choose>
					<c:when test="${interrogazioneAutoreForm.presenzaRicLocale eq 'NO'}">
						<td width="30" class="etichetta"><bean:message key="ricerca.locale"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="30">
						<html:checkbox property="interrGener.ricLocale"	disabled="true"></html:checkbox>
					</c:when>
					<c:otherwise>
						<td width="30" class="etichetta"><bean:message key="ricerca.locale"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="30">
						<html:checkbox property="interrGener.ricLocale"	value="true">
						</html:checkbox>
					</c:otherwise>
				</c:choose>

				</td>
				<td width="30" class="etichetta"><bean:message key="ricerca.indice"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="20"><html:checkbox property="interrGener.ricIndice"
					value="true"></html:checkbox>
				</td>
			</tr>
		</table>
		</div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodInterrogAut">
					<bean:message key="ricerca.button.cerca"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
				<c:choose>
					<c:when test="${interrogazioneAutoreForm.livRicerca eq 'I' and interrogazioneAutoreForm.creaAut eq 'SI'}">
						<td align="center"><html:submit property="methodInterrogAut">
							<bean:message key="button.creaAut"
								bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
						<td align="center"><html:submit property="methodInterrogAut">
							<bean:message key="button.creaAutLoc"
								bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
					</c:when>
					<c:when test="${interrogazioneAutoreForm.livRicerca eq 'P'and interrogazioneAutoreForm.creaAutLoc eq 'SI'}">
						<td align="center"><html:submit property="methodInterrogAut">
							<bean:message key="button.creaAutLoc"
								bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
					</c:when>
				</c:choose>
			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>

<%
	((InterrogazioneAutoreForm)session.getAttribute("interrogazioneAutoreForm")).getInterrGener().setRicLocale(false);
	((InterrogazioneAutoreForm)session.getAttribute("interrogazioneAutoreForm")).getInterrGener().setRicIndice(false);
%>
