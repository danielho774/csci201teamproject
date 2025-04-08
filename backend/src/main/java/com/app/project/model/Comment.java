import java.time.LocalDateTime;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Comment {
    private int id;
    private int postId;
    private int commentId;
    private String comment;
    private LocalDateTime createdAt;

    private Map<Integer, List<Integer>> reactions;

    public void addReaction(int reactionId, int userId) {
        List<Integer> users = reactions.get(reactionId);
        if(users == null){
            users = new ArrayList<>();
            reactions.put(reactionId, users);
        }
        if(!users.contains(userId)) {
            users.add(userId);
        }
    }

    public void removeReaction(int reactionId, int userId) {
        List<Integer> users = reactions.get(reactionId);

        if(users == null || !users.contains(userId)) {
            System.out.println("This user has not reacted to this comment with this reaction.");
            return;
        }
        users.remove(Integer.valueOf(userId)); //dont remove by index on accident

        if(users.isEmpty()) {
            reactions.remove(reactionId);
        }
    }

    public Map<Integer, List<Integer>> getReactions() {
        return reactions;
    }
    
}
