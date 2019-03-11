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
package it.iccu.sbn.servizi.sip2;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.domain.amministrazione.AmministrazioneBiblioteca;
import it.iccu.sbn.ejb.domain.documentofisico.Inventario;
import it.iccu.sbn.ejb.domain.servizi.Servizi;
import it.iccu.sbn.ejb.domain.servizi.serviziweb.ServiziWeb;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.CodiciType.CodiciRicercaType;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioDettaglioVO;
import it.iccu.sbn.ejb.vo.documentofisico.InventarioVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.MovimentoVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.RichiestaRecordType;
import it.iccu.sbn.ejb.vo.servizi.erogazione.ServizioBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.SollecitiVO;
import it.iccu.sbn.ejb.vo.servizi.erogazione.TariffeModalitaErogazioneVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.RicercaUtenteBibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.utenti.dettaglio.UtenteBibliotecaVO;
import it.iccu.sbn.exception.UtenteNotFoundException;
import it.iccu.sbn.exception.UtenteNotProfiledException;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.util.AtomicCyclicCounter;
import it.iccu.sbn.util.MiscString;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.config.ConfigChangeInterceptor;
import it.iccu.sbn.util.config.Configuration;
import it.iccu.sbn.util.rfid.InventarioRFIDParser;
import it.iccu.sbn.util.servizi.ServiziUtil;
import it.iccu.sbn.vo.custom.servizi.MovimentoListaVO;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2.Sip2MessageType;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2BlockPatronRequest;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2CheckinRequest;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2CheckinResponse;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2CheckoutRequest;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2CheckoutResponse;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2EndPatronSessionRequest;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2EndPatronSessionResponse;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2ItemInformationRequest;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2ItemInformationResponse;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2LoginRequest;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2LoginResponse;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2PatronInformationRequest;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2PatronInformationResponse;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2PatronStatusRequest;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2PatronStatusResponse;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2ScStatusRequest;
import it.iccu.sbn.vo.domain.sip2.MessaggioSip2ScStatusResponse;
import it.iccu.sbn.web.constant.RicercaRichiesteType;
import it.iccu.sbn.web.constant.ServiziConstant;
import it.iccu.sbn.web.integration.servizi.erogazione.ControlloAttivitaServizioResult;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO;
import it.iccu.sbn.web.integration.servizi.erogazione.controlloIter.DatiControlloVO.OperatoreType;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;

/**
 * @author almaviva
 *
 */
public class SbnSIP2Listener extends ServiceMBeanSupport implements SbnSIP2ListenerMBean, ConfigChangeInterceptor {

	private static Logger log = Logger.getLogger(SbnSIP2Listener.class);

	private static final AtomicCyclicCounter clientId = new AtomicCyclicCounter();
	private Servizi servizi;
	private ServiziWeb serviziWeb;
	private Inventario inventario;
	private AmministrazioneBiblioteca amministrazioneBib;
	private Server server;

	private static final Map<Long, Client> clients = new ConcurrentHashMap<Long, SbnSIP2Listener.Client>();

	private class Server extends Thread {

		private ServerSocket socket;
		String codPolo = null;
		String codBiblio = null;
		String codTipoServizio = null;
		String codServizio = null;

		public Server(int listenPort) throws IOException {
			super("SbnSIP2Listener@Server");
			socket = new ServerSocket(listenPort);
			socket.setSoTimeout(1000);
		}

		public Server(int listenPort, String codPolo, String codBiblio) throws IOException {
			this(listenPort);
			this.codBiblio = codBiblio;
			this.codPolo = codPolo;
		}

		public Server(int listenPort, String codPolo, String codBiblio, String codTipoServizio) throws IOException {
			this(listenPort, codPolo, codBiblio);
			this.codTipoServizio = codTipoServizio;
		}

		public Server(int listenPort, String codPolo, String codBiblio, String codTipoServizio, String codServizio) throws IOException {
			this(listenPort, codPolo, codBiblio, codTipoServizio);
			this.codServizio = codServizio;
		}

		@Override
		public void run() {
			try {
				while (!isInterrupted())
					try {
						Socket client = socket.accept();
						Client c = new Client(clientId.getNextValue(), client, codPolo, codBiblio, codTipoServizio, codServizio);
						clients.put(c.getId(), c);
						c.start();
					} catch (SocketTimeoutException e) {
						continue;
					}

				socket.close();

			} catch (IOException e) {
				log.error("", e);
			}
		}


		public synchronized void stopAll() {
			this.interrupt();
			Iterator<Client> i = clients.values().iterator();
			while (i.hasNext()) {
				i.next().close();
				i.remove();
			}
		}

	}

	private class Client extends Thread {

		private Socket clientSocket;
		private String usr = "";
		private String pwd = "";
		private String usrEstesa = "";
		private String codPolo = null;
		private String codBiblio = null;
		private String codTipoServizio = null;
		private String codServizio = null;
		private UtenteBibliotecaVO utenteBibliotecaVO = new UtenteBibliotecaVO();
		private char scStatus;
		private final char _SC_STATUS_OK = '0';
		private final char _SC_STATUS_OUT_PAPER = '1';
		private final char _SC_STATUS_SHUT_DOWN = '2';
		private final int _INV_CODPOLO = 0;
		private final int _INV_CODBIB = 1;
		private final int _INV_SERIE = 2;
		private final int _INV_NUM = 3;

		private Logger clog;

		private MessaggioSip2 lastMessage;

		public Client(int id, Socket clientSocket) {
			super("SbnSIP2Listener@Client-" + id );
			this.clientSocket = clientSocket;

			clog = Logger.getLogger(SbnSIP2Listener.class.getCanonicalName() + "@Client-" + id);
			clog.info(this.getName() + " connesso.");
		}

		public Client(int id, Socket clientSocket, String codPolo, String codBiblio) {
			this(id, clientSocket);
			this.codBiblio = codBiblio;
			this.codPolo = codPolo;
		}

		public Client(int id, Socket clientSocket, String codPolo, String codBiblio, String codTipoServizio, String codServizio) {
			this(id, clientSocket, codPolo, codBiblio);
			this.codBiblio = codBiblio;
			this.codPolo = codPolo;
			this.codTipoServizio = codTipoServizio;
			this.codServizio = codServizio;
		}

