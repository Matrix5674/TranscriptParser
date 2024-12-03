import java.util.ArrayList;

public class Person {
    private String name;
    private double duration;
    private double totalTimeTalked;
    private int timesSpeakerSwitched;

    public Person(String name, double duration) {
        this.name = name;
        this.duration = duration;
        this.totalTimeTalked = 0.0;
        this.timesSpeakerSwitched = 0;
    }

    public int getTimesSpeakerSwitched() {
        return timesSpeakerSwitched;
    }

    public void incrementTimesSpeakerSwitched() {
        this.timesSpeakerSwitched++;
    }


    public String getName() {
        return name;
    }

    public double getTimeTalked() {
        return totalTimeTalked;
    }

    public void addtime(double time){
        totalTimeTalked += time;
    }
}
