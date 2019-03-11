<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput"  value="false"/>

<c:choose>
<c:when test="${inserisciSezioneForm.sezione.chiusa}">
	<bean-struts:define id="noinput"  value="true"/>
</c:when>
</c:choose>

<c:choose>
<c:when test="${inserisciSezioneForm.disabilitaTutto}">
	<bean-struts:define id="noinput"  value="true"/>
</c:when>
</c:choose>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/sezioni/inserisciSezione.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>

		<table   width="100%" border="0">

		     <tr>
 						<td  class="etichetta" width="20%" scope="col" align="left">
	                        <bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
 						</td>
                        <td  scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="sezione.codBibl" size="5" readonly="true"></html:text>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodInserisciSezione" disabled="${noinput}">
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>

                        </td>
                        <td  class="etichetta">&nbsp;</td>
			</tr>
		     <tr>
						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.codiceSezione" bundle="acquisizioniLabels" />
						</td>
                        <td colspan="2"  scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="sezione.codiceSezione"  size="7"  maxlength="7" readonly="${noinput}"></html:text>
                        </td>
			</tr>
		     <tr>
						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.nomeSezione" bundle="acquisizioniLabels" />
						</td>
                        <td colspan="2"  scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="sezione.descrizioneSezione" size="50" readonly="${noinput}"></html:text>
                        </td>
			</tr>

		     <tr>
						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.sommaDisp" bundle="acquisizioniLabels" />
						</td>
                        <td colspan="2"  scope="col" align="left">
                        	<bean-struts:write format="0.00" name="inserisciSezioneForm" property="sezione.sommaDispSezione" />
                        </td>
			</tr>
		     <tr>
						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ricerca.label.budget" bundle="acquisizioniLabels" />
						</td>
                        <td scope="col" align="left">
                        	<html:text styleId="testoNormale"  property="sezione.budgetSezioneStr" size="20" readonly="${noinput}" ></html:text>
                        </td>


                       <td scope="col" align="left" class="etichetta" >
	                        <bean:message  key="ricerca.label.dataFineValidita" bundle="acquisizioniLabels" />
                        	<html:text styleId="testoNormale"  property="sezione.dataVal" size="10" readonly="${noinput}" ></html:text>
                         	<bean:message  key="ordine.data.formato" bundle="acquisizioniLabels" />

                        </td>

			</tr>


		     <tr>
						<td  class="etichetta"  scope="col" align="left">
	                        <bean:message  key="ordine.label.noteEtic" bundle="acquisizioniLabels" />
						</td>
                        <td colspan="2" scope="col" align="left">
                        <html:textarea styleId="testoNormale" property="sezione.noteSezione" rows="1" cols="50" readonly="${noinput}"></html:textarea>
						<c:choose>
						<c:when test="${inserisciSezioneForm.disabilitaTutto eq false}">
							<sbn:tastiera limit="80" property="sezione.noteSezione" name="inserisciSezioneForm"></sbn:tastiera>
						</c:when>
						</c:choose>

                        </td>

			</tr>


		</table>
  </div>
  <div id="divFooter">

				<!-- tabella bottoni -->
		<c:choose>
			<c:when test="${inserisciSezioneForm.conferma}">
				<jsp:include page="/WEB-INF/jsp/subpages/acquisizioni/utility/confermaOperazione.jsp" />
			</c:when>
			<c:otherwise>
           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >
				<c:choose>
				<c:when test="${!inserisciSezioneForm.sezione.chiusa}">
					<html:submit styleClass="pulsanti" property="methodInserisciSezione">
						<bean:message key="ricerca.button.salva" bundle="acquisizioniLabels" />
					</html:submit>
					<html:submit styleClass="pulsanti" property="methodInserisciSezione">
						<bean:message key="ricerca.button.ripristina" bundle="acquisizioniLabels" />
					</html:submit>
					<logic:equal  name="inserisciSezioneForm" property="visibilitaIndietroLS" value="true">
						<html:submit  styleClass="pulsanti" property="methodInserisciSezione">
							<bean:message key="ricerca.button.scegli" bundle="acquisizioniLabels" />
						</html:submit>
					</logic:equal>
				</c:when>
				</c:choose>

		 			<html:submit styleClass="pulsanti" property="methodInserisciSezione">
						<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
					</html:submit>

             </td>
             </tr>
      	  </table>
    		</c:otherwise>
		</c:choose>
  	  </div>
	</sbn:navform>
</layout:page>
