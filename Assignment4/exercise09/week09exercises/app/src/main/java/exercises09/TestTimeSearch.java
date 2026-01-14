package exercises09;
// jst@itu.dk * 2025-08-20

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import benchmarking.Benchmark;

public class TestTimeSearch {
  public static void main(String[] args) { new TestTimeSearch(); }

  public TestTimeSearch() {
    final String filename = "src/main/resources/long-text-file.txt";
    final String target= "ipsum";

    
    final ExtendedLongCounter lc = new ExtendedLongCounter();  //Exercise 9.4.1 replaced PrimeCounter by ExtendedLongCounter which is the counter as modified for 9.4.1
    String[] lineArray= readWords(filename);

    System.out.println("Array Size: "+ lineArray.length);
    System.out.println("# Occurences of "+target+ " :"+search(target, lineArray, 0, lineArray.length, lc));

    Benchmark.SystemInfo();
    // Exercise 9.4.3
    Benchmark.Mark7("WordCountSequential", i -> search(target, lineArray, 0, lineArray.length, lc));

    // Exercise 9.4.5 benchmarking the wordCountParallel
    for (int c= 1; c<=32; c++) {
      final int threadCount = c;
      Benchmark.Mark7(String.format("WordCountParallel %7d", threadCount), i -> { return countParallelN(target, lineArray, threadCount, lc);});
    }
  }

  static long search(String x, String[] lineArray, int from, int to, ExtendedLongCounter lc){
    //Search each line of file
    for (int i= from; i<to; i++ ) lc.add(linearSearch(x, lineArray[i]));
    //System.out.println("Found: "+lc.get());
    return lc.get();
  }

  // Exercise 9.4.4
  private static long countParallelN(String target, String[] lineArray, int N, ExtendedLongCounter lc) 
  {
    // uses N threads to search lineArray
    int threadCount = N;
    int range = lineArray.length;
    final int perThread=  range / threadCount;
    Thread[] threads= new Thread[threadCount];
    for (int t= 0; t<threadCount; t++) {
        final int from= perThread * t, 
        to = (t+1==threadCount) ? range : perThread * (t+1); 
        threads[t]= new Thread( () -> {
          for (int i= from; i<to; i++)
            lc.add(linearSearch(target, lineArray[i])); // main line that needed to be changed in comparison with TesCountPrimesThreads
        });
    }
    for (int t= 0; t<threadCount; t++) 
      threads[t].start();
    try {
      for (int t=0; t<threadCount; t++) 
        threads[t].join();
        //System.out.println("Primes: "+lc.get());
    } catch (InterruptedException exn) { }
    return lc.get();
  }

  static int linearSearch(String x, String line) {
    //Search for occurences of c in line
    String[] arr= line.split(" ");
    int count= 0;
    for (int i= 0; i<arr.length; i++ ) if ( (arr[i].equals(x)) ) count++;                   
    return count;
  }

  public static String[] readWords(String filename) {
    try {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        return reader.lines().toArray(String[]::new);   //will be explained in Week10;
    } catch (IOException exn) { return null;}
  }

  
}
