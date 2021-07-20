import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Coding_Project_Final {
	// function to get the maximum number of an array.
	public static int getmax(int[] a) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < a.length; i++) {
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	// function to get the minimum number of an array.
	public static int getmin(int[] a) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < a.length; i++) {
			if (a[i] < min)
				min = a[i];
		}
		return min;
	}

	// function to print the shortest distance and path
	// between source vertex and destination vertex
	private static int getShortestDistance(ArrayList<ArrayList<Integer>> adj, int s, int dest, int v) {
		// predecessor[i] array stores predecessor of
		// i and distance array stores distance of i
		// from s
		int pred[] = new int[v];
		int dist[] = new int[v];

		BFS(adj, s, dest, v, pred, dist); // Call the BFS method here so we can store the distances.

		return dist[dest]; // Returns The shortest path distance between 2 points.

	}

	// a modified version of BFS that stores predecessor
	// of each vertex in array pred
	// and its distance from source in array dist
	private static boolean BFS(ArrayList<ArrayList<Integer>> adj, int src, int dest, int v, int pred[], int dist[]) {
		// a queue to maintain queue of vertices whose
		// adjacency list is to be scanned as per normal
		// BFS algorithm using LinkedList of Integer type
		LinkedList<Integer> queue = new LinkedList<Integer>();

		// boolean array visited[] which stores the
		// information whether ith vertex is reached
		// at least once in the Breadth first search
		boolean visited[] = new boolean[v];

		// initially all vertices are unvisited
		// so v[i] for all i is false
		// and as no path is yet constructed
		// dist[i] for all i set to infinity
		for (int i = 0; i < v; i++) {
			visited[i] = false;
			dist[i] = Integer.MAX_VALUE;
			pred[i] = -1;
		}

		// now source is first to be visited and
		// distance from source to itself should be 0
		visited[src] = true;
		dist[src] = 0;
		queue.add(src);

		// bfs Algorithm for graph traversal.
		while (!queue.isEmpty()) {
			int u = queue.remove();
			for (int i = 0; i < adj.get(u).size(); i++) {
				if (visited[adj.get(u).get(i)] == false) {
					visited[adj.get(u).get(i)] = true;
					dist[adj.get(u).get(i)] = dist[u] + 1;
					pred[adj.get(u).get(i)] = u;
					queue.add(adj.get(u).get(i));

					// stopping condition (when we find
					// our destination)
					if (adj.get(u).get(i) == dest)
						return true;
				}
			}
		}
		return false;
	}

	// function to form edge between two vertices
	// source and dest
	private static void addEdge(ArrayList<ArrayList<Integer>> adj, int i, int j) {
		adj.get(i).add(j);
		adj.get(j).add(i);
	}

	public static void main(String args[]) throws FileNotFoundException {
		// Paste The path of your mol file down here.
		// The mol file should be located in the same directory
		// as the java file.
		File molecules = new File(
				"C:\\Users\\PCuser\\Desktop\\Work\\LAU\\Semestre 6 (Fall 2020)\\BIF455\\Coding project\\Short2\\src\\8.mol");
		String s = "";
		String d = "";
		Scanner scan = new Scanner(molecules);
		// Method to input file into a String.
		while (scan.hasNextLine()) {
			s = scan.nextLine();
			d += s + "\r\n";
		}
		// d is the String storing our mol file.

		// Using Regex, we will search the String called line
		// that has our mol file, and search for all the important
		// content that we need, like No of vertices, No of Edges,
		// and connectivity table between atoms.

		// We used () in our regex to capture the important data we need for our
		// program.
		String line = d;
		String pattern_name = ">  <IUPAC NAME>.*\\r\\n(.*)";
		String pattern_vertices_edges = "\\n (\\d+)\\s+(\\d+).*\\sV\\d{4}\\s";
		String pattern_columns = "\\n\\s+-?\\d+.\\d+\\s+-?\\d+.\\d+\\s+\\d+.\\d+\\s+[A-Z].*((\\r\\n\\s+\\d+\\s+\\d+\\s+\\d+.*)+)";

		// Create a Pattern object
		// THis first pattern is to capture the molecule IUPAC name

		Pattern r = Pattern.compile(pattern_name);

		// Now create matcher object to search for it
		Matcher m = r.matcher(line);
		if (m.find()) {
			System.out.println("IUPAC NAME: " + m.group(1));
		} else {
			System.out.println("No IUPAC name found.");
		}
		// Create a Pattern object
		// This pattern Object is for The vertex (atom no) and edges (no of connrctions)
		Pattern ver_edg = Pattern.compile(pattern_vertices_edges);

		// Now create matcher object to search for it
		Matcher ve = ver_edg.matcher(line);
		if (ve.find()) {

			System.out.println("This molecule is made of " + ve.group(1) + " atoms");
			System.out.println("There is a total of " + ve.group(2) + " connections in this molecule");
		} else {
			System.out.println("NO MATCH");
		}
		// Create a Pattern object For the Columns that we need to calculate
		// our petitjean index
		Pattern X = Pattern.compile(pattern_columns);

		// Now create matcher object to search for it
		Matcher Z = X.matcher(line);
		if (Z.find()) {
			// sort out my array situation
			// split by newline, then split those by space.

			// Number of connections in a molecule (or No of Edges).
			int edge = Integer.parseInt(ve.group(2));

			// array storing the first column of connection between atoms
			// from a mol file.
			int[] col1 = new int[edge];

			// array storing the second column of connection between atoms
			// from a mol file
			int[] col2 = new int[edge];

			// Search through the MolFile to get our specific
			// connection columns.
			String[] Arr = Z.group(1).split("\\R+");
			for (int i = 1; i <= edge; i++) {
				String[] theline = Arr[i].split("\\s+");

				// filling the column arrays by decreasing the number of the mol file by 1
				// because graphs in java starts with the original vertex of 0 and not 1.
				col1[i - 1] = Integer.parseInt(theline[1]);
				col2[i - 1] = Integer.parseInt(theline[2]);
			}
			// Number of atoms in a molecule (or No of Vertices).
			int v = Integer.parseInt(ve.group(1));

			// Adjacency list for storing which Atoms\vertices are connected.
			ArrayList<ArrayList<Integer>> adj = new ArrayList<ArrayList<Integer>>(v);
			for (int i = 0; i < v; i++) {
				adj.add(new ArrayList<Integer>());
			}

			// Creating Our Graph by connecting the atoms with a function called AddEdge.
			for (int i = 0; i < edge; i++) {
				int x = col1[i] - 1;
				int y = col2[i] - 1;
				addEdge(adj, x, y);

			}
			// Array that stores the Eccentricity for each atom\vertex.
			int[] Eccentricities = new int[v];

			// Array that stores All the shortest paths to all possible destinations for 1
			// single atom\vertex.
			int[] b = new int[v];

			for (int f = 0; f < v; f++) {
				// Looping over all the possible sources/starting atoms.

				for (int p = 0; p < v; p++) {
					// Looping over all the possible destinations for each source/atom.

					// Shortest path between 1 atom and the other.
					int q = getShortestDistance(adj, f, p, v);

					// Store all the shortest path for every possible destination of 1 atom.
					b[p] = q;
				}
				// Choose the highest shortest path for all possible destination of an
				// atom.
				// This number is known as Eccentricity
				// All The Eccentricities for all the atoms are stored in this
				// "Eccentricities Array".
				Eccentricities[f] = getmax(b);
			}

			// The radius in a graph is the lowest eccentricity of the graph.
			int radius = getmin(Eccentricities);
			System.out.println("Radius: " + radius);

			// The diameter in a graph is the highest eccentricity of the graph.
			int diameter = getmax(Eccentricities);
			System.out.println("Diameter: " + diameter);
			// Calculation of the PetitJean Index from radius and diameter values.
			float Numerator = (diameter - radius);
			float Denominator = radius;
			float PetitJeanIndex = Numerator / Denominator;
			System.out.println("The PetitJean Index of this molecule is: " + PetitJeanIndex);

		}
		// else statement to not write error in console if we dont find our specific
		// value.
		else
			System.out.println("NO MATCH");

	}
}
