# ArkaProject

Modular backend for managing products, purchase orders, and sales reports. Includes notifications, weekly analytics, and inventory and abandoned cart management.
It is composed of a fleet of microservices, each of them takking care of a Domain, in the context of a DDD:

## Components

- ms_cart MVC - Clean Architecture + DDD + QCRS
- ms_order WebFlux - Clean Architecture + DDD + QCRS
- ms_inventory WebFlux - Clean Architecture + DDD + QCRS
- ms_product MVC - Layer Architecture

## ms_order
