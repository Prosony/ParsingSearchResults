package pojo;
/**
 * Created by ${Prosony} on ${24.10.2016}.
 */
public class ParsingList {

    private  Integer number;
    private String url;
    private String domain;
    private String query;
    private String searchSystem;

    public ParsingList(Integer number, String url, String domain, String query, String searchSystem) {

        this.number = number;
        this.url = url;
        this.domain = domain;
        this.query = query;
        this.searchSystem = searchSystem;
    }

    public ParsingList() {
    }

    public Integer getNumber(){
        return number;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSearchSystem(){
        return searchSystem;
    }

    public void setSearchSystem (String searchSystem){
        this.searchSystem = searchSystem;
    }

    @Override public String toString() {
        return number +" "+ url +" "+ domain +" "+ query +" "+ searchSystem;
    }
}
