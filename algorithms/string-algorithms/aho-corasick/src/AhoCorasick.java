import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

/** Implements the Aho-Corasick algorithm.
 *
 * For the overview of the algorithm, see:
 * https://en.wikipedia.org/wiki/Aho%E2%80%93Corasick_algorithm
 *
 * In short, the Aho-Corasick outputs all occurrences of given character sequences (a dictionary)
 * in a searched character sequence. Example:
 * dictionary: a, ab
 * searched string: aba
 * output: a, ab, a
 * The second 'a' is printed because the third character of the searched string ('a')
 * matches the dictionary element 'a'.
 * */
public class AhoCorasick {
  public void searchInString(String searchedString, List<String> strings) {
    searchInString(searchedString, prepareAhoCorasickAutomaton(strings));
  }

  private class TrieNode {
    public char character;
    public boolean wordEnd;
    public HashMap<Character, TrieNode> children;
    public TrieNode longestSuffix = null;
    public TrieNode dictionarySuffixLink = null;
    public TrieNode parent;

    public TrieNode() {
      character = '\0';
      wordEnd = false;
      children = new HashMap<>();
    }

    public TrieNode(char character) {
      this.character = character;
      wordEnd = false;
      this.children = new HashMap<>();
    }

    public String getNodeWord() {
      StringBuilder word = new StringBuilder();
      TrieNode iterator = this;
      while (iterator.parent != null) {
        word.append(iterator.character);
        iterator = iterator.parent;
      }

      word.reverse();
      return word.toString();
    }
  }

  private TrieNode insertWord(TrieNode root, @NotNull String word) {
    if (word.length() == 0) {
      root.wordEnd = true;
      return root;
    }

    char nextCharacter = word.charAt(0);
    word = word.substring(1);

    if (root.children.containsKey(nextCharacter)) {
      return insertWord(root.children.get(nextCharacter), word);
    } else {
      TrieNode newNode = new TrieNode(nextCharacter);
      root.children.put(nextCharacter, newNode);
      newNode.parent = root;
      return insertWord(newNode, word);
    }
  }

  private void findLongestSuffix(TrieNode parent, TrieNode node, TrieNode root) {
    TrieNode longestSuffix = parent.longestSuffix;
    while (longestSuffix != null && !longestSuffix.children.containsKey(node.character)) {
      longestSuffix = longestSuffix.longestSuffix;
    }

    if (longestSuffix == null) {
      node.longestSuffix = root;
    } else {
      node.longestSuffix = longestSuffix.children.get(node.character);
    }
  }

  private void findAllLongestSuffixes(TrieNode parent, TrieNode root) {
    if (parent == null) return;

    for (TrieNode child : parent.children.values()) {
      findLongestSuffix(parent, child, root);
      findAllLongestSuffixes(child, root);
    }
  }

  private void findDictionarySuffixLink(TrieNode node) {
    TrieNode suffixIterator = node.longestSuffix;
    while (suffixIterator != null) {
      if (suffixIterator.wordEnd) break;
      suffixIterator = suffixIterator.longestSuffix;
    }

    node.dictionarySuffixLink = suffixIterator;
  }

  private void findAllDictionarySuffixLinks(TrieNode node) {
    if (node == null) return;

    for (TrieNode child : node.children.values()) {
      findDictionarySuffixLink(child);
      findAllDictionarySuffixLinks(child);
    }
  }

  private TrieNode prepareAhoCorasickAutomaton(List<String> dictionaryWords) {
    TrieNode root = new TrieNode();

    for (String word : dictionaryWords) {
      insertWord(root, word);
    }

    findAllLongestSuffixes(root, root);
    findAllDictionarySuffixLinks(root);

    return root;
  }

  private void searchInString(String searchedString, TrieNode automaton) {
    TrieNode automatonIterator = automaton;

    for (char character : searchedString.toCharArray()) {
      while (automatonIterator != null && !automatonIterator.children.containsKey(character)) {
        automatonIterator = automatonIterator.longestSuffix;
      }

      if (automatonIterator != null) {
        automatonIterator = automatonIterator.children.get(character);
        while (automatonIterator.dictionarySuffixLink != null) {
          if (automatonIterator.wordEnd) {
            System.out.println(automatonIterator.getNodeWord());
          }
          automatonIterator = automatonIterator.dictionarySuffixLink;
        }
        if (automatonIterator.wordEnd) {
          System.out.println(automatonIterator.getNodeWord());
        }
      } else {
        automatonIterator = automaton;
      }
    }
  }

}