		/**
		 * @param institutionId COD_POLO|COD_BIB|COD_TOTEM
		 * @throws ApplicationException
		 */
		private void refreshTotemInfo(String institutionId) throws ApplicationException {

			clog.debug("----> institutionId: " + institutionId);

			String totemId[] = MiscString.estraiCampi(institutionId, "#");
			if (totemId.length >= 3) {
				this.codPolo = totemId[0];
				this.codBiblio = totemId[1];
				try {
					String confTotem = CommonConfiguration.getProperty(Configuration.SIP2_TOTEM_ + totemId[2], null);
					if (!ValidazioneDati.isFilled(confTotem))
						throw new ApplicationException(SbnErrorTypes.SRV_SIP2_INVALID_PARAMS);
					String[] confSrv = MiscString.estraiCampi(confTotem, "|");
					this.codTipoServizio = ValidazioneDati.trimOrEmpty(confSrv[0]);
					this.codServizio = ValidazioneDati.trimOrEmpty(confSrv[1]);

					StringBuilder buf = new StringBuilder();
					buf.append("----> polo: ").append(codPolo);
					buf.append("; bib: ").append(codBiblio);
					buf.append("; srv: ").append(this.codTipoServizio);
					buf.append("; diritto: ").append(codServizio);

					clog.debug(buf.toString());

				} catch (ApplicationException e) {
					throw e;
				} catch (Exception e) {
					throw new ApplicationException(SbnErrorTypes.SRV_SIP2_INVALID_PARAMS, e);
				}
			}
		}

