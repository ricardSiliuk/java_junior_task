package com.example.wordcounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

public class WordCounter {
    private static String aplhabet = "abcdefghijklmnopqrstuvwxyz";

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
        return "Reading " + filename;

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

    public static void main(String[] args) {
        HashMap<Character, HashMap<String, Integer>> wordCounts = new HashMap<>();
        for (Character letter: aplhabet.toCharArray()) {
            wordCounts.put(letter, null);
        }
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

        // Count occurrences
        for (String content: contents) {
            var temp = content.split("\\s+");
            for (String word: temp) {
                var firstLetter = word.charAt(0);
                var letterBox = wordCounts.get(firstLetter);
                if (letterBox == null) {
                    wordCounts.put(firstLetter, new HashMap<>());
                    letterBox = wordCounts.get(firstLetter);
                }

                var count = letterBox.getOrDefault(word, 0);
                letterBox.put(word, count + 1);
            }
        }
        System.out.println("Done.");
    }
}
