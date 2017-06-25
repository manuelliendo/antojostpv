package application;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.imageio.ImageIO;

import org.omg.CORBA.IMP_LIMIT;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;

/*ESTA CLASE GENERA LA FACTURA EN PDF*/

public class EmisionFactura {

	static ObservableList<String> lista = FXCollections.observableArrayList();
	static ObservableList<ProductoSimple> orden = FXCollections.observableArrayList();

	public static void Emitir(String nitCliente, String razonSocial,
			String total, String montoPagado, String cambio, String cajero)
			throws WriterException, IOException, ClassNotFoundException {
		DateTimeFormatter fecha = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate ld = LocalDate.now();
		readFacturaConfig();
		String codControl = CodigoControl(lista.get(9), lista.get(4),
				fecha.format(ld), total, lista.get(11), lista.get(6));
		ObtenerQR(lista.get(4), lista.get(9), lista.get(6), total, total,
				codControl, nitCliente, "0", "0", "0", "0");
		readOrden();
		generaFactura(lista.get(4), lista.get(9), lista.get(6), lista.get(8),
				nitCliente, razonSocial, codControl, cajero, lista.get(3),
				total, montoPagado, cambio,lista.get(13));
	}

	/* GENERAR CODIGO DE CONTROL */

	public static String CodigoControl(String nFactura, String nit,
			String fechaTrans, String montoTrans, String llaveDosificacion,
			String nAutorizacion) {
		try {
			float mTA = (Float.parseFloat(montoTrans));
			int montoTotalAux = (int) Math.round(mTA);
			montoTrans = String.valueOf(montoTotalAux);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		int spTotal = 0;
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
		for (int i = 0; i < cadena5v.length; i++) {
			sp[i] *= st;
			sp[i] /= cadena5v[i];
			spTotal += sp[i];
		}
		numBase64 = Base64(spTotal);
		codigoDeControl = AllegedRC4(numBase64, llaveDosificacion
				+ aux5Verhoeff);
		return Imprime(codigoDeControl);

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

	public static String Base64(int numero) {
		int cociente = 1;
		int resto = 0;
		String palabra = "";
		char diccionario[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
				'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
				'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
				'v', 'w', 'x', 'y', 'z', '+', '/' };
		while (cociente > 0) {
			cociente = numero / 64;
			resto = numero % 64;
			palabra = diccionario[resto] + palabra;
			numero = cociente;
		}

		return palabra;
	}

	public static String Imprime(String codigo) {
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

	/* FIN GENERAR CODIGO DE CONTROL */

	/* GENERAR QR */
	public static void ObtenerQR(String nit, String nFactura,
			String nAutorizacion, String total, String importeBase,
			String codigoControl, String nitComprador, String importe,
			String importeVentas, String importeNoSujeto, String descuentos)
			throws WriterException, IOException {

		DateTimeFormatter fecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate ld = LocalDate.now();
		String qrCodeText = nit + "|" + nFactura + "|" + nAutorizacion + "|"
				+ fecha.format(ld) + "|" + total + "|" + importeBase + "|"
				+ codigoControl + "|" + nitComprador + "|" + importe + "|"
				+ importeVentas + "|" + importeNoSujeto + "|" + descuentos;
		String workingDir = System.getProperty("user.dir");
		String filePath = workingDir + "\\qr.png";
		int size = 50;
		String fileType = "png";
		File qrFile = new File(filePath);
		createQRImage(qrFile, qrCodeText, size, fileType);
	}

	private static void createQRImage(File qrFile, String qrCodeText, int size,
			String fileType) throws WriterException, IOException {
		// Create the ByteMatrix for the QR-Code that encodes the given String
		Hashtable hintMap = new Hashtable();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText,
				BarcodeFormat.QR_CODE, size, size, hintMap);
		// Make the BufferedImage that are to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,
				BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// Paint and save the image using the ByteMatrix
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		ImageIO.write(image, fileType, qrFile);
	}

	/* FIN GENERAR QR */

	/* GENERAR FACTURA PDF */
	public static void generaFactura(String nit, String nFactura,
			String nAutorizacion, String actividad, String nitCliente,
			String razonSocial, String codigoControl, String cajero,
			String telefono, String total, String montoPagado, String cambio,String extra) {
		try {
			DateTimeFormatter fecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			DateTimeFormatter hora = DateTimeFormatter.ofPattern("HH:mm:ss");
			Rectangle size = new Rectangle(77f, 230f);
			Document doc = new Document(size, 5f, 5f, 20f, 1f);
			PdfWriter.getInstance(doc, new FileOutputStream("prueba.pdf"));
			doc.open();
			Paragraph par = new Paragraph();
			Paragraph par2 = new Paragraph();
			Paragraph par3 = new Paragraph();
			par.setFont(new Font(FontFamily.TIMES_ROMAN, 3f));
			par2.setFont(new Font(FontFamily.TIMES_ROMAN, 3f));
			par3.setFont(new Font(FontFamily.TIMES_ROMAN, 3f));
			par.setAlignment(par.ALIGN_CENTER);
			par.setLeading(3);
			par2.setLeading(3);
			par3.setLeading(3);
			par2.setTabSettings(new TabSettings(20f));
			par.add(lista.get(0) + "\n");
			par.add("SUCURSAL" + lista.get(1) + "\n");
			par.add(lista.get(2) + "\n");
			par.add("TELEFONO: " + lista.get(3) + "\n");
			par.add("LA PAZ - BOLIVIA\n");
			par.add("\n");
			par.setFont(new Font(FontFamily.TIMES_ROMAN, 4f));
			par.add("FACTURA\n");
			par.setFont(new Font(FontFamily.TIMES_ROMAN, 3f));
			par.add("------------------------------------------------------------------\n");
			par.add("NIT: " + nit + "\n");
			par.add("Factura No: " + nFactura + "\n");
			par.add("Autorizacion No: " + nAutorizacion + "\n");
			par.add("------------------------------------------------------------------\n");
			par.add("Actividad económica: " + actividad + "\n\n");
			LocalDate ld = LocalDate.now();
			par2.setAlignment(par.ALIGN_LEFT);
			par2.add("Fecha: " + fecha.format(ld) + "         ");
			LocalDateTime ldt = LocalDateTime.now();
			par2.add(Chunk.TABBING);
			par2.add(" Hora: " + hora.format(ldt) + "\n");
			par2.add("NIT: " + nitCliente + "\n");
			par2.add("SEÑOR(ES): " + razonSocial + "\n");
			par2.add("----------------------------------------------------------------\n");
			
			par2.add("Cant");
			par2.add(Chunk.TABBING);
			par2.add("Detalle");
			par2.add(Chunk.TABBING);
			par2.add("P Unit");
			par2.add(Chunk.TABBING);
			par2.add("Total");
			par2.add("\n");
			/* TODO : AUTOCOMPLETE ORDEN */
			for(int i=0;i<orden.size();i++){
				par2.add(orden.get(i).getCantidad().toString());
				par2.add(Chunk.TABBING);
				par2.add(orden.get(i).getNombre());
				par2.add(Chunk.TABBING);
				par2.add(orden.get(i).getPrecio().toString());
				par2.add(Chunk.TABBING);
				par2.add(orden.get(i).getPrecioTotal().toString());
				par2.add("\n");	
			}
			
			par2.add("----------------------------------------------------------------\n");
			/* FIN AUTOCOMPLETE */
			par2.add(Chunk.TABBING);
			par2.add("IMPORTE TOTAL: Bs.");
			par2.add(Chunk.TABBING);
			par2.add(total);
			par2.add("\n");
			par2.add(Chunk.TABBING);
			par2.add("TOTAL FACTURA: Bs.");
			par2.add(Chunk.TABBING);
			par2.add(total);
			par2.add("\n");
			par2.add(Chunk.TABBING);
			par2.add("EFECTIVO: Bs.");
			par2.add(Chunk.TABBING);
			par2.add(montoPagado);
			par2.add("\n");
			par2.add(Chunk.TABBING);
			par2.add("CAMBIO: Bs.");
			par2.add(Chunk.TABBING);
			par2.add(Chunk.TABBING);
			par2.add(cambio);
			par2.add("\n");
			par2.add("SON: " + CuentaLiteral(total));
			par2.add("\n");
			par2.add("----------------------------------------------------------------\n");
			par2.add("CODIGO DE CONTROL: " + codigoControl + "\n");
			par2.add("FECHA LIMITE DE EMISION: " + "\n");
			String workingDir = System.getProperty("user.dir");
			Image img = Image
					.getInstance(workingDir + "\\qr.png");
			par2.add(Chunk.TABBING);
			img.setAlignment(img.ALIGN_CENTER);
			img.scaleAbsolute(35f, 35f);
			par3.setAlignment(img.ALIGN_CENTER);
			par3.add("\"ESTA FACTURA CONTRIBUYE AL DESARROLLO DEL PAIS. EL USO ILICITO DE ESTA SERA SANCIONADO DE "
					+ "ACUERDO A LA LEY" + "\"" + "\n\n");
			par3.add("Ley No. 453:La interrupcion del servicio debe comunicarse con anterioridad "
					+ "a las Autoridades que correspondan y a los usuarios afectados\n\n");
			par3.add("Cajero: " + cajero + "\n\n");
			par3.add("----------------------------------------------------------------\n");
			par3.add(extra);
			doc.add(par);
			doc.add(par2);
			doc.add(img);
			doc.add(par3);
			doc.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		/* FIN GENERAR PDF */
	}

	/* TRAE INFO DE FACTURACION */
	public static void readFacturaConfig() throws IOException,
			ClassNotFoundException {
		FileInputStream fin;
		try {
			fin = new FileInputStream("ConfigFactura.ser");
			ObjectInputStream ois = new ObjectInputStream(fin);
			List<String> list = (List<String>) ois.readObject();
			lista = FXCollections.observableList(list);
			// lista 00 : nombre restaurante
			// lista 01 : sucursal
			// lista 02 : direccion
			// lista 03 : telefono
			// lista 04 : nit
			// lista 05 : nit2
			// lista 06 : nautorizacion
			// lista 07 : nautorizacion2
			// lista 08 : actividad
			// lista 09 : nfactura
			// lista 10 : nfactura2
			// lista 11 : llavedosificacion
			// lista 12 : llavedosificacion2
			// lista 13 : extra

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void readOrden() throws IOException, ClassNotFoundException{
		FileInputStream fin;
		try {
			fin = new FileInputStream("nombre.ser");
			ObjectInputStream ois = new ObjectInputStream(fin);
			List<ProductoSimple> list = (List<ProductoSimple>) ois.readObject();
			orden = FXCollections.observableList(list);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String CuentaLiteral(String total) {
		ObservableList<String> numUnidades = FXCollections.observableArrayList("Cero", 
				"Uno","Dos", "Tres", "Cuatro", "Cinco","Seis", "Siete","Ocho",
				"Nueve", "Diez","Once", "Doce", "Trece", "Catorce", "Quince", 
				"Dieciseis", "Diecisiete", "Dieciocho", "Diecinueve", "Veinte", 
				"Veintiuno", "Veintidos", "Veintitres" ,"Veinticuatro", "Veinticinco" ,
				"Veintiseis" ,"Veintisiete" ,"Veintiocho" ,"Veintinueve");
		ObservableList<String> numDecenas = FXCollections.observableArrayList(
				"cero","Diez","Veinte","Treinta",  "Cuarenta", "Cincuenta",  "Sesenta", "Setenta", 
				"Ochenta", "Noventa","Cien");
		ObservableList<String> numCentenas = FXCollections.observableArrayList(
				"cero","Ciento", "Doscientos", "Trescientos", "Cuatrocientos", 
				 "Quinientos", "Seiscientos",  "Setecientos", "Ochocientos", 
				 "Novecientos","Mil");
		ObservableList<String> numMiles = FXCollections.observableArrayList(
				"cero","Mil","Dos Mil","Tres Mil","Cuatro Mil","Cinco Mil",
				"Seis Mil","Siete Mil","Ocho Mil","Nueve Mil","Diez Mil");
		
		float num = Float.valueOf(total);
		int auxnum = (int) Math.floor(num);
		int auxDecimal = (Math.round((num%1)*10));
		String output = "";
		
		if(auxnum<=29){
			output = numUnidades.get(auxnum);
		}
		
		if(auxnum>29 && auxnum<=100)
		{
			output = numDecenas.get(auxnum/10);
			if(auxnum%10 != 0 )
			{output += " Y " + numUnidades.get(auxnum%10);}
		}
		
		if(auxnum>100 && auxnum<=1000)
		{
			output = numCentenas.get(auxnum/100) + " ";
			auxnum%=100;
			if(auxnum<=29){
				if(auxnum%10 != 100 )
				{output += numUnidades.get(auxnum);}
			}
			if(auxnum>29 && auxnum<=99)
			{
				
				output += numDecenas.get(auxnum/10);
				if(auxnum%10 != 0 )
				{output += " Y " + numUnidades.get(auxnum%10);}
			}
			
		}
		
		if(auxnum>1000 && auxnum<=10000)
		{
			output = numMiles.get(auxnum/1000) + " ";
			auxnum%=1000;
			if(auxnum>100 && auxnum<=1000)
			{
				output += numCentenas.get(auxnum/100) + " ";
				auxnum%=100;
				if(auxnum<=29){
					output += numUnidades.get(auxnum);
				}
				if(auxnum>29 && auxnum<=99)
				{
					output += numDecenas.get(auxnum/10);
					if(auxnum%10 != 0 )
					{output += " Y " + numUnidades.get(auxnum%10);}
				}
				
			}else{
				if(auxnum<=29){
					if(auxnum != 0)
					output += numUnidades.get(auxnum);
				}
				if(auxnum>29 && auxnum<=99)
				{
					output += numDecenas.get(auxnum/10);
					if(auxnum%10 != 0 )
					{output += " Y " + numUnidades.get(auxnum%10);}
				}
			}
		}
		output += " "+ auxDecimal+"0/100";
		return output;

	}

}
