package com.hannah.hannahworld.Data;
import org.codehaus.jackson.annotate.JsonProperty;
        import org.codehaus.jackson.annotate.JsonIgnoreProperties;
        import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Words {
    public int week;
    public List<String> words;
    public int getWeek() {
        return week;
    }
    public List<String> getWords() {
        return words;
    }
}


