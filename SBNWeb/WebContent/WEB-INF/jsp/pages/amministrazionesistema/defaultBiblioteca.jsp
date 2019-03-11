<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>


<%@page import="it.iccu.sbn.web.actionforms.amministrazionesistema.gestioneDefault.DefaultBibliotecaForm;"%>
<html:xhtml />
<layout:page>

		<div id="divForm">
<sbn:navform action="/amministrazionesistema/defaultBiblioteca.do">
			<div id="divMessaggio">
					<sbn:errors />
			</div>
		</div>

		<div id="divForm">

				<table border="0">
					<tr>
						<td class="testMsg" style="font-weight: bold; font-size: 15px"><bean:message
							key="profilo.titolo.default"
							bundle="amministrazioneSistemaLabels" />:</td>
					</tr>
					<tr>
						<table border = "0" cellspacing="15">
							<logic:iterate id="item" property="elencoAree" name="defaultBibliotecaForm">
								<tr>
									<td class="etichetta" width="300"><bean:message
										key="${item.idAreaSezione}"
										bundle="amministrazioneSistemaLabels" /></td>
									<td>
									<html:submit styleClass="pulsanti" property="${sbn:getUniqueButtonName(defaultBibliotecaForm.SUBMIT_TESTO_PULSANTE, item.idAreaSezione)}"
										bundle="amministrazioneSistemaLabels">
										>
									</html:submit>
									</td>
								</tr>
							</logic:iterate>
					</tr>
				</table>

		</div>

	</sbn:navform>
</layout:page>
