// Allen Bao
// CS 211
// 1/24/2022


// This program reads an input file of preferences and find a stable marriage
// scenario.  The algorithm gives preference to either men or women depending
// upon whether this call is made from main:
//      makeMatches(men, women);
// or whether this call is made:
//      makeMatches(women, men);

import java.io.*;
import java.util.*;

public class StableMarriage {
    public static final String LIST_END = "END";

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.print("What is the input file? ");
        String fileName = console.nextLine();
        Scanner input = new Scanner(new File(fileName));
        System.out.println();

        List<Person> men = readHalf(input);
        List<Person> women = readHalf(input);
        makeMatches(men, women);
        writeList(men, women, "Matches for men");
        writeList(women, men, "Matches for women");
    }

    public static Person readPerson(String line) {
        int index = line.indexOf(":");
        Person result = new Person(line.substring(0, index));
        Scanner data = new Scanner(line.substring(index + 1));
        while (data.hasNextInt()) {
            result.addChoice(data.nextInt());
        }
        return result;
    }

    public static List<Person> readHalf(Scanner input) {
        List<Person> result = new ArrayList<Person>();
        String line = input.nextLine();
        while (!line.equals(LIST_END)) {
            result.add(readPerson(line));
            line = input.nextLine();
        }
        return result;
    }

    public static void makeMatches(List<Person> males, List<Person> females) {

        // Go through both lists of people and set everyone to be free
        for(Person m : males) {
            m.erasePartner();
        }
        for(Person f : females) {
            f.erasePartner();
        }

        while (true) { // call method that returns true if there's a free man
            Person m = findFirstFreePerson(males);

            if (m == null) {
                break; // break the loop if there are no more free males
            }

            Person w = females.get(m.getFirstChoice());

            if (w.hasPartner()) { // if a man is engaged to w, separate them
                males.get(w.getPartner()).erasePartner();
            }

            // set m and w to be engaged with each other
            m.setPartner(females.indexOf(w));
            w.setPartner(males.indexOf(m));

            // delete m and w's successors off each other's lists.
            for (int i = w.getPartnerRank()-1, j = i; i < w.getChoices().size(); ++i) {
                w.getChoices().remove(j);
            }
            for (int i = m.getPartnerRank()-1, j = i; i < m.getChoices().size(); ++i) {
                m.getChoices().remove(j);
            }
        }

    }

    private static Person findFirstFreePerson(List<Person> people) {
        for (Person p : people) {
            if (!p.hasPartner()) {
                return p;
            }
        }
        return null;
    }

    public static void writeList(List<Person> list1,  List<Person> list2,
                                 String title) {
        System.out.println(title);
        System.out.println("Name           Choice  Partner");
        System.out.println("--------------------------------------");
        int sum = 0;
        int count = 0;
        for (Person p : list1) {
            System.out.printf("%-15s", p.getName());
            if (!p.hasPartner()) {
                System.out.println("  --    nobody");
            } else {
                int rank = p.getPartnerRank();
                sum += rank;
                count++;
                System.out.printf("%4d    %s\n", rank,
                                  list2.get(p.getPartner()).getName());
            }
        }
        System.out.println("Mean choice = " + (double) sum / count);
        System.out.println();
    }
}
