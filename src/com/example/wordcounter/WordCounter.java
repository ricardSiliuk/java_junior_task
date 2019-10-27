package com.example.wordcounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * WordCounter assumes that all words are alphabetical - numbers are not counted.
 */
public class WordCounter {
    private static String aplhabet = "abcdefghijklmnopqrstuvwxyz";
    private static String defaultOutput = "C:\\output";

    private static void createOutput(HashMap<Character, HashMap<String, Integer>> wordCounts) throws IOException {
        // Maybe move this to start of the application?
        if (Files.notExists(Paths.get(defaultOutput))) {
            Files.createDirectory(Paths.get(defaultOutput));
        }

        var keys = wordCounts.keySet().toArray();
        for (var key: keys) {
            Path outputFile = Paths.get(defaultOutput, "file_" + key + ".txt");
            var letterBox = wordCounts.get(key);
            var words = letterBox.keySet().toArray();
            if (words.length == 0) {
                continue;
            }
            Files.writeString(outputFile, key.toString());
            for (var word: words) {
                var count = letterBox.get(word);
                var output = String.format("%s %d\n", word, count);
                Files.writeString(outputFile, output, StandardOpenOption.APPEND);
            }
        }
    }

    /**
     * Function to read all files in a dir and return an arrayList of contents.
     *
     * @param dirName - directory to scan
     * @return file contents
     */
    private static ArrayList<String> readFiles(String dirName) throws IOException {
        ArrayList<String> contentList = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(dirName))) {
            paths.filter(Files::isRegularFile).forEach(x -> contentList.add(getFileContents(x)));
        }
        return contentList;
    }

    /**
     *  Function to read contents of a file to a string.
     * @param filename - file to read
     * @return contents as a string
     */
    private static String getFileContents(Path filename) {
        String fileContent = null;
        try {
            fileContent = Files.readString(filename);
        } catch (IOException e) {
            String error = String.format("Error while reading %s", filename);
            System.out.println(error);
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return fileContent;
    }

    /**
     * Lowercase and sanitize (remove non-letters) the string.
     * @param input String to sanitize
     * @return string with all
     */
    private static String sanitizeString(String input) {
        input = input.toLowerCase();
        return input.replaceAll("[^a-z]", " ");
    }

    private static HashMap<Character, HashMap<String, Integer>> countOccurrences(ArrayList<String> contents) {
        HashMap<Character, HashMap<String, Integer>> wordCounts = new HashMap<>();
        for (Character letter: aplhabet.toCharArray()) {
            wordCounts.put(letter, new HashMap<>());
        }
        // Count occurrences
        for (String content: contents) {
            var temp = content.split("\\s+");
            for (String word: temp) {

                // In case file starts with whitespace
                if (word.isEmpty()) {
                    continue;
                }
                var firstLetter = word.charAt(0);
                var letterBox = wordCounts.get(firstLetter);
                var count = letterBox.getOrDefault(word, 0);
                letterBox.put(word, count + 1);
            }
        }
        return wordCounts;
    }

    public static void main(String[] args) {
        ArrayList<String> contents = null;

        // Read contents
        try {
            contents = readFiles("C:\\Temp");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }

        // Sanitize contents
        for (int i = 0; i < contents.size(); i++) {
            String sanitizedContent = sanitizeString(contents.get(i));
            contents.set(i, sanitizedContent);
        }

        var wordCounts = countOccurrences(contents);
        try {
            createOutput(wordCounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
