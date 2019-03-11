<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/periodici/previsionale.do">
		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>
		<br />
		<c:if test="${ (navForm.operazione eq 'PREVISIONALE_GIORNI') and navForm.conferma }">
			<table width="99%" class="sintetica">
				<tr align="center">
					<td><bean:message key="periodici.previsioni.giorno.lun"
						bundle="periodiciLabels" /> <html:multibox name="navForm"
						property="modello.giorniInclusi" value="2" />&nbsp; <bean:message
						key="periodici.previsioni.giorno.mar" bundle="periodiciLabels" />
					<html:multibox name="navForm" property="modello.giorniInclusi"
						value="3" />&nbsp; <bean:message
						key="periodici.previsioni.giorno.mer" bundle="periodiciLabels" />
					<html:multibox name="navForm" property="modello.giorniInclusi"
						value="4" />&nbsp; <bean:message
						key="periodici.previsioni.giorno.gio" bundle="periodiciLabels" />
					<html:multibox name="navForm" property="modello.giorniInclusi"
						value="5" />&nbsp; <bean:message
						key="periodici.previsioni.giorno.ven" bundle="periodiciLabels" />
					<html:multibox name="navForm" property="modello.giorniInclusi"
						value="6" />&nbsp; <bean:message
						key="periodici.previsioni.giorno.sab" bundle="periodiciLabels" />
					<html:multibox name="navForm" property="modello.giorniInclusi"
						value="7" />&nbsp; <bean:message
						key="periodici.previsioni.giorno.dom" bundle="periodiciLabels" />
					<html:multibox name="navForm" property="modello.giorniInclusi"
						value="1" /> <html:hidden name="navForm"
						property="modello.giorniInclusi" value="0" /></td>
				</tr>
			</table>
			<br />
			<hr />
		</c:if>
		<jsp:include
			page="/WEB-INF/jsp/subpages/periodici/intestazionePeriodico.jsp"
			flush="true" /></div>
		<sbn:disableAll disabled="${navForm.conferma}">
		<table style="width: 50%; max-width: 60%;">
			<!--
			<tr>
				<td><bean:message key="periodici.previsioni.modello" bundle="periodiciLabels" /></td>
				<td><html:select name="navForm" property="idModello">
					<html:optionsCollection name="navForm" property="modelli" value="id_modello" label="nome" />
				</html:select>
				</td>
			</tr>
			<tr>
				<td><bean:message key="periodici.previsioni.mod.nome" bundle="periodiciLabels" /></td>
				<td><html:text name="navForm" property="modello.nome" maxlength="50" size="30" /></td>
			</tr>
			<tr>
				<td><bean:message key="periodici.previsioni.mod.descrizione" bundle="periodiciLabels" /></td>
				<td><html:text name="navForm" property="modello.descrizione" maxlength="250" size="50" /></td>
			</tr>
			 -->
			<tr>
				<td><bean:message key="periodici.kardex.tipo.per"
					bundle="periodiciLabels" /></td>
				<td><html:select name="navForm" property="modello.cd_per">
					<html:optionsCollection name="navForm" property="listaPeriodicita"
						value="cd_tabellaTrim" label="ds_tabella" />
				</html:select></td>
			</tr>
			<tr>
				<td><bean:message
					key="periodici.previsioni.mod.num_fasc_per_vol"
					bundle="periodiciLabels" /></td>
				<td><html:text name="navForm"
					property="modello.num_fascicoli_per_volume" maxlength="4" size="5" />
				</td>
			</tr>
			<tr>
				<td><bean:message key="periodici.previsioni.mod.numerazione"
					bundle="periodiciLabels" /></td>
				<td><html:select name="navForm"
					property="modello.cd_tipo_num_fascicolo">
					<html:optionsCollection name="navForm"
						property="listaTipoNumerazione" value="cd_tabellaTrim"
						label="cd_flg3" />
				</html:select> <bean:message key="periodici.previsioni.mod.continuativo"
					bundle="periodiciLabels" /> <html:checkbox name="navForm"
					property="modello.num_fascicolo_continuativo" value="true" /> <html:hidden
					name="navForm" property="modello.num_fascicolo_continuativo"
					value="false" /></td>
			</tr>
		</table>
		<br />
		<table class="sintetica" style="width: 50%; max-width: 60%;">
			<tr>
				<td colspan="2">
				<h3><bean:message
					key="periodici.previsioni.mod.dati.primo.atteso"
					bundle="periodiciLabels" /></h3>
				</td>
			</tr>
			<tr>
				<td><bean:message key="periodici.kardex.volume"
					bundle="periodiciLabels" /></td>
				<td><html:text name="navForm"
					property="modello.num_primo_volume" maxlength="4" size="5" /></td>
			</tr>
			<tr>
				<td><bean:message key="periodici.kardex.annata"
					bundle="periodiciLabels" /></td>
				<td><html:text name="navForm"
					property="ricercaKardex.annataFrom" maxlength="4" size="4" />
				&nbsp;&#47;&nbsp; <html:text name="navForm"
					property="ricercaKardex.annataTo" maxlength="4" size="4" /></td>
			</tr>
			<tr>
				<td><bean:message key="periodici.previsioni.mod.primo_fasc"
					bundle="periodiciLabels" /></td>
				<td><html:text name="navForm"
					property="modello.num_primo_fascicolo" maxlength="255" size="5" />&nbsp;
				</td>
			</tr>
			<tr>
				<td><bean:message key="periodici.kardex.data.conv.fasc"
					bundle="periodiciLabels" /></td>
				<td><html:text name="navForm" property="dataPrimoFasc"
					maxlength="10" size="10" /> &nbsp;<bean:message
					key="periodici.previsioni.mod.posizione.fasc.vol"
					bundle="periodiciLabels" /> <html:text name="navForm"
					property="ricercaKardex.posizione_primo_fascicolo" maxlength="4"
					size="4" /></td>
			</tr>

		</table>
		<br />
		<table>
			<tr>
			<td><bean:message key="periodici.previsioni.mod.escludi"
				bundle="periodiciLabels" /></td>
			<td>
			<table border="0" bordercolor="#dde8f0">
				<tr>
					<th width="100" class="etichetta"><span>gg/mm</span></th>
					<th width="85" class="etichetta" bgcolor="#dde8f0">&nbsp; <html:submit
						styleClass="buttonImageDelLine" property="methodPrev"
						title="Cancella">
						<bean:message key="button.periodici.escludi.elimina"
							bundle="periodiciLabels" />
					</html:submit> <html:submit styleClass="buttonImageNewLine" property="methodPrev"
						title="Inserisci">
						<bean:message key="button.periodici.escludi.inserisci"
							bundle="periodiciLabels" />
					</html:submit></th>
				</tr>
				<logic:iterate id="item" property="modello.dateEscluse"
					name="navForm" indexId="idx">
					<tr>
						<td bgcolor="#FFCC99"><html:text name="navForm"
							property="modello.dateEscluse[${idx}].codice" maxlength="5" /></td>
						<td bgcolor="#FFCC99"><html:radio property="idxEscludi"
							value="${idx}" /></td>
					</tr>
				</logic:iterate>
			</table>
			</td>
			</tr>
			<tr>
				<td><bean:message key="periodici.previsioni.mod.includi"
					bundle="periodiciLabels" /></td>
				<td>
				<table border="0" bordercolor="#dde8f0">
					<tr>
						<th width="100" class="etichetta"><span>gg/mm</span></th>
						<th width="85" class="etichetta" bgcolor="#dde8f0">&nbsp; <html:submit
							styleClass="buttonImageDelLine" property="methodPrev"
							title="Cancella">
							<bean:message key="button.periodici.includi.elimina"
								bundle="periodiciLabels" />
						</html:submit> <html:submit styleClass="buttonImageNewLine"
							property="methodPrev" title="Inserisci">
							<bean:message key="button.periodici.includi.inserisci"
								bundle="periodiciLabels" />
						</html:submit></th>
					</tr>
					<logic:iterate id="item" property="modello.dateIncluse"
						name="navForm" indexId="idx">
						<tr>
							<td bgcolor="#FFCC99"><html:text name="navForm"
								property="modello.dateIncluse[${idx}].codice" maxlength="5" />
							</td>
							<td bgcolor="#FFCC99"><html:radio property="idxIncludi"
								value="${idx}" /></td>
						</tr>
					</logic:iterate>
				</table>
				</td>
			</tr>
			<tr>
				<td><bean:message key="periodici.previsioni.mod.escludi.giorno"
					bundle="periodiciLabels" /></td>
				<td>
				<table>
					<tr>
						<td><bean:message key="periodici.previsioni.giorno.lun"
							bundle="periodiciLabels" /> <html:multibox name="navForm"
							property="modello.giorniEsclusi" value="2" />&nbsp; <bean:message
							key="periodici.previsioni.giorno.mar" bundle="periodiciLabels" />
						<html:multibox name="navForm" property="modello.giorniEsclusi"
							value="3" />&nbsp; <bean:message
							key="periodici.previsioni.giorno.mer" bundle="periodiciLabels" />
						<html:multibox name="navForm" property="modello.giorniEsclusi"
							value="4" />&nbsp; <bean:message
							key="periodici.previsioni.giorno.gio" bundle="periodiciLabels" />
						<html:multibox name="navForm" property="modello.giorniEsclusi"
							value="5" />&nbsp; <bean:message
							key="periodici.previsioni.giorno.ven" bundle="periodiciLabels" />
						<html:multibox name="navForm" property="modello.giorniEsclusi"
							value="6" />&nbsp; <bean:message
							key="periodici.previsioni.giorno.sab" bundle="periodiciLabels" />
						<html:multibox name="navForm" property="modello.giorniEsclusi"
							value="7" />&nbsp; <bean:message
							key="periodici.previsioni.giorno.dom" bundle="periodiciLabels" />
						<html:multibox name="navForm" property="modello.giorniEsclusi"
							value="1" /> <html:hidden name="navForm"
							property="modello.giorniEsclusi" value="0" /></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td><bean:message key="periodici.previsioni.mod.prevedi.per"
					bundle="periodiciLabels" /></td>
				<td><html:text name="navForm" property="durata" maxlength="3"
					size="5" />&nbsp; <html:select name="navForm"
					property="tipoDurata">
					<html:optionsCollection name="navForm" property="listaTipoDurata"
						value="ds_tabella" label="ds_tabella" />
				</html:select></td>
			</tr>
		</table>
		</sbn:disableAll>
		<br />
		<div id="divFooterCommon">
		<table>
			<tr>
				<td class="etichetta"><bean:message
					key="gestionesemantica.soggetto.elementoPerBlocco"
					bundle="gestioneSemanticaLabels" /> <html:text
					styleId="testoNormale" name="navForm"
					property="ricercaKardex.numeroElementiBlocco" size="4"
					maxlength="4" /></td>
			</tr>
		</table>
		</div>
		<div id="divFooter">
		<table align="center">
			<tr>
				<td><sbn:bottoniera buttons="pulsanti" /></td>
			</tr>
		</table>
		</div>
	</sbn:navform>

</layout:page>
<script type="text/javascript"
	src='<c:url value="/scripts/bibliografica/dettaglioTitolo.js" />'></script>