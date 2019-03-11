<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<html:xhtml />
<layout:page>

	<sbn:navform action="/gestionestampe/utenti/stampaUtenti.do">
	<div id="divForm">
		<div id="divMessaggio"><sbn:errors />
	</div>
	<div id="content">
		<hr>

		<table width="100%" align="center" >
			<tr>
				<td class="etichetta"  width="25%" align="right">
					<bean:message key="servizi.utenti.cognomeNome"
						bundle="serviziLabels" />
				</td>
				<td colspan="3">
					<html:text styleId="testoNoBold" property="cognome" size="80" maxlength="131"></html:text><!--
					&nbsp;&nbsp;
					<html:text styleId="testoNoBold" property="nome" size="15"  >
					</html:text>
					&nbsp;&nbsp;
					--><label for="inizio">
						<bean:message key="utenti.label.inizio" bundle="gestioneStampeLabels" />
					</label>
					&nbsp;&nbsp;
					<html:radio styleId="radioAutore" property="tipoRicerca"
						value="ini" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<label for="radioAutore">
						<bean:message key="utenti.label.intero" bundle="gestioneStampeLabels" />
					</label>
					<html:radio styleId="radioAutore" property="tipoRicerca"
						value="int" />
					&nbsp;
					<label for="radioAutore">
						<bean:message key="servizi.ricerca.parole" bundle="serviziLabels" />
					</label>
					<html:radio styleId="radioAutore" property="tipoRicerca" value="par" />

				</td>
			<tr>

			<tr>
				<td class="etichetta" align="right" width="25%" >
					<bean:message key="servizi.utenti.codUtente" bundle="serviziLabels" />
				</td>
				<td>
					<html:text styleId="testoNoBold" property="codUte" size="25" maxlength="12"></html:text>
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
			</tr>

			<tr>
				<td valign="top" width="25%" scope="col">
				<div align="left" class="etichetta"><bean:message
					key="utenti.label.codiceFiscale" bundle="gestioneStampeLabels" /></div>
				</td>


				<td valign="top" scope="col">
				<div align="left"><html:text styleId="testoNormale"
					property="codiceFiscale" size="30" disabled="false"></html:text></div>
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>

			</tr>
			<tr>
				<td class="etichetta" align="right" width="25%" >
		           <bean:message key="servizi.utenti.ateneo" bundle="serviziLabels" />
				</td>
				<td colspan="3">
					<html:select property="codiceAteneo"  >
						<html:optionsCollection property="atenei"
												value="codice"
												label="descrizione" />
					</html:select>
					&nbsp;&nbsp;
					<bean:message key="servizi.utenti.codiceMatricola" bundle="serviziLabels" />
					&nbsp;&nbsp;
					<html:text styleId="testoNoBold" property="matricola" size="15" maxlength="10" ></html:text>
				</td>

			</tr>

			<tr>
				<td valign="top" width="25%" scope="col">
				<div align="left" class="etichetta"><bean:message
					key="utenti.label.eMail" bundle="gestioneStampeLabels" /></div>
				</td>


				<td valign="top" scope="col">
				<div align="left"><html:text styleId="testoNormale" property="email"
					size="30" disabled="false"></html:text></div>
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>

			</tr>

		      <tr>
			    <td colspan="4">
			     	<hr color="#dde8f0"/>
			    </td>
			  </tr>

			<tr>
				<td valign="top" width="25%" scope="col">
				<div align="left" class="etichetta">
				<bean:message key="utenti.label.dataNascita" bundle="gestioneStampeLabels" />
				<bean:message key="servizi.utenti.da" bundle="serviziLabels" /></div>
				</td>
				<td >
					<html:text styleId="testoNormale" property="dataNascitaDa" size="12"  maxlength="10" ></html:text>
					&nbsp;&nbsp;
					<bean:message key="servizi.utenti.a" bundle="serviziLabels" />
					&nbsp;&nbsp;
					<html:text styleId="testoNormale" property="dataNascitaA" size="12" maxlength="10"></html:text>
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
			</tr>

<tr>
   				<td valign="top" width="25%" scope="col"><div align="left" class="etichetta">
               		<bean:message  key="utenti.label.autorizzazione" bundle="gestioneStampeLabels" /></div></td>
               		 <%-- td  valign="top" scope="col" ><div align="left">--%>
				<td valign="top" scope="col">
					<div align="left">
						<html:select  property="tipoAutorizzazione">
						<html:optionsCollection property="elencoAutorizzazioni"
								value="codice" label="descrizione" />
						</html:select>
					</div>
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>

</tr>
<tr>
				<td class="etichetta" width="25%"  align="right">
					<bean:message key="servizi.utenti.fineAutorizzazione" bundle="serviziLabels" />
					<bean:message key="servizi.utenti.da" bundle="serviziLabels" />
				</td>
				<td>
					<html:text styleId="testoNoBold" property="dataFineAutDa" size="12"  maxlength="10" >
					</html:text>
					&nbsp;&nbsp;
					<bean:message key="servizi.utenti.a" bundle="serviziLabels" />
					&nbsp;&nbsp;
					<html:text styleId="testoNoBold" property="dataFineAutA" size="12" maxlength="10"  >
					</html:text>
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>

</tr>


<tr>
   				<td valign="top" width="25%"  scope="col"><div align="left" class="etichetta">
               		<bean:message  key="utenti.label.nazCitta" bundle="gestioneStampeLabels" /></div></td>
               		 <%-- td  valign="top" scope="col" ><div align="left">--%>
				<td valign="top" scope="col">
					<div align="left">
						<html:select  property="tipoNazCitta">
						<html:optionsCollection property="elencoNazCitta"
								value="codice" label="descrizione" />
						</html:select>
					</div>
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>

