package UkkonenAlgorithm;

import java.util.HashMap;
import java.util.Map;

public class SuffixTree {
  private static class MutableInteger {
    int value;

    MutableInteger(int value) {
      this.value = value;
    }
  }

  private static class Node {
    public HashMap<Integer, Node> children = new HashMap<>();
    public Node suffixLink = null;
    public int charBeginIndex;
    public MutableInteger charEndIndex;
    public int suffixBeginIndex;

    public Node(int charBeginIndex, MutableInteger charEndIndex) {
      this.charBeginIndex = charBeginIndex;
      this.charEndIndex = charEndIndex;
    }

    public Node() {}

    public int getLength() {
      return charEndIndex.value - charBeginIndex + 1;
    }
  }

  private class ActivePoint {
    public Node activeNode = root;
    public int activeEdge = -1;
    public int activeLength = 0;

    public ActivePoint() {}
  }

  private static class UkkonnenRule3Exception extends Exception {}

  private String text;
  private MutableInteger currentEnd;
  private Node lastNewNode;
  private Node root;
  private ActivePoint activePoint;
  private int remainingLeavesToInsert;

  public SuffixTree(String text) {
    this.text = text;
    this.lastNewNode = null;

    buildSuffixTree();
  }

  public String getText() {
    return text;
  }

  private void buildSuffixTree() {
    addTerminalCharToText();
    root = new Node();
    currentEnd = new MutableInteger(-1);
    remainingLeavesToInsert = 0;
    activePoint = new ActivePoint();

    for (int charIndex = 0; charIndex < text.length(); charIndex++) {
      executePhase(charIndex);
    }
  }

  private void executePhase(int newCharIndex) {
    remainingLeavesToInsert++;
    currentEnd.value++;
    lastNewNode = null;

    while (remainingLeavesToInsert > 0) {
      try {
        if (activePoint.activeLength == 0) {
          extendFromRoot(newCharIndex);
        } else {
          extendFromActivePoint(newCharIndex);
        }
      } catch (UkkonnenRule3Exception e) {
        break;
      }
    }
  }

  private void extendFromActivePoint(int newCharIndex) throws UkkonnenRule3Exception {
    if (isSuffixInTreeAlready(newCharIndex)) {
      throw new UkkonnenRule3Exception();
    } else {
      splitEdge(newCharIndex);
      if (activePoint.activeNode == root) {
        activePoint.activeEdge++;
        activePoint.activeLength--;
      } else {
        activePoint.activeNode = activePoint.activeNode.suffixLink;
      }
    }
  }

  private void extendFromRoot(int newCharIndex) throws UkkonnenRule3Exception {
    if (isSuffixInTreeAlready(newCharIndex)) {
     throw new UkkonnenRule3Exception();
    } else {
      // add a leaf node to root
      Node newNode = new Node(newCharIndex, currentEnd);
      root.children.put(newCharIndex, newNode);
      remainingLeavesToInsert--;
    }
  }

  private boolean isSuffixInTreeAlready(int newChar) {
    if (activePoint.activeLength == 0) {
      Map.Entry<Integer, Node> entry =
          root.children.entrySet().stream()
              .filter(mapEntry -> areCharsIdentical(mapEntry.getKey(), newChar))
              .findAny()
              .orElse(null);
      if (entry != null) {
        activePoint.activeLength++;
        activePoint.activeEdge = entry.getKey();
        return true;
      }
    } else {
      Node currentContinuingNode = activePoint.activeNode.children.getOrDefault(activePoint.activeEdge, null);
      if (currentContinuingNode == null) {
        return false;
      }
      if (activePoint.activeLength < currentContinuingNode.getLength()) {
        if (areCharsIdentical(activePoint.activeLength, newChar)) {
          activePoint.activeLength++;
          return true;
        }
      } else {
        Map.Entry<Integer, Node> entry =
            currentContinuingNode.children.entrySet().stream()
                .filter(mapEntry -> areCharsIdentical(mapEntry.getKey(), newChar))
                .findAny()
                .orElse(null);
        if (entry != null) {
          activePoint.activeNode = currentContinuingNode;
          activePoint.activeEdge = entry.getKey();
          activePoint.activeLength = 1;
          return true;
        }
      }
    }

    return false;
  }

  private boolean areCharsIdentical(int firstChar, int secondChar) {
    return text.charAt(firstChar) == text.charAt(secondChar);
  }

  private void splitEdge(int newCharIndex) {
    Node nextNode = activePoint.activeNode.children.get(activePoint.activeEdge);
    Node newNode =
            new Node(
                    nextNode.charBeginIndex,
                    new MutableInteger(nextNode.charBeginIndex + activePoint.activeLength - 1));
    nextNode.charBeginIndex += activePoint.activeLength;
    activePoint.activeNode.children.remove(activePoint.activeEdge);
    activePoint.activeNode.children.put(activePoint.activeEdge, newNode);
    newNode.children.put(nextNode.charBeginIndex, nextNode);

    Node newLeaf = new Node(newCharIndex, currentEnd);
    newNode.children.put(newCharIndex, newLeaf);
    newNode.suffixLink = root;
    if (this.lastNewNode != null) {
      this.lastNewNode.suffixLink = newNode;
    }
    this.lastNewNode = newNode;
    remainingLeavesToInsert--;
  }

  private void addTerminalCharToText() {
    this.text += Character.toChars(0xFE89)[0];
  }
}
