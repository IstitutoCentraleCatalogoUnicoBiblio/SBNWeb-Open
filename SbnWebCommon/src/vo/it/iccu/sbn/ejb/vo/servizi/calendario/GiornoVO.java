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
package it.iccu.sbn.ejb.vo.servizi.calendario;

import it.iccu.sbn.ejb.vo.SerializableVO;
import it.iccu.sbn.ejb.vo.servizi.sale.PostoSalaVO;
import it.iccu.sbn.ejb.vo.servizi.sale.SalaVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GiornoVO extends SerializableVO {

	private static final long serialVersionUID = 7076027320825604121L;

	public static class SlotSala {
		private SalaVO sala;
		private List<SlotVO> slots = new ArrayList<SlotVO>(24);
		private List<PostoSalaVO> posti = new ArrayList<PostoSalaVO>();

		private int slotTotali;
		private int slotDisponibili;

		public SlotSala(SalaVO sala) {
			this.sala = sala;
		}

		public SalaVO getSala() {
			return sala;
		}

		public void setSala(SalaVO sala) {
			this.sala = sala;
		}

		public List<SlotVO> getSlots() {
			return slots;
		}

		public void setSlots(List<SlotVO> slots) {
			this.slots = slots;
		}

		public List<PostoSalaVO> getPosti() {
			return posti;
		}

		public void setPosti(List<PostoSalaVO> posti) {
			this.posti = posti;
		}

		public int getSlotTotali() {
			return slotTotali;
		}

		public void setSlotTotali(int slotTotali) {
			this.slotTotali = slotTotali;
		}

		public int getSlotDisponibili() {
			return slotDisponibili;
		}

		public void setSlotDisponibili(int slotDisponibili) {
			this.slotDisponibili = slotDisponibili;
		}

		public String getPrimoSlotDisponibile() {
			StringBuilder buf = new StringBuilder(32);
			for (SlotVO slot : slots) {
				if (!slot.isEmpty()) {
					buf.append(slot.getLabel());
					break;
				}
			}
			return buf.toString();
		}
	}

	public enum StatoGiorno {
		DISPONIBILE, NON_DISPONIBILE, CHIUSO, FESTIVO;
	}

	public enum Disponibilita {
		ALTA, BUONA, BASSA;
	}

	private Date date;
	private StatoGiorno stato = StatoGiorno.DISPONIBILE;
	private Disponibilita disponibilita = Disponibilita.ALTA;

	private List<SlotSala> slotSala = new ArrayList<GiornoVO.SlotSala>();

	public GiornoVO() {
		super();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public StatoGiorno getStato() {
		return stato;
	}

	public void setStato(StatoGiorno stato) {
		this.stato = stato;
	}

	public Disponibilita getDisponibilita() {
		return disponibilita;
	}

	public void setDisponibilita(Disponibilita disponibilita) {
		this.disponibilita = disponibilita;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((stato == null) ? 0 : stato.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GiornoVO other = (GiornoVO) obj;
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (stato != other.stato) {
			return false;
		}
		return true;
	}

	public List<SlotSala> getSlotSala() {
		return slotSala;
	}

	public void setSlotSala(List<SlotSala> slotSala) {
		this.slotSala = slotSala;
	}

	public int getSlotDisponibili() {
		int tot = 0;
		for (SlotSala ss : slotSala)
			tot += ss.slotDisponibili;

		return tot;
	}

}
