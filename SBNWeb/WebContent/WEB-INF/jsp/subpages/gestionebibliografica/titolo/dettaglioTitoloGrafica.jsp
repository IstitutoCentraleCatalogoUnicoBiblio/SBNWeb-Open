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
		<td><c:choose>
			<c:when test="${dettaglioTitoloForm.tipoProspettazione eq 'DET' or dettaglioTitoloForm.tipoProspetSpec eq 'DET'}">
				<table border="0">
					<tr>
						<td width="220" class="etichetta"><bean:message
							key="ricerca.grafic.livAut.specificita"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloGraVO.livAutSpec" descrizione="descLivAutSpecGra" />
						</td>
					</tr>

					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.grafic.designMat"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloGraVO.designMat" descrizione="descDesignMat" />
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="ricerca.grafic.suppPrim"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloGraVO.suppPrim" descrizione="descSuppPrim" />
						</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="ricerca.grafic.indicCol"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloGraVO.indicCol" descrizione="descIndicCol" />
						</td>
					</tr>
				</table>


				<c:choose>
				<c:when
					test="${dettaglioTitoloForm.dettTitComVO.detTitoloGraVO.designMat eq 'a' or
						dettaglioTitoloForm.dettTitComVO.detTitoloGraVO.designMat eq 'b' or
						dettaglioTitoloForm.dettTitComVO.detTitoloGraVO.designMat eq 'c' or
						dettaglioTitoloForm.dettTitComVO.detTitoloGraVO.designMat eq 'k'}">
					<table border="0">
					<tr>
						<td width="220" class="etichetta"><bean:message
							key="ricerca.grafic.indicTecDisegno"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloGraVO.indicTec1" descrizione="descIndicTec1" />
						</td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloGraVO.indicTec2" descrizione="descIndicTec2" />
						</td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloGraVO.indicTec3" descrizione="descIndicTec3" />
						</td>
					</tr>
				</table>

				</c:when>
				<c:otherwise>
					<table border="0">
					<tr>
						<td width="220" class="etichetta" width="120"><bean:message
							key="ricerca.grafic.indicTecGrafica"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloGraVO.indicTecSta1" descrizione="descIndicTecSta1" />
						</td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloGraVO.indicTecSta2" descrizione="descIndicTecSta2" />
						</td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloGraVO.indicTecSta3" descrizione="descIndicTecSta3" />
						</td>
					</tr>
				</table>

				</c:otherwise>
				</c:choose>
				<table border="0">
					<tr>
						<td width="220" class="etichetta"><bean:message
							key="ricerca.grafic.designFun"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale">
							<layout:TextCodDesc codice="dettTitComVO.detTitoloGraVO.designFun" descrizione="descDesignFun" />
						</td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<table border="0">
					<tr>
						<td width="220" class="etichetta"><bean:message
							key="ricerca.grafic.livAut.specificita"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="400" class="testoNormale"><html:select
							property="dettTitComVO.detTitoloGraVO.livAutSpec" style="width:180px">
							<html:optionsCollection property="listaLivAut" value="codice"
										label="descrizioneCodice" />
						</html:select></td>
					</tr>

					<tr>
						<td class="etichetta"><bean:message
							key="gestione.grafic.designMat"
							bundle="gestioneBibliograficaLabels" /></td>
						<td width="300" class="testoNormale"><html:select
							property="dettTitComVO.detTitoloGraVO.designMat"
							style="width:180px"  onchange="this.form.submit()">
							<html:optionsCollection property="listaDesignMat" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="gestione.grafic.suppPrim"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloGraVO.suppPrim"
							style="width:180px">
							<html:optionsCollection property="listaSuppPrim" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="gestione.grafic.indicCol"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="dettTitComVO.detTitoloGraVO.indicCol"
							style="width:180px">
							<html:optionsCollection property="listaIndicCol" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</table>


				<c:choose>
				<c:when
					test="${dettaglioTitoloForm.dettTitComVO.detTitoloGraVO.designMat eq 'a' or
						dettaglioTitoloForm.dettTitComVO.detTitoloGraVO.designMat eq 'b' or
						dettaglioTitoloForm.dettTitComVO.detTitoloGraVO.designMat eq 'c' or
						dettaglioTitoloForm.dettTitComVO.detTitoloGraVO.designMat eq 'k'}">

					<table border="0">
					<tr>
						<td width="220" class="etichetta"><bean:message
							key="ricerca.grafic.indicTecDisegno"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale" width="98"><html:select
							property="dettTitComVO.detTitoloGraVO.indicTec1"
							style="width:180px">
							<html:optionsCollection property="listaIndicTec" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
						<td class="testoNormale" width="98"><html:select
							property="dettTitComVO.detTitoloGraVO.indicTec2"
							style="width:180px">
							<html:optionsCollection property="listaIndicTec" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
						<td class="testoNormale" width="98"><html:select
							property="dettTitComVO.detTitoloGraVO.indicTec3"
							style="width:180px">
							<html:optionsCollection property="listaIndicTec" value="codice"
								label="descrizioneCodice" />
						</html:select></td>

					</tr>
				</table>

				</c:when>
				<c:otherwise>
					<table border="0">
					<tr>
						<td width="220"class="etichetta"><bean:message
							key="ricerca.grafic.indicTecGrafica"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale" width="98"><html:select
							property="dettTitComVO.detTitoloGraVO.indicTecSta1"
							style="width:180px">
							<html:optionsCollection property="listaIndicTecSta" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
						<td class="testoNormale" width="98"><html:select
							property="dettTitComVO.detTitoloGraVO.indicTecSta2"
							style="width:180px">
							<html:optionsCollection property="listaIndicTecSta" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
						<td class="testoNormale" width="98"><html:select
							property="dettTitComVO.detTitoloGraVO.indicTecSta3"
							style="width:180px">
							<html:optionsCollection property="listaIndicTecSta" value="codice"
								label="descrizioneCodice" />
						</html:select></td>

					</tr>
				</table>

				</c:otherwise>
				</c:choose>

				<table border="0">
					<tr>
						<td width="220" class="etichetta"><bean:message
							key="gestione.grafic.designFun"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale" width="300"><html:select
							property="dettTitComVO.detTitoloGraVO.designFun"
							style="width:180px">
							<html:optionsCollection property="listaDesignFun" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose>
</table>
