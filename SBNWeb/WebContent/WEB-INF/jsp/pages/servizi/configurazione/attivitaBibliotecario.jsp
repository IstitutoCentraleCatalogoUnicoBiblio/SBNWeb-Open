<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/servizi/configurazione/AttivitaBibliotecario.do">
		<div id="divForm">
			<div id="divMessaggio"><sbn:errors bundle="serviziMessages" />
			</div>
				<br/>
				<div>
					<strong><bean:message key="servizi.erogazione.attivita" bundle="serviziLabels" />:</strong>&nbsp;&nbsp;
					<html:text styleId="testoNoBold" readonly="true" property="descrizioneAttivita" size="50"></html:text>
				</div>
				<br/>
				<br/>
				<sbn:disableAll checkAttivita="GESTIONE">
				<div id="content" >
						<div style="float:left;" >
							<strong><bean:message key="servizi.configurazione.attivitaBibliotecario.listaBibliotecari" bundle="serviziLabels" /></strong>
							<br/>
							<br/>
							<html:select property="bibliotecariSelected" size="15" multiple="multiple" style="width:200px;">
								<html:optionsCollection property="bibliotecari"
									value="key" label="value.cognomeNome" />
							</html:select>
						</div>

						<div style="float:left;">
							<br/><br/><br/><br/><br/><br/><br/><br/><br/>&nbsp;&nbsp;&nbsp;
							<html:submit  property="methodAttivitaBibliotecario">
								<bean:message key="servizi.bottone.aggiungi" bundle="serviziLabels" />
							</html:submit>&nbsp;&nbsp;&nbsp;
							<!--
							<br/><br/>&nbsp;&nbsp;&nbsp;
							<html:submit style="width:80px; text-align:center;" property="methodAttivitaBibliotecario" >
								<bean:message key="servizi.bottone.rimuovi" bundle="serviziLabels" />
							</html:submit>&nbsp;&nbsp;&nbsp;
							-->
						</div>

						<div style="float:left;">
							<strong><bean:message key="servizi.configurazione.attivitaBibliotecario.bibliotecariAutorizzati" bundle="serviziLabels" /></strong>
							<br/>
							<br/>
							<html:select property="attivita_bibliotecariSelected" size="15" multiple="multiple" style="width:200px;">
								<html:optionsCollection property="attivita_bibliotecari"
									value="key" label="value.cognomeNome" />
							</html:select>
						</div>

						<div style="float:none;">
							<br/><br/><br/><br/><br/><br/><br/><br/><br/>&nbsp;&nbsp;&nbsp;
							<html:submit  property="methodAttivitaBibliotecario" >
								<bean:message key="servizi.bottone.rimuovi" bundle="serviziLabels" />
							</html:submit>&nbsp;&nbsp;&nbsp;
						</div>
				</div>
			</sbn:disableAll>
		</div>


		<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>

		<div id="divFooter" style="float: none; text-align: center">

					<sbn:checkAttivita idControllo="GESTIONE">
					<html:submit property="methodAttivitaBibliotecario" titleKey="servizi.configurazione.servizio.salvaBibliotecari" bundle="serviziLabels">
						<bean:message key="servizi.bottone.ok" bundle="serviziLabels" />
					</html:submit>
					&nbsp;&nbsp;
					</sbn:checkAttivita>
					<html:submit property="methodAttivitaBibliotecario" titleKey="servizi.configurazione.servizio.indietro" bundle="serviziLabels">
						<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
					</html:submit>


		</div>

	</sbn:navform>
</layout:page>
