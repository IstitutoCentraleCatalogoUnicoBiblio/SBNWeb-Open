<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
 <table width="90%" border="0">
<c:choose>
	<c:when test="${navForm.tipoUtente eq 'P'}">

     <tr>
		<td class="etichetta" >
           <bean:message key="servizi.utenti.ateneo" bundle="serviziLabels" />
		</td>
		<td class="testoNoBold">
           <html:select property="uteAna.professione.ateneo" disabled="${navForm.conferma}">
		   <html:optionsCollection property="ateneo" value="codice" label="descrizione" />
		   </html:select>
		</td>

        <td class="etichetta" >
           <bean:message key="servizi.utenti.codiceMatricola" bundle="serviziLabels" />
        </td>
		<td class="testoNoBold">
           <html:text styleId="testoNoBold" property="uteAna.professione.matricola" size="25" disabled="${navForm.conferma}"></html:text>
		</td>

        <td class="etichetta" >
           <bean:message key="servizi.utenti.laurea" bundle="serviziLabels" />
        </td>
		<td class="testoNoBold">
           <html:text styleId="testoNoBold" property="uteAna.professione.corsoLaurea" size="20" disabled="${navForm.conferma}"></html:text>
		</td>

     </tr>
     <tr>
        <td class="etichetta" >
			<bean:message key="servizi.utenti.professione" bundle="serviziLabels" />
        </td>
		<td class="testoNoBold">
			<html:select property="uteAna.professione.professione" disabled="${navForm.conferma}" >
				<html:optionsCollection property="professioneArr"
										value="codice"
										label="descrizioneCodice" />
			</html:select>
		</td>
        <td class="etichetta" >
			<bean:message key="servizi.utenti.occupazione" bundle="serviziLabels" />
        </td>

		<td class="testoNoBold">
           <html:select property="uteAna.professione.idOccupazione" disabled="${navForm.conferma}">
		   <html:optionsCollection property="occupazioni" value="idOccupazioni" label="comboDescrizione" />
		   </html:select>
        </td>
        <td class="etichetta" >
			&nbsp;
        </td>
		<td class="testoNoBold">
			&nbsp;
        </td>

     </tr>
     <tr>
        <td class="etichetta" >
			<bean:message key="servizi.utenti.titoloStudio" bundle="serviziLabels" />

        </td>
		<td class="testoNoBold">
			<html:select property="uteAna.professione.titoloStudio" disabled="${navForm.conferma}">
				<html:optionsCollection property="titoloStudioArr"
										value="codice"
										label="descrizioneCodice" />
			</html:select>
		</td>
        <td class="etichetta" >
           <bean:message key="servizi.utenti.specTitoloStudio" bundle="serviziLabels" />
        </td>

		<td class="testoNoBold">
 		   <html:select property="uteAna.professione.idSpecTitoloStudio" disabled="${navForm.conferma}">
		   <html:optionsCollection property="specTitoloStudio" value="idTitoloStudio" label="comboDescrizione" />
		   </html:select>
        </td>
        <td class="etichetta" >
			&nbsp;
        </td>
		<td class="testoNoBold">
			&nbsp;
        </td>

     </tr>


	</c:when>
</c:choose>
<c:choose>
	<c:when test="${navForm.tipoUtente eq 'E'}">
     <tr>
        <td class="etichetta" >
           <bean:message key="servizi.utenti.personaGiuridica" bundle="serviziLabels" />
        </td>
		<td class="testoNoBold">
			<html:select property="uteAna.professione.personaGiuridica" disabled="true">
				<html:option value="N" bundle="serviziLabels" key="servizi.bottone.no"></html:option>
				<html:option value="S" bundle="serviziLabels" key="servizi.bottone.si"></html:option>
			</html:select>
        </td>
        <td class="etichetta" >
           <bean:message key="servizi.utenti.tipoPersonalita" bundle="serviziLabels" />
        </td>
		<td class="testoNoBold">
           <html:select property="uteAna.professione.tipoPersona" disabled="${navForm.conferma or navForm.uteAna.bibliopolo.uteBibl}">
		   <html:optionsCollection property="tipoPersonalita" value="codice" label="descrizione"  />
		   </html:select>
        </td>
		<td class="etichetta" align="right">
           <bean:message key="servizi.utenti.referente" bundle="serviziLabels" />
		</td>
		<td class="testoNoBold">
           <html:text styleId="testoNoBold" property="uteAna.professione.referente" size="40" disabled="${navForm.conferma}"></html:text>
        </td>

	 </tr>
	</c:when>
</c:choose>
   </table>
   <br/>
