<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/buoniordine/esaminaBuonoOrdine.do">
  <div id="divForm">
  	<div id="divMessaggio">
		<sbn:errors bundle="acquisizioniMessages" />
	</div>
             <table  width="100%"  align="center">
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">
		<table  width="100%" >
		     <tr>
                        <th  class="etichetta" width="10%" scope="col"><div align="left">Codice Polo</div></th>
                        <th scope="col"><div align="left">
                        <input class="testoNormale" name="CodicePolo" type="text" size="5" maxlength="10" value="X11"  disabled>
                        </div></th>
                        <th  class="etichetta" width="10%" scope="col"><div align="left">Codice Bibl. </div></th>
                        <th scope="col"><div align="left">
                        <input class="testoNormale" name="Codice" type="text" size="5" maxlength="10" value="01"  disabled>
                        </div></th>
                        <th  width="10%" scope="col" class="etichetta"><div align="left">Num. buono</div></th>
                        <th scope="col" ><div align="left">
                        <input class="testoNormale" name="Numero" type="text" size="10" maxlength="10" value="000000007" disabled>
                        </div></th>
                        <th width="5%" scope="col" class="etichetta" ><div align="left">Data</div></th>
                        <th scope="col" ><div align="left">
                        <input class="testoNormale" name="data" type="text" size="10" maxlength="10" value="06/02/2006" disabled>
                        </div></th>
		     </tr>
 		     <tr>
                        <td  valign="top" class="etichetta"  scope="col" align="left">Fornitore </td>
                        <td  valign="top" scope="col" colspan="4" align="left" style="width:280px;">
                        <input valign="top" class="testoNormale" name="Fornitore" type="text" size="50" maxlength="50" value="33 - Libreria Grande"  disabled>
						</td>
                        <td  valign="top" scope="col" colspan="2" align="left">
						<a href="../Fornitori/FornitoriRicercaParziale.htm"><img src="../../Images/lente.GIF" alt="Ricerca Fornitore"  border="0"></a>
						</td>
             </tr>
             <tr><td class="etichettaIntestazione" colspan="8">Bilancio:</td></tr>
		     <tr>
                        <th class="etichetta" scope="col"><div align="left">Esercizio </div></th>
                        <th scope="col"><div align="left">
                        <input class="testoNormale" name="Esercizio" type="text" size="10" maxlength="10" value="2006" disabled>
                        </div></th>
                        <th scope="col" class="etichetta"><div align="left">Capitolo</div></th>
                        <th scope="col" colspan="5"><div align="left">
                        <input class="testoNormale" name="Capitolo" type="text" size="10" maxlength="10" value="1" disabled>
                        </div></th>
		     </tr>
		     <tr>
                        <th scope="col" class="etichetta" ><div align="left">Importo</div></th>
                        <th scope="col"><div align="left">
                        <input class="testoNormale" name="importo" type="text" size="10" maxlength="10" value="30,00" disabled>
                        </div></th>
                        <th scope="col"  class="etichetta"><div align="left">Stato</div></th>
                        <th scope="col" colspan="5"><div align="left">
                        <input class="testoNormale" name="stato" type="text" size="10" maxlength="10" value="stampato" disabled>
                        </div></th>
		     </tr>
		     <tr><td  class="etichetta" colspan="8">Num. ordini collegati: <input class="testoNormale" name="numord" type="text" size="3" maxlength="5" value="1" disabled></td></tr>
		     <tr>
                  <td colspan="8">
				  	<table  align="center" width="100%"   style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;"  >
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<th scope="col"><div align="center">Tipo</div></th>
						<th scope="col"><div align="center">Anno</div></th>
						<th scope="col" ><div align="center">Codice</div></th>
						<th scope="col"><div align="center">S</div></th>
						<th scope="col" style="width: 2%;"><div align="center"></div></th>
						<th scope="col" style="width: 10%;"><div align="center">Bid</div></th>
						<th scope="col" style="width: 40%;"><div align="center">Titolo</div></th>
						<th scope="col"><div align="center">Prezzo</div></th>
						<th scope="col" width="2%"><div align="center"></div></th>

					</tr>
				  	<tr class="testoNormale" bgcolor="#FFCC99">
						<td   scope="col"><div align="center">A</div></td>
						<td   scope="col"><div align="center">2006</div></td>
						<td   scope="col"><div align="center">6</div></td>
						<td   scope="col"><div align="center">A</div></td>
						<td   scope="col"><div align="center"><input  type="Checkbox" name="check1"  checked></div></td>
						<td   scope="col"><div align="center">RAV008725</div></td>
						<td   scope="col"><div align="center">Airone : vivere la natura conoscere il mondo<div></td>
						<td   scope="col"><div align="center">30,00</div></td>
						<td   scope="col"><div align="center"><input type="Radio"  name="radio1" ></div></td>

					</tr>

					</table>
				  </td>
			  </tr>
              </table>
		  </td>
		  </tr>
          </table>

           <table align="center"  border="0" style="height:40px" >
            <tr>
             <td >
              <input name="Chiudibuono" type="button" class="pulsanti" id="Chiudibuono" value="Chiudi il buono">
              <a href="InserisciRigaBuonoOrdine.htm"><input name="Insriga" type="button" class="pulsanti" id="Insriga" value="Ins. riga"></a>
              <input name="Cancriga" type="button" class="pulsanti" id="Cancriga" value="Canc. riga"  disabled>
              <input name="Ordine" type="button" class="pulsanti" id="Ordine" value="Ordine">
             </td>
			 </tr>
			 <tr>
             <td  >
              <input name="Conferma" type="button" class="pulsanti" id="Cerca2" value="Ok" onclick="alert('operazione effettuata correttamente!');" >
              <a href="BuoniRicercaParziale.htm"><input  name="Chiudi" type="button" class="pulsanti" id="Chiudi" value="Chiudi"></a>
              <input name="Cancella" type="button" class="pulsanti" id="Cancella" value="Cancella">
              <input name="Stampa" type="button" class="pulsanti" id="Stampa" value="Stampa">
			<html:submit styleClass="pulsanti" property="indietro0">
				<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
			</html:submit>
             </td>
             </tr>
      	  </table>

     	  </div>
	</sbn:navform>
</layout:page>
