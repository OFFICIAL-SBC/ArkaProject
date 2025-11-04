package org.sebasbocruz.ms_inventory.queries.infrastructure.entrypoints;

import lombok.RequiredArgsConstructor;
import org.sebasbocruz.ms_inventory.queries.application.GetProductsToBeSuppliedUseCase;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.dtos.InventoryDTOquery;
import org.sebasbocruz.ms_inventory.queries.infrastructure.adapters.gateways.PdfReportService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class QueryInventoryHandler {

    private final GetProductsToBeSuppliedUseCase getProductsToBeSuppliedUseCase;
    private final PdfReportService pdfReportService;

    Mono<ServerResponse> getProductsToBeSupplied(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(getProductsToBeSuppliedUseCase.getProductsToBeSupplied(), InventoryDTOquery.class);

    }


    public Mono<ServerResponse> generatePdfReport(ServerRequest request) {
        return getProductsToBeSuppliedUseCase.getProductsToBeSupplied()
                .collectList()
                .map(pdfReportService::generateInventoryReport)
                .flatMap(pdfBytes ->
                        ServerResponse.ok()
                                .header("Content-Disposition", "attachment; filename=inventory_report.pdf")
                                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                                .bodyValue(pdfBytes)
                );
    }
}
