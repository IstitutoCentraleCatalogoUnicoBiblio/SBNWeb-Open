<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>


<html:xhtml />
<layout:page>
<sbn:navform
	action="/gestionestampe/semantica/selezioneStampaSemantica.do">
	<div id="divMessaggio">
		<sbn:errors />
	</div>

	<div id="divForm">
		<table border="0">
			<tr>
				<td class="testMsg" style="font-weight: bold; font-size: 15px"><bean:message
					key="semantica.scegli.stampa" bundle="gestioneStampeLabels" />:</td>
			</tr>
			<tr>
				<table border="0" cellspacing="15">
					<logic:iterate id="item" property="listaStampe"	name="selezioneStampaSemanticaForm" indexId="idx">
					<sbn:checkAttivita idControllo="${item.codAttivita}">
						<tr>
							<td class="etichetta" width="300">
								<bs:write name="item" property="descrizione" />
							</td>
							<td>
								<html:submit styleClass="pulsanti"
									property="${sbn:getUniqueButtonName(selezioneStampaSemanticaForm.SUBMIT_TESTO_PULSANTE, idx)}">&gt;
								</html:submit>
							</td>
						</tr>
					</sbn:checkAttivita>
					</logic:iterate>
				</table>
			</tr>
		</table>

	</div>

</sbn:navform>
</layout:page>