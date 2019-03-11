<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/ordini/inventari.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
	  	   <!-- tabella corpo  pagina-->
			<table  align="center" width="100%" border="0" cellpadding="0" cellspacing="0">
              <tr>
                <td>
				<table width="100%"  border="0" cellpadding="0" cellspacing="0">
                    <tr   >
                      <td colspan="8" class="etichettaIntestazione" align="left">Ordine</td>
                    </tr>
                    <tr  >
                      <td colspan="8">&nbsp;</td>
                    </tr>

                    <tr >
          <td class="etichetta" valign="top" ><bean:message  key="ordine.label.bid" bundle="acquisizioniLabels" /></td>
	          <td class="etichetta"  >
		  <html:text styleId="testoNormale"   property="datiOrdine.titolo.codice" size="8"></html:text>
		  </td>
		  <td class="etichetta" valign="top">
		    <bean:message  key="ordine.label.fornitore" bundle="acquisizioniLabels" />
		    </td>
                        <td valign="top" colspan="3" scope="col" align="left" >
		  <html:text styleId="testoNormale" property="datiOrdine.fornitore.codice" size="5"  maxlength="10"></html:text>
		  <html:text styleId="testoNormale" property="datiOrdine.fornitore.descrizione" size="40"  maxlength="40"></html:text>
                        </td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
					  <td>&nbsp;</td>
                      <td>&nbsp;</td>
                    </tr>
                    <tr  >
                      <td colspan="8">&nbsp;</td>
                    </tr>
                    <tr >
                        <td valign="top"  scope="col" align="left" class="etichetta">
                        <bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
                       </td>
                        <td valign="top" scope="col" align="left">
              			<html:text styleId="testoNormale" property="datiOrdine.codBibl" size="5" readonly="true"></html:text>
                       </td>

                        <td  align="left" class="etichetta"><bean:message  key="ricerca.label.tipoOrdine" bundle="acquisizioniLabels" /></td>
                        <td   align="left" class="etichetta">
		<html:select  styleClass="testoNormale"  property="datiOrdine.tipoOrdine" disabled="true">
		<html:optionsCollection  property="listaTipoOrdine" value="codice" label="descrizione" />
		</html:select>
                        </td>
	          <td  class="etichetta"><bean:message  key="ordine.label.data" bundle="acquisizioniLabels" /></td>
          	        <td ><html:text styleId="testoNormale"  property="datiOrdine.dataOrdine" size="10" readonly="true"></html:text></td>
	      <td  class="etichetta"><bean:message  key="ordine.label.numero" bundle="acquisizioniLabels" /></td>
	      <td ><html:text styleId="testoNormale"  property="datiOrdine.codOrdine" size="2"  readonly="true"></html:text></td>
                    </tr>
                    <tr   >
                      <td colspan="8">&nbsp;</td>
                    </tr>

                    <tr >
                      <td colspan="8">&nbsp;</td>
                    </tr>
                    <tr >
                        <td  valign="top" scope="col"  align="left" class="etichetta"><bean:message  key="ricerca.label.biblAffil" bundle="acquisizioniLabels" /></td>
<!--                        <td  valign="top" scope="col" ><div align="left">
				          <html:select  styleClass="testoNormale"  property="biblAffil" style="width:100px">
						  <html:optionsCollection  property="listaBiblAffil" value="codice" label="descrizione" />
						  </html:select>
                        </div></td>
-->
                      <td>
					  <select class="testoNormale" name="Tipo"  style="width:40px">
				            <option value="" ></option>
				            <option value="A" selected>01</option>
				            <option value="L">02</option>
				            <option value="D">03</option>
			          	</select>
					  </td>
                      <td class="etichetta">Serie</td>
                      <td>
					  <select  class="testoNormale" name="serie" >
				            <option value="" >&nbsp;&nbsp;&nbsp;</option>
			          	</select>
					  </td>
                      <td class="etichetta" >N. Inventari</td>
                      <td><input name="NumInv" type="text" size="5"  value="1"></td>
                    </tr>
                    <tr >
                      <td colspan="8">&nbsp;</td>
                    </tr>
                </table>
	  	   <!-- fine tabella corpo  pagina-->
      </table>
		<c:choose>
			<c:when test="${inventariForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
		      <table align="center"  border="0" style="height:40px" cellspacing="0"; cellpadding="0">
		          <tr>
		           <td >
					<html:submit styleClass="pulsanti" property="methodInventari">
						<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodInventari">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>
		           </td>
			 	</tr>
		    	</table>
    		</c:otherwise>
		</c:choose>
     	  </div>
	</sbn:navform>
</layout:page>
