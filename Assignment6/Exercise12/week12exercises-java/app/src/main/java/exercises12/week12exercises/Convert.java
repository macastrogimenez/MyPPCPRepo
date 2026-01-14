package week12exercises;

// This class encodes/decodes banktransfers to/from strings
// Please use JSON for professional applications !!!

public class Convert {

    public String encode(int a1, int a2, int amount) {
        return "{" + Integer.toString(a1) + ", " + Integer.toString(a2) + "!" + Integer.toString(amount) + "}";
    }

    public int getA1(String mes) {
        return Integer.parseInt(mes.substring(1, mes.indexOf(",")));
    }

    public int getA2(String mes) {
        return Integer.parseInt(mes.substring(mes.indexOf(",") + 1, mes.indexOf("!")));
    }

    public int getAmount(String mes) {
        return Integer.parseInt(mes.substring(mes.indexOf("!") + 1, mes.length() - 1));
    }
}