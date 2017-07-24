package application;

public class SFC {

	public static String EmiteCodigo(String nAutorizacion, String nFactura, String nit, String fechaTrans
			,String montoTrans,String llaveDosificacion){
		String cControl = "";
		String codigoControl = CodigoControl(nFactura, nit, fechaTrans, montoTrans, llaveDosificacion,
				nAutorizacion);
		cControl = Imprime(codigoControl);
		return cControl;
	}
	public static String CodigoControl(String nFactura, String nit,
			String fechaTrans, String montoTrans, String llaveDosificacion,
			String nAutorizacion) {

		long inFactura = 0;
		long init = 0;
		long ifechaTrans = 0;
		long imontoTrans = 0;
		long iauxTotal = 0;
		String auxTotal = "";
		String aux5Verhoeff = "";
		int[] cadena5v = new int[5];
		String[] cadenaLlave = new String[5];
		int c = 0;
		String arc4 = "";
		int st = 0;
		int sp[] = new int[5];
		int spTotal=0;
		String numBase64 = "";
		String codigoDeControl = "";

		for (int i = 0; i < 2; i++) {
			nFactura += Verhoeff(nFactura);
			nit += Verhoeff(nit);
			fechaTrans += Verhoeff(fechaTrans);
			montoTrans += Verhoeff(montoTrans);
		}

		inFactura = Long.valueOf(nFactura);
		init = Long.valueOf(nit);
		ifechaTrans = Long.valueOf(fechaTrans);
		imontoTrans = Long.valueOf(montoTrans);
		iauxTotal = inFactura + init + ifechaTrans + imontoTrans;
		auxTotal = Long.toString(iauxTotal);

		for (int i = 0; i < 5; i++) {
			aux5Verhoeff += Verhoeff(auxTotal);
			auxTotal += Verhoeff(auxTotal);
		}

		for (int i = 0; i < 5; i++) {
			cadena5v[i] = (Integer.parseInt(String.valueOf(aux5Verhoeff
					.charAt(i))) + 1);

		}

		c = 0;

		for (int i = 0; i < cadena5v.length; i++) {

			cadenaLlave[i] = "";
			for (int j = 0; j < cadena5v[i]; j++) {
				cadenaLlave[i] += String.valueOf(llaveDosificacion.charAt(c));
				c++;
			}
		}

		nAutorizacion += cadenaLlave[0];
		nFactura += cadenaLlave[1];
		nit += cadenaLlave[2];
		fechaTrans += cadenaLlave[3];
		montoTrans += cadenaLlave[4];
		arc4 = AllegedRC4(nAutorizacion + nFactura + nit + fechaTrans
				+ montoTrans, llaveDosificacion + aux5Verhoeff);
		for (int i = 0; i < arc4.length(); i++) {
			st += Integer.valueOf(arc4.charAt(i));
			int sumParcial = i % 5;
			switch (sumParcial) {
			case 0:
				sp[0] += Integer.valueOf(arc4.charAt(i));
				break;
			case 1:
				sp[1] += Integer.valueOf(arc4.charAt(i));
				break;
			case 2:
				sp[2] += Integer.valueOf(arc4.charAt(i));
				break;
			case 3:
				sp[3] += Integer.valueOf(arc4.charAt(i));
				break;
			case 4:
				sp[4] += Integer.valueOf(arc4.charAt(i));
				break;
			}
		}
		for(int i=0;i<cadena5v.length ;i++){
			sp[i]*=st;
			sp[i]/=cadena5v[i];
			spTotal += sp[i];
		}
		numBase64 = Base64(spTotal);
		codigoDeControl = AllegedRC4(numBase64, llaveDosificacion+aux5Verhoeff);
		return codigoDeControl;

	}

