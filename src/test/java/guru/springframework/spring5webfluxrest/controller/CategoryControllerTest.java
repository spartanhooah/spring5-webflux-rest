package guru.springframework.spring5webfluxrest.controller;

import static guru.springframework.spring5webfluxrest.controller.CategoryController.BASE_URL;
import static guru.springframework.spring5webfluxrest.domain.Category.builder;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {
    private static final String CATEGORY = "Cat1";
    private WebTestClient webTestClient;

    @Mock private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        CategoryController categoryController = new CategoryController(categoryRepository);

        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void listAllCategories() {
        given(categoryRepository.findAll())
                .willReturn(
                        Flux.just(
                                builder().description(CATEGORY).build(),
                                builder().description("Cat2").build()));

        webTestClient.get().uri(BASE_URL).exchange().expectBodyList(Category.class).hasSize(2);
    }

    @Test
    void getCategoryById() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(builder().description(CATEGORY).build()));

        webTestClient
                .get()
                .uri(BASE_URL + "/1")
                .exchange()
                .expectBody(Category.class)
                .isEqualTo(builder().description(CATEGORY).build());
    }
}
