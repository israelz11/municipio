/**
 * @author Lsc. Mauricio Hernandez Leon.
 * @version 1.0
 *
 */
package mx.gob.municipio.centro.view.componentes;

/**
 * modificado mauricio Hernandez Leon. Clase estatica para convertir cantidades a letras
 * Permite especificar la cantidad de renglones que se desee con un largo
 * especifico para cada uno separando en silabas las palabras que no caben en el
 * renglon Permite incluir hasta 9 decimales traducirlos a letras siempre que
 * sean hasta tres decimales Permite expresar el valor 1 de las unidades enteras
 * como "UNO" , "UNA" o "UN" segun se fije el parametro genero_unidad en 0,1 o 2
 * Retorna un array de String igual a la cantidad de renglones especificados
 * permite agregar texto : a) al principio ( prefijo_inicio ) b) despues de la
 * parte entera ( sufijo_enteros ) c) antes de la parte decimal (
 * prefijo_decimal ) d) despues de la parte decimal ( sufijo_decimal ) e) al
 * final ( sufijo_final ) ( dentro de cada uno de estos textos , el caracter '|'
 * se usa para separar en silabas) ( si el texto comienza con un caracter '|'
 * indica que no se debe dejar espacio entre la palabra anterior y este texto .
 * Por ejemplo para agregar "/100.-" a continuacion de los decimales expresados
 * en numeros ( 50/100.- , 950/1000 , 567899/1000000 , etc. ) El valor maximo a
 * traducir : usando el metodo getTexto(String cantidad) =
 * 999999999999999.999999999 usando el metodo getTexto(double cantidad) =
 * 9999999999999999.899999 ( este metodo puede variar la cantidad de decimales
 * si el valor es grande , no asi el metodo anterior. Tiene metodos set... para
 * cada uno de los parametros
 */
public class RMCantidadEnLetras {

	public RMCantidadEnLetras() {
	}

	private String[] xDos = new String[] { "U|NO", "UN", "DOS", "TRES",
			"CUA|TRO", "CIN|CO", "SEIS", "SIE|TE", "O|CHO", "NUE|VE", "DIEZ",
			"ON|CE", "DO|CE", "TRE|CE", "CA|TOR|CE", "QUIN|CE", "DIE|CI|SEIS",
			"DIE|CI|SIE|TE", "DIE|CI|O|CHO", "DIE|CI|NUE|VE", "VEIN|TE",
			"VEIN|TI|UN", "VEIN|TI|DOS", "VEIN|TI|TRES", "VEIN|TI|CUA|TRO",
			"VEIN|TI|CIN|CO", "VEIN|TI|SEIS", "VEINTI|SIE|TE", "VEIN|TI|O|CHO",
			"VEIN|TI|NUE|VE" };

	private String[] xUno = new String[] { "CIEN", "CIEN|TO", "DOS|CIEN|TOS",
			"TRES|CIEN|TOS", "CUA|TRO|CIEN|TOS", "QUI|NIEN|TOS",
			"SEIS|CIEN|TOS", "SE|TE|CIEN|TOS", "O|CHO|CIEN|TOS",
			"NO|VE|CIEN|TOS" };

	private String[] xDiez = new String[] { "VEIN|TI|U|NO", "", "", "TREIN|TA",
			"CUA|REN|TA", "CIN|CUEN|TA", "SE|SEN|TA", "SE|TEN|TA", "O|CHEN|TA",
			"NO|VEN|TA" };

	private String[] xEsp = new String[] { "Y", "MIL", "MI|LLON", "MI|LLO|NES",
			"BI|LLON", "BI|LLO|NES", "DE" };
	private String prefijo_inicio = "", sufijo_enteros = "PE|SOS",
			prefijo_decimales = " ", sufijo_decimales = "|/100.-",
			sufijo_final = "", palabra_cero = "CE|RO";

	private char caracter_proteccion = 42;

	private int[] renglones = new int[] { 300 };

	private char[][] datos;

	private double divisor, xtres;

	private int tres, dos, uno, paso, decena, unidad, haymillones,
			cant_decimales = 2, genero_unidad = 0, renglon, ajuste, posicion,
			posicion_corte, silaba, decimales;
	private Integer importeEntero;

	private boolean traduce_decimales = false;

	public void setRenglones(int[] xrenglones) {
		renglones = xrenglones;
	}

	public void setGeneroUnidad(int xgenero_unidad) {
		genero_unidad = xgenero_unidad;
	}

