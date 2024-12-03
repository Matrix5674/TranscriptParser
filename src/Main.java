import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Main {


    public static void main(String[] args) {
        String PATH = "Assets/DobervichPlanningSession2.vtt";

        runDataCollector(PATH);



    }

    public static void runDataCollector(String PATH){
        //Arraylist containing every individual line
        ArrayList<String> lines = readFile(PATH);

        //Arraylist of every instance someone talked.
        ArrayList<singleLineOfSpeech> instancesPeopleTalked = extractData(lines);

        //Total # of people
        System.out.println("Total # of members: " + numOfMembers(instancesPeopleTalked));

        //Total length of session
        System.out.println("Total length of session: " + getLengthOfMeeting(lines) + " seconds.");

        //Total speaking time
        System.out.println("Total speaking time: " + totalSpeakingTime(instancesPeopleTalked));

        //Total number of speaker switches
        System.out.println("Number of speaker switches: " + numOfSpeakerSwitches(instancesPeopleTalked));
        System.out.println();

        //Total talk time
        System.out.println("Total time members talked: ");
        System.out.println(timeMembersTalked(instancesPeopleTalked));
        System.out.println();

        //Average length talking until speaker switch = totalTimeTalked/NumberOfTimesSpeakerSwitched
        System.out.println("Average time talked: ");
        System.out.println(avgLengthTalking(instancesPeopleTalked));
    }

    public static String avgLengthTalking(ArrayList<singleLineOfSpeech> instancesPeopleTalked){

        ArrayList<Person> uniqueNames = getArrayListOfUniquePeople(instancesPeopleTalked);
        String result = "";



        for (Person up : uniqueNames) { //for every unique person
            String currName = up.getName();
            String lastRegisteredName = "";
            for (singleLineOfSpeech p : instancesPeopleTalked) { // for every time someone talked
                String newName = p.getName();
                if (p.getName().equals(up.getName())) { // if "up" is talking
                    up.addtime(p.getDuration()); //add time to up's arraylist
                }
                if ((newName.equals(currName))){
                    if (!(lastRegisteredName.equals(currName))) up.incrementTimesSpeakerSwitched();
                }
                lastRegisteredName = newName;
            }

            result += (up.getName() + " : " + up.getTimeTalked()/up.getTimesSpeakerSwitched() + " seconds.") + "\n";

        }
        return result;


    }

    public static String totalSpeakingTime (ArrayList<singleLineOfSpeech> instancesPeopleTalked){
        double result = 0.0;
        for(singleLineOfSpeech s : instancesPeopleTalked){
            result += s.getDuration();
        }
        return result + " seconds.";
    }

    public static int numOfSpeakerSwitches(ArrayList<singleLineOfSpeech> instancesPeopleTalked){
        //Dobervich can talk 2 times and the method will count that as a speaker switch.
        String currName = "";
        int count = 0;
        for (singleLineOfSpeech line: instancesPeopleTalked){
            String newName = line.getName();
            if (!(newName.equals(currName))){
                currName = newName;
                count++;
            }
        }
        return count-1; //count starts at 1 higher than it should be
    }

    public static String readName(String line){
        //What if the file isn't as expected, and colon doesn't exist?
        String[] nameSplit = line.split(":");
        if (nameSplit.length > 1){
            return nameSplit[0];
        } else return null;
    }

    public static int numOfMembers(ArrayList<singleLineOfSpeech> instancesPeopleTalked){
        return getArrayListOfUniquePeople(instancesPeopleTalked).size();
    }

    public static double getLengthOfMeeting(ArrayList<String> lines){
        String endTime = lines.get(lines.size()-3).split("-->")[1];

        return getTime(("00:00:00.000 " + "--> " + endTime));
    }

    public static String timeMembersTalked(ArrayList<singleLineOfSpeech> instancesPeopleTalked){
        ArrayList<Person> uniquePeople = getArrayListOfUniquePeople(instancesPeopleTalked);
        double[] totalTimeMembersTalked = new double[uniquePeople.size()];
        String result = "";


        for(int i = 0; i < uniquePeople.size(); i++){ //for every person
            String uniquePerson = uniquePeople.get(i).getName(); //get name of person
            for (singleLineOfSpeech singleLineOfSpeech : instancesPeopleTalked){ //every time that name appears in the transcript
                if (uniquePerson.equals(singleLineOfSpeech.getName())){
                    totalTimeMembersTalked[i] += singleLineOfSpeech.getDuration(); //get the total time the person talked
                }
            }
        }

        for (int i = 0; i < uniquePeople.size(); i++){
            result += uniquePeople.get(i).getName() + ": " + totalTimeMembersTalked[i] + " seconds." + "\n";
        }

        return result;




    }

    public static double getTime (String time) {
        String[] times = time.split("-->");
        String t1 = times[0];
        String t2 = times[1];
        double startTime = convertToSeconds(formatTime(t1));
        double endTime = convertToSeconds(formatTime(t2));
        return endTime-startTime;
    }

    public static ArrayList<Person> getArrayListOfUniquePeople(ArrayList<singleLineOfSpeech> s){
        ArrayList<String> uniquePeopleNames = new ArrayList<>();
        ArrayList<Person> uniquePeopleObjects = new ArrayList<>();

        for (singleLineOfSpeech singleLineOfSpeech :s){
            if (!(uniquePeopleNames.contains(singleLineOfSpeech.getName()))){
                uniquePeopleNames.add(singleLineOfSpeech.getName());
                uniquePeopleObjects.add(new Person(singleLineOfSpeech.getName(), 0));
            }
        }
        return uniquePeopleObjects;
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

    public static ArrayList<singleLineOfSpeech> extractData(ArrayList<String> lines){
        ArrayList<singleLineOfSpeech> instancesPeopleTalked = new ArrayList<>();
        for (int line = 3; line < lines.size()-1; line+=4){
            String name = readName(lines.get(line+1));
            if (name == null){name = instancesPeopleTalked.getLast().getName();}
            instancesPeopleTalked.add(new singleLineOfSpeech(name, getTime(lines.get(line))));
        };




        return instancesPeopleTalked;

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




}