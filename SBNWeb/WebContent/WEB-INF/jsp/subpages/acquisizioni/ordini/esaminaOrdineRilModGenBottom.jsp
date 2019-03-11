<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

			<bean-struts:define id="bibaff">
				<bean-struts:write name="esaminaOrdineModForm" property="biblioNONCentroSistema" />
			</bean-struts:define>

			<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod" disabled="${bibaff}">
				<bean:message key="ricerca.button.bibloaffil" bundle="acquisizioniLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
				<bean:message key="ricerca.button.inventari" bundle="acquisizioniLabels" />
			</html:submit>

			<bean-struts:define id="inventari">
				<bean-struts:write name="esaminaOrdineModForm" property="disabilitazioneBottoneInventari" />
			</bean-struts:define>

			<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod" disabled="${inventari}">
				<bean:message key="ricerca.button.listaInventariOrdine" bundle="acquisizioniLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod" >
				<bean:message key="ricerca.button.operazionesuordine" bundle="acquisizioniLabels" />
			</html:submit>

			<bean-struts:define id="periodici">
				<bean-struts:write name="esaminaOrdineModForm" property="disabilitazioneBottonePeriodici" />
			</bean-struts:define>

			<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod" disabled="${periodici}">
				<bean:message key="ricerca.button.periodici" bundle="acquisizioniLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
				<bean:message key="ricerca.button.stampa" bundle="acquisizioniLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
				<bean:message key="ricerca.button.buonoOrdine" bundle="acquisizioniLabels" />
			</html:submit>
			<logic:equal name="esaminaOrdineModForm" property="enableScorrimento" value="true">
				<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
					<bean:message key="ricerca.button.scorriIndietro" bundle="acquisizioniLabels" />
				</html:submit>

				<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
					<bean:message key="ricerca.button.scorriAvanti" bundle="acquisizioniLabels" />
				</html:submit>
			</logic:equal>
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
				<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
				<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
			</html:submit>
			<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
				<bean:message key="ricerca.button.cancella" bundle="acquisizioniLabels" />
			</html:submit>

			<html:submit styleClass="pulsanti" property="methodEsaminaOrdineMod">
				<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
			</html:submit>