		@Override
		public void run() {
			try {
				clog.debug("start message loop");
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
				String line = null;
				MessaggioSip2 response = null;

				// Location code  [required - login]
				// Institution id [required]
				String ticket = null;

				do {
					line = in.readLine();
					if (line != null) {
						clog.debug("input msg: " + line);
						// Gestione richiesta SIP2
						MessaggioSip2 msgSip2 = MessaggioSip2.createRequestMessage(line);
						if (msgSip2 == null) {
							clog.error("[Unhandled SIP2 request] " + line);
							continue;
						}

						clog.debug("message handler: " + msgSip2.getClass().getCanonicalName());

						try {
							switch (msgSip2.getCodiceMessaggio()) {
							case SIP2_SC_PATRON_STATUS_REQUEST: {
								// 23<language><transaction date><institution id><patron identifier><terminal password><patron password>

								// Lettura bar-code tessera biblioteca
								// Invio status del cliente

								MessaggioSip2PatronStatusRequest msg = (MessaggioSip2PatronStatusRequest) msgSip2;
								MessaggioSip2PatronStatusResponse resp = new MessaggioSip2PatronStatusResponse(true, msg);

								refreshTotemInfo(msg.getInstitutionId());

								usr = msg.getPatronIdentifier();
								pwd = msg.getPatronPassword();

								clog.debug("----> patronId: " + usr);

								// check bar-code ??
								// field validPatron (2.00 optional, Y/N)

/*								-- patron status
								14-char, fixed-length field. This field is described in the preliminary NISO standard Z39.70-
								199x. A Y in any position indicates that the condition is true. A blank (code $20) in this position
								means that this condition is not true. For example, the first position of this field corresponds to
								"charge privileges denied" and must therefore contain a code $20 if this patron’s privileges are
								authorized.

								Position Definition (Y: condizione verificata; [blank]: condizione non verificata)
								0 charge privileges denied
								1 renewal privileges denied
								2 recall privileges denied
								3 hold privileges denied
								4 card reported lost
								5 too many items charged
								6 too many items overdue
								7 too many renewals
								8 too many claims of items returned
								9 too many items lost
								10 excessive outstanding fines
								11 excessive outstanding fees
								12 recall overdue
								13 too many items billed
*/

								resp.setPatronStatus("              "); //default (patron status OK)

								try {
//									usrEstesa = servizi.espandiCodUtente(usr);
//									RicercaUtenteBibliotecaVO ricercaUtenteBibliotecaVO = preparaRicercaUtente(usrEstesa);
//									utenteBibliotecaVO = servizi.getDettaglioUtente(ticket, ricercaUtenteBibliotecaVO, null, Locale.getDefault());
									ticket = getUser(null, usr, ticket);
//									codBiblio = ((MessaggioSip2PatronStatusRequest)msgSip2).getInstitutionId();
									resp.setPersonalName(utenteBibliotecaVO.getCognomeNome());

									checkUser();
									// nessuna eccezione: utenza valida (non scaduta)
									resp.setValidPatron("Y");
									resp.setValidPatronPassword("Y");

//									utenteBibliotecaVO.getAutorizzazioni()

//									List<ServizioVO> srv = utenteBibliotecaVO.getAutorizzazioni().getListaServizi();
//									srv.get(0).getAutorizzazione()
//									AutorizzazioneVO autVO = new AutorizzazioneVO();
//									autVO.getCodAutorizzazione()

//									ServizioVO.DELMOD
//									ValidazioneDati.in(servizioVO.getStato(), ... );

//									utenteBibliotecaVO.getAutorizzazioni().getCodTipoAutor()
//									utenteBibliotecaVO.getAutorizzazioni().getAutorizzazione() //String
								} catch (Exception e) {
									resp.addScreenMessage(e.getMessage());
									resp.setPatronStatus("YYYY          ");
									clog.error(e.getMessage(), e);
								}

								response = resp;
								break;
							}

							case SIP2_SC_PATRON_INFORMATION: {
								response = doPatronInfo(ticket, msgSip2);
								break;
							}

							case SIP2_SC_BLOCK_PATRON: {
								// 01<card retained><transaction date><institution id><blocked card msg><patron identifier><terminal password>

								// Patron forget to take library card
								// Blocks patron's card

								MessaggioSip2BlockPatronRequest msg = (MessaggioSip2BlockPatronRequest) msgSip2;
								MessaggioSip2PatronStatusResponse resp = new MessaggioSip2PatronStatusResponse(true, msg);

								refreshTotemInfo(msg.getInstitutionId());

/*								-- patron status
								14-char, fixed-length field. This field is described in the preliminary NISO standard Z39.70-
								199x. A Y in any position indicates that the condition is true. A blank (code $20) in this position
								means that this condition is not true. For example, the first position of this field corresponds to
								"charge privileges denied" and must therefore contain a code $20 if this patron’s privileges are
								authorized.

								Position Definition (Y: condizione verificata; [blank]: condizione non verificata)
								0 charge privileges denied
								1 renewal privileges denied
								2 recall privileges denied
								3 hold privileges denied
								4 card reported lost
								5 too many items charged
								6 too many items overdue
								7 too many renewals
								8 too many claims of items returned
								9 too many items lost
								10 excessive outstanding fines
								11 excessive outstanding fees
								12 recall overdue
								13 too many items billed
*/

								// controlla lo stato del richiedente
								// imposta il patronStatus
								resp.setPatronStatus("YYYY          "); //charge privileges denied

								response = resp;
								break;
							}

							case SIP2_SC_LOGIN: {
								// 93<UID algorithm><PWD algorithm><login user id><login password><location code>

								// Login utente (bibliotecario)
								// Startup

								MessaggioSip2LoginResponse resp;
								try {
									MessaggioSip2LoginRequest msg = (MessaggioSip2LoginRequest) msgSip2;
									resp = new MessaggioSip2LoginResponse(true, msg.getSequenceNumber());

//									refreshTotemInfo(((MessaggioSip2LoginRequest)msgSip2).get);

									// ok = 0 (login failed - default)
									response = resp;
									char loginOk = '0';

//									// Codici polo e biblioteca estrapolati dal LocationCode della richiesta
//									// LocationCode [campo da opzionale a richiesto]
//									codPolo = ((MessaggioSip2LoginRequest)msgSip2).getCodicePolo();
//									codBiblio = ((MessaggioSip2LoginRequest)msgSip2).getCodiceBiblioteca();
//									codServizio = ((MessaggioSip2LoginRequest)msgSip2).getCodiceServizio();
//									ticket = SbnSIP2Ticket.getUtenteSIP2Ticket(codPolo, codBiblio, clientSocket.getInetAddress());

									clog.debug("[Login request] user: "+msg.getLoginUserID()+" / polo: "+codPolo+" / biblio: "+codBiblio);

									// Nome utente
//									usr = ((MessaggioSip2LoginRequest)msgSip2).getLoginUserID();

//									usrEstesa = servizi.espandiCodUtente(usr);
//									RicercaUtenteBibliotecaVO ricercaUtenteBibliotecaVO = preparaRicercaUtente(usrEstesa);
//									utenteBibliotecaVO = servizi.getDettaglioUtente(ticket, ricercaUtenteBibliotecaVO, null, Locale.getDefault());
									ticket = getUser(msg, null, ticket);

//									clog.debug("[Credentials from DB] id: "+utenteBibliotecaVO.getIdUtente()+" / bibUtente: "+utenteBibliotecaVO.getCodiceBiblioteca()+" / nome: "+utenteBibliotecaVO.getNome()+" / password: "+utenteBibliotecaVO.getPassword());

									// Password utente
									pwd = msg.getLoginPassword();
									// pwd criptata
/*									if (!pwd.equals(utenteBibliotecaVO.getPassword())){
										response = resp;
										throw new Exception("Errore Login: password errata");
									}*/

									try {
										checkUser();
										loginOk = '1';
									} catch (Exception e) {
										loginOk = '0';
										clog.error(e.getMessage(), e);
									}

									clog.debug("[Login ok] chksum: "+resp.getChecksum()+" / seqNr: "+resp.getSequenceNumber());

									resp.setOk(loginOk); //login ok
									response = resp;

									clog.debug("[Output String] " + resp.toString());

								} catch (DaoManagerException e) {
									clog.error(e.getMessage());
								} catch (UtenteNotFoundException e) {
									clog.error(e.getMessage());
								} catch (UtenteNotProfiledException e) {
									clog.error(e.getMessage());
								} catch (Exception e) {
									clog.error(e.getMessage());
								}
								break;
							}

							case SIP2_SC_STATUS_MESSAGE: {
								// 99<status code><max print width><protocol version>

								// SC Status
								// Dopo il Login in caso di connessione tramite Socket

//								if (codBiblio==null)
									// login non effettuato

								//default status ok
								MessaggioSip2ScStatusRequest msg = (MessaggioSip2ScStatusRequest) msgSip2;
								MessaggioSip2ScStatusResponse resp = new MessaggioSip2ScStatusResponse(true, codBiblio, msg);

//								refreshTotemInfo(((MessaggioSip2ScStatusRequest)msgSip2).getI);

								// status-code: char(1), required
//								0 SC unit is OK
//								1 SC printer is out of paper
//								2 SC is about to shut down
								scStatus = msg.getStatusCode();

								try {
									// nome della biblioteca
//									resp.setLibraryName("");
									//resp.setLibraryName(amministrazioneBiblioteca.getBiblioteca("SBW", " IC").getNom_biblioteca());
									BibliotecaVO bib = getAmministrazioneBib().getBiblioteca(codPolo, codBiblio);
									resp.setLibraryName(bib != null ? bib.getNom_biblioteca() : "");

								} catch (Exception e) {
									// Messaggio di errore
									resp.setCheckinOk('N');
									resp.setCheckoutOk('N');
									resp.addScreenMessage(e.getMessage());
									clog.error(e.getMessage(), e);
								}

								response = resp;
								break;
							}

							case SIP2_SC_ITEM_INFORMATION: {
								response = doItemInfo(ticket, msgSip2);
								break;
							}

							case SIP2_SC_CHECKOUT: {
								response = doCheckout(ticket, msgSip2);
								break;
							}

							case SIP2_SC_CHECKIN: {
								response = doCheckin(ticket, msgSip2);
								break;
							}

							case SIP2_SC_END_PATRON_SESSION: {
								//35<transaction date><institution id><patron identifier><terminal password><patron password>

								// Fine della sessione

								MessaggioSip2EndPatronSessionRequest msg = (MessaggioSip2EndPatronSessionRequest)msgSip2;
								MessaggioSip2EndPatronSessionResponse resp = new MessaggioSip2EndPatronSessionResponse(true, msg);

								refreshTotemInfo(msg.getInstitutionId());

								response = resp;

								// end session
								codPolo = null;
								codBiblio = null;
								utenteBibliotecaVO = new UtenteBibliotecaVO();
								ticket = null;
								usr = null;
								pwd = null;

								break;
							}

							case SIP2_SC_ACS_RESEND_REQUEST: {
								// 97

								// Richiesta di ri-trasmissione dell'ultima risposta

/*								Request ACS Resend, Message 97
								This message requests the ACS to re-transmit its last message.  It is sent by the SC to the ACS when the
								checksum in a received message does not match the value calculated by the SC.  The ACS should respond
								by re-transmitting its last message.  This message should never include a “sequence number” field, even
								when error detection is enabled, but would include a “checksum” field since checksums are in use.

								Example:
								97<CR>
*/
								response = lastMessage;
								break;
							}

							default:
								clog.error("[Unhandled SIP2 request] " + line);
								continue;
							}

							String outputLine = response.toString();
							// Invio della risposta
							out.print(outputLine + "\r");

							out.flush(); // invia subito

							clog.debug("[Response sent] ---> "+outputLine);

							//ultimo messaggio scambiato (se diverso da resend)
							if (response.getCodiceMessaggio() != Sip2MessageType.SIP2_SC_ACS_RESEND_REQUEST)
								lastMessage = response;

						} catch (Exception e) {
							clog.error("[Exception] " + e.getMessage());
						}

					}

				} while (!isInterrupted() && line != null);

				clog.info(this.getName() + " disconnesso.");

				in.close();
				out.close();
				this.close();

			} catch (IOException e) {
				clog.error("[IOException] " + e.getMessage());
			} catch (Exception e) {
				clog.error("[Exception] " + e.getMessage());
			}
		}


