import java.nio.file.Paths;
import java.nio.file.InvalidPathException;

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
        String repoName = "";
        try {
            repoName = pathToName(args[0]);
        } catch (InvalidPathException e) {
            System.out.println("Invalid path");
            System.exit(1);
        }
        System.out.println(repoName);
    }
}