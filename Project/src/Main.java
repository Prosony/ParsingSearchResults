import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.CoreParser;
import pojo.StdOut;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/***
 * Created by Prosony 0.3.2v
 */


public class Main {


    private static StdOut stdOut = new StdOut();
    private int deepList = 0;

    @Parameter(names={"--query", "-q"})
    private static String querySearch;
    @Parameter(names={"--search", "-s"})
    private static String findSearchSystem;
    @Parameter(names={"--deepness", "-d"})
    private static int maxNumberString;
    @Parameter(names={"--offset","-offs"})
    private static int beginNumberString;
    @Parameter(names = {"-proxy","-px"})
    private static List<File> files;

    public static void main(String ... args) {

        Main main = new Main();
        new JCommander(main, args);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //Scanner in = new Scanner(System.in);

        ArrayList dataList = new ArrayList<>();
        Boolean triger = false;
        CoreParser start;
        stdOut.println("_____________version_0.3.2_______________________________");
        stdOut.println("query: " + querySearch + " Search System: " + findSearchSystem + " deepness: " + maxNumberString
                + " beginNumberString: " + beginNumberString);
        stdOut.println(files);

        switch (findSearchSystem){
            case "Google":
                start = new CoreParser(querySearch, findSearchSystem, maxNumberString, files, beginNumberString);
                dataList = start.startWork();
                triger = true;
                break;
            case "google":
                start = new CoreParser(querySearch, findSearchSystem, maxNumberString, files, beginNumberString);
                dataList = start.startWork();
                triger = true;
                break;
            case "Yandex":
                start = new CoreParser(querySearch, findSearchSystem, maxNumberString, files, beginNumberString);
                dataList = start.startWork();
                triger = true;
                break;
            case "yandex":
                start = new CoreParser(querySearch, findSearchSystem, maxNumberString, files, beginNumberString);
                dataList = start.startWork();
                triger = true;
                break;
            default: stdOut.println("error, only --search(-s): Google (google) or Yandex (yandex)");
        }

        if(triger){
            String json = gson.toJson(dataList);
            stdOut.println(json);
        }
        System.exit(0);
    }


}