		private MessaggioSip2 doItemInfo(String ticket, MessaggioSip2 msgSip2) throws Exception {
			// 17<transaction date><institution id>< item identifier ><terminal password>

			// Item information

			MessaggioSip2ItemInformationRequest msg = (MessaggioSip2ItemInformationRequest) msgSip2;
			MessaggioSip2ItemInformationResponse resp = new MessaggioSip2ItemInformationResponse(true, msg);

			refreshTotemInfo(msg.getInstitutionId());

			try {
				// id libro (inventario)
				String inventarioItem = msg.getItemIdentifier();
				DatiControlloVO datiControllo = preparaDatiControllo(ticket, inventarioItem, false);

				resp.setTransactionDate(msgSip2.getSIP2Date(null)); //YYYYMMDDZZZZHHMMSS
				resp.setItemIdentifier(inventarioItem);

				// Controllo esistenza inventario documento
				InventarioDettaglioVO dati = checkDocumentoUser(datiControllo, ticket, false);

				// Movimento
				Map listaMovimentiVO =
					getServizi().getListaMovimentiPerErogazione(
							ticket,
							datiControllo.getMovimento(),
							RicercaRichiesteType.RICERCA_PER_INVENTARIO,
							Locale.getDefault());
				List<MovimentoListaVO> listaRichieste = (List<MovimentoListaVO>)listaMovimentiVO.get(ServiziConstant.RICHIESTE);
				//datiControllo.setMovimento(listaRichieste.get(0));
				if (ValidazioneDati.isFilled(listaRichieste) ) {
					// Esiste un movimento associato
					datiControllo.setMovimento(listaRichieste.get(0));
					// 		Cod Attivita del movimento:
					// 		- 03 consegnato al lettore
					// 		- 04 restituito	(stato mov C e stato richiesta H)
//					17:38:04,482 INFO  [SbnSIP2Listener] - cod attivita : 03
//					17:38:04,482 INFO  [SbnSIP2Listener] - cod stato mov: A
//					17:38:04,482 INFO  [SbnSIP2Listener] - cod stato ric: G
//					clog.debug("- cod attivita : " + datiControllo.getMovimento().getCodAttivita());
//					clog.debug("- cod stato mov: " + datiControllo.getMovimento().getCodStatoMov());
//					clog.debug("- cod stato ric: " + datiControllo.getMovimento().getCodStatoRic());

					if ( "03".equals(datiControllo.getMovimento().getCodAttivita().trim()) &&
							(datiControllo.getMovimento().getDataFineEffString()==null) ){
						// data prevista di fine prestito (se presente)
						if ( datiControllo.getMovimento().getDataFinePrev()!=null ){
							//resp.setDueDate(msgSip2.getSIP2Date(datiControllo.getMovimento().getDataFinePrev()));
							resp.setDueDate(datiControllo.getMovimento().getDataFinePrevString());
						}
						resp.setCirculationStatus("04");	// charged
					} else {
						resp.setCirculationStatus("03");	// available
					}

				} else {
					// Non esiste un movimento associato
					resp.setCirculationStatus("03");	// available
				}

				resp.setTitleIdentifier(dati.getTitIsbdTrattato());

//				resp.setRecallDate("");		// data sollecito / optional YYYYMMDDZZZZHHMMSS
//				resp.setHoldPickupDate("");	// data di raccolta / optional YYYYMMDDZZZZHHMMSS

				// Utente non obbligatorio
				resp.setSecurityMarker("02");		// 3M tattle-tape security strip
				resp.setFeeType("01");				// other/unknown

			} catch (Exception ve) {
				// Messaggio di errore
				resp.setCirculationStatus("01");	// other
				resp.addScreenMessage(ve.getMessage());
				if (resp.getTitleIdentifier()==null) resp.setTitleIdentifier("Untitled");
				clog.error(ve.getMessage(), ve);
			}

			return resp;
		}

