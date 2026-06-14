import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;

public class ReadAFolder
{
    public static void main(String[] args) throws IOException
    {
        // change this path to your folder containing .txt files
        String folderPath = "C:/Users/brian/Documents/Me";
        List<String> fileList = new ArrayList<>();
        fileList.clear();
        fileList = readFolder(folderPath);

        // read each file in the list and search for a keyword
        for (String fileContent : fileList)
        {
            // Example: search for the keyword "example"
            String keyword = "example";

            if (fileContent.contains(keyword))
            {
                System.out.println("Keyword '" + keyword + "' found in file content.");
            } else
            {
                System.out.println("Keyword '" + keyword + "' not found in file content.");
            }
        }
    }

    public static List<String> readFolder(String folderPath) throws IOException
    {
        List<String> fileList = new ArrayList<>();
        try
        {
            // Ensure the folder exists and is a directory
            Path dir = Paths.get(folderPath);
            if (!Files.exists(dir) || !Files.isDirectory(dir))
            {
                System.err.println("Error: The specified path is not a valid directory.");
                return;
            }

            // List all .txt files in the folder
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt");

            for (Path filePath : stream)
            {
                System.out.println("=== Reading file: " + filePath.getFileName() + " ===");

                try
                {
                    // Read all lines from the file
                    List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

                    fileList.clear();
                    fileList.addAll(lines);
                    return fileList;
                } catch (IOException e)
                {
                    System.err.println("Error reading file " + filePath.getFileName() + ": " + e.getMessage());
                    e.printStackTrace();
                }

            }
        } catch (IOException)
        {
            System.err.println("Error accessing directory: " + e.getMessage());
            e.printStackTrace();
        }
    }
}