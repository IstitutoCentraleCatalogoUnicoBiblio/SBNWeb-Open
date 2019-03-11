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
package it.iccu.sbn.ejb.vo.amministrazionesistema.codici;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.common.CodiciOrdinamentoType;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CodiceConfigVO extends SerializableVO {

	private static final long serialVersionUID = 5916533873369858519L;

	private static final String PROPS_SEPARATOR = "\\|";
	private static final String FIELD_SEPARATOR = ";";

	private static final int DICT_FIELDS_COUNT = 6;
	private static final int CROSS_FIELDS_COUNT = 1;

	private static final int CROSS_TOKENS_COUNT = 4;
	private static final int MAX_CD_TAB_LENGTH = 14;
	private static final int MAX_CD_LENGTH = 10;

	private static final String TEST_D = "uni=0|ALPHA|NOBLANK;marc21=1|ALPHA|BLANK;mat=1|ALL|NOBLANK;tab=DICT|1|NUMERIC|READ;";
	private static final String TEST_C = "tab=CROSS|LTSE|LMER|READ";

	private static final Random dummy = new java.security.SecureRandom();

	private boolean used = false;
	private TabellaType tipoTabella;
	private int length;
	private CodiceValueType type = CodiceValueType.ALL;
	private CodiciPermessiType permessi;

	private int cd_unimarc_length;
	private CodiceValueType cd_unimarc_type;
	private boolean cd_unimarc_allow_blank = false;

	private int cd_marc21_length;
	private CodiceValueType cd_marc21_type;
	private boolean cd_marc21_allow_blank = false;

	private int tp_materiale_length;
	private CodiceValueType tp_materiale_type;
	private boolean tp_materiale_allow_blank = false;

	private int ds_ulteriore_length;

	private CodiciOrdinamentoType ordinamento;

	private CodiceFlagConfigVO flg2Config;
	private CodiceFlagConfigVO flg3Config;
	private CodiceFlagConfigVO flg4Config;
	private CodiceFlagConfigVO flg5Config;
	private CodiceFlagConfigVO flg6Config;
	private CodiceFlagConfigVO flg7Config;
	private CodiceFlagConfigVO flg8Config;
	private CodiceFlagConfigVO flg9Config;
	private CodiceFlagConfigVO flg10Config;
	private CodiceFlagConfigVO flg11Config;

	private String cdTabella;
	private String descrizione;
	private String nomeTabella;

	private CodiciType tpTabellaP;
	private CodiciType tpTabellaC;

	private String nomeTabellaP;
	private String nomeTabellaC;

	private Tb_codici dbRow;

	private enum FieldType {
		tab, uni, marc21, mat, ult, ord;
	}


	public static final CodiceConfigVO build(Tb_codici dbRow)
			throws ValidationException {

		CodiceConfigVO config = new CodiceConfigVO(dbRow.getCd_flg1());

		config.dbRow = dbRow;

		config.flg2Config = CodiceFlagConfigVO.build(2, dbRow.getCd_flg2());
		config.flg3Config = CodiceFlagConfigVO.build(3, dbRow.getCd_flg3());
		config.flg4Config = CodiceFlagConfigVO.build(4, dbRow.getCd_flg4());
		config.flg5Config = CodiceFlagConfigVO.build(5, dbRow.getCd_flg5());
		config.flg6Config = CodiceFlagConfigVO.build(6, dbRow.getCd_flg6());
		config.flg7Config = CodiceFlagConfigVO.build(7, dbRow.getCd_flg7());
		config.flg8Config = CodiceFlagConfigVO.build(8, dbRow.getCd_flg8());
		config.flg9Config = CodiceFlagConfigVO.build(9, dbRow.getCd_flg9());
		config.flg10Config = CodiceFlagConfigVO.build(10, dbRow.getCd_flg10());
		config.flg11Config = CodiceFlagConfigVO.build(11, dbRow.getCd_flg11());

		config.cdTabella = trimAndSet(dbRow.getCd_tabella());
		config.descrizione = trimAndSet(dbRow.getDs_tabella());
		config.nomeTabella = trimAndSet(dbRow.getTp_tabella());

		return config;
	}

	public static final CodiceConfigVO build(TB_CODICI dbRow) throws ValidationException {
		Tb_codici tmp = new Tb_codici();
		ClonePool.copyCommonProperties(tmp, dbRow);
		return build(tmp);
	}

	private CodiceConfigVO(String config) throws ValidationException {
		if (isNull(config))
			throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);
		try {
			config = config.replaceAll("\\s+", "");
			String[] fields = config.split(FIELD_SEPARATOR);
//			if (fields.length != DICT_FIELDS_COUNT && fields.length != CROSS_FIELDS_COUNT)
//				throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);
			if (fields.length > DICT_FIELDS_COUNT)
				throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);

			for (String field : fields)
				configureField(field);

			if (tipoTabella == null)
				throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);

			if (tipoTabella == TabellaType.CROSS && fields.length != CROSS_FIELDS_COUNT)
				throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);

		} catch (ValidationException ve) {
			throw ve;
		} catch (Exception e) {
			throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);
		}

	}

	private void configureField(String field) throws ValidationException {
		if (isNull(field))
			throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);

		String[] tokens = field.split("=");
		if (tokens.length != 2)
			throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);

		String[] props = null;
		switch(FieldType.valueOf(tokens[0].toLowerCase())) {
		case tab:
			props = tokens[1].split(PROPS_SEPARATOR);
			tipoTabella = TabellaType.valueOf(props[0]);
			switch(tipoTabella) {
			case DICT:
				createDICT(tokens[1]);
				break;
			case CROSS:
				createCROSS(tokens[1]);
				return;
			}
			break;

		case uni:
			props = tokens[1].split(PROPS_SEPARATOR);
			if (props.length != 3)
				throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);

			cd_unimarc_length = Integer.valueOf(props[0]);
			if (cd_unimarc_length > MAX_CD_LENGTH)
				throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);
			cd_unimarc_type = CodiceValueType.valueOf(props[1]);
			cd_unimarc_allow_blank = (CodiceBlankType.valueOf(props[2]) == CodiceBlankType.BLANK);
			break;

		case marc21:
			props = tokens[1].split(PROPS_SEPARATOR);
			if (props.length != 3)
				throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);

			cd_marc21_length = Integer.valueOf(props[0]);
			if (cd_marc21_length > MAX_CD_LENGTH)
				throw new ValidationException(
						SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);
			cd_marc21_type = CodiceValueType.valueOf(props[1]);
			cd_marc21_allow_blank = (CodiceBlankType.valueOf(props[2]) == CodiceBlankType.BLANK);
			break;

		case mat:
			props = tokens[1].split(PROPS_SEPARATOR);
			if (props.length != 3)
				throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);

			tp_materiale_length = Integer.valueOf(props[0]);
			tp_materiale_type = CodiceValueType.valueOf(props[1]);
			tp_materiale_allow_blank = (CodiceBlankType.valueOf(props[2]) == CodiceBlankType.BLANK);
			break;

		case ult:
			ds_ulteriore_length = Integer.valueOf(tokens[1]);
			break;

		case ord:
			ordinamento = CodiciOrdinamentoType.fromString(tokens[1]);
			break;
		}

	}

	private void createCROSS(String expression) throws ValidationException {
		try {
			String[] tokens = expression.trim().split(PROPS_SEPARATOR);
			if (tokens.length != CROSS_TOKENS_COUNT)
				throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);
			tpTabellaP = CodiciType.fromString(tokens[1]);
			tpTabellaC = CodiciType.fromString(tokens[2]);
			permessi = CodiciPermessiType.valueOf(tokens[3]);

		} catch (Exception e) {
			throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);
		}
	}

	private void createDICT(String expression) throws ValidationException {

		String[] tokens = expression.trim().split(PROPS_SEPARATOR);
		if (tokens.length != 4)
			throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);
		length = Integer.valueOf(tokens[1]);
		if (length > MAX_CD_TAB_LENGTH)
			throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);
		used = (length > 0);
		type = CodiceValueType.valueOf(tokens[2]);
		permessi = CodiciPermessiType.valueOf(tokens[3]);
	}

	public boolean isUsed() {
		return used;
	}

	public TabellaType getTipoTabella() {
		return tipoTabella;
	}

	public int getLength() {
		return length;
	}

	public CodiceValueType getType() {
		return type;
	}

	public int getCd_unimarc_length() {
		return cd_unimarc_length;
	}

	public int getCd_marc21_length() {
		return cd_marc21_length;
	}

	public CodiceValueType getCd_unimarc_type() {
		return cd_unimarc_type;
	}

	public CodiceValueType getCd_marc21_type() {
		return cd_marc21_type;
	}

	public int getDs_ulteriore_length() {
		return ds_ulteriore_length;
	}

	public CodiciPermessiType getPermessi() {
		return permessi;
	}

	public boolean isCdUnimarcUsed() {
		return (cd_unimarc_length > 0);
	}

	public boolean isCdMarc21Used() {
		return (cd_marc21_length > 0);
	}

	public boolean isTpMaterialeUsed() {
		return (tp_materiale_length > 0);
	}

	public boolean isDsUlterioreUsed() {
		return (ds_ulteriore_length > 0);
	}

	public CodiceFlagConfigVO getFlg2Config() {
		return flg2Config;
	}

	public CodiceFlagConfigVO getFlg3Config() {
		return flg3Config;
	}

	public CodiceFlagConfigVO getFlg4Config() {
		return flg4Config;
	}

	public CodiceFlagConfigVO getFlg5Config() {
		return flg5Config;
	}

	public CodiceFlagConfigVO getFlg6Config() {
		return flg6Config;
	}

	public CodiceFlagConfigVO getFlg7Config() {
		return flg7Config;
	}

	public CodiceFlagConfigVO getFlg8Config() {
		return flg8Config;
	}

	public CodiceFlagConfigVO getFlg9Config() {
		return flg9Config;
	}

	public CodiceFlagConfigVO getFlg10Config() {
		return flg10Config;
	}

	public CodiceFlagConfigVO getFlg11Config() {
		return flg11Config;
	}

	public String getCdTabella() {
		return cdTabella;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getNomeTabella() {
		return nomeTabella;
	}

	public boolean isCd_unimarc_allow_blank() {
		return cd_unimarc_allow_blank;
	}

	public boolean isCd_marc21_allow_blank() {
		return cd_marc21_allow_blank;
	}

	public CodiciType getTpTabellaP() {
		return tpTabellaP;
	}

	public CodiciType getTpTabellaC() {
		return tpTabellaC;
	}

	public List<CodiceFlagConfigVO> getUsedFlags() {

		List<CodiceFlagConfigVO> flags = new ArrayList<CodiceFlagConfigVO>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields)
			if (field.getName().matches("flg\\d+Config")) {
				CodiceFlagConfigVO fc;
				try {
					fc = (CodiceFlagConfigVO) field.get(this);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				if (fc.isUsed())
					flags.add(fc);
			}

		return flags;
	}

	public void validate(CodiceVO codice) throws ValidationException {

		super.validate();
		if (codice == null)
			throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);

		if (tipoTabella == TabellaType.CROSS) {
			if (isNull(codice.getFlag1()) || isNull(codice.getFlag2()))
				throw new ValidationException(SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE);
			return;
		}

		String cdTabella = codice.getCdTabella();
		if (isNull(cdTabella))
			throw new ValidationException(SbnErrorTypes.AMM_CODICE_CAMPO_OBBLIGATORIO, "Codice");
		switch (type) {
		case ALPHA:
			if (isFilled(cdTabella) && !isAlphabetic(cdTabella))
				throw new ValidationException(SbnErrorTypes.AMM_CODICE_FORMATO_ERRATO, "Codice");
			break;
		case NUMERIC:
			if (isFilled(cdTabella) && !isNumeric(cdTabella))
				throw new ValidationException(SbnErrorTypes.AMM_CODICE_FORMATO_ERRATO, "Codice");
			break;
		case ALL:
		default:
			break;
		}

		if (length(cdTabella) > length)
			throw new ValidationException(SbnErrorTypes.AMM_CODICE_LUNGHEZZA_CODICE, "Codice");

		if (isNull(codice.getDescrizione()))
			throw new ValidationException(SbnErrorTypes.AMM_CODICE_CAMPO_OBBLIGATORIO, "Descrizione");

		//cd unimarc
		if (isCdUnimarcUsed()) {
			String cd_unimarc = codice.getCd_unimarc();
			if (isNull(cd_unimarc) && !cd_unimarc_allow_blank)
				throw new ValidationException(SbnErrorTypes.AMM_CODICE_CAMPO_OBBLIGATORIO, "Cod. Unimarc");

			if (length(cd_unimarc) > cd_unimarc_length)
				throw new ValidationException(SbnErrorTypes.AMM_CODICE_LUNGHEZZA_CODICE, "Cod. Unimarc");

			switch (cd_unimarc_type) {
			case ALPHA:
				if (isFilled(cd_unimarc) && !isAlphabetic(cd_unimarc))
					throw new ValidationException(SbnErrorTypes.AMM_CODICE_FORMATO_ERRATO, "Cod. Unimarc");
				break;
			case NUMERIC:
				if (isFilled(cd_unimarc) && !isNumeric(cd_unimarc))
					throw new ValidationException(SbnErrorTypes.AMM_CODICE_FORMATO_ERRATO, "Cod. Unimarc");
				break;
			case ALL:
			default:
				break;
			}
		}

		//cd marc21
		if (isCdMarc21Used()) {
			String cd_marc21 = codice.getCd_marc21();
			if (isNull(cd_marc21) && !cd_marc21_allow_blank)
				throw new ValidationException(SbnErrorTypes.AMM_CODICE_CAMPO_OBBLIGATORIO, "Cod. Marc21");

			if (length(cd_marc21) > cd_marc21_length)
				throw new ValidationException(SbnErrorTypes.AMM_CODICE_LUNGHEZZA_CODICE, "Cod. Marc21");

			switch (cd_marc21_type) {
			case ALPHA:
				if (isFilled(cd_marc21) && !isAlphabetic(cd_marc21))
					throw new ValidationException(SbnErrorTypes.AMM_CODICE_FORMATO_ERRATO, "Cod. Marc21");
				break;
			case NUMERIC:
				if (isFilled(cd_marc21) && !isNumeric(cd_marc21))
					throw new ValidationException(SbnErrorTypes.AMM_CODICE_FORMATO_ERRATO, "Cod. Marc21");
				break;
			case ALL:
			default:
				break;
			}
		}

		//tp materiale
		if (isTpMaterialeUsed()) {
			String materiale = codice.getMateriale();
			if (isNull(materiale) && !tp_materiale_allow_blank)
				throw new ValidationException(SbnErrorTypes.AMM_CODICE_CAMPO_OBBLIGATORIO, "Tipo Materiale");

			if (materiale.length() > 1)
				throw new ValidationException(SbnErrorTypes.AMM_CODICE_LUNGHEZZA_CODICE, "Tipo Materiale");

			switch (tp_materiale_type) {
			case ALPHA:
				if (isFilled(materiale) && !isAlphabetic(materiale))
					throw new ValidationException(SbnErrorTypes.AMM_CODICE_FORMATO_ERRATO, "Tipo Materiale");
				break;
			case NUMERIC:
				if (isFilled(materiale) && !isNumeric(materiale))
					throw new ValidationException(SbnErrorTypes.AMM_CODICE_FORMATO_ERRATO, "Tipo Materiale");
				break;
			case ALL:
			default:
				break;
			}
		}

		//ds ulteriore
		if (isDsUlterioreUsed()) {
			String ds_cdsbn_ulteriore = codice.getDs_cdsbn_ulteriore();
			if (length(ds_cdsbn_ulteriore) > ds_ulteriore_length)
				throw new ValidationException(SbnErrorTypes.AMM_CODICE_LUNGHEZZA_CODICE, "Descr. Ulteriore");
		}

		//flags
		List<CodiceFlagConfigVO> flags = getUsedFlags();
		if (!isFilled(flags))
			return; // non sono usati flag;

		for (CodiceFlagConfigVO flag : flags)
			flag.validate(codice.getFlag(flag.getFlg()));
	}

	public static synchronized String getDummyCode() {
		long lng = Long.valueOf(dummy.nextLong());
		String tmp = Long.toHexString(lng);
		return ValidazioneDati.fillRight(tmp, 'X', 20).substring(0, 10);
	}

	public Tb_codici getDbRow() {
		return dbRow;
	}

	public String getNomeTabellaP() {
		return nomeTabellaP;
	}

	public String getNomeTabellaC() {
		return nomeTabellaC;
	}

	public boolean isTp_materiale_allow_blank() {
		return tp_materiale_allow_blank;
	}

	public int getTp_materiale_length() {
		return tp_materiale_length;
	}

	 public static void main(String[] args) throws ValidationException {
		 CodiceConfigVO testd = new CodiceConfigVO(TEST_D);
		 System.out.println(testd);

		 CodiceConfigVO testc = new CodiceConfigVO(TEST_C);
		 System.out.println(testc);
	 }

	public CodiceValueType getTp_materiale_type() {
		return tp_materiale_type;
	}

	public CodiciOrdinamentoType getOrdinamento() {
		return ordinamento;
	}

	public String getTipoTabP() {
		return tpTabellaP.getTp_Tabella();
	}

	public String getTipoTabC() {
		return tpTabellaC.getTp_Tabella();
	}

}