		private MessaggioSip2 doPatronInfo(String ticket, MessaggioSip2 msgSip2) throws Exception {

			//63<language><transaction date><summary><institution id><patron identifier>
			//<terminal password><patron password><start item><end item>

			// Lettura bar-code tessera biblioteca
			// Invio informazioni sul cliente

			MessaggioSip2PatronInformationRequest msg = (MessaggioSip2PatronInformationRequest) msgSip2;
			MessaggioSip2PatronInformationResponse resp = new MessaggioSip2PatronInformationResponse(true, msg);

			refreshTotemInfo(msg.getInstitutionId());

			usr = msg.getPatronIdentifier();
			pwd = msg.getPatronPassword();

			clog.debug("----> patronId: " + usr);

			resp.setPatronStatus("              "); //default (patron status OK)

			try {
				ticket = getUser(null, usr, ticket);
				checkUser(); // nessuna eccezione: utenza valida (non scaduta)

				DatiControlloVO datiControllo = preparaDatiControllo(ticket, null, true);

//				resp.setCurrencyType("");				// Tipo valuta
//				resp.setFeeAmount("");					// Ammontare / Costo
//				resp.setFeeLimit("");					// Limite Ammontare / Costo

//		    	resp.setHoldItemsCount("0000");			// Nr. oggetti prenotati
//		    	resp.setOverdueItemsCount("0000");		// Nr. prestiti scaduti
//		    	resp.setChargedItemsCount("0000");		// Nr. oggetti a carico (prestati)
//		    	resp.setFineItemsCount("0000");			// Nr. oggetti multati (ritardi)
//		    	resp.setRecallItemsCount("0000");		// Nr. oggetti richiamati (solleciti ricevuti dopo la scadenza)
//		    	resp.setUnavailableHoldsCount("0000");	// Nr. oggetti prenotati ma non disponibili

				resp.setPersonalName(utenteBibliotecaVO.getCognomeNome());
				resp.setValidPatron("Y");
				resp.setValidPatronPassword("Y");

				Map<String, Object> map = getServizi().getListaMovimentiPerErogazione(ticket, datiControllo.getMovimento(), RicercaRichiesteType.RICERCA_PER_UTENTE, Locale.getDefault());
				// nr richieste totali utente
				int nrRichieste = ((List<MovimentoVO>) map.get(ServiziConstant.RICHIESTE)).size();
				// nr solleciti totali utente
				int nrSolleciti = 0;
				List<SollecitiVO> listaSolleciti = new ArrayList<SollecitiVO>();
				List<Long> listaCodRichServ = new ArrayList<Long>();
				Long codRichServTemp;
				for (int i=0; i<((List<SollecitiVO>) map.get(ServiziConstant.SOLLECITI)).size(); i++){
					// elimina duplicati (più solleciti per stessa richiesta)
					codRichServTemp = (((List<SollecitiVO>) map.get(ServiziConstant.SOLLECITI)).get(i)).getCodRichServ();
					if (!listaCodRichServ.contains(codRichServTemp)){
						// non esiste - aggiunge
						listaCodRichServ.add( codRichServTemp );
						listaSolleciti.add( ((List<SollecitiVO>) map.get(ServiziConstant.SOLLECITI)).get(i) );
						nrSolleciti++;
					}
				}
				codRichServTemp = null;
				listaCodRichServ = null;
				// nr richieste scadute totali utente
				Map<String, Object> mapScadute = getServizi().getListaMovimentiPerErogazione(ticket, datiControllo.getMovimento(), RicercaRichiesteType.RICERCA_RICHIESTE_SCADUTE_UTENTE, Locale.getDefault());
				int nrRichiesteScadute = ((List<MovimentoVO>) mapScadute.get(ServiziConstant.RICHIESTE)).size();
				// nr movimenti attivi utente
				int nrPrestiti = getServizi().getNumeroMovimentiAttiviPerUtente(ticket, datiControllo.getMovimento());
				// nr prenotazioni utente
				int nrPrenotazioni = nrRichieste-nrPrestiti;

				clog.debug("nr richieste totali: "+ nrRichieste);
				clog.debug("nr richieste scadute: "+ nrRichiesteScadute);
				clog.debug("nr solleciti: "+ nrSolleciti);
				clog.debug("nr prestiti : "+ nrPrestiti);
		    	clog.debug("nr prenotazioni: "+  nrPrenotazioni);

		    	resp.setHoldItemsCount( ValidazioneDati.fillLeft(String.valueOf(nrPrenotazioni), '0', 4) );
		    	resp.setChargedItemsCount( ValidazioneDati.fillLeft(String.valueOf(nrPrestiti), '0', 4) );
		    	resp.setOverdueItemsCount( ValidazioneDati.fillLeft(String.valueOf(nrRichiesteScadute), '0', 4) );
		    	resp.setRecallItemsCount( ValidazioneDati.fillLeft(String.valueOf(nrSolleciti), '0', 4) );
//		    	resp.setFineItemsCount("0000");			// Nr. oggetti multati (ritardi)
//		    	resp.setUnavailableHoldsCount("0000");	// Nr. oggetti prenotati ma non disponibili

		    	String summary = msg.getSummary();
				if (ValidazioneDati.isFilled(summary)) {
			    	// 10-char fixed-length field
		    		int pos = summary.indexOf("Y");
					if (pos > -1) {
		    			// detailed information requested
/*											0 hold items		- prenotati
						1 overdue items		- scaduti
						2 charged items		- a carico
						3 fine items		- ritardi
						4 recall items		- solleciti
						5 unavailable holds	- prenotati non disponibili
						6 fee items			- nr. massimo
*/
						switch (pos) {
						case 0:	// prenotati
							clog.debug("------------ nr. richieste");
							for (int i = 0; i < nrRichieste; i++) {
								MovimentoVO prenotazione = ((List<MovimentoVO>) map.get(ServiziConstant.RICHIESTE)).get(i);
								String chiaveDoc = getChiaveDocumento(prenotazione);
								resp.addHoldItem(chiaveDoc);
							}
							break;
						case 1:	// scaduti
							clog.debug("------------ nr. scadute");
							for (int i = 0; i < nrRichiesteScadute; i++) {
								MovimentoVO scaduta = ((List<MovimentoVO>) mapScadute.get(ServiziConstant.RICHIESTE)).get(i);
								String chiaveDoc = getChiaveDocumento(scaduta);
								resp.addOverdueItem(chiaveDoc);
							}
							break;
						case 2:	// prestiti
							List<MovimentoVO> listaPrestiti = getServizi().getListaMovimentiAttiviPerUtente(ticket, datiControllo.getMovimento());
							clog.debug("------------ nr. prestiti");
							// nr movimenti attivi utente
							for (int i = 0; i < listaPrestiti.size(); i++) {
								MovimentoVO prestito = listaPrestiti.get(i);
								String chiaveDoc = getChiaveDocumento(prestito);
								resp.addChargedItem(chiaveDoc);
							}
							break;
						case 4:	// solleciti
							clog.debug("------------ nr. solleciti");
							for (int i = 0; i < nrSolleciti; i++) {
//								sollecito = ((List<SollecitiVO>) map.get(ServiziConstant.SOLLECITI)).get(i);
								SollecitiVO sollecito = listaSolleciti.get(i);
								MovimentoVO movimento = sollecito.getMovimento();
								//almaviva5_20170413 fix per codice inventario a null
								String chiaveDoc = getChiaveDocumento(movimento);
								clog.debug("- " + i);
//								clog.debug(sollecito.getProgrSollecito() + " / " + sollecito.getDataInvioString());
//								clog.debug(sollecito.getProgrSollecito());
								clog.debug(sollecito.getCodRichServ());
//								resp.addRecallItem("nr. " + sollecito.getProgrSollecito() + " del " + sollecito.getDataInvioString());
								//resp.addRecallItem("cod. richiesta " + sollecito.getCodRichServ());
								resp.addRecallItem(chiaveDoc);
							}
							break;

						default:
							break;
						}

						// nr richieste totali utente
//						((List<MovimentoVO>) map.get(ServiziConstant.RICHIESTE)).get(0).get
						// solleciti utente
//						((List<SollecitiVO>) map.get(ServiziConstant.SOLLECITI)).get(0).get
						// richieste scadute utente
//						((List<MovimentoVO>) mapScadute.get(ServiziConstant.RICHIESTE)).get(0).get

						// nr movimenti attivi utente
//						int nrPrestiti = servizi.getNumeroMovimentiAttiviPerUtente(ticket, datiControllo.getMovimento());
						// nr prenotazioni utente
//						int nrPrenotazioni = nrRichieste-nrPrestiti;
		    		}
		    	}
				map = null;
				mapScadute = null;

			} catch (Exception e) {
				resp.addScreenMessage(e.getMessage());
				resp.setPatronStatus("YYYY          ");
				resp.setValidPatron("N");
				resp.setValidPatronPassword("N");
				clog.error(e.getMessage(), e);
			}

			return resp;
		}

		private String getChiaveDocumento(MovimentoVO mov) {
			// almaviva5_20170413 fix per codice inventario a null
			if (mov.isRichiestaSuInventario()) {
				String chiaveInventario = ConversioneHibernateVO.toWeb().chiaveInventario(mov.getCodBibInv(),
						mov.getCodSerieInv(), Integer.valueOf(mov.getCodInvenInv()));
				clog.debug(chiaveInventario);
				clog.debug(mov.getBid());
				return chiaveInventario;
			} else {
				//richiesta su doc. non sbn
				String chiaveDoc = mov.getCodBibDocLett() + " " + mov.getTipoDocLett() +
						" " + mov.getCodDocLet() + " " + mov.getProgrEsempDocLet();
				clog.debug(chiaveDoc);
				return chiaveDoc;
			}

		}

		private ServizioBibliotecaVO recuperaServizioUtente(String ticket) throws Exception {
			// id del servizio richiesto
			List<ServizioBibliotecaVO> lstDirittiUtente = getServizi().getServiziAttivi(
					ticket, codPolo, utenteBibliotecaVO.getCodiceBiblioteca(),
					usrEstesa, codBiblio, DaoManager.now(), false);

			ServizioBibliotecaVO servizioVO = null;

			clog.debug("###################################################");
			for (ServizioBibliotecaVO s : lstDirittiUtente) {
				if (ValidazioneDati.equals(codTipoServizio, ValidazioneDati.trimOrEmpty(s.getCodTipoServ()) )) {
					servizioVO = s;
					clog.debug("# id servizio  : " + s.getIdServizio());
					clog.debug("# cod servizio : " + s.getCodServ());
					clog.debug("# cod tipo serv: " + s.getCodTipoServ());
					clog.debug("# tipo servizio: " + CodiciProvider.cercaDescrizioneCodice(s.getCodTipoServ(),
											CodiciType.CODICE_TIPO_SERVIZIO,
											CodiciRicercaType.RICERCA_CODICE_SBN));
					clog.debug("# durata movim : " + s.getDurMov());
					clog.debug("# nr max movim : " + s.getNumMaxMov());
					clog.debug("# nr max rich  : " + s.getNumMaxRich());

					this.codServizio = s.getCodServ();
					break;
				}
			}
			clog.debug("###################################################");
			return servizioVO;
		}