	public static int Verhoeff(String a) {
		String invertido;
		int inv[] = { 0, 4, 3, 2, 1, 5, 6, 7, 8, 9 };
		int check = 0;
		int[][] mul = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
				{ 1, 2, 3, 4, 0, 6, 7, 8, 9, 5 },
				{ 2, 3, 4, 0, 1, 7, 8, 9, 5, 6 },
				{ 3, 4, 0, 1, 2, 8, 9, 5, 6, 7 },
				{ 4, 0, 1, 2, 3, 9, 5, 6, 7, 8 },
				{ 5, 9, 8, 7, 6, 0, 4, 3, 2, 1 },
				{ 6, 5, 9, 8, 7, 1, 0, 4, 3, 2 },
				{ 7, 6, 5, 9, 8, 2, 1, 0, 4, 3 },
				{ 8, 7, 6, 5, 9, 3, 2, 1, 0, 4 },
				{ 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 } };

		int[][] per = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
				{ 1, 5, 7, 6, 2, 8, 3, 0, 9, 4 },
				{ 5, 8, 0, 3, 7, 9, 6, 1, 4, 2 },
				{ 8, 9, 1, 6, 0, 4, 3, 5, 2, 7 },
				{ 9, 4, 5, 3, 1, 2, 6, 8, 7, 0 },
				{ 4, 2, 8, 6, 5, 7, 3, 9, 0, 1 },
				{ 2, 7, 9, 3, 8, 0, 6, 4, 1, 5 },
				{ 7, 0, 4, 6, 9, 1, 3, 2, 5, 8 } };

		invertido = InvierteNumero(a);
		for (int i = 0; i < invertido.length(); i++) {
			int aux1 = Integer.parseInt(String.valueOf(invertido.charAt(i)));
			int aux2 = ((i + 1) % 8);

			int aux3 = per[aux2][aux1];
			check = mul[check][aux3];

		}
		return inv[check];
	}

	public static String InvierteNumero(String n) {
		String aux = "";
		for (int i = (n.length() - 1); i >= 0; i--) {
			aux += n.charAt(i);
		}
		return aux;
	}

	public static String AllegedRC4(String mensaje, String key) {
		int[] state = new int[256];
		int x = 0;
		int y = 0;
		int index1 = 0;
		int index2 = 0;
		int nMen = 0;
		String mensajeCifrado = "";
		for (int i = 0; i < 256; i++) {
			state[i] = i;
		}
		for (int i = 0; i < 256; i++) {
			index2 = (Integer.valueOf(key.charAt(index1)) + state[i] + index2) % 256;
			// intercambia valores
			int aux = state[i];
			state[i] = state[index2];
			state[index2] = aux;
			// actualiza index1
			index1 = (index1 + 1) % key.length();
		}
		for (int i = 0; i < mensaje.length(); i++) {
			x = (x + 1) % 256;
			y = (state[x] + y) % 256;
			// intercambia valores
			int aux = state[x];
			state[x] = state[y];
			state[y] = aux;
			nMen = Integer.valueOf(mensaje.charAt(i))
					^ state[(state[x] + state[y]) % 256];
			mensajeCifrado += RellenaCero(Integer.toHexString(nMen));
		}
		return mensajeCifrado.toUpperCase();
	}

	public static String RellenaCero(String d) {
		if (d.length() == 1) {
			d += "0";
			d = InvierteNumero(d);
		}
		return d;
	}

	public static String Base64 (int numero){
		int cociente = 1;
		int resto = 0;
		String palabra = "";
		char diccionario [] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
				'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
				'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
				'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
				'y', 'z', '+', '/'};
		while(cociente>0){
			cociente = numero / 64;
			resto = numero % 64;
			palabra = diccionario[resto] + palabra;
			numero = cociente;
		}
		
		return palabra;
	}

	public static String Imprime (String codigo){
		String codigoAux = "";
		int c = 0;
		for (int i = 0; i < codigo.length(); i++) {
			codigoAux += codigo.charAt(i);
			if (codigo.length() == 10) {
				if (((i + 1) % 2 == 0) && (c < 4)) {
					codigoAux += "-";
					c++;
				}
			}
			if (codigo.length() == 8) {
				if (((i + 1) % 2 == 0) && (c < 3)) {
					codigoAux += "-";
					c++;
				}
			}

		}
		return codigoAux;
	}

}
