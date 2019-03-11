<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>

<html:xhtml />
<layout:page>
	<sbn:navform action="/acquisizioni/documenti/inserisciDoc.do">
  <div id="divForm">
	<div id="divMessaggio"><sbn:errors /></div>

		<table   width="100%" border="0">
     		<tr><td  colspan="7" class="etichetta">&nbsp;</td></tr>
		     <tr>
 						<td  class="etichetta" width="15%" scope="col"><div align="left">Cod. Bib.</div></td>
                        <td  scope="col"><div align="left">
                        <input class="testoNormale" name="Codice" type="text" size="5"  value="01"  disabled>
                       </div></td>
						<td  class="etichetta"  scope="col"><div align="right">Numero</div></td>
                        <td  scope="col"><div align="left">
                        <input class="testoNormale" name="statosugg" type="text" size="5"  value="62"  disabled>
                       </div></td>
						<td  class="etichetta" width="15%" scope="col"><div align="right">Tipo</div></td>
                        <td  scope="col"><div align="left">
                        <input class="testoNormale" name="Codice" type="text" size="20"  value="Documento posseduto dalla biblioteca"  disabled>
                       </div></td>
 						<td  class="etichetta" scope="col"><div align="left">&nbsp;</div></td>

			</tr>
		     <tr>
 						<td  class="etichetta" width="15%" scope="col"><div align="left">Cod. Utente</div></td>
                        <td  scope="col"><div align="left">
                        <input class="testoNormale" name="Codice" type="text" size="5"  value=""  disabled>
                        <input class="testoNormale" name="Codice" type="text" size="5"  value=""  disabled>
                       </div></td>
						<td  class="etichetta"  scope="col"><div align="right">Fonte</div></td>
                        <td  scope="col"><div align="left">
                        <input class="testoNormale" name="statosugg" type="text" size="10"  value="Bibliotecario"  disabled>
                       </div></td>

			</tr>
		     <tr>
                        <th  width="10%" scope="col" class="etichetta"><div align="left">Biblioteca</div></th>
                        <th scope="col" colspan="4"><div align="left">
			              <select disabled class="testoNormale" name="Natura" style="width:300px" >
			      		  <option value="" selected></option>
			              <option value="D" >A - Accettato</option>
			              <option value="C">W - Attesa di risposta</option>
			              <option value="M" >O - Ordinario</option>
			              </select>
                        </div></th>

            </tr>
		     <tr>
 						<td  class="etichetta" width="15%" scope="col"><div align="left">Segnatura</div></td>
                        <td  scope="col" colspan="3"><div align="left">
                        <input class="testoNormale" name="Codice" type="text" size="40"  value="" >
                       </div></td>
						<td  class="etichetta"  scope="col"><div align="right">N. volume</div></td>
                        <td  scope="col"><div align="left">
                        <input class="testoNormale" name="statosugg" type="text" size="10"  value=""  >
                       </div></td>

			</tr>
		     <tr>
                        <th  width="10%" scope="col" class="etichetta"><div align="left">Cat. Fruiz.</div></th>
                        <th scope="col" ><div align="left">
			              <select  class="testoNormale" name="Natura" style="width:60px" >
			      		  <option value="" selected></option>
			              <option value="D" >C - consultazione e fotoriproduzione</option>
			              <option value="C">A - consultazione, prestito, fotoriproduzione</option>
			              <option value="M" >D - prestito e consultazione</option>
			              <option value="M" >B - solo consultazione</option>
			              <option value="M" >T - tutti i servizi</option>
			              </select>
                        </div></th>
                        <th  width="10%" colspan="2" scope="col" class="etichetta"><div align="right">Non disponibile per</div></th>
                        <th scope="col" ><div align="left">
			              <select  class="testoNormale" name="Natura" style="width:60px" >
			      		  <option value="" selected></option>
			              <option value="D" >A - alluvionato</option>
			              <option value="C">D - danneggiato</option>
			              <option value="M" >R - in restauro</option>
 			              <option value="M" >L - legatura</option>
  			              <option value="M" >Q - periodico non rilegato</option>
  			              <option value="M" >S - smarrito</option>

			              </select>
                        </div></th>
						<td  class="etichetta" colspan="2" scope="col"><div align="left">Pubbl. Seriale
						<input  class="testoNormale" type="Checkbox"   name="check1" >
                       </div></td>

            </tr>
		     <tr>
 						<td  class="etichetta"  scope="col"><div align="left">Bid</div></td>
                        <td  scope="col" colspan="2"><div align="left">
                        <input class="testoNormale" name="Codice" type="text" size="10"  value="" >
                      <a href="../../Interrogazione/SbnInterrogazioneTitolo.htm"><img src="../Images/lente.GIF" alt="Ricerca Sezione" width="20" height="21" border="0"></a>
                       </div></td>
 						<td  class="etichetta"  scope="col"><div align="right">Data ins.</div></td>
                        <td  scope="col"><div align="left">
                        <input class="testoNormale" name="Codice" type="text" size="10"  value="03/05/2006">
                        </div></td>
 						<td  class="etichetta"  scope="col"><div align="left">Data agg.
                        <input class="testoNormale" name="Codice" type="text" size="10"  value="03/05/2006">
                        </div></td>

 			</tr>

		     <tr>
 						<td  class="etichetta"  scope="col"><div align="left">Titolo</div></td>
                        <td  scope="col" colspan="6"><div align="left">
                        <input class="testoNormale" name="Codice" type="text" size="90"  value="" >
                       </div></td>

			</tr>
		     <tr>
 						<td  class="etichetta"  scope="col"><div align="left">Autore</div></td>
                        <td  scope="col" colspan="6"><div align="left">
                        <input class="testoNormale" name="Codice" type="text" size="90"  value="" >
                       </div></td>

			</tr>
		     <tr>
 						<td  class="etichetta"  scope="col"><div align="left">Note</div></td>
                        <td  scope="col" colspan="6"><div align="left">
                        <input class="testoNormale" name="Codice" type="text" size="90"  value="" >
                       </div></td>

			</tr>

		     <tr>
 						<td  class="etichetta" scope="col"><div align="left">Editore</div></td>
                        <td  scope="col" ><div align="left">
                        <input class="testoNormale" name="Codice" type="text" size="10"  value=""  >
                       </div></td>
 						<td  class="etichetta" scope="col"><div align="right">Luogo</div></td>
                        <td  scope="col" ><div align="left">
                        <input class="testoNormale" name="Codice" type="text" size="10"  value=""  >
                       </div></td>

			</tr>
		     <tr>
                        <th  width="10%" scope="col" class="etichetta"><div align="left">Paese</div></th>
                        <th scope="col" ><div align="left">
			            <select class="testoNormale" name="Natura" style="width:100px" >
                        <option value="" selected></option>
                        <option value=""  >IT - ITALIA</option>
                        <option value=" 02">FR - Francia</option>
                        <option value=" 03">JP - Giappone</option>
                        </select>
                        </div></th>
                        <th  width="10%"  scope="col" class="etichetta"><div align="right">Lingua</div></th>
                        <th scope="col" ><div align="left">
			            <select class="testoNormale" name="Natura" style="width:100px" >
                        <option value="" selected></option>
                        <option value="" >ITA - Italiano</option>
                        <option value=" 03">FRE - Francese</option>
                        <option value=" 03">JPN - Giapponese</option>
                        </select>
                        </div></th>
						<td  class="etichetta"  scope="col"><div align="right">Anno</div></td>
                        <td  scope="col"><div align="left">
                        <input class="testoNormale" name="statosugg" type="text" size="10"  value=""  >
                       </div></td>

            </tr>

		     <tr>
						<td   class="etichetta"   align="left" scope="col">Note per il lettore</td>
                        <td  colspan="5" scope="col"><div align="left">
                        <textarea cols="80" class="testoNormale"></textarea>
                       </div></td>

			</tr>

     		<tr><td  colspan="7" class="etichetta">&nbsp;</td></tr>

