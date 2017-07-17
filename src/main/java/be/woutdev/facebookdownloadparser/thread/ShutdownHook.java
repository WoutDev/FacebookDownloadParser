package be.woutdev.facebookdownloadparser.thread;

import be.woutdev.facebookdownloadparser.Parser;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by Wout on 17/07/2017.
 */
public class ShutdownHook extends Thread
{
    private final Parser parser;

    public ShutdownHook(Parser parser)
    {
        this.parser = parser;
    }

    @Override
    public void run()
    {
        try
        {
            Files.walkFileTree(parser.getTemp(), new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                {
                    Files.delete(file);

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
                {
                    if (exc == null)
                    {
                        Files.delete(dir);

                        return FileVisitResult.CONTINUE;
                    }

                    throw exc;
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