</tr>

<tr>
   				<td valign="top" width="25%"  scope="col"><div align="left" class="etichetta">
               		<bean:message  key="utenti.label.professione" bundle="gestioneStampeLabels" /></div></td>
               		 <%-- td  valign="top" scope="col" ><div align="left">--%>
				<td valign="top" scope="col">
					<div align="left">
						<html:select  property="tipoProfessione" onchange="this.form.submit();">
						<html:optionsCollection property="elencoProfessioni"
								value="codice" label="descrizione" />
						</html:select>
						&nbsp;&nbsp;
						<bean:message key="servizi.utenti.occupazione" bundle="serviziLabels" />
						&nbsp;&nbsp;
						<html:select property="occupazione"  >
							<html:optionsCollection property="occupazioni"
													value="codOccupazione"
													label="desOccupazione" />
						</html:select>

					</div>
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>

</tr>

<tr>
   				<td valign="top" width="25%"  scope="col"><div align="left" class="etichetta">
               		<bean:message  key="utenti.label.titStudio" bundle="gestioneStampeLabels" /></div></td>
               		 <%-- td  valign="top" scope="col" ><div align="left">--%>
				<td valign="top" scope="col">
					<div align="left">
						<html:select  property="tipoTitStudio" onchange="this.form.submit();">
						<html:optionsCollection property="elencoTitStudio"
								value="codice" label="descrizione" />
						</html:select>
						&nbsp;&nbsp;
						<bean:message key="servizi.spectitolostudio.listaSpecialita" bundle="serviziLabels" />
						&nbsp;&nbsp;
						<html:select property="specificita" >
							<html:optionsCollection property="specTitoloStudio"
													value="codSpecialita"
													label="desSpecialita" />
						</html:select>
					</div>
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>

</tr>


<tr>
   				<td valign="top" width="25%"  scope="col"><div align="left" class="etichetta">
               		<bean:message  key="utenti.label.provResid" bundle="gestioneStampeLabels" /></div></td>
               		 <%-- td  valign="top" scope="col" ><div align="left">--%>
				<td valign="top" scope="col">
					<div align="left">
						<html:select  property="tipoProvResid">
						<html:optionsCollection property="elencoProvResid"
								value="codice" label="descrizione" />
						</html:select>
					</div>
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>

</tr>

<tr>
   				<td valign="top" width="25%"  scope="col"><div align="left" class="etichetta">
		           <bean:message key="servizi.utenti.tipoPersonalitaGiur" bundle="serviziLabels" />
               		</div>
               	</td>
				<td valign="top" scope="col">
					<div align="left">
			           <html:select property="tipoPersona">
					   <html:optionsCollection property="tipoPersonalita" value="codice" label="descrizione" />
					   </html:select>
					</div>
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
</tr>

<tr>
   				<td valign="top" width="25%"  scope="col"><div align="left" class="etichetta">
		           <bean:message key="servizi.utenti.materieDiInteresse" bundle="serviziLabels" />
               		</div>
               	</td>
				<td valign="top" scope="col">
					<div align="left">
			           <html:select property="materia" >
					   <html:optionsCollection property="materie" value="idMateria" label="descrizione" />
					   </html:select>
					</div>
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>
				<td class="etichetta" align="right">
					&nbsp;
				</td>

</tr>

		</table>



		<!-- FINE tabella corpo COLONNA + LARGA -->

	  	<table   width="100%" border="0">
		      <tr>
			    <td>
			     	<hr color="#dde8f0"/>
			    </td>
			  </tr>
	   	</table>
	  	<table   width="100%" border="0">
		      <tr>

						<bean-struts:size id="comboSize" name="stampaUtentiForm" property="elencoModelli" />
						<logic:greaterEqual name="comboSize" value="2">
<!--							Selezione Modello Via Combo-->
							<td  width="25%"  scope="col"  ><div align="left" class="etichetta">
		               		<bean:message  key="utenti.label.modello" bundle="gestioneStampeLabels" />
							</div></td>
							<td align="left">
							<html:select  styleClass="testoNormale"  property="tipoModello" >
								<html:optionsCollection  property="elencoModelli" value="jrxml" label="descrizione" />
							</html:select>
							</td>
						</logic:greaterEqual>
						<logic:lessThan name="comboSize" value="2">
							Selezione Modello Hidden
							<td  width="15%"  scope="col"  ><div align="left" class="etichetta">
							&nbsp;
							</div></td>
							<td align="left">
							&nbsp;
							<html:hidden property="tipoModello" value="${stampaUtentiForm.elencoModelli[0].jrxml}" />
							</td>
						</logic:lessThan>
			  </tr>
	   	</table>

		<HR>

		<jsp:include flush="true" page="../common/tipoStampa.jsp" />

		<hr>
		<table align="center" border="0" style="height:40px">
			<tr>
				<td>
					<html:submit styleClass="pulsanti" property="methodStampaUtenti">
						<bean:message key="button.conferma" bundle="gestioneStampeLabels" />
					</html:submit>

					<html:submit styleClass="pulsanti"
						property="methodStampaUtenti">
						<bean:message key="button.indietro" bundle="gestioneStampeLabels" />
					</html:submit>

					<!--<html:submit styleClass="pulsanti"
						property="methodStampaUtenti">
						<bean:message key="button.stampa" bundle="gestioneStampeLabels" />
					</html:submit>-->
				</td>
			</tr>
		</table>
	</sbn:navform>
</layout:page>