		public void close() {
			this.interrupt();
			try {
				if (clientSocket.isConnected())
					clientSocket.close();
			} catch (IOException e) {
				clog.error("", e);
			} finally {
				clients.remove(this.getId());
			}
		}

		private RicercaUtenteBibliotecaVO preparaRicercaUtente(String codUtenteEsteso){
			RicercaUtenteBibliotecaVO ricerca = new RicercaUtenteBibliotecaVO();
			ricerca.setCodPolo(codPolo);
			ricerca.setCodBib(codBiblio);
			//almaviva5_20120412 segnalazione TO0: fix per cod.utente non standard
			//ricerca.setCodBibUte(" " + codUtenteEsteso.substring(0, 2));
			ricerca.setCodUte(codUtenteEsteso);

			return ricerca;
		}

		private DatiControlloVO preparaDatiControllo(String ticket, String inventario, boolean user) throws Exception {
			MovimentoVO mov = new MovimentoVO();
			mov.setCodPolo(codPolo);
			mov.setCodBibOperante(codBiblio);
			mov.setCodBibDest(codBiblio);
			if (user) {
				mov.setCodBibUte(utenteBibliotecaVO.getCodiceBiblioteca());
				mov.setCodUte(utenteBibliotecaVO.getCodiceUtente());
			}

			mov.setCodServ(codServizio);
			mov.setCodTipoServ(codTipoServizio);

			Timestamp now = DaoManager.now();
			mov.setDataInizioPrev(now);
			mov.setDataRichiesta(now);
			mov.setFlTipoRec(RichiestaRecordType.FLAG_RICHIESTA);
			mov.setFlSvolg("L");

			String firmaUtente = DaoManager.getFirmaUtente(ticket);
			mov.setUteIns(firmaUtente);
			mov.setUteVar(firmaUtente);
			mov.setTsIns(now);
			mov.setFlCanc("N");

			// richiesta su inventario
			if (ValidazioneDati.isFilled(inventario) ) {

				try {
					InventarioVO inv = InventarioRFIDParser.parse(inventario);
					mov.setCodSerieInv(inv.getCodSerie());
					mov.setCodInvenInv(inv.getCodInvent() + "");
					mov.setCodBibInv(ValidazioneDati.rtrim(getInventarioInfo(inventario, _INV_CODBIB)));
				} catch (Exception e) {
					throw new Exception("Numero inventario errato");
				}
			}

			return new DatiControlloVO(ticket, mov, OperatoreType.SIP2, false, null);
		}

		/**
		 * Restituisce le informazioni di cui è composto l'inventario (Item Identifier)
		 *
		 * Struttura di inventario
		 * 		1) 14 caratteri
		 * 			cod bib			2 (0-1)
		 * 			cod serie		3 (2-4)
		 * 			cod inv			9 (5-13)
		 *
		 * 		2) 18 caratteri
		 * 			cod polo		3 (0-2)
		 * 			spazio			1 (3)
		 * 			cod bib			2 (4-5)
		 * 			cod serie		3 (6-8)
		 * 			cod inv			9 (9-17)
		 *
		 * Valori di info:
		 * 		[_INV_CODPOLO]	Codice Polo (se presente)
		 * 		[_INV_CODBIB]	Codice Biblioteca
		 * 		[_INV_SERIE]	Serie inventariale
		 * 		[_INV_NUM]		Numero dell'inventario
		 *
		 * @param inventario string
		 * @param info int (valori ammessi: 0, 1, 2, 3)
		 * @return
		 * @throws ValidationException
		 */
		private String getInventarioInfo(String inventario, int info) throws Exception {
			// Inventario non valido se:
			// 1) is null
			// 2) lunghezza diversa da 14 e 18
			InventarioVO inv = InventarioRFIDParser.parse(inventario);

			switch (info) {
				case _INV_CODPOLO: //cod polo (se presente)
					return ValidazioneDati.coalesce(inv.getCodPolo(), codPolo);

				case _INV_CODBIB: //cod biblio
					return ValidazioneDati.coalesce(inv.getCodBib(), codBiblio);

				case _INV_SERIE: //serie inventariale
					return inv.getCodSerie();

				case _INV_NUM: //numero inventario
					return (inv.getCodInvent() + "");

				default:
					return inventario;
			}
		}

		/**
		 * Controlli su inventario (esistenza) e utente
		 *
		 * @param datiControllo
		 * @param ticket
		 * @param controlliRichiestaSIP2
		 * @return
		 * @throws Exception
		 */
		private InventarioDettaglioVO checkDocumentoUser(DatiControlloVO datiControllo, String ticket, boolean controlliRichiestaSIP2) throws Exception {
			// controllo esistenza inventario / documento
			InventarioDettaglioVO dati = getInventario().getInventarioDettaglio(
					codPolo, codBiblio,
					datiControllo.getMovimento().getCodSerieInv(),
					Integer.parseInt(datiControllo.getMovimento().getCodInvenInv()),
					Locale.getDefault(), ticket);
			// codice categoria di fruizione - obbligatorio
			datiControllo.getMovimento().setCat_fruizione(dati.getCodFrui());

			// periodico: anno obbligatorio
			String annata = datiControllo.getMovimento().getAnnoPeriodico();
			if ( (ValidazioneDati.strIsNull(annata) || !ValidazioneDati.strIsNumeric(annata))
					&& ("S".equalsIgnoreCase(dati.getNatura())) ){
				datiControllo.getMovimento().setNatura(dati.getNatura());
				datiControllo.getMovimento().setAnnoPeriodico(dati.getAnnoAbb());
			}

			// Controlli su utente e documento
			if (controlliRichiestaSIP2) {
				getServizi().eseguiControlliRichiestaSIP2(datiControllo);
				if (!ControlloAttivitaServizioResult.isOK(datiControllo.getResult())) {
					throw new Exception(datiControllo.getResult().toString());
				}
			}
			return dati;
		}

		/**
		 *
		 * @param ticket
		 * @return ticket utente
		 */
		private String getTicket(String ticket) {
			return (ticket == null)	? SbnSIP2Ticket.getUtenteSIP2Ticket(codPolo, codBiblio, clientSocket.getInetAddress()) : ticket;
		}

