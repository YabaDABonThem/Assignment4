// Allen Bao
// CS 211
// 1/24/2022

// This program reads an input file of preferences and find a stable marriage
// scenario.  The algorithm gives preference to either men or women depending
// upon whether this call is made from main:
//      makeMatches(men, women);
// or whether this call is made:
//      makeMatches(women, men);
// This program was insanely annoying to finish and took me around 8 hours to finish everything

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

        Person m = null;
        while ((m = findFirstFreePerson(males)) != null) { // call method that returns true if there's a free man
            // get the first woman on m's like to pair them with each other
            Person w = females.get(m.getFirstChoice());

            if (w.hasPartner()) { // if a man is engaged to w, separate them
                males.get(w.getPartner()).erasePartner();
            }

            // set m and w to be engaged with each other
            int man = males.indexOf(m);
            int woman = females.indexOf(w);
            m.setPartner(woman);
            w.setPartner(man);

            // delete m and w's successors off each other's lists.
            // first we delete w from all m's successors, because that will not change the length of w's preferences.
            // we need to start from the man after m on w's list, but also need to make sure that it's less than the
            // actual size of the list, or else it'd go out of bounds
            int mIndex = w.getChoices().indexOf(man);

            // we break up w's choice list using subList, and go through all the men after m
            // we use int to represent the men, because they are stored as ints on w's list
            for (int q : w.getChoices().subList(mIndex+1, w.getChoices().size())) {
                // we call a private method that deletes all the choices after a certain
                deleteChoice(males.get(q), woman);
            }
            // Remove all m's successors from w's preferences list using the private method we coded
            deleteAfterwardsChoices(w, man);
        }
    }

    // private method to determine whether we should continue making matches
    private static Person findFirstFreePerson(List<Person> people) {
        // go through all the people and check if they have a partner or any remaining choices
        for (Person p : people) {
            // if they're open and have remaining choices then return an instance of them
            if (!p.hasPartner() && p.getChoices().size() > 0) {
                return p;
            }
        }
        // if everyone is taken and no new matches can be made, return null
        return null;
    }

    // private method to delete a certain Person off someone's choice list
    private static void deleteChoice(Person p, int choice) {
        // use an iterator to delete to avoid modifying the ArrayList while iterating it
        Iterator<Integer> itr = p.getChoices().iterator();

        // Go through all the people in the choice list and delete the matching Person.
        while (itr.hasNext()) {
            Integer number = itr.next();

            if (number == choice) {
                itr.remove();
                return; // empty return on void method works
            }
        }
    }

    // private method to delete everyone after a certain choice on a person's list
    private static void deleteAfterwardsChoices(Person p, int choice) {
        Iterator<Integer> itr = p.getChoices().iterator();

        boolean startDelete = false;
        while (itr.hasNext()) {
            if (startDelete) {
                itr.next();
                itr.remove();
            } else {
                startDelete = (itr.next() == choice);
            }
        }
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
