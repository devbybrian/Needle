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

            // get folder path
            String path = promptFolderPath(br);
            Path folderPath = Path.of(path);

            // ask for a search term
            String keyword = getSearchTerm(br);

            // list folder and read the file and search for the term
            fileList = readFolder(folderPath.toString());

            getFolderPath(folderPath);
            searchFile(keyword, fileList);

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

    public static void searchFile(String keyword, List<String> fileList) throws IOException
    {
        String fileName = "";
        keyword = keyword.toLowerCase();
        List<String> matchingFiles = new ArrayList<>();

        // read each file in the list and search for a keyword
        for (String filePathStr : fileList)
        {
            try
            {
                Path filePath = Paths.get(filePathStr); // convert string to Path
                List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
                String content = String.join("\n", lines).toLowerCase();
                

                fileName = filePath.getFileName().toString(); // extract just the file name
                
                if (searchText(content, keyword))
                {
                    matchingFiles.add(fileName);
                }                
            } catch (IOException e)
            {
                System.err.println("Error reading file " + filePathStr + ": " + e.getMessage());
            }
        }
        
        if (matchingFiles.isEmpty())
        {
            System.out.println("No files contained '" + keyword + "'");
        }
        else
        {
            System.out.println("Found " + keyword + " in: ");
            for (String file : matchingFiles)
            {
                System.out.println("- " + file);
            }
        }
    }

    public static boolean searchText(String fileContent, String keyword)
    {
        return fileContent.contains(keyword);
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

