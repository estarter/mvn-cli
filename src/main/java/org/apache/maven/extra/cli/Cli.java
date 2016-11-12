package org.apache.maven.extra.cli;

import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Parser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

/**
 * @author Alexey Merezhin
 */
public class Cli {

    public static void main(String[] args) throws IOException {
        TerminalBuilder builder = TerminalBuilder.builder();
        Terminal terminal = builder.build();
        Completer completer = null;
        Parser parser = null;
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .parser(parser)
                .build();

        while (true) {
            String prompt = "> ";
            String rightPrompt = "";

            String line = reader.readLine(prompt, rightPrompt, null, null);

            terminal.writer().println(line);
            terminal.flush();
        }
    }
}
