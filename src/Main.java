import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static ArrayList<String> lines = new ArrayList<>();
    public static ArrayList<People> people = new ArrayList<>();
    public static String PATH = "Assets/DobervichPlanningSession2.vtt";

    public static void main(String[] args) {
        lines = readFile(PATH);

        for (int line = 3; line < lines.size()-1; line+=4){
            people.add(new People(readName(lines.get(line+1)), getTime(lines.get(line))));
        };

    }

    public static int numOfSpeakerSwitches(){
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

    public static Double[] formatTime(String t) {
        String[] result = t.split(":");
        Double[] times = new Double[4];
        for (int i = 0; i < result.length; i++){
            times[i] = Double.parseDouble(result[i]);
        }
        return times;
    }

    public static double convertToSeconds(Double[] time){
        return (((time[0]*60)+time[1])*60)+time[2];
    }

}