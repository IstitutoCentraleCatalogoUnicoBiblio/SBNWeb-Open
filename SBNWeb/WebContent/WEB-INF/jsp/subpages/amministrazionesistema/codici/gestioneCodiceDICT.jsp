<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<table>
	<tr>
		<td><bean:message key="elenco.codici.titolo.codice"
			bundle="amministrazioneSistemaLabels" />&nbsp; <span
			style="font-size: 80%;"> (Max.&nbsp;<c:out
			value="${navForm.configCodici.length}" />&nbsp;car.) </span>:&nbsp;
		</td>
		<td><html:text property="dettaglio.cdTabella"
			size="${navForm.configCodici.length}"
			maxlength="${navForm.configCodici.length}"
			disabled="${!navForm.dettaglio.nuovo}" />
			<span style="font-size: 80%;">&nbsp;*&nbsp;</span>
		</td>
	</tr>
	<tr>
		<td><bean:message key="dettaglio.codici.titolo.descrizione"
			bundle="amministrazioneSistemaLabels" /></td>
		<td><html:textarea property="dettaglio.descrizione" rows="3"
			cols="85" />
			<span style="font-size: 80%;">&nbsp;*&nbsp;</span>
		</td>
	</tr>
	<c:if test="${navForm.configCodici.cdUnimarcUsed}">
		<tr>
			<td><bean:message key="dettaglio.codici.titolo.cdSbnMarc"
				bundle="amministrazioneSistemaLabels" />&nbsp; <span
				style="font-size: 80%;"> (Max.&nbsp;<c:out
				value="${navForm.configCodici.cd_unimarc_length}" />&nbsp;car.)
			</span>:&nbsp;</td>
			<td><html:text property="dettaglio.cd_unimarc"
				size="${navForm.configCodici.cd_unimarc_length}"
				maxlength="${navForm.configCodici.cd_unimarc_length}" />
			<c:if test="${!navForm.configCodici.cd_unimarc_allow_blank}">
				<span style="font-size: 80%;">&nbsp;*&nbsp;</span>
			</c:if>
			</td>
		</tr>
	</c:if>
	<c:if test="${navForm.configCodici.cdMarc21Used}">
		<tr>
			<td><bean:message key="dettaglio.codici.titolo.cdUnimarc"
				bundle="amministrazioneSistemaLabels" />&nbsp; <span
				style="font-size: 80%;"> (Max.&nbsp;<c:out
				value="${navForm.configCodici.cd_marc21_length}" />&nbsp;car.)
			</span>:&nbsp;</td>
			<td><html:text property="dettaglio.cd_marc21"
				size="${navForm.configCodici.cd_marc21_length}"
				maxlength="${navForm.configCodici.cd_marc21_length}" />
			<c:if test="${!navForm.configCodici.cd_marc21_allow_blank}">
				<span style="font-size: 80%;">&nbsp;*&nbsp;</span>
			</c:if>
			</td>
		</tr>
	</c:if>
	<c:if test="${navForm.configCodici.tpMaterialeUsed}">
		<tr>
			<td><bean:message key="dettaglio.codici.titolo.tpMateriale"
				bundle="amministrazioneSistemaLabels" />&nbsp; <span
				style="font-size: 80%;">(Max.&nbsp;1&nbsp;car.)</span>:&nbsp;</td>
			<td><html:text property="dettaglio.materiale"
				size="1"
				maxlength="1" />
			<c:if test="${!navForm.configCodici.tp_materiale_allow_blank}">
				<span style="font-size: 80%;">&nbsp;*&nbsp;</span>
			</c:if>
			</td>
		</tr>
	</c:if>
	<c:if test="${navForm.configCodici.dsUlterioreUsed}">
		<tr>
			<td><bean:message key="dettaglio.codici.titolo.dsUlteriore"
				bundle="amministrazioneSistemaLabels" />&nbsp; <span
				style="font-size: 80%;"> (Max.&nbsp;<c:out
				value="${navForm.configCodici.ds_ulteriore_length}" />&nbsp;car.)
			</span>:&nbsp;</td>
			<td><html:textarea property="dettaglio.ds_cdsbn_ulteriore"
				cols="85" rows="${navForm.dsUlterioreRowCount}" /></td>
		</tr>
	</c:if>
	<logic:iterate id="flag" name="navForm"
		property="configCodici.usedFlags">
		<tr>
			<td><bean:write name="flag" property="label" />&nbsp;<span
				style="font-size: 80%;"> (Max.&nbsp;<c:out
				value="${flag.length}" />&nbsp;car.) </span>:&nbsp;</td>
			<td>
			<c:set var="flagValue" value="dettaglio.flag${flag.flg}" />
			<c:choose>
				<c:when test="${flag.type eq 'LIST'}">
					<html:select property="${flagValue}">
						<html:optionsCollection name="flag" property="values"
							value="codice" label="descrizione" />
					</html:select>
				</c:when>
				<c:otherwise>
					<html:text property="${flagValue}" size="${flag.length}"
						maxlength="${flag.length}" />
				</c:otherwise>
			</c:choose>
			<c:if test="${!flag.allow_blank}">
				<span style="font-size: 80%;">&nbsp;*&nbsp;</span>
			</c:if>	</td>
		</tr>
	</logic:iterate>
	<tr>
		<td><bean:message key="dettaglio.codici.nuovo.data"
			bundle="amministrazioneSistemaLabels" />&nbsp; <span
			style="font-size: 80%;">(GG-MM-AAAA)</span>&nbsp;:</td>
		<td><html:text property="dettaglio.dataAttivazione" size="12"
			maxlength="10" disabled="true" /></td>
	</tr>
</table>
<span style="font-size: 11px"><i><bean:message key="nuovo.bibliotecario.obbligo" bundle="amministrazioneSistemaLabels"/></i>.</span>
