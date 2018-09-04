import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> path = search(setupGraph(setupHashmap()), "Julia Roberts", "Kevin Bacon");
        Collections.reverse(path);
        for (String i : path) {
            System.out.println(i);
        }
    }

    public static Graph setupGraph(HashMap<String, ArrayList<String>> hashMap) {
        Graph g = new Graph();
        for (String movieTitle : hashMap.keySet()) {
            Node movie = new Node(movieTitle);
            g.addNode(movie.name, movie, false);

            for (String actorName : hashMap.get(movie.name)) {
                Node actor = g.getNode(actorName);
                if (actor == null) {
                    actor = new Node(actorName);
                    g.addNode(actorName, actor, true);
                }
                movie.addEdge(actor);
            }
        }

        return g;
    }

    public static HashMap<String, ArrayList<String>> setupHashmap() {
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fr = new FileReader("test.json");
            int i;
            while ((i = fr.read()) != -1)
                sb.append((char) i);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IO Error");
            System.exit(1);
        }
        Pattern title_pattern = Pattern.compile("(\"title\")");
        Matcher m = title_pattern.matcher(sb.toString());
        int count = 0;
        while (m.find()) {
            count++;
        }
        JsonObject jsonObject = new Gson().fromJson(sb.toString(), JsonObject.class);
        HashMap<String, ArrayList<String>> movies = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String title = jsonObject.get("movies").getAsJsonArray().get(i).getAsJsonObject().get("title").getAsString();
            ArrayList<String> cast = jsonArrayToArrayList(jsonObject.get("movies").getAsJsonArray().get(i).getAsJsonObject().get("cast").getAsJsonArray());
            movies.put(title, cast);
        }
        return movies;
    }

    public static ArrayList<String> jsonArrayToArrayList(JsonArray jsonArray) {
        ArrayList<String> listdata = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                listdata.add(jsonArray.get(i).getAsString());
            }
        }
        return listdata;
    }

    public static ArrayList<String> search(Graph graph, String startName, String endName) {
        graph.softReset();
        Queue<Node> nodeQueue = new LinkedList<>();

        Node start = graph.getNode(startName);
        if (start == null) {
            System.out.println("Actor does not exist in db");
            System.exit(1);
        }
        nodeQueue.add(start);
        start.searched = true;

        Node end = graph.getNode(endName);
        while (!nodeQueue.isEmpty()) {
            Node current = nodeQueue.poll();

            if (current.name.equals(endName)) {
                System.out.println("Found!");
                break;
            }

            for (Node neighbor : current.edges) {
                if (!neighbor.searched) {
                    neighbor.searched = true;
                    neighbor.parent = current;
                    nodeQueue.add(neighbor);
                }
            }
        }

        ArrayList<String> nodeTitleList = new ArrayList<>();
        nodeTitleList.add(end.name);
        Node parent = end.parent;
        while (parent != null) {
            nodeTitleList.add(parent.name);
            parent = parent.parent;
        }

        return nodeTitleList;
    }
}
