package org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.gateways;

import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos.InventoryDTOquery;
import org.springframework.stereotype.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfReportService {

    public byte[] generateInventoryReport(List<InventoryDTOquery> items) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font font = new Font(Font.HELVETICA, 16, Font.BOLD);
            document.add(new Paragraph("Inventory Products To Be Supplied", font));
            document.add(Chunk.NEWLINE);

            // Table
            PdfPTable table = new PdfPTable(4); // columns
            table.setWidthPercentage(100);
            addTableHeader(table);
            addRows(table, items);
            document.add(table);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Product ID", "Warehouse", "Available", "Threshold")
                .forEach(header -> {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(header));
                    table.addCell(cell);
                });
    }

    private void addRows(PdfPTable table, List<InventoryDTOquery> items) {
        for (InventoryDTOquery item : items) {
            table.addCell(String.valueOf(item.getProductId()));
            table.addCell(item.getWarehouseName());
            table.addCell(String.valueOf(item.getAvailableStock()));
            table.addCell(String.valueOf(item.getThresholdStock()));
        }
    }
}
