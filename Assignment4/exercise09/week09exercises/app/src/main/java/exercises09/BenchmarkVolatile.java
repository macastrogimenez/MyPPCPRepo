package exercises09;

import benchmarking.Benchmark;

class BenchmarkVolatile {
    public static void main(String[] args) { 
        new BenchmarkVolatile(); 
    }
    
    public BenchmarkVolatile() {
        Benchmark.SystemInfo();
        
        TestVolatile tv = new TestVolatile();
        
        Benchmark.Mark7("non-volatile inc", i -> {
            tv.inc();
            return 0.0;
        });
        
        Benchmark.Mark7("volatile inc", i -> {
            tv.vInc();
            return 0.0;
        });
    }
}