	public void setCantidadDecimales(int xcant_decimales) {
		cant_decimales = xcant_decimales;
	}

	public void setPrefijoInicio(String xprefijo_inicio) {
		prefijo_inicio = xprefijo_inicio;
	}

	public void setSufijoFinal(String xsufijo_final) {
		sufijo_final = xsufijo_final;
	}

	public void setSufijoEnteros(String xsufijo_enteros) {
		sufijo_enteros = xsufijo_enteros;
	}

	public void setPrefijoDecimales(String xprefijo_decimales) {
		prefijo_decimales = xprefijo_decimales;
	}

	public void setSufijoDecimales(String xsufijo_decimales) {
		sufijo_decimales = xsufijo_decimales;
	}

	public void setCaracterProteccion(char xcaracter_proteccion) {
		caracter_proteccion = xcaracter_proteccion;
	}

	public void setTraduceDecimales(boolean xtraduce_decimales) {
		traduce_decimales = xtraduce_decimales;
	}

	public void setPalabraCero(String xpalabra_cero) {
		palabra_cero = xpalabra_cero;
	}

	private String[] getTexto(Double importe, int qdecimales) {

		importeEntero = importe.intValue();
		decimales = qdecimales;

		divisor = 1.00E12;

		haymillones = 0;

		if (cant_decimales > 3)
			traduce_decimales = false;

		ajuste = 10;

		for (int i = 1; i < cant_decimales; i++) {
			ajuste = ajuste * 10;
		}

		if (cant_decimales == 0)
			ajuste = 0;

		datos = new char[renglones.length][renglones[0]];

		for (int x = 0; x < renglones.length; x++) {
			datos[x] = new char[renglones[x]];
			for (int y = 0; y < renglones[x]; y++) {
				datos[x][y] = caracter_proteccion;
			}
		}

		xDos[0] = "U|NO";
		xDiez[0] = "VEIN|TI|UNO";
		if (genero_unidad == 2) {
			xDos[0] = "UN";
			xDiez[0] = "VEIN|TI|UN";
		}
		if (genero_unidad == 1) {
			xDos[0] = "U|NA";
			xDiez[0] = "VEIN|TI|U|NA";
		}

		renglon = posicion = 0;

		for (paso = 0; paso < 5; paso++) {
			xtres = (importe / divisor);
			tres = (int) xtres;
			importe = importe - (tres * divisor);
			divisor = divisor / 1000;
			if (tres > 0)
				traducir(tres);
		}
		// estas dos siguientes lineas es para la configuracion de los millones
		int residuo = importeEntero % 1000000;
		if (residuo == 0)
			pasarTexto(xEsp[6]);

		if ((palabra_cero.length() > 0) && renglon == 0 && posicion == 0) {
			if (prefijo_inicio.length() > 0)
				pasarTexto(prefijo_inicio);
			pasarTexto(palabra_cero);
		}

		if ((sufijo_enteros.length() > 0)
				&& ((renglon > 0) || (posicion > prefijo_inicio.length())))
			pasarTexto(sufijo_enteros);

		tres = (int) (Math.rint(importe * ajuste));

		if (decimales > 0)
			tres = decimales;

		if (tres > 0 && (prefijo_decimales.length() > 0)
				&& (renglon != 0 || posicion != 0))
			pasarTexto(prefijo_decimales);

		if (tres > 0 && traduce_decimales) {
			paso = 5;
			traducir(tres);
		}

		if (tres >= 1 && tres <= 9 && !traduce_decimales)
			pasarTexto(("0" + tres + ""));
		else if (tres > 0 && !traduce_decimales)
			pasarTexto(("" + tres + ""));
		else if (tres == 0 && !traduce_decimales)
			pasarTexto(("00"));
		if (tres > 0 && (sufijo_decimales.length() > 0))
			pasarTexto(sufijo_decimales);
		else if (tres == 0 && (sufijo_decimales.length() > 0))
			pasarTexto(sufijo_decimales);
		if (sufijo_final.length() > 0)
			pasarTexto(sufijo_final);

		String[] texto = new String[datos.length];

		for (int i = 0; i < datos.length; i++) {
			texto[i] = new String(datos[i]).trim();
		}

		return texto;
	}

	private void iniciarSilaba() {

		posicion_corte = posicion;
		silaba = 0;
	}

	private void paseCaracter(char caracter) {
		datos[renglon][posicion] = caracter;
		silaba++;
		posicion++;
	}

	private void sumeRenglon() {
		renglon++;
		posicion = 0;
		iniciarSilaba();
	}

