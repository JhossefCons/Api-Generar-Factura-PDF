package facturacion.facturacion.compra;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
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

        try {
            ClassPathResource resource = new ClassPathResource("logo.jpeg");
            ImageData imageData = ImageDataFactory.create(resource.getURL());
            Image logo = new Image(imageData);
            logo.setWidth(100);
            document.add(logo);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            document.add(new Paragraph("Logo no disponible"));
        } catch (IOException e) {
            e.printStackTrace();
            document.add(new Paragraph("Error al cargar el logo"));
        }

        // Encabezado de la empresa
        document.add(new Paragraph("CocaCola"));
        document.add(new Paragraph("Dirección: XXXXX"));
        document.add(new Paragraph("NIT: XXXXXXX"));
        document.add(new Paragraph("Contacto: XXXXX"));

        document.add(new Paragraph(" "));  // Espacio entre el encabezado y la tabla

        List<List<String>> tableData = Arrays.asList(
                Arrays.asList("Producto", "Cantidad","Descripcion","IVA", "Precio Unitario", "Total"),
                Arrays.asList("Producto 1", "2","Descipcion del producto","19%", "50", "100"),
                Arrays.asList("Producto 2", "3","Descipcion del producto","19%", "30", "90"),
                Arrays.asList("Producto 3", "1","Descipcion del producto","19%", "70", "70")
        );
        // Tabla de datos
        Table table = new Table(tableData.get(0).size());
        for (List<String> rowData : tableData) {
            for (String cellData : rowData) {
                table.addCell(new Cell().add(new Paragraph(cellData)));
            }
        }
        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}
