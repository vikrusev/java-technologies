package bg.sofia.uni.fmi.mjt.grep.utils;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import static bg.sofia.uni.fmi.mjt.grep.Main.THRESHOLD;
import static bg.sofia.uni.fmi.mjt.grep.Main.properties;
import static bg.sofia.uni.fmi.mjt.grep.enums.PropertiesNames.*;

public class MyRunnable implements Runnable {

    public static FileOutputStream outputFileStream = null;    // shared
    private static ReentrantLock lock = new ReentrantLock();

    private File file;
    private int sectionOfFile;  // which section of file to 'grep'
    private AtomicInteger lineNumber = new AtomicInteger(1);    // keeps track of the line

    public MyRunnable(File file, int sectionOfFile) {
        this.file = file;
        this.sectionOfFile = sectionOfFile;
    }

    /*
     * Retrieves all lines from a section and checks for the wanted string
     */
    @Override
    public void run() {
        try (Stream<String> lines = Files.lines(this.file.toPath())) {
            lines.skip(this.sectionOfFile * THRESHOLD)
                    .limit(THRESHOLD)
                    .forEach(this::checkLine);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 1. Determines wheather or not the line contains the wanted string
     * 2. Prints it to the right place (console or file)
     *
     * @param line - the line
     */
    private void checkLine(String line) {
        String toFind = (String) properties.get(PROP_TO_FIND);
        String tempLine = line;

        if ((boolean) properties.get(PROP_IGNORE)) {
            tempLine = tempLine.toLowerCase();
            toFind = toFind.toLowerCase();
        }

        if ((boolean) properties.get(PROP_WHOLE)) {
            if (tempLine.matches(".*\\b" + toFind + "\\b.*")) {
                log(this.lineNumber, line);
            }
        } else {
            if (tempLine.contains(toFind)) {
                log(this.lineNumber, line);
            }
        }

        this.lineNumber.addAndGet(1);
    }

    /*
     * Simple log function - prints formatted line to the console or the 'outputFile'
     * The write to a file is locked until the process has finished
     *
     * @param lineNumber - the line number
     * @param lineText - the line text
     */
    private void log(AtomicInteger lineNumber, String lineText) {

        StringBuilder toWrite = new StringBuilder(this.file.getPath() + ":" +
                                                    lineNumber + ":" +
                                                    lineText +
                                                    System.lineSeparator());

        if (properties.get(PROP_PATH_DST).equals("")) {
            System.out.print(toWrite);
        }
        else {
            lock.lock();
            try {
                outputFileStream.write(toWrite.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

}