</table>
<hr>
<table   width="100%" style="background-color:#F7F7F7 ; border-color: #5A96DF;	border-style:outset; border-width: 1px;">
				  	<table  align="center" width="100%"  border="1"    bgcolor="#e0e0e0">
				  	<tr class="etichetta">
						<th scope="col" align="center" >Copia/Inventario</th>
						<th scope="col" align="center" >Annata</th>
						<th scope="col" align="center" >Non disponibile per</th>
						<th scope="col" align="center" style="width: 10%;">Fonte</th>
						<th scope="col" align="center" style="width: 10%;">Canc.</th>
						<th scope="col" width="2%" align="center"></th>
					</tr>

				  	<tr class="testoNormale">
						<td bgcolor="#ffffff" scope="col"><div align="center"><input  class="testoNormale" name="anno" type="text" size="10" value="vol. 1"></div></td>
						<td bgcolor="#ffffff" scope="col"><div align="center"><input   class="testoNormale" name="codice" type="text" size="10"  value="" ></div></td>
						<td bgcolor="#ffffff" scope="col"><div align="center">
			              <select  class="testoNormale" name="Natura"  >
			      		  <option value="" selected></option>
			              <option value="D" >A - alluvionato</option>
			              <option value="C">D - danneggiato</option>
			              <option value="M" >R - in restauro</option>
 			              <option value="M" >L - legatura</option>
  			              <option value="M" >Q - periodico non rilegato</option>
  			              <option value="M" >S - smarrito</option>
			              </select>
						</div></td>
						<td bgcolor="#ffffff" scope="col"><div align="center"><input  class="testoNormale" name="titolo" type="text"   size="15" value="bibliotecario" disabled></div></td>
						<td bgcolor="#ffffff" scope="col"><div align="center"><input  class="testoNormale" name="prezzo" type="text" size="5"  value="" disabled> </div></td>
						<td bgcolor="#ffffff" scope="col"><div align="center"><input type="Radio" class="testoNormale"  name="radio1" ></div></td>

					</tr>

					</table>
</table>
	<br>

				<!-- tabella bottoni -->
           <table width="100%"  border="0" style="height:40px" >
             <td  valign="top" align="center">
              <input name="Conferma" type="button" class="pulsanti" id="Cerca2" value="Ok"  onclick="javascript:alert('operazione effettuata correttamente!');">
              <input name="Cerca" type="reset" class="pulsanti" id="Cerca2" value="Pulisci">
              <a href="SugLetRicercaParziale.htm"><input name="Annulla" type="submit" class="pulsanti" id="Annulla" value="Annulla"></a>
              <a href="#"><input name="Chiudi" type="button" class="pulsanti" id="Chiudi" value="Ins. copia"></a>
              <a href="#"><input name="Chiudi" type="button" class="pulsanti" id="Chiudi" value="Canc. copia"></a>
 			<html:submit styleClass="pulsanti" property="indietro0">
				<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
			</html:submit>

             </td>
             </tr>
      	  </table>
	  			<!-- fine tabella bottoni -->
     	  </div>
	</sbn:navform>
</layout:page>
