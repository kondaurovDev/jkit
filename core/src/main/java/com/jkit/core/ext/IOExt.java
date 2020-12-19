package com.jkit.core.ext;

import io.vavr.API;
import io.vavr.CheckedConsumer;
import io.vavr.collection.List;
import io.vavr.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.function.Consumer;
import java.util.zip.ZipFile;

public interface IOExt {

    Logger logger = LoggerFactory.getLogger("jkit.core");

    static Logger createLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    static void out(String text) {
        out(l -> l.println(text));
    }

    static void out(Consumer<PrintStream> handler) {
        handler.accept(System.out);
    }

    static Try<String> readResource(String path) {

        return Option
            .of(IOExt.class.getResourceAsStream(path))
            .toTry(() -> new java.lang.Error("resource not found: " + path))
            .flatMap(StreamExt::inputStreamToString);

    }

    static Try<Void> sleepSec(Integer sec) {
        return Try
            .run(() -> Thread.sleep(1000 * sec))
            .onFailure(e -> IOExt.debug("sleep", e));
    }

    static Try<String> readFileContent(String filename) {
        return Try
            .of(() -> new String(Files.readAllBytes(Paths.get(filename))))
            .onFailure(e -> IOExt.debug("Can't read file content", e));
    }

    static Try<Void> writeToFile(
        String file,
        InputStream is
    ) {
        return Try
            .run(() -> {
                FileOutputStream fos = new FileOutputStream(new File(file));
                int inByte;
                while((inByte = is.read()) != -1)
                    fos.write(inByte);
                is.close();
                fos.close();
            })
            .onFailure(e -> IOExt.debug("save stream to file", e));
    }

    static Try<Void> writeToFile(
        String file,
        String content
    ) {
        return Try
            .run(() -> {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(content);
                writer.close();
            })
            .onFailure(e -> IOExt.debug("Write to file", e));
    }

    static void log(CheckedConsumer<Logger> handle) {
        TryExt.get(() -> {
            handle.accept(logger);
            return null;
        }, "Log message");
    }

    static void debug(String msg, Throwable err) {
        log(l -> l.debug(msg + ": " + err.getMessage()));
    }

    static Try<Process> runCmd(
        List<String> command,
        List<String> envParams
    ) {
        return runCmd(
            command.toJavaArray(String[]::new),
            envParams.toJavaArray(String[]::new)
        );
    }

    static Try<Process> runCmd(
        String... command
    ) {
        return runCmd(command, new String[]{});
    }

    static Try<Process> runCmd(
        String[] command,
        String[] envParams
    ) {

        return TryExt.get(() ->
            Runtime.getRuntime().exec(
                command,
                envParams
            ),
            "Run command"
        );

    }

    static Try<String> getOutput(
        Process process
    ) {

        return API.For(
            StreamExt.inputStreamToString(process.getInputStream()),
            StreamExt.inputStreamToString(process.getErrorStream()),
            Try.of(process::waitFor)
        ).yield((stdout, stderr, exitVal) -> {
            if (exitVal == 0) {
                return stdout;
            } else {
                return stderr;
            }
        });

    }

    static Try<?> unzipFile(
        String zipFile,
        String entryPath,
        String extractTo
    ) {
        return Try
            .of(() -> new ZipFile(zipFile))
            .mapTry(z -> z.getInputStream(z.getEntry(entryPath)))
            .onFailure(e -> IOExt.debug("zip file", e))
            .flatMap(is -> IOExt.writeToFile(extractTo, is));
    }

    static boolean fileExists(
        String file
    ) {
        return new File(file).exists();
    }

}
