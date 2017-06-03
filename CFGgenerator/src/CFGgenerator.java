import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CFGgenerator {

	public static void main(String[] args) throws IOException {
		CFGconstructor cfg = new CFGconstructor();
		cfg.computeCFGInfo();
		cfg.sortLeaders();
		cfg.basicBlockGenerator();
		cfg.printblock();

		// GraphGenerator g=new GraphGenerator();
		// g.generateGraph();
		//

	}

}
