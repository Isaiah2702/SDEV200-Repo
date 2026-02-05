import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CountKeywords {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java CountKeywords <JavaSourceFile>");
            return;
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.isFile()) {
            System.out.println("File " + args[0] + " does not exist");
            return;
        }

        int count = countKeywords(file);
        System.out.println("The number of keywords in " + file.getPath() + " is " + count);
    }

    public static int countKeywords(File file) throws IOException {
        // Same keyword set as typical Liang example (includes true/false/null)
        String[] keywordString = {
                "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
                "continue", "default", "do", "double", "else", "enum", "extends", "for", "final", "finally",
                "float", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
                "native", "new", "package", "private", "protected", "public", "return", "short", "static",
                "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try",
                "void", "volatile", "while", "true", "false", "null"
        };

        Set<String> keywordSet = new HashSet<>(Arrays.asList(keywordString));

        String src = Files.readString(file.toPath());

        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean inString = false;
        boolean inChar = false;

        int count = 0;
        int n = src.length();

        // We'll build identifiers when we're in "code" state (not comment/string/char)
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < n; i++) {
            char c = src.charAt(i);
            char next = (i + 1 < n) ? src.charAt(i + 1) : '\0';

            // --- Handle exiting line comment ---
            if (inLineComment) {
                if (c == '\n') {
                    inLineComment = false;
                }
                continue;
            }

            // --- Handle exiting block comment ---
            if (inBlockComment) {
                if (c == '*' && next == '/') {
                    inBlockComment = false;
                    i++; // skip '/'
                }
                continue;
            }

            // --- Handle string literal ---
            if (inString) {
                if (c == '\\' && next != '\0') { // escape sequence inside string
                    i++; // skip escaped char
                } else if (c == '"') {
                    inString = false;
                }
                continue;
            }

            // --- Handle char literal ---
            if (inChar) {
                if (c == '\\' && next != '\0') { // escape inside char literal
                    i++;
                } else if (c == '\'') {
                    inChar = false;
                }
                continue;
            }

            // We are in normal code here. First: detect start of comment/string/char.
            if (c == '/' && next == '/') {
                flushTokenAndCount(token, keywordSet);
                inLineComment = true;
                i++; // skip second '/'
                continue;
            }

            if (c == '/' && next == '*') {
                flushTokenAndCount(token, keywordSet);
                inBlockComment = true;
                i++; // skip '*'
                continue;
            }

            if (c == '"') {
                flushTokenAndCount(token, keywordSet);
                inString = true;
                continue;
            }

            if (c == '\'') {
                flushTokenAndCount(token, keywordSet);
                inChar = true;
                continue;
            }

            // Build tokens (Java identifiers: start with letter/_/$, then letter/digit/_/$)
            if (isIdentifierPart(c)) {
                token.append(c);
            } else {
                // delimiter -> end token
                count += flushTokenAndCount(token, keywordSet);
            }
        }

        // flush any trailing token
        count += flushTokenAndCount(token, keywordSet);

        return count;
    }

    private static boolean isIdentifierPart(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '$';
    }

    private static int flushTokenAndCount(StringBuilder token, Set<String> keywordSet) {
        if (token.length() == 0)
            return 0;

        String word = token.toString();
        token.setLength(0);

        return keywordSet.contains(word) ? 1 : 0;
    }
}
