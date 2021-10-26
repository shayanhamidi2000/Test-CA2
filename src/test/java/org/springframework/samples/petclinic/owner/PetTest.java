package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PetTest {
	@Test
    public void StateVerification_removeVisit_aDummyVisitIsProvided_dummyVisitMustBeRemovedFromVisit(){
		// Arrange
		Visit dummyVisit = new Visit();
		Set<Visit> visitsInternal = new HashSet<>();
		visitsInternal.add(dummyVisit);
		Pet cut = new Pet();
		cut.setVisitsInternal(visitsInternal);

		// Act
		cut.removeVisit(dummyVisit);

		// Assert
		assertTrue(cut.getVisits().size() == 0, "No Visits Must Be left!");
	}

	@Test
	public void BehaviourVerification_removeVisit_aDummyVisitIsProvided_dummyVisitMustBeRemovedFromVisit(){
		// Arrange
		Visit dummyVisit = new Visit();
		Pet cut = spy(new Pet());
		Set<Visit> mockedVisitList = mock(Set.class);
		when(mockedVisitList.remove(dummyVisit)).thenReturn(true);
		when(cut.getVisitsInternal()).thenReturn(mockedVisitList);

		// Act
		cut.removeVisit(dummyVisit);

		// Assert
		verify(mockedVisitList, times(1)).remove(dummyVisit);
		verify(cut, times(1)).getVisitsInternal();
	}
}
