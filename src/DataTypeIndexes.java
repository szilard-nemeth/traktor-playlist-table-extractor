import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

public class DataTypeIndexes {
    private Map<String, Integer> dataTypeToIndexMapping = new HashMap<>();

    public void add(String dataType, int index) {
        dataTypeToIndexMapping.put(StringEscapeUtils.unescapeHtml4(dataType), index);
    }
    
    public Integer getIndexByType(String type) {
        return dataTypeToIndexMapping.get(type);
    }
}
