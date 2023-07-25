package words;

import java.util.Collection;

/**
 * Checks the word pairs sequentially as it will be done with a graphical user
 * interface.
 */
public class SequentialChecker implements WordPairChecker {

  private Collection<WordPair> pairs;

  /**
   * Constructor
   * 
   * @param pairs -
   *          the word pairs to be asked
   */
  public SequentialChecker(Collection<WordPair> pairs) {
    this.pairs = pairs;
  }

  /**
   * Checks if the give word pair exists in the list of word pairs (native and
   * foreign have to be equal). Its possible to translate a word in any given
   * way; as long as there exists one translation in the list as well.<br>
   * Notice: As soon as a combination has been translated correclty, it will be
   * removed from the list. This is because every translation should be
   * translated by the user; it is not correct to translate one word, that has
   * different translations, multiple times with the same translation.
   * 
   * @param wordpair
   * @return boolean - is the translation correct (<code>true</code>) or not (<code>false</code>)?
   */
  public boolean isCorrect(WordPair wordpair) {
    boolean correct;
    if (pairs.contains(wordpair)) {
      correct = true;
      pairs.remove(wordpair); // one pair can only be correct once, every
                              // combination must be asked!
    } else {
      correct = false;
    }
    return correct;
  }
}
