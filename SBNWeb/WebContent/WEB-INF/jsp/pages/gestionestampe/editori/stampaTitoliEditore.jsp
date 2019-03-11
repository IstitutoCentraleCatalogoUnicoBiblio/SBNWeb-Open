<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>


<html:xhtml />
<layout:page>

	<sbn:navform action="/gestionestampe/editori/stampaTitoliEditore.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<table width="100%" border="0">
				<tr>
					<td colspan="4">
						<div class="etichetta">
							<bean:message key="documentofisico.bibliotecaT"	bundle="documentoFisicoLabels" />
							<html:text disabled="true" styleId="testoNormale" property="codBib" size="5" maxlength="3"></html:text>

							<!--
							Intervento MAggio 2013 - viene eliminato il cartiglio che permette la stampa dei titoli
							delle biblioteche affiliate nella richiesta di stampa Titoli per Editore
							<span disabled="true"> <html:submit
									title="Lista Biblioteche" styleClass="buttonImageListaSezione"
									disabled="false" property="methodStampaTitoliEditore">
									<bean:message key="documentofisico.lsBib"
										bundle="documentoFisicoLabels" />
								</html:submit>
							</span>
							-->
							<bean-struts:write name="stampaTitoliEditoreForm"
								property="descrBib" />
						</div></td>
				</tr>
			</table>
			<table width="100%"
				style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
				<tr>
					<th class="etichetta" width="10%">Editore</th>
				<tr>
					<td><html:radio property="checkEditore" value="editore"
							onchange="this.form.submit()" /> <bean:message
							key="editori.label.editore" bundle="gestioneStampeLabels" /></td>
					<td><html:text styleId="testo" property="codEditore" size="5"
							maxlength="10"
							disabled="${stampaTitoliEditoreForm.disableCodEditore}"></html:text>
						<html:text styleId="testo" property="descrEditore" size="60"
							maxlength="50"
							disabled="${stampaTitoliEditoreForm.disableDescrEditore}"></html:text>
						<html:submit title="Verifica esistenza editore"
							styleClass="buttonImage" property="methodStampaTitoliEditore"
							disabled="${noinputForn}">
							<bean:message key="editori.label.editore"
								bundle="gestioneStampeLabels" />
						</html:submit> <%--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sbn:tastiera limit="80"
							name="stampaTitoliEditoreForm" property="descrEditore"></sbn:tastiera>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message key="ricerca.inizio"
							bundle="acquisizioniLabels" /> <html:radio
							property="checkTipoRicerca" value="inizio" /> <bean:message
							key="ricerca.intero" bundle="acquisizioniLabels" /> <html:radio
							property="checkTipoRicerca" value="intero" /> <bean:message
							key="ricerca.parole" bundle="acquisizioniLabels" /> <html:radio
							property="checkTipoRicerca" value="parole" />--%>
					</td>
				</tr>
				<tr>
					<td><html:radio property="checkEditore" value="isbn"
							onchange="this.form.submit()" /> <bean:message
							key="editori.label.isbn" bundle="gestioneStampeLabels" /></td>
					<td><html:text styleId="testo" property="isbn" size="40"
							maxlength="30" disabled="${stampaTitoliEditoreForm.disableIsbn}"></html:text>
					</td>
				</tr>
				<tr>
					<td><html:radio property="checkEditore" value="regione"
							onchange="this.form.submit()" /> <bean:message
							key="editori.label.regione" bundle="gestioneStampeLabels" />
					</td>
					<td><html:select styleClass="testoNormale" property="regione"
							onchange="this.form.submit()"
							disabled="${stampaTitoliEditoreForm.disableRegione}">
							<html:optionsCollection property="listaRegioni" value="codice"
								label="descrizioneCodice" />
						</html:select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <bean:message
							key="editori.label.provincia" bundle="gestioneStampeLabels" /> <html:select
							styleClass="testoNormale" property="provincia"
							disabled="${stampaTitoliEditoreForm.disableProvincia}">
							<html:optionsCollection property="listaProvincie" value="codice"
								label="descrizioneCodice1" />
						</html:select>
					</td>
				</tr>

				<tr>
					<td><html:radio property="checkEditore" value="paese"
							onchange="this.form.submit()" /> <bean:message
							key="editori.label.paese" bundle="gestioneStampeLabels" />
					</td>
					<td><html:select styleClass="testoNormale" property="paese"
							onchange="this.form.submit()"
							disabled="${stampaTitoliEditoreForm.disablePaese}">
							<html:optionsCollection property="listaPaesi" value="codice"
								label="descrizioneCodice" /></html:select>
					</td>
				</tr>

			</table>
			<table width="100%"
				style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
				<tr>
					<th class="etichetta" width="10%">Titoli</th>
				<tr>
					<td><bean:message key="editori.label.dataPubbli1Da"
							bundle="gestioneStampeLabels" />
					</td>
					<td><html:text styleId="testo" property="dataPubbl1Da"
							size="10" maxlength="4"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message
							key="editori.label.dataPubbliA" bundle="gestioneStampeLabels" />
						<html:text styleId="testo" property="dataPubbl1A" size="10"
							maxlength="4"></html:text>
					</td>
				</tr>
				<tr>
					<td><bean:message key="editori.label.tipoRecord"
							bundle="gestioneStampeLabels" />
					</td>
					<td><html:select property="tipoRecord">
							<html:optionsCollection property="listaTipiRecord" value="codice"
								label="descrizioneCodice" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td><bean:message key="editori.label.lingua"
							bundle="gestioneStampeLabels" />
					</td>
					<td><html:select styleClass="testoNormale" property="lingua">
							<html:optionsCollection property="listaLingue" value="codice"
								label="descrizioneCodice" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td><bean:message key="editori.label.natura"
							bundle="gestioneStampeLabels" /></td>
					<td colspan="3"><html:select property="natura">
							<html:optionsCollection property="listaNature" value="codice"
								label="descrizione" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td><td><bean:message key="editori.label.titTutti"
							bundle="gestioneStampeLabels" /> <html:radio
							property="checkTipoPosseduto" value="titPossTutti" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <bean:message
							key="editori.label.titPossBibl" bundle="gestioneStampeLabels" />
						<html:radio property="checkTipoPosseduto" value="titPossSoloBib" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<bean:message key="editori.label.titNonPossBibl"
							bundle="gestioneStampeLabels" /> <html:radio
							property="checkTipoPosseduto" value="titNonPossBib" /></td>
				</tr>
			</table>
			<table width="100%"
				style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
				<tr>
					<th class="etichetta" width="10%">Inventari</th>
				<tr>
					<td><bean:message key="editori.label.dataIngressoDa"
							bundle="gestioneStampeLabels" />
					</td>
					<td><html:text styleId="testo" property="dataIngressoDa"
							size="25" maxlength="12"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message
							key="editori.label.dataIngressoA" bundle="gestioneStampeLabels" />
						<html:text styleId="testo" property="dataIngressoA" size="25"
							maxlength="12"></html:text></td>
				</tr>
				<tr>
					<td><bean:message key="editori.label.tipoAcq"
							bundle="gestioneStampeLabels" /></td>
					<td colspan="3"><html:select property="tipoAcq">
							<html:optionsCollection property="listaTipoAcq" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
				</tr>

				<tr>
					<td><bean:message key="documentofisico.tipoMaterialeT"
						bundle="documentoFisicoLabels" /></td>
					<td colspan="4"><html:select property="codiceTipoMateriale">
						<html:optionsCollection property="listaTipoMateriale" value="codice"
							label="descrizione" />
					</html:select></td>
				</tr>


			</table>
			<table width="100%"
				style="border-color: #5A96DF; border-style: dotted; border-width: 1px;">
				<tr>
					<th class="etichetta" width="10%">Classificazione</th>
				<tr>
					<td><bean:message key="editori.label.classSistema"
							bundle="gestioneStampeLabels" />
					</td>
					<td><html:select property="sistema">
							<html:optionsCollection property="listaClassificazioni"
								value="codice" label="descrizioneCodice" />
						</html:select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<bean:message
							key="editori.label.classSimbolo" bundle="gestioneStampeLabels" />
					<html:text styleId="testo" property="simbolo" size="25"
							maxlength="12"></html:text></td>
				</tr>
			</table>
			<HR>
		</div>
		<div id="divFooter">
	<table align="center" border="0" style="height:40px" >
		<tr >
			<td>
				<html:submit styleClass="pulsanti" property="methodStampaTitoliEditore">
				<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
				</html:submit>

				<html:submit styleClass="pulsanti" property="methodStampaTitoliEditore">
					<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
				</html:submit>
			</td>
		</tr>
	</table>
  </div>
</sbn:navform>
</layout:page>
