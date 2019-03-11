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
	<sbn:navform action="/gestionesemantica/thesauro/SinteticaTermini.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<br />
			<table class="sintetica">
				<tr>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="sintetica.did" bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="gestionesemantica.thesauro.thesauro"
							bundle="gestioneSemanticaLabels" />
					</th>
					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="sintetica.headerStato"
							bundle="gestioneSemanticaLabels" />
					</th>

					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="sintetica.termine"
							bundle="gestioneSemanticaLabels" />
					</th>

					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="gestionesemantica.thesauro.legatoTitoli"
							bundle="gestioneSemanticaLabels" />
					</th>

					<th class="etichetta" scope="col" bgcolor="#dde8f0" align="center">
						<bean:message key="gestionesemantica.thesauro.legatoTermini"
							bundle="gestioneSemanticaLabels" />
					</th>
				</tr>
				<bean-struts:define id="color" value="#FEF1E2" />
				<logic:iterate id="listaRicerca" property="listaTermini.risultati"
					name="SinteticaTerminiForm" indexId="progr">
					<sbn:rowcolor var="color" index="progr" />
					<tr bgcolor="${color}">
						<td >
							<sbn:linkbutton index="did" name="listaRicerca" value="did"
								key="button.analitica" bundle="gestioneSemanticaLabels"
								title="analitica" property="didScelto"
								disabled="${listaRicerca.did eq '--'}"/>
						</td>
						<td >
							<bean-struts:write name="listaRicerca" property="codThesauro" />
						</td>
						<td >
							<bean-struts:write name="listaRicerca" property="livelloAutorita" />
						</td>
						<td >
							<bean-struts:write name="listaRicerca" property="termine" />
						</td>
						<td  align="center">
							<html:submit disabled="${listaRicerca.numTitoliPolo eq '0'}"
								property="${sbn:getUniqueButtonName(SinteticaTerminiForm.SUBMIT_TITOLI_COLLEGATI, listaRicerca.did)}">
									${listaRicerca.numTitoliPolo}
								</html:submit>

						</td>
						<td  align="center">
							<html:submit
								disabled="${listaRicerca.numTerminiCollegati eq '0'}"
								property="${sbn:getUniqueButtonName(SinteticaTerminiForm.SUBMIT_TERMINI_COLLEGATI, listaRicerca.did)}">
									${listaRicerca.numTerminiCollegati}
								</html:submit>
						</td>

					</tr>
				</logic:iterate>
			</table>
		</div>

		<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center">
						<html:submit property="methodSinteticaTermini">
							<bean:message key="button.annulla"
								bundle="gestioneSemanticaLabels" />
						</html:submit>
					</td>
				</tr>
			</table>
		</div>

	</sbn:navform>
</layout:page>