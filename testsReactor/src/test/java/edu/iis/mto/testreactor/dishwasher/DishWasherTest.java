package edu.iis.mto.testreactor.dishwasher;

import static org.junit.Assert.*;

import edu.iis.mto.testreactor.dishwasher.engine.Engine;
import edu.iis.mto.testreactor.dishwasher.engine.EngineException;
import edu.iis.mto.testreactor.dishwasher.pump.WaterPump;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class DishWasherTest {

    private Door door;

    private DirtFilter dirtFilter;

    private Engine engine;

    private WaterPump waterPump;

    @Before
    public void setUp() {
        door = mock(Door.class);
        dirtFilter = mock(DirtFilter.class);
        engine = mock(Engine.class);
        waterPump = mock(WaterPump.class);
    }

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
    public void dishWasherStartShouldReturnErrorFilterIfFilterIsNotClean() {
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

    @Test
    public void dishWasherStartShouldCallEngineRunProgramOnce() throws EngineException {
        Door door = mock(Door.class);
        Engine engine = mock(Engine.class);
        DirtFilter dirtFilter = mock(DirtFilter.class);
        WaterPump waterPump = mock(WaterPump.class);

        when(door.closed()).thenReturn(true);
        when(dirtFilter.capacity()).thenReturn(60.0d);

        DishWasher dishWasher = new DishWasher(waterPump, engine, dirtFilter, door);
        ProgramConfiguration program = ProgramConfiguration.builder().withProgram(WashingProgram.ECO).withTabletsUsed(true).withFillLevel(FillLevel.HALF).build();
        dishWasher.start(program);
        verify(engine, times(1)).runProgram(program.getProgram());
    }

    @Test
    public void whenWashingProgramIsIntensiveTimeOfWashingShouldBe120() {
        Door door = mock(Door.class);
        Engine engine = mock(Engine.class);
        DirtFilter dirtFilter = mock(DirtFilter.class);
        WaterPump waterPump = mock(WaterPump.class);

        when(door.closed()).thenReturn(true);
        when(dirtFilter.capacity()).thenReturn(60.0d);

        DishWasher dishWasher = new DishWasher(waterPump, engine, dirtFilter, door);
        ProgramConfiguration program = ProgramConfiguration.builder().withProgram(WashingProgram.INTENSIVE).withTabletsUsed(true).withFillLevel(FillLevel.HALF).build();
        RunResult runResult = dishWasher.start(program);
        assertEquals(runResult.getRunMinutes(), 120);
    }

    @Test
    public void dishWasherStartShouldReturnSuccessWithCorrectValuesGiven() {
        Door door = mock(Door.class);
        Engine engine = mock(Engine.class);
        DirtFilter dirtFilter = mock(DirtFilter.class);
        WaterPump waterPump = mock(WaterPump.class);

        when(door.closed()).thenReturn(false);
        when(dirtFilter.capacity()).thenReturn(60.0d);

        DishWasher dishWasher = new DishWasher(waterPump, engine, dirtFilter, door);
        ProgramConfiguration program = ProgramConfiguration.builder().withProgram(WashingProgram.ECO).withTabletsUsed(true).build();
        RunResult result = dishWasher.start(program);
        RunResult expectedResult = RunResult.builder().withStatus(Status.SUCCESS).build();
        assertEquals(result.getStatus(), expectedResult.getStatus());
    }



}
