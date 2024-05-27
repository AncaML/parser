public class MyResultReduce {
    private String nameDoc;
    private float rang;
    private int lengthMax;
    private int nrWordsMax;

    public MyResultReduce(String nameDoc, float rang, int lengthMax, int nrWordsMax) {
        this.nameDoc = nameDoc;
        this.rang = rang;
        this.lengthMax = lengthMax;
        this.nrWordsMax = nrWordsMax;
    }

    public float getRang() {
        return rang;
    }
    public String getStringRang(){
        return String.format("%.2f", this.rang);
    }

    public String getNameDoc() {
        return nameDoc;
    }

    public int getLengthMax() {
        return lengthMax;
    }

    public int getNrWordsMax() {
        return nrWordsMax;
    }

    @Override
    public String toString() {
        return "MyResultReduce{" +
                "nameDoc='" + nameDoc + '\'' +
                ", rang=" + rang +
                ", lengthMax=" + lengthMax +
                ", nrWordsMax=" + nrWordsMax +
                '}';
    }
}
