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
package it.iccu.sbn.ejb.utils.blocchi;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.utils.jms.JMSBlocchiMessagePump;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoInterceptor;
import it.iccu.sbn.ejb.vo.common.DescrittoreBloccoVO;
import it.iccu.sbn.ejb.vo.common.IdListaMetaInfoVO;
import it.iccu.sbn.util.IdGenerator;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

@SuppressWarnings("rawtypes")
public final class DescrittoreBlocchiUtil {

	private static class InterceptorReference extends SoftReference<DescrittoreBloccoInterceptor> {

		private final String idLista;

		public InterceptorReference(String idLista, DescrittoreBloccoInterceptor referent,
				ReferenceQueue<DescrittoreBloccoInterceptor> queue) {
			super(referent, queue);
			this.idLista = idLista;

		}

		public String getIdLista() {
			return idLista;
		}

	}

	private static Logger log = Logger.getLogger(DescrittoreBlocchiUtil.class);
	private static long  idListaCount = 0;

	private static final ReferenceQueue<DescrittoreBloccoInterceptor> garbaged = new ReferenceQueue<DescrittoreBloccoInterceptor>();

	private static final Map<String, InterceptorReference> interceptors = new ConcurrentHashMap<String, InterceptorReference>();

	private static final synchronized String creaIdLista() {
		long id  = IdGenerator.getId() + idListaCount++;
		return Long.toHexString(id).toUpperCase();
	}

	private static final void addInterceptor(String idLista, DescrittoreBloccoInterceptor interceptor) {
		InterceptorReference ref = new InterceptorReference(idLista, interceptor, garbaged);
		interceptors.put(idLista, ref);
	}

	private static final DescrittoreBloccoInterceptor getInterceptor(String idLista) {
		SoftReference<DescrittoreBloccoInterceptor> ref = interceptors.get(idLista);
		if (ref == null)
			return null;

		DescrittoreBloccoInterceptor interceptor = ref.get();
		if (interceptor == null) {
			interceptors.remove(idLista);
			return null;
		}

		return interceptor;
	}

	private static void callInterceptor(DescrittoreBloccoVO blocco) throws Exception {
		if (blocco != null && blocco.getIdLista() != null) {
			DescrittoreBloccoInterceptor interceptor = getInterceptor(blocco.getIdLista());
			if (interceptor != null) {
				log.debug("call interceptor per ricerca id "
						+ blocco.getIdLista() + " ("
						+ interceptor.getClass().getSimpleName() + "@"
						+ Integer.toHexString(interceptor.hashCode()) + ")");
				interceptor.intercept(blocco);
			}
		}
	}

	private static List<DescrittoreBloccoVO> generaDescrittoriBlocco(List list,
			int elemBlocco, String idLista, int ultimoBlocco, DescrittoreBloccoInterceptor interceptor) {

		if (list == null) return null;
		if (elemBlocco < 1)
			elemBlocco = Integer.MAX_VALUE;

		List<DescrittoreBloccoVO> blocchi = new ArrayList<DescrittoreBloccoVO>();
		int size = list.size();
		if (size < 1) {
			blocchi.add(new DescrittoreBloccoVO(null, 1, 0, 0, 0, 0, 0, null));
			return blocchi;
		}

		int numBlocchi = (int) (Math.ceil((double) size / (double) elemBlocco));
		if (ValidazioneDati.strIsNull(idLista))
			idLista = creaIdLista();

		//almaviva5_20110630 #4521
		if (interceptor != null) {
			addInterceptor(idLista, interceptor);
			log.info("Creato interceptor per ricerca id " + idLista + " (class: "
					+ interceptor.getClass().getCanonicalName() + ")");
		}

		for (int i = 0; i < numBlocchi; i++) {
			int start = (elemBlocco * i);
			int count = (size - start);
			if (count > elemBlocco)
				count = elemBlocco;

			List segmento = list;
			try {
				if (numBlocchi > 1)
					segmento = new ArrayList(list.subList(start, start + count));

				blocchi.add(new DescrittoreBloccoVO(idLista, ultimoBlocco + i + 1,
						size, start, count, numBlocchi, elemBlocco, segmento));

			} catch (IndexOutOfBoundsException e) {
				return null;
			}

		}
		if (idLista != null)
			log.info("Creato nuovo descrittore per ricerca id " + idLista + " ("
				+ numBlocchi + " blocchi / sintetica n. " + idListaCount + ")");

		return blocchi;
	}

