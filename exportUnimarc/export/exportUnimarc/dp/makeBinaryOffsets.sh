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
echo `${ora}` "Inizio processo di makeBinaryOffsets.sh" > makeBinaryOffsets.proc


echo ===================
echo INIZIO ELABORAZIONE
echo ===================




#echo 6. Creazione dei file di offset binary (per velocizzare export. Evitare di convertire da ascii a long o long long gli offset runtime )
#	ascii2binaryOffset filenameOffset_In filenameOffset_Out keyLength addressType (long | longlong)

./ascii2binaryOffset input/950coll.out.srt.bid.off					input/boff/950coll.out.srt.bid.off					10 long
./ascii2binaryOffset input/950coll.out.srt.kloc.off.srt				input/boff/950coll.out.srt.kloc.off.srt				 9 long
./ascii2binaryOffset input/950coll.out.srt.kloc.off.srt				input/boff/950coll.out.srt.kloc.off.srt				10 long
./ascii2binaryOffset input/950ese.out.srt.bid.off					input/boff/950ese.out.srt.bid.off					10 long
./ascii2binaryOffset input/950inv.out.srt.bid.off					input/boff/950inv.out.srt.bid.off					10 long
./ascii2binaryOffset input/tb_977.out.off							input/boff/tb_977.out.off							10 long
./ascii2binaryOffset input/tba_ordini.out.swp.srt.off				input/boff/tba_ordini.out.swp.srt.off				10 long
./ascii2binaryOffset input/tbc_possessore_provenienza.out.srt.off	input/boff/tbc_possessore_provenienza.out.srt.off	10 long
./ascii2binaryOffset input/tbc_sezione_collocazione.out.srt.off		input/boff/tbc_sezione_collocazione.out.srt.off		16 long
./ascii2binaryOffset input/tbf_biblioteca.out.off.srt				input/boff/tbf_biblioteca.out.off.srt				 6 long
./ascii2binaryOffset input/tb_autore.out.off.srt					input/boff/tb_autore.out.off.srt					10 long
./ascii2binaryOffset input/tb_cartografia.out.off.srt				input/boff/tb_cartografia.out.off.srt				10 long
./ascii2binaryOffset input/tb_classe.out.off.srt					input/boff/tb_classe.out.off.srt					36 long
./ascii2binaryOffset input/tbc_nota_inv.out.srt.off					input/boff/tbc_nota_inv.out.srt.off					16 long
./ascii2binaryOffset input/tb_composizione.out.off.srt				input/boff/tb_composizione.out.off.srt				10 long
./ascii2binaryOffset input/tb_grafica.out.off.srt					input/boff/tb_grafica.out.off.srt					10 long
./ascii2binaryOffset input/tb_impronta.out.off.srt					input/boff/tb_impronta.out.off.srt					10 long
./ascii2binaryOffset input/tb_incipit.out.srt.off					input/boff/tb_incipit.out.srt.off					10 long
./ascii2binaryOffset input/tb_luogo.out.off.srt						input/boff/tb_luogo.out.off.srt						10 long
./ascii2binaryOffset input/tb_marca.out.off.srt						input/boff/tb_marca.out.off.srt						10 long
./ascii2binaryOffset input/tb_musica.out.off.srt					input/boff/tb_musica.out.off.srt					10 long
./ascii2binaryOffset input/tb_nota.out.srt.off						input/boff/tb_nota.out.srt.off						10 long
./ascii2binaryOffset input/tb_numero_std.out.srt.off				input/boff/tb_numero_std.out.srt.off				10 long
./ascii2binaryOffset input/tb_parola.out.srt.off					input/boff/tb_parola.out.srt.off					10 long
./ascii2binaryOffset input/tb_personaggio.out.srt.off				input/boff/tb_personaggio.out.srt.off				10 long
./ascii2binaryOffset input/tb_rappresent.out.off.srt				input/boff/tb_rappresent.out.off.srt				10 long
./ascii2binaryOffset input/tb_repertorio.out.srt.off				input/boff/tb_repertorio.out.srt.off				10 long
./ascii2binaryOffset input/tb_soggetto.out.off.srt					input/boff/tb_soggetto.out.off.srt					10 long
# longlong per indice. long per polo?
./ascii2binaryOffset input/tb_titolo.out.off.srt					input/boff/tb_titolo.out.off.srt					10 longlong
./ascii2binaryOffset input/tr_aut_aut.out.inv.srt.off				input/boff/tr_aut_aut.out.inv.srt.off				10 long
./ascii2binaryOffset input/tr_aut_aut.out.srt.rel.off				input/boff/tr_aut_aut.out.srt.rel.off				10 long
./ascii2binaryOffset input/trc_poss_prov_inventari.out.srt.off		input/boff/trc_poss_prov_inventari.out.srt.off		16 long
./ascii2binaryOffset input/tr_per_int.out.srt.off					input/boff/tr_per_int.out.srt.off					10 long
./ascii2binaryOffset input/tr_rep_mar.out.srt.off					input/boff/tr_rep_mar.out.srt.off					10 long
./ascii2binaryOffset input/tr_tit_aut.out.srt.off					input/boff/tr_tit_aut.out.srt.off					20 longlong
./ascii2binaryOffset input/tr_tit_aut.out.srt.rel.off				input/boff/tr_tit_aut.out.srt.rel.off				10 long
# longlong per indice. long per polo?
./ascii2binaryOffset input/tr_tit_bib.out.srt.off					input/boff/tr_tit_bib.out.srt.off					10 longlong
./ascii2binaryOffset input/tr_tit_cla.out.srt.rel.off				input/boff/tr_tit_cla.out.srt.rel.off				10 long
./ascii2binaryOffset input/tr_tit_luo.out.srt.rel.off				input/boff/tr_tit_luo.out.srt.rel.off				10 long
./ascii2binaryOffset input/tr_tit_mar.out.srt.off					input/boff/tr_tit_mar.out.srt.off					20 long
./ascii2binaryOffset input/tr_tit_mar.out.srt.rel.off				input/boff/tr_tit_mar.out.srt.rel.off				10 long
./ascii2binaryOffset input/tr_tit_sog_bib.out.srt.rel.off			input/boff/tr_tit_sog_bib.out.srt.rel.off			10 long
./ascii2binaryOffset input/tr_tit_tit.out.inv.srt.rel.off			input/boff/tr_tit_tit.out.inv.srt.rel.off			10 long
./ascii2binaryOffset input/tr_tit_tit.out.srt.off.srt				input/boff/tr_tit_tit.out.srt.off.srt				20 long
./ascii2binaryOffset input/tr_tit_tit.out.srt.rel.off				input/boff/tr_tit_tit.out.srt.rel.off				10 long
./ascii2binaryOffset input/ts_link_multim.out.srt.off				input/boff/ts_link_multim.out.srt.off               10 long


