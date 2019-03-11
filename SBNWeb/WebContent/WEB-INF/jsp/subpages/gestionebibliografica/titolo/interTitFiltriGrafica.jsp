<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione titolo - Area Grafica
		almaviva2 - Inizio Codifica Agosto 2006
-->
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"
	prefix="bean-struts"%>


<table width="100%" border="0" bgcolor="#FFCC99">
	<tr>
		<td>
		<table border="0">
			<tr>
				<td width="130" class="etichetta"><bean:message
					key="ricerca.grafic.designMat" bundle="gestioneBibliograficaLabels" />
				</td>
				<td width="100" class="testoNormale"><html:select
					property="interrGrafic.designSpecMaterSelez" style="width:100px" onchange="this.form.submit()">
					<html:optionsCollection
						property="interrGrafic.listaDesignSpecMater" value="codice"
						label="descrizioneCodice" />
				</html:select></td>
				<td width="100" class="testoNormale"></td>
				<td width="100" class="testoNormale"></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message key="ricerca.grafic.suppPrim"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:select
					property="interrGrafic.supportoPrimarioSelez" style="width:100px">
					<html:optionsCollection
						property="interrGrafic.listaSupportoPrimario" value="codice"
						label="descrizioneCodice" />
				</html:select></td>
				<td width="100" class="testoNormale"></td>
				<td width="100" class="testoNormale"></td>
			</tr>
			<tr>
				<td class="etichetta"><bean:message key="ricerca.grafic.indicCol"
					bundle="gestioneBibliograficaLabels" /></td>
				<td class="testoNormale"><html:select
					property="interrGrafic.indicatoreColoreSelez" style="width:100px">
					<html:optionsCollection
						property="interrGrafic.listaIndicatoreColore" value="codice"
						label="descrizioneCodice" />
				</html:select></td>
				<td width="100" class="testoNormale"></td>
				<td width="100" class="testoNormale"></td>
			</tr>




			<c:choose>
				<c:when
					test="${interrogazioneTitoloForm.interrGrafic.designSpecMaterSelez eq 'a' or
						interrogazioneTitoloForm.interrGrafic.designSpecMaterSelez eq 'b' or
						interrogazioneTitoloForm.interrGrafic.designSpecMaterSelez eq 'c' or
						interrogazioneTitoloForm.interrGrafic.designSpecMaterSelez eq 'k'}">
					<tr>
						<td width="130" class="etichetta"><bean:message
							key="ricerca.grafic.indicTecDisegno"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="100" class="testoNormale"><html:select
							property="interrGrafic.indicatoreTecnicaSelez1Disegno"
							style="width:100px">
							<html:optionsCollection
								property="interrGrafic.listaIndicatoreTecnicaDisegno"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
						<td width="100" class="testoNormale"><html:select
							property="interrGrafic.indicatoreTecnicaSelez2Disegno"
							style="width:100px">
							<html:optionsCollection
								property="interrGrafic.listaIndicatoreTecnicaDisegno"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
						<td width="100" class="testoNormale"><html:select
							property="interrGrafic.indicatoreTecnicaSelez3Disegno"
							style="width:100px">
							<html:optionsCollection
								property="interrGrafic.listaIndicatoreTecnicaDisegno"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td width="130" class="etichetta"><bean:message
							key="ricerca.grafic.indicTecGrafica"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="100" class="testoNormale"><html:select
							property="interrGrafic.indicatoreTecnicaSelez1Grafica"
							style="width:100px">
							<html:optionsCollection
								property="interrGrafic.listaIndicatoreTecnicaGrafica"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
						<td width="100" class="testoNormale"><html:select
							property="interrGrafic.indicatoreTecnicaSelez2Grafica"
							style="width:100px">
							<html:optionsCollection
								property="interrGrafic.listaIndicatoreTecnicaGrafica"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
						<td width="100" class="testoNormale"><html:select
							property="interrGrafic.indicatoreTecnicaSelez3Grafica"
							style="width:100px">
							<html:optionsCollection
								property="interrGrafic.listaIndicatoreTecnicaGrafica"
								value="codice" label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</c:otherwise>
			</c:choose>

			<tr>
				<td width="130" class="etichetta"><bean:message
					key="ricerca.grafic.designFun" bundle="gestioneBibliograficaLabels" />
				</td>
				<td width="100" class="testoNormale"><html:select
					property="interrGrafic.designatoreFunzioneSelez"
					style="width:100px">
					<html:optionsCollection
						property="interrGrafic.listaDesignatoreFunzione" value="codice"
						label="descrizioneCodice" />
				</html:select></td>
				<td width="100" class="testoNormale"></td>
				<td width="100" class="testoNormale"></td>

			</tr>
		</table>
		</td>
	</tr>
</table>
