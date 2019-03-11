<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<logic:notEmpty name="navForm" property="kardex.intestazione">
	<table width="97%">
	<c:choose>
	<c:when test="${navForm.kardex.tipo eq 'ORDINE'}">
		<tr>
			<td style="max-width: 70%">
				<span class="testoBold"><bean:message key="periodici.esame.ordine" bundle="periodiciLabels"/></span>&nbsp;
				<bs:write name="navForm" property="kardex.intestazione.ordine.descrizione" />
			</td>
			<td>
				<span class="testoBold"><bean:message key="periodici.esame.fornitore" bundle="periodiciLabels"/></span>&nbsp;
				<bs:write name="navForm" property="kardex.intestazione.ordine.fornitore" />
			</td>
			<jsp:include page="/WEB-INF/jsp/subpages/periodici/intestazioneRange.jsp" flush="true" />
		</tr>
	</c:when>
	<c:when test="${navForm.kardex.tipo eq 'COLLOCAZIONE'}">
		<tr>
			<td style="max-width: 70%">
				<span class="testoBold"><bean:message key="periodici.esame.collocazione" bundle="periodiciLabels"/></span>&nbsp;
				<bs:write name="navForm" property="kardex.intestazione.collocazione.descrizione" />
				<logic:notEmpty name="navForm" property="kardex.intestazione.collocazione.consis">
					&nbsp;&#40;<bs:write name="navForm" property="kardex.intestazione.collocazione.consis" />&#41;
				</logic:notEmpty>
			</td>
			<jsp:include page="/WEB-INF/jsp/subpages/periodici/intestazioneRange.jsp" flush="true" />
			<td>
				&nbsp;
			</td>
		</tr>
	</c:when>
	<c:when test="${navForm.kardex.tipo eq 'ESEMPLARE'}">
		<tr>
			<td style="max-width: 70%">
				<span class="testoBold"><bean:message key="periodici.esame.esemplare" bundle="periodiciLabels"/></span>&nbsp;
				<bs:write name="navForm" property="kardex.intestazione.esemplare.descrizione" />
				<logic:notEmpty name="navForm" property="kardex.intestazione.esemplare.cons_doc">
					&nbsp;&#40;<bs:write name="navForm" property="kardex.intestazione.esemplare.cons_doc" />&#41;
				</logic:notEmpty>
			</td>
			<jsp:include page="/WEB-INF/jsp/subpages/periodici/intestazioneRange.jsp" flush="true" />
			<td>
				&nbsp;
			</td>
		</tr>
	</c:when>
	<c:when test="${navForm.kardex.tipo eq 'TITOLO'}">
		<tr>
			<td style="max-width: 70%">
				&nbsp;
			</td>
			<jsp:include page="/WEB-INF/jsp/subpages/periodici/intestazioneRange.jsp" flush="true" />
			<td>
				&nbsp;
			</td>
		</tr>
	</c:when>
	</c:choose>
	<tr>
		<td colspan="4">
			<span class="testoBold"><bean:message key="periodici.esame.titolo" bundle="periodiciLabels"/></span>&nbsp;
			<bs:write name="navForm" property="kardex.intestazione.descrizionePeriodico" />
		</td>
	</tr>
	<c:choose>
		<c:when test="${navForm.kardex.tipo ne 'ORDINE'}">
			<logic:notEmpty name="navForm" property="kardex.intervalloAnnate">
				<tr>
					<td colspan="4">
						<span class="testoBold"><bean:message key="periodici.kardex.annate" bundle="periodiciLabels"/></span>&nbsp;
						<bs:write name="navForm" property="kardex.intervalloAnnate"/>
					</td>
				</tr>
			</logic:notEmpty>
		</c:when>
		<c:otherwise>
			<logic:notEmpty name="navForm" property="kardex.intestazione.ordine.abbonamentoDal">
				<tr>
					<td colspan="4">
						<!--<span class="testoBold">-->
						<bean:message key="periodici.kardex.abbonamento" bundle="periodiciLabels"
								arg0="${navForm.kardex.intestazione.ordine.abbonamentoDal}"
								arg1="${navForm.kardex.intestazione.ordine.abbonamentoAl}" />
						<!--</span>	--></td>
				</tr>
			</logic:notEmpty>
		</c:otherwise>
	</c:choose>
	</table>
</logic:notEmpty>
