<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<sbn:disableAll checkAttivita="GESTIONE">
	<div>
		<!-- ATTIVITA' SELEZIONATA -->
		<div class="etichetta" style="float: left; width: 60px;">
			<bean:message key="servizi.erogazione.attivita"
				bundle="serviziLabels" />
			&nbsp;&nbsp;
		</div>
		<div style="float: left;">
			<c:choose>
				<c:when test="${navForm.nuovo}">
					<html:select property="iterServizio.codAttivita">
						<html:optionsCollection property="lstCodiciAttivita"
							value="codiceDescrizione" label="descrizioneParentesiCod" />
					</html:select>&nbsp;&nbsp;
							</c:when>
				<c:otherwise>
					<html:text styleId="testoNoBold" readonly="true"
						property="iterServizio.descrizione" size="50"></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</c:otherwise>
			</c:choose>
		</div>
		<!-- OBBLIGATORIETA' -->
		<div style="float: left;">
			<bean:message key="servizi.bottone.obbligatoria"
				bundle="serviziLabels" />
			&nbsp;&nbsp;
			<html:checkbox property="obbligatoria"
				disabled="${navForm.conferma or navForm.confermaContinuaConfigurazione}" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<html:hidden property="obbligatoria" value="false" />
		</div>
		<!-- PROROGABILITA -->
		<div style="float: none;">
			<bean:message key="servizi.bottone.prorogabile"
				bundle="serviziLabels" />
			&nbsp;&nbsp;
			<html:checkbox property="prorogabile"
				disabled="${navForm.conferma or navForm.confermaContinuaConfigurazione}" />
			<html:hidden property="prorogabile" value="false" />
		</div>
	</div>
	<br />

	<br />
	<br />

	<!-- STATO RICHIESTA -->
	<div>
		<div style="float: left; width: 100px">
			<bean:message key="servizi.erogazione.statoRichiesta"
				bundle="serviziLabels" />
		</div>
		<div style="float: none;">
			<html:select property="iterServizio.codStatoRich"
				disabled="${navForm.conferma || navForm.confermaContinuaConfigurazione}">
				<html:optionsCollection property="lstCodiciRichiesta" value="codice"
					label="descrizione" />
			</html:select>
		</div>
		<br />
	</div>
	<!-- STATO MOVIMENTO -->
	<div>
		<div style="float: left; width: 100px">
			<bean:message key="servizi.erogazione.statoMovimento"
				bundle="serviziLabels" />
		</div>
		<div style="float: none;">
			<html:select property="iterServizio.codStatoMov"
				disabled="${navForm.conferma || navForm.confermaContinuaConfigurazione}">
				<html:optionsCollection property="lstCodiciMovimento" value="codice"
					label="descrizione" />
			</html:select>
		</div>
		<br />
	</div>

	<br />
	<hr align="left">
	<br />
	<div class="etichetta"
		style="float: left; width: 300px; font-weight: bold;">
		<bean:message
			key="servizi.configurazione.dettaglioAttivita.stampaModulo"
			bundle="serviziLabels" />
	</div>

	<br />
	<br />
	<div>
		<!-- DETTAGLI STAMPA-->
		<div style="float: left;">
			<bean:message
				key="servizi.configurazione.dettaglioAttivita.numeroCopie"
				bundle="serviziLabels" />
			:&nbsp;&nbsp;
			<html:text property="iterServizio.numPag" size="2" maxlength="2"
				disabled="${navForm.conferma || navForm.confermaContinuaConfigurazione}"></html:text>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<bean:message key="servizi.configurazione.dettaglioAttivita.testo"
				bundle="serviziLabels" />
			:&nbsp;&nbsp;
		</div>
		<div style="float: none;">
			<html:textarea property="iterServizio.testo" cols="50" rows="2"
				disabled="${navForm.conferma || navForm.confermaContinuaConfigurazione}"></html:textarea>
		</div>
	</div>
</sbn:disableAll>
