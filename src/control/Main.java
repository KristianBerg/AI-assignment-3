package control;

import model.AwesomeLocalizer;
import model.DummyLocalizer;
import view.RobotLocalizationViewer;

public class Main {
	/*
	 * build your own if you like, this is just an example of how to start the viewer
	 * ...
	 */
	
	public static void main( String[] args) {
		int gridSize = 4;
		if(args.length > 0)
			gridSize = Integer.parseInt(args[0]);

		/*
		 * generate you own localiser / estimator wrapper here to plug it into the 
		 * graphics class.
		 */
		EstimatorInterface l = new AwesomeLocalizer(gridSize);

		RobotLocalizationViewer viewer = new RobotLocalizationViewer( l);

		/*
		 * this thread controls the continuous update. If it is not started, 
		 * you can only click through your localisation stepwise
		 */
		new LocalizationDriver( 20, viewer).start();
		// uncomment below to test average accuracy over multiple iterations
		/*while(true){
			l.update();
		}*/
	}
}	