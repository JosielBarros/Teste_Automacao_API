package data_class;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MessageData {

    @JsonProperty("success")
    private String success;

    @JsonProperty("error")
    private String error;

    @JsonProperty("not_found")
    private String notFound;

    @JsonProperty("not_allowed")
    private String notAllowed;
}
