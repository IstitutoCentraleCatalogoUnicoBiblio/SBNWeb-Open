<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="formName" name="org.apache.struts.action.mapping.instance" property="name" type="java.lang.String" />

<c:choose>
<c:when test="${formName eq 'esaminaOrdineModForm'}">
	<bean-struts:define id="faseModifica" value="true"/>
    <html:hidden name="esaminaOrdineModForm" property="submitDinamico" />
	<bean-struts:define id="noinput"  value="false"/>
	<bean-struts:define id="ordineAperto" value="false"/>

	<c:choose>
	<c:when test="${esaminaOrdineModForm.disabilitaTutto ||  esaminaOrdineModForm.ordineApertoAbilitaInput}">
		<bean-struts:define id="noinput" value="true"/>
		<bean-struts:define id="ordineAperto" value="true"/>
	</c:when>
	</c:choose>

</c:when>

</c:choose>

<c:choose>
<c:when test="${formName eq 'esaminaOrdineForm'}">
	<bean-struts:define id="faseModifica" value="false"/>
    <html:hidden name="esaminaOrdineForm" property="submitDinamico" />
	<bean-struts:define id="noinput"  value="false"/>
	<bean-struts:define id="ordineAperto" value="false"/>
	<c:choose>
	<c:when test="${esaminaOrdineForm.disabilitaTutto}">
		<bean-struts:define id="noinput" value="true"/>
		<bean-struts:define id="faseModifica" value="true"/>
	</c:when>
	</c:choose>
</c:when>
</c:choose>



        <tr>
		  <td  class="etichetta"><bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" /></td>
		  <td ><html:text styleId="testoNormale"  property="datiOrdine.codBibl" size="3"  readonly="true"></html:text>
		  <c:choose>
			<c:when test="${formName eq 'esaminaOrdineForm'}">
	          <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodEsaminaOrdine" disabled="${noinput}">
				<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
			  </html:submit>
			</c:when>
		  </c:choose>
		  </td>
        </tr>

        <tr>
		  <td  class="etichetta"><bean:message  key="ordine.label.confORD" bundle="acquisizioniLabels" /></td>
		  <td >
    		<bean:message  key="ricerca.label.soloAnno" bundle="acquisizioniLabels" />
 		  	<html:text styleId="testoNormale" property="datiOrdine.annoOrdine" size="4" readonly="true"></html:text>
    		&nbsp;&nbsp;
     	    <bean:message  key="ricerca.label.nr" bundle="acquisizioniLabels" />
		    <html:text styleId="testoNormale"  property="datiOrdine.codOrdine" size="10"  readonly="true"></html:text>
    		&nbsp;&nbsp;
			<bean:message  key="ordine.label.data" bundle="acquisizioniLabels" />
			<html:text styleId="testoNormale"  property="datiOrdine.dataOrdine" size="10" readonly="true"></html:text>
    		&nbsp;&nbsp;
	       	<bean:message  key="ordine.label.stampato" bundle="acquisizioniLabels" />
			<html:checkbox   property="datiOrdine.stampato" disabled="true"></html:checkbox>
    		&nbsp;&nbsp;
			<bean:message  key="ordine.label.stato" bundle="acquisizioniLabels" />
			<c:choose>
			<c:when test="${esaminaOrdineForm.statiCDL}">
			          <html:select   styleClass="testoNormale"  property="datiOrdine.statoOrdine"    style="width:40px" >
						  <html:option value="C">C - CHIUSO</html:option>
						  <html:option value="A">A - APERTO</html:option>
					  </html:select>
			</c:when>
			<c:otherwise>
			          <html:select  disabled="true"  styleClass="testoNormale"  property="datiOrdine.statoOrdine"    style="width:40px">
					  <html:optionsCollection property="listaStato" value="codice" label="descrizioneCodice" />
					  </html:select>
			</c:otherwise>
			</c:choose>
		  </td>
        </tr>
        <tr  >
          <td class="etichetta" valign="top" ><bean:message  key="ordine.label.bid" bundle="acquisizioniLabels" /></td>
          <td class="etichetta" >

				<c:choose>
					<c:when test="${formName eq 'esaminaOrdineForm'}">
						<table border="0" cellpadding="0" cellspacing="0"  bordercolor="#7F9DB9" >
						<tr>
							<td valign="top" align="left" >
								<html:text styleId="testoNormale"   property="datiOrdine.titolo.codice" size="10" readonly="${noinput}"></html:text>
							</td>
							<td bgcolor="#EBEBE4"  >
								<bean-struts:write  name="esaminaOrdineForm" property="datiOrdine.titolo.descrizione" />
							</td>
							<td valign="top" align="left" >
				                <html:submit  styleClass="buttonImage" property="methodEsaminaOrdine" disabled="${noinput}">
									<bean:message  key="ordine.bottone.searchTit" bundle="acquisizioniLabels" />
								</html:submit>
							</td>
							<td valign="top" align="left" >
					          &nbsp;&nbsp;
					          <bean:message  key="ordine.label.natura" bundle="acquisizioniLabels" />
					          <html:select  disabled="true" style="width:40px" styleClass="testoNormale"  property="datiOrdine.naturaOrdine"  onchange="this.form.submitDinamico.value='true'; this.form.submit();"   >
							  <html:optionsCollection  property="listaNatura" value="codice" label="descrizioneCodice"  />
							  </html:select>
								<noscript>
								<html:submit styleClass="pulsanti" property="methodEsaminaOrdine" >
								<bean:message key="button.ok" bundle="acquisizioniLabels" />
								</html:submit>
								</noscript>
							</td>
						</tr>
						</table >
					</c:when>
					<c:otherwise>
						<c:choose>
						<c:when test="${formName eq 'esaminaOrdineModForm'}">
						<table border="0"  cellpadding="0" cellspacing="0" bordercolor="#7F9DB9">
						<tr>
							<td valign="top" align="left" >
								<html:text styleId="testoNormale"   property="datiOrdine.titolo.codice" size="10" readonly="${noinput}"></html:text>
							</td>
							<td  valign="top" bgcolor="#EBEBE4" >
									<bean-struts:write  name="esaminaOrdineModForm" property="datiOrdine.titolo.descrizione" />
							</td>
							<td valign="top" align="left">
				                <html:submit  styleClass="buttonImage" property="methodEsaminaOrdineMod" disabled="${noinput}">
									<bean:message  key="ordine.bottone.searchTit" bundle="acquisizioniLabels" />
								</html:submit>
							</td>
							<td valign="top" align="left" style="width:25%">
					          &nbsp;&nbsp;
					          <bean:message  key="ordine.label.natura" bundle="acquisizioniLabels" />
					          <html:select  disabled="true"  styleClass="testoNormale"  property="datiOrdine.naturaOrdine"  onchange="this.form.submitDinamico.value='true'; this.form.submit();"   >
							  <html:optionsCollection  property="listaNatura" value="codice" label="descrizioneCodice"  />
							  </html:select>
								<noscript>
								<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod" >
								<bean:message key="button.ok" bundle="acquisizioniLabels" />
								</html:submit>
								</noscript>
							</td>
						</tr>
						</table>

						</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
		  </td>
        </tr>

