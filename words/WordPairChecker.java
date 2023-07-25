package words;

/**
 * Interface for checking word pairs.
 */
public interface WordPairChecker {

  /**
   * Checks if a word pair was translated correctly (the definition of 'correct'
   * is in responsibility of the implementations).
   * 
   * @param wordpair
   * @return boolean
   */
  public boolean isCorrect(WordPair wordpair);

}
