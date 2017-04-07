import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static com.sun.org.apache.xalan.internal.xsltc.cmdline.Compile.printUsage;

public class ToDo {
  public static void main(String[] args) {
    if (args.length == 0) {
      printUsage();
    } else if (args.length == 1 && args[0].equals("-l")) {
      listTasks();
    } else if (args[0].equals("-a")) {
      if (args.length == 1) {
        System.out.println("Unable to add: no task provided");
      } else if (args.length == 2) {
        addTask(args[1]);
      } else {
        System.out.println("Too many arguments!");
      }
    } else if (args[0].equals("-r")) {
      if (args.length == 1) {
        System.out.println("Unable to remove: no index provided");
      } else if (args.length == 2) {
        try {
          removeTask(Integer.valueOf(args[1]));
        } catch (NumberFormatException e) {
          System.out.println("Unable to remove: index is not a number");
        }
      } else {
        System.out.println("Too many arguments!");
      }
    } else if (args[0].equals("-c")) {
      if (args.length == 1) {
        System.out.println("Unable to check: no index provided");
      } else if (args.length == 2) {
        try {
          checkTask(Integer.valueOf(args[1]));
        } catch (NumberFormatException e) {
          System.out.println("Unable to check: index is not a number");
        }
      } else {
        System.out.println("Too many arguments!");
      }
    } else {
      System.out.println("Unsupported argument");
    }
  }

  private static void printUsage() {
    List<String> usageText = loadFile("usage.txt");
    for (int i = 0; i < usageText.size(); i++) {
      System.out.println(usageText.get(i));
    }
  }

  private static List<String> loadFile(String fileName) {
    Path usage = Paths.get(fileName);
    try {
      List<String> loadedText = Files.readAllLines(usage);
      return loadedText;
    } catch (IOException e) {
      System.out.println("Something is wrong with " + fileName + " file");
    }
    return null;
  }

  private static void listTasks() {
    List<String> listText = loadFile("data.csv");
    if (listText.size() == 0) {
      System.out.println("No todos for today! :)");
    } else {
      for (int i = 0; i < listText.size(); i++) {
        System.out.println(i + 1 + " - " + listText.get(i));
      }
    }
  }

  private static void addTask(String newTask) {
    List<String> listText = loadFile("data.csv");
    String firstLineStart = listText.get(0).substring(0, 3);
    listText.add(firstLineStart.equals("[ ]") || firstLineStart.equals("[x]") ? "[ ] " + newTask : newTask);
    Path newList = Paths.get("data.csv");
    try {
      Files.write(newList, listText);
    } catch (IOException e) {
      System.out.println("Can't write, sorry!");
    }
  }

  private static void removeTask(int index) {
    List<String> listText = loadFile("data.csv");
    if (index > listText.size()) {
      System.out.println("Unable to remove: index is out of bound");
      return;
    }
    listText.remove(index - 1);
    Path newList = Paths.get("data.csv");
    try {
      Files.write(newList, listText);
    } catch (IOException e) {
      System.out.println("Something is wrong with the file.");
    }
  }

  private static void checkTask(int index) {
    List<String> listText = loadFile("data.csv");
    if (index > listText.size()) {
      System.out.println("Unable to check: index is out of bound");
      return;
    }
    for (int i = 0; i < listText.size(); i++) {
      String thisLine = listText.get(i);
      String thisLineStart = thisLine.substring(0, 3);
      String thisLineText = thisLine.substring(3);
      if (i == index - 1) {
        if (thisLineStart.equals("[ ]")) {
          listText.set(i, "[x]" + thisLineText);
        } else if (!(thisLineStart.equals("[x]"))) {
          listText.set(i, "[x] " + thisLine);
        }
      } else if (!(thisLineStart.equals("[ ]") || thisLineStart.equals("[x]"))) {
        listText.set(i, "[ ] " + thisLine);
      }
    }

    Path newList = Paths.get("data.csv");
    try {
      Files.write(newList, listText);
    } catch (IOException e) {
      System.out.println("Something is wrong with the file.");
    }
  }
}