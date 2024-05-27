import java.util.List;
import java.util.Map;

public class MyResult {
    private Map<Integer,Integer> dictionary;
    private List<String> listWords;
    private String nameDoc;

    public MyResult(Map<Integer, Integer> dictionary, List<String> listWords, String nameDoc) {
        this.dictionary = dictionary;
        this.listWords = listWords;
        this.nameDoc = nameDoc;
    }

    public Map<Integer, Integer> getDictionary() {
        return dictionary;
    }

    public List<String> getListWords() {
        return listWords;
    }

    public String getNameDoc() {
        return nameDoc;
    }

    public void setNameDoc(String nameDoc) {
        this.nameDoc = nameDoc;
    }

    @Override
    public String toString() {
        return "MyResult{" +
                "dictionary=" + dictionary +
                ", listWords=" + listWords +
                ", nameDoc='" + nameDoc + '\'' +
                '}';
    }
}
