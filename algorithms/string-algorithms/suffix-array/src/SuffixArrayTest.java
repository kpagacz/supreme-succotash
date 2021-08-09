import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class SuffixArrayTest {
  @Test
  void testSuffixArray() {
    SuffixArray array = new SuffixArray("banana");
    Assertions.assertArrayEquals(new int[] {5, 3, 1, 0, 4, 2}, array.getArray());
  }
}
