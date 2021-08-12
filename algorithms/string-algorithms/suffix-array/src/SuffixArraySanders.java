/**
 * This is an implementation of the Juha Karkkainen and Peter Sanders's suffix array construction
 * algorithm. Given the string s of length n it creates a suffix array - a lexicographically sorted
 * suffix array.
 *
 * The original article by Karkkainen and Sanders: http://www.cs.cmu.edu/~guyb/realworld/papersS04/KaSa03.pdf
 *
 * <p>Of note is it's worst cases running time complexity, which is O(n), where n is the length of
 * the string.
 */
public class SuffixArraySanders {
  private String text;
  private char[] textCharArray;
  private int[] suffixArray;
  private int ALPHABET_SIZE = 256;

  public SuffixArraySanders(String text) {
    this.text = text;
    this.textCharArray = text.toCharArray();
    buildSuffixArray();
  }

  private void buildSuffixArray() {
    int n0 = (text.length() + 2) / 3;
    int n1 = (text.length() + 1) / 3;
    int n2 = text.length() / 3;
    int n02 = n0 + n2;
    int[] s12 = new int[n02 + 3];
    s12[n02] = s12[n02 + 1] = s12[n02+2] = 0;
    int[] SA12 = new int[n02 + 3];
    SA12[n02] = SA12[n02 + 1] = SA12[n02+2] = 0;
    int[] s0 = new int[n0];
    int[] SA0 = new int[n0];

    // generate positions of mod 1 and mod 2 suffixes
    // the "+(n0-n1)" adds a dummy mod 1 suffix if n%3 == 1
    for(int i = 0, j = 0; i < text.length() + (n0 - n1); i++) if(i%3 != 0) s12[j++] = i;

    // lsb radix sort the mod 1 and mod 2 triples
    radixPass(s12, SA12, textCharArray, n02, ALPHABET_SIZE, 2);
    radixPass(SA12, s12, textCharArray, n02, ALPHABET_SIZE, 1);
    radixPass(s12, SA12, textCharArray, n02, ALPHABET_SIZE, 0);
  }

  private void radixPass(int[] a, int[] b, char[] r, int n, int K, int offset) {
    int [] counts = new int[K];
    for(int i = 0; i < n; i++) ++counts[r[a[i] + offset]];
    for(int i = 0, sum = 0; i <= K; i++) {
      int t = counts[i];
      counts[i] = sum;
      sum += t;
    }
    for(int i = 0; i < n; i++) b[counts[r[a[i] + offset]]++] = a[i];
  }
}
