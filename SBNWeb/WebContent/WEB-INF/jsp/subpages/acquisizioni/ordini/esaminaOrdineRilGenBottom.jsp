<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

 			<bean-struts:define id="bibaff">
				<bean-struts:write name="esaminaOrdineForm" property="biblioNONCentroSistema" />
			</bean-struts:define>
           <!-- tabella bottoni -->

             <td >
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
				<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
				<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdine" disabled="${bibaff}">
				<bean:message key="ricerca.button.bibloaffil" bundle="acquisizioniLabels" />
			</html:submit>
			<!--

			<html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
				<bean:message key="crea.button.importaDa" bundle="acquisizioniLabels" />
			</html:submit>


			-->
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
				<bean:message key="ricerca.button.stampa" bundle="acquisizioniLabels" />
			</html:submit>

			<html:submit styleClass="pulsanti" property="methodEsaminaOrdine">
				<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
			</html:submit>
			<logic:equal  name="esaminaOrdineForm" property="visibilitaIndietroLS" value="true">
				<html:submit  styleClass="pulsanti" property="methodEsaminaOrdine">
					<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
				</html:submit>
			</logic:equal>

             </td>
	  			<!-- fine tabella bottoni -->


