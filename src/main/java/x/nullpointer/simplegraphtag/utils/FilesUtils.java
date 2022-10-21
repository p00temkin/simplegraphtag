package x.nullpointer.simplegraphtag.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilesUtils.class);

    public static void writeToFileUNIXNoException(final String outString, final String filePath) {
        try {
            writeToFile(outString, filePath, "\n");
        } catch (Exception e) {
            LOGGER.warn("Exception: " + e.getMessage());
        }
    }

    public static void writeToFile(final String outString, final String filePath, final String delimiter) throws Exception {
        Writer out = null;
        try {
            File oldFile = new File(filePath);
            if (oldFile.exists()) {
                if (oldFile.delete() == false) {
                    LOGGER.warn("Unable to delete " + oldFile.getAbsolutePath());
                }
            }
            out = new OutputStreamWriter(new FileOutputStream(filePath, true), StandardCharsets.UTF_8);
            out.write(outString + delimiter);
            out.close();
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static void deleteFileNoException(final String filePath) {
        File hashFile = new File(filePath);
        if (hashFile.exists()) {
            if (hashFile.delete()) {
                LOGGER.debug("Deleted file " + filePath);
            } else {
                LOGGER.warn("Did not delete file " + filePath);
            }
        }
    }

    public static List<String> readAllLinesFrom(String primaryPath) {

        primaryPath = StringsUtils.determineExistingFilePath(primaryPath);
        File priFile = new File(primaryPath);
        if (!priFile.exists()) {
            LOGGER.error("Fallback file does not exist at " + priFile.getAbsolutePath());
            LOGGER.error("TERMINATING... ");
            SystemUtils.halt();
        }

        final Path pth = priFile.toPath();
        if (!priFile.exists()) {
            try {
                LOGGER.error("Found no file at " + priFile.getCanonicalPath());
                SystemUtils.halt();
            } catch (IOException e) {
                LOGGER.error("IO exception while reading file: " + Arrays.toString(e.getStackTrace()));
                SystemUtils.halt();
            }
        }
        try {
            List<String> lines = Files.readAllLines(pth, StandardCharsets.UTF_8);
            return lines;
        } catch (Exception e) {
            LOGGER.error("IO exception while reading file " + primaryPath + ": " + Arrays.toString(e.getStackTrace()));
            SystemUtils.halt();
        }
        return new ArrayList<String>();
    }

    public static List<String> readAllLinesFrom(String primaryPath, int lineLimit) {

        int lineCount = 0;
        primaryPath = StringsUtils.determineExistingFilePath(primaryPath);
        File priFile = new File(primaryPath);
        if (!priFile.exists()) {
            LOGGER.error("Fallback file does not exist at " + priFile.getAbsolutePath());
            LOGGER.error("TERMINATING... ");
            SystemUtils.halt();
        }

        if (!priFile.exists()) {
            try {
                LOGGER.error("Found no file at " + priFile.getCanonicalPath());
                SystemUtils.halt();
            } catch (IOException e) {
                LOGGER.error("IO exception while reading file: " + Arrays.toString(e.getStackTrace()));
                SystemUtils.halt();
            }
        }
        try {
            List<String> lines = new ArrayList<String>();

            FileReader fr = new FileReader(priFile);   
            BufferedReader br = new BufferedReader(fr);
            String line;  
            while((line=br.readLine())!=null)  {  
                if (lineCount < lineLimit) {
                    lines.add(line);
                    lineCount++;
                }
            }  
            br.close();
            return lines;
        } catch (Exception e) {
            LOGGER.error("IO exception while reading file " + primaryPath + ": " + Arrays.toString(e.getStackTrace()));
            SystemUtils.halt();
        }
        return new ArrayList<String>();
    }

    public static void appendToFileUNIXNoException(final String outString, final String filePath) {
        try {
            appendToFile(outString, filePath, "\n");
        } catch (Exception e) {
            LOGGER.info("e: " + e.getMessage());
        }
    }

    public static void appendToFile(final String outString, final String filePath, final String delimiter) throws Exception {
        Writer out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(filePath, true), StandardCharsets.UTF_8);
            out.write(outString + delimiter);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

}
