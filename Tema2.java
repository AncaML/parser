import java.io.*;
import java.util.*;
import java.util.concurrent.*;
public class Tema2 {
    public Tema2() {
    }

    public void action(ArrayList<String> listaFiles, int d, String sep, List<MyMapCallable> listaT){
        int offset, bytesRead;
        for (String listaFile : listaFiles) {
            offset = 0;
            bytesRead = 0;
            try {
                RandomAccessFile doc = new RandomAccessFile(listaFile, "r");
                long bytes = doc.length();
                while (bytesRead != -1) {
                    byte[] bytesR = new byte[d];
                    bytesRead = doc.read(bytesR, 0, d);
                    String sir = new String(bytesR);
                    if (bytesRead > 0) {
                        int nouInceput;
                        int nouSfarsit = 0;
                        if (offset != 0) { //nu sunt la inceput de file
                            String firstCh = String.valueOf(sir.indexOf(0));
                            if (!sep.contains(firstCh)) {
                                doc.seek(offset - 1);
                                byte[] findSep = new byte[1];
                                doc.readFully(findSep);
                                String beforeLastCh = new String(findSep);
                                if (!sep.contains(beforeLastCh)) {
                                    int j;
                                    for (j = 0; j < bytesRead; j++) {
                                        if (sep.contains(String.valueOf(sir.charAt(j)))) {
                                            break;
                                        }
                                    }
                                    if (j == bytesRead && offset + j == bytes) {
                                        nouInceput = (int) bytes;
                                    } else {
                                        nouInceput = offset + j;
                                    }
                                } else {
                                    nouInceput = offset;
                                }
                            } else {
                                nouInceput = offset;
                            }
                        } else {
                            nouInceput = offset;
                        }
                        String lastCh = String.valueOf(sir.charAt(bytesRead - 1));
                        if (!sep.contains(lastCh)) {
                            if (offset + bytesRead < bytes) {
                                doc.seek(offset + bytesRead);
                                int j;
                                int dif;
                                if (bytes - (offset + bytesRead) > d) {
                                    dif = d;
                                } else {
                                    dif = (int) bytes - (offset + bytesRead);
                                }
                                for (j = 0; j < dif; j++) {
                                    byte[] findS = new byte[1];
                                    doc.readFully(findS);
                                    String firstSep = new String(findS);
                                    if (sep.contains(firstSep)) {
                                        break;
                                    }
                                }
                                if (j != dif || dif + offset + bytesRead == (int) bytes) {
                                    nouSfarsit = offset + bytesRead + j;
                                }
                            } else {
                                nouSfarsit = offset + bytesRead;
                            }
                        } else {
                            nouSfarsit = offset + bytesRead;
                        }
                        if (nouSfarsit - nouInceput != 0) {
                            MyMapCallable task2 = new MyMapCallable(listaFile, nouInceput, nouSfarsit - nouInceput);
                            listaT.add(task2);
                        }
                    }

                    offset = offset + bytesRead;
                    doc.seek(offset);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: Tema2 <workers> <in_file> <out_file>");
            return;
        }
        int workers = Integer.parseInt(args[0]);
        Tema2 obj = new Tema2();
        String sep = ";:/?~\\.,><`[]{}()!@#$%^&-_+'=*\"| \t\r\n";
        File input = new File(args[1]);
        int d = 0, nrFiles;
        String file;
        List<MyMapCallable> listaT = new ArrayList<>();
        ArrayList<String> listaFiles = new ArrayList<>();
        try {
            BufferedReader read = new BufferedReader(new FileReader(input));
            d = Integer.parseInt(read.readLine());
            nrFiles = Integer.parseInt(read.readLine());
            for(int i = 0; i < nrFiles; i++){
                file = read.readLine();
                listaFiles.add(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExecutorService tpe = Executors.newFixedThreadPool(workers);
        obj.action(listaFiles, d, sep, listaT);
        List<Future<MyResult>> res = null;
        try {
            res = tpe.invokeAll(listaT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String,List<MyResult>> results = new HashMap<>();
        List<MyReduceCallable> listaR = new ArrayList<>();
        if(res != null) {
            for (Future<MyResult> resF : res) {
                try {
                    MyResult taskRes = resF.get();
                    if (results.containsKey(taskRes.getNameDoc())) {
                        List<MyResult> aux = results.get(taskRes.getNameDoc());
                        aux.add(taskRes);
                    } else {
                        List<MyResult> fin = new ArrayList<>();
                        fin.add(taskRes);
                        results.put(taskRes.getNameDoc(), fin);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            }
            List<Future<MyResultReduce>> resFIN = null;
            for(String key : results.keySet()){
                MyReduceCallable taskR = new MyReduceCallable(key, results.get(key));
                listaR.add(taskR);
            }
            try {
                resFIN = tpe.invokeAll(listaR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tpe.shutdown();
            List<MyResultReduce> finalTouch = new ArrayList<>();

            if(resFIN != null){
                for(Future<MyResultReduce> resu : resFIN){
                    try {
                        MyResultReduce aux = resu.get();
                        finalTouch.add(aux);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                finalTouch.sort(new SortMyReduce());
                try {
                    BufferedWriter fileOut = new BufferedWriter(new FileWriter(args[2]));
                    for(MyResultReduce w : finalTouch){
                        fileOut.write(w.getNameDoc() + ","+w.getStringRang() +"," +w.getLengthMax() + "," + w.getNrWordsMax() +"\n");
                    }
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
