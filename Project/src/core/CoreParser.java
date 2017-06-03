package core;

import generic.ParsingList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.String.valueOf;

/**
 * Created by Prosony on 27.02.2017.
 */
public class CoreParser {

    /**param for search*/
    private List files;
    private String querySearch, findSearchSystem;
    private int maxNumberString;
    private int deepList;

    private boolean exit = false;
    private int numberProxy = 0;

    private int numberString = 1;

    private Date date = new Date();

    private static Proxy proxy;
    private double percent;
    private int maxProxy;

    private ArrayList<ParsingList> data = new ArrayList<>();
    private ArrayList<String> proxyList = new ArrayList<>();
    private ArrayList<Integer> portList = new ArrayList<>();

    private boolean useProxy = false; // have we proxy or not

    public CoreParser(String querySearch, String findSearchSystem, int maxNumberString, List files, int beginNumberString){

        this.files = files;
        this.querySearch = querySearch;
        this.maxNumberString = maxNumberString;
        this.findSearchSystem = findSearchSystem;
        this.deepList = beginNumberString;
        if(files != null) {
            useProxy = true;
            getProxyAndPort();
        }

    }

    public ArrayList startWork(){// return ArrayList



        if((findSearchSystem.equals("Google"))|(findSearchSystem.equals("google"))){
            percent = 0;
            System.out.println("Open Thread Google");
            parsingGoogle();
        }
        if((findSearchSystem.equals("Yandex"))|(findSearchSystem.equals("yandex"))){
            percent = 0;
            System.out.println("Open Thread Yandex");
            parsingYandex();
        }
        return data;
    }

    private  void parsingGoogle() {
        Elements h3Elements = null;
        Document document = null;
        while(true) {
            if (numberString <= maxNumberString) {
                if (useProxy) {
                    document = connectionWithProxy();
                }else {
                  //  document = connectWithoutProxy();
                    document = connectWithoutProxy();
                }
                if (exit) {
                    break;
                }
                if (document != null) {
                    h3Elements = document.getElementsByAttributeValue("class", "r");
                }
                if (h3Elements != null) {
                    h3Elements.forEach(h3Element -> {
                        if (numberString <= maxNumberString) {
                            System.out.println("hey");
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
                                            percent = (double) numberString / maxNumberString;
                                            System.out.println("Progress :" + percent * 100 + "%");
                                            numberString = numberString + 1;
                                            break;

                                        case "https":

                                            url = url.substring(8);
                                            domain = getDomain(url);
                                            aElement.empty();
                                            data.add(new ParsingList(numberString, url, domain, querySearch, findSearchSystem));
                                            percent = (double) numberString / maxNumberString;
                                            System.out.println("Progress :" + percent * 100 + "%");
                                            numberString = numberString + 1;
                                            break;
                                    }
                                    break;

                                default:
                                    break; // miss table
                            }
                        } else {
                            System.out.println("Table full");
                        }
                    });
                   // data.forEach(System.out::println);
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
        Elements h2Elements = null;
        Document document;
        while(true) {
            if (numberString <= maxNumberString) {
                if (useProxy) {
                    document = connectionWithProxy();
                }else {
                    //  document = connectWithoutProxy();
                    document = connectWithoutProxy();
                }
                if (exit) {
                    break;
                }
                if (document != null) {
                    //h2Elements = document.getElementsByAttributeValue("class", "link organic__url link link_cropped_no");
                    h2Elements = document.getElementsByAttributeValue("class", "link organic__url link_cropped_no i-bem");
                   // System.out.println(h2Elements);
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
                                    percent = (double)numberString/maxNumberString;
                                    System.out.println("Progress :" + percent*100+"%");
                                    numberString = numberString + 1;
                                    break;

                                case "https":
                                    url = url.substring(8);
                                    domain = getDomain(url);
                                    data.add(new ParsingList(numberString, url, domain, querySearch, findSearchSystem));
                                    percent = (double)numberString/maxNumberString;
                                    System.out.println("Progress :" + percent*100+"%");
                                    numberString = numberString + 1;
                                    break;
                            }
                        }else{
                            System.out.println("Table full");
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

    private Document connectionWithProxy() {

        Document document;
        if ((findSearchSystem.equals("Google")) | (findSearchSystem.equals("google"))) {
                while (useProxy) {
                    connectionProxy();
                    try {
                        System.out.println("GOOGLE");
                        document = Jsoup.connect("https://www.google.ru/search?ie=UTF-8&q=" + querySearch
                                + "&num=" + "100" + "&start=" + deepList + "&filter=0").proxy(proxy).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0)"
                                + " Gecko/20100101 Firefox/25.0").followRedirects(true).get();
                        System.out.println("Connection with Proxy OK");
                        return document;
                    } catch (IOException e) {
                        errorHandle(e);
                    }
                }
        }
        if ((findSearchSystem.equals("Yandex")) | (findSearchSystem.equals("yandex"))){
            System.out.println("connectionWithProxy methods Yandex!__");
            while (useProxy) {
                connectionProxy();
                try {
                    System.out.println("YANDEX");
                    document = Jsoup.connect("https://yandex.ru/search/?lr=119271&text="
                            + querySearch + "&p=" + deepList).proxy(proxy).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0)"
                            + " Gecko/20100101 Firefox/25.0").followRedirects(true).get();
                    System.out.println("Connection with Proxy OK");
                    return document;
                } catch (IOException e) {
                    errorHandle(e);
                }
            }
        }
        return null;
    }

