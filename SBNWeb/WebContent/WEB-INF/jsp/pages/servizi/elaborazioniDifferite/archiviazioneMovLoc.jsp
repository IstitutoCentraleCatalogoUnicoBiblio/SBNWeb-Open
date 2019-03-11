<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>


<html:xhtml />
<layout:page>
	<sbn:navform action="/servizi/elaborazioniDifferite/archiviazioneMovLoc.do">
		<div id="divForm">
			<div id="divMessaggio"><sbn:errors /></div>
			<br>
			<table width="100%" border="0">
				<tr>
					<td colspan="2">
						<bean:message  key="servizi.archiviazioneMovimenti.tit" bundle="serviziLabels" />
					</td>
				</tr>
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>

				<tr>
					<td width="20%" >
					<div class="etichetta"><bean:message
						key="servizi.utenti.codiceBiblioteca" bundle="serviziLabels" />
					<html:text disabled="true" styleId="testoNormale" property="codBib"
						size="5" maxlength="3"></html:text> <span disabled="true">
						<html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodArchMovLoc" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
						</html:submit></span>
					 </div>
					</td>
					<td align="left">
						<bean-struts:write	name="archiviazioneMovLocForm" property="descrBib" />
					</td>
				</tr>

				<tr>
					<td align="right" class="etichetta">
						<bean:message key="servizi.archiviazioneMovimenti.dataInizio" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="dataInizio" size="10" maxlength="10" />
					</td>
				</tr>

				<tr>
					<td align="right" class="etichetta">
						<bean:message key="servizi.archiviazioneMovimenti.data" bundle="serviziLabels" />
					</td>
					<td align="left">
						<html:text styleId="testoNoBold" property="dataSvecchiamento" size="10" maxlength="10" />
					</td>
				</tr>
			</table>
		</div>
		<br>
		<br>
		<div id="divFooter">
			<table align="center">
				<tr>
					<td align="center">
						<html:submit styleClass="pulsanti" property="methodArchMovLoc">
							<bean:message key="servizi.bottone.conferma" bundle="serviziLabels" />
						</html:submit>

						<html:submit styleClass="pulsanti"	property="methodArchMovLoc">
							<bean:message key="servizi.bottone.indietro" bundle="serviziLabels" />
						</html:submit>

					</td>
				</tr>
			</table>
		</div>
	</sbn:navform>
</layout:page>



