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

        for (String file : fileList)    // FILE LOOP
        {
            int count = 0;
            fileName = Path.of(file).getFileName().toString(); // gets file name separate from path
            // System.out.println(fileName);

            try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) // prints file contents
            {
                String line = "";
                while ((line = reader.readLine()) != null)   // LINE LOOP
                {
                    // search for a keyword
                    count += searchText(line, keyword);
                }
            } catch(IOException e)
            {
                e.printStackTrace();
            }

            if (count >= 1)
            {
                System.out.println(keyword + " found in: ");
                System.out.println("- " + fileName + " - " + count + " match(es)");
            }
            else
            {
                System.out.println(keyword + " not found");
            }

            count = 0;
        }
    }

    
    public static int searchText(String fileContent, String keyword)
    {
        // split text into words
        int count = 0;
        String regex = "[,\\.\\s]";
        String[] words = fileContent.split(regex);

        // loop through every word
        for (String word : words)
        {
            if (keyword.equalsIgnoreCase(word))
            {
                count++;
            }
        }
        return count;
    }
}

/*
    searchFile()
        loops through files
        reads contents
        asks searchText() for match count
        prints results

    searchText()
        counts how many times keyword appears
        returns the count

        Split text into words
        Loop through words
        Compare each word with keyword
        Count matches
        Return count
*/