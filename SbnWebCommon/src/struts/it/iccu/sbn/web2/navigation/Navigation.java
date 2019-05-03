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
package it.iccu.sbn.web2.navigation;

import gnu.trove.THashMap;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.util.IdGenerator;
import it.iccu.sbn.web.vo.MarcaImageCache;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.NavigationForward.DirectionType;
import it.iccu.sbn.web2.tags.NavigationErrorsTag;
import it.iccu.sbn.web2.util.Constants;
import it.iccu.sbn.web2.util.LinkableTagUtils;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.config.ForwardConfig;

public class Navigation extends SerializableVO {

	private static final long serialVersionUID = -7830502518969563978L;

	private static final String NAVIGATION_INSTANCE = "NAVIGATION_INSTANCE";
	private static Logger log = Logger.getLogger(Navigation.class);

	public static final String NAVIGATIONBAR_PARAM = "navigation";
	public static final String NAVIGATIONBAR_BACK_PARAM = "navigation_back";
	public static final String NAVIGATIONBAR_SUBMIT = "nav.btn";
	public static final String NAVIGATION_PREPROCESSOR = "nav.pre.processor.class";

	public static final String ACTION_WITH_FORM = "nav.action.without.form";

	protected static final String NAVIGATION_CURRENT_ELEMENT = "nav.curr.element";
	protected static final String NULL = "nav.null";

	private NavigationCache cache;
	private Map<String, NavigationElement> bookmarks;
	private Map<String, NavigationAttribute> attributes;
	private MarcaImageCache marcaImageCache;

	private AtomicBoolean locked;
	private AtomicBoolean isNew;

	private NavigationHook hook;

	//attributi non serializzati
	private transient HttpServletRequest currentRequest;
	private transient HttpSession session;

	private NavigationElement lockingElement;

	private final int navigationId;



	public class NavigationAttribute extends SerializableVO {

		private static final long serialVersionUID = -2212063832057550954L;
		private final NavigationElement element;
		private final Serializable value;

		public NavigationAttribute(NavigationElement e, Serializable value) {
			this.element = e;
			this.value = value;
		}

		public NavigationElement getElement() {
			return element;
		}

		public Serializable getValue() {
			return value;
		}

	}

	/**
	  * Recupera dalla request l'istanza corrente della navigazione
	  * @param request request http corrente
	  * @return Istanza corrente della navigazione
	 */
	public static final Navigation getInstance(HttpServletRequest request) {

		if (request == null)
			return null;
		HttpSession session = request.getSession();
		if (session == null)
			return null;

		Navigation instance = (Navigation) session.getAttribute(NAVIGATION_INSTANCE);
		if (instance == null) {
			instance = new Navigation(session);
			session.setAttribute(NAVIGATION_INSTANCE, instance);
		}

		instance.setCurrentRequest(request);
		return instance;
	}
	/**
	  * Recupera dalla sessione l'istanza corrente della navigazione
	  * @param request request http corrente
	  * @return Istanza corrente della navigazione
	 */
	public static final Navigation getInstance(HttpSession session) {

		if (session == null)
			return null;

		Navigation instance = (Navigation) session.getAttribute(NAVIGATION_INSTANCE);
		if (instance == null) {
			instance = new Navigation(session);
			session.setAttribute(NAVIGATION_INSTANCE, instance);
		}

		instance.setCurrentRequest(null);
		return instance;
	}

	protected Navigation(HttpSession session) {
		this.cache = new NavigationCache(this);
		this.bookmarks = new THashMap<String, NavigationElement>();
		this.attributes = new THashMap<String, NavigationAttribute>();
		this.session = session;
		this.locked = new AtomicBoolean(false);
		this.isNew = new AtomicBoolean(true);

		this.marcaImageCache = null;
		this.navigationId = IdGenerator.getId();
		log.info(String.format("Creata istanza id %d per sessione %s", getNavigationId(), session.getId()) );
	}

	private void setCurrentRequest(HttpServletRequest request) {
		this.currentRequest = request;
	}

	protected void removeFormFromScope(NavigationElement e) {

		if (currentRequest != null)
			currentRequest.removeAttribute(e.getName());
		session.removeAttribute(e.getName());
	}

