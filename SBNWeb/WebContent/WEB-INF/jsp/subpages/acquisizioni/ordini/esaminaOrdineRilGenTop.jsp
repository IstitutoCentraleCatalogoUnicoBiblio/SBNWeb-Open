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
	<c:choose>
	<c:when test="${esaminaOrdineForm.disabilitaTutto}">
		<bean-struts:define id="noinput" value="true"/>
		<bean-struts:define id="ordineAperto" value="true"/>
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
          <html:select  disabled="true"  styleClass="testoNormale"  property="datiOrdine.statoOrdine"    style="width:40px">
		  <html:optionsCollection property="listaStato" value="codice" label="descrizioneCodice" />
		  </html:select>

		  </td>
        </tr>


