<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Tipo materiale Moderno/Antico
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

		<sbn:navform action="/gestionebibliografica/titolo/interrogazioneListeDiConfronto.do" enctype="multipart/form-data">

			<div id="divForm">
			<div id="divMessaggio"><sbn:errors bundle="gestioneBibliograficaMessages" /></div>

				<hr color="#dde8f0" />

				<table align="center" border="0" width="95%">
					<tr>
						<td class="schedaOff" colspan="2"><bean:message	key="label.caricaListaConfrontoDaFile" bundle="gestioneBibliograficaLabels" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
				</table>
				<table align="center" border="0" width="95%">
					<tr>
						<td class="etichetta">Carica file:&nbsp; <html:file property="uploadImmagine" /></td>

						<td class="etichetta">
							<bean:message key="label.caricaConFusioneAutomatica"	bundle="gestioneBibliograficaLabels" />
							<html:checkbox property="caricaConFusioneAutomatica" /><html:hidden property="caricaConFusioneAutomatica" value="false" />
						</td>

						<td class="etichetta">
							<html:submit property="methodInterrogLisConf"><bean:message key="button.caricaTabelleDB" bundle="gestioneBibliograficaLabels" />
							</html:submit>
						</td>
					</tr>
				</table>



				<table align="center" border="0" width="95%">
					<tr>
						<td class="schedaOff" colspan="2"><bean:message	key="label.interrogaListaConfronto" bundle="gestioneBibliograficaLabels" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
				</table>
				<table align="center" border="0" width="95%">
					<tr>
						<td class="etichetta"><bean:message key="ricerca.listeConf.nomeLista" bundle="gestioneBibliograficaLabels" />:</td>
						<td class="testoNormale"><html:select
							property="dataListaSelez" style="width:300px">
							<html:optionsCollection property="listaDataLista" value="codice" label="descrizione" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="ricerca.listeConf.statoLavorRecord" bundle="gestioneBibliograficaLabels" />:</td>
						<td class="testoNormale"><html:select
							property="statoLavorRecordSelez" style="width:300px">
							<html:optionsCollection property="listaStatoLavorRecord" value="codice" label="descrizione" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><html:submit property="methodInterrogLisConf">
							<bean:message key="ricerca.button.cerca" bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
					</tr>
				</table>


				<table align="center" border="0" width="95%">
					<tr>
						<td class="schedaOff" colspan="2"><bean:message	key="label.cancellaListaConfronto" bundle="gestioneBibliograficaLabels" /></td>
						<td colspan="2">&nbsp;</td>
					</tr>
				</table>
				<table align="center" border="0" width="95%">
					<tr>
						<td class="etichetta"><bean:message key="label.cancellaListaConfronto.attenzione1" bundle="gestioneBibliograficaLabels" /></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="label.cancellaListaConfronto.attenzione2" bundle="gestioneBibliograficaLabels" /></td>
					</tr>
				</table>
				<table align="center" border="0" width="95%">
					<tr>
						<td class="etichetta"><bean:message key="ricerca.listeConf.nomeLista" bundle="gestioneBibliograficaLabels" />:</td>
						<td class="testoNormale"><html:select
							property="dataListaSelezPerCanc" style="width:300px">
							<html:optionsCollection property="listaDataLista" value="codice" label="descrizione" />
						</html:select></td>
						<td class="etichetta"><html:submit property="methodInterrogLisConf">
							<bean:message key="button.cancellaTabelleDB" bundle="gestioneBibliograficaLabels" />
						</html:submit></td>
					</tr>
				</table>


			</div>


			<div id="divFooter">
		</div>
	</sbn:navform>


</layout:page>
