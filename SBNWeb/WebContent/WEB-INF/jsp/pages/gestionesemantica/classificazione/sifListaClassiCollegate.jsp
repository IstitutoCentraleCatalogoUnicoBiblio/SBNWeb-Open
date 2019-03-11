<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>

<html:xhtml />
<layout:page>

	<sbn:navform
		action="/gestionesemantica/classificazione/SIFListaClassiCollegate.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<table width="100%" border="0">
				<tr>
					<td class="etichetta">
						<strong><bean:message key="catalogazione.bid"
								bundle="gestioneSemanticaLabels" /> </strong>
					<td class="etichetta">
						<strong><bean:message key="catalogazione.testoBid"
								bundle="gestioneSemanticaLabels" /> </strong>
					</td>
				</tr>
				<tr>
					<td width="10%">
						<html:text styleId="testoNoBold" property="datiSIF.bid" size="14"
							readonly="true" />
					</td>
					<td width="100%" align="left">
						<html:text styleId="testoNoBold" styleClass="expandedLabel"
							property="datiSIF.titolo" readonly="true" />

					</td>
				</tr>
			</table>


			<sbn:blocchi numBlocco="numBlocco" numNotizie="totRighe"
				parameter="methodSinteticaCla" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe" livelloRicerca="P" />

			<logic:notEmpty name="SIFListaClassiCollegateForm" property="output">
				<table class="sintetica">
					<tr>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
							<bean:message key="sintetica.progr"
								bundle="gestioneSemanticaLabels" />
						</th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
							<bean:message key="sintetica.condiviso"
								bundle="gestioneSemanticaLabels" />
						</th>

						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
							<bean:message key="sintetica.identificativo"
								bundle="gestioneSemanticaLabels" />
						</th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
							<bean:message key="sintetica.headerStato"
								bundle="gestioneSemanticaLabels" />
						</th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
							<bean:message key="sintetica.descrizione"
								bundle="gestioneSemanticaLabels" />
						</th>
						<th class="etichetta" scope="col" bgcolor="#dde8f0"></th>
					</tr>
					<bean-struts:define id="color" value="#FEF1E2" />
					<logic:iterate id="listaRicerca" property="output.risultati"
						name="SIFListaClassiCollegateForm" indexId="progr">
						<sbn:rowcolor var="color" index="progr" />
						<tr>
							<td bgcolor="${color}" >
								<sbn:anchor name="listaRicerca" property="progr" />
								<bean-struts:write name="listaRicerca" property="progr" />
							</td>
							<td bgcolor="${color}" >
								<bean-struts:write name="listaRicerca" property="condivisoLista" />
							</td>
							<td bgcolor="${color}" >
								<bean-struts:write name="listaRicerca"
									property="identificativoClasse" />
							</td>
							<td bgcolor="${color}" >
								<bean-struts:write name="listaRicerca"
									property="livelloAutorita" />
							</td>

							<td bgcolor="${color}" >
								<bean-struts:write name="listaRicerca" property="parole" />
							</td>
							<td bgcolor="${color}" >
								<html:radio property="codSelezionato"
									value="${listaRicerca.simbolo}" />
							</td>

						</tr>
					</logic:iterate>
				</table>
			</logic:notEmpty>

			<sbn:blocchi numBlocco="numBlocco" numNotizie="totRighe"
				parameter="methodSinteticaCla" totBlocchi="totBlocchi"
				elementiPerBlocco="maxRighe" bottom="true" />

		</div>
		<div id="divFooter">
			<table align="center">
				<tr>

					<td align="center">
						<html:submit property="methodSinteticaCla">
							<bean:message key="button.scegli"
								bundle="gestioneSemanticaLabels" />
						</html:submit>
					</td>

					<td align="center">
						<html:submit property="methodSinteticaCla">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit>
					</td>

				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>

