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
package it.iccu.sbn.util.validation;

import it.iccu.sbn.ejb.DomainEJBFactory;
import it.iccu.sbn.ejb.exception.ApplicationException;
import it.iccu.sbn.polo.orm.servizi.Tbl_parametri_biblioteca;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import org.apache.log4j.Logger;

public class JsScriptSessionImpl implements JsScriptSession {

	private static Logger log = Logger.getLogger(JsScriptSession.class);

	private final ScriptEngine engine;
	private final ScriptContext ctx;

	private static final Map<String, CompiledScript> scripts = new ConcurrentHashMap<String, CompiledScript>();

	JsScriptSessionImpl() {
		engine = new ScriptEngineManager().getEngineByName("javascript");
		ctx = new SimpleScriptContext();
	}

	public Map<String, Object> exec(String scriptKey, Map<String, Object> params) throws ApplicationException {
		try {
			return DomainEJBFactory.getInstance().getPolo().execScript(this, scriptKey, params);
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	public ScriptEngine getEngine() {
		return engine;
	}

	public ScriptContext getCtx() {
		return ctx;
	}

	public Map<String, CompiledScript> getScripts() {
		return scripts;
	}

	public static void test() {
		try {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
			ScriptContext ctx = new SimpleScriptContext();
		    Bindings params = ctx.getBindings(ScriptContext.ENGINE_SCOPE);
		    params.put("polo", "CSW");
		    params.put("bib", " IC");

			StringBuilder script = new StringBuilder();
			script.setLength(0);
			script.append("importClass(Packages.it.iccu.sbn.servizi.codici.CodiciProvider);\r\n" +
					"importClass(Packages.it.iccu.sbn.ejb.vo.common.CodiciType);\r\n" +
					"\r\n" +
					"function loadCodici(type) {\r\n" +
					"	return CodiciProvider.getCodici(type);\r\n" +
					"};\r\n" +
					"\r\n" +
					"function concatena(list) {\r\n" +
					"	var text='';\r\n" +
					"	for (idx=0;idx<list.size();idx++) {\r\n" +
					"			text += '; ' + list.get(idx).cd_tabellaTrim;\r\n" +
					"	};\r\n" +
					"	return text;\r\n" +
					"};\r\n" +
					"\r\n" +
					"var out = concatena(loadCodici(CodiciType.CODICE_LIVELLO_AUTORITA));" +
					"importClass(Packages.it.iccu.sbn.persistence.dao.servizi.UtilitaDAO);" +
					"var dao = new UtilitaDAO();" +
					"var parbib = dao.getParametriBiblioteca(polo, bib);" +
					"var giggi=\"pluto\";");

			engine.eval(script.toString(), ctx);

			Tbl_parametri_biblioteca parbib = (Tbl_parametri_biblioteca) params.get("parbib");

			//engine.eval("parbib.setNum_lettere(3);", ctx);

			log.debug(parbib);

		} catch (Exception e) {
			log.error("", e);
		}

	}

}
