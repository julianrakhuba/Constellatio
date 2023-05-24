package application;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.w3c.dom.Node;

public class XML {
	
	public static List<Node> children(Node n){
		return IntStream.range(0, n.getChildNodes().getLength()).mapToObj(n.getChildNodes()::item).collect(Collectors.toList());
	}
	
	public static String  atr(Node n, String name) {
		if(n.getAttributes().getNamedItem(name) != null) {
			return n.getAttributes().getNamedItem(name).getNodeValue();
		}else {
			return null;
		}		
	}
}
