import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Main {


    public static void main(String[] args) {
        ArrayList<String> lines = new ArrayList<>();
        String PATH = "Assets/DobervichPlanningSession2.vtt";
        lines = readFile(PATH);
        ArrayList<Person> people = extractData(lines);


        //Average length talking until speaker switch

    }

    public static void avgLengthTalking(){

    }

    public static ArrayList<Person> extractData(ArrayList<String> lines){
        ArrayList<Person> people = new ArrayList<>();
        String startTime = lines.get(3);
        for (int line = 3; line < lines.size()-1; line+=4){
            people.add(new Person(readName(lines.get(line+1)), getTime(lines.get(line))));
        };
        String endTime = lines.get(lines.size()-3);

        for (String name : getListOfUniqueNames(people))
            for(Person p : people){
                if (p.getName().equals(name)){
                    p.addtime(p.getDuration());
                }
            }

    }

    public static int numOfMembers(ArrayList<Person> people){
        return getListOfUniqueNames(people).size();
    }


    public static void timeMembersTalked(ArrayList<Person> people){
        ArrayList<String> uniquePeople = getListOfUniqueNames(people);
        String a = "";
        double[] totalDurations = new double[uniquePeople.size()];


        for(int i = 0; i < uniquePeople.size(); i++){
            String uniquePerson = uniquePeople.get(i);
            for (Person person: people){
                if (uniquePerson.equals(person.getName())){
                    totalDurations[i] += person.getDuration();
                }
            }
        }

        for (int i = 0; i < uniquePeople.size(); i++){
            a += uniquePeople.get(i) + ": " + totalDurations[i];
        }

    }



    public static double getLengthOfMeeting(String endTime){
        String e = endTime.split("-->")[1];
        return getTime(("00:00:00.000 " + "--> " + e));
    }

    public static int numOfSpeakerSwitches(ArrayList<Person> people){
        return people.size();
    }



    public static String readSpeech (String line){
        return line.split(":")[2];
    }

    public static String readName(String line){
        return line.split(":")[0];
    }

    public static ArrayList<String> readFile (String PATH){
        ArrayList<String> lines = new ArrayList<>();
        try {
            List<String> contents = Files.readAllLines(Paths.get(PATH));
            lines.addAll(contents);
        } catch (IOException e){
            e.printStackTrace();
        }
        return lines;
    }

    public static double getTime (String time) {
        String[] times = time.split("-->");
        String t1 = times[0];
        String t2 = times[1];
        double startTime = convertToSeconds(formatTime(t1));
        double endTime = convertToSeconds(formatTime(t2));
        return endTime-startTime;
    }

    public static double[] formatTime(String t) {
        String[] result = t.split(":");
        double[] times = new double[4];
        for (int i = 0; i < result.length; i++){
            times[i] = Double.parseDouble(result[i]);
        }
        return times;
    }

    public static double convertToSeconds(double[] time){
        return (((time[0]*60)+time[1])*60)+time[2];
    }


    public static ArrayList<String> getListOfUniqueNames(ArrayList<Person> p){
        ArrayList<String> people = new ArrayList<>();
        for (Person person:p){
            if (!(people.contains(person.getName()))){
                people.add(person.getName());
            }
        }
        return people;
    }

}