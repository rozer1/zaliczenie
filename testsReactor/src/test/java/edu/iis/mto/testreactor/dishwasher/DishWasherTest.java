package edu.iis.mto.testreactor.dishwasher;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.iis.mto.testreactor.dishwasher.engine.Engine;
import edu.iis.mto.testreactor.dishwasher.pump.WaterPump;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class DishWasherTest {

    @Test
    public void withDoorOpenDishWasherShouldReturnStatus() {
        Door door = mock(Door.class);
        DirtFilter dirtFilter = mock(DirtFilter.class);
        Engine engine = mock(Engine.class);
        WaterPump waterpPump = mock(WaterPump.class);

        when(door.closed()).thenReturn(false);
        when(dirtFilter.capacity()).thenReturn(60.0d);

        DishWasher dishWasher = new DishWasher(waterpPump, engine, dirtFilter, door);
        ProgramConfiguration program = ProgramConfiguration.builder().withProgram(WashingProgram.ECO).withTabletsUsed(true).withFillLevel(FillLevel.HALF).build();
        RunResult result = dishWasher.start(program);
        RunResult expectedResult = RunResult.builder().withStatus(Status.DOOR_OPEN).build();
        assertEquals(result.getStatus(), expectedResult.getStatus());
    }

    @Test
    public void dishWasherStartShouldReturnSuccessWithCorrectValuesGiven() {
        Door door = mock(Door.class);
        DirtFilter dirtFilter = mock(DirtFilter.class);
        Engine engine = mock(Engine.class);
        WaterPump waterpPump = mock(WaterPump.class);

        when(door.closed()).thenReturn(true);
        when(dirtFilter.capacity()).thenReturn(33.0d);

        DishWasher dishWasher = new DishWasher(waterpPump, engine, dirtFilter, door);
        ProgramConfiguration program = ProgramConfiguration.builder().withProgram(WashingProgram.ECO).withTabletsUsed(true).withFillLevel(FillLevel.HALF).build();
        RunResult result = dishWasher.start(program);
        RunResult expectedResult = RunResult.builder().withStatus(Status.ERROR_FILTER).build();
        assertEquals(result.getStatus(), expectedResult.getStatus());
    }


}
