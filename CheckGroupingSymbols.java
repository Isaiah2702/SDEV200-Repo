import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Deque;

public class CheckGroupingSymbols {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java CheckGroupingSymbols <JavaSourceFile>");
            return;
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.isFile()) {
            System.out.println("File " + args[0] + " does not exist");
            return;
        }

        String src = Files.readString(file.toPath());

        Result result = checkPairs(src);

        if (result.ok) {
            System.out.println("Correct grouping pairs");
        } else {
            System.out.println("Incorrect grouping pairs");
            System.out.println("Reason: " + result.message);
        }
    }

    private static class Result {
        boolean ok;
        String message;

        Result(boolean ok, String message) {
            this.ok = ok;
            this.message = message;
        }
    }

    public static Result checkPairs(String src) {
        Deque<Character> stack = new ArrayDeque<>();

        boolean inLineComment = false;
        boolean inBlockComment = false;
        boolean inString = false;
        boolean inChar = false;

        int n = src.length();

        for (int i = 0; i < n; i++) {
            char c = src.charAt(i);
            char next = (i + 1 < n) ? src.charAt(i + 1) : '\0';

            // --- Handle line comment ---
            if (inLineComment) {
                if (c == '\n')
                    inLineComment = false;
                continue;
            }

            // --- Handle block comment ---
            if (inBlockComment) {
                if (c == '*' && next == '/') {
                    inBlockComment = false;
                    i++; // skip '/'
                }
                continue;
            }

            // --- Handle string literal ---
            if (inString) {
                if (c == '\\' && next != '\0') {
                    i++; // skip escaped char
                } else if (c == '"') {
                    inString = false;
                }
                continue;
            }

            // --- Handle char literal ---
            if (inChar) {
                if (c == '\\' && next != '\0') {
                    i++;
                } else if (c == '\'') {
                    inChar = false;
                }
                continue;
            }

            // We are in normal code; detect entry into comment/string/char
            if (c == '/' && next == '/') {
                inLineComment = true;
                i++;
                continue;
            }
            if (c == '/' && next == '*') {
                inBlockComment = true;
                i++;
                continue;
            }
            if (c == '"') {
                inString = true;
                continue;
            }
            if (c == '\'') {
                inChar = true;
                continue;
            }

            // --- Grouping symbol logic (proper nesting via stack) ---
            if (isOpening(c)) {
                stack.push(c);
            } else if (isClosing(c)) {
                if (stack.isEmpty()) {
                    return new Result(false, "Found closing '" + c + "' with no matching opener.");
                }
                char open = stack.pop();
                if (!matches(open, c)) {
                    return new Result(false,
                            "Mismatched symbols: opened with '" + open + "' but closed with '" + c + "'.");
                }
            }
        }

        if (!stack.isEmpty()) {
            return new Result(false, "Unclosed symbol(s) remain on stack: " + stack);
        }

        return new Result(true, "All grouping symbols properly matched.");
    }

    private static boolean isOpening(char c) {
        return c == '(' || c == '{' || c == '[';
    }

    private static boolean isClosing(char c) {
        return c == ')' || c == '}' || c == ']';
    }

    private static boolean matches(char open, char close) {
        return (open == '(' && close == ')')
                || (open == '{' && close == '}')
                || (open == '[' && close == ']');
    }
}
