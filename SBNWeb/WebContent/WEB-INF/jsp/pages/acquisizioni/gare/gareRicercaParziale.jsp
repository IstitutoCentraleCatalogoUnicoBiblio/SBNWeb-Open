<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<bean-struts:define id="noinput"  value="false"/>
<c:choose>
<c:when test="${gareRicercaParzialeForm.disabilitaTutto}">
      <script type="text/javascript">
		<bean-struts:define id="noinput"  value="true"/>
      </script>
</c:when>
</c:choose>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/gare/gareRicercaParziale.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>
    <table   width="100%" 	border-style: dotted; border-width: 1px;">
      <tr>
        <td  width="9%" scope="col" class="etichetta" align="left">
         <bean:message  key="ricerca.label.codiceBibl" bundle="acquisizioniLabels" />
        </td>
        <td scope="col" width="5%" align="left">
			<html:text styleId="testoNormale" property="codBibl" size="10" readonly="true"></html:text>
            <html:submit title="elenco" styleClass="buttonImageListaSezione" property="methodGareRicercaParziale" disabled="${noinput}">
				<bean:message  key="ricerca.label.bibliolist" bundle="acquisizioniLabels" />
			</html:submit>

        </td>
        <td scope="col" colspan="2" class="etichetta" align="left">
         <bean:message  key="buono.label.statoBuono" bundle="acquisizioniLabels" />
			<html:select styleClass="testoNormale"  name="gareRicercaParzialeForm" property="statoRichiestOfferta" disabled="${noinput}">
			<html:optionsCollection  property="listaStatoRichiestaOfferta" value="codice" label="descrizione" />
			</html:select>
        </td>

      </tr>
      <tr>
        <td width="8%" class="etichetta" align="right">
         <bean:message  key="ordine.label.bid" bundle="acquisizioniLabels" />
        </td>
        <td scope="col"  class="etichetta" width="44%" align="left">
<!--    	  <html:text styleId="testoNormale" property="desBid" size="30" readonly="true"></html:text> -->
<!--		  <html:link action="/gestionebibliografica/titolo/interrogazioneTitolo.do" ><img border="0"   alt="ricerca titolo"  src='<c:url value="/images/lente.GIF" />'/></html:link>-->

			<table border="0" cellpadding="0" cellspacing="0"  >
			<tr>
			<td valign="top" align="left">
				<html:text styleId="testoNormale" property="codBid" size="10" readonly="${noinput}"></html:text>
			</td>
			<td bgcolor="#EBEBE4" valign="top" align="left" >
				<bean-struts:write  name="gareRicercaParzialeForm" property="desBid" />
			</td>
			<td valign="top" align="left">
              <html:submit  styleClass="buttonImage" property="methodGareRicercaParziale" disabled="${noinput}">
				<bean:message  key="ordine.bottone.searchTit" bundle="acquisizioniLabels" />
			  </html:submit>
			</td>

			</tr>
			</table >
        </td>
      </tr>

      <tr>
        <td scope="col" class="etichetta" width="17%" align="right">
	         <bean:message  key="ricerca.label.codRichOff" bundle="acquisizioniLabels" />
        </td>
        <!--	onchange="this.form.submit();"-->
        <td scope="col" class="etichetta" width="17%" align="left" >
			<html:text styleId="testoNormale" property="codRichiestaOfferta" size="5" ></html:text>
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
				<td width="150" class="testoNormale">
				<html:select
					property="tipoOrdinamSelez" >
					<html:optionsCollection property="listaTipiOrdinam"
						value="codice" label="descrizione" />
				</html:select></td>
		     </tr>
      	  </table>
   	  	</div>
		 <div id="divFooter">

	<table align="center"  border="0" style="height:40px" >
            <tr>
             <td align="center">
             <sbn:checkAttivita idControllo="CERCA">
			<html:submit styleClass="pulsanti" property="methodGareRicercaParziale">
				<bean:message key="ricerca.button.cerca" bundle="acquisizioniLabels" />
			</html:submit>
			</sbn:checkAttivita>

		<c:choose>
			<c:when test="${!gareRicercaParzialeForm.LSRicerca}">
             <sbn:checkAttivita idControllo="CREA">
				<html:submit styleClass="pulsanti" property="methodGareRicercaParziale">
					<bean:message key="ricerca.button.crea" bundle="acquisizioniLabels" />
				</html:submit>
			</sbn:checkAttivita>
			</c:when>
		</c:choose>

				<c:choose>
					<c:when test="${gareRicercaParzialeForm.visibilitaIndietroLS}">
						<html:submit styleClass="pulsanti" property="methodGareRicercaParziale">
							<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
						</html:submit>
					</c:when>
				</c:choose>
			 </td>
		  </tr>
      	  </table>

     	  </div>
	</sbn:navform>
</layout:page>
