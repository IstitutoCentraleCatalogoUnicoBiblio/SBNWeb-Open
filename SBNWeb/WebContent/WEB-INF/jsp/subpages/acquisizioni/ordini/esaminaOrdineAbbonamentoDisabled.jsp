<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

        <tr>
          <td class="etichettaIntestazione" ><bean:message  key="ordine.label.abbonamento" bundle="acquisizioniLabels" /></td>
          <td colspan="6" rowspan="2" class="etichetta">
              <table width="95%"   cellspacing="1" cellpadding="1" style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;">
                <tr>
                  <td width="15%" scope="col" class="etichetta" align="left"><bean:message  key="ordine.label.numFasc" bundle="acquisizioniLabels" /></td>
                  <td width="10%" scope="col" align="left">
					<html:text styleId="testoNormale"   property="datiOrdine.numFascicoloAbbOrdine"   size="4" readonly="true"></html:text>
                  </td>
                  <td width="17%" scope="col" class="etichetta"  align="left"><bean:message  key="ordine.label.dataAbb" bundle="acquisizioniLabels" /></td>
                  <td width="36%" scope="col"><span class="etichetta">
   					<html:text styleId="testoNormale"   property="datiOrdine.dataPubblFascicoloAbbOrdine"  size="8" readonly="true"></html:text>
                  </td>
                  <td width="15%"  scope="col" class="etichetta"><bean:message  key="ordine.label.NVol" bundle="acquisizioniLabels" /></td>
                  <td width="7%" scope="col">
   					<html:text styleId="testoNormale"   property="datiOrdine.numVolAbbOrdine"  size="3" readonly="true"></html:text>
                  </td>
                </tr>
                <tr>
                  <td class="etichetta"><bean:message  key="ordine.label.periodo" bundle="acquisizioniLabels" /></td>
                  <td style="width:20%;" align="left">
					<html:select style="width:40px" styleClass="testoNormale" property="periodo" disabled="true">
					<html:optionsCollection  property="listaPeriodo" value="codice" label="descrizione" />
					</html:select>
				  </td>
                  <td class="etichetta"><bean:message  key="ordine.label.annata" bundle="acquisizioniLabels" /></td>
                  <td>
   					<html:text styleId="testoNormale"   property="datiOrdine.annataAbbOrdine" size="4" readonly="true"></html:text>
                  </td>
 				  <td >&nbsp;</td>
                  <td >&nbsp;</td>
	          	</tr>
		      </table>
			</td>
   	        <td  class="etichetta"><bean:message  key="ordine.label.stampato" bundle="acquisizioniLabels" />&nbsp;
					<html:checkbox   property="datiOrdine.stampato"></html:checkbox>
   			</td>

	  	   <!-- fine tabella corpo  pagina-->

        </tr>