	public static DescrittoreBloccoVO saveBlocchi(String ticket, List sintetica,
			int elemBlocco, DescrittoreBloccoInterceptor interceptor) {

		List<DescrittoreBloccoVO> blocchi = generaDescrittoriBlocco(sintetica, elemBlocco, null, 0, interceptor);
		if (!ValidazioneDati.isFilled(blocchi) )
			return null;

		try {
			JMSBlocchiMessagePump pump = JMSBlocchiMessagePump.getInstance();
			if (blocchi.size() > 1)
				pump.pump(ticket, blocchi);

			IdListaMetaInfoVO meta = new IdListaMetaInfoVO(ValidazioneDati.last(blocchi));
			pump.sendMetaInfo(ticket, meta);

			//almaviva5_20110630 #4521
			DescrittoreBloccoVO blocco1 = blocchi.get(0);
			callInterceptor(blocco1);

			return blocco1;
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}


	public static DescrittoreBloccoVO appendBlocchi(String ticket, List sintetica,
			int elemBlocco, String idLista, int ultimoBlocco, DescrittoreBloccoInterceptor interceptor) {

		List<DescrittoreBloccoVO> blocchi = generaDescrittoriBlocco(sintetica, elemBlocco, idLista, ultimoBlocco, interceptor);
		if (blocchi == null)
			return null;

		try {
			JMSBlocchiMessagePump msgPump = JMSBlocchiMessagePump.getInstance();
			msgPump.pump(ticket, blocchi);

			//almaviva5_20110630 #4521
			DescrittoreBloccoVO blocco1 = blocchi.get(0);
			callInterceptor(blocco1);

			return blocco1;

		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	public static DescrittoreBloccoVO nextBlocco(String ticket, String idLista,
			int numBlocco) throws EJBException, ValidationException {

		if (ValidazioneDati.strIsNull(ticket) || ValidazioneDati.strIsNull(idLista) || numBlocco < 2)
			throw new ValidationException("Parametri invalidi");

		DescrittoreBloccoVO blocco;
		try {
			blocco = JMSBlocchiMessagePump.getInstance().receive(ticket, idLista, numBlocco);
			if (blocco == null)
				return null;

			//almaviva5_20110630 #4521
			callInterceptor(blocco);

		} catch (Exception e) {
			throw new EJBException(e);
		}

		log.info("Recuperato blocco " + numBlocco + " per ricerca id " + idLista);
		return blocco;
	}

	public static DescrittoreBloccoVO browseBlocco(String ticket, String idLista,
			int numBlocco) throws EJBException, ValidationException {

		if (ValidazioneDati.strIsNull(ticket) || ValidazioneDati.strIsNull(idLista) || numBlocco < 1)
			return null;

		DescrittoreBloccoVO blocco;
		try {
			blocco = JMSBlocchiMessagePump.getInstance().browse(ticket, idLista, numBlocco);
			if (blocco == null)
				return null;

			//almaviva5_20110630 #4521
			callInterceptor(blocco);

		} catch (Exception e) {
			throw new EJBException(e);
		}

		log.info("Recuperato blocco " + numBlocco + " per ricerca id " + idLista);
		return blocco;
	}

	public static void clearBlocchiIdLista(String ticket, String idLista)
			throws EJBException, ValidationException {

		if ( ValidazioneDati.strIsNull(ticket) || ValidazioneDati.strIsNull(idLista) )
			throw new ValidationException("Parametri invalidi");

		try {
			JMSBlocchiMessagePump pump = JMSBlocchiMessagePump.getInstance();
			pump.clearIdLista(ticket, idLista);
			pump.receiveMetaInfo(ticket, idLista);
			//almaviva5_20110630 #4521
			interceptors.remove(idLista);
		} catch (Exception e) {
			throw new EJBException(e);
		}
		log.info("Pulizia blocchi per ricerca id " + idLista);
		return;
	}

	public static void clearBlocchiAll(String ticket) throws EJBException, ValidationException {
		if (ValidazioneDati.strIsNull(ticket) )
			throw new ValidationException("Parametri invalidi");
		try {
			JMSBlocchiMessagePump.getInstance().clearAll(ticket);

			// almaviva5_20110712 check reference scartate
			InterceptorReference ref;
			while ( (ref = (InterceptorReference) garbaged.poll()) != null) {
				log.debug("interceptor reference garbaged (idLista " + ref.getIdLista() + ")");
				interceptors.remove(ref.getIdLista());
			}

		} catch (Exception e) {
			throw new EJBException(e);
		}
		log.info("Pulizia blocchi per ticket " + ticket);
		return;
	}

}
