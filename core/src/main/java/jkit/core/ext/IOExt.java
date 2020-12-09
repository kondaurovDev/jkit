package jkit.core.ext;

import io.vavr.CheckedConsumer;
import io.vavr.collection.List;
import io.vavr.control.*;
import jkit.core.model.UserError;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.function.Consumer;
import java.util.zip.ZipFile;

public interface IOExt {

    Logger logger = LoggerFactory.getLogger("jc.Global");

    static Logger createLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    static void out(String text) {
        out(l -> l.println(text));
    }

    static void out(Consumer<PrintStream> handler) {
        handler.accept(System.out);
    }

    static Either<UserError, String> readResource(String path) {

        return Option
            .of(IOExt.class.getResourceAsStream(path))
            .toEither(() -> UserError.create("resource not found: " + path))
            .flatMap(IOExt::inputStreamToString);

    }

    static Either<UserError, Void> sleepSec(Integer sec) {
        return TryExt.getAndVoid(
            () -> Thread.sleep(1000 * sec),
            "sleep"
        );
    }

    static Either<UserError, String> readFileContent(String filename) {
        return TryExt.get(() ->
            new String(Files.readAllBytes(Paths.get(filename))),
            "Can't read file content"
        );
    }

    static Either<UserError, Void> writeToFile(
        String file,
        InputStream is
    ) {
        return TryExt.getAndVoid(() -> {
            FileOutputStream fos = new FileOutputStream(new File(file));
            int inByte;
            while((inByte = is.read()) != -1)
                 fos.write(inByte);
            is.close();
            fos.close();
        }, "save stream to file");
    }

    static Either<UserError, Void> writeToFile(
        String file,
        String content
    ) {
        return TryExt.get(() -> {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.close();
            return null;
        }, "Write to file");
    }

    static void log(CheckedConsumer<Logger> handle) {
        TryExt.getOrThrow(() -> {
            handle.accept(logger);
            return null;
        }, "Log message");
    }

    static Either<UserError, String> inputStreamToString(
        InputStream inputStream
    ) {
        return StreamExt
            .readAllBytes(inputStream)
            .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
            .mapLeft(e -> e.withError("Can't read input stream"));
    }

    static Either<UserError, Process> runCmd(
        List<String> command,
        List<String> envParams
    ) {
        return runCmd(
            command.toJavaArray(String[]::new),
            envParams.toJavaArray(String[]::new)
        );
    }

    static Either<UserError, Process> runCmd(
        String... command
    ) {
        return runCmd(command, new String[]{});
    }

    static Either<UserError, Process> runCmd(
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

    static Either<UserError, String> getOutput(
        Process process
    ) {
        return getOutput(process, false);
    }

    static Either<UserError, String> getOutput(
        Process process,
        Boolean output
    ) {

        return Try
            .of(() -> {
                val stdout = IOExt.inputStreamToString(
                    process.getInputStream()
                );
                val stderr = IOExt.inputStreamToString(
                    process.getErrorStream()
                ).flatMap(s -> Either.<UserError, String>left(UserError.create(s)));
                int exitVal = process.waitFor();
                if (exitVal == 0) {
                    return stdout;
                } else {
                    return stderr;
                }
            })
            .toEither()
            .mapLeft(e -> UserError.create("stream to string", e))
            .flatMap(r -> r);

    }

    static Either<UserError, ?> unzipFile(
        String zipFile,
        String entryPath,
        String extractTo
    ) {
        return Try
            .of(() -> new ZipFile(zipFile))
            .mapTry(z -> z.getInputStream(z.getEntry(entryPath)))
            .toEither()
            .mapLeft(e -> UserError.create("zip file", e))
            .flatMap(is -> IOExt.writeToFile(extractTo, is));
    }

    static boolean fileExists(
        String file
    ) {
        return new File(file).exists();
    }

    static <L, R> Either<L, R> log(
        Either<L, R> action,
        String actionName
    ) {
        val result = action.fold(
            e -> "error - " + Option.of(e).toString(),
            r -> "success - " + Option.of(r).toString()
        );

        IOExt.out(
            String.format(
                "%s: %s",
                actionName,
                result
            )
        );

        return action;
    }

}
