package archivos;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;


public class EscribirFichero {

	public  void  crearPDF(){
		try {
			PdfWriter write = new PdfWriter("src\\ficheros\\archivo"+fechaHora()+".pdf");
			PdfDocument pdf = new PdfDocument(write);
			Document document = new Document(pdf,PageSize.A4.rotate());
			document.setMargins(20, 20, 20, 20);
			PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
			PdfFont blod = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
			Table table = new Table(new float[]{4,4});
			table.setWidthPercent(100);
			BufferedReader br = new BufferedReader(new FileReader("src\\ficheros\\Identificador.txt"));
			String linea = br.readLine();

			procces(table, linea, font, true);
			while((linea=br.readLine())!=null){
				procces(table, linea, font, true);
			}

			document.add(table);
			br.close();
			document.flush();
			document.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static  void procces(Table table, String linea, PdfFont font, boolean isHeader){
		StringTokenizer tkn =new StringTokenizer(linea,"\t");
		while(tkn.hasMoreTokens()){
			if(isHeader){
				table.addHeaderCell(new Cell().add(new Paragraph(tkn.nextToken()).setFont(font)));
			}else{
				table.addCell(new Cell().add(new Paragraph(tkn.nextToken()).setFont(font)));
			}
		}
	}

	public  void crearListasId(String token){
		try {
			FileWriter escritura = new FileWriter("src\\ficheros\\Identificador.txt", true);
			BufferedWriter buffer = new BufferedWriter(escritura);
			buffer.write(token);
			buffer.newLine();
			buffer.flush();
			escritura.close();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	public String crearListasTokens(String token){	
		String fichero = "src\\ficheros\\listaTokens_"+fechaHora()+".txt";
		try {
			FileWriter escritura = new FileWriter(fichero, true);
			BufferedWriter buffer = new BufferedWriter(escritura);
			buffer.write(token);
			buffer.newLine();
			buffer.flush();
			escritura.close();

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return fichero;
	}

	public String crearListasArbolDerivacion(String token){
		String fichero = "src\\ficheros\\arbolDerivacion.txt";
		try {
			FileWriter escritura = new FileWriter(fichero);
			BufferedWriter buffer = new BufferedWriter(escritura);
			buffer.write(token);
			buffer.newLine();
			buffer.flush();
			escritura.close();

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return fichero;
	}

	public static String fechaHora(){
		Calendar c =  Calendar.getInstance();
		String fecha =c.get(Calendar.DATE)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.YEAR);
		String hora =c.get(Calendar.HOUR_OF_DAY)+"-"+c.get(Calendar.MINUTE)+"-"+c.get(Calendar.SECOND);
		return fecha+"_"+hora;

	}


	public boolean eliminar(){
		File fichero = new File("src\\ficheros\\Identificador.txt");
		if (fichero.exists()){
			fichero.delete();
			return true;
		}
		else
			return false;
	}

	public void escribirTabla(String nuevoDato) throws IOException {
		String tokenAux = "";
		String cadenaCompleta = "";
		FileReader f = new FileReader("src\\ficheros\\filename.txt");
		BufferedReader b = new BufferedReader(f);
		while((tokenAux = b.readLine()) != null)
			cadenaCompleta += tokenAux + "\n";
		
		try {
			String ruta = "src\\ficheros\\filename.txt";
			File file = new File(ruta);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(new File("src\\ficheros\\filename.txt"));
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(cadenaCompleta + nuevoDato);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void vaciarArchivoTabla() {
		try {
			FileWriter fw = new FileWriter(new File("src\\ficheros\\filename.txt"));
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("");
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