	// Il metodo reset viene chiamato SOLO E SOLTANTO dalla
	// NavigationRequestProcessor
	protected synchronized void reset() {
		if (this.marcaImageCache != null) {
			this.marcaImageCache.clearAll();
			this.marcaImageCache = null;
		}

		this.cache.removeAll();
		this.clearBookmarks();
		this.clearAttributes();
		this.isNew.set(false);	// non è più nuova

		if (hook != null)
			hook.reset(currentRequest);
	}

	protected void lockWith(NavigationElement element) {
		this.locked.set(true);
		this.lockingElement = element;

	}

	protected NavigationElement unlock() {
		this.locked.set(false);
		return this.lockingElement;
	}

	public boolean isLocked() {
		return this.locked.get();
	}

	public boolean isNew() {
		return isNew.get();
	}

	public void addBookmark(String bookmark) {

		if (isNull(bookmark))
			return;
		NavigationElement currentElement = this.cache.getCurrentElement();
		if (currentElement != null) {

			this.bookmarks.put(bookmark, currentElement);
			log.info("Creato bookmark per form '"
							+ currentElement.getActionForm() + "' ('"
							+ bookmark + "')");
		}
	}

	public void clearBookmarks() {
		this.bookmarks.clear();
	}

	public String createBookmark() {
		NavigationElement currentElement = this.cache.getCurrentElement();
		if (currentElement == null)
			return null;

		String bookmark = "bookmark_" + currentElement.getName() + "_"
				+ currentElement.getUniqueId();

		this.addBookmark(bookmark);
		return bookmark;
	}

	public boolean bookmarkExists(String bookmark) {
		return this.bookmarks.containsKey(bookmark);
	}

	public boolean bookmarksExist(String... bookmarks) {
		for (String bookmark : bookmarks)
			if (this.bookmarks.containsKey(bookmark))
				return true;
		return false;
	}

	/**
	  * Recupera il path della action che occupa la posizione precedente sulla barra
	  * di navigazione
	  * @return path della action precedente, <code>null</code> altrimenti
	 */
	public String getActionCaller() {

		if (cache.isEmpty())
			return null;

		String path;
		NavigationElement prev = cache
				.getElementAt(cache.getCurrentPosition() - 1);
		if (prev == null)
			return null;

		path = prev.getMapping().getPath();
		return path;
	}

	/**
	  * Recupera l'istanza della form della action che occupa la posizione precedente sulla barra
	  * di navigazione
	  * @return form della action precedente, <code>null</code> altrimenti
	 */
	public ActionForm getCallerForm() {

		if (cache.isEmpty())
			return null;

		NavigationElement prev = cache
				.getElementAt(cache.getCurrentPosition() - 1);
		if (prev == null)
			return null;

		return prev.getForm();
	}

	protected Map<String, NavigationElement> getBookmarks() {
		return bookmarks;
	}

	public NavigationCache getCache() {
		return cache;
	}

	public MarcaImageCache getMarcaImageCache() {
		if (this.marcaImageCache == null)
			this.marcaImageCache = new MarcaImageCache();
		return this.marcaImageCache;
	}

	public String getUserTicket() {
		UserVO utente = this.getUtente();
		if (utente != null)
			return utente.getTicket();

		return null;
	}

	public UserVO getUtente() {
		UserVO utente = null;
		if (this.session != null)
			utente = (UserVO) session.getAttribute(Constants.UTENTE_KEY);
		return utente;
	}

	public NavigationForward goBack() {
		return new NavigationForward(DirectionType.BACK, false);
	}

	public NavigationForward goBack(boolean fromBar) {
		return new NavigationForward(DirectionType.BACK, fromBar);
	}

	public NavigationForward goBack(ForwardConfig config) {
		return new NavigationForward(config, DirectionType.BACK, false);
	}

	public NavigationForward goBack(ForwardConfig config, boolean fromBar) {
		return new NavigationForward(config, DirectionType.BACK, fromBar);
	}

	public NavigationForward goForward(ForwardConfig config) {
		return new NavigationForward(config, DirectionType.FORWARD, false);
	}

	public NavigationForward goForward(ForwardConfig config, boolean fromBar) {
		return new NavigationForward(config, DirectionType.FORWARD, fromBar);
	}

	public NavigationForward goForward(ForwardConfig config, boolean fromBar, ActionForm form) {
		NavigationForward forward = new NavigationForward(config, DirectionType.FORWARD, fromBar);
		return forward.forceFormInstance(form);
	}

