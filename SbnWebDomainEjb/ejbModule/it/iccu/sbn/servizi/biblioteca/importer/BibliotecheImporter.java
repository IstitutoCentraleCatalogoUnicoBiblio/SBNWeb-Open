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
package it.iccu.sbn.servizi.biblioteca.importer;

import it.iccu.sbn.ejb.utils.ValidazioneDati;
import it.iccu.sbn.ejb.vo.amministrazionesistema.BibliotecaVO;
import it.iccu.sbn.util.Isil;
import it.iccu.sbn.vo.custom.amministrazione.biblioteca.json.Biblioteca;
import it.iccu.sbn.vo.custom.amministrazione.biblioteca.json.Biblioteche;
import it.iccu.sbn.vo.custom.amministrazione.biblioteca.json.CodiciIdentificativi;
import it.iccu.sbn.vo.custom.amministrazione.biblioteca.json.Indirizzo;

import org.apache.log4j.Logger;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

import static it.iccu.sbn.ejb.utils.ValidazioneDati.coalesce;
import static it.iccu.sbn.ejb.utils.ValidazioneDati.trunc;

public class BibliotecheImporter {

	static Logger log = Logger.getLogger(BibliotecheImporter.class);

	private JSONAnagrafeBinder binder = new JSONAnagrafeBinderImpl();

	private Biblioteche biblioteche;

	public int execute() {
		log.debug("--------------BIBLIOTECHE IMPORTER STARTED-------------");
		biblioteche = binder.bind();
		int size = ValidazioneDati.size(biblioteche.getBiblioteche());
		log.debug("Biblioteche lette dal json: " + size);

		return size;
	}

	/**
	 * Cerca la biblioteca per codice univoco {@code isil}
	 * @param isil codice anagrafe univoco
	 * @return un {@code Optional} che contiene la biblioteca trovata
	 */
	public Optional<Biblioteca> find(final String isil) {
		Optional<Biblioteca> bib = Stream.of(biblioteche.getBiblioteche()).filter(new Predicate<Biblioteca>() {
			public boolean test(Biblioteca bib) {
				CodiciIdentificativi codici = bib.getCodici_identificativi();
				return codici != null && codici.isSBN() && ValidazioneDati.equals(codici.getIsil(), isil);
			}
		}).findSingle();

		return bib;
	}

	public BibliotecaVO merge(BibliotecaVO b, Biblioteca ab) {

		CodiciIdentificativi codici = ab.getCodici_identificativi();
		if (codici != null) {
			//codici sbn
			if (codici.isSBN()) {
				b.setCod_polo(codici.getCodPolo());
				b.setCod_bib(codici.getCodBib());
			}

			//codice isil
			Isil isil = Isil.parse(codici.getIsil());
			if (isil.withPaese()) {
				b.setPaese(isil.getPaese());
				b.setCd_ana_biblioteca(isil.getSuffisso());
			} else {
				//senza codice
				b.setPaese("UN");
				b.setCd_ana_biblioteca(isil.getSuffisso());
			}
		}

		b.setUnit_org(coalesce(b.getUnit_org(), "n\\a"));
		b.setIndirizzo(coalesce(b.getIndirizzo(), "n\\a"));
		b.setCap(coalesce(b.getCap(), "00000"));
		b.setProvincia(coalesce(b.getProvincia(), "EE"));
		b.setTipo_biblioteca("Z");
		b.setLocalita(coalesce(b.getLocalita(), "n\\a"));
		b.setCod_bib_cs("");
		b.setChiave_bib(" ");
		b.setChiave_ente(" ");

		Indirizzo loc = ab.getLocation();
		if (loc != null) {
			b.setIndirizzo(trunc(loc.getIndirizzo(), 80));
			b.setCap(loc.getCap());
			//b.setProvincia(loc.getProvincia().getNome());
			b.setLocalita(loc.getFrazione());
		}

		//List<Contatto> contatti = ab.getContatti();

		b.setNom_biblioteca(trunc(ab.getDenominazioni().getUfficiale(), 80));

		return b;
	}

}
