package bg.sofia.uni.fmi.mjt.grep;

import bg.sofia.uni.fmi.mjt.grep.utils.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static bg.sofia.uni.fmi.mjt.grep.enums.PropertiesNames.*;
import static bg.sofia.uni.fmi.mjt.grep.utils.ConcurrentUtils.stop;

public class Main {

    public static CommandParameters properties;     // parsed grep command
    public static final int THRESHOLD = 5000;       // max lines for a thread to be 'alone' in a file

    static ThreadPoolExecutor exec;


    /*
     * Finds all encounters in a directory (including subdirectories) containing a given string
     *
     * @param command - the 'grep' command to be executed
     * @throws IllegalArgumentException if a parameter in 'command' cannot be resolved
     */
    static void grep(String command) throws IllegalArgumentException {
        properties = new CommandParameters(command);
        File root = new File((String) properties.get(PROP_PATH_SRC));

        execute(root);
    }

    /*
     * Executes the 'grep' command
     *
     * @param root - the root directory of type File
     */
    private static void execute(File root) {

        int maxThreads = (int) properties.get(PROP_MAX_THREADS);

        // prepare the pool
        if (exec.getMaximumPoolSize() > maxThreads) {
            exec.setCorePoolSize(maxThreads);
            exec.setMaximumPoolSize(maxThreads);
        }
        else {
            exec.setMaximumPoolSize(maxThreads);
            exec.setCorePoolSize(maxThreads);
        }

        LinkedList<File> files = getFiles(root);

        boolean outputFileHandled = true;
        if (!properties.get(PROP_PATH_DST).equals("")) {
            outputFileHandled = createDeleteDestinationFile();
        }

        if (outputFileHandled) {
            for (File file : files) {
                grepFile(file, getLinesCount(file), 0); // the section is always 0 in the beginning
            }
        }
    }

    /*
     * This method 'greps' a single file.
     * The important thing to notice is that it evaluates the number of the file's lines.
     * If this number exceeds 'THRESHOLD', then the file will be recursively provided to as many threads needed to
     * 'grep' it.
     *
     * @param file - the file to be 'grepped'
     * @param lines - the number of remaining lines in the file. It recursively reaches 0 <= lines <= THRESHOLD
     * @param section - defines which section of the file the responsible thread should 'grep'
     */
    private static void grepFile(File file, long lines, int section) {
        if (lines <= 0) {
            return;
        }

        if (lines <= THRESHOLD) {
            MyRunnable task = new MyRunnable(file, section);
            exec.execute(task);

            return;
        }

        MyRunnable task1 = new MyRunnable(file, section);
        MyRunnable task2 = new MyRunnable(file, section + 1);
        exec.execute(task1);
        exec.execute(task2);

        grepFile(file, lines - 2 * THRESHOLD, section + 2);
    }

    /*
     * Finds the number of lines in a file
     *
     * @param file - the file
     * @return number of lines
     */
    private static long getLinesCount(File file) {
        long linesCount = 0;

        try (Stream<String> lines = Files.lines(file.toPath())) {
            linesCount = lines.count();
        } catch (IOException e) {
            e.getStackTrace();
        }

        return linesCount;
    }

    /*
     * 1. Cleans MyRunnable.outputFileStream if it has been opened
     * 2. Attempts to delete the output file if it exists
     * 3. Creates a new output file and sets the 'outputFileStream' for all threads
     */
    private static boolean createDeleteDestinationFile() {
        try {
            if (MyRunnable.outputFileStream != null) {
                MyRunnable.outputFileStream.close();
                MyRunnable.outputFileStream = null;
            }

            File outputFile = new File((String) properties.get(PROP_PATH_DST));
            Files.deleteIfExists(outputFile.toPath());

            try {
                if (!outputFile.createNewFile()) {
                    throw new IOException();
                }
            } catch (IOException e) {
                System.err.println("Couldn't find/create output file. Enter a new command");
                return false;
            }

            MyRunnable.outputFileStream = new FileOutputStream(
                                                (new File((String) properties.get(PROP_PATH_DST))), true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    /*
     * Recursively retrieves all files from the root directory and subdirectories
     * Excludes the outputFile (case-insensitive) if passed in the 'grep' command
     *
     * @param root - the root directory
     * @return all files
     */
    private static LinkedList<File> getFiles(File root) {

        LinkedList<File> files = new LinkedList<>();

        File[] dirs = root.listFiles((dir, name) -> new File(dir, name).isDirectory());
        File[] currentFiles = root.listFiles((dir, name) -> new File(dir, name).isFile());

        if (currentFiles != null) {
            for (File file : currentFiles) {
                if (!file.getPath().equalsIgnoreCase((String) properties.get(PROP_PATH_DST))) {
                    files.push(file);
                }
            }
        }

        if (dirs != null) {
            if (dirs.length == 0) {
                return files;
            }

            for (File dir : dirs) {
                files.addAll(getFiles(dir));
            }
        }

        return files;
    }


    public static void main(String[] args) {

        // initialize the executor
        final int initialPoolSize = 5;
        exec = new ThreadPoolExecutor(initialPoolSize, initialPoolSize, 1, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>());

        try (Scanner console = new Scanner(System.in)) {
            String consoleInput;

            while ((consoleInput = console.nextLine()) != null) {
                if (consoleInput.trim().equalsIgnoreCase("quit")) {
                    break;
                }
                try {
                    grep(consoleInput);
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stop(exec);
    }
}
