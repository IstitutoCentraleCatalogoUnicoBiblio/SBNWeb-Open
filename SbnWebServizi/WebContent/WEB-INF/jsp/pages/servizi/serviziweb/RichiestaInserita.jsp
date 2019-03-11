<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />

<layout:page>
<div id="header"></div>
<div id="data">
	<sbn:navform action="/serviziweb/richiestaInserita.do" >
	<table cellspacing="0" width="100%" border="0">

			<tr>
				<th colspan="4" class="etichetta" align="right">
				<bean:message key="servizi.documento.poloEserWeb"
					bundle="serviziWebLabels" />
				<hr>
				</th>
			</tr>
			<tr>
				<th colspan="4" class="etichetta" align="center">
				<bean:message key="servizi.documento.insRich"
					bundle="serviziWebLabels" />

				</th>
			</tr>
			<tr>
				<td >
					&nbsp;
				</td>
			</tr>

			<tr>
				<th colspan="4" class="etichetta" align="center">
				<bean:message key="servizi.documento.successoRich"
					bundle="serviziWebLabels" />

				<hr>
				</th>
			</tr>
			<tr>
				<td align="center">
					<html:submit styleClass="submit" property="paramRicIns">
						<bean:message key="servizi.bottone.ok" bundle="serviziWebLabels" />
					</html:submit>

				</td>
			</tr>
		</table>
	</sbn:navform>
</div>
</layout:page>