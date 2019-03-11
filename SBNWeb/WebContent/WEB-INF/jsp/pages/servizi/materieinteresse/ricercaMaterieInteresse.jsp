<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/materieinteresse/RicercaMaterieInteresse">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />

			</div>
			<br>
			<table width="100%" border="0">
				<tr>
					<td width="24%" align="right" class="etichetta">
						<bean:message key="servizi.utenti.codiceBiblioteca" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="datiRicerca.codBib" size="5" readonly="true"></html:text>
					</td>
				</tr>
				<tr>
					<td class="etichetta" align="right">
						<bean:message key="servizi.materieinteresse.header.desMateria" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="datiRicerca.descrizione" maxlength="30" readonly="false"></html:text>
					</td>
				</tr>
			</table>
		</div>
		<br/>
		<br/>

		<div id="divFooterCommon">
			<table width="100%" border="0" style="height:40px">
				<tr>
					<td width="80" class="etichetta">
						<bean:message key="servizi.label.elementiPerBlocco" bundle="serviziLabels" />
					</td>
					<td width="150" class="testoNormale">
						<html:text property="datiRicerca.numeroElementiBlocco" size="5" maxlength="4" />
					</td>
					<td width="75" class="etichetta">
						<bean:message key="servizi.label.ordinamento" bundle="serviziLabels" />
					</td>
					<td width="150" class="testoNormale">
						<html:select property="datiRicerca.ordinamento" >
							<html:optionsCollection property="lstTipiOrdinamento" value="codice" label="descrizione" />
						</html:select>
					</td>
				</tr>
			</table>
		</div>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center">
						<html:submit property="methodRicMaterie">
							<bean:message key="servizi.bottone.cerca" bundle="serviziLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>
