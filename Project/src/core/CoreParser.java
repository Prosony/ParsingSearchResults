package core;
/**
 * Created by Prosony on 27.02.2017.
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import generic.ParsingList;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static java.lang.String.valueOf;

public class CoreParser {

    private static Document doc;
    private static List files;
    private static StdOut stdOut = new StdOut();

    private boolean exit = false;
    private boolean proxySwitch = false;
    private boolean deleteProxy = false;
    private int numberProxy;
    private int deepList;
    private int numberString = 1;
    private int maxNumberString;

    private Elements h3Elements;
    private Elements h2Elements;
    private ArrayList<ParsingList> data = new ArrayList<>();
    private final Random random = new Random();
    private Date date = new Date();


    private String querySearch, findSearchSystem;

    private static Proxy proxy;
    private static double persent;
    private static int maxProxy;

    private ArrayList<String> proxyList = new ArrayList<>();
    private ArrayList<Integer> portList = new ArrayList<>();

    public CoreParser(String querySearch, String findSearchSystem, int maxNumberString, List files, int beginNumberString){

        CoreParser.files = files;
        this.querySearch = querySearch;
        this.maxNumberString = maxNumberString;
        this.findSearchSystem = findSearchSystem;
        this.deepList = beginNumberString;
        getProxyAndPort();
    }
    //Turn on pars._____________________________________________________
    public ArrayList startWork(){// return ArrayList

        if((findSearchSystem.equals("Google"))|(findSearchSystem.equals("google"))){
                                    persent = 0;
                                    stdOut.println("Open Thread Google");
                                    parsingGoogle();
        }
        if((findSearchSystem.equals("Yandex"))|(findSearchSystem.equals("yandex"))){
                                    persent = 0;
                                    stdOut.println("Open Thread Yandex");
                                    parsingYandex();

        }
        return data;
    }

    private  void parsingGoogle() {

        Document doc;
        while(true) {
            if (numberString <= maxNumberString) {
                pauseParser();
                doc = connectionWithProxy();
                if (proxySwitch) {
                    doc = connectWithoutProxy();
                }
                if (exit) {
                    break;
                }

                if (doc != null) {
                    h3Elements = doc.getElementsByAttributeValue("class", "r");
                }
                if (h3Elements != null) {
                    h3Elements.forEach(h3Element -> {
                        if (numberString <= maxNumberString) {
                            stdOut.println("hey");
                            String domain;
                            Element aElement = h3Element.child(0);
                            String url = aElement.attr("class");

                            switch (url) {
                                case "":
                                    url = aElement.attr("href");
                                    switch (url.substring(0,5)) {
                                        case "http:":

                                            url = url.substring(7);
                                            domain = getDomain(url);
                                            aElement.empty();
                                            data.add(new ParsingList(numberString, url, domain, querySearch, findSearchSystem));
                                            persent = (double) numberString / maxNumberString;
                                            stdOut.println("Progress :" + persent * 100 + "%");
                                            numberString = numberString + 1;
                                            break;

                                        case "https":

                                            url = url.substring(8);
                                            domain = getDomain(url);
                                            aElement.empty();
                                            data.add(new ParsingList(numberString, url, domain, querySearch, findSearchSystem));
                                            persent = (double) numberString / maxNumberString;
                                            stdOut.println("Progress :" + persent * 100 + "%");
                                            numberString = numberString + 1;
                                            break;
                                    }
                                    break;

                                default:
                                    break; // miss table
                            }
                        } else {
                            stdOut.println("Table full");
                        }
                    });
                    data.forEach(System.out::println);
                }
                deepList = deepList + 100;
            }else {
                deepList = 0;
                numberString = 1;
                break;
            }
        }
    }

    private void parsingYandex() {
        Document doc;
        stdOut.println("yandex pars...");
        while(true) {
            if (numberString <= maxNumberString) {
                pauseParser();
                doc = connectionWithProxy();
                if (proxySwitch) {
                doc = connectWithoutProxy();
                }
                if (exit) {
                break;
                }
                if (doc != null) {
                h2Elements = doc.getElementsByAttributeValue("class", "link organic__url link link_cropped_no");
                }
                if (h2Elements != null) {
                    h2Elements.forEach((Element h2Element) -> {
                        if (numberString <= maxNumberString) {

                                        String url = h2Element.attr("href");
                                        String domain;
                                        switch (url.substring(0,5)) {
                                            case "http:":
                                                url = url.substring(7); //URL
                                                domain = getDomain(url);
                                                data.add(new ParsingList(numberString, url, domain, querySearch, findSearchSystem));
                                                persent = (double)numberString/maxNumberString;
                                                stdOut.println("Progress :" + persent*100+"%");
                                                numberString = numberString + 1;
                                                break;

                                            case "https":
                                                url = url.substring(8);
                                                domain = getDomain(url);
                                                data.add(new ParsingList(numberString, url, domain, querySearch, findSearchSystem));
                                                persent = (double)numberString/maxNumberString;
                                                stdOut.println("Progress :" + persent*100+"%");
                                                numberString = numberString + 1;
                                                break;
                                        }
                        }else{
                            stdOut.println("Table full");
                        }
                    });
                    //data.forEach(System.out::println);
                }
                deepList = deepList + 1;
            }else{
                numberString = 1;
                deepList = 0;
                break;
            }
        }
    }



    private boolean nextProxy(){

        if (proxyList.get(numberProxy) != null){
                    if((numberProxy <= maxProxy)){

                                            if (((deleteProxy))){
                                                if ((maxProxy > 0)){

                                                                proxyList.remove(numberProxy);
                                                                portList.remove(numberProxy);
                                                                maxProxy = maxProxy - 1;
                                                                stdOut.println("We have ");
                                                                stdOut.println("proxyList.toArray().length: " + proxyList.toArray().length);
                                                                stdOut.println("maxProxy: " + maxProxy);
                                                                stdOut.println(proxyList);
                                                                stdOut.println(portList);
                                                                deleteProxy = false;
                                                }else{
                                                    proxyList.remove(numberProxy);
                                                    portList.remove(numberProxy);
                                                    proxyList.add(null);
                                                    portList.add(null);
                                                    proxySwitch = true;
                                                    deleteProxy = false;
                                                    return false;
                                                }

                                            }
                        stdOut.println("_______________________________________________________________________________________");
                        stdOut.println("with proxy...");
                        stdOut.println("Connecting proxy : port " + proxyList.get(numberProxy) + " " + portList.get(numberProxy));

                        proxy = new Proxy(
                                Proxy.Type.SOCKS,
                                InetSocketAddress.createUnresolved(proxyList.get(numberProxy), portList.get(numberProxy))
                        );

                        return true;
                   }
        }

        proxySwitch = true;
        stdOut.println("Proxy list is empty");
        return false;

    }

    private Document connectionWithProxy(){

        boolean parseConnectProxy = true;
        nextProxy();

        if(proxyList.get(numberProxy) != null) {
            if ((findSearchSystem.equals("Google")) | (findSearchSystem.equals("google"))) {
                while (parseConnectProxy) {
                    try {
                        stdOut.println("GOOGLE");
                        doc = Jsoup.connect("https://www.google.ru/search?ie=UTF-8&q=" + querySearch
                                + "&num=" + "100" + "&start=" + deepList + "&filter=0").proxy(proxy).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0)"
                                + " Gecko/20100101 Firefox/25.0").followRedirects(true).get();
                        stdOut.println("Connection with Proxy");
                        parseConnectProxy = false;
                    } catch (IOException e) {
                        parseConnectProxy = errorHandle(e);
                    }
                }
            }
            if ((findSearchSystem.equals("Yandex")) | (findSearchSystem.equals("yandex"))){
                while (parseConnectProxy) {
                    try {
                        stdOut.println("YANDEX");
                        doc = Jsoup.connect("https://yandex.ru/search/?lr=119271&text="
                                + querySearch + "&p=" + deepList).proxy(proxy).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0)"
                                + " Gecko/20100101 Firefox/25.0").followRedirects(true).get();
                        stdOut.println("Connection with Proxy OK");
                        parseConnectProxy = false;
                    } catch (IOException e) {
                        parseConnectProxy = errorHandle(e);
                    }
                }

            }
        }
        return doc;
    }

    private boolean errorHandle(IOException e){
        String stringError;
        String[] proxyError = { "org.jsoup.HttpStatusException: HTTP error fetching URL. Status=403",
                                "org.jsoup.HttpStatusException: HTTP error fetching URL. Status=404",
                                "org.jsoup.HttpStatusException: HTTP error fetching URL. Status=503",
                                };
        boolean parseConnectProxy;
        stringError = e.toString();
        stdOut.println("string Error: " + stringError);
        if (stringError.length() > 66){stringError = stringError.substring(0,66);}

        if ((stringError.equals(proxyError[0]))|(stringError.equals(proxyError[1]))|(stringError.equals(proxyError[2]))) {
                            stdOut.println("Do something with Capcha");
        }
        stdOut.println("_______________________________________________________________________________________");
        /* else {
                            stdOut.println("proxySwitch: " + proxySwitch);
                            stdOut.println("string Error: " + stringError);
                            logOut(e);
                            exit = true;
                            parseConnectProxy = false;
                            e.printStackTrace();
        }*/
        deleteProxy = true;
        parseConnectProxy = nextProxy();
        return  parseConnectProxy;

    }
    private Document connectWithoutProxy(){
        Document doc = null;
        stdOut.println("List proxy : port " + proxyList.get(numberProxy) + " " + portList.get(numberProxy));
        if ((findSearchSystem.equals("Google"))|(findSearchSystem.equals("google"))) {
            try {
                stdOut.println("Pars Google Without proxy ");
                doc = Jsoup.connect("https://www.google.ru/search?ie=UTF-8&q=" + querySearch
                        + "&num=" + "100" + "&start=" + deepList + "&filter=0").userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0)"
                        + " Gecko/20100101 Firefox/25.0").followRedirects(true).get();
                h3Elements = doc.getElementsByAttributeValue("class", "r");
            } catch (IOException e) {
                exit = true;
                logOut(e);
                e.printStackTrace();
            }
        }else{
            try {
                stdOut.println("Pars Yandex Without proxy ");
                doc = Jsoup.connect("https://yandex.ru/search/?lr=119271&text="
                        + querySearch + "&p="+deepList).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0)"
                        + " Gecko/20100101 Firefox/25.0").followRedirects(true).get();
                h2Elements = doc.getElementsByAttributeValue("class", "link organic__url link link_cropped_no");
            } catch (IOException e) {
                exit = true;
                e.printStackTrace();
            }
        }
        return doc;
    }
    /**_________________________________________________Other_methods_____________________________________________________*/
    private String getDomain(String url){
        String domain;
        int i = 1;
        while (true) {
            if (url.charAt(i) == '/') {
                domain = url.substring(0, i);
                break;
            }
            i = i + 1;
        }
        return domain;
    }

    private void pauseParser(){
        int timeOut = rand();
        try {
            stdOut.println("Pause: "+timeOut+" millisecond");
            Thread.sleep(timeOut);
        } catch (InterruptedException e) {
            logOut(e);
            e.printStackTrace();
        }
    }

    private void getProxyAndPort(){

    maxProxy = files.toArray().length - 1;
    int i = 0;
    while(true) {
        if (i <= maxProxy) {

            int count = 0;
            String part;
            part = String.valueOf(files.get(i));

            while(count < part.length()){

                if (part.charAt(count) == ':'){

                    proxyList.add(part.substring(0 , count));
                    portList.add(Integer.parseInt(part.substring(count+1, part.length())));
                    count = part.length();
                }
                count++;
            }
        }else{
            break;
        }
        stdOut.println("proxy№ " + i +": "+ proxyList.get(i));
        stdOut.println("port№ " + i +": "+ portList.get(i));
        i++;
    }
}
    private Date getDate() {
        return date;
    }

    private void logOut(IOException e){
        System.err.println(getDate()+ " " + valueOf(e) + " Error on Persent: " + persent*100);
    }
    private void logOut(InterruptedException e){
        stdOut.println(getDate() + valueOf(e) + "Error on Persent: " + persent*100);
    }

    private int rand() {
        int timeOut;
        timeOut = random.nextInt(10);
            switch (timeOut) {
                case 0: timeOut = 5000; break;
                case 1: timeOut = 6000; break;
                case 2: timeOut = 7000; break;
                case 3: timeOut = 8000; break;
                case 4: timeOut = 9000; break;
                case 5: timeOut = 10000; break;
                case 6: timeOut = 11000; break;
                case 7: timeOut = 12000; break;
                case 8: timeOut = 13000; break;
                default: timeOut = 5000; break;
            }
        return timeOut;
    }



}