		/**
		 * Restituisce il ticket utente della sessione di lavoro.
		 *
		 * @param request
		 * @param user
		 * @param ticket
		 * @return ticket utente
		 * @throws Exception
		 */
		private String getUser(MessaggioSip2LoginRequest request, String user, String ticket) throws Exception {
			String newTicket;
			if (request != null) {
				usr = request.getLoginUserID();
				newTicket = SbnSIP2Ticket.getUtenteSIP2Ticket(codPolo, codBiblio, clientSocket.getInetAddress());
			} else {
				usr = (user == null) ? usr : user;
				newTicket = (ticket == null) ? SbnSIP2Ticket.getUtenteSIP2Ticket(codPolo, codBiblio, clientSocket.getInetAddress())	: ticket;
			}
			usrEstesa = ServiziUtil.espandiCodUtente(usr);
			RicercaUtenteBibliotecaVO ricercaUtenteBibliotecaVO = preparaRicercaUtente(usrEstesa);
			utenteBibliotecaVO = getServizi().getDettaglioUtente(ticket, ricercaUtenteBibliotecaVO, null, Locale.getDefault());
			//almaviva5_20160527 utente non trovato per userId, secondo tentativo per cod.fiscale
			if (utenteBibliotecaVO == null) {
				String codFiscale = user.toUpperCase();
				ricercaUtenteBibliotecaVO.setCodFiscale(codFiscale);
				ricercaUtenteBibliotecaVO.setCodUte(null);
				utenteBibliotecaVO = getServizi().getDettaglioUtente(ticket, ricercaUtenteBibliotecaVO, null, Locale.getDefault());
			}

			if (utenteBibliotecaVO == null)
				throw new Exception("Utente non trovato");

			usrEstesa = utenteBibliotecaVO.getCodiceUtente();	//potrebbe contenere il cod.fiscale.

			return newTicket;
		}

		/**
		 * Controllo scadenza delle credenziali
		 *
		 * @throws Exception
		 */
		private void checkUser() throws Exception {
			// Controllo scadenza delle credenziali
			long ts = System.currentTimeMillis();

			Calendar now = Calendar.getInstance();
			now.setTimeInMillis(ts);
			int gg_max_ute = getServiziWeb().getLimMax(codPolo);

			Calendar lastAccess = Calendar.getInstance();
			lastAccess.setTimeInMillis(utenteBibliotecaVO.getLastAccess().getTime());

			if (gg_max_ute == 0)
				lastAccess = now;
			else
				lastAccess.add(Calendar.DAY_OF_MONTH, gg_max_ute);

			if (now.after(lastAccess) ) {
				throw new Exception("Utenza scaduta. Per la riattivazione consultare la biblioteca.");
			}
		}

		private MessaggioSip2 doCheckin(String ticket, MessaggioSip2 msgSip2) throws Exception {

			// 09<no block><transaction date><return date><current location><institution id>
			//<item identifier><terminal password><item properties><cancel>

			// Restituzione

			MessaggioSip2CheckinRequest msg = (MessaggioSip2CheckinRequest)msgSip2;
			MessaggioSip2CheckinResponse resp =	new MessaggioSip2CheckinResponse(true, msg);

			refreshTotemInfo(msg.getInstitutionId());

			try {
				ticket = getTicket(ticket);

				// id libro (inventario)
				String inventarioItem = msg.getItemIdentifier();
				DatiControlloVO datiControllo = preparaDatiControllo(ticket, inventarioItem, false);

				Map listaMovimentiVO =
					getServizi().getListaMovimentiPerErogazione(
							ticket,
							datiControllo.getMovimento(),
							RicercaRichiesteType.RICERCA_PER_INVENTARIO,
							Locale.getDefault());
				List<MovimentoListaVO> listaRichieste = (List<MovimentoListaVO>)listaMovimentiVO.get(ServiziConstant.RICHIESTE);
				if (!ValidazioneDati.isFilled(listaRichieste) )
					throw new Exception("Movimento non trovato");
				datiControllo.setMovimento(ValidazioneDati.first(listaRichieste));

				ServizioBibliotecaVO servizioVO = getServizi().getServizioBiblioteca(ticket, codPolo, codBiblio, codTipoServizio, codServizio);
				datiControllo.setServizio(servizioVO);
				datiControllo.getMovimento().setCodAttivita("04"); //restituzione documento
				datiControllo.getMovimento().setDataFineEff(DaoManager.now()); //restituzione documento

				getServizi().aggiornaIterRichiestaSIP2(datiControllo);
				if (!ControlloAttivitaServizioResult.isOK(datiControllo.getResult())){
					throw new Exception(datiControllo.getResult().toString());
				}

				//almaviva5_20120308 invio mail a utenti prenotati dopo riconsegna
				invioMailUtentiPrenotati(ticket, datiControllo.getMovimento());

				resp.setPatronIdentifier(datiControllo.getMovimento().getCodUte());
				InventarioDettaglioVO dati = checkDocumentoUser(datiControllo, ticket, false);
//				datiControllo.getDataSospensione()
//				datiControllo.getGgSospensione()
//				datiControllo.getGgRitardo()
//				datiControllo.getDataRedisp()
				resp.setTitleIdentifier(dati.getTitIsbdTrattato());

				resp.setOk('1');
				resp.setResensitize('Y');

			} catch (Exception ve) {
				// Messaggio di errore
				resp.addScreenMessage(ve.getMessage());
				resp.setResensitize('N');
				resp.setTitleIdentifier("Untitled");
				clog.error(ve.getMessage(), ve);
			}

			return resp;
		}

