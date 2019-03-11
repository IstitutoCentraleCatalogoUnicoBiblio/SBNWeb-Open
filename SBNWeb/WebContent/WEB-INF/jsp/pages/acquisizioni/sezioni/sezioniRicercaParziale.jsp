<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/sezioni/sezioniRicercaParziale.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>

<c:choose>
	<c:when test="${sezioniRicercaParzialeForm.gestSez}">


		  <table   width="100%" border="0">
		     <tr>
                        <td  width="10%" scope="col" class="etichetta" align="left">
							<bean:message  key="ricerca.label.codBibl" bundle="acquisizioniLabels" />
                        </td>
                        <td scope="col" align="left">
						  <html:text styleId="testoNormale" property="codBiblio" size="3" readonly="true"></html:text>
	                        <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodSezioniRicercaParziale" >
								<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
							</html:submit>
                        </td>
                        <td scope="col" colspan="2" class="etichetta" align="left">
							<bean:message  key="ricerca.label.codSezione" bundle="acquisizioniLabels" />
						  <html:text styleId="testoNormale" property="codSezione" size="7"></html:text>
                        </td>

                        <td  colspan="2" scope="col" class="etichetta" align="left">
							<bean:message  key="ricerca.label.nomeSezione" bundle="acquisizioniLabels" />
  					  		<html:text styleId="testoNormale" property="nomeSezione" size="30"></html:text>
                        </td>
             </tr>
		     <tr>
                        <td   scope="col" class="etichetta" align="right">
							&nbsp;
                        </td>
                        <td scope="col" align="left">
                        	<bean:message  key="ordine.label.aperto" bundle="acquisizioniLabels" />
							<html:radio property="soloChiuse" value="false" />
							<bean:message  key="ordine.label.chiuso" bundle="acquisizioniLabels" />
							<html:radio property="soloChiuse" value="true" />
                        </td>
		     </tr>


         </table>
   	  </div>
	 <div id="divFooterCommon">
			<!-- tabella bottoni -->
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
			<!-- tabella bottoni -->
		 <div id="divFooter">

           <table align="center" border="0" style="height:40px" >
            <tr>
             <td >
             <sbn:checkAttivita idControllo="CERCA">
				<html:submit styleClass="pulsanti" property="methodSezioniRicercaParziale">
					<bean:message key="ricerca.button.cerca" bundle="acquisizioniLabels" />
				</html:submit>
			</sbn:checkAttivita>

		<c:choose>
			<c:when test="${!sezioniRicercaParzialeForm.LSRicerca}">
             <sbn:checkAttivita idControllo="CREA">
				<html:submit styleClass="pulsanti" property="methodSezioniRicercaParziale">
					<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
				</html:submit>
			</sbn:checkAttivita>
			</c:when>
		</c:choose>

				<c:choose>
					<c:when test="${sezioniRicercaParzialeForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodSezioniRicercaParziale">
							<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
				</c:choose>
             </td>
          </tr>
      	  </table>
	  			<!-- fine tabella bottoni -->
	</c:when>
</c:choose>

    	  </div>
	</sbn:navform>
</layout:page>
