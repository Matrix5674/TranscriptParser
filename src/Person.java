import java.util.ArrayList;

public class Person {

    private String name;
    private double duration;
    String startTime, endTime;
    private ArrayList<Double> timeTalked = new ArrayList<Double>();

    public Person(String name, double duration) {
        this.name = name;
        this.duration = duration;
    }

    public void setDuration(double d){
        duration = d;
    }

    public String getName() {
        return name;
    }


    public double getDuration() {
        return duration;
    }

    public ArrayList<Double> getTimeTalked() {
        return timeTalked;
    }


    public void addtime(double time){
        timeTalked.add(time);
    }
}
