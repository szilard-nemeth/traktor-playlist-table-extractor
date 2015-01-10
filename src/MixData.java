import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

public class MixData {
    private Map<Integer, String> actualTrackDataMap = new HashMap<>();
    private List<Map<Integer, String>> trackDataList = new ArrayList<>();
    private String sourceFileName;

    private static final String NUM = "Num.";
    private static final String TITLE = "Title";
    private static final String ARTIST = "Artist";

    private static final String DEFAULT_FORMAT_STRING = "%s - %s-%s";
    private static final String FORMAT_STRING_WITHOUT_ARTIST = "%s - %s";

    public MixData(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    public void startNewIndividualTrackData() {
        // add the previous track data map to the list
        trackDataList.add(actualTrackDataMap);
        actualTrackDataMap = new HashMap<>();
    }

    public void addData(int index, String data) {
        actualTrackDataMap.put(index, StringEscapeUtils.unescapeHtml4(data));
    }

    public void finalizeData() {
        trackDataList.add(actualTrackDataMap);
    }

    public void print(DataTypeIndexes dataTypeIndexes) {
        System.out.println("=============================================");
        System.out.println("==========" + sourceFileName + "==========");
        printToStream(dataTypeIndexes, System.out);
        System.out.println("=============================================");
    }

    
    public void printToFile(DataTypeIndexes dataTypeIndexes, String directoryPath) throws FileNotFoundException {
        File resultFile = new File(directoryPath,sourceFileName + ".txt");
        PrintStream fileStream = new PrintStream(resultFile);
        printToStream(dataTypeIndexes, fileStream);
        fileStream.close();
    }

    private void printToStream(DataTypeIndexes dataTypeIndexes, OutputStream stream) {
        Integer numIndex = dataTypeIndexes.getIndexByType(NUM);
        Integer titleIndex = dataTypeIndexes.getIndexByType(TITLE);
        Integer artistIndex = dataTypeIndexes.getIndexByType(ARTIST);
        for (Map<Integer, String> individualTrackData : trackDataList) {
            String num = individualTrackData.get(numIndex);
            String artist = individualTrackData.get(artistIndex);
            String title = individualTrackData.get(titleIndex);
            String line = null;
            if (artist == null) {
                line = String.format(FORMAT_STRING_WITHOUT_ARTIST, num, title);
            } else {
                line = String.format(DEFAULT_FORMAT_STRING, num, artist, title);
            }
            ((PrintStream) stream).println(line);
        }
    }
}
