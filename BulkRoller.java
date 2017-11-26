import java.lang.System;
import java.util.Scanner;


public class BulkRoller {

    private DndCalculator calc;
    
    public BulkRoller(DndCalculator c) {
        this.calc = c;
    }

    public double roll(int attacks, String attack,
            double threshold, String dmg) {

        double totalDmg = 0.0;
        for (int i = 0; i < attacks; i++) {
            if (this.calc.math(attack) >= threshold) {
                totalDmg += this.calc.math(dmg);
            }
        }

        return totalDmg;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BulkRoller r = new BulkRoller(new DndCalculator());

        System.out.print("Number of attacks: ");
        int numAttacks = Integer.parseInt(sc.nextLine());
        System.out.print("Attack roll: ");
        String attack = sc.nextLine();
        System.out.print("AC: ");
        int ac = Integer.parseInt(sc.nextLine());
        System.out.print("Damage roll: ");
        String dmg = sc.nextLine();

        System.out.println(r.roll(numAttacks, attack, ac, dmg));
    }
}