		private MessaggioSip2 doCheckout(String ticket, MessaggioSip2 msgSip2) throws Exception {

			// 11<SC renewal policy><no block><transaction date><nb due date><institution id>
			//<patron identifier><item identifier><terminal password><patron password><item properties>
			//<fee acknowledged><cancel>

			// Richiesta di prestito

			//default not ok
			MessaggioSip2CheckoutRequest msg = (MessaggioSip2CheckoutRequest)msgSip2;
			MessaggioSip2CheckoutResponse resp = new MessaggioSip2CheckoutResponse(true, msg);

			refreshTotemInfo(msg.getInstitutionId());

			try {
				// id libro (inventario)
				String inventarioItem = msg.getItemIdentifier();
				clog.debug("----> itemId: " + inventarioItem);

				String patronId = msg.getPatronIdentifier();
				clog.debug("----> patronId: " + patronId);

				ticket = getUser(null, patronId, ticket);
				checkUser(); // nessuna eccezione: utenza valida (non scaduta)

				ServizioBibliotecaVO servizioVO = recuperaServizioUtente(ticket);
				if (servizioVO == null)
					throw new Exception("Utente non abilitato al servizio");

				// almaviva5_20120307 calcolo cod mod erogazione
				List<TariffeModalitaErogazioneVO> tme = getServizi()
						.getTariffaModalitaErogazione(ticket, codPolo, codBiblio, servizioVO.getCodTipoServ(), null);
				if (ValidazioneDati.size(tme) != 1)
					throw new Exception("Errore configurazione modalità erogazione");

				String codModErog = tme.get(0).getCodErog();
				clog.debug("# mod. erog.   : "+ codModErog);

				if (!ValidazioneDati.isFilled(codModErog))
					throw new Exception("Errore configurazione modalità erogazione");

				DatiControlloVO datiControllo = preparaDatiControllo(ticket, inventarioItem, true);
				datiControllo.setServizio(servizioVO);
				MovimentoVO mov = datiControllo.getMovimento();
				mov.setCodTipoServ(servizioVO.getCodTipoServ());
				mov.setDataFinePrev(ServiziUtil.calcolaDataFinePrevista(servizioVO, mov.getDataInizioPrev()));
				mov.setCod_erog(codModErog);

				// Controllo esistenza inventario documento
				InventarioDettaglioVO dati = checkDocumentoUser(datiControllo, ticket, true);

				resp.setTitleIdentifier(dati.getTitIsbdTrattato());

				// prepara la richiesta
				getServizi().aggiornaIterRichiestaSIP2(datiControllo);
				if (!ControlloAttivitaServizioResult.isOK(datiControllo.getResult())){
					throw new Exception(datiControllo.getResult().toString());
				}

				Map listaMovimentiVO =
					getServizi().getListaMovimentiPerErogazione(
							ticket,
							datiControllo.getMovimento(),
							RicercaRichiesteType.RICERCA_PER_INVENTARIO,
							Locale.getDefault());
				List<MovimentoListaVO> listaRichieste = (List<MovimentoListaVO>)listaMovimentiVO.get(ServiziConstant.RICHIESTE);

				datiControllo.setMovimento(listaRichieste.get(0));
				datiControllo.getMovimento().setCodAttivita("03"); //consegna documento al lettore
				datiControllo.getMovimento().setDataInizioEff(DaoManager.now());
				getServizi().aggiornaIterRichiestaSIP2(datiControllo);

				clog.debug("--> data inizio effettiva: " + datiControllo.getMovimento().getDataInizioEffString());
				clog.debug("--> data fine prevista   : " + datiControllo.getMovimento().getDataFinePrevString());

				if (!ControlloAttivitaServizioResult.isOK(datiControllo.getResult())){
					throw new Exception(datiControllo.getResult().toString());
				}

				resp.setTransactionDate(msgSip2.getSIP2Date(datiControllo.getMovimento().getDataInizioEff()));
				//resp.setDueDate(msgSip2.getSIP2Date(datiControllo.getMovimento().getDataFinePrev()));
				resp.setDueDate(datiControllo.getMovimento().getDataFinePrevString());
				resp.setOk('1');				// ok
				resp.setDesensitize('Y');		//
				resp.setSecurityInhibit("Y");	//
				resp.setRenewalOk('N');			// primo prestito

			} catch (Exception ve) {
				resp.setSecurityInhibit("N");	//
				resp.setRenewalOk('N');			// primo prestito
				// Messaggio di errore
				resp.addScreenMessage(ve.getMessage());
				clog.error(ve.getMessage(), ve);
			}

			return resp;
		}


	} //end class Client

	public SbnSIP2Listener() {
		log.info("=== SbnSIP2Listener.Constructor()");
	}

	public void startService() throws Exception {
		log.info("=== SbnSIP2Listener.startService()");
		CommonConfiguration.addInterceptor(this, (String[])null);
		startSIP2Daemon();
	}

	private void bind() throws Exception {
		String codPolo = CommonConfiguration.getProperty(Configuration.SIP2_CURR_POLE, null);
		String codBibl = CommonConfiguration.getProperty(Configuration.SIP2_CURR_BIBLIO, null);
		String codTipoSrv = CommonConfiguration.getProperty(Configuration.SIP2_CURR_COD_TIPOSERVIZIO, "PS");	//default
		String codSrv = CommonConfiguration.getProperty(Configuration.SIP2_CURR_COD_SERVIZIO, "GEN");			//default
		//almaviva5_20120307 segnalazione TO0: reso parametrico il valore host
		String host = CommonConfiguration.getProperty(Configuration.SIP2_BASE_URL);
		int listenPort = CommonConfiguration.getPropertyAsInteger(Configuration.SIP2_BIND_PORT, 7892);

		if (!ValidazioneDati.isFilled(codPolo) || !ValidazioneDati.isFilled(codBibl)
				|| !ValidazioneDati.isFilled(codTipoSrv) || !ValidazioneDati.isFilled(codSrv)
				|| !ValidazioneDati.isFilled(host) ) {
			//throw new ApplicationException(SbnErrorTypes.SRV_SIP2_INVALID_PARAMS);
			log.warn("Configurazione SIP2 incompleta. Impossibile avviare il servizio.");
			return;
		}

		server = new Server(listenPort, codPolo, ValidazioneDati.fillLeft(codBibl, ' ', 3), codTipoSrv, codSrv );
		server.start();

		log.info("totem codice polo default: " + codPolo);
		log.info("totem codice biblioteca default: " + codBibl);
		log.info("totem codice tipo servizio default: " + codTipoSrv);
		log.info("totem codice diritto default: " + codSrv);
		log.info("totem base host: " + host);
		log.info("bind su porta TCP: " + listenPort);
	}

	public void stopService() throws Exception {
		log.info("=== SbnSIP2Listener.stopService()");
		this.stopSIP2Daemon();
	}

	private void invioMailUtentiPrenotati(String ticket, MovimentoVO mov) {

		try {
			List<MovimentoListaVO> prenotazioni = getServizi()
					.getPrenotazioni(ticket, mov, mov.getCodBibDest(), Locale.getDefault(), null);

			for (MovimentoListaVO p : prenotazioni)
				getServizi().inviaMailUtentePrenotazione(ticket, p);
		} catch (Exception e) {
			log.error("", e);
		}

	}

	public void onConfigPropertyChange(String key) throws Exception {
		return;
	}

	public void onConfigReload(Set<String> changedProperties) throws Exception {
		stopSIP2Daemon();
		Thread.yield();
		startSIP2Daemon();

	}

	public void startSIP2Daemon() throws Exception {
		bind();
	}

	public void stopSIP2Daemon() throws Exception {
		if (server != null)
			server.stopAll();
	}

	public static void main(String[] args) {
		return;
	}

	public Servizi getServizi() throws Exception {
		if (servizi != null)
			return servizi;

		this.servizi = DomainEJBFactory.getInstance().getServizi();

		return servizi;
	}

	public ServiziWeb getServiziWeb() throws Exception {
		if (serviziWeb != null)
			return serviziWeb;

		this.serviziWeb = DomainEJBFactory.getInstance().getServiziWeb();

		return serviziWeb;
	}

	public Inventario getInventario() throws Exception {
		if (inventario != null)
			return inventario;

		this.inventario = DomainEJBFactory.getInstance().getInventario();
		return inventario;
	}

	public AmministrazioneBiblioteca getAmministrazioneBib() throws Exception {
		if (amministrazioneBib != null)
			return amministrazioneBib;

		this.amministrazioneBib = DomainEJBFactory.getInstance().getBiblioteca();

		return amministrazioneBib;
	}


}
