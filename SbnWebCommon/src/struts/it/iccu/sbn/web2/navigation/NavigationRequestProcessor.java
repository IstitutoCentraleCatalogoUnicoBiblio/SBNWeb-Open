/*
 * NavigationRequestProcessor.java
 *
 * Created on September 12, 2006, 5:17 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.iccu.sbn.web2.navigation;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.util.IdGenerator;
import it.iccu.sbn.web.vo.UserVO;
import it.iccu.sbn.web2.navigation.NavigationForward.DirectionType;
import it.iccu.sbn.web2.tags.LinkTastieraSbnTag;
import it.iccu.sbn.web2.util.LinkableTagUtils;
import it.iccu.sbn.web2.util.NavigationListener;
import it.iccu.sbn.web2.util.NavigationMappingChecker;
import it.iccu.sbn.web2.util.NavigationPreprocessor;
import it.iccu.sbn.web2.util.NavigationListener.Event;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ModuleUtils;

/**
 *
 * @author Antonio
 * @author almaviva
 */
public class NavigationRequestProcessor extends RequestProcessor {

	private static final Logger log = Logger.getLogger(NavigationRequestProcessor.class);

	private static final String NAV_CHECK_STOP = "nav.check.stop";
	private static final String NAV_ERROR_REDIRECT_ID = "nav.err.redirect.id";

	private NavigationPreprocessor preprocessor = null;

	private final void _log(HttpServletRequest request, String value, Level level, String name) {

		Navigation navi = Navigation.getInstance(request);
		UserVO utente = navi.getUtente();
		InetAddress addr = navi.getUserAddress();
		if (utente != null && addr != null)
			log.log(level, String.format("(navId: %d, uid: %s, ip: %s) - %s",
					navi.getNavigationId(), utente.getUserId(),	addr.getHostAddress(), value));
		else
			log.log(level, String.format("(navId: %d) - %s", navi.getNavigationId(), value));
	}

	protected boolean processPreprocess(HttpServletRequest request,
			HttpServletResponse response) {

		NavigationPreprocessor pp = getPreprocessor();
		pp.setSessionInfo(request.getSession());

		return pp.preprocess(request, response);

	}

	protected boolean checkNavigationCoherence(HttpServletRequest request,
			HttpServletResponse response, Navigation navi)
			throws Exception {

		// ogni jsp con navigazione trasporta un input hidden con l'id della form
		String currentElement = request.getParameter(Navigation.NAVIGATION_CURRENT_ELEMENT);
		// in alternativa (se ho scelto un link) potrei trovare il form target
		String ftarget = request.getParameter(LinkableTagUtils.FORM_TARGET_PARAM);
		if (currentElement == null)
			currentElement = ftarget;

		// se non ho l'info richiesta procedo con la navigazione
		if (ValidazioneDati.strIsNull(currentElement))
			return true;

		// se l'id è 0 ho cliccato sul menu laterale
		Integer id = Integer.valueOf(currentElement);
		if (id < 1) return true;

		// inserito per evitare loop sul parameter
		Boolean stop = (Boolean)request.getAttribute(NAV_CHECK_STOP);

		if (currentElement != null &&
			stop == null &&
			navi.getCache().getElementById(id) == null) {

			// la form richiesta non è più presente sulla navigazione, sessione corrotta
			LinkableTagUtils.addError(request, new ActionMessage("errors.navigation.corrupted") );

			navi.reset();
			request.setAttribute(NAV_CHECK_STOP, true);	// inserito per evitare loop sul parameter
			request.getRequestDispatcher("/blank.do").forward(request, response);
			return false;
		}

		return true;
	}

	// //////////////////////////////////////////////////////////////////////////////////

	private NavigationPreprocessor getPreprocessor() {
		if (preprocessor == null) {
			preprocessor = (NavigationPreprocessor) servlet.getServletContext().getAttribute(Navigation.NAVIGATION_PREPROCESSOR);
			log.debug("Recupero Preprocessor: " + preprocessor.getClass().getCanonicalName());
		}
		return preprocessor;
	}

