package data_class;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class BreedsData {

    @JsonProperty("breed")
    private List<String> breed;

    @JsonProperty("sub_breed")
    private String[] subBreed;
}
