package guru.springframework.spring5webfluxrest.controller;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static guru.springframework.spring5webfluxrest.controller.VendorController.BASE_URL;
import static guru.springframework.spring5webfluxrest.domain.Vendor.builder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class VendorControllerTest {
    private static final String VENDOR = "Vendor1";
    private WebTestClient webTestClient;

    @Mock VendorRepository vendorRepository;

    @BeforeEach
    void setUp() {
        VendorController vendorController = new VendorController(vendorRepository);

        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void getAllVendors() {
        given(vendorRepository.findAll())
                .willReturn(
                        Flux.just(
                                builder().name(VENDOR).build(), builder().name("Vendor2").build()));

        webTestClient.get().uri(BASE_URL).exchange().expectBodyList(Vendor.class).hasSize(2);
    }

    @Test
    void getVendorById() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(builder().name(VENDOR).build()));

        webTestClient
                .get()
                .uri(BASE_URL + "/1")
                .exchange()
                .expectBody(Vendor.class)
                .isEqualTo(builder().name(VENDOR).build());
    }

    @Test
    void createVendor() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(builder().build()));

        Mono<Vendor> vendorToSave = Mono.just(builder().name("Some vendor").build());

        webTestClient
                .post()
                .uri(BASE_URL)
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void updateVendor() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(builder().name("Some vendor").build());

        webTestClient
                .put()
                .uri(BASE_URL + "/someId")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isAccepted();
    }
}
