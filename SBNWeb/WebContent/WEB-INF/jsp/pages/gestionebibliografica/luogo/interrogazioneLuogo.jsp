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

<%@ page import="it.iccu.sbn.web.actionforms.gestionebibliografica.luogo.InterrogazioneLuogoForm"%>

<html:xhtml />
<layout:page>


	<sbn:navform action="/gestionebibliografica/luogo/interrogazioneLuogo.do">

		<div id="divForm">

		<div id="divMessaggio"><sbn:errors
			bundle="gestioneBibliograficaMessages" /></div>
		<c:choose>
			<c:when test="${interrogazioneLuogoForm.provenienza eq 'NEWLEGAME'}">
				<table border="0">
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.titoloRiferimento"
							bundle="gestioneBibliograficaLabels" />:</td>
						<td width="20" class="testoNormale"><html:text
							property="areaDatiLegameTitoloVO.bidPartenza" size="10" readonly="true"
							></html:text></td>
						<td width="150" class="etichetta"><html:text
							property="areaDatiLegameTitoloVO.descPartenza" size="50" readonly="true"
							></html:text></td>
					</tr>
				</table>
				<hr color="#dde8f0" />
			</c:when>
		</c:choose>

		<table border="0">
			<tr>
				<td width="90" class="etichetta"><bean:message
					key="ricerca.denominazione" bundle="gestioneBibliograficaLabels" />:</td>
				<td class="testoNormale"><html:text
					property="interrGener.denominazione" size="70" maxlength="80"></html:text>
					<sbn:tastiera limit="80" property="interrGener.denominazione" name="interrogazioneLuogoForm"></sbn:tastiera>
					</td>
				<td class="etichetta">
					<bean:message key="ricerca.inizio"	bundle="gestioneBibliograficaLabels" />
					<html:radio	property="interrGener.tipoRicerca" value="inizio" />
					<bean:message key="ricerca.intero" bundle="gestioneBibliograficaLabels" />
					<html:radio	property="interrGener.tipoRicerca" value="intero" />
				</td>
			</tr>
		</table>
		<table border="0">
			<tr>
				<td width="90" class="etichetta"><bean:message key="ricerca.lid"
					bundle="gestioneBibliograficaLabels" />:</td>
				<td width="60" class="testoNormale"><html:text
					property="interrGener.lid" size="10" maxlength="10"></html:text></td>
			</tr>
		</table>

		<table border="0">
			<tr>
				<td width="90" class="etichetta"><bean:message key="ricerca.nazione"
					bundle="gestioneBibliograficaLabels" /></td>
				<td width="30" class="testoNormale"><html:select
					property="interrGener.paeseSelez" style="width:70px">
					<html:optionsCollection property="interrGener.listaPaese"
						value="codice" label="descrizioneCodice" />
				</html:select></td>
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
				<td align="center"><html:submit property="methodInterrogLuo">
					<bean:message key="ricerca.button.cerca"
						bundle="gestioneBibliograficaLabels" />
				</html:submit></td>

				<c:choose>
					<c:when test="${interrogazioneLuogoForm.livRicerca eq 'I' and interrogazioneLuogoForm.creaLuo eq 'SI'}">
						<td align="center"><html:submit property="methodInterrogLuo">
							<bean:message key="button.creaLuo"
								bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
						<td align="center">
							<!-- la gestione del luogo SOLO LOCALE VIENE ELIMINATA
							<html:submit property="methodInterrogLuo"><bean:message key="button.creaLuoLoc"	bundle="gestioneBibliograficaLabels" /></html:submit>
							-->
						</td>

					</c:when>
					<c:when test="${interrogazioneLuogoForm.livRicerca eq 'P'and interrogazioneLuogoForm.creaLuoLoc eq 'SI'}">
						<td align="center">
						<!-- la gestione del luogo SOLO LOCALE VIENE ELIMINATA
						<html:submit property="methodInterrogLuo"><bean:message key="button.creaLuoLoc"	bundle="gestioneBibliograficaLabels" /></html:submit>
						-->
						</td>
					</c:when>
				</c:choose>
			</tr>
		</table>
		</div>

	</sbn:navform>
</layout:page>

<%
((InterrogazioneLuogoForm)session.getAttribute("interrogazioneLuogoForm")).getInterrGener().setRicLocale(false);
((InterrogazioneLuogoForm)session.getAttribute("interrogazioneLuogoForm")).getInterrGener().setRicIndice(false);
%>