import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

import javax.swing.JFileChooser;

public class CFGconstructor {
	BufferedReader br = null;
	int numberofLeaders = 0, row = 0;
	boolean leaderSwitch = false;
	int[] leaders, leadersFinal;
	BasicBlock[] basicBlock;
	String fileName = "F:\\individual_project\\programs\\null pointer exception\\casestudy1\\bin\\casestudy1\\bytecode_formatted.txt";

	int[][] edge;

	public CFGconstructor() {

		leaders = new int[20];
		leaders[numberofLeaders++] = 0;
		edge = new int[10][2];

	}

	public void computeCFGInfo() throws IOException {

		try {
			// String filename=filePicker();
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String currentLine;
		while ((currentLine = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(currentLine);
			if (leaderSwitch) {

				String addr = st.nextToken();
				int presentAddress = Integer.parseInt(addr);
				leaders[numberofLeaders++] = presentAddress;
				System.out.println("leader: " + leaders[numberofLeaders - 1]);
				leaderSwitch = false;
				st = new StringTokenizer(currentLine);

			}

			while (st.hasMoreTokens()) {
				String token = st.nextToken();

				if (token.equals(":")) {
					// continue;
				}

				if (token.contains("wait()") || token.contains("notify()")) {

				}

				if (token.equals("monitorenter")) {

					StringTokenizer st1 = new StringTokenizer(currentLine);
					String addr = st1.nextToken();

					// monitorenter and monitorexit instruction are leaders
					int presentAddress = Integer.parseInt(addr);
					leaders[numberofLeaders++] = presentAddress;
					System.out.println("leader: " + leaders[numberofLeaders - 1]);

				}

				if (token.equals("monitorexit")) {
					leaderSwitch = true;
				}

				if (token.equals("goto") || token.equals("goto_w") || token.equals("jsr") || token.equals("jsr_w")
						|| token.equals("ret") || token.equals("tableswitch") || token.equals("lookupswitch")) {
					StringTokenizer st2 = new StringTokenizer(currentLine);
					String addr = st2.nextToken();
					int presentAddress = Integer.parseInt(addr);
					int targetAddress = Integer.parseInt(st.nextToken());
					leaders[numberofLeaders++] = targetAddress;
					System.out.println("leader: " + leaders[numberofLeaders - 1]);
					leaderSwitch = true;

					// construction of edge
					edge[row][0] = presentAddress;
					edge[row][1] = targetAddress;
					row++;

				}

				if (token.charAt(0) == 'i' && token.charAt(1) == 'f') {
					StringTokenizer st2 = new StringTokenizer(currentLine);
					String addr = st2.nextToken();
					int presentAddress = Integer.parseInt(addr);
					int targetAddress = Integer.parseInt(st.nextToken());
					leaders[numberofLeaders++] = presentAddress;
					leaders[numberofLeaders++] = targetAddress;
					leaderSwitch = true;
					// construction of edge
					edge[row][0] = presentAddress;
					edge[row][1] = targetAddress;
					row++;
				}

				// Separating monitorenter and monitorexit instructions

			} // end of while loop when currntLine doen't has no more tokens
		} // end of while loop when there is no more lines to read in file

	}

	private String filePicker() {
		JFileChooser fileChooser = new JFileChooser();
		// fileChooser.setCurrentDirectory(new
		// File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			return selectedFile.getAbsolutePath();
		}
		return null;
	}

	/*function to sort the leaders and remove zeors in unassigned space of array.*/
	int[][] sortLeaders() {
		leadersFinal = new int[numberofLeaders]; 
		int index = 0;
		/*copy each element from leaders to leadersfinal array avoiding zero's*/
		for (int i = 0; i < leaders.length; i++) {

			if (leaders[i] != 0) {

				leadersFinal[index++] = leaders[i];

			}
		}
		System.out.println(Arrays.toString(leadersFinal));
		leadersFinal = IntStream.of(leadersFinal).distinct().toArray();
		numberofLeaders = leadersFinal.length;
		Arrays.sort(leadersFinal);
		System.out.println("final leaders are: " +Arrays.toString(leadersFinal));
		for (int i = 0; i < edge.length; i++) {
			for (int j = 0; j < edge[i].length; j++) {
				System.out.print(edge[i][j]);

			}
			System.out.println("");
		}

		// GraphGenerator g=new GraphGenerator();
		// g.generateGraph(leadersFinal,edge);

		return null;
	}

	void basicBlockGenerator() throws IOException {
		basicBlock = new BasicBlock[numberofLeaders + 1];
		for (int i = 0; i < numberofLeaders + 1; i++) {
			basicBlock[i] = new BasicBlock();
		}
		basicBlock[0].setBlockName("start");
		basicBlock[0].setPrevBlock("null");
		basicBlock[0].setNextBlock("b0");
		basicBlock[0].setLeader(0);
		int i;
		for (i = 0; i < numberofLeaders; i++) {

			basicBlock[i].setBlockName("b" + i);
			basicBlock[i].setLeader(leadersFinal[i]);
			if (i == 0) {
				basicBlock[i].setPrevBlock("start");

			} else {
				basicBlock[i].setPrevBlock("b" + (i - 1));
			}
			if (i == numberofLeaders - 1) {
				basicBlock[i].setNextBlock("stop");
			} else {
				basicBlock[i].setNextBlock("b" + (i + 1));
			}

			try {
				br = new BufferedReader(new FileReader(fileName));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(currentLine);
				int token = Integer.parseInt(st.nextToken());
				if (token == (leadersFinal[i])) {
					if (i == numberofLeaders - 1) {
						basicBlock[i].addInstLines(leadersFinal[i]);
						int token1;
						while ((currentLine = br.readLine()) != null) {
							// currentLine=br.readLine();
							StringTokenizer st1 = new StringTokenizer(currentLine);
							token1 = Integer.parseInt(st1.nextToken());
							basicBlock[i].addInstLines(token1);

						}
					} else {
						basicBlock[i].addInstLines(leadersFinal[i]);
						int token1;
						do {
							currentLine = br.readLine();
							if (currentLine == null || i == numberofLeaders - 1) {
								break;
							}
							StringTokenizer st1 = new StringTokenizer(currentLine);
							token1 = Integer.parseInt(st1.nextToken());
							if (!(token1 == (leadersFinal[i + 1]))) {
								basicBlock[i].addInstLines(token1);
							}
						} while (!(token1 == (leadersFinal[i + 1])));
						// break;
					}
				}

			}

		}
		basicBlock[numberofLeaders].setBlockName("stop");
		basicBlock[numberofLeaders].setPrevBlock("b" + i);
		basicBlock[numberofLeaders].setNextBlock("null");
		basicBlock[numberofLeaders].setLeader(0);

		GraphGenerator g = new GraphGenerator();
		g.generateGraph(basicBlock, edge);

	}

	void printblock() {
		for (int i = 0; i < numberofLeaders; i++) {
			System.out.println("--------------------------------------------------");

			System.out.println("previous basic block: " + basicBlock[i].getPrevBlock());
			System.out.println("basic block name: " + basicBlock[i].getBlockName());
			System.out.println("next basic block: " + basicBlock[i].getNextBlock());
			System.out.println("leader is " + basicBlock[i].getLeader());
			System.out.println("basic block lines: " + basicBlock[i].getInstLines());
		}
	}

}
