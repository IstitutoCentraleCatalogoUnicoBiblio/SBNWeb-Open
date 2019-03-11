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

<%@ page import="it.iccu.sbn.web.actionforms.gestionebibliografica.marca.InterrogazioneMarcaForm"%>

<html:xhtml />
<layout:page>


	<sbn:navform action="/gestionebibliografica/marca/interrogazioneMarca.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors
			bundle="gestioneBibliograficaMessages" /></div>
		<c:choose>
			<c:when test="${interrogazioneMarcaForm.provenienza eq 'NEWLEGAME'}">
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
				<td width="80" class="etichetta"><bean:message
					key="ricerca.descrizione" bundle="gestioneBibliograficaLabels" />:
				</td>
				<td class="testoNormale"><html:text
					property="interrGener.descrizione" size="60"></html:text>
					<sbn:tastiera limit="60" property="interrGener.descrizione" name="interrogazioneMarcaForm"></sbn:tastiera></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="80" class="etichetta"><bean:message
					key="ricerca.parolechiave" bundle="gestioneBibliograficaLabels" />:
				</td>
				<td class="testoNormale"><html:text
					property="interrGener.parolaChiave1" size="20"></html:text><sbn:tastiera limit="20" property="interrGener.parolaChiave1" name="interrogazioneMarcaForm"></sbn:tastiera></td>
				<td class="testoNormale"><html:text
					property="interrGener.parolaChiave2" size="20"></html:text><sbn:tastiera limit="20" property="interrGener.parolaChiave2" name="interrogazioneMarcaForm"></sbn:tastiera></td>
				<td class="testoNormale"><html:text
					property="interrGener.parolaChiave3" size="20"></html:text><sbn:tastiera limit="20" property="interrGener.parolaChiave3" name="interrogazioneMarcaForm"></sbn:tastiera></td>
			</tr>
		</table>

<!--	BUG MANTIS 3364 - almaviva2 24.11.2009 - Si porta la citazione a riga nuova per everne la massima lunghezza -->
		<table border="0">
			<tr>
				<td width="80" class="etichetta"><bean:message key="ricerca.mid"
					bundle="gestioneBibliograficaLabels" />:</td>
				<td width="110" class="testoNormale"><html:text
					property="interrGener.mid" size="10"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="100" class="etichetta"><bean:message
					key="ricerca.citazionestand" bundle="gestioneBibliograficaLabels" />:
				</td>
				<td width="100" class="testoNormale"><html:select property="interrGener.citazioneStandardSelez" style="width:100px">
					<html:optionsCollection	property="interrGener.listaCitazioneStandard" value="codice" label="descrizioneCodice" style="width:700px"/>

				</html:select></td>
				<td width="100" class="testoNormale"><html:text
					property="interrGener.siglaRepertorio" size="30"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="80" class="etichetta"><bean:message key="ricerca.motto"
					bundle="gestioneBibliograficaLabels" />:</td>
				<td class="testoNormale"><html:text property="interrGener.motto"
					size="60"></html:text>
					<sbn:tastiera limit="60" property="interrGener.motto" name="interrogazioneMarcaForm"></sbn:tastiera>
</td>
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
					property="interrGener.tipoOrdinamSelez" style="width:200px">
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
				<td width="30" class="etichetta"><bean:message key="ricerca.locale"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="30"><html:checkbox property="interrGener.ricLocale"
					value="true">
				</html:checkbox>
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
				<td align="center"><html:submit property="methodInterrogMar">
					<bean:message key="ricerca.button.cerca"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>
				<c:choose>
					<c:when test="${interrogazioneMarcaForm.livRicerca eq 'I' and interrogazioneMarcaForm.creaMar eq 'SI'}">
						<td align="center"><html:submit property="methodInterrogMar">
							<bean:message key="button.creaMar"
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
((InterrogazioneMarcaForm)session.getAttribute("interrogazioneMarcaForm")).getInterrGener().setRicLocale(false);
((InterrogazioneMarcaForm)session.getAttribute("interrogazioneMarcaForm")).getInterrGener().setRicIndice(false);
%>