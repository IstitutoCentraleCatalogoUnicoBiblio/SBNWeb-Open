<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

            <table width="100%"  border="0" >
              <tr>
                              <td width="16%" class="etichetta">
					<bean:message key="ricerca.codiceBibl" bundle="gestioneStampeLabels" />:
                </td>
                 <td valign="top" scope="col"><div align="left">
              			<html:text styleId="testoNormale" property="codiceBibl" size="5" disabled="true"></html:text>
                        </div></td>

                 <td width="16%" class="etichetta">
                	<bean:message key="ricerca.serieInvent" bundle="gestioneStampeLabels" />:
                </td>
                <td width="23%" class="etichetta">
	                <html:text styleId="testoNormale" property="serieInvent" size="15"></html:text>
                </td>
              </tr>

           </table>
             <hr color="#dde8f0"/>
            <table width="100%"  border="0" >
              <tr>
                <td width="16%" class="etichetta">
                	<bean:message key="ricerca.buono" bundle="gestioneStampeLabels" />:
                </td>
                <td width="23%" class="etichetta">
	                <html:text styleId="testoNormale" property="buono" size="10"></html:text>
                </td>
                <td>&nbsp;</td>
              </tr>
            </table>
			<hr color="#dde8f0"/>
			<table width="100%"  border="0" >
              <tr>

              <td scope="col" ><div align="left" class="etichetta">
              		<bean:message  key="ricerca.codInventDa" bundle="gestioneStampeLabels" /></div></td>
                        <td>
				 		  <html:text styleId="testoNormale" property="codInventDa" size="10"></html:text>
                        </td>
                        <td colspan="2" ><div align="left" class="etichetta"><bean:message  key="ricerca.codInventA" bundle="gestioneStampeLabels" />
				 		  <html:text styleId="testoNormale" property="codInventA" size="10"></html:text>
                        </div></td>
                </tr>

                <tr>
            </table>
			<table width="100%"  border="0" >
              <tr>
                <td width="16%" class="etichetta">
	                <bean:message key="ricerca.annoFatt" bundle="gestioneStampeLabels" />:
                </td>
                <td width="27%">
	                <html:text styleId="testoNormale" property="annoFatt" size="5"></html:text>
                </td>
         	  <td width="16%" class="etichetta">
	                <bean:message key="ricerca.progFatt" bundle="gestioneStampeLabels" />:
                </td>
                <td width="27%">
	                <html:text styleId="testoNormale" property="progFatt" size="5"></html:text>
                </td>
              </tr>
           </table>



