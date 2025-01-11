package hu.ponte.homework.pontevotehomework.exception;

public class IdeaNotExistsException extends RuntimeException {


    public IdeaNotExistsException(Long ideaId) {
        super("Idea with id: " + ideaId + " does not exist");
    }

    public IdeaNotExistsException(String ideaText) {
        super("Idea with text:" + ideaText + " does not exist");
    }


}
