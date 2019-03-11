<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://common.web.sbn.iccu.it/sbn" prefix="sbn"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="layout"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean-struts"%>

<html:xhtml />
<layout:page>

<sbn:navform action="/acquisizioni/ordini/controllaOrdine.do">

  <div id="divForm">
  	<div id="divMessaggio">
		<div align="center" class="messaggioInfo"><sbn:errors bundle="acquisizioniMessages" /></div>
	</div>

<!-- table--->
             <table  width="100%"  align="center">
       	     <tr>
       	        <!-- tabella corpo COLONNA + LARGA -->
                <td align="left" valign="top" width="100%">
		<table  width="100%" >
		     <tr><td colspan="7" class="navigazione3">Capitolo di bilancio </td></tr>

		     <tr>
                        <th scope="col"><div align="center"></div></th>
						<th  class="etichetta" width="10%" scope="col"><div align="left">Codice Bibl. </div></th>
                        <th scope="col"><div align="left">
                        <input class="testoNormale" name="Codice" type="text" size="5" maxlength="10" value="01"  disabled>
                        </div></th>
                        <th class="etichetta" scope="col"><div align="left">Esercizio </div></th>
                        <th scope="col"><div align="left">
                        <input class="testoNormale" name="Esercizio" type="text" size="10" maxlength="10" value="2006" disabled>
                        </div></th>
                        <th scope="col" class="etichetta"><div align="left">Capitolo</div></th>
                        <th scope="col" ><div align="left">
                        <input class="testoNormale" name="Capitolo" type="text" size="10" maxlength="10" value="3" disabled>
                        </div></th>

 		     <tr>
		     <tr><td colspan="7" class="etichetta" >Budget <input  class="testoNormale"  name="budget" type="text" size="15" maxlength="15" value="50000,00" disabled></td></tr>
		     <tr>
                  <td colspan="7">
				  	<table align="center" width="100%"  style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;" >
				  	<tr class="etichetta" bgcolor="#dde8f0">
	     				<th scope="col"><div align="center"></div></th>
						<td align="center" title="Codice impegno" scope="col">Imp.</td>
						<td align="center" scope="col">Budget</td>
						<td align="center" scope="col" >Impegnato</td>
						<td align="center" scope="col">Fatturato</td>
						<td align="center" scope="col">Pagato</td>
						<td align="center" scope="col">Disp. di cassa</td>
					</tr>
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<td align="center" scope="col" colspan="3">&nbsp;</td>
						<td align="center" scope="col">impegn. da Fatt.</td>
						<td align="center" scope="col" colspan="2">&nbsp;</td>
						<td align="center" scope="col">Disp. di compet.</td>
					</tr>

				  	<tr class="testoNormale" bgcolor="#FFCC99">
						<td   scope="col"><div align="center"><input  type="Radio" name="check1"></div></td>
						<td   scope="col"><div align="center">
						<select class="testoNormale" name="Stato" style="width:40px" >
		  		          <option value="" ></option>
		                  <option value="1" selected>1  - Monografie e periodici non in abbonamento</option>
		                  <option value="2">2  - Periodici in abbonamento</option>
		        		  <option value="3" >3  - Collane</option>
	           	        </select>
						</div></td>
						<td   scope="col"><div align="center"><input  class="testoNormale" name="budget" type="text" size="10" maxlength="15" value="10000,00" ></div></td>
						<td   scope="col"><div align="center"><input   class="testoNormale" name="impegnato" type="text" size="10" maxlength="15" value="4000,00" disabled></div></td>
						<td   scope="col"><div align="center"><input   class="testoNormale" name="fatturato" type="text" size="10" maxlength="15" value="1000,00" disabled></div></td>
						<td   scope="col"><div align="center"><input   class="testoNormale" name="pagato" type="text" size="10" maxlength="15" value="0,00" disabled></div></td>
						<td   scope="col"><div align="center"><input   class="testoNormale" name="dispcassa" type="text" size="10" maxlength="15" value="10000,00" disabled></div></td>
					</tr>
				  	<tr class="testoNormale" bgcolor="#FFCC99">
						<td   align="center" scope="col" colspan="3">&nbsp;</td>
						<td   scope="col"><div align="center"><input   class="testoNormale" name="impegnatoFatt" type="text" size="10" maxlength="15" value="3000,00" disabled></div></td>
						<td   align="center" scope="col" >&nbsp;</td>
						<td   scope="col"><div align="center"><input   class="testoNormale" name="dispcomp" type="text" size="10" maxlength="15" value="6000,00" disabled></div></td>
					</tr>


				  	<tr class="testoNormale">
						<td   scope="col"><div align="center"><input  type="Radio" name="check1"></div></td>
						<td   scope="col"><div align="center">
						<select class="testoNormale" name="Stato" style="width:40px" >
		  		          <option value="" ></option>
		                  <option value="1" >1  - Monografie e periodici non in abbonamento</option>
		                  <option value="2" selected>2  - Periodici in abbonamento</option>
		        		  <option value="3" >3  - Collane</option>
	           	        </select>
						</div></td>
						<td   scope="col"><div align="center"><input  class="testoNormale" name="budget" type="text" size="10" maxlength="15" value="30000,00"> </div></td>
						<td   scope="col"><div align="center"><input   class="testoNormale" name="impegnato" type="text" size="10" maxlength="15" value="2000,00" disabled></div></td>
						<td   scope="col"><div align="center"><input   class="testoNormale" name="fatturato" type="text" size="10" maxlength="15" value="0,00" disabled></div></td>
						<td   scope="col"><div align="center"><input   class="testoNormale" name="pagato" type="text" size="10" maxlength="15" value="0,00" disabled></div></td>
						<td   scope="col"><div align="center"><input class="testoNormale" name="dispcassa" type="text" size="10" maxlength="15" value="30000,00" disabled></div></td>
					</tr>
					<tr>
						<td   align="center" scope="col" colspan="3">&nbsp;</td>
						<td   scope="col"><div align="center"><input type="text" class="testoNormale" name="impegnatoFatt"  size="10" maxlength="15" value="2000,00" disabled></div></td>
						<td   align="center" scope="col" colspan="2" >&nbsp;</td>
						<td   scope="col"><div align="center"><input type="text" class="testoNormale" name="dispcomp"  size="10" maxlength="15" value="28000,00" disabled></div></td>
					</tr>



					</table>
			   <br><br>
				  	<table  align="center" width="100%"  style="border-color: #5A96DF;	border-style: dotted; border-width: 1px;"  >
				  	<tr class="etichetta" bgcolor="#dde8f0">
						<th scope="col"><div align="center">Cod. Sez.</div></th>
						<th scope="col"><div align="center">Nome Sezione</div></th>
						<th scope="col" colspan="2"><div align="center" >Somma Disp.</div></th>

					</tr>
				  	<tr class="testoNormale" bgcolor="#FFCC99">
						<td   scope="col"><div align="center">COLLIT</div></td>
						<td   scope="col"><div align="center">COLLEZIONI ITALIANE</div></td>
						<td   scope="col"><div align="center">+</div></td>
						<td   scope="col"><div align="center">900.000,00</div></td>
					</tr>
				  	<tr class="testoNormale" bgcolor="#FEF1E2">
						<td   scope="col"><div align="center">COLLST</div></td>
						<td   scope="col"><div align="center">COLLEZIONI STRANIERE</div></td>
						<td   scope="col"><div align="center">+</div></td>
						<td   scope="col"><div align="center">200.000,00</div></td>
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
			<html:submit styleClass="pulsanti" property="methodControllaOrdine">
				<bean:message key="ricerca.button.indietro" bundle="acquisizioniLabels" />
			</html:submit>

             </td>

			 </tr>
      	  </table>

	</div>
	</sbn:navform>
</layout:page>
