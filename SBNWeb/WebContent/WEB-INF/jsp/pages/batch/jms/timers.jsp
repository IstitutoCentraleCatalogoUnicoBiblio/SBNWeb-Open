<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<html:xhtml />
<layout:page>
	<sbn:errors bundle="stampeLabels" />
	<html:form action="/timers.do">
		<div id="content">
		<table width="100%" border="0">
			<tr>
				<td colspan="2"><br />
				<strong>Gestione attivazione delle code asincrone con timer</strong></td>
			</tr>
			<tr>
				<td><label for="oraMinutiAttivazione">Ora minuti attivazione(hhmm)</label>
				</td>
				<td><html:text styleId="oraMinutiAttivazione"
					property="oraMinutiAttivazione" size="5" maxlength="4"></html:text>
				</td>
			</tr>
			<tr>
				<td><label for="timeout">Timeout (in secondi)</label></td>
				<td><html:text styleId="timeout" property="timeout" size="5"
					maxlength="4"></html:text></td>
			</tr>
			<tr>
				<td><label for="timeout">Stato coda asincrona</label></td>
				<td><html:textarea property="infoCoda" cols="60" rows="4"></html:textarea>
				</td>
			</tr>
		</table>
		</div>
		<div id="footer">
			<html:submit styleClass="submit" value="Attiva Timer"	property="attivaTimer" title="Timer" />
			<html:submit styleClass="submit" value="Cancella Timer" property="cancellaTimer" title="Cancella Timer" />
			<html:submit styleClass="submit" value="Info stato timers" property="btnInfoTimers" title="Info stato timers" />
			<html:submit styleClass="submit" value="Info coda asincrona" property="btnInfoCodaAsincrona" title="Info coda asincrona" />
		</div>

	</html:form>
</layout:page>
