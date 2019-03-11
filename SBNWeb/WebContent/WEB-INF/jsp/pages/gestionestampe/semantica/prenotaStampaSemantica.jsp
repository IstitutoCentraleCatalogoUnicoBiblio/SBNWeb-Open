<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>

	<sbn:navform
		action="/gestionestampe/semantica/prenotaStampaSemantica.do">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
			<div id="content">
				<table width="100%" align="center">
					<tr>
						<!-- tabella corpo COLONNA + LARGA -->
						<td align="left" valign="top" width="100%"><c:if
								test="${navForm.parametri.codAttivita eq 'ZS431' or navForm.parametri.codAttivita eq 'ZS432'}">
								<table width="100%" border="0">
									<tr>
										<td width="15%" valign="top" scope="col">
											<div align="left" class="etichetta">
												<bean:message key="soggettario.label.codSogg"
													bundle="gestioneStampeLabels" />
											</div>
										</td>
										<td valign="top" scope="col">
											<div align="left">
												<html:select styleClass="testoNormale"
													property="parametri.codSoggettario">
													<html:optionsCollection property="listaSoggettari"
														value="codice" label="descrizione" />
												</html:select>
											</div>
										</td>
										<td><div align="left" class="etichetta">
												<bean:message key="ricerca.edizione"
													bundle="gestioneSemanticaLabels" />
												&nbsp;
												<html:select styleClass="testoNormale"
													property="parametri.edizioneSoggettario">
													<html:optionsCollection property="listaEdizioni"
														value="cd_tabellaTrim" label="ds_tabella" />
												</html:select>
											</div></td>
									</tr>
								</table>
							</c:if> <c:if test="${navForm.parametri.codAttivita eq 'ZS437'}">
								<table width="100%" border="0">
									<tr>
										<td width="15%" valign="top" scope="col">
											<div align="left" class="etichetta">
												<bean:message key="thesauroUt.label.codThe"
													bundle="gestioneStampeLabels" />
											</div>
										</td>
										<td valign="top" scope="col">
											<div align="left">
												<html:select styleClass="testoNormale"
													property="parametri.codThesauro">
													<html:optionsCollection property="listaThesauri"
														value="codice" label="descrizione" />
												</html:select>
											</div>
										</td>
									</tr>
								</table>
							</c:if>

							<hr />

							<table border="0">
								<tr>
									<td valign="top" width="25%" scope="col">
										<div align="left" class="etichetta">
											<bean:message key="thesauroPolo.label.insDal"
												bundle="gestioneStampeLabels" />
										</div>
									</td>
									<td valign="top" scope="col">
										<div align="left">
											<html:text styleId="testoNormale"
												property="parametri.insFrom" size="10" maxlength="10"></html:text>
										</div>
									</td>
									<td valign="top" scope="col">
										<div align="left" class="etichetta">
											<bean:message key="thesauroPolo.label.insAl"
												bundle="gestioneStampeLabels" />
										</div>
									</td>
									<td valign="top" scope="col">
										<div align="left">
											<html:text styleId="testoNormale" property="parametri.insTo"
												size="10" maxlength="10"></html:text>
										</div>
									</td>
								</tr>
								<tr>
									<td valign="top" width="25%" scope="col">
										<div align="left" class="etichetta">
											<bean:message key="thesauroPolo.label.aggDal"
												bundle="gestioneStampeLabels" />
										</div>
									</td>
									<td valign="top" scope="col">
										<div align="left">
											<html:text styleId="testoNormale"
												property="parametri.varFrom" size="10" maxlength="10"></html:text>
										</div>
									</td>

									<td valign="top" scope="col">
										<div align="left" class="etichetta">
											<bean:message key="thesauroPolo.label.aggAl"
												bundle="gestioneStampeLabels" />
										</div>
									</td>
									<td valign="top" scope="col">
										<div align="left">
											<html:text styleId="testoNormale" property="parametri.varTo"
												size="10" maxlength="10"></html:text>
										</div>
									</td>
								</tr>
							</table>

							<hr /> <c:if test="${navForm.parametri.codAttivita eq 'ZS431'}">
								<jsp:include flush="true"
									page="/WEB-INF/jsp/subpages/gestionestampe/semantica/opzioniSoggettario.jsp" />
							</c:if> <c:if test="${navForm.parametri.codAttivita eq 'ZS432'}">
								<jsp:include flush="true"
									page="/WEB-INF/jsp/subpages/gestionestampe/semantica/opzioniDescrittori.jsp" />
							</c:if> <c:if test="${navForm.parametri.codAttivita eq 'ZS437'}">
								<jsp:include flush="true"
									page="/WEB-INF/jsp/subpages/gestionestampe/semantica/opzioniThesauro.jsp" />
							</c:if>
					</tr>
				</table>
				<HR>
				<jsp:include flush="true"
					page="/WEB-INF/jsp/pages/gestionestampe/common/tipoStampa.jsp" />
				<HR>
				<div id="divFooter">
					<table align="center" border="0" style="height: 40px">
						<tr>
							<td><html:submit styleClass="pulsanti"
									property="methodStampaSem">
									<bean:message key="button.conferma"
										bundle="gestioneStampeLabels" />
								</html:submit> <html:submit styleClass="pulsanti" property="methodStampaSem">
									<bean:message key="button.indietro"
										bundle="gestioneStampeLabels" />
								</html:submit></td>
						</tr>
					</table>
				</div>
			</div>
	</sbn:navform>
</layout:page>
