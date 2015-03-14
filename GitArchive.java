import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.DateTimeException;

/**
 * Export master commit from git repository.
 * 
 * @author (Serge Guzik) 
 */
class GitArchive
{   
    private static void checkGitCommand()
    {
        try {
            Runtime.getRuntime().exec("git");    
        } catch (Exception e) {
            System.out.println("There is no 'git' command");
            System.exit(1);
        }
    }

    public static void main(String... args)
    {
        checkGitCommand();

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

        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(new File(workTreePath));
        pb.command("git", "show", "-s", "--format=%ct %h");
        
        Long commitTimeStamp = 0L;
        String commitDateTime = "";
        String commitHash = "";

        try {
            Scanner scan = new Scanner(pb.start().getInputStream());
            commitTimeStamp = scan.nextLong();
            commitHash = scan.next();
        } catch (Exception e) {
            System.out.println("Fail to read commit info");
            System.exit(1);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");

        try {
            commitDateTime = Instant.ofEpochSecond(commitTimeStamp)
                .atZone(ZoneId.systemDefault())
                .format(formatter);
        } catch (DateTimeException e) {
            System.out.println("Bad timestamp");
            System.exit(1);
        }
        
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