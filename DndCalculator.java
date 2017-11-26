/*
 * This Program was written by Jacob S. Lisner
 * Packaged by Perry Cate
 * The StdIn and StdOut classes are from Princeton's Algs4 package used in
 * COS126 and COS226.
 */
public class DndCalculator {
    
    private int reduce;
    private int valueprime;
    private class Node {
        private int id;
        private double value;
        private char function;
        private int value2;
        public Node () {
            id = 0;
        }
        public Node (double input) {
            id = 1;
            value = input;
        }
        public Node (char input) {
            id = 2;
            function = input;
        }
        public Node(int input) {
            id = 3;
            value2 = input;
        }
    }
    public DndCalculator() {
        reduce = 0;
        valueprime = 0;
    }
    //performs math functions
    public double math (String input) {
        Node[] set = new Node[input.length()];
        int nodelength = input.length();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                String send = input.substring(i+1, input.length());
                int current = 1;
                if (i != 0 && set[i-1].id == 2 && set[i-1].function == 'r') {
                    int k = i - 2;
                    current = 0;
                    set[k+1].id = 0;
                    int mult = 1;
                    while (k != -1 && set[k].id == 3 ) {
                        current = current + mult*set[k].value2;
                        mult = mult*10;
                        set[k].id = 0;
                        k--;
                    }
                }
                i++;
                double pvalue = 0;
                for (int k = 0; k < current; k++) {
                    pvalue = pvalue + math(send);
                }
                set[i] = new Node(pvalue);
                if (i - 2 != -1 && (input.charAt(i-2) == ')' || 
                                    Character.isDigit(input.charAt(i-2))))  
                    set[i-1] = new Node('*');
                else
                    set[i-1] = new Node();
                for (int j = 0; j < reduce; j++) {
                    i++;
                    set[i] = new Node();
                }
                if (i != input.length() - 1 && (Character.isDigit(input.charAt(i+1))))
                    set[i-1] = new Node('*');
            }
            else if (input.charAt(i) == ')') {
                reduce = i;
                nodelength = i;
                break;
            }
            else if (input.charAt(i) == ' ')
                set[i] = new Node();
            else if (Character.isDigit(input.charAt(i))) {
                set[i] = new Node((Integer.parseInt((""+input.charAt(i)))));
            }
            else {
                set[i] = new Node(input.charAt(i));
            }
        }
        //converts sequence of ints to one int
        for (int i = 0; i < nodelength; i++) {
            if (set[i].id == 3) {
                int dist = 0;
                while (i != nodelength - 1 && set[i+1].id == 3) {
                    set[i+1].id = 0;
                    set[i - dist].value2 = 10*set[i - dist].value2 + set[i+1].value2;
                    i++;
                    dist++;
                }
            }
        }
        //calculates dice rolls
        for (int i = 0; i < nodelength; i++) {
            if (set[i].id == 2 && set[i].function == 'd') {
                set[i].id = 0;    
                int j = i-1;
                int first;
                int second;
                while(set[j].id != 3)
                    j--;
                
                first = set[j].value2;
                set[j].id = 0;
                j = i + 1;
                while(set[j].id != 3)
                    j++;
                second = set[j].value2;
                int subtotal = 0;
                for (int k = 0; k < first; k++)
                    subtotal = subtotal + ((int) (Math.random()*second)) + 1;
                set[j].value2 = subtotal;
            }
        }
        for (int i = 0; i < nodelength; i++) {
            if (set[i].id == 2 && set[i].function == '^') {
                set[i].id = 0;    
                int j = i-1;
                double first;
                double second;
                while(set[j].id != 3 && set[j].id != 1) {
                    j--;
                }
                if (set[j].id == 3)
                    first = (double) set[j].value2;
                else
                    first = set[j].value;
                set[j].id = 0;
                j = i + 1;
                
                while(set[j].id != 3 && set[j].id != 1) {
                    j++;
                }
                if (set[j].id == 3)
                    second = (double) set[j].value2;
                else
                    second = set[j].value;
                set[j].id = 1;
                    set[j].value = Math.pow(first, second);
            }
        }
        
        for (int i = 0; i < nodelength; i++) {
            if (set[i].id == 2 && (set[i].function == '*' || set[i].function == '/')) {
                set[i].id = 0;    
                int j = i-1;
                double first;
                double second;
                while(set[j].id != 3 && set[j].id != 1) {
                    j--;
                }
                if (set[j].id == 3)
                    first = (double) set[j].value2;
                else
                    first = set[j].value;
                set[j].id = 0;
                j = i + 1;
                
                while(set[j].id != 3 && set[j].id != 1) {
                    j++;
                }
                if (set[j].id == 3)
                    second = (double) set[j].value2;
                else
                    second = set[j].value;
                set[j].id = 1;
                if (set[i].function == '*')
                    set[j].value = first*second;
                else
                    set[j].value = first/second;    
            }
        }
        
        double value = 0.0;
        boolean operator = true;
        for (int i = 0; i < nodelength; i++) {
            if(set[i].id == 0)
                continue;
            else if (set[i].id == 1 || set[i].id == 3) {
                double current;
                if (set[i].id == 1)
                    current = set[i].value;
                else
                    current = (double) set[i].value2;
                if (operator)
                    value = value + current;
                else
                    value = value - current;
            }
            else {
                if (set[i].function == '+')
                    operator = true;
                else
                    operator = false;
            }           
        }
        return value;
    }
    public static void main(String[] args) {
        DndCalculator output = new DndCalculator();
        while (!StdIn.isEmpty()) {
            String line = StdIn.readLine();
            if (line.equals("done"))
                break;
            else if (line.equals("help")) {
                System.out.println();
                System.out.println("Functions:");
                System.out.println("+ addition");
                System.out.println("- subtraction");
                System.out.println("* multiplication");
                System.out.println("/ division");
                System.out.println("^ power");
                System.out.println("() order-of-operations");
                System.out.println("#d# roll (first)-number of (second) sided dice");
                System.out.println("#r() add # iterations of function in ()");
                System.out.println("help: pull up this menu");
                System.out.println("done: end program");
                System.out.println();
            }
            else {         
                double value = output.math(line);
                System.out.println(value);
            }
            
        }
    }
}
