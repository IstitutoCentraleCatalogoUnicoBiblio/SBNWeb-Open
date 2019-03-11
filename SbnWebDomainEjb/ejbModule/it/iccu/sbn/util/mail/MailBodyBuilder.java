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
package it.iccu.sbn.util.mail;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.ejb.vo.servizi.sale.PrenotazionePostoVO;
import it.iccu.sbn.ejb.vo.servizi.serviziweb.UtenteWeb;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;

public class MailBodyBuilder extends SerializableVO {

	private static final long serialVersionUID = -8743178747439237043L;

	private static StringBuilder aggiungiNotaNoReply(StringBuilder body) {
		body.append("\n\n").append("Attenzione: Questo è un messaggio generato automaticamente.").append("\n");
		body.append("Si prega pertanto di non rispondere alla casella mittente.");
		return body;
	}

	public static class GestioneBibliografica {

		public static String segnalazionePresenzaGestionali(String bid, String segnalazione) {
			StringBuilder body = new StringBuilder();
			body.append("Spett.le signore/a").append("\n\n");
			body.append("Si informa che a seguito della cancellazione sul Sistema centrale della notizia ").append("\n");
			body.append("con identificativo " + bid + ", in fase di cancellazione in allineamento della notizia stessa").append("\n");
			body.append("sulla Base dati del Vostro Polo è risultato presente " + segnalazione  + " che dovrà quindi essere spostato/rimosso.").append("\n\n");
			body.append("Grazie.");

			body = aggiungiNotaNoReply(body);

			return body.toString();
		}

	}

	public static class Servizi {

		public static String prenotazioneDocRestituito(MovimentoListaVO p) throws Exception {

			StringBuilder body = new StringBuilder();
			body.append("Spett.le signore/a").append("\n");
			body.append(p.getCognomeNome()).append("\n\n");
			body.append("Si comunica l'avvenuta restituzione del documento:").append("\n\n");
			body.append(p.getTitolo()).append("\n\n"); //<area del titolo e dell'indicazione di responsabilità; area della pubblicazione>

			String volume = p.getNumVolume();
			if (ValidazioneDati.isFilled(volume) )
				body.append("Volume: ").append(volume).append("\t");
			String anno = p.getAnnoPeriodico();
			if (ValidazioneDati.isFilled(anno) )
				body.append("Anno: ").append(anno);
			if (ValidazioneDati.isFilled(volume) || ValidazioneDati.isFilled(anno) )
				body.append("\n");

			body.append("Inventario: ").append(p.getDatiInventario())
				.append("\t").append("Collocazione: ").append(p.getSegnatura())
				.append("\n\n");

			body.append("Da Lei prenotato in data ").append(p.getDataRichiestaString());

			//almaviva5_20151012 gestione priorità prenotazioni
			if (p.getFlTipoRec() == RichiestaRecordType.FLAG_RICHIESTA) {
				body.append("\n").append("Il documento resterà a sua disposizione fino al ").append(p.getDataInizioPrevNoOraString());
			}

			body = aggiungiNotaNoReply(body);

			//seguito da <vol.> e <anno> (presi dalla richiesta); <inventario> e <collocazione>. Il prestito è scaduto il ... "
			return body.toString();
		}

		public static String nuovaRichiesta(MovimentoListaVO mov) throws Exception {

			StringBuilder body = new StringBuilder();
			body.append("Si notifica l'inserimento della richiesta di servizio n. ").append(mov.getIdRichiesta()).append("\n\n");
			body.append("per l'utente: ").append(mov.getCognomeNome()).append("\n");
			body.append("tesserino: ").append(mov.getCodUte()).append("\n");
			body.append("servizio: ").append(mov.getTipoServizio()).append("\n\n");
			body.append("relativa al documento:").append("\n");
			body.append("Titolo: ").append(mov.getTitolo()).append("\n"); //<area del titolo e dell'indicazione di responsabilità; area della pubblicazione>

			String volume = mov.getNumVolume();
			if (ValidazioneDati.isFilled(volume) )
				body.append("Volume: ").append(volume).append("\t");
			String anno = mov.getAnnoPeriodico();
			if (ValidazioneDati.isFilled(anno) )
				body.append("Anno: ").append(anno);
			if (ValidazioneDati.isFilled(volume) || ValidazioneDati.isFilled(anno) )
				body.append("\n");

			body.append("Inventario: ").append(mov.getDatiInventario())
				.append("\t").append("Collocazione: ").append(mov.getSegnatura());

			body = aggiungiNotaNoReply(body);

			return body.toString();
		}


