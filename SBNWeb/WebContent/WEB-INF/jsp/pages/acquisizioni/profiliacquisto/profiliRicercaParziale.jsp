<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput"  value="false"/>
<c:choose>
<c:when test="${profiliRicercaParzialeForm.disabilitaTutto}">
      <script type="text/javascript">
		<bean-struts:define id="noinput"  value="true"/>
      </script>
</c:when>
</c:choose>



<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/profiliacquisto/profiliRicercaParziale.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>
<c:choose>
	<c:when test="${profiliRicercaParzialeForm.gestProf}">

		  <table   width="100%" border="0">
		     <tr>
                        <td  width="10%" scope="col" class="etichetta" align="left">
	                   	<bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
						<html:text styleId="testoNormale" property="codBibl" size="3" readonly="true"></html:text>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodProfiliRicercaParziale" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>
                        </td>
                        <td class="etichetta" scope="col" align="left">
	                   	<bean:message  key="ricerca.label.codSezione" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
						<html:text styleId="testoNormale" property="codSezione" size="7" readonly="${noinput}"></html:text>
                        <html:submit  styleClass="buttonImageListaSezione" property="methodProfiliRicercaParziale" disabled="${noinput}">
							<bean:message  key="ricerca.label.sezione" bundle="acquisizioniLabels" />
						</html:submit>

                        </td>
             </tr>
		     <tr>
                        <td  width="10%" scope="col" class="etichetta" align="left">
	                   	<bean:message  key="ricerca.label.descrizione" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
						<html:text styleId="testoNormale" property="descrizioneProf" size="50" readonly="${noinput}"></html:text>
                        </td>
                        <td scope="col" class="etichetta" align="left">
	                   	<bean:message  key="ricerca.label.codProfilo" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
						<html:text styleId="testoNormale" property="codProfilo" size="5"  ></html:text>
                        </td>

             </tr>

            </table>
   	  </div>
	 <div id="divFooterCommon">
           <table  width="100%"   border="0" style="height:40px" >
		     <tr>
				<td width="80" class="etichetta"><bean:message
					key="ricerca.label.elembloccoshort" bundle="acquisizioniLabels" /></td>
				<td width="150" class="testoNormale"><html:text
					property="elemXBlocchi" size="5"></html:text></td>
				<td width="75" class="etichetta"><bean:message
					key="ricerca.label.ordinamento" bundle="acquisizioniLabels" /></td>
				<td width="150" class="testoNormale"><html:select
					property="tipoOrdinamSelez" >
					<html:optionsCollection property="listaTipiOrdinam"
						value="codice" label="descrizione" />
				</html:select></td>
		     </tr>
      	  </table>
  	  </div>
 <div id="divFooter">

			<!-- tabella bottoni -->
           <table align="center" border="0" style="height:40px" >
            <tr>
            <td>
             <sbn:checkAttivita idControllo="CERCA">
			<html:submit styleClass="pulsanti" property="methodProfiliRicercaParziale">
				<bean:message key="ricerca.button.cerca" bundle="acquisizioniLabels" />
			</html:submit>
			</sbn:checkAttivita>

		<c:choose>
			<c:when test="${!profiliRicercaParzialeForm.LSRicerca}">
             <sbn:checkAttivita idControllo="CREA">
				<html:submit styleClass="pulsanti" property="methodProfiliRicercaParziale">
					<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
				</html:submit>
			</sbn:checkAttivita>
			</c:when>
		</c:choose>


		<c:choose>
			<c:when test="${profiliRicercaParzialeForm.visibilitaIndietroLS}">
				<html:submit styleClass="pulsanti" property="methodProfiliRicercaParziale">
					<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
				</html:submit>
			</c:when>
		</c:choose>
				</td>
		     </tr>
      	  </table>

	</c:when>
</c:choose>

    	  </div>
	</sbn:navform>
</layout:page>
