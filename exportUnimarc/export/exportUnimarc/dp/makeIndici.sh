#!/bin/bash
#/*******************************************************************************
#  * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
#  *
#  * This program is free software: you can redistribute it and/or modify
#  * it under the terms of the GNU Affero General Public License as published
#  * by the Free Software Foundation, either version 3 of the License, or
#  * (at your option) any later version.
#  *
#  * This program is distributed in the hope that it will be useful,
#  * but WITHOUT ANY WARRANTY; without even the implied warranty of
#  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  * GNU Affero General Public License for more details.
#  *
#  * You should have received a copy of the GNU Affero General Public License
#  * along with this program. If not, see <http://www.gnu.org/licenses/>.
#  ******************************************************************************/

# -----------------------------------------------------------------------------------
# Per redirigere l'output si STDERR e STDOUT su file con processo in background usare
#
#  ./makeIndici.sh &> makeIndici.log &
# -----------------------------------------------------------------------------------

# se non si mette questo il sort toppa sull'underscore
export LC_COLLATE=C


#-------------
# nohup ./makeIndici.sh > makeIndici.log &
#
# Comandi utili
# per estrarre un insieme di righe: awk -F, 'NR>1000 && NR<=1010 {print $1}' tr_tit_tit.out.srt
#
#-------------

ora="date +%Y-%m-%d-%H:%M:%S"
echo `${ora}` "Inizio processo di makeIndici.sh" > makeIndici.proc


echo ===================
echo INIZIO ELABORAZIONE
echo ===================



echo ==================================================================================================
echo "Ordina i file che hanno occorrenze multiple della chiave"
echo ==================================================================================================

java it.finsiel.offlineExport.RearrangeFields ../db_export/tba_ordini.out input/tba_ordini.out.swp

echo sort ../db_export/tba_ordini.out.swp
sort -k 1.1,1.10 -o input/tba_ordini.out.swp.srt input/tba_ordini.out.swp

echo sort tb_numero_std.out
sort -k 1.1,1.10 -o input/tb_numero_std.out.srt ../db_export/tb_numero_std.out

echo sort ../db_export/tr_tit_bib.out
sort -k 1,22 -o input/tr_tit_bib.out.srt ../db_export/tr_tit_bib.out

echo sort ../db_export/tr_tit_aut.out
sort -k 1,23 -o input/tr_tit_aut.out.srt ../db_export/tr_tit_aut.out

echo sort tb_incipit.out
sort -k 1.1,1.10 -o input/tb_incipit.out.srt ../db_export/tb_incipit.out

echo sort tb_personaggio.out
sort -k 1.1,1.10 -o input/tb_personaggio.out.srt ../db_export/tb_personaggio.out

echo sort ../db_export/tr_per_int.out
sort -T ./tmp -k 1,10 -o input/tr_per_int.out.srt ../db_export/tr_per_int.out

echo sort tbc_nota_inv.out
#sort -t% -k 4.1,4.10 -o input/tbc_nota_inv.out.srt ../db_export/tbc_nota_inv.out
sort -k 1,28 -o input/tbc_nota_inv.out.srt ../db_export/tbc_nota_inv.out

echo sort ../db_export/tbc_possessore_provenienza.out
sort -k 1,10 -o input/tbc_possessore_provenienza.out.srt ../db_export/tbc_possessore_provenienza.out


echo sort ../db_export/tbc_sezione_collocazione
sort -k 1,22 -o input/tbc_sezione_collocazione.out.srt ../db_export/tbc_sezione_collocazione.out

echo sort ../db_export/trc_poss_prov_inventari.out
sort -k 1.33,1.35 -k 1.27,1.29 -k 1.14,1.23 -o input/trc_poss_prov_inventari.out.srt ../db_export/trc_poss_prov_inventari.out

echo sort tb_repertorio.out
sort  -t% -k1 -n -o input/tb_repertorio.out.srt ../db_export/tb_repertorio.out

echo sort tb_rep_mar.out, ordiniamo sul mid
sort  -k 1.14,1.23 -o input/tr_rep_mar.out.srt ../db_export/tr_rep_mar.out

echo sort ../db_export/tb_nota.out
sort -k 1,10 -o input/tb_nota.out.srt ../db_export/tb_nota.out

echo sort tr_rep_aut.out
sort -k 1.1,1.10 -o input/tr_rep_aut.out.srt ../db_export/tr_rep_aut.out

