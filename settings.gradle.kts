rootProject.name = "flowforge"

include(
    "libraries:event-envelope",
    "libraries:observability-starter",
    "libraries:test-support",
    "services:api-gateway",
    "services:customer-service",
    "services:catalog-service",
    "services:order-service",
    "services:payment-service",
    "services:provisioning-service",
    "services:notification-service",
    "provider-simulators:payment-provider-simulator",
    "provider-simulators:legacy-provider-simulator",
    "provider-simulators:nextgen-provider-simulator",
)
