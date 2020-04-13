package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import guru.springframework.spring5webfluxrest.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static java.util.Arrays.asList;

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
            Vendor burgers = new Vendor();
            burgers.setName("Bob's Burgers");

            Vendor gyros = new Vendor();
            gyros.setName("Tom & Jerry's");

            vendorRepository.saveAll(asList(burgers, gyros)).blockLast();
        }
    }

    private void loadCategories() {
        if (categoryRepository.count().block() == 0) {
            Category fruits = new Category();
            fruits.setDescription("Fruits");

            Category dried = new Category();
            dried.setDescription("Dried");

            Category fresh = new Category();
            fresh.setDescription("Fresh");

            Category exotic = new Category();
            exotic.setDescription("Exotic");

            Category nuts = new Category();
            nuts.setDescription("Nuts");

            categoryRepository.saveAll(asList(fruits, dried, fresh, exotic, nuts)).blockLast();

            System.out.println("Loaded categories: " + categoryRepository.count().block());
        }
    }
}
