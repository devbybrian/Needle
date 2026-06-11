import java.io.*;

class NeedleSearchEngine
{
    public static void main(String[] args) throws Exception
    {
        try
        {
            // create a new BufferReader
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // get file path
            String path = getFilePath(br);

            // read the file
            BufferedReader fileReader = new BufferedReader(new FileReader(path));

            String cont;

            // ask for a search term
            String keyword = getSearchTerm(br);

            // read the file and search for the term
            readFile(path, keyword);

            fileReader.close();
        } catch (FileNotFoundException e)
        {
            System.out.println("NeedleSearchEngine: " + e);
        } catch (IOException e)
        {
            System.out.println("NeedleSearchEngine: " + e);
        }
    }

    public static String getFilePath(BufferedReader br) throws IOException
    {
        System.out.println("Enter the file path: ");
        return br.readLine();
    }

    public static String getSearchTerm(BufferedReader br) throws IOException
    {
        System.out.println("Enter search keyword: ");
        return br.readLine();
    }

    public static void readFile(String path, String keyword) throws IOException
    {
        BufferedReader fileReader = new BufferedReader(new FileReader(path));
        String cont;

        while ((cont = fileReader.readLine()) != null)
        {
            searchText(cont, keyword, path);
        }
    }

    public static void searchText(String text, String keyword, String path)
    {
        if (text.contains(keyword))
        {
            System.out.println("Found " + keyword + " in: " + path);
        }
        else
        {
            System.out.println("No matches found in: " + path);
        }
    }
}
