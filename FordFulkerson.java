import java.io.*;
import java.util.*;

public class FordFulkerson {

    public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph) {
        ArrayList<Integer> Stack = new ArrayList<Integer>();

        ArrayList<Edge> edges = graph.getEdges();

        ArrayList<Integer> temp = new ArrayList<>(source);
        Stack.add(source);
        boolean b = false;

        for (int i = 0; i <= edges.size(); i++) {
            for (int j = 0; j <= edges.size(); j++) {
                if (graph.getEdge(source, j) != null && graph.getEdge(source, j).weight != 0 && !temp.contains(j)) {
                    temp.add(j);
                    Stack.add(j);
                    source = j;
                    b = true;
                }
            }
            if (!b) {
                if (Stack.size() != 1) {
                    Stack.remove(Stack.size() - 1);
                    source = Stack.get(Stack.size() - 1);
                } else {
                    return null;
                }
            }
            if (!source.equals(destination)) {
                b = false;
            } else {
                break;
            }
        }
        return Stack;
    }

    public static void fordfulkerson(Integer source, Integer destination, WGraph graph, String filePath) {
        String answer = "";
        String myMcGillID = "260788250"; //Please initialize this variable with your McGill ID
        int maxFlow = 0;

        WGraph copy = new WGraph(graph);
        ArrayList<Edge> copyE = copy.getEdges();
        ArrayList<Integer> path = pathDFS(source, destination, copy);
        for (Edge e : copyE) {
            e.weight = 0;
        }
        ArrayList<Edge> edges = graph.getEdges();
        int minCap = edges.get(0).weight;
        for (Edge e : edges) {
            if (e.weight < minCap)
                minCap = e.weight;
        }
        while (true) {
            copyE = copy.getEdges();
            ArrayList<Edge> temp = new ArrayList<>();
            WGraph g = new WGraph();

            for (Edge e : copyE) {
                int c0 = e.nodes[0];
                int c1 = e.nodes[1];
                Edge c = graph.getEdge(c0, c1);
                if (e.weight < c.weight) {
                    Edge edge = new Edge(c0, c1, (c.weight - e.weight));
                    int gc0 = edge.nodes[0];
                    int gc1 = edge.nodes[1];
                    Edge gc = g.getEdge(gc0, gc1);
                    if (gc == null) {
                        g.addEdge(edge);
                        temp.add(edge);
                    } else {
                        gc.weight = graph.getEdge(c0, c1).weight - e.weight;
                    }
                }
            }
            ArrayList<Edge> residE = g.getEdges();
            ArrayList<Integer> resid = pathDFS(source, destination, g);

            if (resid == null) {
                for (int i = 0; i < copy.getNbNodes(); i++) {
                    if (copy.getEdge(i, destination) != null)
                        maxFlow += copy.getEdge(i, destination).weight;
                }
                graph = copy;
                break;
            }
            minCap = residE.get(0).weight;
            for (Edge e : residE) {
                if (e.weight < minCap) {
                    minCap = e.weight;
                }
            }

            int residS = resid.size() - 1;
            for (int i = 0; i < residS; i++) {
                g.getEdge(resid.get(i), resid.get(i + 1)).weight = minCap;
            }

            for (int i = 0; i < residS; i++) {
                int e0 = resid.get(i);
                int e1 = resid.get(i + 1);
                Edge e = g.getEdge(e0, e1);
                if (temp.contains(e)) //check after
                    copy.getEdge(e0, e1).weight += e.weight;
            }
        }
        answer += maxFlow + "\n" + graph.toString();
        writeAnswer(filePath + myMcGillID + ".txt", answer);
        System.out.println(answer);
    }

    public static void writeAnswer(String path, String line) {
        BufferedReader br = null;
        File file = new File(path);
        // if file doesnt exists, then create it

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(line + "\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String file = args[0];
        File f = new File(file);
        WGraph g = new WGraph(file);
        fordfulkerson(g.getSource(), g.getDestination(), g, f.getAbsolutePath().replace(".txt", ""));
    }
}