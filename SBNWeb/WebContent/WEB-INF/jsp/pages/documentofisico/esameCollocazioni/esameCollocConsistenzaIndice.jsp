<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />

<layout:page>
	<sbn:errors bundle="documentoFisicoMessages" />
	<sbn:navform
		action="/documentofisico/esameCollocazioni/esameCollocConsistenzaIndice.do">
	<html:hidden property="action"/>
		<div id="content">

		<table width="100%" border="0">
			<tr>
				<td width="18%"><div class="etichetta"><bean:message
					key="documentofisico.bibliotecaT" bundle="documentoFisicoLabels" /></div></td>
				<td width="18%"><div class="testoNormale"><html:text disabled="true"
					property="codBib" size="10" maxlength="10"></html:text></div></td>
				<td width="56%" align="left" class="testoNormale"><bean-struts:write
					name="esameCollocConsistenzaIndiceForm" property="descrBib" /></td>
			</tr>
		</table>

		<table width="100%" align="center">
			<tr valign="top">
				<td width="18%"><div class="etichetta"><bean:message
					key="documentofisico.titoloT" bundle="documentoFisicoLabels" />:
				<span class="etichetta"></span> <bean-struts:write
					name="esameCollocConsistenzaIndiceForm" property="bid" /></div></td></tr>
			<tr valign="top">
				<td width="82%"><div class="testoNormale"><bean-struts:write
					name="esameCollocConsistenzaIndiceForm"
					property="titoloBid" /></div></td></tr>
		</table>
		<table width="100%" align="center">
			<tr valign="top">
				<td width="18%"><div class="etichetta"><bean:message
					key="documentofisico.consistenzaEsemplareT" bundle="documentoFisicoLabels" /></div>
				<span class="testoNormale"></span> <bean-struts:write
					name="esameCollocConsistenzaIndiceForm" property="consistenzaEsemplare" /></td>
			</tr>
			<tr valign="top">
				<td width="82%"><div class="etichetta"></div></td></tr>
		</table>
     <table width="100%" border="0">
       <tr>
         <td width="19%" height="24" scope="col"><div align="left" class="etichetta">
         	<bean:message key="documentofisico.consistenzaIndiceT"
           					bundle="documentoFisicoLabels" /></div></td>
         <td width="81%" scope="col"><div align="left" class="testoNormale">
           <html:textarea styleId="testoNormale" cols="40" rows=""
				property="consistenzaIndice" ></html:textarea></div></td>
        </tr>
       <tr>
         <td><div align="left" class="etichetta">
         		<bean:message key="documentofisico.mutiloT"
           					bundle="documentoFisicoLabels" /></div></td>
         <td scope="col"><html:checkbox property="mutilo"></html:checkbox></td>
       </tr>
     </table>
	 </div>

<div id="footer">
 <!--
		gestione bottoni
-->
	<table align="center">
		<tr>
			<td>
				<html:submit styleClass="pulsanti" property="methodEsameConsIndice">
					<bean:message key="documentofisico.bottone.ok"
								bundle="documentoFisicoLabels" /></html:submit>
				<html:submit styleClass="pulsanti" property="methodEsameConsIndice">
					<bean:message key="documentofisico.bottone.indietro"
								bundle="documentoFisicoLabels" /></html:submit>
			</td>
		</tr>
	</table>
</div>
</sbn:navform>

</layout:page>
