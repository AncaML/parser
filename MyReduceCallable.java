import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

class MyReduceCallable implements Callable<MyResultReduce> {

    String nameDoc;
    List<MyResult> listRes;
    private Map<Integer,Integer> dictionary = new HashMap<>();
    private ArrayList<String> listWords = new ArrayList<>();

    public MyReduceCallable(String nameDoc, List<MyResult> listRes) {
        this.nameDoc = nameDoc;
        this.listRes = listRes;
    }
    public int fib(int n){
        int f1 = 0, f2 = 1, fn;
        for (int i = 1; i <= n; ++i) {
            fn = f1 + f2;
            f1 = f2;
            f2 = fn;
        }
        return f1;
    }
    @Override
    public MyResultReduce call(){
        int maxKey = 0;
        for (MyResult listRe : listRes) {
            Map<Integer, Integer> aux = listRe.getDictionary();
            for (Integer key : aux.keySet()) {
                if (maxKey < key) {
                    maxKey = key;
                }
                if (dictionary.containsKey(key)) {
                    dictionary.replace(key, dictionary.get(key) + aux.get(key));
                } else {
                    dictionary.put(key, aux.get(key));
                }
            }
            listWords.addAll(listRe.getListWords());
        }
        int finalMaxLength = maxKey;
        listWords.removeIf(p ->(p.length() < finalMaxLength));
        float rang;
        int partialSum = 0;
        int nrApr = 0;
        for(Integer len : dictionary.keySet()){
            partialSum = partialSum + (fib(len + 1) * dictionary.get(len));
            nrApr = nrApr + dictionary.get(len);
        }
        rang = (float)partialSum / nrApr;
        return new MyResultReduce(nameDoc, rang, finalMaxLength, listWords.size());
    }
}