	private void pasarTexto(String palabra) {

		char[] desglose = palabra.toCharArray();

		if (posicion > 0 && (posicion < (renglones[renglon] - 1))
				&& (desglose[0] != 124)) {
			datos[renglon][posicion] = 32;
			posicion++;
		} else

		if (posicion > 0 && (posicion == (renglones[renglon] - 1))
				&& (desglose[0] != 124)) {
			datos[renglon][posicion] = 32;
			sumeRenglon();
		}

		iniciarSilaba();

		for (int i = 0; i < desglose.length; i++) {

			if (desglose[i] == 124 && i > 0
					&& (posicion == (renglones[renglon] - 1))) {
				datos[renglon][posicion] = 45;
				sumeRenglon();
			} else

			if (desglose[i] == 124 && i > 0
					&& (posicion < (renglones[renglon] - 1))) {
				iniciarSilaba();
			} else

			if (desglose[i] != 124 && (posicion < (renglones[renglon] - 1))) {
				paseCaracter(desglose[i]);
			} else

			if ((desglose[i] != 124) && (posicion == (renglones[renglon] - 1))
					&& (i == (desglose.length - 1))) {
				paseCaracter(desglose[i]);
				sumeRenglon();
			} else

			if (desglose[i] != 124 && (posicion == (renglones[renglon] - 1))
					&& (i < (desglose.length - 1))) {

				posicion = posicion_corte;
				datos[renglon][posicion] = 45;
				posicion++;

				if (posicion < renglones[renglon]) {
					// De que sirve asignarle posicion a posicion?
					for (posicion = posicion; posicion < renglones[renglon]; posicion++) {
						datos[renglon][posicion] = 45;
					}
				}
				int xsilaba = silaba;
				sumeRenglon();

				for (int z = (i - xsilaba); z < (i + 1); z++) {
					datos[renglon][posicion] = desglose[z];
					posicion++;
				}
			}
		}
	}

	private void traducir(int mil) {
		if (renglon == 0 && posicion == 0 && prefijo_inicio.length() > 0)
			pasarTexto(prefijo_inicio);
		uno = mil / 100;
		dos = mil - (uno * 100);
		decena = dos / 10;
		unidad = dos - (decena * 10);
		if (mil == 100)
			pasarTexto(xUno[0]);
		else if (uno > 0)
			pasarTexto(xUno[uno]);
		//
		boolean entro = false;
		if ((dos >= 1 && dos < 30) && (paso != 4 || dos != 21)) {
			pasarTexto(xDos[dos]);
			if (dos == 1)
				entro = true;
		}
		if (paso == 4 && dos == 21)
			pasarTexto(xDiez[0]);
		//
		if (paso == 4 && dos == 1 && entro == false)
			pasarTexto(xDos[0]);

		if (dos > 29) {
			pasarTexto(xDiez[decena]);
			if (unidad > 0) {
				pasarTexto(xEsp[0]);
				if (paso != 4 || unidad != 1)
					pasarTexto(xDos[unidad]);
				if (paso == 4 && unidad == 1)
					pasarTexto(xDos[0]);
			}
		}
		if (paso == 0 && mil == 1) {
			pasarTexto(xEsp[4]);
		}
		if (paso == 0 && mil > 1) {
			pasarTexto(xEsp[5]);
		}
		if ((paso == 2 && mil > 1) || haymillones == 1) {
			pasarTexto(xEsp[3]);
			haymillones = 0;
		}
		if (paso == 1 && mil > 0) {
			pasarTexto(xEsp[1]);
			haymillones = 1;
		}

		if ((paso == 2 && mil == 1 && haymillones == 0)) {
			pasarTexto(xEsp[2]);
		}
		if (paso == 3 && mil > 0) {
			pasarTexto(xEsp[1]);
		}

	}

	public String[] convertir(String cantidad) {

		String parte_entera = ((cantidad.substring(0, cantidad.indexOf('.'))) + ".0");
		cantidad = cantidad.substring(cantidad.indexOf('.') + 1);
		if (cantidad.length() > cant_decimales)
			cantidad = cantidad.substring(0, cant_decimales);
		if (cantidad.length() < cant_decimales) {
			for (int i = cantidad.length(); i < cant_decimales; i++) {
				cantidad = cantidad + "0";
			}
		}

		return getTexto(Double.parseDouble(parte_entera), Integer
				.parseInt(cantidad));
	}

	public String[] convertir(double cantidad) {

		return getTexto(cantidad, 0);
	}

}
