import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;

class NeedleSearchEngine
{
    public static void main(String[] args) throws Exception
    {
        try
        {
            // create a new BufferReader
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
            // file list
            List<String> fileList = new ArrayList<>();

            // get file path
            String path = promptFolderPath(br);

            Path folderPath = Path.of(path);
            
            String cont;

            // ask for a search term
            String keyword = getSearchTerm(br);

            // list folder and read the file and search for the term
            fileList = readFolder(folderPath.toString());

            getFolderPath(folderPath);
            readFile(keyword, fileList);

        } catch (FileNotFoundException e)
        {
            System.out.println("NeedleSearchEngine: " + e);
        } catch (IOException e)
        {
            System.out.println("NeedleSearchEngine: " + e);
        }
    }

    public static String promptFolderPath(BufferedReader br) throws IOException
    {
        System.out.println("Enter the folder path: ");
        return br.readLine();
    }

    // read folder
    public static List<String> readFolder(String folderPath) throws IOException
    {
        List<String> results = new ArrayList<>();
        Path dir = Path.of(folderPath);

        // verify if folderPath actually exists
        if (!Files.exists(dir) || !Files.isDirectory(dir))
        {
            System.err.println("Error: The specified path is not a valid directory.");
            return results;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt"))
        {
            for (Path filePath : stream)
            {
                results.add(filePath.toString());
            }
        } catch (IOException e)
        {
            System.err.println("Error accessing directory: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }

    public static String getSearchTerm(BufferedReader br) throws IOException
    {
        System.out.println("Enter search keyword: ");
        return br.readLine();
    }

    public static void readFile(String keyword, List<String> fileList) throws IOException
    {
        // read each file in the list and search for a keyword
        for (String filePath : fileList)
        {
            try
            {
                List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
                String content = String.join("\n", lines);
                searchText(content, keyword, filePath);
            } catch (IOException e)
            {
                System.err.println("Error reading file " + filePath + ": " + e.getMessage());
            }
        }
    }

    public static void searchText(String fileContent, String keyword, String path)
    {
        if (fileContent.contains(keyword))
        {
            System.out.println("Found " + keyword + " in: " + path);
        }
        else
        {
            System.out.println("No files contained '" + keyword + "'");
        }
    }

    // print folder path info
    public static void getFolderPath(Path folderPath)
    {
        if (folderPath == null)
        {
            System.out.println("Folder path is null");
            return;
        }
        System.out.println("Searching folder: " + folderPath.toString());
    }
}

