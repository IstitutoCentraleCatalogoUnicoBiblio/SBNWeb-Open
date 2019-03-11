/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.iccu.sbn.ejb.exception;

public class ValidationExceptionCodici {
	public static int successo = 2;
	public static int errore = 1;
	public static int validazione = 0;


	/**
	 * Errori Gestione Bibliografica dal codice 1000 al codice 1999
	 */
	public static int erroreGenerico = 1000;
	public static int noCanPrim = 1001;
	public static int incongNumStandard = 1002;
	public static int soloUnCanPrim = 1003;
	public static int noTitolo = 1004;
	public static int bidErrato = 1005;
	public static int livRicObblig = 1006;
	public static int livRicLocObblig = 1007;
	public static int selObblNaturaD = 1008;
	public static int formDataInv = 1009;
	public static int data1AMagData1Da = 1010;
	public static int data2AMagData2Da = 1011;
	public static int insObblDataDa1 = 1012;
	public static int data1DaMagOggi = 1013;
	public static int antDataDa1Mag1830 = 1014;
	public static int data2DaMagOggi = 1015;
	public static int antDataDa2Mag1830 = 1016;
	public static int insObblDataDa2 = 1017;
	public static int obblNomeColl = 1018;
	public static int natIncongTipRec = 1019;
	public static int selObblOggSint = 1020;
	public static int selObblLisEsamina = 1021;
	public static int noSelFunz = 1022;
	public static int soloUnaFunz = 1023;
	public static int vidErrato = 1024;
	public static int isadnErrato = 1025;
	public static int dataNasDaMagOggi = 1026;
	public static int insObblDataDaNas = 1027;
	public static int dataNasAMagOggi = 1028;
	public static int dataNasAMagDataNasDa = 1029;
	public static int dataMorDaMagOggi = 1030;
	public static int insObblDataDaMor = 1031;
	public static int dataMorAMagOggi = 1032;
	public static int dataMorAMagDataMorDa = 1033;
	public static int midErrato = 1034;


	/**
	 * Errori Servizi dal codice 2000 al codice 2999
	 */
	public static int seviziErrato = 2000;
	public static int sevizividErrato = 2001;

