package it.iccu.sbn.vo.custom.documentofisico;

import it.iccu.sbn.ejb.utils.ValidazioneDati;

import java.util.List;

import org.hibernate.transform.ResultTransformer;

public class TbcInventarioKey {

	public static class Transformer implements ResultTransformer {

		private static final long serialVersionUID = 1L;

		public Object transformTuple(Object[] tuple, String[] alias) {
			return new TbcInventarioKey((String)tuple[0], (String)tuple[1], (String)tuple[2], (Integer)tuple[3]);
		}

		public List transformList(List list) {
			return list;
		}

	}

	private String cd_polo, cd_bib, cd_serie;
	private int cd_inven;

	public String getCd_polo() {
		return cd_polo;
	}

	public String getCd_bib() {
		return cd_bib;
	}

	public String getCd_serie() {
		return cd_serie;
	}

	public int getCd_inven() {
		return cd_inven;
	}

	private TbcInventarioKey(String cd_polo, String cd_bib, String cd_serie, int cd_inven) {
		super();
		this.cd_polo = cd_polo;
		this.cd_bib = cd_bib;
		this.cd_serie = cd_serie;
		this.cd_inven = cd_inven;
	}

}
