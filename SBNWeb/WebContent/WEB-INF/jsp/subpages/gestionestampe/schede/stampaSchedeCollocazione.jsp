<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.dallaSezione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dallaSezione" size="10" maxlength="10"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.dallaCollocazione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dallaCollocazione" size="10" maxlength="50"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.specificazioneDallaCollocazione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="specificazioneDallaCollocazione" size="10" maxlength="50"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
             </tr>
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.allaCollocazione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="allaCollocazione" size="10" maxlength="50"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.specificazioneAllaCollocazione" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="specificazioneAllaCollocazione" size="10" maxlength="50"></html:text>
 						<html:link action="/pathDaDefinire/actionDaDefinire.do" ><img border="0"  align="middle" alt="Ricerca formato etichetta"  src='<c:url value="/images/lente.GIF" />'/></html:link>
                        </td>
             </tr>
		</table>
		<table   width="100%" border="0">
		     <tr>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.serie" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
						<html:select  styleClass="testoNormale"  property="serie">
						<html:optionsCollection  property="listaSerie" value="codice" label="descrizione" />
						</html:select>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.dalNumero" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="dalNumero" size="10" maxlength="50"></html:text>
                        </td>
                        <td   valign="top" scope="col" align="left"> <div class="etichetta" >
						<bean:message  key="schede.label.alNumero" bundle="gestioneStampeLabels" />
						</div></td>
                        <td  valign="top" scope="col" align="left">
 						<html:text styleId="testoNormale" property="alNumero" size="10" maxlength="50"></html:text>
                        </td>
             </tr>
		</table>
