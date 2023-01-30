package bg.sofia.uni.fmi.mjt.git;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class Commit {

    private String hash;
    private String message;
    private String date;
    private Commit isAfter;

    //files -> the files in the repo of the commit
    private Set<String> files = new HashSet<>();

    public Commit(String message, Commit isAfter) {
        this.message = message;
        this.isAfter = isAfter;

        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM d HH:mm yyyy");
        this.date = date.format(formatter);
        this.hash = hexDigest(this.date + message);
    }

    public Commit(Commit commit) {
        this.hash = commit.getHash();
        this.message = commit.getMessage();
        this.date = commit.getDate();
        this.isAfter = commit.getPrevious();
    }

    public String hexDigest(String input) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return convertBytesToHex(bytes);
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private String convertBytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte current : bytes) {
            hex.append(String.format("%02x", current));
        }

        return hex.toString();
    }

    public String getHash() {
        return this.hash;
    }

    public String getMessage() {
        return this.message;
    }

    public Commit getPrevious() {
        return this.isAfter;
    }

    public String getDate() {
        return this.date;
    }

    Set<String> getFiles() {
        return this.files;
    }
}
