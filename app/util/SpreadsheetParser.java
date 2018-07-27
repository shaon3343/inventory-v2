package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface SpreadsheetParser {
    ArrayList<ArrayList<String>> parseSpreadsheet(File spreadsheet) throws IOException;
}
