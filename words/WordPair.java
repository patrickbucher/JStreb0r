package words;

/**
 * Represents a pair of words (one in the native- and one in a foreign-language)
 */
public class WordPair {

  /** the native word (in your native language) */
  private String nativeWord;

  /** the foreign word (in a foreign language) */
  private String foreignWord;

  /** should the word be asked? */
  private boolean ask;

  /**
   * Constructor
   */
  public WordPair() {
  }

  /**
   * Constructor
   * 
   * @param nativeWord
   * @param foreignWord
   */
  public WordPair(String nativeWord, String foreignWord) {
    this.nativeWord = nativeWord;
    this.foreignWord = foreignWord;
  }

  /**
   * @return String - the native word
   */
  public String getNativeWord() {
    return nativeWord;
  }

  /**
   * @param nativeWord
   */
  public void setNativeWord(String nativeWord) {
    this.nativeWord = nativeWord;
  }

  /**
   * @return String - the foreign word
   */
  public String getForeignWord() {
    return foreignWord;
  }

  /**
   * @param foreignWord
   */
  public void setForeignWord(String foreignWord) {
    this.foreignWord = foreignWord;
  }

  /**
   * @return boolean
   */
  public boolean isAsk() {
    return ask;
  }

  /**
   * @param ask
   */
  public void setAsk(boolean ask) {
    this.ask = ask;
  }

  /**
   * generated hashCode-method
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((foreignWord == null) ? 0 : foreignWord.hashCode());
    result = prime * result
        + ((nativeWord == null) ? 0 : nativeWord.hashCode());
    return result;
  }

  /**
   * generated equals-method
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final WordPair other = (WordPair) obj;
    if (foreignWord == null) {
      if (other.foreignWord != null)
        return false;
    } else if (!foreignWord.equals(other.foreignWord))
      return false;
    if (nativeWord == null) {
      if (other.nativeWord != null)
        return false;
    } else if (!nativeWord.equals(other.nativeWord))
      return false;
    return true;
  }

  /**
   * @return String
   */
  public String toString() {
    return nativeWord.concat(" ").concat(foreignWord);
  }

}
