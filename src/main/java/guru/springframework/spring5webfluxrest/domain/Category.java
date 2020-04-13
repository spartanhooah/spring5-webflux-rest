package guru.springframework.spring5webfluxrest.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
//@NoArgsConstructor
@Document
public class Category {
    @Id private String id;

    private String description;
}
