package org.apache.maven.extra.cli;

import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Parser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * @author Alexey Merezhin
 */
public class Cli {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: mvn-cli /my/proj/pom.xml");
            System.exit(1);
        }

        MavenProject project = new MavenProject(args[0]);

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
            write(terminal, "loading...");

            String prompt = "> ";
            String rightPrompt = "";

            String line = reader.readLine(prompt, rightPrompt, null, null);

            write(terminal, line);
        }
    }

    private static void write(Terminal terminal, String msg) {
        terminal.writer().println(msg);
        terminal.writer().flush();
    }
}
