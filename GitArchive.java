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
    public static void main(String... args)
    {
        String workTreePath = "";
        String repoName = "";
        try {
            Path path = Paths.get(args[0]);
            workTreePath = path.toString();
            repoName = path.getFileName().toString();
        } catch (InvalidPathException e) {
            System.out.println("Invalid path");
            System.exit(1);
        }

        System.out.println(workTreePath);
        System.out.println(repoName);

        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(new File(workTreePath));
        pb.command("git", "show", "-s", "--format=%ct %h");
        
        String commitDateTime = "";
        String commitHash = "";

        try {
            Scanner scan = new Scanner(pb.start().getInputStream());
            commitDateTime = scan.next();
            commitHash = scan.next();
        } catch (IOException e) {
            System.out.println("Fail to read commit info");
            System.exit(1);
        }

        System.out.println(commitDateTime);
        System.out.println(commitHash);

        String outputFileName = repoName + "." + commitDateTime + "." + commitHash + ".zip";
        String outputFilePath = "../" + outputFileName;

        pb.command("git", "archive", "-o", outputFilePath, "master");
        try {
            int exitStatus = pb.start().waitFor();
            if (exitStatus == 0) {
                System.out.println("Archive created: " + outputFileName);
            } else {
                System.out.println("Oops, git failed to create archive");
                System.exit(1);
            }
        } catch (IOException e) {
            System.out.println("Fail to archive commit");
            System.exit(1);
        } catch (InterruptedException e) {
            System.out.println("Git process was terminated");
            System.exit(1);
        }
    }
}