	public NavigationForward goToBookmark(String bookmark, boolean fromBar) {

		if (isNull(bookmark))
			return null;
		NavigationElement element = this.bookmarks.get(bookmark);
		if (element == null)
			return null;

		NavigationForward forward = new NavigationForward(DirectionType.BOOKMARK, fromBar);
		forward.setName("bookmark");
		forward.setPath(element.getMapping().getPath() + ".do");
		forward.setBookmarkPosition(element.getPosition());
		return forward;
	}

	public ActionForward goToNearestBookmark(String[] bookmarks, ActionForward defaultFwd, boolean fromBar) {

		if (!isFilled(bookmarks))
			return defaultFwd;

		int targetPosition = -1;
		int minDiff = Integer.MAX_VALUE;
		int position = this.cache.getCurrentElement().getPosition();
		for (String bookmark : bookmarks) {
			NavigationElement element = this.bookmarks.get(bookmark);
			if (element == null)
				continue;
			int diff = Math.abs(position - element.getPosition());
			if (diff < minDiff) {
				targetPosition = element.getPosition();
				minDiff = diff;
			}
		}

		if (targetPosition > -1) {
			NavigationForward forward = new NavigationForward(DirectionType.BOOKMARK, fromBar);
			forward.setName("bookmark");
			NavigationElement element = this.cache.getElementAt(targetPosition);
			forward.setPath(element.getMapping().getPath() + ".do");
			forward.setBookmarkPosition(element.getPosition());
			return forward;
		}

		return defaultFwd;
	}

	/**
	  * Verifica se la action corrente occupa la prima posizione sulla barra di navigazione
	  * @return <code>true</code> se la la action è la prima, <code>false</code> altrimenti
	 */
	public boolean isFirst() {

		if (cache.isEmpty())
			return false;

		NavigationElement element = cache.getCurrentElement();
		if (element == null)
			return false;

		return (element.getPosition() == 0);
	}

	/**
	  * Verifica se la <code>action</code> corrente è stata invocata tramite la barra di navigazione
	  * @return <code>true</code> se invocata da barra, <code>false</code> altrimenti
	 */
	public boolean isFromBar() {
		if (currentRequest == null)
			return false;
		return (currentRequest.getParameter(NAVIGATIONBAR_PARAM) != null);
	}

	/**
	  * Verifica se la <code>action</code> corrente è stata invocata tramite un <code>Navigation.goBack()</code>
	  * @return <code>true</code> se invocata da barra, <code>false</code> altrimenti
	 */
	public boolean isFromBack() {
		if (currentRequest == null)
			return false;
		return (currentRequest.getParameter(NAVIGATIONBAR_BACK_PARAM) != null);
	}

	/**
	  * Recupera il path della action che ha invocato il <code>goBack()</code>
	  * di navigazione
	  * @return path della action chiamante, <code>null</code> altrimenti
	 */
	public String getBackAction() {
		if (currentRequest == null)
			return null;
		return currentRequest.getParameter(NAVIGATIONBAR_BACK_PARAM);
	}

	/**
	  * Verifica se la action corrente occupa l'ultima posizione sulla barra di navigazione
	  * @return <code>true</code> se la la action è l'ultima, <code>false</code> altrimenti
	 */
	public boolean isLast() {

		if (cache.isEmpty())
			return false;

		NavigationElement element = cache.getCurrentElement();
		if (element == null)
			return false;

		return (element.getPosition() == cache.getLastPosition());
	}

	public void makeFirst() {
		if (this.isFirst())
			return;

		NavigationElement currentElement = this.cache.getCurrentElement();
		if (currentElement != null)
			this.cache.shiftTo(currentElement.getPosition());
	}

	public void makeFirst(String bookmark) {
		if (isNull(bookmark))
			return;
		NavigationElement element = this.bookmarks.get(bookmark);
		if (element != null)
			this.cache.shiftTo(element.getPosition());
	}

	public void registerButton(String key) {
		if (isNull(key))
			return;
		this.cache.getCurrentElement().setButton(key);
	}

