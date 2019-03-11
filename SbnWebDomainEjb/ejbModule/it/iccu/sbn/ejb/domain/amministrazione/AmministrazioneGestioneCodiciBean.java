/**
 *
 */
package it.iccu.sbn.ejb.domain.amministrazione;

import it.iccu.sbn.ejb.exception.ValidationException;
import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceConfigVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceFlagConfigVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceFlagValueType;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiceVO;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.CodiciPermessiType;
import it.iccu.sbn.ejb.vo.amministrazionesistema.codici.TabellaType;
import it.iccu.sbn.ejb.vo.common.CodiciType;
import it.iccu.sbn.ejb.vo.common.TB_CODICI;
import it.iccu.sbn.ejb.vo.gestionebibliografica.titolo.ComboCodDescVO;
import it.iccu.sbn.persistence.dao.amministrazione.CodiciDao;
import it.iccu.sbn.persistence.dao.common.DaoManager;
import it.iccu.sbn.persistence.dao.exception.DaoManagerException;
import it.iccu.sbn.polo.orm.amministrazione.Tb_codici;
import it.iccu.sbn.servizi.ProfilerManager;
import it.iccu.sbn.servizi.ProfilerManagerMBean;
import it.iccu.sbn.servizi.codici.CodiciProvider;
import it.iccu.sbn.servizi.ticket.TicketChecker;
import it.iccu.sbn.util.ConvertiVo.ConversioneHibernateVO;
import it.iccu.sbn.util.cloning.ClonePool;
import it.iccu.sbn.web.vo.SbnErrorTypes;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.transaction.UserTransaction;

import org.apache.log4j.Logger;


public abstract class AmministrazioneGestioneCodiciBean extends TicketChecker implements SessionBean {

	private static final long serialVersionUID = 8540398692909652725L;

	private static Logger log = Logger.getLogger(AmministrazioneGestioneCodiciBean.class);

	private SessionContext ctx;


	public void ejbCreate() {
		return;
	}

	public void setSessionContext(SessionContext ctx) throws EJBException, RemoteException {
		this.ctx = ctx;
		return;
	}


	public List<CodiceConfigVO> cercaConfigTabelleCodici(String user) throws DaoManagerException, ValidationException, EJBException {
		try {
			CodiciDao dao = new CodiciDao();
			boolean root = user.toUpperCase().trim().equals("ROOT");
			List<Tb_codici> elencoCodiciDB = dao.getConfigTabelleCodici(root);
			List<CodiceConfigVO> output = new ArrayList<CodiceConfigVO>(ValidazioneDati.size(elencoCodiciDB) + 1);
			for (Tb_codici codice  : elencoCodiciDB) {

				CodiceConfigVO configVO = null;
				try {
					configVO = CodiceConfigVO.build(codice);
					switch (configVO.getTipoTabella()) {
					case DICT:
						break;
					case CROSS:
						CodiciType tpTabellaP = configVO.getTpTabellaP();
						CodiciType tpTabellaC = configVO.getTpTabellaC();
						if (tpTabellaP == null || tpTabellaC == null) {
							log.error("Errore configurazione tabella codici '" + codice.getCd_tabella().trim() + "'");
							continue;
						}
						setField(configVO, "nomeTabellaP", dao.getConfigTabelleCodici(tpTabellaP.getTp_Tabella()).getDs_tabella());
						setField(configVO, "nomeTabellaC", dao.getConfigTabelleCodici(tpTabellaC.getTp_Tabella()).getDs_tabella());
					}
				} catch (ValidationException e) {
					if (e.getErrorCode() == SbnErrorTypes.AMM_CONFIGURAZIONE_CODICE) {
						log.error("Errore configurazione tabella codici '" + codice.getCd_tabella().trim() + "'");
						continue;
					}
					throw e;
				}

				//almaviva5_20100108 le tabelle hidden non sono gestibili
				if (configVO.getPermessi() != CodiciPermessiType.HIDDEN)
					output.add(configVO);
			}
			return output;
		}
		catch (DaoManagerException e) {
			log.error("", e);
			return null;
		}

	}