	protected MultipartRequestHandler getMultipartHandler(
			HttpServletRequest request) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		ModuleConfig moduleConfig = ModuleUtils.getInstance().getModuleConfig(request);
		String multipartClass = moduleConfig.getControllerConfig().getMultipartClass();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		MultipartRequestHandler multipartHandler =
			(MultipartRequestHandler) classLoader.loadClass(multipartClass).newInstance();

		return multipartHandler;
	}

	private void adjustNavigationBar(HttpServletRequest request,
			NavigationCache cache, NavigationElement e) {

		if (cache.isEmpty())
			return;

		for (int index = cache.getLastPosition(); index >= 0; index--) {
			NavigationElement element = cache.getElementAt(index);
			element.setLast(element.getPosition() == cache.getLastPosition());
			element.setHref(e != element);
		}

		// Se ho specificato un'ancora interna alla pagina la imposto sul tag
		// body della jsp
		// FUNZIONA SOLO CON JAVASCRIPT!!
		if (ValidazioneDati.isFilled(e.getAnchorId()))
			request.setAttribute(LinkableTagUtils.ANCHOR_PREFIX, e
					.getAnchorId());

	}

	private void clearNavigation(HttpServletRequest request,
			HttpServletResponse response, NavigationCache cache, NavigationElement e) {

		int lastPosition = cache.getLastPosition();
		int position = e.getPosition();

		if (cache.isEmpty() || position == lastPosition)
			return;

		for (int index = lastPosition; index > position; index--) {
			NavigationElement old = cache.getElementAt(index);
			this.removeFormFromScope(request, old);
			doListener(request, response, old.getActionInstance(), old.getForm(), Event.DESTROY);
		}

		cache.clearFromHere(position);
		int newLastPosition = cache.getLastPosition();

		for (int index = 0; index <= newLastPosition; index++) {
			NavigationElement old = cache.getElementAt(index);
			this.putFormInScope(request, old);
		}

		_log(request, "Pulizia navigazione da posizione " + position + " ('" + e.getName() + "')", Level.INFO, e.getName());
	}

	private ActionForm createActionForm(HttpServletRequest request, ActionMapping mapping) {

		String attribute = mapping.getAttribute();
		if (attribute == null)
			return null;
		String name = mapping.getName();
		FormBeanConfig config = moduleConfig.findFormBeanConfig(name);
		if (config == null)
			return null;

		try {
			_log(request, "Creata nuova istanza di '" + attribute + "'", Level.INFO, attribute);
			return config.createActionForm(this.servlet);
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InstantiationException e) {

			e.printStackTrace();
		}

		return null;
	}

	protected ActionForm processActionForm(HttpServletRequest request,
			HttpServletResponse response, ActionMapping mapping) {
		ActionForm instance, newInstance;
		NavigationElement element = null;

		// creo la cache o la recupero dalla sessione
		Navigation navi = Navigation.getInstance(request);
		NavigationCache cache = navi.getCache();

		MultipartRequestHandler mrh = null;

		//almaviva5_20090916 fix1 per richieste multipart (file upload)
		//Le richieste multipart trasportano i parametri in maniera
		//differente e non sono subito disponibili alla navigazione.
		//E' necessario quindi anticipare il caricamento dei parametri
		//da parte di Struts per utilizzarli nella navigazione
		boolean isMultipart = (request instanceof MultipartRequestWrapper);
		if (isMultipart)
			try {
				mrh = getMultipartHandler(request);
				mrh.setMapping(mapping);
				mrh.setServlet(servlet);
				mrh.handleRequest(request);

			} catch (Exception e) {
				log.error("", e);
			}


		boolean fromBar = this.restoreSession(request, response, navi, false);

		instance = super.processActionForm(request, response, mapping);
		if (instance == null) {
			HttpSession session = request.getSession();
			boolean withForm = (Boolean) ValidazioneDati.coalesce(session.getAttribute(Navigation.ACTION_WITH_FORM), true);
			this.restoreSession(request, response, navi, withForm);
			session.removeAttribute(Navigation.ACTION_WITH_FORM);
			return null;
		}

		// Controllo la richiesta del passo precedente
		switch (cache.getDirection()) {

		case DEFAULT:
			element = cache.getElementByForm(instance);
			// se questo form non è presente nella cache sto
			// procedendo nella navigazione e devo cancellare eventuali
			// form che seguono quello corrente
			if (element == null) {
				NavigationElement prev = cache.getCurrentElement();
				if (prev != null)
					this.clearNavigation(request, response, cache, prev);
				element = this.saveNewForm(request, cache, mapping, instance);
			} else if (!fromBar )//&& !isBlockButton(request, element))
				this.clearNavigation(request, response, cache, element);

			instance = element.getForm();
			break;

		case FORWARD:
			// questo metodo costringe struts a creare una nuova form
			// senza cercarne un'istanza già presente nello scope.
			// Inoltre devo cancellare eventuali form che seguono
			// quello corrente
			if (cache.getCurrentPosition() != cache.getLastPosition()) {
				NavigationElement prev = cache.getCurrentElement();
				if (prev != null)
					this.clearNavigation(request, response, cache, prev);
			}
			if (cache.getElementByForm(instance) == null)
				newInstance = instance;
			else
				newInstance = this.createActionForm(request, mapping);

			element = this.saveNewForm(request, cache, mapping, newInstance);
			instance = newInstance;
			break;

		case BACK:
			element = cache.getElementByForm(instance);
			if (element == null)
				element = this.saveNewForm(request, cache, mapping, instance);
			else
				element.setBack(true);

			this.clearNavigation(request, response, cache, element);

			instance = element.getForm();
			break;

		case BOOKMARK:
			element = cache.getElementAt(cache.getBookmarkPosition());
			if (element == null)
				element = this.saveNewForm(request, cache, mapping, instance);

			this.clearNavigation(request, response, cache, element);

			instance = element.getForm();
			break;
		}

		this.adjustNavigationBar(request, cache, element);
		this.putFormInScope(request, element);
		this.updateVkbd(request);

		//almaviva5_20090916 fix2 per richieste multipart (file upload)
		//La lettura precedente (fix1) ha distrutto i parametri della richiesta.
		//Dobbiamo popolare la form in anticipo rispetto al flow
		//tipico di Struts.
		if (isMultipart && mrh != null)
			prepopulateForm(instance, mrh);

		return instance;
	}

	private void prepopulateForm(ActionForm form, MultipartRequestHandler handler) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
		    Hashtable<?, ?> elements = handler.getAllElements();
		    Enumeration<?> e = elements.keys();
		    while (e.hasMoreElements()) {
		    	String key = (String)e.nextElement();
		    	params.put(key, elements.get(key));
		    }

		    if (ValidazioneDati.isFilled(params))
		    	BeanUtils.populate(form, params);

		} catch (Exception e) {
			log.error("", e);
		}
	}

	private void updateVkbd(HttpServletRequest request) {
		String fieldValue = (String) request.getAttribute(LinkTastieraSbnTag.KEYBOARD_RESPONSE);
		if (fieldValue == null)
			return;

		request.removeAttribute(LinkTastieraSbnTag.KEYBOARD_RESPONSE);
		try {
			String[] token = fieldValue.split(String.valueOf(LinkableTagUtils.SEPARATORE));
			int id = Integer.valueOf(token[0]);
			Object target = Navigation.getInstance(request).getCache().getElementById(id).getForm();
			PropertyUtils.setProperty(target, token[1], token[2]);
			_log(request, "Imposta campo origine '" + token[1] + "' a: " + token[2], Level.INFO, null);
		} catch (Exception e) {
			e.printStackTrace();
			Navigation.getInstance(request).setExceptionLog(e);
		}
		return;
	}

	@Override
	protected void processForwardConfig(HttpServletRequest request,
			HttpServletResponse response, ForwardConfig config)
			throws IOException, ServletException {

		Navigation navi = Navigation.getInstance(request);
		NavigationCache cache = navi.getCache();

		if (config instanceof NavigationForward) {
			NavigationForward forward = (NavigationForward) config;

			//almaviva5_20090811 salvo i messaggi in caso di redirect
			if (forward.getRedirect())
				processErrorsBeforeRedirect(request, navi, forward);

			// Controllo la richiesta del passo precedente
			switch (forward.getDirection()) {
			case DEFAULT:
				cache.setDirection(DirectionType.DEFAULT);
				break;

			case FORWARD:
				cache.setDirection(DirectionType.FORWARD);
				String path = forward.getPath();
				int ext = path.indexOf('.');
				if (ext > -1)
					path = path.substring(0, ext);
				ActionMapping mapping = super.processMapping(request, response,	path);
				if (mapping != null) {
					this.removeFormFromScope(request, mapping.getName());

					ActionForm form = forward.getFormInstance();
					if (form != null) {
						FormBeanConfig formConfig = moduleConfig.findFormBeanConfig(mapping.getName());
						if (formConfig.getType().equals(form.getClass().getCanonicalName()))
							this.putFormInScope(request, mapping.getName(),	form);
					}
				}
				break;

			case BACK:
				//almaviva5_20071214
				NavigationElement current = cache.getCurrentElement();
				forward.addParameter(Navigation.NAVIGATIONBAR_BACK_PARAM, current.getUri() + ".do");

				if (forward.getName() == null || forward.getPath() == null) {
					cache.setDirection(DirectionType.DEFAULT);
					if (cache.getCurrentPosition() < 1)
						break;

					// se non ho specificato un forward particolare
					// provo a tornare alla posizione precedente sulla barra
					NavigationElement prev = cache.getPreviousElement();
					prev.setBack(true);
					this.clearNavigation(request, response, cache, prev);
					forward.setName("NAVIGATION_BACK");
					forward.setPath(prev.getUri() + ".do");
					break;

				} else {
					cache.setDirection(DirectionType.BACK);
					break;
				}

			case BOOKMARK:
				cache.setDirection(DirectionType.BOOKMARK);
				cache.setBookmarkPosition(forward.getBookmarkPosition());
				break;
			} // end switch

			// simulazione navigazione da barra
			if (forward.isFromBar())
				forward.addParameter(Navigation.NAVIGATIONBAR_PARAM, "-1");

		} else {
			// comportamento standard
			cache.setDirection(DirectionType.DEFAULT);
		}

		this.processAnchor(request);
		super.processForwardConfig(request, response, config);
		//config.g
	}

	private void processErrorsBeforeRedirect(HttpServletRequest request,
			Navigation navi, NavigationForward forward) {

		ActionMessages errors = LinkableTagUtils.getErrors(request);
		if (errors.isEmpty())
			return;

		// id univoco per i messaggi da conservare attraverso il redirect della request;
		String id = IdGenerator.getId() + "";
		navi.setAttribute(id, errors, true);

		forward.addParameter(NAV_ERROR_REDIRECT_ID, id);
	}

	private void processErrorsAfterRedirect(HttpServletRequest request,
			Navigation navi) {

		String id = request.getParameter(NAV_ERROR_REDIRECT_ID);
		if (id == null)
			return;

		ActionMessages savedErrors = (ActionMessages)navi.getAttribute(id);
		navi.removeAttribute(id); // comunque vada questo dato non serve più
		if (savedErrors == null || savedErrors.isEmpty())
			return;

		// se la request trasporta già altri messaggi aggiungo quelli salvati,
		// altrimenti imposto nella request l'oggetto recuperato dalla navigazione
		ActionMessages errors = LinkableTagUtils.getErrors(request);
		if (!errors.isEmpty())
			savedErrors.add(errors);

		request.setAttribute(Globals.ERROR_KEY, savedErrors);

	}

	@Override
	protected ActionMapping processMapping(HttpServletRequest request,
			HttpServletResponse response, String path) throws IOException {

		ActionMapping mapping = super.processMapping(request, response, path);
		if (mapping == null || !(mapping instanceof NavigationMappingChecker))
			return mapping;

		NavigationElement currentElement = Navigation.getInstance(request).getCache().getCurrentElement();
		if (currentElement != null && mapping == currentElement.getMapping())	//chiamo me stesso
			return mapping;

		boolean check = true;//((NavigationMappingChecker)mapping).check(request, currentElement);
		if (check)
			return mapping;

		//Errore!!!
		LinkableTagUtils.addError(request, new ActionMessage("errors.navigation.corrupted"));
		return super.processMapping(request, response, "/blank");
	}

	@Override
	protected ActionForward processActionPerform(HttpServletRequest request,
			HttpServletResponse response, Action action, ActionForm form,
			ActionMapping mapping) throws IOException, ServletException {

		Navigation navi = Navigation.getInstance(request);
		NavigationElement element = null;
		try {
			NavigationCache cache = navi.getCache();
			if (navi.isLocked()) {
				LinkableTagUtils.addError(request, new ActionMessage("errors.navigation.locked") );
				element = navi.getLockingElement();
				if (element != null)
					_log(request, String.format("ERRORE PERFORM: eId: %d; Applicazione bloccata in altra transazione",
						element.getUniqueId() ), Level.ERROR, null);
				else
					_log(request, "Applicazione bloccata in altra transazione", Level.ERROR, null);

				String parameter = request.getParameter(LinkableTagUtils.FORM_TARGET_PARAM);
				if (parameter != null) {
					NavigationElement target = cache.getElementById(Integer.valueOf(parameter));
					if (target == null)
						return mapping.findForward("blank");

					element = target;
					return target.getMapping().getInputForward();

				} else
					return mapping.getInputForward();
			}

			// la navigazione è impegnata in una transazione
			element = cache.getCurrentElement();
			navi.lockWith(element);

			if (element != null) {
				element.setActionInstance(action);
				if (log.isDebugEnabled()) {
					String param = ValidazioneDati.coalesce(request.getParameter(element.getMapping().getParameter()), "n/a");
					element.setPerformStart(System.currentTimeMillis());
					String name = action.getClass().getSimpleName();
					_log(request, String.format("INIZIO PERFORM: eId: %d; action: %s; button: '%s'",
						element.getUniqueId(), name, param), Level.DEBUG, name);
				}
			}

			if (this.checkVkbd(cache, request))
				return mapping.findForward("tastiera");

			if (!navi.isNew() || !getPreprocessor().isNew() ) {
				cache.purge();
				ActionForward navigation = checkNavigationBar(request, response, form, mapping, navi, element);
				if (navigation != null)
					return navigation;
			}

			ActionForward forward = super.processActionPerform(request, response, action, form, mapping);

			//almaviva5_20090811 recupero gli eventuali messaggi a fronte di un redirect;
			processErrorsAfterRedirect(request, navi);

			//almaviva5_20100204 messaggio avviso per redeploy sistema
			getPreprocessor().afterAction(request, response, action, form, mapping);

			return forward;

		} finally {
			// sblocca la navigation bar
			element = navi.unlock();

			if (log.isDebugEnabled() && element != null) {
				double end = (double)(System.currentTimeMillis() - element.getPerformStart()) / 1000;
				String name = element.getActionInstance().getClass().getSimpleName();
				_log(request, String.format("FINE   PERFORM: eId: %d; action: %s (in %.3f secondi)",
					element.getUniqueId(), name, end), Level.DEBUG, name);
			}

		}
	}

	private void doListener(HttpServletRequest request,
			HttpServletResponse response, Action action, ActionForm form,
			NavigationListener.Event event) {

		if ( !(action instanceof NavigationListener))
			return;

		try {
			NavigationListener listener = (NavigationListener)action;
			switch (event) {
			case ENTER:
				listener.enter(request, response, form);
				break;
			case LEAVE:
				listener.leave(request, response, form);
				break;
			case DESTROY:
				listener.destroy(request, response, form);
				break;
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private ActionForward checkNavigationBar(HttpServletRequest request,
			HttpServletResponse response, ActionForm form,
			ActionMapping mapping, Navigation navi,
			NavigationElement currentElement) {

		int id = -1;
		Enumeration<?> params = request.getParameterNames();

		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			if (param.startsWith(Navigation.NAVIGATIONBAR_SUBMIT)) {

				String paramValue = request.getParameter(param);
				if (paramValue.equals(Navigation.NULL))
					continue;

				try {
					String token[] = param.split("-");
					id = Integer.valueOf(token[1]);
				} catch (NumberFormatException e) {
					navi.setExceptionLog(e);
					return null;
				}

				NavigationCache cache = navi.getCache();
				NavigationElement nextElement = cache.getElementById(id);
				if (nextElement == null) {
					ActionMessages errors = (ActionMessages) request
							.getAttribute(Globals.ERROR_KEY);
					if (errors == null) {
						errors = new ActionMessages();
						request.setAttribute(Globals.ERROR_KEY, errors);
					}
					errors.add(ActionMessages.GLOBAL_MESSAGE,
							new ActionMessage("errors.navigation.corrupted"));
					return mapping.findForward("blank");
				}

				doListener(request, response, currentElement
						.getActionInstance(), form,
						NavigationListener.Event.LEAVE);

				this.putFormInScope(request, nextElement);
				ActionRedirect forward = new ActionRedirect();
				forward.setName("navigation");
				forward.setPath(nextElement.getMapping().getPath() + ".do");
				forward.addParameter(Navigation.NAVIGATIONBAR_PARAM,
						nextElement.getPosition());
				forward.addParameter(LinkableTagUtils.FORM_TARGET_PARAM,
						nextElement.getUniqueId());

				doListener(request, response, nextElement.getActionInstance(),
						nextElement.getForm(), NavigationListener.Event.ENTER);

				return forward;

			}
		}
		return null;
	}

	private boolean checkVkbd(NavigationCache cache, HttpServletRequest request) {

		Enumeration<?> params = request.getParameterNames();

		// Cerco tra i parametri della request quello del pulsante
		// generato dal tag sbn:tastiera
		while (params.hasMoreElements()) {
			String paramName = (String) params.nextElement();
			if (paramName.startsWith(LinkableTagUtils.VKBD_BUTTON))
				try {

					String paramValue = request.getParameter(paramName);
					String sep = String.valueOf(LinkableTagUtils.SEPARATORE);
					if (paramValue.equals(sep))
						continue;

					String vkbdData = ValidazioneDati.unmaskString(paramName.split(sep)[1]);
					String[] token = vkbdData.split(sep);

					int pos = Integer.valueOf(token[3]);
					if (pos != cache.getCurrentPosition())
						return false;

					request.setAttribute(LinkTastieraSbnTag.KEYBOARD_DATA_PARAM, vkbdData);
					return true;

				} catch (Exception e) {
					Navigation.getInstance(request).setExceptionLog(e);
					return false;
				}
		}
		return false;
	}

	private void processAnchor(HttpServletRequest request) {
		ActionMessages errors = (ActionMessages) request
				.getAttribute(Globals.ERROR_KEY);
		if (errors != null && errors.size() > 0)
			request.removeAttribute(LinkableTagUtils.ANCHOR_PREFIX);
	}

	private void putFormInScope(HttpServletRequest request, NavigationElement e) {

		request.setAttribute(e.getName(), e.getForm());
		HttpSession session = request.getSession();
		session.setAttribute(e.getName(), e.getForm());
	}

	private void putFormInScope(HttpServletRequest request, String name,
			ActionForm form) {
		request.setAttribute(name, form);
		HttpSession session = request.getSession();
		session.setAttribute(name, form);
	}

	private void removeFormFromScope(HttpServletRequest request, String name) {
		request.removeAttribute(name);
		HttpSession session = request.getSession();
		session.removeAttribute(name);
	}

	private void removeFormFromScope(HttpServletRequest request,
			NavigationElement e) {

		request.removeAttribute(e.getName());
		HttpSession session = request.getSession();
		session.removeAttribute(e.getName());
	}

	private boolean restoreSession(HttpServletRequest request,
			HttpServletResponse response, Navigation navi, boolean clear) {

		NavigationCache cache = navi.getCache();
		if (cache.isEmpty())
			return false;

		if (clear)
			try {
				// elimino tutti i riferimenti alle form dallo scope
				List<NavigationElement> elements = cache.getLockedElements();
				for (NavigationElement e : elements) {
					this.removeFormFromScope(request, e);
					doListener(request, response, e.getActionInstance(), e.getForm(), Event.DESTROY);
				}

				getPreprocessor().resetNavigation(request);
				navi.reset();
				_log(request, "Pulizia completa della navigazione", Level.INFO, null);
				return false;
			} finally {
				cache.unlockElements();
			}

		// almaviva5_20070929 se l'utente fa click due volte consecutive su un link
		// devo reindirizzare sulla pagina corrente.
		// I link arrivano sempre col metodo HTTP GET
		// ATTENZIONE: qui si presume che il link sia generato dai tag sbn
		// che chiamano sempre la action corrente.
		if (request.getMethod().equalsIgnoreCase("GET") && navi.isLocked())
			cache.setDirection(DirectionType.DEFAULT);

		// Navigazione tramite link barra navigazione
		String position = request.getParameter(Navigation.NAVIGATIONBAR_PARAM);
		if (position != null) {
			int pos = Integer.valueOf(position);
			// se pos < 0 l'invocazione della navigazione è derivata da goBack()
			if (pos < 0)
				pos = cache.getCurrentPosition();
			NavigationElement element = cache.getElementAt(pos);
			this.putFormInScope(request, element);
			return true;
		}

		Enumeration<?> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = (String) params.nextElement();
			if (param.startsWith(Navigation.NAVIGATIONBAR_SUBMIT)) {
				String paramValue = request.getParameter(param);
				if (!paramValue.equals(Navigation.NULL))
					return true;
			}
		}

		return false;
	}

	private NavigationElement saveNewForm(HttpServletRequest request,
			NavigationCache cache, ActionMapping mapping, ActionForm form) {
		NavigationElement element;

		element = cache.addElement(mapping, form);
		_log(request, "Salvata nuova istanza di '" + element.getName()
				+ "' in posizione " + element.getPosition() +
				" [eId: " + element.getUniqueId() + "]",
				Level.INFO, element.getName());
		return element;
	}

	@SuppressWarnings("unused")
	private boolean isBlockButton(HttpServletRequest request,
			NavigationElement element) {

		if (element.getButton() == null)
			return false;
		String param = request.getParameter(element.getMapping().getParameter());
		if (param == null)
			return false;

		ModuleConfig moduleConfig = element.getMapping().getModuleConfig();
		MessageResourcesConfig mrc[] = moduleConfig.findMessageResourcesConfigs();
		String message = null;
		for (int i = 0; i < mrc.length; i++) {
			MessageResources resources = (MessageResources) request.getAttribute(mrc[i].getKey());
			message = resources.getMessage(element.getButton());
			if (!(message == null || message.startsWith("???")))
				break;
		}

		if (message == null)
			return false;

		return (message.equals(param));
	}

}
