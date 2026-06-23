import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;

class SearchResult
{
    public String fileName;
    public int matchCount;

    public SearchResult(String fileName, int matchCount)
    {
        this.fileName = fileName;
        this.matchCount = matchCount;
    }
}

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

            List<SearchResult> results = searchFile(keyword, fileList);
            printResults(results, keyword);

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

    public static List<SearchResult> searchFile(String keyword, List<String> fileList) throws IOException
    {
        List<SearchResult> searchResult = new ArrayList<>();

        for (String file : fileList)    // FILE LOOP
        {
            int matchCount = 0;
            String fileName = Path.of(file).getFileName().toString(); // gets file name separate from path
            // System.out.println(fileName);

            try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) 
            {
                String line = "";
                while ((line = reader.readLine()) != null)   // LINE LOOP
                {
                    // search for a keyword
                    matchCount += searchText(line, keyword);
                }
            } catch(IOException e)
            {
                e.printStackTrace();
            }

            searchResult.add(new SearchResult(fileName, matchCount));
        }

        return searchResult;
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

    public static void printResults(List<SearchResult> results, String keyword)
    {
        System.out.println("\n=== SEARCH RESULTS ===");
        System.out.println("Keyword: " + keyword);
        System.out.println();

        int totalFilesWithMatches = 0;
        int totalMatches = 0;

        for (SearchResult result : results)
        {
            if (result.matchCount > 0)
            {
                System.out.println(result.fileName + " - " + result.matchCount + " match(es)");
                totalFilesWithMatches++;
                totalMatches += result.matchCount;
            }
        }

        if (totalFilesWithMatches == 0)
        {
            System.out.println("No files contained the keyword.");
        }
        else
        {
            System.out.println();
            System.out.println("Summary:");
            System.out.println("Files with matches: " + totalFilesWithMatches);
            System.out.println("Total matches: " + totalMatches);
        }
    }
}

/*
    Searching folder:
    C:\Documents\Me

    Keyword: java

    Results:
    - notes.txt (4 matches)
    - tutorial.txt (2 matches)

    Total files searched: 7
    Files containing keyword: 2
    Total matches: 6
*/