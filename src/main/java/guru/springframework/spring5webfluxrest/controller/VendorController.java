package guru.springframework.spring5webfluxrest.controller;

import static guru.springframework.spring5webfluxrest.controller.VendorController.BASE_URL;

import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class VendorController {
    public static final String BASE_URL = "/api/v1/vendors";

    private final VendorRepository vendorRepository;

    @GetMapping
    Flux<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @GetMapping("{id}")
    Mono<Vendor> getVendorById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<Vendor> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);

        return vendorRepository.save(vendor);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<Vendor> patchVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        Mono<Vendor> foundVendor = vendorRepository.findById(id);

        return foundVendor
                .filter(found -> !Objects.equals(found.getName(), vendor.getName()))
                .flatMap(
                        f -> {
                            f.setName(vendor.getName());

                            return vendorRepository.save(f);
                        })
                .switchIfEmpty(foundVendor);
    }
}
