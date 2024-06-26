package facturacion.facturacion.compra;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;


@Service
public class PdfService {

    //Funcion que genera un codigo alfanumerico aleatorio
    public String generateCUFE (){
        UUID uuid = UUID.randomUUID();

        return uuid.toString().replace("-", "");
    }

    // Funcion que recibe 2 String para hacer un parrafo con uno de ellos en negrita
    public Paragraph boldText(String textToBold, String textToNormal){
        Paragraph paragraph = new Paragraph();
        Text textbold = new Text(textToBold).setBold();
        Text textNormal = new Text(textToNormal);
        paragraph.add(textbold);
        paragraph.add(textNormal);
        return paragraph;
    }

    public byte[] generateInvoicePdf() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        document.setFontSize(10);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();

        Image logo = null;
        try {
            ClassPathResource resource = new ClassPathResource("logo.jpeg");
            ImageData imageData = ImageDataFactory.create(resource.getURL());
            logo = new Image(imageData);
            logo.setWidth(100);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Informacion de empresa
        Table enterpriseData = new Table(1);
        enterpriseData.setWidthPercent(100);
        enterpriseData.addCell(new Cell().add("**Nombre de la empresa**").setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        enterpriseData.addCell(new Cell().add(boldText("Direccion: ","**Direccion**")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        enterpriseData.addCell(new Cell().add(boldText("NIT: ","***NIT***")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        enterpriseData.addCell(new Cell().add(boldText("Contacto: ","**Contacto**")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        enterpriseData.addCell(new Cell().add(new Paragraph("Factura Electrónica de compra")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        enterpriseData.addCell(new Cell().add(boldText("Resolución de facturación aprobado por la DIAN No.","\n ############")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

        Table enterpriseDetails = new Table(2);
        enterpriseDetails.setWidthPercent(100);
        if (logo != null) {
            enterpriseDetails.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));
        }else {
            enterpriseDetails.addCell(new Cell().add("Imagen no encontrada").setBorder(Border.NO_BORDER));
        }
        enterpriseDetails.addCell(new Cell().add(enterpriseData).setBorder(Border.NO_BORDER));
        document.add(enterpriseDetails);
        document.add(new Paragraph(" "));

        //Añadir codigo CUFE
        document.add(boldText("CODIGO CUFE: ",generateCUFE()).setTextAlignment(TextAlignment.CENTER).setFontSize(8));

        // Tabla detalles de factura
        Table invoiceDetails = new Table(2);
        invoiceDetails.setWidthPercent(100);
        invoiceDetails.addCell(new Cell().add(boldText("FECHA DE EMISIÓN: ", String.valueOf(LocalDate.now()))));
        invoiceDetails.addCell(new Cell().add(boldText("AUT. NUMERACIÓN FAC: "," 18764063496317")));
        invoiceDetails.addCell(new Cell().add(boldText("HORA DE EMISIÓN: ",dateFormat.format(date))));
        invoiceDetails.addCell(new Cell().add(boldText("FECHA DE VENCIMIENTO: "," 2024/05/06")));
        document.add(invoiceDetails);

        // Tabla detalles del Emisor
        document.add(new Paragraph("DATOS DEL EMISOR").setTextAlignment(TextAlignment.CENTER).setBold());
        Table transmitterDetails = new Table(2);
        transmitterDetails.setWidthPercent(100);
        transmitterDetails.addCell(new Cell().add(boldText("NIT: "," **NIT**")));
        transmitterDetails.addCell(new Cell().add(boldText("CORREO: ","**CORREO***")));
        transmitterDetails.addCell(new Cell().add(boldText("NOMBRE: ","**NOMBRE**")));
        transmitterDetails.addCell(new Cell().add(boldText("DIRECCION: ","**DIRECCION**")));
        transmitterDetails.addCell(new Cell().add(boldText("TIPO CONTRIBUYENTE: ","**TIPO CONTRIBUYENTE**")));
        transmitterDetails.addCell(new Cell().add(boldText("TELEFONO: ","**+57311**")));
        document.add(transmitterDetails);

        // Tabla detalles del tercero
        document.add(new Paragraph("DATOS DEL CLIENTE").setTextAlignment(TextAlignment.CENTER).setBold());
        Table clientDetails = new Table(2);
        clientDetails.setWidthPercent(100);
        clientDetails.addCell(new Cell().add(boldText("CÓDIGO DEL CLIENTE: "," **codigo**")));
        clientDetails.addCell(new Cell().add(boldText("DEPARTAMENTO: ","**departamento***")));
        clientDetails.addCell(new Cell().add(boldText("TIPO DE DOCUMENTO: ","**Cédula**")));
        clientDetails.addCell(new Cell().add(boldText("CIUDAD: ","**ciudad**")));
        clientDetails.addCell(new Cell().add(boldText("NÚMERO DE DOCUMENTO: ","**documento**")));
        clientDetails.addCell(new Cell().add(boldText("TELEFONO: ","**+57311**")));
        clientDetails.addCell(new Cell().add(boldText("NOMBRE DE CLIENTE: ","**nombre**")));
        clientDetails.addCell(new Cell().add(boldText("CORREO: ","**@gmail.com**")));
        clientDetails.addCell(new Cell().add(boldText("DIRECCIÓN DEL CLIENTE: ","**direccion cliente**")));
        clientDetails.addCell(new Cell().add(boldText("TIPO DE PERSONA: ","**tipo de persona**")));
        document.add(clientDetails);

        // tabla de productos
        document.add(new Paragraph("PRODUCTOS").setTextAlignment(TextAlignment.CENTER).setBold());
        Table itemTable = new Table(new float[]{1, 3, 1, 2, 2});
        itemTable.setWidthPercent(100);
        itemTable.addHeaderCell(new Cell().add(new Paragraph("CANTIDAD").setBold()).setBackgroundColor(Color.LIGHT_GRAY));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("DESCRIPCIÓN").setBold()).setBackgroundColor(Color.LIGHT_GRAY));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("IVA").setBold()).setBackgroundColor(Color.LIGHT_GRAY));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("PRECIO UNITARIO").setBold()).setBackgroundColor(Color.LIGHT_GRAY));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("VALOR TOTAL").setBold()).setBackgroundColor(Color.LIGHT_GRAY));

        // Productos
        List<List<String>> items = Arrays.asList(
                Arrays.asList("564", "PASTILLA DE CUAJO", "19%", "2326", "1,311,864"),
                Arrays.asList("2", "SAL", "NO APLICA", "23,260", "46,520")
        );

        for (List<String> item : items) {
            itemTable.addCell(new Cell().add(new Paragraph(item.get(0))));
            itemTable.addCell(new Cell().add(new Paragraph(item.get(1))));
            itemTable.addCell(new Cell().add(new Paragraph(item.get(2))));
            itemTable.addCell(new Cell().add(new Paragraph(item.get(3))));
            itemTable.addCell(new Cell().add(new Paragraph(item.get(4))));
        }

        itemTable.addCell(new Cell().add(boldText("Cantidad Total: ","**cantidad productos**")));
        document.add(itemTable);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // Cuenta total
        Table summaryTable = new Table(2);
        summaryTable.setWidthPercent(100);
        summaryTable.addCell(new Cell().add("TOTAL SIN IMPUESTOS").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        summaryTable.addCell(new Cell().add("$$$$$$$"));
        summaryTable.addCell(new Cell().add("TOTAL CON IMPUESTOS").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        summaryTable.addCell(new Cell().add("$$$$$$$"));
        summaryTable.addCell(new Cell().add("TOTAL A PAGAR").setBold().setBackgroundColor(Color.LIGHT_GRAY));
        summaryTable.addCell(new Cell().add("$$$$$$$"));
        summaryTable.addCell(new Cell().add(boldText("Valor en letras: ","**Valor en letras** M/CTE")).setBorder(Border.NO_BORDER));
        document.add(summaryTable);

        document.add(new Paragraph(" "));

        document.add(new Paragraph("**FACTURA GENERADA CON FINES EDUCATIVOS, NO TIENE NINGUN VALOR**").setFontColor(Color.RED).setTextAlignment(TextAlignment.CENTER));

        document.close();

        return baos.toByteArray();
    }
}
