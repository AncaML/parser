import java.util.Comparator;

public class SortMyReduce implements Comparator<MyResultReduce> {

    @Override
    public int compare(MyResultReduce o1, MyResultReduce o2) {
        if (o2.getRang() - o1.getRang() > 0){
            return 1;
        }
        else if(o2.getRang() - o1.getRang() < 0){
            return -1;
        }
        return 0;
    }
}