		public static String rifiutoRichiesta(MovimentoListaVO mov) {
			StringBuilder body = new StringBuilder();
			body.append("Spett.le signore/a").append("\n");
			body.append(mov.getCognomeNome()).append("\n\n");
			body.append("Si comunica che il servizio relativo al documento:").append("\n\n");
			body.append(mov.getTitolo()).append("\n\n"); //<area del titolo e dell'indicazione di responsabilità; area della pubblicazione>

			String volume = mov.getNumVolume();
			if (ValidazioneDati.isFilled(volume) )
				body.append("Volume: ").append(volume).append("\t");
			String anno = mov.getAnnoPeriodico();
			if (ValidazioneDati.isFilled(anno) )
				body.append("Anno: ").append(anno);
			if (ValidazioneDati.isFilled(volume) || ValidazioneDati.isFilled(anno) )
				body.append("\n");

			body.append("Inventario: ").append(mov.getDatiInventario())
				.append("\t").append("Collocazione: ").append(mov.getSegnatura())
				.append("\n\n");

			body.append("Da Lei richiesto in data ").append(mov.getDataRichiestaString());

			body.append("\n").append("non può essere erogato.");
			body.append("\n").append("Per ulteriori informazioni rivolgersi alla biblioteca.");

			body = aggiungiNotaNoReply(body);

			return body.toString();
		}

		public static String nuovaPrenotazionePosto(PrenotazionePostoVO pp) {

			StringBuilder body = new StringBuilder();
			body.append("Si notifica l'inserimento della prenotazione posto n. ").append(pp.getId_prenotazione()).append("\n\n");
			body.append("per l'utente: ").append(pp.getUtente().getCognomeNome()).append("\n");
			body.append("tesserino: ").append(pp.getUtente().getCodUtente()).append("\n");
			body.append("sala: ").append(pp.getPosto().getSala().getDescrizione()).append("\n\n");
			body.append("relativa al giorno: ").append(DateUtil.formattaData(pp.getTs_inizio())).append("\n");
			body.append("dalle: ").append(DateUtil.formattaOrario(pp.getTs_inizio())).append(" alle: ")
					.append(DateUtil.formattaOrario(pp.getTs_fine())).append("\n");

			body = aggiungiNotaNoReply(body);

			return body.toString();
		}

		public static String rifiutoPrenotazionePosto(PrenotazionePostoVO pp) {

			StringBuilder body = new StringBuilder();
			body.append("Si notifica il rifiuto della prenotazione posto n. ").append(pp.getId_prenotazione()).append("\n\n");
			body.append("sala: ").append(pp.getPosto().getSala().getDescrizione()).append("\n\n");
			body.append("relativa al giorno: ").append(DateUtil.formattaData(pp.getTs_inizio())).append("\n");
			body.append("dalle: ").append(DateUtil.formattaOrario(pp.getTs_inizio())).append(" alle: ")
					.append(DateUtil.formattaOrario(pp.getTs_fine())).append("\n");

			body = aggiungiNotaNoReply(body);

			return body.toString();
		}

	}

	public static class ServiziWeb {

		public static String passwordTemporanea(UtenteWeb uteWeb) {

			StringBuilder body = new StringBuilder();
			body.append("Codice utente: ").append(uteWeb.getUserId()).append('\n');
		    body.append("Password temporanea: '").append(uteWeb.getPassword()).append("'.");

			body = aggiungiNotaNoReply(body);

			return body.toString();
		}

		public static String autoregistrazioneUtente(UtenteWeb uteWeb) {

			StringBuilder body = new StringBuilder();
			body.append("Sei stato registrato alla biblioteca prescelta con:").append("\n\n");
			body.append("Codice utente: ").append(uteWeb.getUserId()).append('\n');
		    body.append("Password temporanea: '").append(uteWeb.getPassword()).append("'.");

			body = aggiungiNotaNoReply(body);

			return body.toString();
		}
	}

}
