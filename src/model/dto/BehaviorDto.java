package model.dto;


public class BehaviorDto  implements Comparable<BehaviorDto> {
    int     behaviorId;
    String  name;
    int     score;

    public BehaviorDto(int behaviorId, String name, int score) {
        this.behaviorId = behaviorId;
        this.name = name;
        this.score = score;
    }

    public BehaviorDto() {}

    public int getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(int behaviorId) {
        this.behaviorId = behaviorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(BehaviorDto b) {
		return Integer.compare(this.getScore(), b.getScore());
    }
}
