package org.sebasbocruz.ms_orders.infrastructure.adapters.DTOS.OUT;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

// ! A Java file can contain:
// ? ONE PUBLIC TOP-LEVEL CLASS -> Here the file must match that class name
// ? Additional NON-PUBLIC classes are allowed in the same file

public record OrderResponseDTO(
        Long orderId,
        String status,                 // resolved name, not order_state_id
        String currencyCode,           // ISO 4217, not currency_id
        double total,
        OffsetDateTime createdAt,
        DeliveryEstimate delivery,
        AddressDTO deliveryAddress,
        List<ShipmentResponse> shipments
) {}

record DeliveryEstimate(
        OffsetDateTime earliest,
        OffsetDateTime latest
){}


record ShipmentResponse(
        Long shipmenID,
        String status,
        ShipmentOrigin origin,
        OffsetDateTime estimatedArrival,
        BigDecimal distanceKm,
        List<ShipmentItemResponse> items
){}


record ShipmentOrigin(
        String name,
        String city,
        String country,
        AddressDTO address
){}

record ShipmentItemResponse(
        Long productId,
        String name,
        int units,
        double unitPrice,
        double total
){}