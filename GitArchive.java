import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

/**
 * Export master commit from git repository.
 * 
 * @author (Serge Guzik) 
 */
class GitArchive
{   
    private static String pathToName(String path) throws InvalidPathException
    {
        return Paths.get(path).getFileName().toString();
    }
    
    public static void main(String... args)
    {
        String workTreePathString = "";
        String workTreeParentString = "";
        String gitDirPathString = "";
        String repoName = "";
        try {
            Path workTreePath = Paths.get(args[0]);
            workTreePathString = workTreePath.toString();
            workTreeParentString = workTreePath.getParent().toString();
            gitDirPathString = workTreePath.resolve(".git").toString();
            repoName = workTreePath.getFileName().toString();
        } catch (InvalidPathException e) {
            System.out.println("Invalid path");
            System.exit(1);
        }

        System.out.println(workTreePathString);
        System.out.println(gitDirPathString);
        System.out.println(repoName);

        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(new File(workTreePathString));
        pb.command("git", "show", "-s", "--format=%ct %h");
        
        String commitDateTime = "";
        String commitHash = "";

        try {
            Scanner scan = new Scanner(pb.start().getInputStream());
            commitDateTime = scan.next();
            commitHash = scan.next();
        } catch (IOException e) {
            System.exit(1);
        }

        System.out.println(commitDateTime);
        System.out.println(commitHash);

        String outputFilePath = "../" + repoName + "." + commitDateTime + "." + commitHash + ".zip";

        pb.command("git", "archive", "-o", outputFilePath, "master");
        try {
            pb.start();
        } catch (IOException e) {
            System.exit(1);
        }
    }
}