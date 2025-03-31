package services;
import Algorithms.IAlgoMatchingStrings;
import Algorithms.IAlgoRabinKarpSearch;
import Algorithms.IAlgoKMPSearch;
public class SearchService {
        private IAlgoMatchingStrings strategy;

        public void setSearchStrategy(IAlgoMatchingStrings strategy) {
            this.strategy = strategy;
        }

        public int setAutomaticStrategy(String text,String word) {
            if (word.length() > 10) {
                setSearchStrategy(new IAlgoKMPSearch());
            } else {
                setSearchStrategy(new IAlgoRabinKarpSearch());
            }
            return strategy.search(text, word);

        }

    }


