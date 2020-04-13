package guru.springframework.spring5webfluxrest.bootstrap;

import static java.util.Arrays.asList;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    @Override
    public void run(String... args) {
        loadCategories();
        loadVendors();
    }

    private void loadVendors() {
        if (vendorRepository.count().block() == 0) {
            Vendor burgers = Vendor.builder().name("Bob's Burgers").build();
            Vendor gyros = Vendor.builder().name("Tom & Jerry's").build();

            vendorRepository.saveAll(asList(burgers, gyros)).blockLast();
        }
    }

    private void loadCategories() {
        if (categoryRepository.count().block() == 0) {
            Category fruits = Category.builder().description("Fruits").build();
            Category dried = Category.builder().description("Dried").build();
            Category fresh = Category.builder().description("Fresh").build();
            Category exotic = Category.builder().description("Exotic").build();
            Category nuts = Category.builder().description("Nuts").build();

            categoryRepository.saveAll(asList(fruits, dried, fresh, exotic, nuts)).blockLast();

            System.out.println("Loaded categories: " + categoryRepository.count().block());
        }
    }
}
