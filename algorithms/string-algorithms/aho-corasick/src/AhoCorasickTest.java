import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class AhoCorasickTest {
  AhoCorasick aho;

  @BeforeEach
  void setUp() {
    aho = new AhoCorasick();
  }

  @Test
  void testSearchingInString() {
    String testString = "abccab";
    ArrayList<String> words = new ArrayList<>(List.of("a", "ab", "bab", "bc", "bca", "c", "caa"));

    aho.searchInString(testString, words);
  }
}
