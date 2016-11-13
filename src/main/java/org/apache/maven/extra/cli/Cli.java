package org.apache.maven.extra.cli;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Parser;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * @author Alexey Merezhin
 */
public class Cli {
    private CliMojo cliMojo;
    private Terminal terminal;

    public Cli(CliMojo cliMojo) throws Exception {
        this.cliMojo = cliMojo;
        run();
    }

    void run() throws Exception {
        TerminalBuilder builder = TerminalBuilder.builder();
        terminal = builder.build();
        Completer completer = new ArgumentCompleter(new StringsCompleter("deps"));
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

            processCommand(line);
        }
    }

    private void processCommand(String command) throws DependencyTreeBuilderException {
        if (command.startsWith("deps")) {
            cliMojo.showDeps();
            return;
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: mvn-cli /my/proj/pom.xml");
            System.exit(1);
        }

        //Project project = new Project(args[0]);

    }

    private void write(String msg) {
        terminal.writer().println(msg);
        terminal.writer().flush();
    }
}