	protected void removeBookmarks(NavigationElement element) {

		List<String> tmp = new ArrayList<String>();
		for (Map.Entry<String, NavigationElement> entry : this.bookmarks.entrySet() ) {
			if (entry.getValue().getUniqueId() == element.getUniqueId() )
				tmp.add(entry.getKey());
		}

		for (String key : tmp) {
			this.bookmarks.remove(key);
			log.info(String.format("Eliminato bookmark '%s'", key));
		}
	}

	public void removeBookmark(String bookmark) {

		if (isNull(bookmark))
			return;
		this.bookmarks.remove(bookmark);
		log.info(String.format("Eliminato bookmark '%s'", bookmark));
	}

	public void setDescrizioneX(String descr) {
		NavigationElement element = this.cache.getCurrentElement();
		if (element != null)
			element.setDescrizione(descr);
	}

	public void setExceptionLog(Throwable t) {
		if (currentRequest != null && t != null)
			currentRequest.setAttribute(NavigationErrorsTag.EXCEPTION_LOG_TAG, t);
	}

	public void setSuffissoTesto(String suffix) {
		NavigationElement element = this.cache.getCurrentElement();
		if (element != null)
			element.setSuffissoTesto(suffix);
	}

	public void setTesto(String testo) {
		NavigationElement element = this.cache.getCurrentElement();
		if (element != null) {
			String msg = LinkableTagUtils.findMessage(currentRequest, Locale.getDefault(), testo);
			element.setTesto(msg != null ? msg : testo);
		}
	}

	public void purgeThis() {
		NavigationElement element = this.cache.getCurrentElement();
		if (element != null)
			element.setPurge(true);
		this.isNew.set(false);
	}

	public Serializable getAttribute(String key) {
		NavigationAttribute attribute = attributes.get(key);
		if (attribute != null)
			return attribute.getValue();

		return null;
	}

	public void setAttribute(String key, Serializable value) {
		setAttribute(key, value, false);
	}

	public void setAttribute(String key, Serializable value, boolean stable) {
		NavigationElement currentElement = cache.getCurrentElement();
		if (stable)
			attributes.put(key, new NavigationAttribute(null, value));
		else
			attributes.put(key, new NavigationAttribute(currentElement, value));
		log.info("Creato attributo '"+ key + "' per form '" + currentElement.getName() +
			"' (posizione " + currentElement.getPosition() + ")");
	}

	public void removeAttribute(String key) {
		attributes.remove(key);
	}

	protected void removeAttributes(NavigationElement element) {

		List<String> tmp = new ArrayList<String>(this.attributes.size());
		for (Map.Entry<String, NavigationAttribute> entry : this.attributes.entrySet() ) {
			NavigationElement e = entry.getValue().element;
			if (e != null && e.getUniqueId() == element.getUniqueId())
				tmp.add(entry.getKey());
		}

		for (String key : tmp) {
			this.attributes.remove(key);
			log.info("Eliminato attributo '"+ key + "' per form '" + element.getName() +
					"' (posizione " + element.getPosition() + ")");
		}
	}

	public void clearAttributes() {
		this.attributes.clear();
	}

	public String getPolo() {
		return (String) session.getAttribute(Constants.POLO_CODICE);
	}

	public InetAddress getUserAddress() {
		if (currentRequest == null)
			return null;

		try {
			return InetAddress.getByName(currentRequest.getRemoteAddr());
		} catch (UnknownHostException e) {
			log.error("", e);
			return null;
		}
	}

	public void setHook(NavigationHook hook) {
		this.hook = hook;
	}

	public NavigationHook getHook() {
		return hook;
	}

	public NavigationElement getLockingElement() {
		return lockingElement;
	}

	public int getNavigationId() {
		return navigationId;
	}

	/**
	 * Elimina questo elemento dalla navigazione solo se il path del forward in input
	 * è diverso da quello configurato.
	 * @param forward il forward di destinazione
	 */

	public void purgeThisIf(ActionForward forward) {
		NavigationElement element = this.cache.getCurrentElement();
		if (element != null && forward != null) {
			String defaultPath = element.getMapping().getInputForward().getPath();
			String forwardPath = forward.getPath();
			if (!ValidazioneDati.equals(defaultPath, forwardPath))
				this.purgeThis();
		}
	}

	public String getCurrentElementTextKey() {
		NavigationElement element = this.cache.getCurrentElement();
		if (element != null)
			return element.getTextKey();

		return null;
	}

}
