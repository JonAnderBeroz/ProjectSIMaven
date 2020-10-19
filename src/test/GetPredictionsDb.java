package test;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import configuration.ConfigXML;
import dataAccess.DataAccessImplementation;
import domain.Event;
import domain.Prediction;
import domain.Question;
import exceptions.NoAnswers;
import exceptions.QuestionNotFound;
import testBlDa.TestFacadeImplementation;
class GetPredictionsDb {

	static DataAccessImplementation sut = new DataAccessImplementation(ConfigXML.getInstance().getDataBaseOpenMode().equals("initialize"));
	static TestFacadeImplementation testBL = new TestFacadeImplementation();
	private Event ev;
	@Test
	void allCorrect() {
		try {
			List<Prediction> predictions = new ArrayList<Prediction>();
			predictions.add(new Prediction("f", 4));
			String queryText="proba galdera";
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date oneDate=null;;
			try {
				oneDate = sdf.parse("05/10/2022");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
			//configure the state of the system (create object in the dabatase)
			 ev = testBL.addEvent(queryText,oneDate );
			Question q = sut.createQuestion(ev, "f", 2, predictions);
			List<Prediction> result = sut.getQuestionPredictions(q.getQuestionNumber());
			if (predictions.size() != result.size()) {
				fail();
			} else {
				for (int i = 0; i < predictions.size(); i++) {
					if (!predictions.get(i).getPredictionNumber().equals(result.get(i).getPredictionNumber())) {
						fail();
					}
				}
				assertTrue(true);
			}
		}catch (Exception e) {
			e.printStackTrace();
			fail();
		}finally {
			testBL.removeEvent(ev);
		}
	}

	@Test
	void noAnswers() {
		try {
			List<Prediction> predictions = new ArrayList<Prediction>();
			String queryText="proba galdera";
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date oneDate=null;;
			try {
				oneDate = sdf.parse("05/10/2022");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
			//configure the state of the system (create object in the dabatase)
			 ev = testBL.addEvent(queryText,oneDate );
			Question q = sut.createQuestion(ev, "f", 2, predictions);
			sut.getQuestionPredictions(q.getQuestionNumber());
			fail();
		}catch (NoAnswers e) {
			assertTrue(true);
		}catch (Exception e) {
			fail();
		}finally {
			testBL.removeEvent(ev);
		}
	}
	
	@Test
	void questionNotFoundIdNotInDB() {
		try {
			sut.getQuestionPredictions(89);
			fail();
		}catch (QuestionNotFound e) {
			assertTrue(true);
		}catch (Exception e) {
			fail();
		}
	}
	
	@Test
	void questionNotFoundIdNull() {
		try {
			sut.getQuestionPredictions((Integer) null);
			fail();
		}catch (QuestionNotFound e) {
			assertTrue(true);
		}catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	void questionNotFoundIdNotValid() {
		try {
			sut.getQuestionPredictions(-4);
			fail();
		}catch (QuestionNotFound e) {
			assertTrue(true);
		}catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
