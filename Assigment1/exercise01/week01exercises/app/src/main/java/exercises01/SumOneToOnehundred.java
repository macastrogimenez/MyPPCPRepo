package exercises01;

public class SumOneToOnehundred {
    private long start, spent;
    // start= System.nanoTime();   
    // "code you are measuring"
    // spent += System.nanoTime()-start;

    public SumOneToOnehundred(){
        printSums();
        System.out.println("Your computer spent "+this.spent+" nanoseconds adding the numbers from 1 to 100");
    }

    public void printSums(){
        int acc = 0;
        start = System.nanoTime();
        int[] numbers = new int[100];
        for(int i = 1; i<=100; i++){
            numbers [i-1]=i;
        };
        for (int i : numbers) {
            acc = acc + i;
        }
        //System.out.println(acc);
        spent += System.nanoTime()-start;

    }    
    public static void main(String[] args) {
        new SumOneToOnehundred();
    }
    
}


