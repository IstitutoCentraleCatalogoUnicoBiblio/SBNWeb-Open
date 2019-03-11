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
package it.iccu.sbn.util.ConvertiVo.calendario;

import it.iccu.sbn.ejb.utils.DateUtil;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;
import it.iccu.sbn.util.ConvertiVo.DataBindingConverter;

import java.lang.reflect.Type;
import java.util.Date;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public abstract class CalendarioDataBinder extends DataBindingConverter {

	private static final long serialVersionUID = -92679864763275871L;

	private static final Type LOCALTIME_TYPE = new TypeToken<LocalTime>(){}.getType();
	private static final Type JDK_DATE_TYPE = new TypeToken<Date>(){}.getType();

	protected static class LocalTimeConverter implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
		/** Real ISO8601 Format specifier */
		private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
				.append(ISODateTimeFormat.time().getPrinter(), ISODateTimeFormat.localTimeParser().getParser())
				.toFormatter();

		public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext ctx) {
			return new JsonPrimitive(src.toString(FORMATTER));
		}

		public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx)
				throws JsonParseException {
			String value = json.getAsString();
			return isFilled(value) ? LocalTime.parse(value, FORMATTER) : null;
		}
	}

	protected static class JdkDateConverter implements JsonSerializer<Date>, JsonDeserializer<Date> {

		public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext ctx) {
			return new JsonPrimitive(DateUtil.toFormatoIso(src));
		}

		public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx)
				throws JsonParseException {
			String value = json.getAsString();
			return isFilled(value) ? DateUtil.toDateISO(value) : null;
		}
	}

	protected GsonBuilder builder;

	public CalendarioDataBinder() {
		super();
		builder = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(LOCALTIME_TYPE, new LocalTimeConverter() )
			.registerTypeAdapter(JDK_DATE_TYPE, new JdkDateConverter() )
			.addSerializationExclusionStrategy(new ExclusionStrategy() {

				String[] EXCLUDED_FIELDS = new String[] { "progr", "tsIns", "uteIns", "tsVar", "uteVar", "flCanc",
						"uniqueId", "creationTime", "start", "end", "elementi" };
				Class<?>[]	EXCLUDED_CLASS = new Class[] {BibliotecaVO.class, SalaVO.class };

				public boolean shouldSkipField(FieldAttributes fattrs) {
					return ValidazioneDati.in(fattrs.getName(), EXCLUDED_FIELDS);
				}

				public boolean shouldSkipClass(Class<?> clazz) {
					return ValidazioneDati.in(clazz, EXCLUDED_CLASS);
				}
			});

	}

}
