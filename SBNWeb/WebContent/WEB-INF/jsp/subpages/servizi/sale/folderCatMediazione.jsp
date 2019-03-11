<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"	prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<sbn:blocchi numBlocco="bloccoSelezionato" numNotizie="blocco.totRighe"
	parameter="methodRicercaSale" totBlocchi="blocco.totBlocchi"
	elementiPerBlocco="blocco.maxRighe" disabled="${navForm.conferma}" />
<sbn:disableAll disabled="${navForm.conferma}">
	<table class="sintetica">
		<tr>
			<th width="1%" class="header">#</th>
			<th class="header">
				<bean:message key="servizi.sale.posti.catMediazione" bundle="serviziLabels" />
			</th>
			<th width="1%" class="header">
				<bean:message key="servizi.erogazione.movimento.supporto" bundle="serviziLabels" />
			</th>
			<th width="30%" class="header">
				<bean:message key="documentofisico.tipoMaterialeT" bundle="documentoFisicoLabels" />
			</th>
			<th width="1%" class="header">
				<bean:message key="servizi.calendario.calendario" bundle="serviziLabels" />
			</th>
			<th width="1%" class="header" />
		</tr>
		<l:iterate id="med" name="navForm" property="categorieMediazione" indexId="progr">
			<tr class="row alt-color">
				<td><bs:write name="med" property="cd_cat_mediazione" /></td>
				<td><bs:write name="med" property="descr" /></td>
				<td>
					<c:if test="${med.richiedeSupporto}">
						<bean:message key="label.yes" />
					</c:if>
				</td>
				<td>
					<div>
						<ul class="etichetta">
							<l:iterate id="sup" property="supporti" name="med" indexId="cat">
								<li><bs:write name="sup" /></li>
							</l:iterate>
						</ul>
					</div>
				</td>
				<td>
					<c:if test="${not empty med.calendario }">
						<html:img page="/styles/images/calendar.svg"
							style="width:18px; height: 18px; display:block; margin:auto;"
							title="${med.calendario.descrizione}"/>
					</c:if>
				</td>
				<td>
					<html:radio property="selectedCat" value="${med.repeatableId}" />
				</td>
			</tr>
		</l:iterate>
	</table>
</sbn:disableAll>
