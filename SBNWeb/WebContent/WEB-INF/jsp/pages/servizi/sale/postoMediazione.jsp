<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="l"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<script type="text/javascript" src='<c:url value="/scripts/servizi/sale/sale.js" />'></script>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/sale/postoMediazione.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors bundle="serviziMessages" />
			</div>
			<br>
			<table>
				<tr>
					<td bgcolor="${color}" class="header">
						<strong>
							<bean:message key="servizi.sale.posti.intervallo" bundle="serviziLabels" />&nbsp;
							<bs:write name="navForm" property="gruppoPosti.gruppo" />
						</strong>
					</td>
					<td bgcolor="${color}" class="header">
						<bean:message key="servizi.utenti.da" bundle="serviziLabels" />&nbsp;
						<html:text name="navForm" property="gruppoPosti.posto_da" styleClass="digit_short" disabled="true" />&nbsp;
						<bean:message key="servizi.utenti.a" bundle="serviziLabels" />&nbsp;
						<html:text name="navForm" property="gruppoPosti.posto_a" styleClass="digit_short" disabled="true" />
					</td>
				</tr>
			</table>
			<hr/>
			<table class="sintetica">
				<tr>
					<th width="1%" class="header">#</th>
					<th width="7%" class="header">
						<bean:message key="servizi.sale.posti.catMediazione" bundle="serviziLabels" />
					</th>
					<th width="7%" class="header">
						<bean:message key="documentofisico.tipoMaterialeT" bundle="documentoFisicoLabels" />
					</th>
					<th width="1%" class="header" />
				</tr>
				<l:iterate id="med" name="navForm" property="categorieMediazione" indexId="progr">
					<sbn:rowcolor var="color" index="progr" />
					<tr>
						<td bgcolor="${color}"><bs:write name="med" property="cd_cat_mediazione" /></td>
						<td bgcolor="${color}"><bs:write name="med" property="descr" /></td>
						<td bgcolor="${color}">
							<div>
								<ul class="etichetta">
									<l:iterate id="sup" property="supporti" name="med" indexId="cat">
										<li><bs:write name="sup" /></li>
									</l:iterate>
								</ul>
							</div>
						<td bgcolor="${color}">
							<html:hidden property="selected" value="" />
							<html:multibox property="selected" value="${med.uniqueId}" />
						</td>
					</tr>
				</l:iterate>
			</table>
			<br>
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
