package com.bootcamp.menu_maker.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import com.bootcamp.menu_maker.entity.Menu;
import com.bootcamp.menu_maker.entity.PlatoBase;

import java.io.IOException;

@Service
public class PdfBoxGeneratorService {

    public void generateMenuPdf(Menu menu, String destination, double porcentajeIva) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

            // Título del menú
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.beginText();
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText("Menú: " + menu.getNombre());
            contentStream.newLine();

            // Información del menú
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.showText("Fecha de creación: " + menu.getFechaCreacion());
            contentStream.newLine();
            contentStream.showText("Última modificación: " + menu.getFechaModificacion());
            contentStream.newLine();

            // Tabla de platos
            contentStream.newLine();
            contentStream.showText(String.format("%-20s %-20s %-10s %-20s", "Tipo de Plato", "Nombre", "Precio (€)", "Descripción"));
            contentStream.newLine();
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            for (PlatoBase plato : menu.getPlatos()) {
                contentStream.showText(String.format("%-20s %-20s %-10.2f %-20s",
                        plato.getTipoPlato(),
                        plato.getNombre(),
                        plato.getPrecio(),
                        plato.getDescripcion()));
                contentStream.newLine();
            }

            // Calcular precios
            double totalSinIva = menu.calcularTotalPrecios();
            double totalConIva = menu.calcularTotalConIva(porcentajeIva);

            // Mostrar resumen
            contentStream.newLine();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.showText(String.format("Total sin IVA: %.2f €", totalSinIva));
            contentStream.newLine();
            contentStream.showText(String.format("Total con IVA (%.2f%%): %.2f €", porcentajeIva, totalConIva));
            contentStream.endText();
        }

        // Guardar el documento
        document.save(destination);
        document.close();
    }
}
