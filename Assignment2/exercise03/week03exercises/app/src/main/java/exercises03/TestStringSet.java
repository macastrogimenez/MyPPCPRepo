// For week 3
// raup@itu.dk * 2025-09-03

package exercises03;

import java.util.List;
import java.util.ArrayList;

public class TestStringSet {

    StringSet s; 

    public TestStringSet() throws InterruptedException {
        s = new StringSet(); // (init(s))

        Thread t1 = new Thread(() -> { // (init(t1))
                s.findOrAdd("PCPP");
        });
        Thread t2 = new Thread(() -> { // (init(t2))
                s.find("PCPP"); 
        });

        t1.start(); // (start(t1))
        t2.start(); // (start(t2))
    }



    // Simplification of the class StringNumberer https://github.com/soot-oss/soot/blob/9290783ff21cccbc3a10cea5e97e0bfb352071c1/src/main/java/soot/util/StringNumberer.java#L36
    public class StringSet {

        private final List<String> list = new ArrayList<String>();

        public synchronized int findOrAdd(String s) {
            //lock here (lock())
            int ret = list.indexOf(s); // read access on the list (1)
            if (ret == -1) {      
                list.add(s); // write access on the list (2)
            }
            return ret;
            //unlock here (unlock()) 
        }

        public synchronized int find(String s) {
            return list.indexOf(s); // read access on the list (3)
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new TestStringSet(); 
    }
}
