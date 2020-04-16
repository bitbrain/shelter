package de.bitbrain.shelter.core.story;

import de.bitbrain.shelter.i18n.Bundle;
import de.bitbrain.shelter.i18n.Messages;

import java.util.ArrayList;
import java.util.List;

public class StoryTeller {

    private List<String> texts;

    public StoryTeller(Messages ... messages) {
        texts = new ArrayList<String>();
        for (Messages message : messages) {
            texts.add(Bundle.get(message));
        }
    }

    public String getNextStoryPoint() {
        if (texts.isEmpty()) {
            return "";
        }
        return texts.remove(0);
    }

    public boolean hasNextStoryPoint() {
        return !texts.isEmpty();
    }
}