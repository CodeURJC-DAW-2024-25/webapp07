package es.codeurjc.backend.service;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import es.codeurjc.backend.dto.OrderDTO;
import es.codeurjc.backend.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfService {

    public byte[] generateInvoicePdf(OrderDTO orderDTO, UserDTO userDTO) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        document.setMargins(20, 20, 20, 20);

        try {
            // Header
            document.add(new Paragraph("Voltereta Croqueta")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBackgroundColor(new DeviceRgb(15, 23, 43))
                    .setFontColor(new DeviceRgb(254, 161, 22))
                    .setFontSize(32)
                    .simulateBold()
                    .setMarginBottom(20));

            // Order Details
            document.add(new Paragraph("Order Nº: " + orderDTO.id())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .simulateBold());

            document.add(new Paragraph(" ").setFontSize(8));

            // User Table
            Table userTable = new Table(new float[]{2, 4}).useAllAvailableWidth();
            userTable.addCell(new Cell().add(new Paragraph("Name:").simulateBold()));
            userTable.addCell(new Cell().add(new Paragraph(userDTO.firstName() + " " + userDTO.lastName())));
            userTable.addCell(new Cell().add(new Paragraph("Address:").simulateBold()));
            userTable.addCell(new Cell().add(new Paragraph(orderDTO.address())));
            document.add(userTable);

            document.add(new Paragraph(" ").setFontSize(8));

            // Dishes Table
            Table dishTable = new Table(new float[]{4, 2}).useAllAvailableWidth();
            dishTable.addHeaderCell(new Cell().add(new Paragraph("Dish").simulateBold()));
            dishTable.addHeaderCell(new Cell().add(new Paragraph("Price (€)").simulateBold()));

            orderDTO.dishes().forEach(dish -> {
                double price = dish.price();
                dishTable.addCell(new Cell().add(new Paragraph(dish.name())))
                        .addCell(new Cell().add(new Paragraph(String.format("%.2f", price)))
                                .setTextAlignment(TextAlignment.RIGHT));
            });

            document.add(dishTable);
            document.add(new Paragraph(" ").setFontSize(8));

            // Summary Table
            double deliveryCost = 4.99;
            double finalPrice = orderDTO.totalPrice() + deliveryCost;

            Table summary = new Table(new float[]{4, 2}).useAllAvailableWidth();
            summary.addCell(new Cell().add(new Paragraph("Subtotal:")));
            summary.addCell(new Cell().add(new Paragraph("€" + String.format("%.2f", orderDTO.totalPrice())))
                    .setTextAlignment(TextAlignment.RIGHT));
            summary.addCell(new Cell().add(new Paragraph("Delivery:")));
            summary.addCell(new Cell().add(new Paragraph("€4.99"))
                    .setTextAlignment(TextAlignment.RIGHT));
            summary.addCell(new Cell().add(new Paragraph("Total:").simulateBold()));
            summary.addCell(new Cell().add(new Paragraph("€" + String.format("%.2f", finalPrice)).simulateBold())
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(summary);

            // Footer
            document.add(new Paragraph(" ").setFontSize(8));
            document.add(new Paragraph("© 2025 Voltereta Croqueta. All rights reserved.\nCalle Gran Vía 10 Madrid")
                    .setTextAlignment(TextAlignment.JUSTIFIED)
                    .setFontSize(10));

        } finally {
            document.close(); // Asegura que el documento se cierre incluso si hay errores
        }

        return outputStream.toByteArray();
    }
}