# 15/10/2010
echo sort ../db_export/tb_parola.out
sort -T ./tmp -k 1,10 -o input/tb_parola.out.srt ../db_export/tb_parola.out

echo sort ts_link_multim.out
sort -k 1.14,1.24 -o input/ts_link_multim.out.srt ../db_export/ts_link_multim.out

echo sort ../db_export/tr_tit_mar.out
sort -T ./tmp -u -k 1,23 -o input/tr_tit_mar.out.srt ../db_export/tr_tit_mar.out



#echo sort tr_sog_des.out
#sort -k 1.1,1.10 -o input/tr_sog_des.out.srt ../db_export/tr_sog_des.out
#echo sort tr_sog_des.out INVERSA
#sort -k 1.14,1.23 -k 1,10 -o input/tr_sog_des.out.srt ../db_export/tr_sog_des.out
#echo sort tr_des_des.out
#sort -k 1.1,1.10 -o input/tr_des_des.out.srt ../db_export/tr_des_des.out



# 22/04/2013
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_descrittore.out input/tb_descrittore.out.off
echo sort input/tb_descrittore.off
sort -T ./tmp -k 1.1,1.10 -o input/tb_descrittore.out.off.srt input/tb_descrittore.out.off


# CID/BID invertiti rispetto a polo
#echo sort tr_sog_des.out
#sort -k 1.1,1.10 -o input/tr_sog_des.out.srt ../db_export/tr_sog_des.out
sort -k 1.14,1.23 -k 1,10 -o input/tr_sog_des.out.srt ../db_export/tr_sog_des.out
java it.finsiel.offlineExport.CreateOffsetFile input/tr_sog_des.out.srt input/tr_sog_des.out.srt.off db=polo


echo sort tr_des_des.out
sort -k 1.1,1.10 -o input/tr_des_des.out.srt ../db_export/tr_des_des.out
java it.finsiel.offlineExport.CreateOffsetFile input/tr_des_des.out.srt input/tr_des_des.out.srt.off

echo sort tr_des_des.out invertita
sort -k 1.14,1.23 -o input/tr_des_des.out.inv.srt ../db_export/tr_des_des.out
java it.finsiel.offlineExport.CreateOffsetFile input/tr_des_des.out.inv.srt input/tr_des_des.out.inv.srt.off listaInvertita

# end 22/04/2013



echo =================================================================================================
echo "Step 1. Crea il file degli offset [indici]: CreateOffsetFile.class a partire dai dati esportati dal DB"
echo =================================================================================================
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tbf_biblioteca.out input/tbf_biblioteca.out.off
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_autore.out input/tb_autore.out.off
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_classe.out input/tb_classe.out.off
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_impronta.out input/tb_impronta.out.off
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_luogo.out input/tb_luogo.out.off
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_marca.out input/tb_marca.out.off
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_soggetto.out input/tb_soggetto.out.off
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_titolo.out input/tb_titolo.out.off
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tr_aut_aut.out input/tr_aut_aut.out.off
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_grafica.out input/tb_grafica.out.off
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_cartografia.out input/tb_cartografia.out.off
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_musica.out input/tb_musica.out.off
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_rappresent.out input/tb_rappresent.out.off
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_composizione.out input/tb_composizione.out.off
java it.finsiel.offlineExport.CreateOffsetFile input/tba_ordini.out.swp.srt input/tba_ordini.out.swp.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tr_tit_bib.out.srt input/tr_tit_bib.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tr_tit_aut.out.srt input/tr_tit_aut.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tb_numero_std.out.srt input/tb_numero_std.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tb_personaggio.out.srt input/tb_personaggio.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tr_per_int.out.srt input/tr_per_int.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tb_incipit.out.srt input/tb_incipit.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tbc_nota_inv.out.srt input/tbc_nota_inv.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tbc_possessore_provenienza.out.srt input/tbc_possessore_provenienza.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tbc_sezione_collocazione.out.srt input/tbc_sezione_collocazione.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/trc_poss_prov_inventari.out.srt input/trc_poss_prov_inventari.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tb_repertorio.out.srt input/tb_repertorio.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tr_rep_mar.out.srt input/tr_rep_mar.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tb_nota.out.srt input/tb_nota.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tr_rep_aut.out.srt input/tr_rep_aut.out.srt.off

# 15/10/2010
java it.finsiel.offlineExport.CreateOffsetFile input/ts_link_multim.out.srt input/ts_link_multim.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tb_parola.out.srt input/tb_parola.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tr_tit_mar.out.srt input/tr_tit_mar.out.srt.off