	/**
	 * Errori DocumentoFisico dal codice 3000 al codice 3999
	 */
	public static int noCodSezione = 3000;
	public static int pidErrato = 3001;
	/**
	 * Errori Acquisizioni dal codice 4000 al codice 4999
	 */
	public static int  erroreGenericoAcquisizioni = 4000;
	public static int  assenzaRisultati = 4001;
	public static int  cambierroreCampoTassoObbligatorio = 4002;
	public static int  cambierroreCampoValutaObbligatorio = 4003;
	public static int  cambierroreCampoTassoNumerico = 4004;
	public static int  cambierroreCampoValutaAlfabetico = 4005;
	public static int cambierroreInserimentoEsistenzaRecord = 4006;
	public static int cambierroreModificaRecordNonUnivoco = 4007;
	public static int cambierroreCampoValutaEccedente = 4008;
	public static int cambierroreCampoDescrizioneValutaEccedente = 4009;
	public static int cambierroreCampoDescrizioneValutaAlfabetico = 4010;
	public static int cambierroreInserimentoTasso = 4011;
	public static int erroreData=4012;
	public static int ordinierroreCampoEsercizioNumerico=4013;
	public static int ordinierroreCampoEsercizioEccedente=4014;
	public static int ordinierroreCampoCapitoloNumerico=4015;
	public static int ordinierroreCampoCapitoloEccedente=4016;
	public static int ordinierroreCampoNumeroNumerico=4017;
	public static int ordinierroreCampoCodFornitoreNumerico=4018;
	public static int ordinierroreCodSezioneAlfabetico=4019;
	public static int ordinierroreCodSezioneEccedente=4020;
	public static int ordinierroreCodBiblAlfabetico=4021;
	public static int ordinierroreCodBiblEccedente=4022;
	public static int ordinierroreCampoDataOrdineObbligatorio=4023;
	public static int ordinierroreCampoTipoOrdineObbligatorio=4024;
	public static int ordinierroreCampoTipoOrdineAlfabetico=4025;
	public static int ordinierroreCampoTipoOrdineEccedente=4026;
	public static int ordinierroreCampoBilancioEsercizioObbligatorio=4027;
	public static int ordinierroreCampoBilancioCapitoloObbligatorio=4028;
	public static int ordinierroreCampoBilancioImpegnoObbligatorio=4029;
	public static int ordinierroreCampoSezioneObbligatorio=4030;
	public static int ordinierroreCampoSezioneAlfabetico=4031;
	public static int ordinierroreCampoSezioneEccedente=4032;
	public static int ordinierroreCampoBidObbligatorio=4033;
	public static int ordinierroreCampoBidCodiceAlfabetico=4034;
	public static int ordinierroreCampoBidCodiceEccedente=4035;
	public static int ordinierroreCampoBidDescrizioneAlfabetico=4036;
	public static int ordinierroreCampoBidDescrizioneEccedente=4037;
	public static int ordinierroreCampoFornitoreObbligatorio=4038;
	public static int ordinierroreCampoFornitoreCodiceNumerico=4039;
	public static int ordinierroreCampoFornitoreCodiceEccedente=4040;
	public static int ordinierroreCampoFornitoreDescrizioneAlfabetico=4041;
	public static int ordinierroreCampoFornitoreDescrizioneEccedente=4042;
	public static int ordinierroreCampoDataFineAbbonObbligatorio=4043;
	public static int ordinierroreCampoNumFascicoloAbbonObbligatorio=4044;
	public static int ordinierroreCampoDataPubblFascicoloAbbonObbligatorio=4045;
	public static int erroreDataFineAbbonamento=4046;
	public static int erroreDataPubblicazioneFascicolo=4047;
	public static int ordineIncongruenzaTipoFornTipoOrd=4048;
	public static int ordineIncongruenzaNaturaTitNaturaOrd=4049;
	public static int ordineIncongruenzaBilancioInesistente=4050;
	public static int ordineIncongruenzaSezioneInesistente=4051;
	public static int ordineIncongruenzaTitoloInesistente=4052;
	public static int ordineIncongruenzaFornitoreInesistente=4053;
	public static int ordineerroreDecimalPoint=4054;
	public static int ordineerrorePrezzoNumerico=4055;
	public static int ordineerroreCancellaAperto=4056;
	public static int fornitoreInserimentoImpossibile=4057;
	public static int fornitoreInserimentoErroreBaseDati=4058;
	public static int ordinierroreCampoNomeFornObbligatorio=4059;
	public static int ordinierroreCampoNomeFornAlfabetico=4060;
	public static int ordinierroreCampoNomeFornEccedente=4061;
	public static int ordinierroreCampoTipoPartnerFornObbligatorio=4062;
	public static int ordinierroreCampoTipoPartnerFornEccedente=4063;
	public static int ordinierroreCampoPaeseFornObbligatorio=4064;
	public static int ordinierroreCampoPaeseFornEccedente=4065;
	public static int ordinierroreCampoProvFornObbligatorio=4066;
	public static int ordinierroreCampoProvFornEccedente=4067;
	public static int ordinierroreCampoUOFornEccedente=4068;
	public static int ordinierroreCampoIndFornEccedente=4069;
	public static int ordinierroreCampoCPostFornEccedente=4070;
	public static int ordinierroreCampoCittaFornEccedente=4071;
	public static int ordinierroreCampoTelFornEccedente=4072;
	public static int ordinierroreCampoFaxFornEccedente=4073;
	public static int ordinierroreCampoCFiscFornEccedente=4074;
	public static int ordinierroreCampoPIvaFornEccedente=4075;
	public static int ordinierroreCampoEMailFornEccedente=4076;
	public static int ordinierroreCampoNoteFornEccedente=4077;
	public static int ordinierroreCampoTipoPagFornEccedente=4078;
	public static int ordinierroreCampoCodCliFornEccedente=4079;
	public static int ordinierroreCampoNomContFornEccedente=4080;
	public static int ordinierroreCampTelContFornEccedente=4081;
	public static int ordinierroreCampFaxContFornEccedente=4082;
	public static int ordinierroreCampValutaContFornEccedente=4083;
	public static int ordineerroreCodFornNumerico=4084;
	public static int ordineerroreCodProfAcqNumerico=4085;
	public static int ordineerroreCancellaNonAnnullato=4086;
	public static int ordinierroreCampoBilancioImpegnoAlfabetico=4087;
	public static int ordinierroreCampoBilancioImpegnoEccedente=4088;
	public static int ordineerroreBilBudgetNumerico=4089;
	public static int ordineerroreBilDispCassaNumerico=4090;
	public static int ordineerroreBilDispCompetenzaNumerico=4091;
	public static int ordineerroreBilFatturatoNumerico=4092;
	public static int ordineerroreBilImpegnatoNumerico=4093;
	public static int ordineerroreBilImpFattNumerico=4094;
	public static int ordineerroreBilPagatoNumerico=4095;
	public static int ordinierroreCampoCodBiblObbligatorio=4096;
	public static int ordineerroreBilBudgetObbligatorio=4097;
	public static int bilancioerroreCodiceImpegnoPresente=4098;
	public static int bilancioRigheCodiceImpegnoEccedenti=4099;
	public static int bilancioTotincongruenza=4100;
	public static int sezioneerroreCodSezioneAlfabetico=4101;
	public static int sezioneerroreCodSezioneEccedente=4102;
	public static int sezioneerroreCampoDescrSezioneAlfabetico=4103;
	public static int sezioneerroreCampoDescrSezioneEccedente=4104;
	public static int sezioneerroreCampoCodSezioneObbligatorio=4105;
	public static int sezioneerroreCampoDescrSezioneObbligatorio=4106;
	public static int sezioneerroreCampoAnnoValiditaObbligatorio=4107;
	public static int sezioneerroreCampoSommaDispNumerico=4108;
	public static int sezioneerroreCampoNoteSezioneAlfabetico=4109;
	public static int sezioneerroreCampoNoteSezioneEccedente=4110;
	public static int sezioneerroreCampoBudgetSezioneNumerico=4111;
	public static int sezioneerroreCampoAnnoValiditaSezioneNumerico=4112;
	public static int sezioneerroreCampoAnnoValiditaSezioneEccedente=4113;
	public static int ordinierroreCampoBilancioIncongruente=4114;
	public static int ordinierroreCampoFornitoreIncongruente=4115;
	public static int buonoOdineRigheOrdineAssenti=4116;
	public static int ordinierroreOrdineBuonoNOTApertooStampato=4117;
	public static int ordinierroreBuonoOrdineStampato=4118;
	public static int ordineIncongruenzaDescrTitTitOrd=4119;
	public static int ordineNONtrovato=4120;
	public static int sezioneerroreCampoAnnoOrdineObbligatorio=4121;
	public static int sezioneerroreCampoAnnoOrdineEccedente=4122;
	public static int sezioneerroreCampoAnnoOrdineNumerico=4123;
	public static int sezioneerroreCampoCodiceOrdineObbligatorio=4124;
	public static int sezioneerroreCampoCodiceOrdineNumerico=4125;
	public static int ordinierroreOrdineRipetuto=4126;
	public static int fatturaerroreCampoBilancioIncongruente=4127;
	public static int fatturaerroreCampoFornitoreIncongruente=4128;
	public static int fatturaRigheAssenti=4129;
	public static int fatturaerroreOrdineNOTApertooStampato=4130;
	public static int fatturaerroreNumFatturaAlfabetico=4131;
	public static int fatturaerroreNumFatturaEccedente=4132;
	public static int fatturaerroreStatoFatturaAlfabetico=4133;
	public static int fatturaerroreStatoFatturaEccedente=4134;
	public static int fatturaerroreCampoProgrNumerico=4135;
	public static int fatturaerroreTipoFatturaAlfabetico=4136;
	public static int fatturaerroreTipoFatturaEccedente=4137;
	public static int fatturaerroreStatoFatturaNumerico=4138;
	public static int fatturaerroreNumFatturaNumerico=4139;
	public static int erroreCampoAnnoNumerico=4140;
	public static int erroreCampoAnnoNumericoEccedente=4141;
	public static int fatturaerroreimportoNumerico=4142;
	public static int fatturaerrorescontoNumerico=4143;
	public static int fatturaerroreimportorigaNumerico=4144;
	public static int fatturaTotincongruenza=4145;
	public static int fatturaerroreCampoAnnoFatturaObbligatorio=4146;
	public static int fatturaerroreCampoDataRegObbligatorio=4147;
	public static int fatturaerroreCampoTipoObbligatorio=4148;
	public static int fatturaerroreDataFattObbligatorio=4149;
	public static int fatturaerroreNumFattObbligatorio=4150;
	public static int fatturaerroreImportoFattObbligatorio=4151;
	public static int fatturaerroreScontoFattObbligatorio=4152;
	public static int fatturaerroreValutaObbligatorio=4153;
	public static int fatturaerroreStatoObbligatorio=4154;
	public static int fatturaerroreImportoRigaFattObbligatorio=4155;
	public static int fatturaerroreRigaBilancioEsercizioObbligatorio=4156;
	public static int fatturaerroreRigaBilancioCapitoloObbligatorio=4157;
	public static int fatturaerroreRigaBilancioTipoImpObbligatorio=4158;
	public static int fatturaerroreRigaOrdineTipoObbligatorio=4159;
	public static int fatturaerroreRigaOrdineAnnoObbligatorio=4160;
	public static int fatturaerroreRigaOrdineCodiceObbligatorio=4161;
	public static int ordinierroreCampoDescProfiloAlfabetico=4162;
	public static int ordinierroreCampoDescProfiloEccedente=4163;
	public static int ordinierroreCampoDescProfiloObbligatorio=4164;
	public static int ordinierroreCampPaeseAlfabetico=4165;
	public static int profilierroreFornitoreRipetuto=4166;
	public static int comunicazioneerroreTipoDocAlfabetico=4167;
	public static int comunicazioneerroreTipoDocValoreNonAmmesso=4168;
	public static int comunicazioneerroreTipoDocEccedente=4169;
	public static int comunicazioneerroreDirComAlfabetico=4170;
	public static int comunicazioneerroreDirComEccedente=4171;
	public static int comunicazioneerroreTipoMsgAlfabetico=4172;
	public static int comunicazioneerroreTipoMsgEccedente=4173;
	public static int comunicazioneerroreCodMsgNumerico=4174;
	public static int fatturaerroreCampoChiaveNonNullo=4175;
	public static int comunicazioneerroreStatoMsgAlfabetico=4176;
	public static int comunicazioneerroreStatoMsgEccedente=4177;
	public static int comunicazioneerroreTipoInvioAlfabetico=4178;
	public static int comunicazioneerroreTipoInvioEccedente=4179;
	public static int comunicazionierroreCampoDataObbligatorio=4180;
	public static int comunicazionierroreCampoTipoDocObbligatorio=4181;
	public static int comunicazionierroreCampoDirComObbligatorio=4182;
	public static int comunicazionierroreCampoTipoMsgObbligatorio=4183;
	public static int comunicazionierroreDatiOrdineObbligatorio=4184;
	public static int comunicazionierroreDatiFatturaObbligatorio=4185;
	public static int comunicazioneerroreStatoMsgNumerico=4186;
	public static int comunicazionierroreIncongruenzaTipoMessaggio=4187;
	public static int suggerimentoerroreCodSuggNumerico=4188;
	public static int suggerimentoerroreCodBibliotecarioNumerico=4189;
	public static int suggerimentoerroreCodBibliotecarioEccedente=4190;
	public static int suggerimentoerroreBibliotecarioDescrizioneAlfabetico=4191;
	public static int suggerimentoerroreBibliotecarioDescrizioneEccedente=4192;
	public static int suggerimentoerroreStatoSuggAlfabetico=4193;
	public static int suggerimentoerroreStatoSuggEccedente=4194;
	public static int suggerimentoerroreStatoSuggObbligatorio=4195;
	public static int suggerimentoerroreSezioneObbligatorio=4196;
	public static int suggerimentoerroreBibliotecarioObbligatorio=4197;
	public static int suggerimentoerroreTitoloObbligatorio=4198;
	public static int documentoerroreCodDocNumerico=4199;
	public static int documentoerroreStatoSuggDocEccedente=4200;
	public static int documentoerroreStatoSuggDocAlfabetico=4201;
	public static int documentoerroreCodUtenteSecondaParteNumerico=4202;
	public static int documentoerroreCodUtenteSecondaParteEccedente=4203;
	public static int documentoerroreCodUtentePrimaParteAlfabetico=4204;
	public static int documentoerroreCodUtentePrimaParteEccedente=4205;
	public static int documentoerroreCampoAnnoEdizioneNumerico=4206;
	public static int documentoerroreCampoAnnoEdizioneEccedente=4207;
	public static int documentoerroreTitoloDescObbligatorio=4208;
	public static int gareerroreCodRichNumerico=4209;
	public static int gareerroreStatoRichAlfabetico=4210;
	public static int gareerroreStatoRichEccedente=4211;
	public static int gareerroreNumCopieObbligatorio=4212;
	public static int erroreDataRichiestaOfferta=4213;
	public static int erroreDataInvioPartecipanteGara=4214;
	public static int garaerroreCampoDataInvioObbligatorio=4215;
	public static int offertaerroreIDoffAlfabetico=4216;
	public static int offertaerroreIDoffEccedente=4217;
	public static int offertaerroreAutoreAlfabetico=4218;
	public static int offertaerroreAutoreEccedente=4219;
	public static int offertaerroreClassifAlfabetico=4220;
	public static int offertaerroreClassifEccedente=4221;
	public static int ordinierroreTitoloNonInPolo=4222;
	public static int buonierroreCampoNumBuonoObbligatorio=4223;
	public static int ricercaDaRaffinare=4224;
	public static int ordineIncongruenzaValutaInesistente=4225;
	public static int buonierroreCampoNumEsistente=4226;
	public static int importoErrato=4227;
	public static int fatturaNONtrovata=4228;
	public static int valoreNullo=4229;
	public static int gareVincitoreUnico=4230;
	public static int erroreDataInvalida=4231;
	public static int validaInvCodSerieObbligatorio=4232;
	public static int validaInvCodSerieEccedente=4233;
	public static int validaInvCodInventEccedente=4234;
	public static int validaInvCodInventNonNumerico=4235;
	public static int duplicazioneFornitore=4236;
	public static int duplicazioneInventario=4237;
	public static int rilegaturaChiusura=4238;
	public static int tipoImpegnoRilegatura=4239;
	public static int noChiusuraInventari=4239;
	public static int inventariNonRientrati=4240;
	public static int fatturaPagataContabilizzata=4241;
	public static int erroreCancellaSez=4242;
	public static int erroreCancella=4243;
	public static int erroreCancellaFornitore=4244;
	public static int ordinierroreSforamentoBudgetBilancio=4245;
	public static int erroreCancellaBilancio=4246;
	public static int ordinierroreOrdineBuonoOrdineLegato=4247;
	public static int cambierroreAssenzaValutaRiferimento=4248;
	public static int fatturaPagataDaControllare=4249;
	public static int ordinierroreCodPoloEccedente=4250;
	public static int ordinierroreCodPoloAlfabetico=4251;
	public static int ordinierroreCampoCodPoloObbligatorio=4252;
	public static int erroreCancellaCambio=4253;
	public static int ordinierroreAutomatismoTipoImpegno=4254;
	public static int fatturaContabilizzataDaControllare=4255;
	public static int scartoOrdiniNonStampati=4256;
	public static int operazioneInConcorrenza=4257;
	public static int sezioneerroreCampoNotaeEccedenteParametrizzata=4258;
	public static int cambierrorePresenzaValutaRiferimento=4259;
	public static int ordinierroreCampoDataAbbonOrdineObbligatorio=4260;
	public static int sezioneerroreCampoAnnoAbbonamentoNumerico=4261;
	public static int sezioneerroreCampoAnnoAbbonamentoEccedente=4262;
	public static int naturaNonAmmessa=4264;
	public static int ricercaDaRaffinareTroppi=4265;
	public static int ordinierroreCampoPeriodoValAbbonObbligatorio=4266;
	public static int ordineerroreCancellaStampato=4267;
	public static int rilegaturaInventariAssenti=4268;
	public static int indicaData=4269;
	public static int tipoImpegnoNonRilegatura=4270;
	public static int indicaDataEsercizio=4271;
	public static int fatturaerroreCampoFornitoreIncongruenteNC=4272;
	public static int ricercaDaRaffinareTemp=4273;

	public static int ordinierroreCampoIsbnFornEccedente=4274;
	public static int fornitorierroreCampoIsbnMancante=4275;

}
