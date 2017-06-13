import java.util.HashMap;

public class CodeStore {
	HashMap<Integer, String> hmap = new HashMap<Integer, String>();
	
	void addCodeEntry(int key,String code){
		hmap.put(key, code);
	}
}
