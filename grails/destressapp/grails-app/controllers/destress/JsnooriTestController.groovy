package destress

import fr.loria.parole.jsnoori.model.teacher.feedback.Feedback;
import fr.loria.parole.jsnoori.model.audio.AudioSignal;
import fr.loria.parole.jsnoori.model.segmentation.*;

class JsnooriTestController {

    def index() { 
		try {
			String filepath = "/Users/Anjana/Dropbox/School/THESIS/CODE/thesis-code/Annotation/test_list1/Audio/SH05_501.wav";
			AudioSignal testaudio = new AudioSignal("testname", filepath);
			//Segmentation[] testseg = util.open(testfile);
			String output = "I opened a file! " + testaudio.name;
			render output; 
		} catch (Exception e) {
			render e.printStackTrace();
		}
	}
}
