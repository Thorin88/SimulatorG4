import java.lang.Math;
import java.util.ArrayList;

import static utility.Constants.*;

import java.awt.Color;

public class UtilityFunctions {

  // So can be used without instance of class

  // https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
  public static int randomNumber(int min, int max) {

    return (min + (int)(Math.random() * ((max - min) + 1)));

  }

  public static int urlToID(String url) {

    int lastSlash = url.lastIndexOf("/");

    String tempUrl = url.substring(0,lastSlash);

    int secondLastSlash = tempUrl.lastIndexOf("/");

    String stringToReturn = tempUrl.substring(secondLastSlash + 1);

    // System.out.println(stringToReturn);

    int intToReturn;

    try {

      intToReturn = Integer.parseInt(stringToReturn);

    } catch (NumberFormatException e) {

      System.out.println("Error extracting ID from URL (2)");
      return 1;

    }

    return intToReturn;

  }

  public static int indexOfMaxOfFloatArray(float[] array) {

    float max = MIN;
    int index = 0;
    int size = array.length;

    for (int i = 0; i < size; i++) {

      if (array[i] >= max) {

        max = array[i];
        index = i;

      }

    }

    return index;

  }

  public static int indexOfMinOfFloatArray(float[] array) {

    float min = MAX;
    int index = 0;
    int size = array.length;

    for (int i = 0; i < size; i++) {

      if (array[i] <= min) {

        min = array[i];
        index = i;

      }

    }

    return index;

  }

  public static ArrayList<Integer> indexesOfMaxOfFloatArray(float[] array) {

    float max = MIN;
    // int index = 0;
    int size = array.length;
    ArrayList<Integer> indexesOfMax = new ArrayList<Integer>();

    for (int i = 0; i < size; i++) {

      if (array[i] >= max) {

        if (array[i] > max) {

          max = array[i];
          indexesOfMax.clear();

        }

        indexesOfMax.add(i);

      }

    }

    return indexesOfMax;

  }

  public static ArrayList<Integer> indexesOfMinOfFloatArray(float[] array) {

    float min = MAX;
    // int index = 0;
    int size = array.length;
    ArrayList<Integer> indexesOfMin = new ArrayList<Integer>();

    for (int i = 0; i < size; i++) {

      if (array[i] <= min) {

        if (array[i] < min) {

          min = array[i];
          indexesOfMin.clear();

        }

        indexesOfMin.add(i);

      }

    }

    return indexesOfMin;

  }

  // returns -1 if list was empty
  public static int indexOfMinOfFloatArrayList(ArrayList<Float> list) {

    float min = MAX;
    int index = -1;
    int size = list.size();

    for (int i = 0; i < size; i++) {

      if (list.get(i) == null) { continue; }

      if (list.get(i) <= min) {

        min = list.get(i);
        index = i;

      }

    }

    return index;

  }

  public static int indexOfMaxOfFloatArrayList(ArrayList<Float> list) {

    float max = MIN;
    int index = -1;
    int size = list.size();

    for (int i = 0; i < size; i++) {

      if (list.get(i) == null) { continue; }

      if (list.get(i) >= max) {

        max = list.get(i);
        index = i;

      }

    }

    return index;

  }

  public static ArrayList<Integer> indexesOfMinOfFloatArrayList(ArrayList<Float> list) {

    float min = MAX;
    // int index = 0;
    int size = list.size();
    ArrayList<Integer> indexesOfMin = new ArrayList<Integer>();

    for (int i = 0; i < size; i++) {

      if (list.get(i) == null) { continue; }

      if (list.get(i) <= min) {

        if (list.get(i) < min) {

          min = list.get(i);
          indexesOfMin.clear();

        }

        indexesOfMin.add(i);

      }

    }

    return indexesOfMin;

  }

  public static ArrayList<Integer> indexesOfMaxOfFloatArrayList(ArrayList<Float> list) {

    float max = MIN;
    // int index = 0;
    int size = list.size();
    ArrayList<Integer> indexesOfMax = new ArrayList<Integer>();

    for (int i = 0; i < size; i++) {

      if (list.get(i) == null) { continue; }

      if (list.get(i) >= max) {

        if (list.get(i) > max) {

          max = list.get(i);
          indexesOfMax.clear();

        }

        indexesOfMax.add(i);

      }

    }

    return indexesOfMax;

  }

  public static String toCaps(String name) {

    // https://stackoverflow.com/questions/3904579/how-to-capitalize-the-first-letter-of-a-string-in-java
    name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();

    while (name.contains("-")) {

      int index = name.indexOf("-");
      // https://stackoverflow.com/questions/13386107/how-to-remove-single-character-from-a-string
      name = name.substring(0, index) + " " + name.substring(index+1).substring(0,1).toUpperCase() + name.substring(index+2);

    }

    return name;

  }

  public static Color typeToColour(int typeID) {

    switch (typeID) {

      case 1:
        return new Color(168,168,120);
      case 2:
        return new Color(192,48,40);
      case 3:
        return new Color(168,144,240);
      case 4:
        return new Color(160,64,160);
      case 5:
        return new Color(224,192,104);
      case 6:
        return new Color(184,160,56);
      case 7:
        return new Color(168,184,32);
      case 8:
        return new Color(112,88,152);
      case 9:
        return new Color(184,184,208);
      case 10:
        return new Color(240,128,48);
      case 11:
        return new Color(104,144,240);
      case 12:
        return new Color(120,200,80);
      case 13:
        return new Color(248,208,48);
      case 14:
        return new Color(248,88,136);
      case 15:
        return new Color(152,216,216);
      case 16:
        return new Color(112,56,248);
      case 17:
        return new Color(112,88,72);
      case 18:
        return new Color(238,153,172);
      default:
        // Typeless
        return new Color(104,160,144);

    }

  }


}
