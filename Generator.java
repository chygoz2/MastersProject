import java.util.Random; 
import java.util.*;
import java.text.*;
import java.math.*;
import java.nio.file.*;
import java.nio.charset.Charset;

public class Generator
{
	static int smallestGraph;
	static int largestGraph;
	static int densityCourseness;
	static int numOfGraphs;

	public static void generateGraphs(int s, int l, int d, int n)
	{
		double increment = 1/(double)d;

		for (int i = s; i != l; i++)
		{
			for (double j = increment; j <= 1; j = j + increment)
			{
				//generate a graph of size i and density j, and label it with number k
				for (int k = 0; k !=numOfGraphs; k++)
				{
					int[][] graph = generateGraph(i, j, k);
					ArrayList<String> g = graphToString(graph);
					createFile(i, j, k, g);
					// for (String p : g)
					// {
					// 	System.out.println(g);
					// 	System.out.println();
					// }
					if (j == 1)
					{
						break;
					}				
				}
			}
		}
	}

	public static int[][] generateGraph(int size, double density, int label)
	{
		int[][] graph = new int[size][size];

		for (int i = 0; i != size; i++)
		{
			for (int j = i; j != size; j++)
			{
				double r = Math.random();
				if (r <= density)
				{
				    graph[i][j] = 1;
					graph[j][i] = graph[i][j];
				}
			}
		}

		// for (int i = 0; i != size; i++)
		// {
		// 	for (int j = 0; j != size; j++)
		// 	{
		// 		System.out.print(graph[i][j] + "");
		// 	}
		// 	System.out.print("\n");
		// }

		// System.out.println();
		return graph;
	}

	public static ArrayList<String> graphToString(int[][] graph)
	{
		ArrayList<String> lines = new ArrayList<String>();

		int size = graph[0].length;

		for (int i = 0; i != size; i++)
		{
			String line = "";

			for (int j = 0; j != size; j++)
			{
				line = line + graph[i][j] + " "; 
			}

			lines.add(line);
		}

		return lines;
	}

	public static void createFile(int size, double density, int label, ArrayList<String> lines)
	{
		DecimalFormat df = new DecimalFormat("#.#");
		df.setRoundingMode(RoundingMode.FLOOR);
		double densityRounded = ((Number) density).doubleValue();
		String topDir = "./generated_graphs";
		String sizeDir = "./generated_graphs/size_" + size + "/";
		String fname = "./generated_graphs/size_" + size + "/" + "graph_" + size + "_" + df.format(densityRounded) + "_" + label + ".txt";

		// String fname = "graph_" + size + "_" + df.format(densityRounded) + "_" + label + ".txt";

		try
		{
			Path file = Paths.get(fname);
			Files.createDirectories(file.getParent());
			Files.write(file, lines, Charset.forName("UTF-8"));
			System.out.println("Writing to " + fname);
		}
		catch(Exception e)
		{
			System.out.println("oops");
		}
	}

	public static void main(String[] args) 
	{
		if (args.length == 0)
		{
			System.out.println("Input is of the form \n \t java Generate _lowerbound_ _upperbound_ _densitysteps_ _#graphs_");
		}
		else
		{
			smallestGraph = Integer.parseInt(args[0]);
			largestGraph = Integer.parseInt(args[1]);
			densityCourseness = Integer.parseInt(args[2]);
			numOfGraphs = Integer.parseInt(args[3]);

			generateGraphs(smallestGraph, largestGraph, densityCourseness, numOfGraphs);
		}
	} 
}