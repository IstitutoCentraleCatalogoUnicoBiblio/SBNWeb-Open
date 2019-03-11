// ** I18N
Calendar._DN = new Array
("Domenica",
 "Luned&igrave;",
 "Marted&igrave;",
 "Mercoled&igrave;",
 "Gioved&igrave;",
 "Venerd&igrave;",
 "Sabato",
 "Domenica");

Calendar._SDN = new Array
("Dom",
 "Lun",
 "Mar",
 "Mer",
 "Gio",
 "Ven",
 "Sab",
 "Dom");

Calendar._MN = new Array
("Gennaio",
 "Febbraio",
 "Marzo",
 "Aprile",
 "Maggio",
 "Giugno",
 "Luglio",
 "Agosto",
 "Settembre",
 "Ottobre",
 "Novembre",
 "Dicembre");

// short month names
Calendar._SMN = new Array
("Gen",
 "Feb",
 "Mar",
 "Apr",
 "Mag",
 "Giu",
 "Lug",
 "Ago",
 "Set",
 "Ott",
 "Nov",
 "Dic");

// tooltips
Calendar._TT = {};
Calendar._TT["INFO"] = "a proposito del calendario";

Calendar._TT["ABOUT"] =
"Selezione della data:\n" +
"- Usa i bottoni \xab, \xbb per selezionare l'anno\n" +
"- Usa i bottoni " + String.fromCharCode(0x2039) + ", " + String.fromCharCode(0x203a) + " per selezionare il mese\n" +
"- Utilizza il mouse per una selezione rapida.";
Calendar._TT["ABOUT_TIME"] = "\n\n" +
"selezione dell'ora:\n" +
"- Clicca sull'ora visualizzata per aumentarla\n" +
"- o Shift-click per diminuirla\n" +
"- o click a trascina per la selezione rapida.";

//Calendar._TT["TOGGLE"] = "Modifica il primo giorno della settimana";
Calendar._TT["PREV_YEAR"] = "Anno prec. (tieni premuto per menu)";
Calendar._TT["PREV_MONTH"] = "Mese prec. (tieni premuto per menu)";
Calendar._TT["GO_TODAY"] = "Vai a oggi";
Calendar._TT["NEXT_MONTH"] = "Mese succ. (tieni premuto per menu)";
Calendar._TT["NEXT_YEAR"] = "Anno succ. (tieni premuto per menu)";
Calendar._TT["SEL_DATE"] = "Seleziona data";
Calendar._TT["DRAG_TO_MOVE"] = "Trascina per spostare";
Calendar._TT["PART_TODAY"] = " (oggi)";
//Calendar._TT["MON_FIRST"] = "Parti da luned&igrave;";
//Calendar._TT["SUN_FIRST"] = "Parti da domenica";
Calendar._TT["DAY_FIRST"] = "Parti da %s";
Calendar._TT["WEEKEND"] = "0,6";

Calendar._TT["CLOSE"] = "Chiudi";
Calendar._TT["TODAY"] = "Oggi";
Calendar._TT["TIME_PART"] = "(Shift-)Click o trascina per cambiare valore";

// date formats
Calendar._TT["DEF_DATE_FORMAT"] = "%d-%m-%Y";
Calendar._TT["TT_DATE_FORMAT"] = "%a, %e %b ";

Calendar._TT["WK"] = "Set";
