public class HexagonPrinting {

    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    private static Object prinkHex(){
        Object hex = new Object();
        int count =5;
        int count2 = 0;
        boolean firstEntry = false;
        for(int j=0;j<6;j++) {
            for (int i = 0; i < 11; i++) {
                if (i < count) {
                    System.out.print(" ");

                } else
//                    if(firstEntry=false){
//                        print
//                    }
                    System.out.print(ANSI_YELLOW + " x" + ANSI_RESET);
            }
            count-=1;
            System.out.println();
        }
        for(int j=0;j<6;j++) {
            for (int i = 0; i < 11; i++) {
                if (i > count) {
                    System.out.print(ANSI_YELLOW + " x" + ANSI_RESET);

                } else
                    System.out.print(" ");
            }
            count+=1;
            System.out.println();
        }
        return hex;
    }

    public static void main(String[] args) {

        Object hex = new Object();
        prinkHex();

    }
}