	private static final boolean setField(Serializable entity, String idName, String value) {
		try {
			Field field = entity.getClass().getDeclaredField(idName);
			field.setAccessible(true);
			field.set(entity, value);
			return true;

		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}

	public List<CodiceVO> cercaTabellaCodici(String cdTabella) throws DaoManagerException, EJBException {
		try {
			CodiciDao dao = new CodiciDao();
			Tb_codici config = dao.getConfigTabelleCodici(cdTabella);
			final CodiceConfigVO configVO = CodiceConfigVO.build(config);

			List<Tb_codici> elencoCodiciDB = dao.cercaTabellaCodici(cdTabella, configVO.getTipoTabella());
			List<CodiceVO> output = new ArrayList<CodiceVO>();

			switch (configVO.getTipoTabella()) {
			case DICT:
			for (Tb_codici codice : elencoCodiciDB) {
				CodiceVO codiceVO = ConversioneHibernateVO.toWeb().elementoTabellaCodici(codice);
				for (CodiceFlagConfigVO flagConfig : configVO.getUsedFlags()) {
					if (flagConfig.getType() != CodiceFlagValueType.LIST)
						continue;
					setDescrizioneFlag(flagConfig, codiceVO);
				}
				output.add(codiceVO);
			}
			break;
			case CROSS:
				for (Tb_codici codice : elencoCodiciDB) {
					CodiceVO codiceVO = ConversioneHibernateVO.toWeb().elementoTabellaCodici(codice);
					codiceVO.setDescrP(CodiciProvider.getDescrizioneCodiceSBN(configVO.getTpTabellaP(), codice.getCd_flg1()));
					codiceVO.setDescrC(CodiciProvider.getDescrizioneCodiceSBN(configVO.getTpTabellaC(), codice.getCd_flg2()));
					output.add(codiceVO);
				}
			}

			//ordinamento della tabella
			if (configVO.getTipoTabella() == TabellaType.DICT)
				Collections.sort(output, new Comparator<CodiceVO>() {

					public int compare(CodiceVO o1, CodiceVO o2) {
						String v1 = o1.getCdTabella();
						String v2 = o2.getCdTabella();

						try {
							switch (configVO.getType()) {
							case ALL:
							case ALPHA:
							default:
								return v1.compareToIgnoreCase(v2);

							case NUMERIC:
								int i1 = Integer.valueOf(v1);
								int i2 = Integer.valueOf(v2);
								return i1 - i2;
							}

						} catch (RuntimeException e) {
							log.error("", e);
							return 0;
						}
					}
				});

			return output;
		}
		catch (DaoManagerException e) {
			log.error("", e);
			return null;
		} catch (Exception e) {
			throw new EJBException(e);
		}

	}

	private void setDescrizioneFlag(CodiceFlagConfigVO config,	CodiceVO codice) {
		String value = codice.getFlag(config.getFlg());
		if (ValidazioneDati.strIsNull(value))
			return;
		for (ComboCodDescVO combo : config.getValues()) {
			if (!ValidazioneDati.equals(value, combo.getCodice()))
				continue;
			codice.setDescrizioneFlag(config.getFlg(), combo.getDescrizione());
		}

	}

	public boolean salvaTabellaCodici(CodiceVO codice, boolean validate) throws DaoManagerException, ValidationException, EJBException {
		try {
			CodiciDao dao = new CodiciDao();
			if (validate) {
				Tb_codici config = dao.getConfigTabelleCodici(codice.getNomeTabella());
				CodiceConfigVO configVO = CodiceConfigVO.build(config);
				configVO.validate(codice);
			}

			Tb_codici tb_codici = ConversioneHibernateVO.toHibernate().elementoTabellaCodici(codice);
			dao.salvaTabellaCodici(tb_codici);

		} catch (DaoManagerException e) {
			log.error("", e);
			return false;
		}

		try {
			ProfilerManagerMBean pm = ProfilerManager.getProfilerManagerInstance();
			pm.reloadCodici();

		} catch (Exception e) {
			log.error("Errore durante il refresh dei nuovi codici", e);
		}

		return true;
	}

	public boolean salvaTabellaCodici(TB_CODICI codice, boolean validate) throws DaoManagerException, ValidationException, EJBException {
		Tb_codici row = new Tb_codici();
		ClonePool.copyCommonProperties(row, codice);
		UserTransaction tx = ctx.getUserTransaction();

		try {
			DaoManager.begin(tx);
			return salvaTabellaCodici(ConversioneHibernateVO.toWeb().elementoTabellaCodici(row), validate);

		} catch (Exception e) {
			DaoManager.rollback(tx);
			throw new EJBException(e);

		} finally {
			try {
				DaoManager.commit(tx);
			} catch (Exception e) { }
		}

	}

	public boolean abilitaTabella(String ticket, CodiceConfigVO config,
			CodiciPermessiType permessi) throws DaoManagerException, EJBException {

		Tb_codici dbRow = config.getDbRow();
		CharSequence read = new StringBuilder("|READ");
		CharSequence write = new StringBuilder("|WRITE");

		String tbConfig = dbRow.getCd_flg1().trim();
		switch (permessi) {
		case READ:
			dbRow.setCd_flg1(tbConfig.replace(write, read));
			break;
		case WRITE:
			dbRow.setCd_flg1(tbConfig.replace(read, write));
			break;
		}

		log.debug("abilitaTabella(): tb_codici.cg_flg1 --> " + dbRow.getCd_flg1());
		CodiciDao dao = new CodiciDao();
		dao.salvaTabellaCodici(dbRow);

		return true;

	}

	public TB_CODICI caricaCodice(String cdTabella, CodiciType type) throws DaoManagerException, ValidationException, EJBException {
		UserTransaction tx = ctx.getUserTransaction();
		try {
			DaoManager.begin(tx);
			CodiciDao dao = new CodiciDao();
			Tb_codici codice = dao.getCodice(cdTabella, type);
			if (codice == null)
				return null;
			TB_CODICI web = new TB_CODICI();
			ClonePool.copyCommonProperties(web, codice);
			return web;

		} catch (Exception e) {
			DaoManager.rollback(tx);
			throw new EJBException(e);

		} finally {
			try {
				DaoManager.commit(tx);
			} catch (Exception e) {	}
		}
	}

	public void deleteTabellaCodici(String cdTabella, CodiciType type) throws DaoManagerException, ValidationException, EJBException {
		UserTransaction tx = ctx.getUserTransaction();
		try {
			DaoManager.begin(tx);
			CodiciDao dao = new CodiciDao();
			Tb_codici codice = dao.getCodice(cdTabella, type);
			if (codice == null)
				return;

			dao.getCurrentSession().delete(codice);

		} catch (Exception e) {
			DaoManager.rollback(tx);
			throw new EJBException(e);

		} finally {
			try {
				DaoManager.commit(tx);
			} catch (Exception e) {	}
		}
	}

}