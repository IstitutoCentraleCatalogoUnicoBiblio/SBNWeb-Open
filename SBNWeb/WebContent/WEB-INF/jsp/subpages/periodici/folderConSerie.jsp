<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>


<logic:notEmpty name="navForm" property="esame.serie">
	<bs:size id="size" name="navForm" property="esame.serie" />
	<table class="sintetica">
		<logic:iterate name="navForm" property="esame.serie" id="item" indexId="idx">
			<sbn:rowcolor var="color" index="idx" />
				<tr bgcolor="${color}">
				<td>
				<sbn:anchor name="item" property="progr"/>
				<table width="100%">
				<c:choose>
					<c:when test="${item.tipo eq 'ESEMPLARE'}">
						<tr bgcolor="${color}">
						<td width="20%" class="testoBold"><bean:message key="periodici.esame.serie" bundle="periodiciLabels"/></td>
						<td><sbn:linkbutton index="esemplare.uniqueId"
								name="item" value="esemplare.cd_doc" key="button.periodici.esamina"
								bundle="periodiciLabels" title="serie"
								property="selectedItem" submit="true" />
							&nbsp;&#40;<bs:write name="item" property="esemplare.cons_doc"/>&#41;
						</td>

						</tr>
						<tr bgcolor="${color}">
						<td class="testoBold"><bean:message key="periodici.esame.collocazione" bundle="periodiciLabels"/></td>
						<td><sbn:linkbutton index="collocazione.uniqueId"
								name="item" value="collocazione.descrizione" key="button.periodici.esamina"
								bundle="periodiciLabels" title="collocazione"
								property="selectedItem" submit="true" />
								<logic:notEmpty name="item" property="collocazione.consis">
									&nbsp;&#40;<bs:write name="item" property="collocazione.consis"/>&#41;
								</logic:notEmpty>
						</td>
						</tr>
						<logic:notEmpty name="item" property="ordine">
							<tr bgcolor="${color}">
							<td class="testoBold"><bean:message key="periodici.esame.fornitore" bundle="periodiciLabels"/></td>
							<td><bs:write name="item" property="ordine.fornitore" /></td>
							</tr>
							<tr bgcolor="${color}">
							<td class="testoBold"><bean:message key="periodici.esame.ordini" bundle="periodiciLabels"/></td>
							<td><sbn:linkbutton index="ordine.uniqueId"
									name="item" value="ordine.descrizione" key="button.periodici.esamina"
									bundle="periodiciLabels" title="ordine"
									property="selectedItem" submit="true" /></td>
							</tr>
						</logic:notEmpty>
					</c:when>
					<c:when test="${item.tipo eq 'COLLOCAZIONE'}">
						<tr bgcolor="${color}">
						<td width="20%" class="testoBold"><bean:message key="periodici.esame.collocazione" bundle="periodiciLabels"/></td>
						<td><sbn:linkbutton index="collocazione.uniqueId"
								name="item" value="collocazione.descrizione" key="button.periodici.esamina"
								bundle="periodiciLabels" title="collocazione"
								property="selectedItem" submit="true" />
							<logic:notEmpty name="item" property="collocazione.consis">
								&nbsp;&#40;<bs:write name="item" property="collocazione.consis"/>&#41;
							</logic:notEmpty>
						</td>
						</tr>
						<logic:notEmpty name="item" property="ordine">
							<tr bgcolor="${color}">
							<td class="testoBold"><bean:message key="periodici.esame.fornitore" bundle="periodiciLabels"/></td>
							<td><bs:write name="item" property="ordine.fornitore" /></td>
							</tr>
							<tr bgcolor="${color}">
							<td class="testoBold"><bean:message key="periodici.esame.ordini" bundle="periodiciLabels"/></td>
							<td><sbn:linkbutton index="ordine.uniqueId"
									name="item" value="ordine.descrizione" key="button.periodici.esamina"
									bundle="periodiciLabels" title="ordine"
									property="selectedItem" submit="true" /></td>
							</tr>
						</logic:notEmpty>
					</c:when>
					<c:when test="${item.tipo eq 'ORDINE'}">
						<tr bgcolor="${color}">
						<td width="20%" class="testoBold"><bean:message key="periodici.esame.ordini.noserie" bundle="periodiciLabels"/></td>
						<td><sbn:linkbutton index="ordine.uniqueId"
								name="item" value="ordine.descrizione" key="button.periodici.esamina"
								bundle="periodiciLabels" title="ordine"
								property="selectedItem" submit="true" /></td>
						</tr>
					</c:when>
				</c:choose>
				<c:if test="${idx lt (size - 1)}">
					<tr bgcolor="${color}"><td colspan="2">&nbsp;</td></tr>
				</c:if>
				</table>
				</td>
				<td width="3%"><html:radio property="selectedItem" value="${item.uniqueId}" /></td>
		</logic:iterate>
	</table>
</logic:notEmpty>