#java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_descrittore.out input/tb_descrittore.out.off
#java it.finsiel.offlineExport.CreateOffsetFile input/tr_sog_des.out.srt input/tr_sog_des.out.srt.off
#java it.finsiel.offlineExport.CreateOffsetFile input/tr_des_des.out.srt input/tr_des_des.out.srt.off

# 16/12/14
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_977.out input/tb_977.out.off
# 12/02/2015
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_audiovideo.out input/tb_audiovideo.out.off
# 19/02/2015
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_disco_sonoro.out input/tb_disco_sonoro.out.off

# 09/03/2015
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_titset_1.out input/tb_titset_1.out.off

# 03/12/2015
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_termine_thesauro.out input/tb_termine_thesauro.out.off

# 19/04/2016
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tb_titset_2.out input/tb_titset_2.out.off

# 12/10/2016
java it.finsiel.offlineExport.CreateOffsetFile ../db_export/tr_bid_altroid.out input/tr_bid_altroid.out.off


echo =================================================================================================
echo "Step 2 Ordina il file degli offset"
echo =================================================================================================


echo sort input/tb_autore.out.off
sort -k 1.1,1.10 -o input/tb_autore.out.off.srt input/tb_autore.out.off

echo sort input/tb_classe.out.off
sort -k 1.1,1.36 -o input/tb_classe.out.off.srt input/tb_classe.out.off

echo sort input/tb_impronta.out.off
sort -k 1.1,1.34 -o input/tb_impronta.out.off.srt input/tb_impronta.out.off

echo sort input/tb_luogo.out.off
sort -k 1.1,1.34 -o  input/tb_luogo.out.off.srt  input/tb_luogo.out.off

echo sort input/tb_marca.out.off
sort -k 1.1,1.34 -o input/tb_marca.out.off.srt input/tb_marca.out.off

echo sort input/tb_soggetto.out.off
sort -k 1.1,1.10 -o input/tb_soggetto.out.off.srt input/tb_soggetto.out.off

echo sort input/tb_titolo.out.off
sort -k 1.1,1.10 -o input/tb_titolo.out.off.srt input/tb_titolo.out.off

echo sort input/tr_aut_aut.out.off
sort -k 1.1,1.34 -o input/tr_aut_aut.out.off.srt input/tr_aut_aut.out.off

echo sort input/tb_grafica.out.off
sort -k 1.1,1.10 -o input/tb_grafica.out.off.srt input/tb_grafica.out.off

echo sort input/tb_cartografia.out.off
sort -k 1.1,1.10 -o input/tb_cartografia.out.off.srt input/tb_cartografia.out.off

echo sort input/tb_musica.out.off
sort -k 1.1,1.10 -o input/tb_musica.out.off.srt input/tb_musica.out.off

echo sort input/tb_rappresent.out.off
sort -k 1.1,1.10 -o input/tb_rappresent.out.off.srt input/tb_rappresent.out.off

echo sort input/tb_composizione.out.off
sort -k 1.1,1.10 -o input/tb_composizione.out.off.srt input/tb_composizione.out.off


#echo sort input/tb_descrittore.out.off
#sort -k 1.1,1.10 -o input/tb_descrittore.out.off.srt input/tb_descrittore.out.off

echo sort input/tbf_biblioteca.out.off
sort -k 1.1,1.6 -o input/tbf_biblioteca.out.off.srt input/tbf_biblioteca.out.off

sort -k 1.1,1.10 -o input/tb_977.out.off.srt input/tb_977.out.off

# 12/02/2015
sort -k 1.1,1.10 -o input/tb_audiovideo.out.off.srt input/tb_audiovideo.out.off

# 19/02/2015
sort -k 1.1,1.10 -o input/tb_disco_sonoro.out.off.srt input/tb_disco_sonoro.out.off

# 09/03/2015
sort -k 1.1,1.10 -o input/tb_titset_1.out.off.srt input/tb_titset_1.out.off

# 03/12/2015
sort -k 1.1,1.10 -o input/tb_termine_thesauro.out.off.srt input/tb_termine_thesauro.out.off

# 19/04/2016
sort -k 1.1,1.10 -o input/tb_titset_2.out.off.srt input/tb_titset_2.out.off

# 12/10/2016
sort -k 1.1,1.20 -o input/tr_bid_altroid.out.off.srt  input/tr_bid_altroid.out.off


