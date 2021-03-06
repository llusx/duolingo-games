package cth.webapp.duogames.duogames.model.memory;

import lombok.Getter;

public class Pair {

    @Getter
    private final String word;
    @Getter
    private final String answer;

    public Pair(String word, String answer) {
        this.word = word;
        this.answer = answer;
    }

    public boolean equals(Pair newPair) {
        return (this.answer.equalsIgnoreCase(newPair.getAnswer())
                && this.word.equalsIgnoreCase(newPair.getWord()));
    }
}
