<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


		<br>
		<table width="100%" border="0">
			<tr>
				<td class="etichetta" align="right">
					<bean:message key="servizi.utenti.cognomeNome" bundle="serviziLabels" />
				</td>
				<td>
					<html:text styleId="testoNoBold" property="ricerca.cognome" size="80" maxlength="131" readonly="${RicercaUtentiForm.conferma}">
					</html:text>
					<!--
					&nbsp;
					<html:text styleId="testoNoBold" property="ricerca.nome" size="30" maxlength="50" readonly="${RicercaUtentiForm.conferma}">
					</html:text>
					-->
					&nbsp;
					<label for="inizio">
						<bean:message key="servizi.ricerca.inizio" bundle="serviziLabels" />
					</label>
					&nbsp;
					<html:radio styleId="radioAutore" property="ricerca.tipoRicerca" value="ini"  disabled="${RicercaUtentiForm.conferma}"/>
					&nbsp;
					<label for="radioAutore">
						<bean:message key="servizi.ricerca.intero" bundle="serviziLabels" />
					</label>
					&nbsp;
					<html:radio styleId="radioAutore" property="ricerca.tipoRicerca" value="int"  disabled="${RicercaUtentiForm.conferma}"/>
					&nbsp;
					<label for="radioAutore">
						<bean:message key="servizi.ricerca.parole" bundle="serviziLabels" />
					</label>
					<html:radio styleId="radioAutore" property="ricerca.tipoRicerca" value="par"  disabled="${RicercaUtentiForm.conferma}"/>

				</td>
			</tr>

			<tr>
				<td class="etichetta" align="right">
					<bean:message key="servizi.utenti.codUtente" bundle="serviziLabels" />
				</td>
				<td>
					<html:text styleId="testoNoBold" property="ricerca.codUte" size="25" maxlength="128" readonly="${RicercaUtentiForm.conferma}"></html:text>
				</td>
			</tr>
			<tr>
				<td class="etichetta" align="right">
					<bean:message key="servizi.utenti.codiceFiscale" bundle="serviziLabels" />
				</td>
				<td align="left">
					<html:text styleId="testoNoBold" property="ricerca.codFiscale" size="25" maxlength="16" readonly="${RicercaUtentiForm.conferma}"></html:text>
				</td>
			</tr>
			<tr>
				<td class="etichetta" align="right">
		           <bean:message key="servizi.utenti.ateneo" bundle="serviziLabels" />
				</td>
				<td>
					<html:select property="ricerca.codiceAteneo"  disabled="${RicercaUtentiForm.conferma}">
						<html:optionsCollection property="atenei"
												value="codice"
												label="descrizione" />
					</html:select>
					&nbsp;&nbsp;
					<bean:message key="servizi.utenti.codiceMatricola" bundle="serviziLabels" />
					&nbsp;&nbsp;
					<html:text styleId="testoNoBold" property="ricerca.matricola" size="15" maxlength="10" readonly="${RicercaUtentiForm.conferma}"></html:text>
				</td>
			</tr>
			<tr>
				<td class="etichetta" align="right">
					<bean:message key="servizi.utenti.email" bundle="serviziLabels" />
				</td>
				<td>
					<html:text styleId="testoNoBold" property="ricerca.mail" size="50" readonly="${RicercaUtentiForm.conferma}"></html:text>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="etichetta" align="right">
					<hr color="#dde8f0" />
				</td>
			</tr>

			<tr>
				<td class="etichetta" align="right">
					<bean:message key="servizi.utenti.dataNascita" bundle="serviziLabels" />
					<bean:message key="servizi.utenti.da" bundle="serviziLabels" />
				</td>
				<td>
					<html:text styleId="testoNoBold" property="ricerca.dataNascita" size="12" maxlength="10" readonly="${RicercaUtentiForm.conferma}">
					</html:text>
					&nbsp;&nbsp;
					<bean:message key="servizi.utenti.a" bundle="serviziLabels" />
					&nbsp;&nbsp;
					<html:text styleId="testoNoBold" property="ricerca.dataNascitaA" size="12" maxlength="10"   readonly="${RicercaUtentiForm.conferma}">
					</html:text>

				</td>
			</tr>

			<tr>
				<td class="etichetta" align="right">
					<bean:message key="servizi.utenti.tipoAutorizzazione" bundle="serviziLabels" />
				</td>
				<td>
					<html:select property="ricerca.tipoAutorizzazione"  disabled="${RicercaUtentiForm.conferma}">
						<html:optionsCollection property="tipoAutor"
												value="codAutorizzazione"
												label="desAutorizzazione" />
					</html:select>
				</td>
			</tr>
			<tr>
				<td class="etichetta" align="right">
					<bean:message key="servizi.utenti.fineAutorizzazione" bundle="serviziLabels" />
					<bean:message key="servizi.utenti.da" bundle="serviziLabels" />
				</td>
				<td>
					<html:text styleId="testoNoBold" property="ricerca.dataFineAut" size="12"  maxlength="10"  readonly="${RicercaUtentiForm.conferma}">
					</html:text>
					&nbsp;&nbsp;
					<bean:message key="servizi.utenti.a" bundle="serviziLabels" />
					&nbsp;&nbsp;
					<html:text styleId="testoNoBold" property="ricerca.dataFineAutA" size="12" maxlength="10"  readonly="${RicercaUtentiForm.conferma}">
					</html:text>

				</td>
			</tr>



			<tr>
				<td width="23%" class="etichetta" align="right">
					<bean:message key="servizi.utenti.nazioneCitta" bundle="serviziLabels" />
				</td>
				<td>
					<html:select property="ricerca.nazCitta"  disabled="${RicercaUtentiForm.conferma}">
						<html:optionsCollection property="nazCitta"
												value="codice"
												label="descrizioneCodice" />
					</html:select>
				</td>
			</tr>



			<tr>
				<td class="etichetta" align="right">
					<bean:message key="servizi.utenti.professione" bundle="serviziLabels" />
				</td>
				<td>
					<html:select property="ricerca.professione" onchange="this.form.submit();" disabled="${RicercaUtentiForm.conferma}" >
						<html:optionsCollection property="professione"
												value="codice"
												label="descrizioneCodice" />
					</html:select>
					&nbsp;&nbsp;
					<bean:message key="servizi.utenti.occupazione" bundle="serviziLabels" />
					&nbsp;&nbsp;
					<html:select property="ricerca.occupazione"  disabled="${RicercaUtentiForm.conferma}">
						<html:optionsCollection property="occupazioni"
												value="codOccupazione"
												label="desOccupazione" />
					</html:select>
				</td>
			</tr>
			<tr>
				<td class="etichetta" align="right">
					<bean:message key="servizi.utenti.titoloStudio" bundle="serviziLabels" />
				</td>
				<td>
					<html:select property="ricerca.titStudio" disabled="${RicercaUtentiForm.conferma}" onchange="this.form.submit();">
						<html:optionsCollection property="titoloStudio"
												value="codice"
												label="descrizioneCodice" />
					</html:select>
					&nbsp;&nbsp;
					<bean:message key="servizi.spectitolostudio.listaSpecialita" bundle="serviziLabels" />
					&nbsp;&nbsp;
					<html:select property="ricerca.specificita" disabled="${RicercaUtentiForm.conferma}">
						<html:optionsCollection property="specTitoloStudio"
												value="codSpecialita"
												label="desSpecialita" />
					</html:select>

				</td>
			</tr>
			<tr>
				<td width="23%" class="etichetta" align="right">
					<bean:message key="servizi.utenti.provinciaResidenza" bundle="serviziLabels" />
				</td>
				<td align="left">
					<html:select property="ricerca.provResidenza" disabled="${RicercaUtentiForm.conferma}" >
						<html:optionsCollection property="provinciaResidenza"
												value="codice"
												label="descrizioneCodice" />
					</html:select>
				</td>
			</tr>
			<tr>
		        <td class="etichetta" >
		           <bean:message key="servizi.utenti.personaGiuridica" bundle="serviziLabels" />
		        </td>
				<td class="testoNoBold">
		           <html:select property="ricerca.personaGiuridica" disabled="${RicercaUtentiForm.conferma}" onchange="this.form.submit();">
				   <html:optionsCollection property="listaPersonaGiurid" value="codice" label="descrizione" />
				   </html:select>

				<c:choose>
					<c:when test="${RicercaUtentiForm.ricerca.personaGiuridica eq 'S'}">
						&nbsp;&nbsp;
			           <bean:message key="servizi.utenti.tipoPersonalitaGiur" bundle="serviziLabels" />
						&nbsp;&nbsp;
			           <html:select property="ricerca.tipoPersona" disabled="${RicercaUtentiForm.conferma}">
					   <html:optionsCollection property="tipoPersonalita" value="codice" label="descrizione" />
					   </html:select>
					</c:when>
				</c:choose>
		        </td>
				   <!--
			        <td class="etichetta" >
			           <bean:message key="servizi.utenti.tipoPersonalitaGiur" bundle="serviziLabels" />
			        </td>
					<td class="testoNoBold">
			           <html:select property="ricerca.tipoPersona" disabled="${RicercaUtentiForm.conferma}">
					   <html:optionsCollection property="tipoPersonalita" value="codice" label="descrizione" />
					   </html:select>
			        </td>
					-->
			</tr>
			<tr>
		        <td class="etichetta" >
		           <bean:message key="servizi.utenti.materieDiInteresse" bundle="serviziLabels" />
		        </td>
				<td class="testoNoBold">
		           <html:select property="ricerca.materia" disabled="${RicercaUtentiForm.conferma}">
				   <html:optionsCollection property="materie" value="idMateria" label="descrizione" />
				   </html:select>
		        </td>
			</tr>

		</table>
