import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class AhoCorasickTest {
  AhoCorasick aho;

  @Test
  void testSearchingInString() {
    String testString = "abccab";
    ArrayList<String> words = new ArrayList<>(List.of("a", "ab", "bab", "bc", "bca", "c", "caa"));

    aho.searchInString(testString, words);
  }
}
