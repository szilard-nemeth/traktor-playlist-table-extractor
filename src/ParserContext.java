import java.io.FileNotFoundException;

import org.htmlcleaner.TagNode;

public class ParserContext {
    private DataTypeIndexes dataTypeIndexes;
    private MixData mixData;

    private boolean insideTable = false;
    private int tableRowCount = 0;
    private int columnCount = 0;
    private String directoryPath;

    public ParserContext(String directoryPath, String sourceFileName) {
        dataTypeIndexes = new DataTypeIndexes();
        mixData = new MixData(sourceFileName);
        this.directoryPath = directoryPath;
    }

    public void handleTag(TagNode tag) {
        String tagName = tag.getName();
        String tagAsString = tag.getText().toString();
        if ("table".equals(tagName)) {
            if (insideTable) {
                throw new RuntimeException("2 tables found in file, export format changed?");
            } else {
                insideTable = true;
            }
        } else if ("tr".equals(tagName)) {
            columnCount = 0;
            ++tableRowCount;
            if (tableRowCount > 2) {
                mixData.startNewIndividualTrackData();
            }
        } else if ("th".equals(tagName)) {
            if (tableRowCount == 1) {
                // store the location (index) of the relevant data
                // types
                dataTypeIndexes.add(tagAsString, columnCount);
                ++columnCount;
            }
        } else if ("td".equals(tagName)) {
            mixData.addData(columnCount, tagAsString);
            ++columnCount;
        }
    }

    public void finish() throws FileNotFoundException {
        // add the last track data map
        mixData.finalizeData();
        mixData.print(dataTypeIndexes);
        mixData.printToFile(dataTypeIndexes, directoryPath);
    }

}
