package facturacion.facturacion.compra;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.*;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

@Service
public class PdfService {

    public byte[] generateInvoicePdf() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

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
        if (logo != null) {
            document.add(logo);
        }
        document.add(new Paragraph("Nombre de la Empresa")
                .setTextAlignment(TextAlignment.CENTER).setBold());
        document.add(new Paragraph("Dirección: XXXXX")
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("NIT: XXXXXXX")
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Contacto: XXXXX")
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Factura Electrónica de Venta")
                .setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(14));
        document.add(new Paragraph("No. POBE 505")
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Resolución de facturación aprobado por la DIAN No. 18764063496317")
                .setTextAlignment(TextAlignment.CENTER));

        // detalles de factura
        Table invoiceDetails = new Table(2);
        invoiceDetails.setWidthPercent(100);
        invoiceDetails.addCell(new Cell().add(new Paragraph("FECHA DE EMISIÓN: 2024/05/06").setBold())
                .setBorder(Border.NO_BORDER));
        invoiceDetails.addCell(new Cell().add(new Paragraph("AUT. NUMERACIÓN FAC: 18764063496317").setBold())
                .setBorder(Border.NO_BORDER));
        invoiceDetails.addCell(new Cell().add(new Paragraph("HORA DE EMISIÓN: 17:04:59").setBold())
                .setBorder(Border.NO_BORDER));
        invoiceDetails.addCell(new Cell().add(new Paragraph("PREFIJO: POBE").setBold())
                .setBorder(Border.NO_BORDER));
        invoiceDetails.addCell(new Cell().add(new Paragraph("FECHA DE VENCIMIENTO: 2024/05/06").setBold())
                .setBorder(Border.NO_BORDER));
        invoiceDetails.addCell(new Cell().add(new Paragraph("RANGO AUTORIZADO: 1-10000").setBold())
                .setBorder(Border.NO_BORDER));
        invoiceDetails.addCell(new Cell().add(new Paragraph("MÉTODO DE PAGO: Contado").setBold())
                .setBorder(Border.NO_BORDER));
        invoiceDetails.addCell(new Cell().add(new Paragraph("FECHA INICIO DE VIGENCIA: 2024/01/09").setBold())
                .setBorder(Border.NO_BORDER));
        document.add(invoiceDetails);

        // detalles del cliente
        document.add(new Paragraph("DATOS DEL RECEPTOR")
                .setTextAlignment(TextAlignment.CENTER).setBold());
        Table clientDetails = new Table(2);
        clientDetails.setWidthPercent(100);
        clientDetails.addCell(new Cell().add(new Paragraph("CÓDIGO DEL CLIENTE: XXXXX").setBold())
                .setBorder(Border.NO_BORDER));
        clientDetails.addCell(new Cell().add(new Paragraph("DEPARTAMENTO: Cauca").setBold())
                .setBorder(Border.NO_BORDER));
        clientDetails.addCell(new Cell().add(new Paragraph("TIPO DE DOCUMENTO: Cédula de ciudadanía").setBold())
                .setBorder(Border.NO_BORDER));
        clientDetails.addCell(new Cell().add(new Paragraph("CIUDAD: POPAYÁN").setBold())
                .setBorder(Border.NO_BORDER));
        clientDetails.addCell(new Cell().add(new Paragraph("NÚMERO DE DOCUMENTO: XXXXX").setBold())
                .setBorder(Border.NO_BORDER));
        clientDetails.addCell(new Cell().add(new Paragraph("TELEFONO: +57311...").setBold())
                .setBorder(Border.NO_BORDER));
        clientDetails.addCell(new Cell().add(new Paragraph("NOMBRE DE CLIENTE: XXXXX").setBold())
                .setBorder(Border.NO_BORDER));
        clientDetails.addCell(new Cell().add(new Paragraph("CORREO: @gmail.com").setBold())
                .setBorder(Border.NO_BORDER));
        clientDetails.addCell(new Cell().add(new Paragraph("DIRECCIÓN DEL CLIENTE: XXXXX").setBold())
                .setBorder(Border.NO_BORDER));
        clientDetails.addCell(new Cell().add(new Paragraph("TIPO DE PERSONA: Persona Natural").setBold())
                .setBorder(Border.NO_BORDER));
        document.add(clientDetails);

        // Tabla de productos
        document.add(new Paragraph("ÍTEM").setBold());
        Table itemTable = new Table(new float[]{2, 2, 2, 2, 2, 2, 2});
        itemTable.setWidthPercent(100);
        itemTable.addHeaderCell(new Cell().add(new Paragraph("CÓDIGO DE PRODUCTO").setBold()));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("DESCRIPCIÓN").setBold()));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("UM").setBold()));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("CANTIDAD").setBold()));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("VALOR UNITARIO").setBold()));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("DESCUENTO").setBold()));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("VALOR IMPUESTO").setBold()));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("VALOR TOTAL").setBold()));

        // productos
        List<List<String>> items = Arrays.asList(
                Arrays.asList("753759280888", "VENU 2 PLUS POWDER GRAY PASSIV", "NA*S/L*6S0022517 94", "1.00", "1,989,900.00", "198,990.00", "0.00", "1,790,910.00"),
                Arrays.asList("flete", "FLETE", "94", "1.00", "10,900.00", "0.00", "0.00", "10,900.00")
        );

        for (List<String> item : items) {
            for (String detail : item) {
                itemTable.addCell(new Cell().add(new Paragraph(detail)));
            }
        }
        document.add(itemTable);

        // Cuenta total
        Table summaryTable = new Table(2);
        summaryTable.setWidthPercent(100);
        summaryTable.addCell(new Cell().add(new Paragraph("Cantidad Total: 2").setBold())
                .setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph("Total sin impuesto: 1,801,810.00").setBold())
                .setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph("Total impuesto: 0.00").setBold())
                .setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph("Total COP: 1,801,810.00").setBold())
                .setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph("Valor en letras: UN MILLON OCHOCIENTOS UN MIL OCHOCIENTOS DIEZ PESOS M/CTE").setBold())
                .setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        document.add(summaryTable);

        document.close();

        return baos.toByteArray();
    }
}
