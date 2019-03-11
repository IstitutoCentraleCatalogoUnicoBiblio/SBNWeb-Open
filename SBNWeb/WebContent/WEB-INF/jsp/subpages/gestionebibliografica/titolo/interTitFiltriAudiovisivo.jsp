<!-- almaviva2 Evolutiva Ottobre 2014 per Gestione nuovi Tipi Materiale Audiovisivo/Discosonoro
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

		<c:choose>
			<c:when	test="${interrogazioneTitoloForm.interrGener.tipiRecordSelez eq 'g'}">


				<table border="0">
					<tr>
						<td width="250" class="etichetta"><bean:message
							key="ricerca.audiov.tipoVideo" bundle="gestioneBibliograficaLabels" />
						</td>
						<td width="100" class="testoNormale"><html:select
							property="interrAudiovisivo.tipoVideoSelez" style="width:100px" onchange="this.form.submit()">
							<html:optionsCollection
								property="interrAudiovisivo.listaTipoVideo" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="ricerca.audiov.formaPubblDistr"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="interrAudiovisivo.formaPubblDistrSelez" style="width:100px">
							<html:optionsCollection
								property="interrAudiovisivo.listaFormaPubblDistr" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message key="ricerca.audiov.tecnicaVideoFilm"
							bundle="gestioneBibliograficaLabels" /></td>
						<td class="testoNormale"><html:select
							property="interrAudiovisivo.tecnicaVideoFilmSelez" style="width:100px">
							<html:optionsCollection
								property="interrAudiovisivo.listaTecnicaVideoFilm" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
				</table>

			</c:when>

			<c:when	test="${interrogazioneTitoloForm.interrGener.tipiRecordSelez eq 'i'
					|| interrogazioneTitoloForm.interrGener.tipiRecordSelez eq 'j'}">


				<table>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.audiov.formaPubblicazioneDisco" bundle="gestioneBibliograficaLabels" />
						</td>
						<td class="testoNormale"><html:select
							property="interrAudiovisivo.formaPubblicazioneSelez"
							style="width:100px">
							<html:optionsCollection
								property="interrAudiovisivo.listaFormaPubblicazione" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="ricerca.audiov.velocita" bundle="gestioneBibliograficaLabels" />
						</td>
						<td class="testoNormale"><html:select
							property="interrAudiovisivo.velocitaSelez"
							style="width:100px">
							<html:optionsCollection
								property="interrAudiovisivo.listaVelocita" value="codice"
								label="descrizioneCodice" />
						</html:select></td>
					</tr>

				</table>
			</c:when>
			</c:choose>
		</td>
	</tr>
</table>
