import java.util.*;

public class Person {
    public static final int NOBODY = -1;

    private String name;
    private List<Integer> preferences;
    private List<Integer> oldPreferences;
    private int partner;

    public Person(String name) {
        this.name = name;
        preferences = new ArrayList<Integer>();
        oldPreferences = new ArrayList<Integer>();
        erasePartner();
    }

    public void erasePartner() {
        partner = NOBODY;
    }

    public boolean hasPartner() {
        return partner != NOBODY;
    }

    public int getPartner() {
        return partner;
    }

    public void setPartner(int partner) {
        this.partner = partner;
    }

    public String getName() {
        return name;
    }

    public boolean hasChoices() {
        return !preferences.isEmpty();
    }

    public int getFirstChoice() {
        return preferences.get(0);
    }

    public void addChoice(int person) {
        preferences.add(person);
        oldPreferences.add(person);
    }

    public List<Integer> getChoices() {
        return preferences;
    }

    public int getPartnerRank() {
        return oldPreferences.indexOf(partner) + 1;
    }
}
