<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>

<html:xhtml />
<layout:page>

	<sbn:navform action="/elaborazioniDifferite/esporta/estrattoreIdAuthority.do" enctype="multipart/form-data">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>

			<table border="0" width="100%">
				<tr>
					<td class="testo" colspan="3">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
					<td class="testo" align="right"><logic:notEmpty name="navForm"
							property="extractionTime">
							<bean:message key="label.ultimaEstrazioneDB"
								bundle="esportaLabels" />&nbsp;
								<bs:write name="navForm" property="extractionTime" />
						</logic:notEmpty></td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
					<td class="testo" align="right"><logic:notEmpty name="navForm"
							property="extractionTime">
							<bean:message key="label.checkUltimaEstrazioneDB"
								bundle="esportaLabels" />&nbsp;
							<bean:message key="label.si" bundle="esportaLabels" />
							<html:radio name="navForm" property="esporta.exportDB"
								value="true" />
							<bean:message key="label.no" bundle="esportaLabels" />
							<html:radio name="navForm" property="esporta.exportDB"
								value="false" />
						</logic:notEmpty></td>
				</tr>
			</table>

			<!-- begin MENU NAVIGAZIONE TABs -->
			<table width="100%" border="0" bgcolor="#FEF1E2">
				<c:choose>
					<c:when test="${navForm.esporta.authority eq 'AU'}">
						<table width="100%" border="0" class="SchedaImg1">
							<tr>
								<td width="86" class="schedaOn" align="center">
									<div align="center">Autori</div>
								</td>
							</tr>
						</table>
					</c:when>
				</c:choose>
				<!-- end MENU NAVIGAZIONE TABs -->
				<!-- begin campi EXPORT -->
				<!-- begin parte comune a tutti i tabs -->
				<!-- end parte comune a tutti i tabs -->
				<c:choose>
					<c:when test="${navForm.esporta.authority eq 'AU'}">
						<!-- richiesta conversione con lista da FILE -->
						<table class="sintetica" width="100%">
							<tr>
								<td>
									<table align="center" border="0" width="95%">
										<tr>
											<td class="schedaOff" colspan="2"><bean:message
													key="label.caricalistaetichettefile" bundle="esportaLabels" /></td>
											<td width="80%">&nbsp;</td>
										</tr>
										<tr>
											<td class="etichetta"><bean:message
													key="label.selezionafile" bundle="esportaLabels" /></td>
											<td class="etichetta"><html:file property="inputFile"
													value="esporta.fileIdListPathNameLocal" size="60" /></td>
											<td class="etichetta"><html:submit
													property="methodEstrattoreIdAuth">
													<bean:message key="button.caricafile"
														bundle="esportaLabels" />
												</html:submit></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</c:when>


				</c:choose>
			</table>

			<div id="divFooter">
				<table align="center">

					<tr>
						<td align="center"><html:submit
								property="methodEstrattoreIdAuth">
								<bean:message key="button.prenotaEstr" bundle="esportaLabels" />
							</html:submit></td>
					</tr>

				</table>
			</div>

		</div>
		<!-- end campi EXPORT -->

	</sbn:navform>
</layout:page>
