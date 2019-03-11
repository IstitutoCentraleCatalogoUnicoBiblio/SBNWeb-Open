<!--	SBNWeb - Rifacimento ClientServer
		JSP di interrogazione Autore
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

<html:xhtml />
<layout:page>

	<sbn:navform
		action="/gestionebibliografica/marca/sinteticaImmaginiMarche.do">

		<div id="divForm">

			<div id="divMessaggio">
				<sbn:errors bundle="gestioneBibliograficaMessages" />
			</div>
			<c:choose>
				<c:when
					test="${sinteticaImmaginiMarcheForm.prospettazionePerLegami eq 'SI'}">
					<table border="0">
						<tr>
							<td class="etichetta">
								<bean:message key="ricerca.titoloRiferimento"
									bundle="gestioneBibliograficaLabels" />
								:
							</td>
							<td width="20" class="testoNormale">
								<html:text property="areaDatiLegameTitoloVO.bidPartenza"
									size="10" readonly="true"></html:text>
							</td>
							<td width="150" class="etichetta">
								<html:text property="areaDatiLegameTitoloVO.descPartenza"
									size="50" readonly="true"></html:text>
							</td>
						</tr>
					</table>
					<hr color="#dde8f0" />
				</c:when>
			</c:choose>
			<br>
			<logic:notEmpty name="sinteticaImmaginiMarcheForm"
				property="listaSintImmagini">
				<table width="100%" border="0">
					<tr bgcolor="#dde8f0">
						<th>
							<bean:message key="sintetica.citazione"
								bundle="gestioneBibliograficaLabels" />
						</th>
						<th>
							<bean:message key="button.immaginiMarche"
								bundle="gestioneBibliograficaLabels" />
						</th>
						<th width="3%"></th>
					</tr>
					<logic:iterate id="img" name="sinteticaImmaginiMarcheForm"
						property="listaSintImmagini" indexId="idx">
						<sbn:rowcolor var="color" index="idx" />
						<tr bgcolor="${color}">
							<td>
								<sbn:linkbutton index="mid" name="img" value="citazione"
									key="button.analiticaMar" bundle="gestioneBibliograficaLabels"
									title="analitica" property="selezImg" />
							</td>
							<td>
								<logic:iterate id="figure" name="img" property="keyImage">
									<html:img page="/caricaImmagineMarca.do?IMGKEY=${figure}"
										width="100" height="100" />&nbsp;
								</logic:iterate>
							</td>
							<td>
								<html:radio property="selezImg" value="${img.mid}" />
							</td>

						</tr>
					</logic:iterate>
				</table>
			</logic:notEmpty>
		</div>

		<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center">
						<html:submit property="methodSintImmMar">
							<bean:message key="button.analiticaMar"
								bundle="gestioneBibliograficaLabels" />
						</html:submit>
					</td>
					<td align="center">
						<html:submit property="methodSintImmMar">
							<bean:message key="button.annulla"
								bundle="gestioneBibliograficaLabels" />
						</html:submit>
					</td>
					<c:choose>
						<c:when
							test="${sinteticaMarcheForm.prospettazionePerLegami eq 'SI'}">
							<td align="center">
								<html:submit property="methodSintImmMar">
									<bean:message key="button.gestLegami.lega"
										bundle="gestioneBibliograficaLabels" />
								</html:submit>
							</td>
						</c:when>
					</c:choose>

				</tr>
			</table>
		</div>

	</sbn:navform>
</layout:page>