echo Step 2a Sort per preparare la creazione delle relazioni
echo =================================================================================================

echo sort ../db_export/tr_tit_sog_bib.out
sort -t% -k5 -o input/tr_tit_sog_bib.out.srt ../db_export/tr_tit_sog_bib.out

echo sort ../db_export/tr_tit_cla.out
sort -k 1,10 -o input/tr_tit_cla.out.srt ../db_export/tr_tit_cla.out

# 03/12/2015
echo sort ../db_export/trs_termini_titoli_biblioteche.out
sort -k 1,10 -o input/trs_termini_titoli_biblioteche.out.srt ../db_export/trs_termini_titoli_biblioteche.out


echo sort ../db_export/tr_aut_aut.out
sort -k 1,10 -o input/tr_aut_aut.out.srt ../db_export/tr_aut_aut.out

echo sort ../db_export/tr_aut_aut.out INVERSA
sort -k 1.14,1.23 -k 1,10 -o input/tr_aut_aut.out.inv.srt ../db_export/tr_aut_aut.out


echo sort ../db_export/tr_tit_luo.out
sort -k 1,23 -o input/tr_tit_luo.out.srt ../db_export/tr_tit_luo.out

echo sort ../db_export/tr_tit_tit.out
sort -k 1,23 -o input/tr_tit_tit.out.srt ../db_export/tr_tit_tit.out

echo sort ../db_export/tr_tit_tit.out INVERSA
sort -k 1.14,1.23 -k 1,10 -o input/tr_tit_tit.out.inv.srt ../db_export/tr_tit_tit.out

# 03/12/2015
echo sort ../db_export/trs_termini_titoli_biblioteche.out
sort -k 1,23 -o input/trs_termini_titoli_biblioteche.out.srt ../db_export/trs_termini_titoli_biblioteche.out




echo Step 3. Crea i file delle relazioni: CreateRelationFile.class
echo =================================================================================================

java it.finsiel.offlineExport.CreateRelationFile input/tr_aut_aut.out.srt input/tr_aut_aut.out.srt.rel 9
java it.finsiel.offlineExport.CreateRelationFile input/tr_aut_aut.out.inv.srt input/tr_aut_aut.out.inv.srt.rel 9
java it.finsiel.offlineExport.CreateRelationFile input/tr_tit_aut.out.srt input/tr_tit_aut.out.srt.rel 16
java it.finsiel.offlineExport.CreateRelationFile input/tr_tit_cla.out.srt input/tr_tit_cla.out.srt.rel 10

# 03/12/2015
java it.finsiel.offlineExport.CreateRelationFile input/trs_termini_titoli_biblioteche.out.srt input/trs_termini_titoli_biblioteche.out.srt.rel 10

java it.finsiel.offlineExport.CreateRelationFile input/tr_tit_luo.out.srt input/tr_tit_luo.out.srt.rel 9
java it.finsiel.offlineExport.CreateRelationFile input/tr_tit_mar.out.srt input/tr_tit_mar.out.srt.rel 8
java it.finsiel.offlineExport.CreateRelationFile input/tr_tit_sog_bib.out.srt input/tr_tit_sog_bib.out.srt.rel 12
java it.finsiel.offlineExport.CreateRelationFile input/tr_tit_tit.out.srt input/tr_tit_tit.out.srt.rel 18
java it.finsiel.offlineExport.CreateRelationFile input/tr_tit_tit.out.inv.srt input/tr_tit_tit.out.inv.srt.rel 18


echo =================================================================================================
echo "Step 4. Crea il file degli offset per le relazioni: CreateOffsetFile.class"
echo =================================================================================================

java it.finsiel.offlineExport.CreateOffsetFile input/tr_tit_luo.out.srt input/tr_tit_luo.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tr_tit_tit.out.srt input/tr_tit_tit.out.srt.off
sort -k 1,20 -o input/tr_tit_tit.out.srt.off.srt input/tr_tit_tit.out.srt.off



java it.finsiel.offlineExport.CreateOffsetFile input/tr_aut_aut.out.srt input/tr_aut_aut.out.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tr_aut_aut.out.inv.srt input/tr_aut_aut.out.inv.srt.off
java it.finsiel.offlineExport.CreateOffsetFile input/tr_aut_aut.out.srt.rel input/tr_aut_aut.out.srt.rel.off

