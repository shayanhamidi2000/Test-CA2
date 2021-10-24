package org.springframework.samples.petclinic.owner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import java.util.ArrayList;
import java.util.List;


public class PetManagerTest {

    // approach: Mockisty
    @Test
    public void findOwner_anIdIsProvided_findByIdIsCalledOnOwnersWithProvidedId(){
        // Arrange
        org.slf4j.Logger mockedLogger = mock(org.slf4j.Logger.class); // (Mock Object)
        OwnerRepository mockedOwnerRepository = mock(OwnerRepository.class); // (Mock Object)
        Owner dummyOwner = new Owner(); // (dummy Owner)
        int ownerId = 1;
        doNothing().when(mockedLogger).info("find owner {}", ownerId);
        when(mockedOwnerRepository.findById(ownerId)).thenReturn(dummyOwner);
        PetManager cut = new PetManager(mock(PetTimedCache.class), // mocked Object
                mockedOwnerRepository,
                mockedLogger);

        // Act
        Owner actualOwner = cut.findOwner(ownerId);

        // Assert
        assertEquals(dummyOwner, actualOwner, "The owners do not correspond!");
        verify(mockedLogger, times(1)).info("find owner {}", ownerId); // (behaviour verification)
        verify(mockedOwnerRepository, times(1)).findById(ownerId); // (behaviour verification)
    }

    // approach: Mockisty
    @Test
    public void newPet_anOwnerIsProvided_aNewPetMustBeInstantiatedAndaddPetIsCalledOnOwner(){
        // Arrange
        org.slf4j.Logger mockedLogger = mock(org.slf4j.Logger.class); // (Mock Object)
        Owner mockedOwner = mock(Owner.class); // (Mocked Owner)
        doNothing().when(mockedOwner).addPet(any(Pet.class));
        int ownerId = 1;
        when(mockedOwner.getId()).thenReturn(ownerId);
        doNothing().when(mockedLogger).info("add pet for owner {}", ownerId);
        PetManager cut = new PetManager(mock(PetTimedCache.class), // mocked Object
                mock(OwnerRepository.class),
                mockedLogger);
        // Act
        Pet actualPet = cut.newPet(mockedOwner);

        // Assert
        verify(mockedLogger, times(1)).info("add pet for owner {}", ownerId); // (behaviour verification)
        verify(mockedOwner, times(1)).addPet(any(Pet.class)); // (behaviour verification)
        verify(mockedOwner, times(1)).getId(); // (behaviour verification)
    }

    // approach: Mockisty
    @Test
    public void findPet_anIdIsProvided_getIsCalledOnPetsWithProvidedId(){
        // Arrange
        org.slf4j.Logger mockedLogger = mock(org.slf4j.Logger.class); // (Mock Object)
        PetTimedCache mockedPets = mock(PetTimedCache.class); // (Mock Object)
        int petId = 1;
        Pet dummyPet = new Pet(); // (Dummy Pet)
        doNothing().when(mockedLogger).info("find pet by id {}", petId);
        when(mockedPets.get(petId)).thenReturn(dummyPet);
        PetManager cut = new PetManager(mockedPets,
                mock(OwnerRepository.class), // mocked Object
                mockedLogger);

        // Act
        Pet actualPet = cut.findPet(petId);

        // Assert
        assertEquals(dummyPet, actualPet, "The Pets do not correspond!");
        verify(mockedLogger, times(1)).info("find pet by id {}", petId); // (behaviour verification)
        verify(mockedPets, times(1)).get(petId); // (behaviour verification)
    }

    // Mockisty
    @Test
    public void savePet_aPetAndAnOwnerAreProvided_saveIsCalledOnPetsWithProvidedPetAndAlsoAddPetIsCalledOnProvidedOwnerWithProvidedPet(){
        // Arrange
        org.slf4j.Logger mockedLogger = mock(org.slf4j.Logger.class); // (Mock Object)
        PetTimedCache mockedPets = mock(PetTimedCache.class); // (Mock Object)
        int petId = 1;
        Pet mockedPet = mock(Pet.class); // (Mock Object)
        Owner mockedOwner = mock(Owner.class);
        when(mockedPet.getId()).thenReturn(petId);
        doNothing().when(mockedOwner).addPet(mockedPet);
        doNothing().when(mockedLogger).info("save pet {}", petId);
        doNothing().when(mockedPets).save(mockedPet);
        PetManager cut = new PetManager(mockedPets,
                mock(OwnerRepository.class),// mocked Object
                mockedLogger);

        // Act
        cut.savePet(mockedPet, mockedOwner);

        // Assert
        verify(mockedLogger, times(1)).info("save pet {}", petId); // (behaviour verification)
        verify(mockedPets, times(1)).save(mockedPet); // (behaviour verification)
        verify(mockedOwner, times(1)).addPet(mockedPet); // (behaviour verification)
        verify(mockedPet, times(1)).getId(); // (behaviour verification)
    }

    // Mockisty
    @Test
    public void getOwnerPets_anOwnerIdIsProvided_findOwnerIsCalledAndCorrespondingPetsAreReturned(){
        // Arrange
        int ownerId = 1;
        org.slf4j.Logger mockedLogger = mock(org.slf4j.Logger.class); // (mock Object)
        doNothing().when(mockedLogger).info("finding the owner's pets by id {}", ownerId);
        List<Pet> expectedOwnerPetList = new ArrayList<>(); // (dummy Object)
        Owner mockedOwner = mock(Owner.class); // (mock Object)
        when(mockedOwner.getPets()).thenReturn(expectedOwnerPetList);
        PetManager cut = spy(new PetManager(mock(PetTimedCache.class), // (mock object)
                mock(OwnerRepository.class), // (mock Object)
                mockedLogger
        ));
        when(cut.findOwner(ownerId)).thenReturn(mockedOwner);

        // Act
        List<Pet> actualOwnerPetList = cut.getOwnerPets(ownerId);

        // Assert
        assertEquals(expectedOwnerPetList, actualOwnerPetList, "The Pet Lists do not correspond!");
        verify(cut, times(2)).findOwner(ownerId);
        verify(mockedLogger, times(1)).info("finding the owner's pets by id {}", ownerId);
        verify(mockedOwner, times(1)).getPets();
    }
}
