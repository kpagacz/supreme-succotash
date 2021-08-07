package UkkonenAlgorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SuffixTreeTest {

    private SuffixTree suffixTree;

    @Test
    void createSuffixTreeTest() {
        SuffixTree tree = new SuffixTree("xyzxyaxyz");
    }
}