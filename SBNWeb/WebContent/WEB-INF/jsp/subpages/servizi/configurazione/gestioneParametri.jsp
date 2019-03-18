<%@ taglib uri="http://struts.apache.org/tags-html-el"       prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt"            prefix="c"   %>
<%@ taglib uri="http://struts.apache.org/tags-bean-el"       prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>

<sbn:disableAll checkAttivita="GESTIONE">
<br/>
	<div>
		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.dettaglio.ins_richieste_utente" bundle="serviziLabels" />&nbsp;&#x00B9;
		</div>
		<div style="float:none;">
			<html:hidden property="inserimentoUtente" />
			<html:checkbox property="parametriBib.ammessoInserimentoUtente" onclick="validateSubmit('inserimentoUtente', 'SI');" ></html:checkbox>
			<html:hidden property="parametriBib.ammessoInserimentoUtente" value="false"></html:hidden>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

    		<c:choose>
				<c:when test="${navForm.parametriBib.ammessoInserimentoUtente}">
					<bean:message key="servizi.configurazione.dettaglio.anche_da_remoto" bundle="serviziLabels" />&nbsp;&#x00B9;&nbsp;&nbsp;&nbsp;

					<html:checkbox property="parametriBib.ancheDaRemoto" ></html:checkbox>
					<html:hidden property="parametriBib.ancheDaRemoto" value="false"></html:hidden>&nbsp;

				</c:when>
			</c:choose>

		</div>
	</div>
	<br/>

	<div>
		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.parametriBiblioteca.ammessaAutoregistrazioneUtente" bundle="serviziLabels" />&nbsp;&#x00B2;
		</div>
		<div style="float:none;">
			<html:checkbox property="parametriBib.ammessaAutoregistrazioneUtente"></html:checkbox>
			<html:hidden property="parametriBib.ammessaAutoregistrazioneUtente" value="false"></html:hidden>&nbsp;
		</div>
	</div>
	<br/>

	<div>
		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.parametriBiblioteca.mail.nuovaRichiesta" bundle="serviziLabels" />
		</div>
		<div style="float:none;">
			<html:text property="parametriBib.mailNotifica" size="40" maxlength="160" />
		</div>
	</div>
	<br/>

	<div>
		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.parametriBiblioteca.ggValiditaPrelazione" bundle="serviziLabels" />&nbsp;&#x00B3;
		</div>
		<div style="float:none;">
			<html:text property="parametriBib.ggValiditaPrelazione" size="1" maxlength="2" ></html:text>
		</div>
	</div>
	<br/>

	<div>
		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.parametriBiblioteca.numMaxPrenotazioni" bundle="serviziLabels" />
		</div>
		<div style="float:none;">
			<html:text property="parametriBib.numeroPrenotazioni" size="1" maxlength="2" ></html:text>
		</div>
	</div>
	<br/>

	<div>
		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.parametriBiblioteca.prenot.priorita" bundle="serviziLabels" />
		</div>
		<div style="float:none;">
			<html:checkbox property="parametriBib.prioritaPrenotazioni" />
			<html:hidden property="parametriBib.prioritaPrenotazioni" value="false" />
		</div>
	</div>
	<br/>

	<div>
		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.parametriBiblioteca.tipoRinnovo" bundle="serviziLabels" />
		</div>
		<div style="float:none;">
			<html:select property="parametriBib.tipoRinnovo" styleClass="w10em">
				<html:optionsCollection property="listaTipoRinnovo" value="cd_tabellaTrim" label="ds_tabella"/>
			</html:select>
		</div>
	</div>
	<br/>

	<div>
		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.parametriBiblioteca.ggRinnovoRichiesta" bundle="serviziLabels" />
		</div>
		<div style="float:none;">
			<html:text property="parametriBib.ggRinnovoRichiesta" size="1" maxlength="2" ></html:text>
		</div>
	</div>
	<br/>

	<div>
		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.parametriBiblioteca.numMaxLettere" bundle="serviziLabels" />
		</div>
		<div style="float:none;">
			<html:hidden property="cambiato" />
			<html:select property="parametriBib.numeroLettere" onchange="validateSubmit('cambiato', 'SI');" >
				<html:optionsCollection property="lstNumMaxSolleciti" value="codice" label="codice"/>
			</html:select>
		</div>
	</div>
	<br/>

    <c:choose>
		<c:when test="${navForm.parametriBib.numeroLettere ge 1}">
			<div>
				<div style="float:left; width:35%">
					<bean:message key="servizi.configurazione.parametriBiblioteca.ggPrimaLettera" bundle="serviziLabels" />
				</div>
				<div style="float:none;">
					<html:text property="parametriBib.ggRitardo1" size="1" maxlength="2" ></html:text>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<bean:message key="servizi.configurazione.parametriBiblioteca.modInvio1" bundle="serviziLabels" />
					<html:select property="parametriBib.codModalitaInvio1Sollecito1">
						<html:optionsCollection property="lstModalitaInvio" value="codice" label="descrizione"/>
					</html:select>
					&nbsp;&nbsp;&nbsp;
					<bean:message key="servizi.configurazione.parametriBiblioteca.modInvio2" bundle="serviziLabels" />
					<html:select property="parametriBib.codModalitaInvio2Sollecito1">
						<html:optionsCollection property="lstModalitaInvio" value="codice" label="descrizione"/>
					</html:select>
					&nbsp;&nbsp;&nbsp;
					<bean:message key="servizi.configurazione.parametriBiblioteca.modInvio3" bundle="serviziLabels" />
					<html:select property="parametriBib.codModalitaInvio3Sollecito1">
						<html:optionsCollection property="lstModalitaInvio" value="codice" label="descrizione"/>
					</html:select>
				</div>
			</div>
			<br/>
		</c:when>
	</c:choose>

    <c:choose>
		<c:when test="${navForm.parametriBib.numeroLettere ge 2}">
			<div>
				<div style="float:left; width:35%">
					<bean:message key="servizi.configurazione.parametriBiblioteca.ggSecondaLettera" bundle="serviziLabels" />
				</div>
				<div style="float:none;">
					<html:text property="parametriBib.ggRitardo2" size="1" maxlength="2" ></html:text>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<bean:message key="servizi.configurazione.parametriBiblioteca.modInvio1" bundle="serviziLabels" />
					<html:select property="parametriBib.codModalitaInvio1Sollecito2">
						<html:optionsCollection property="lstModalitaInvio" value="codice" label="descrizione"/>
					</html:select>
					&nbsp;&nbsp;&nbsp;
					<bean:message key="servizi.configurazione.parametriBiblioteca.modInvio2" bundle="serviziLabels" />
					<html:select property="parametriBib.codModalitaInvio2Sollecito2">
						<html:optionsCollection property="lstModalitaInvio" value="codice" label="descrizione"/>
					</html:select>
					&nbsp;&nbsp;&nbsp;
					<bean:message key="servizi.configurazione.parametriBiblioteca.modInvio3" bundle="serviziLabels" />
					<html:select property="parametriBib.codModalitaInvio3Sollecito2">
						<html:optionsCollection property="lstModalitaInvio" value="codice" label="descrizione"/>
					</html:select>
				</div>
			</div>
			<br/>
		</c:when>
	</c:choose>

    <c:choose>
		<c:when test="${navForm.parametriBib.numeroLettere eq 3}">
			<div>
				<div style="float:left; width:35%">
					<bean:message key="servizi.configurazione.parametriBiblioteca.ggTerzaLettera" bundle="serviziLabels" />
				</div>
				<div style="float:none;">
					<html:text property="parametriBib.ggRitardo3" size="1" maxlength="2" ></html:text>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<bean:message key="servizi.configurazione.parametriBiblioteca.modInvio1" bundle="serviziLabels" />
					<html:select property="parametriBib.codModalitaInvio1Sollecito3">
						<html:optionsCollection property="lstModalitaInvio" value="codice" label="descrizione"/>
					</html:select>
					&nbsp;&nbsp;&nbsp;
					<bean:message key="servizi.configurazione.parametriBiblioteca.modInvio2" bundle="serviziLabels" />
					<html:select property="parametriBib.codModalitaInvio2Sollecito3">
						<html:optionsCollection property="lstModalitaInvio" value="codice" label="descrizione"/>
					</html:select>
					&nbsp;&nbsp;&nbsp;
					<bean:message key="servizi.configurazione.parametriBiblioteca.modInvio3" bundle="serviziLabels" />
					<html:select property="parametriBib.codModalitaInvio3Sollecito3">
						<html:optionsCollection property="lstModalitaInvio" value="codice" label="descrizione"/>
					</html:select>
				</div>
			</div>
			<br/>
		</c:when>
	</c:choose>

	<br/>

	<div class="msgOK1n">&#x00B9;&nbsp;</div>
	<div class="msgOK1"><bean:message key="servizi.configurazione.parametriBiblioteca.messaggio.sceltaServizi" bundle="serviziLabels" />
	</div>
	<br/>

	<div class="msgOK1n">&#x00B2;&nbsp;</div>
	<div class="msgOK1"><bean:message key="servizi.configurazione.parametriBiblioteca.messaggio.ammessaAutoregistrazioneUtente" bundle="serviziLabels" />
	</div>
	<br/>

	<div class="msgOK1n">&#x00B3;&nbsp;</div>
	<div class="msgOK1"><bean:message key="servizi.configurazione.parametriBiblioteca.messaggio.sceltaDiritti" bundle="serviziLabels" />
	</div>
	<br/>

	<c:choose>
		<c:when test="${navForm.parametriBib.numeroLettere gt 0}">
			<div class="msgOK1n">&#x2074;&nbsp;</div>
			<div class="msgOK1">
			<bean:message key="servizi.configurazione.parametriBiblioteca.messaggio.invio.sollecito" bundle="serviziLabels" />
			</div>
			<br/>
		</c:when>
	</c:choose>

	<div>
		<%-- <h3><bean:message key="servizi.configurazione.parametriBiblioteca.categorie" bundle="serviziLabels" /></h3> --%>
		<h3><bean:message key="servizi.configurazione.parametriBiblioteca.assegnazioneCategorie" bundle="serviziLabels" /></h3>

		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.parametriBiblioteca.catFrui.default" bundle="serviziLabels" />&nbsp;&#x2075;
		</div>
		<div style="float:none;">
			<html:select property="parametriBib.codFruizione">
				<html:optionsCollection property="lstCatFruizione" value="codice" label="descrizione"/>
			</html:select>
		</div>
		<div style="float:left; width:35%">
			<%-- <bean:message key="servizi.configurazione.parametriBiblioteca.catFrui.default_continua" bundle="serviziLabels" /> --%>
			<bean:message key="servizi.configurazione.parametriBiblioteca.catFrui.default_1" bundle="serviziLabels" />
		</div>

		<br/>
		<br/>

		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.parametriBiblioteca.catRiprod.default" bundle="serviziLabels" /><%-- &nbsp;&#x2076; --%>
		</div>
		<div style="float:none;">
			<html:select property="parametriBib.codRiproduzione">
				<html:optionsCollection property="lstCatRiproduzione" value="codice" label="descrizione"/>
			</html:select>
		</div>

		<br/>
		<div style="float:left; width:35%">
			<bean:message key="servizi.bottone.sale.categorieMediazione" bundle="serviziLabels" />&nbsp;&#x2076;
		</div>
		<div style="float:none;">
			<html:select property="parametriBib.catMediazioneDigit">
				<html:optionsCollection property="listaCatMediazione" value="cd_cat_mediazione" label="descr" />
			</html:select>
		</div>
		<%--
		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.parametriBiblioteca.catRiprod.default_continua" bundle="serviziLabels" />
		</div>
		<br/>
		--%>

		<br/>
		<div class="msgOK1n">&#x2075;&nbsp;</div>
		<%-- <div class="msgOK1"><bean:message key="servizi.configurazione.parametriBiblioteca.messaggio.catFrui" bundle="serviziLabels" /> --%>
		<div class="msgOK1"><bean:message key="servizi.configurazione.parametriBiblioteca.messaggio.catFruizione" bundle="serviziLabels" />
		</div>
		<br/>
		<div class="msgOK1n">&#x2076;&nbsp;</div>
		<div class="msgOK1"><bean:message key="servizi.configurazione.parametriBiblioteca.messaggio.catMediazione" bundle="serviziLabels" />
		<%--
		<br/>
		<div class="msgOK1n">&#x2076;&nbsp;</div>
		<div class="msgOK1"><bean:message key="servizi.configurazione.parametriBiblioteca.messaggio.catRiprod" bundle="serviziLabels" />
		</div>
		--%>

	</div>

	<div>
		<h3><bean:message key="servizi.configurazione.parametriBiblioteca.ill.servizi" bundle="serviziLabels" /></h3>

		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.parametriBiblioteca.ill.serviziAttivi" bundle="serviziLabels" />
		</div>
		<div style="float:none;">
			<html:checkbox property="parametriBib.serviziILLAttivi" />
			<html:hidden property="parametriBib.serviziILLAttivi" value="false" />
		</div>
		<br/>
		<div style="float:left; width:35%">
			<bean:message key="servizi.configurazione.parametriBiblioteca.ill.tipoAut" bundle="serviziLabels" />
		</div>
		<div style="float:none;">
			<html:select property="parametriBib.autorizzazioneILL.codAutorizzazione">
				<html:optionsCollection property="listaTipoAutorizzazione" value="codAutorizzazione" label="desAutorizzazione"/>
			</html:select>
		</div>
	</div>

</sbn:disableAll>