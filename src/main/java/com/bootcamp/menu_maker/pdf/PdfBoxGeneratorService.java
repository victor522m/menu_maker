package com.bootcamp.menu_maker.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import com.bootcamp.menu_maker.entity.Menu;
import com.bootcamp.menu_maker.entity.PlatoBase;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.awt.Color;

@Service
public class PdfBoxGeneratorService {

    // Constantes de configuración
    private static final float MARGIN = 50;
    private static final float LINE_HEIGHT = 14;
    private static final PDType1Font HEADER_FONT = PDType1Font.HELVETICA_BOLD;
    private static final PDType1Font BODY_FONT = PDType1Font.HELVETICA;
    private static final int HEADER_SIZE = 14;
    private static final int BODY_SIZE = 10;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Color ACCENT_COLOR = new Color(0, 102, 204);
    private static final Color LIGHT_GRAY = new Color(220, 220, 220);

    public void generateMenuPdf(Menu menu, String destination, double porcentajeIva) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            float currentY = page.getMediaBox().getHeight() - MARGIN;

            // Configuración inicial
            contentStream.setLeading(LINE_HEIGHT * 1.2f);

            // Cabecera
            currentY = drawHeader(contentStream, menu, currentY);

            // Información del menú
            currentY = drawMenuInfo(contentStream, menu, currentY);

            // Tabla de platos
            currentY = drawPlatosTable(contentStream, menu, document, currentY);

            // Totales
            drawTotals(contentStream, menu, porcentajeIva, currentY);

            contentStream.close();
            document.save(destination);
        }
    }

    private float drawHeader(PDPageContentStream contentStream, Menu menu, float y) throws IOException {
        // Logo (ejemplo)
        // PDImageXObject pdImage = PDImageXObject.createFromFile("logo.png", document);
        // contentStream.drawImage(pdImage, MARGIN, y - 30, 50, 30);

        // Título
        contentStream.setFont(HEADER_FONT, HEADER_SIZE + 4);
        contentStream.setNonStrokingColor(ACCENT_COLOR);
        writeText(contentStream, "Menú: " + menu.getNombre(), MARGIN, y);
        y -= LINE_HEIGHT * 2;

        return y;
    }

    private float drawMenuInfo(PDPageContentStream contentStream, Menu menu, float y) throws IOException {
        contentStream.setFont(BODY_FONT, BODY_SIZE);
        contentStream.setNonStrokingColor(Color.BLACK);

        String created = "Creado: " + menu.getFechaCreacion().format(DATE_FORMATTER);
        String modified = "Modificado: " + menu.getFechaModificacion().format(DATE_FORMATTER);

        y = writeText(contentStream, created, MARGIN, y);
        y = writeText(contentStream, modified, MARGIN, y);

        return y - LINE_HEIGHT;
    }

    private float drawPlatosTable(PDPageContentStream contentStream, Menu menu, PDDocument document, float y) throws IOException {
        // Cabecera de tabla
        contentStream.setFont(HEADER_FONT, HEADER_SIZE);
        contentStream.setNonStrokingColor(Color.WHITE);

        // Fondo de cabecera
        contentStream.setNonStrokingColor(LIGHT_GRAY);
        contentStream.addRect(MARGIN, y - 10, 500, -20);
        contentStream.fill();

        // Texto de cabecera
        contentStream.setNonStrokingColor(Color.BLACK);
        writeText(contentStream, "Tipo", MARGIN + 10, y - 5);
        writeText(contentStream, "Nombre", MARGIN + 120, y - 5);
        writeText(contentStream, "Precio", MARGIN + 300, y - 5);
        writeText(contentStream, "Descripción", MARGIN + 380, y - 5);
        y -= 30;

        // Filas de platos
        contentStream.setFont(BODY_FONT, BODY_SIZE);
        for (PlatoBase plato : menu.getPlatos()) {
            if (y < MARGIN + 100) { // Salto de página
                contentStream.close();
                PDPage newPage = new PDPage(PDRectangle.A4);
                document.addPage(newPage);
                contentStream = new PDPageContentStream(document, newPage);
                y = newPage.getMediaBox().getHeight() - MARGIN;
            }

            // Línea separadora
            contentStream.setLineWidth(0.5f);
            contentStream.moveTo(MARGIN, y);
            contentStream.lineTo(MARGIN + 500, y);
            contentStream.stroke();

            // Contenido
            writeText(contentStream, plato.getTipoPlato(), MARGIN + 10, y - 15);
            writeText(contentStream, plato.getNombre(), MARGIN + 120, y - 15);
            writeText(contentStream, String.format("%.2f €", plato.getPrecio()), MARGIN + 300, y - 15);
            y -= LINE_HEIGHT;

            // Descripción con ajuste de línea
            y = writeWrappedText(contentStream, plato.getDescripcion(), MARGIN + 380, y, 100);
            y -= LINE_HEIGHT;
        }

        return y;
    }

    private void drawTotals(PDPageContentStream contentStream, Menu menu, double iva, float y) throws IOException {
        contentStream.setFont(HEADER_FONT, HEADER_SIZE);
        contentStream.setNonStrokingColor(ACCENT_COLOR);

        double totalSinIva = menu.calcularTotalPrecios();
        double totalConIva = menu.calcularTotalConIva(iva);

        y -= 30;
        writeText(contentStream, "Resumen:", MARGIN, y);
        y -= LINE_HEIGHT;

        contentStream.setFont(BODY_FONT, BODY_SIZE);
        contentStream.setNonStrokingColor(Color.BLACK);
        writeText(contentStream, String.format("Total sin IVA: %.2f €", totalSinIva), MARGIN + 20, y);
        y -= LINE_HEIGHT;
        writeText(contentStream, String.format("IVA (%s%%): %.2f €", iva, totalConIva - totalSinIva), MARGIN + 20, y);
        y -= LINE_HEIGHT;
        contentStream.setFont(HEADER_FONT, HEADER_SIZE);
        writeText(contentStream, String.format("Total con IVA: %.2f €", totalConIva), MARGIN + 20, y);
    }

    private float writeText(PDPageContentStream contentStream, String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
        return y - LINE_HEIGHT;
    }

    private float writeWrappedText(PDPageContentStream contentStream, String text, float x, float y, float maxWidth) throws IOException {
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        
        for (String word : words) {
            // Agregar espacio entre palabras solo si no es la primera palabra de la línea
            String testLine = (line.length() == 0) ? word : line.toString() + " " + word;
            float width = BODY_FONT.getStringWidth(testLine) / 1000 * BODY_SIZE;
            
            if (width > maxWidth) {
                // Dibujar la línea actual y empezar una nueva
                contentStream.beginText();
                contentStream.newLineAtOffset(x, y);
                contentStream.showText(line.toString());
                contentStream.endText();
                
                // Iniciar una nueva línea con la palabra que excedió el ancho
                line = new StringBuilder(word);
                y -= LINE_HEIGHT;
            } else {
                line.append(line.length() == 0 ? "" : " ").append(word);
            }
        }
        
        // Dibujar la última línea restante
        if (line.length() > 0) {
            contentStream.beginText();
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(line.toString());
            contentStream.endText();
        }
        
        return y;
    }
}
