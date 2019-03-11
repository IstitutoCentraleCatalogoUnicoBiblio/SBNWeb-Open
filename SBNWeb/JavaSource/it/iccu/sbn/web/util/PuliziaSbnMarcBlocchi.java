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
package it.iccu.sbn.web.util;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.util.config.CommonConfiguration;
import it.iccu.sbn.util.jms.ConstantsJMS;
import it.iccu.sbn.util.jms.JMSUtil;
import it.iccu.sbn.util.jndi.JNDIUtil;
import it.iccu.sbn.web2.action.SinteticaLookupDispatchAction;
import it.iccu.sbn.web2.navigation.Navigation;

import java.util.Set;

import javax.jms.Queue;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public final class PuliziaSbnMarcBlocchi {

	private static Logger log = Logger.getLogger(PuliziaSbnMarcBlocchi.class);

	public static void removeSbnMarcIdLista(Navigation navi) {

		Set<String> idListaSet = (Set<String>) navi.getAttribute(SinteticaLookupDispatchAction.SBNMARC_IDLISTA);
		if (!ValidazioneDati.isFilled(idListaSet) )
			return;

		try {
			InitialContext ctx = JNDIUtil.getContext();
			JMSUtil jms = new JMSUtil(ctx);
			Queue queue = (Queue) ctx.lookup(CommonConfiguration.getProperty("QUEUE_NAME_SBNMARC"));
			String ticket = navi.getUserTicket();
			for (String idLista : idListaSet) {
				String selector = ConstantsJMS.ID_BLOCCO + " LIKE '" + idLista + "_%'";
				log.info(ticket + " -> Eliminazione blocchi SbnMarc per idLista " + idLista);
				jms.discardQueue(queue, selector);
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

}
