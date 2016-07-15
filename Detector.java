import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Detector {
	
	public static void detectClaw(UndirectedGraph graph){
		Map phase1Result = DetectClaw.phaseOne(graph);
		if((boolean)phase1Result.get("clawFound")){
			UndirectedGraph claw = (UndirectedGraph)phase1Result.get("claw");
			Utility.printGraph(claw);
		}else{
			Map phase2Result = DetectClaw.phaseTwo(graph);
			if((boolean)phase2Result.get("clawFound")){
				UndirectedGraph claw = (UndirectedGraph)phase2Result.get("claw");
				Utility.printGraph(claw);
			}else{
				System.out.println("Claw not found");
			}
		}
	}
}
