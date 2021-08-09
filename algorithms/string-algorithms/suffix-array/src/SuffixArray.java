import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

public class SuffixArray {
  static class SuffixComparator implements Comparator<Suffix> {
    public int compare(Suffix first, Suffix second) {
      if (first.firstRank == second.firstRank) {
        return Integer.compare(first.secondRank, second.secondRank);
      } else {
        return Integer.compare(first.firstRank, second.firstRank);
      }
    }
  }

  static class Suffix {
    public int charIndex;
    public int firstRank;
    public int secondRank;

    public Suffix(int charIndex) {
      this.charIndex = charIndex;
      firstRank = secondRank = 0;
    }
  }

  private final Suffix[] suffixArray;
  public int[] charIndexSuffixArray;

  public SuffixArray(String text) {
    suffixArray = new Suffix[text.length()];
    charIndexSuffixArray = new int[text.length()];
    buildSuffixArray(text);
  }

  public int[] getArray() {
    return charIndexSuffixArray;
  }

  private void buildSuffixArray(String text) {
    for (int charIndex = 0; charIndex < text.length(); charIndex++) {
      suffixArray[charIndex] = new Suffix(charIndex);
      suffixArray[charIndex].firstRank = (int) text.charAt(charIndex) - (int) 'a';
      suffixArray[charIndex].secondRank =
          (charIndex + 1 < text.length()) ? (int) text.charAt(charIndex + 1) - (int) 'a' : -1;
    }

    Arrays.sort(suffixArray, new SuffixComparator());

    int[] indicesRanks = new int[text.length()];
    for (int k = 4; k < 2 * text.length(); k = k * 2) {
      int rank = 0;
      int prev_rank = suffixArray[0].firstRank;
      suffixArray[0].firstRank = rank;
      indicesRanks[suffixArray[0].charIndex] = rank;

      for (int i = 1; i < text.length(); i++) {
        if (suffixArray[i].firstRank == prev_rank
            && suffixArray[i].secondRank == suffixArray[i - 1].secondRank) {
          prev_rank = suffixArray[i].firstRank;
          suffixArray[i].firstRank = rank;
        } else {
          prev_rank = suffixArray[i].firstRank;
          suffixArray[i].firstRank = ++rank;
        }
        indicesRanks[suffixArray[i].charIndex] = rank;
      }

      for (Suffix suffix : suffixArray) {
        int nextIndex = suffix.charIndex + k / 2;
        suffix.secondRank = nextIndex < text.length() ? indicesRanks[nextIndex] : -1;
      }

      Arrays.sort(suffixArray, new SuffixComparator());
    }

    for (int i = 0; i < suffixArray.length; i++) {
      charIndexSuffixArray[i] = suffixArray[i].charIndex;
    }
  }
}
