import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ListMaker {

    public static ArrayList<String> myArrList = new ArrayList<>();
    public static Scanner in = new Scanner(System.in);
    public static boolean query = false;
    public static int index = 1;
    public static boolean isDirty = false;
    public static boolean quickSave = false;

    public static void main(String[] args) {
        String menuCommand;
        do {
            ViewMyList("Current list:");
            menuCommand = SafeInput.getRegExString(in, """
                    Select a menu command:
                    A – Add an item to the list
                    D – Delete an item from the list
                    I – Insert an item into the list
                    V – View the list
                    M – Move an item
                    O – Open a list file from disk
                    S – Save the current list file to disk
                    C – Clear removes all the elements from the current list
                    Q – Quit the program
                    """, "[AaDdIiVvMmOoSsCcQq]").toUpperCase();
            try {

                switch (menuCommand) {
                    case "A":
                        addItem("You selected A");
                        isDirty = true;
                        break;
                    case "D":
                        if(myArrList.isEmpty()){
                            System.out.println("You must add elements to the list before you can use the delete option");
                        }
                        else {
                            delete("You selected D");
                            isDirty = true;
                        }
                        break;
                    case "I":
                        if(myArrList.isEmpty()){
                            System.out.println("You must add elements to the list before you can use the insert option");
                        }
                        else {
                            insert("You selected I");
                            isDirty = true;
                        }
                        break;
                    case "V":
                        ViewMyList("You selected V");
                        break;
                    case "M":
                        if(myArrList.size()<2){
                            move("");
                        }
                        else {
                            move("You selected M");
                        }
                        isDirty = true;
                        break;
                    case "O":
                        myArrList = open("You selected O", SafeInput.getNonZeroLenString(in, "Enter the file name to open"));
                        break;
                    case "S":
                        save("list saved", SafeInput.getNonZeroLenString(in,"Enter the file name to save"));
                        break;
                    case "C":
                        clear("You selected C");
                        break;
                    case "Q":
                        query = quit(in, "You selected Q");
                        break;
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error file not found");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!query);

    }


    // MENU METHODS

    public static void addItem(String echo) {
        System.out.println(echo);
        myArrList.add((SafeInput.getNonZeroLenString(in, "Enter a new element to add to the list")));
        index++; // start at one but subtract one in code for delete method
    }
    public static void delete(String echo) {
        System.out.println(echo);
        index = SafeInput.getRangedInt(in, "Select an element from the list to delete", 1, myArrList.size()) - 1;
        myArrList.remove(index);
    }
    public static void insert(String echo) {
        System.out.println(echo);
        index = SafeInput.getRangedInt(in, "Select where you want to put this element", 1, myArrList.size()) - 1;
        myArrList.add(index, (SafeInput.getNonZeroLenString(in, "Enter a element for this position in the list")));
    }
    public static void ViewMyList(String echo) {
        System.out.println(echo);
        for (index = 1; index <= myArrList.size(); index++) {
            System.out.println(index + ". " + myArrList.get(index - 1));
        }
        System.out.println();
    }
    public static void move(String echo) {
        System.out.println(echo);
        if(myArrList.size()<2){
            System.out.println("You must add two elements to the list to use the move option");
            addItem("Try adding some elements");
            System.out.println(echo);
            addItem("add second element to then use the move option");
            System.out.println(echo);
            ViewMyList("Here is the current list");
            System.out.println(echo);
        }
        index = SafeInput.getRangedInt(in, "Select what element you want to move", 1, myArrList.size())-1;
        int newPos = SafeInput.getRangedInt(in, "Select where you want to put this element", 1, myArrList.size())-1;
       if(newPos < index){
            myArrList.add(newPos, myArrList.get(index));
            myArrList.remove(index + 1); // when selected element is moved up in the list
        }
        if(newPos > index){
            myArrList.add(newPos+1, myArrList.get(index));
            myArrList.remove(index); // when selected element is moved down in the list
        }
    }
    public static ArrayList<String> open(String echo, String fileName) throws FileNotFoundException {
        try
        {
            System.out.println(echo);
            if(isDirty) {
            System.out.println("This list has unsaved changes. Loading a new list now will overwrite the current list.");
            quickSave = SafeInput.getYNConfirm(in, "Do you want to save the current list? [Y] save list [N] Overwrite list");
                if (quickSave) {
                    save("List saved", SafeInput.getNonZeroLenString(in, "Enter the file name to save"));
                    System.out.println(echo);
                }
             }
            Path filePath = Paths.get(fileName);
            myArrList = new ArrayList<>(Files.readAllLines(filePath));
            isDirty = false;
            System.out.println("New list loaded successfully");
        }catch (IOException e){
            System.out.println("An error occurred while accessing the file. Please check the file path and try again.");
        }
        return myArrList;
    }
    public static void save(String echo, String fileName) throws IOException {
        System.out.println(echo);
        Path filePath = Paths.get(fileName);
        Files.write(filePath,myArrList);
        isDirty = false;
    }
    public static void clear(String echo){
        System.out.println(echo);
        boolean confirmClear = SafeInput.getYNConfirm(in, "Are you sure you want to clear the list?");
        if(confirmClear)
            myArrList.clear();
    }
    public static boolean quit (Scanner pipe,String echo) throws IOException{
        System.out.println(echo);
        if(isDirty){
            System.out.println("Your list needs to be saved. You will lose changes to this list if you quit");
            quickSave = SafeInput.getYNConfirm(in, "Do you want to save the current list? [Y] save list [N] abandon list");
            if (quickSave) {
                save("List saved", SafeInput.getNonZeroLenString(in, "Enter the file name to save"));
                System.out.println(echo);
            }
        }
        return SafeInput.getYNConfirm(pipe,"Are you sure you want to quit the program");
    }
}