    private void connectionProxy(){
        if (proxyList.get(numberProxy) != null){
            proxy = new Proxy(
                    Proxy.Type.SOCKS,
                    InetSocketAddress.createUnresolved(proxyList.get(numberProxy), portList.get(numberProxy))
            );
        }
    }
    private void deleteProxy(){
        if ((maxProxy > 0)){
            proxyList.remove(numberProxy);
            portList.remove(numberProxy);
            maxProxy = maxProxy - 1;
            System.out.println("proxy was deleted");
            System.out.println("proxyList.toArray().length: " + proxyList.toArray().length);
            System.out.println("maxProxy: " + maxProxy);
            System.out.println(proxyList);
            System.out.println(portList);
        }else {
            proxyList.remove(numberProxy);
            portList.remove(numberProxy);
            System.out.println(" LAST proxy was deleted!");
            useProxy = false;
        }
    }
    private boolean errorHandle(IOException e){

        String stringError;
        String[] proxyError = { "org.jsoup.HttpStatusException: HTTP error fetching URL. Status=403",
                "org.jsoup.HttpStatusException: HTTP error fetching URL. Status=404",
                "org.jsoup.HttpStatusException: HTTP error fetching URL. Status=503",
        };
        stringError = e.toString();
        System.out.println("string Error: " + stringError);
        if (stringError.length() > 66){stringError = stringError.substring(0,66);}

        if ((stringError.equals(proxyError[0]))|(stringError.equals(proxyError[1]))|(stringError.equals(proxyError[2]))) {
            System.out.println("Do something with Capcha");
        }
        deleteProxy();
        return true;
    }

    /**_________________________________________________Parsing_without_proxy_____________________________________________*/
    private Document connectWithoutProxy(){
        Document doc = null;

        if ((findSearchSystem.equals("Google"))|(findSearchSystem.equals("google"))) {
            try {
                System.out.println("Pars Google Without proxy ");
                doc = Jsoup.connect("https://www.google.ru/search?ie=UTF-8&q=" + querySearch
                        + "&num=" + "100" + "&start=" + deepList + "&filter=0").userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0)"
                        + " Gecko/20100101 Firefox/25.0").followRedirects(true).get();
            } catch (IOException e) {
                exit = true;
                logOut(e);
                e.printStackTrace();
            }
        }
        if ((findSearchSystem.equals("Yandex"))|(findSearchSystem.equals("yandex"))){
            try {
                System.out.println("Pars Yandex Without proxy ");
                doc = Jsoup.connect("https://yandex.ru/search/?lr=119271&text="
                        + querySearch + "&p="+deepList).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0)"
                        + " Gecko/20100101 Firefox/25.0").followRedirects(true).get();
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
            System.out.println("proxy№ " + i +": "+ proxyList.get(i));
            System.out.println("port№ " + i +": "+ portList.get(i));
            i++;
        }
    }
    private Date getDate() {
        return date;
    }

    private void logOut(IOException e){
        System.err.println(getDate()+ " " + valueOf(e) + " Error on Persent: " + percent*100);
    }
    private void logOut(InterruptedException e){
        System.out.println(getDate() + valueOf(e) + "Error on Persent: " + percent*100);
    }
}