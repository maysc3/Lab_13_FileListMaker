import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ListMaker
{

    public static ArrayList<String> myArrList = new ArrayList<>();
    public static Scanner in = new Scanner(System.in);
    public static boolean query = false;
    public static int index = 1;
    public static void main(String[] args)
    {
            String menuCommand;
            do {
                ViewMyList("Current list:");
                menuCommand= SafeInput.getRegExString(in, """
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
                            break;
                        case "D":
                            delete("You selected D");
                            break;
                        case "I":
                            insert("You selected I");
                            break;
                        case "V":
                            ViewMyList("You selected V");
                            break;
                        case "M":
                            move("You selected M");
                            break;
                        case "O":
                            myArrList = open("You selected O", SafeInput.getNonZeroLenString(in,"Enter the file name to open"));
                            break;
                        case "S":

                            break;
                        case "C":
                            clear("You selected C");
                            break;
                        case "Q":
                            query = quit(in, "You selected Q");
                            break;
                    }
                }catch (FileNotFoundException e)
                {
                    System.out.println("File not found");
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }while(!query);

    }
        public static void addItem (String echo){
        System.out.println(echo);
        myArrList.add((SafeInput.getNonZeroLenString(in,"Enter a new element to add to the list")));
        index++; // start at one but subtract one in code for delete method
    }
        public static void delete (String echo){
        System.out.println(echo);
        index = SafeInput.getRangedInt(in,"Select an element from the list to delete",1,myArrList.size())-1;
        myArrList.remove(index);
    }
        public static void insert(String echo){
        System.out.println(echo);
        index =  SafeInput.getRangedInt(in,"Select where you want to put this element",1,myArrList.size())-1;
        myArrList.add(index,(SafeInput.getNonZeroLenString(in,"Enter a element for this position in the list")));
    }
        public static void ViewMyList(String echo){
        System.out.println(echo);
        for(index=1; index<=myArrList.size();index++){
            System.out.println(index + ". "+myArrList.get(index-1));
        }
        System.out.println();
    }
        public static void move (String echo){
        System.out.println(echo);
        index =  SafeInput.getRangedInt(in,"Select what element you want to move",1,myArrList.size())-1;
        int newPos = SafeInput.getRangedInt(in,"Select where you want to put this element",1,myArrList.size())-1;
        myArrList.add(newPos,myArrList.get(index));
        myArrList.remove(index+1);
    }
        public static ArrayList<String> open(String echo,String fileName) throws IOException {
        System.out.println(echo);
        Path filePath = Paths.get(fileName);
        myArrList = new ArrayList<>(Files.readAllLines(filePath));
        return myArrList;
    }







        public static void clear(String echo){
        System.out.println(echo);
        boolean confirmClear = SafeInput.getYNConfirm(in, "Are you sure you want to clear the list?");
        if(confirmClear)
            myArrList.clear();
    }
        public static boolean quit (Scanner pipe,String echo){
        System.out.println(echo);
        return SafeInput.getYNConfirm(pipe,"Are you sure you want to quit the program");

    }
}
