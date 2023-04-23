import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
public class Main {
    static ArrayList<String> list = new ArrayList<>();
    public static void main(String[] args) {

        boolean done = false;
        String cmd = "";
        Scanner console = new Scanner(System.in);
        boolean needsToBeSaved = false;
        String fileName = "";

        do {
            //display list
            cmd = printMenu(console, list);
            //execute the choice
            switch (cmd)
            {
                case "A":
                    addToList(console, list);
                    needsToBeSaved = true;
                    break;
                case "D":
                    deleteFromList(console, list);
                    needsToBeSaved = true;
                    break;
                case "Q":
                    if (SafeInput.getYNConfirm(console, "Are you sure")){
                        done =true;
                    }
                    System.exit(0); //quits
                    break;
                case "O":
                    openListFile(console, list, needsToBeSaved);
                    needsToBeSaved = true;
                    break;
                case "S":
                    saveFile(list, fileName);
                    needsToBeSaved = false;
                    break;
                case "C":
                    clearList(list);
                    needsToBeSaved = true;
                    break;
                case "V":
                    viewList(list);
                    needsToBeSaved = true;
                    break;

            }
            System.out.println("cmd is " + cmd);
        }
        while (!done);
    }

    public static String printMenu(Scanner in, ArrayList list){
        if(list.isEmpty()){
            System.out.println("Your list is empty.");
        }else {
            System.out.println("Current list: ");
            for (int i=0; i < list.size(); i++){
                System.out.printf("  %d. %s\n", i+1, list.get(i));
            }
        } return SafeInput.getRegExString(in, "Enter menu choice:\n  A-Add\n  C-Clear\n  D-Delete\n  O-Open\n  S-Save\n  V-View\n  Q-Quit\n", "[AaCcDdOoSsVvQq]").toUpperCase();
    }
    public static void addToList(Scanner in, ArrayList list){
        String objectToAdd = SafeInput.getNonZeroLenString(in, "Enter the item to add to the array list" );
        list.add(objectToAdd); //adds object to list
    }
    public static void  deleteFromList(Scanner in, ArrayList list ){
        int objectToDelete = SafeInput.getRangedInt(in, "Enter the number of the item to delete", 1, list.size());
        list.remove(objectToDelete-1); //removes object based on base 1 index
    }
    public static void viewList(ArrayList list){
        for (int i=0; i<list.size(); i++){
            System.out.println(list.get(i));
        }
    }
    private static void displayList() {
        if (list.size() != 0) {
            for (int i = 0; i < list.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, list.get(i)); //for index based 1
            }
        }
        else
            System.out.println("++++          List is empty           ++++");
        System.out.println("");
    }

    public static void clearList(ArrayList list){
        list.clear();
    }
    private static String openListFile(Scanner in, ArrayList list, boolean needsToBeSaved){
        if (needsToBeSaved){
            String prompt = "A new list will result in losing the current one. Do you want to continue";
            boolean newList = SafeInput.getYNConfirm(in, prompt);
            if (!newList){
                return "";
            }
        }
        clearList(list);
        Scanner inFile;
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        chooser.setFileFilter(filter);
        String line;

        Path target = new File(System.getProperty("user.dir")).toPath();
        target = target.resolve("src");
        chooser.setCurrentDirectory(target.toFile());

        try {
            if(chooser.showOpenDialog(null) ==JFileChooser.APPROVE_OPTION){
                target = chooser.getSelectedFile().toPath();
                inFile = new Scanner(target);
                System.out.println("Opening file: " + target.getFileName());
                while (inFile.hasNextLine()){
                    line = inFile.nextLine();
                    list.add(line);
                }
            }
            //no file selected
            else {
                System.out.println("Must select a file");
            }
        }
        catch (IOException ex){
            System.out.println("IOException error");
        }
        return target.toFile().toString();
    }

    public static void saveFile(ArrayList list, String fileName){
        PrintWriter outputFile;
        Path target = new File(System.getProperty("user.dir")).toPath();
        if (fileName.equals("")){
            target = target.resolve("src\\list.txt");
        } else {
            target = target.resolve(fileName);
        }
        try {
            outputFile = new PrintWriter(target.toString());
            for (int i=0; i< list.size(); i++){
                outputFile.close();
                System.out.printf("");
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}