./ascii2binaryOffset input/tb_audiovideo.out.off.srt				input/boff/tb_audiovideo.out.off.srt				10 long
./ascii2binaryOffset input/tb_disco_sonoro.out.off.srt				input/boff/tb_disco_sonoro.out.off.srt				10 long

./ascii2binaryOffset input/tb_titset_1.out.off.srt					input/boff/tb_titset_1.out.off.srt					10 longlong

# 03/12/2015
./ascii2binaryOffset input/tb_termine_thesauro.out.off.srt			input/boff/tb_termine_thesauro.out.off.srt 10 long
./ascii2binaryOffset input/trs_termini_titoli_biblioteche.out.srt.rel.off		input/boff/trs_termini_titoli_biblioteche.out.srt.rel.off 10 long

# 19/04/2016
./ascii2binaryOffset input/tb_titset_2.out.off.srt					input/boff/tb_titset_2.out.off.srt					10 longlong

# 28/06/2016
./ascii2binaryOffset  input/tb_descrittore.out.off.srt 				input/boff/tb_descrittore.out.off.srt				10 long
./ascii2binaryOffset  input/tr_sog_des.out.srt.off 					input/boff/tr_sog_des.out.srt.off					10 long
./ascii2binaryOffset  input/tr_des_des.out.srt.off 					input/boff/tr_des_des.out.srt.off					10 long

# 13/10/2014
./ascii2binaryOffset input/tr_bid_altroid.out.off.srt				input/boff/tr_bid_altroid.out.off.srt				10 long



#define CLASSE_KEY_LENGTH	36 // 02/02/2015
#define KEYLOC_KEY_LENGTH	9
#define TR_KEY_LENGTH	20
#define BIBLIOTECA_KEY_LENGTH 6
#define OFFSET_LENGTH 11
#define INVENTARIO_KEY_LENGTH	16
#define TBC_SEZIONE_COLLOCAZIONE_KEY_LENGTH 16
#define LF_LENGTH 1
#define POLO_KEY_LENGTH	3





echo =================
echo FINE ELABORAZIONE
echo =================


echo `${ora}` "Fine processo di makeBinaryOffsets.sh" >> makeBinaryOffsets.proc

