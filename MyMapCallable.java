import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

class MyMapCallable implements Callable<MyResult> {
    String nameDoc;
    int offset;
    int size;
    String regex = "[;:/?~.,><`\\[\\]{}()!@#$%^&\\-_+'=*\"| \\t\\r\\n]";
    Map<Integer,Integer> dictionary = new HashMap<>();
    List<String> listWords;

    public MyMapCallable(String nameDoc, int offset, int size) {
        this.nameDoc = nameDoc;
        this.offset = offset;
        this.size = size;
    }
    @Override
    public MyResult call() {
        try {
            RandomAccessFile file = new RandomAccessFile(nameDoc,"r");
            byte[] sentence = new byte[this.size];
            file.seek(this.offset);
            file.readFully(sentence);
            String s = new String(sentence);
            String[] words = s.split(regex);
            int maxi = 0;
            for (String word : words) {
                if (word.length() > 0) {
                    if (dictionary.containsKey(word.length())) {
                        dictionary.replace(word.length(), dictionary.get(word.length()) + 1);
                    } else {
                        dictionary.put(word.length(), 1);
                    }
                }
                if (word.length() >= maxi) {
                    maxi = word.length();
                }
            }
            listWords = new ArrayList<>(Arrays.asList(words));
            int finalMaxi = maxi;
            listWords.removeIf(p -> (p.length() < finalMaxi));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] lastPath = nameDoc.split("/");
        return new MyResult(dictionary, listWords, lastPath[lastPath.length - 1]);
    }
}