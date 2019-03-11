<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bs"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/elaborazioniDifferite/importaUtenti.do" enctype="multipart/form-data">
		<div id="divForm">
			<div id="divMessaggio">
				<sbn:errors />
			</div>
					<table border="0"  align="center" class="etichetta">
						<tr><td align="center"></td></tr>
						<tr><td align="center">Procedura di inserimento degli UTENTI LETTORI </td></tr>
						<tr><td align="center">e delle relative autorizzazioni a partire da un file sequenziale</td></tr>
						<tr><td align="center"></td></tr>
					</table>

					<table border="0"  align="center" class="etichetta">

						<tr>
							<td align="center">
								<table align="center">
									<tr>
										<td class="etichetta">Carica file:&nbsp; <html:file property="uploadImmagine" />
										</td>
									</tr>
									<sbn:checkAttivita idControllo="ESSE3">
									<tr>
										<!-- LFV 18/07/18 checkbox verifica dati esse3-->
		                       			<td class="etichetta"><bean:message  key="servizi.importaUtenti.tipoDatiImportUtenti" bundle="serviziLabels" />
					 		  				<!--<html:checkbox property="isEsse3"  /> -->
					 		  				<html:select property="isEsse3">
					 		  				<html:option value="false">Tracciato 1</html:option>
					 		  				<html:option value="true">Tracciato 2</html:option>
					 		  				</html:select>
	                        			</td>
									</tr>
									</sbn:checkAttivita>
									<tr>
										<td class="etichetta">
											<html:submit property="methodImportaUte">
												<bean:message key="servizi.bottone.caricaFileImportaUtenti" bundle="serviziLabels" />
											</html:submit>
										</td>
									</tr>
									<tr>
	                        			<td class="etichetta"><bean:message  key="servizi.importaUtenti.data" bundle="serviziLabels" />
					 		  				<html:text styleId="testoNormale" property="dataDa" size="10"></html:text>
	                        			</td>

									</tr>
								</table>
							</td>
						</tr>

					</table>



		</div>
		<div id="divFooter">
		<table align="center">

			<tr>
				<td align="center"><html:submit property="methodImportaUte">
					<bean:message key="servizi.bottone.richiestaImportaUtenti" bundle="serviziLabels" />
				</html:submit></td>
			</tr>

		</table>
		</div>
	</sbn:navform>
</layout:page>