java it.finsiel.offlineExport.CreateOffsetFile input/tr_aut_aut.out.inv.srt.rel input/tr_aut_aut.out.inv.srt.rel.off
java it.finsiel.offlineExport.CreateOffsetFile input/tr_tit_aut.out.srt.rel input/tr_tit_aut.out.srt.rel.off

java it.finsiel.offlineExport.CreateOffsetFile input/tr_tit_cla.out.srt.rel input/tr_tit_cla.out.srt.rel.off

# 03/12/2015
java it.finsiel.offlineExport.CreateOffsetFile input/trs_termini_titoli_biblioteche.out.srt.rel input/trs_termini_titoli_biblioteche.out.srt.rel.off

java it.finsiel.offlineExport.CreateOffsetFile input/tr_tit_luo.out.srt.rel input/tr_tit_luo.out.srt.rel.off
java it.finsiel.offlineExport.CreateOffsetFile input/tr_tit_mar.out.srt.rel input/tr_tit_mar.out.srt.rel.off
java it.finsiel.offlineExport.CreateOffsetFile input/tr_tit_sog_bib.out.srt.rel input/tr_tit_sog_bib.out.srt.rel.off

java it.finsiel.offlineExport.CreateOffsetFile input/tr_tit_tit.out.inv.srt.rel input/tr_tit_tit.out.inv.srt.rel.off
java it.finsiel.offlineExport.CreateOffsetFile input/tr_tit_tit.out.srt.rel input/tr_tit_tit.out.srt.rel.off


echo =================================================================================================
echo "Step 5. Preparazione per creazione delle 950: Select950.class"
echo =================================================================================================
java it.finsiel.offlineExport.Select950 stepInventari ../db_export/tbc_inventario.out input/950inv.out
java it.finsiel.offlineExport.Select950 stepCollocazioni ../db_export/tbc_collocazione.out input/950coll.out
java it.finsiel.offlineExport.Select950 stepEsemplari ../db_export/tbc_esemplare_titolo.out input/950ese.out

echo sort ../db_out_postgres/tbf_biblioteca_in_polo.out
sort  -t% -k1 -o input/tbf_biblioteca_in_polo.out.srt ../db_export/tbf_biblioteca_in_polo.out


echo Ordiniamo a partire dal BID di INVENTARIO
echo -----------------------------------------

echo sort input/950inv.out
sort -t% -k1.1,1.10 -k4.1,4.3 -k2.1,2.9 -o input/950inv.out.srt input/950inv.out

echo Ordiniamo a partire dal BID DI COLLOCAZIONE
echo -----------------------------------------

echo sort input/950coll.out
sort -t% -k1.1,1.10 -k4.1,4.3 -k2.1,2.10 -k3.1,3.9 -k9,4 -o input/950coll.out.srt input/950coll.out

echo Ordiniamo a partire dal BID DI ESEMPLARE
echo -----------------------------------------

echo sort input/950ese.out
sort -t% -k1.1,1.10 -k2.1,2.9 -k3.1,3.3 -k5 -o input/950ese.out.srt input/950ese.out

echo Crea il file degli offset: CreateOffsetFile.class [.bid.off e .kloc.off]
echo ------------------------------------------------------------------------
java it.finsiel.offlineExport.CreateOffsetFile input/950inv.out.srt  input/950inv.out.srt.bid.off
java it.finsiel.offlineExport.CreateOffsetFile input/950coll.out.srt input/950coll.out.srt.bid.off input/950coll.out.srt.kloc.off
java it.finsiel.offlineExport.CreateOffsetFile input/950ese.out.srt  input/950ese.out.srt.bid.off

echo Ordina il file degli offset per keyloc

echo sort input/950coll.out.srt.kloc.off
sort -t% -k1.1,1.9 -o input/950coll.out.srt.kloc.off.srt input/950coll.out.srt.kloc.off


#echo ===========================================================================================
#echo " Crea indici per varianti di soggetto"
#echo ===========================================================================================
#./makeIndici.soggettti_varianti.sh



#echo ===========================================================================================
#echo "Step 6. Creazione dei file di offset binary \(per velocizzare export. Evitare di convertire da ascii a long o long long gli offset runtime \)"
#echo ===========================================================================================
#	ascii2binaryOffset filenameOffset_In filenameOffset_Out keyLength addressType (long | longlong)
#
#./makeBinaryOffsets.sh


echo =================
echo FINE ELABORAZIONE
echo =================


echo `${ora}` "Fine processo di makeIndici.sh" >> makeIndici.proc

