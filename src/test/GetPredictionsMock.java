package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import dataAccess.DataAccess;
import domain.Prediction;
import exceptions.NoAnswers;
import junit.framework.TestCase;

@RunWith(MockitoJUnitRunner.class)
public class GetPredictionsMock extends TestCase {

	@Mock
	DataAccess dataAccess = Mockito.mock(DataAccess.class);

	@InjectMocks
	BLFacade sut = new BLFacadeImplementation(dataAccess);

	@Test
	public void getPredictionListCorrect() {		
		try {
			//test params
			List<Prediction> predictions = new ArrayList<Prediction>();
			predictions.add(new Prediction("F", 4));
			//mockito config
			Mockito.doReturn(predictions).when(dataAccess).getQuestionPredictions(Mockito.anyInt());
			//call sut
			List <Prediction> predictionsReality = sut.getQuestionPredictions(1);
			boolean valid = true;
			if (predictions.size() != predictionsReality.size()) {
				fail();
			} else {
				for (int i = 0; i < predictions.size(); i++) {
					if(!predictions.get(i).equals(predictionsReality.get(i))) {
						fail();
					}
				}
				assertTrue(valid);
			}
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void noAnswersError() {
		try {
			//test params
			List<Prediction> predictions = null;
			//mockito config
			Mockito.doThrow(new NoAnswers("No prediction")).when(dataAccess).getQuestionPredictions(Mockito.anyInt());
			//call sut
			sut.getQuestionPredictions(1);
			fail();
		} catch (NoAnswers na) {
			assertTrue(true);
		} catch(Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
