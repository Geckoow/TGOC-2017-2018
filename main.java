
import java.io.*;
import java.nio.file.Files;
import java.util.stream.Stream;

public class main{
	public static int[][] cost;
	private static String path;
	public static void rewrite(String path){
		try {
			File file = new File(path);
			RandomAccessFile tools = new RandomAccessFile(file, "rw");
			String l = tools.readLine();
			if(l.length() <  10) {
				long length = tools.length() - tools.getFilePointer();
				byte[] nexts = new byte[(int) length];
				tools.readFully(nexts);
				tools.seek(0);
				tools.write(nexts);
				tools.setLength(nexts.length);
				tools.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void output(String output){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("output" + path)));
			writer.write(output);
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static int[][] readData() throws IOException {
		path = "challenge1.dat";
		rewrite(path);
		return Files.lines(new File(path).toPath()).filter(l -> !l.isEmpty())
				.map(line -> line.trim().split("\\s+"))
				.map(spittedLine -> Stream.of(spittedLine).mapToInt(Integer::parseInt).toArray())
				.toArray(int[][]::new);
	}
	public static void main(String [] args) throws IOException {
		cost = readData();
		Solution2 bestSol = new Solution2(new Integer[2], cost);
		while(bestSol.getCost() != 23691){
			int[][] graph = new int[0][];
			Evolution2 evol = new Evolution2(50,1f/2,1f/2, cost);
			bestSol = evol.run(10);
			output(bestSol.toString());
		}
	}
}
