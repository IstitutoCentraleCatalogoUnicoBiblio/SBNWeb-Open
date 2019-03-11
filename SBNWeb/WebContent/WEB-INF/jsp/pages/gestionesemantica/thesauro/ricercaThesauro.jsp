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
	<sbn:navform action="/gestionesemantica/thesauro/RicercaThesauro.do">

		<div id="divForm">
		<div id="divMessaggio"><sbn:errors /></div>

		<c:if
			test="${RicercaThesauroForm.modalita eq 'CREA_LEGAME_TERMINI'
							or RicercaThesauroForm.modalita eq 'TRASCINA_TITOLI'}">
			<jsp:include
				page="/WEB-INF/jsp/subpages/gestionesemantica/thesauro/datiDidPartenza.jsp" />
			<br>
		</c:if> <c:if test="${RicercaThesauroForm.modalita eq 'CREA_LEGAME_TITOLO'}">
			<jsp:include
				page="/WEB-INF/jsp/subpages/gestionesemantica/catalogazionesemantica/datiTitolo.jsp" />
			<br>
		</c:if> <logic:notEmpty name="RicercaThesauroForm" property="listaThesauri">
			<table>
				<tr>
					<td width="13%" class="etichetta"><bean:message
						key="gestionesemantica.thesauro.thesauro"
						bundle="gestioneSemanticaLabels" /></td>
					<td><html:select styleClass="testoNormale"
						property="ricercaComune.codThesauro"
						disabled="${RicercaThesauroForm.modalita eq 'CREA_LEGAME_TERMINI'}">
						<html:optionsCollection property="listaThesauri" value="codice"
							label="descrizione" />
					</html:select></td>
				</tr>
			</table>

			<table width="100%">
				<c:if test="${RicercaThesauroForm.modalita eq 'CERCA'}">
					<tr>
						<td class="etichetta" colspan="2"><bean:message
							key="gestionesemantica.thesauro.ricercaTitoli"
							bundle="gestioneSemanticaLabels" /></td>
					</tr>
					<tr>
						<td width="13%" class="etichetta"><bean:message
							key="gestionesemantica.thesauro.termini"
							bundle="gestioneSemanticaLabels" /></td>
						<td width="69%"><html:text
							property="ricercaComune.ricercaTitoliCollegati.termineThes0"
							styleId="testoNormale" size="50" maxlength="80" /> <sbn:tastiera
							limit="80" name="RicercaThesauroForm"
							property="ricercaComune.ricercaTitoliCollegati.termineThes0" />
						</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><html:text
							property="ricercaComune.ricercaTitoliCollegati.termineThes1"
							styleId="testoNormale" size="50" maxlength="80" /> <sbn:tastiera
							limit="80" name="RicercaThesauroForm"
							property="ricercaComune.ricercaTitoliCollegati.termineThes1" />
						</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><html:text
							property="ricercaComune.ricercaTitoliCollegati.termineThes2"
							styleId="testoNormale" size="50" maxlength="80" /> <sbn:tastiera
							limit="80" name="RicercaThesauroForm"
							property="ricercaComune.ricercaTitoliCollegati.termineThes2" />
						</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><html:text
							property="ricercaComune.ricercaTitoliCollegati.termineThes3"
							styleId="testoNormale" size="50" maxlength="80" /> <sbn:tastiera
							limit="80" name="RicercaThesauroForm"
							property="ricercaComune.ricercaTitoliCollegati.termineThes3" />
						</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><html:text
							property="ricercaComune.ricercaTitoliCollegati.termineThes4"
							styleId="testoNormale" size="50" maxlength="80" /> <sbn:tastiera
							limit="80" name="RicercaThesauroForm"
							property="ricercaComune.ricercaTitoliCollegati.termineThes4" />
						</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td class="etichetta"><bean:message
							key="gestionesemantica.thesauro.descrittore"
							bundle="gestioneSemanticaLabels" /></td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</c:if>
				<tr>
					<td class="etichetta"><bean:message
						key="gestionesemantica.thesauro.termine"
						bundle="gestioneSemanticaLabels" /></td>
					<td colspan="3"><html:text styleId="testoNormale"
						property="ricercaComune.ricercaThesauroDescrittore.testoDescr"
						size="50" maxlength="80"></html:text> <sbn:tastiera limit="80"
						name="RicercaThesauroForm"
						property="ricercaComune.ricercaThesauroDescrittore.testoDescr" />
					&nbsp;<bean:message key="ricerca.inizio"
						bundle="gestioneBibliograficaLabels" />&nbsp;<html:radio
						property="ricercaComune.ricercaStringaTipo"
						value="STRINGA_INIZIALE" /> <bean:message key="ricerca.intero"
						bundle="gestioneBibliograficaLabels" />&nbsp;<html:radio
						property="ricercaComune.ricercaStringaTipo" value="STRINGA_ESATTA" />
					<%--<html:select styleClass="testoNormale"
								property="ricercaComune.ricercaStringaTipo">
								<html:optionsCollection property="listaRicercaTipo"
									value="codice" label="descrizione" />
							</html:select>--%></td>
				</tr>
				<tr>
					<td class="etichetta"><bean:message
						key="gestionesemantica.thesauro.did"
						bundle="gestioneSemanticaLabels" /></td>
					<td><html:text styleId="testoNormale"
						property="ricercaComune.ricercaThesauroDescrittore.did"
						maxlength="10"></html:text></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td class="etichetta"><bean:message
						key="gestionesemantica.thesauro.parole"
						bundle="gestioneSemanticaLabels" /></td>
					<td><html:text
						property="ricercaComune.ricercaThesauroDescrittore.parole0"
						styleId="testoNormale" size="50" maxlength="80" /> <sbn:tastiera
						limit="80" name="RicercaThesauroForm"
						property="ricercaComune.ricercaThesauroDescrittore.parole0" /></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>

					<td><html:text
						property="ricercaComune.ricercaThesauroDescrittore.parole1"
						styleId="testoNormale" size="50" maxlength="80" /> <sbn:tastiera
						limit="80" name="RicercaThesauroForm"
						property="ricercaComune.ricercaThesauroDescrittore.parole1" /></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><html:text
						property="ricercaComune.ricercaThesauroDescrittore.parole2"
						styleId="testoNormale" size="50" maxlength="80" /> <sbn:tastiera
						limit="80" name="RicercaThesauroForm"
						property="ricercaComune.ricercaThesauroDescrittore.parole2" /></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><html:text
						property="ricercaComune.ricercaThesauroDescrittore.parole3"
						styleId="testoNormale" size="50" maxlength="80" /> <sbn:tastiera
						limit="80" name="RicercaThesauroForm"
						property="ricercaComune.ricercaThesauroDescrittore.parole3" /></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</table>
			<table>
				<tr>
					<td class="etichetta"><bean:message
						key="gestionesemantica.thesauro.elementoPerBlocco"
						bundle="gestioneSemanticaLabels" /></td>
					<td><html:text styleId="testoNormale"
						property="ricercaComune.elemBlocco" size="10" maxlength="10">
					</html:text></td>
					<!-- ORDINAMENTO -->
					<td class="etichetta"><bean:message
						key="gestionesemantica.thesauro.ordinamento"
						bundle="gestioneSemanticaLabels" /> <html:select
						styleClass="testoNormale"
						property="ricercaComune.ordinamentoTermine">
						<html:optionsCollection property="listaOrdinamentoDescrittore"
							value="codice" label="descrizione" />
					</html:select></td>
				</tr>
			</table>
		</logic:notEmpty></div>
		<!-- BOTTONIERA inserire solo i SOLO td -->

		<div id="divFooter">
		<table align="center">
			<tr>
				<td align="center"><html:submit property="methodRicercaThes">
					<bean:message key="gestionesemantica.thesauro.bottone.cerca"
						bundle="gestioneSemanticaLabels" />
				</html:submit> <logic:equal name="RicercaThesauroForm" property="enableCrea"
					value="true">

					<html:submit property="methodRicercaThes">
						<bean:message key="gestionesemantica.thesauro.bottone.crea"
							bundle="gestioneSemanticaLabels" />
					</html:submit>
				</logic:equal></td>
				<c:if test="${RicercaThesauroForm.modalita ne 'CERCA'}">
					<td align="center"><html:submit property="methodRicercaThes">
						<bean:message key="button.annulla"
							bundle="gestioneSemanticaLabels" />
					</html:submit></td>
				</c:if>
			</tr>
		</table>


		</div>
	</sbn:navform>

</layout:page>

