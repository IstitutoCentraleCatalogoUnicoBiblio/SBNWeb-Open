/**
 *
 */
package it.iccu.sbn.ejb.vo.gestionesemantica;

public enum FormaNomeType {

	// partenza-arrivo
	ACCETTATA_ACCETTATA			("A-A"),
	ACCETTATA_RINVIO			("A-R"),
	RINVIO_ACCETTATA			("R-A");

	private final String value;

	private FormaNomeType(String value) {
		this.value = value;
	}

	public static final FormaNomeType fromString(String value) {
		for (FormaNomeType type : FormaNomeType.values())
			if (type.value.equals(value))
				return type;
		return null;
	}

}