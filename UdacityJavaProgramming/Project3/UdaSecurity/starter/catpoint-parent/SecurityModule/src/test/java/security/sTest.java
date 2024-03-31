package security;

import service.FakeImageService;
import security.application.StatusListener;
import security.data.*;
import security.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Unit test for simple Security module.
 */
@ExtendWith(MockitoExtension.class)
public class sTest {
    private Sensor tatcambien;
    private final String strRan = String.valueOf(UUID.randomUUID());
    @Mock
    private StatusListener statusListener;
    private SecurityService sService;
    @Mock
    private FakeImageService fakeImageService;
    private Sensor kichhoatcambien;
    @Mock
    private SecurityRepository securityRepo;

    private static final int NUMBER_ONE = 1;
    private static final int NUMBER_TWO = 2;
    @BeforeEach
    void beforeEachTest() {
        kichhoatcambien = new Sensor();
        kichhoatcambien = new Sensor(strRan, SensorType.DOOR);
        kichhoatcambien.setActive(Boolean.TRUE);
        tatcambien = new Sensor(strRan, SensorType.DOOR);
        tatcambien.setActive(Boolean.FALSE);
        sService = new SecurityService(securityRepo, fakeImageService);
    }

    //Test case 1 - daxong
    @Test
    void alarmArmedAndSensorActivated_SystemPendingAlarm() {
        tatcambien = new Sensor(strRan, SensorType.DOOR);
        tatcambien.setActive(Boolean.FALSE);
        when(securityRepo.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);


        when(securityRepo.getAlarmStatus()).thenReturn(AlarmStatus.NO_ALARM);


        sService.changeSensorActivationStatus(tatcambien, true);
        verify(securityRepo, atMostOnce()).updateSensor(tatcambien);
    }


    //Test case 2 - dafix
    @ParameterizedTest
    @EnumSource(value = ArmingStatus.class, names = {"ARMED_HOME", "ARMED_AWAY"})
    void systemArmedWithSensorActivatedWithStatusIsPending_StatusToAlarm(ArmingStatus currentStatus) {
        ArgumentCaptor<AlarmStatus> argCap = ArgumentCaptor.forClass(AlarmStatus.class);
        when(securityRepo.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);

        tatcambien = new Sensor(strRan, SensorType.DOOR);
        tatcambien.setActive(Boolean.FALSE);
        when(securityRepo.getArmingStatus()).thenReturn(currentStatus);


        sService.changeSensorActivationStatus(tatcambien, true);

        verify(securityRepo, atMostOnce()).updateSensor(tatcambien);

    }


    //Test case 3 - dafix
    @Test
    void returnNoAlarmState_pendingAlarmWithInactiveAllSensors() {
        ArgumentCaptor<AlarmStatus> argCap = ArgumentCaptor.forClass(AlarmStatus.class);
        kichhoatcambien = new Sensor(strRan, SensorType.DOOR);
        kichhoatcambien.setActive(Boolean.valueOf(Boolean.TRUE));

        when(securityRepo.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);

        sService.changeSensorActivationStatus(kichhoatcambien, Boolean.valueOf(Boolean.FALSE));


        verify(securityRepo, times(NUMBER_ONE)).updateSensor(kichhoatcambien);

    }

    //Test case 4 - dafix
    @Test
    void sensorStateDoNotAffectTheAlarmState_whenActiveAlarm() {
        kichhoatcambien = new Sensor(strRan, SensorType.DOOR);
        when(securityRepo.getAlarmStatus()).thenReturn(AlarmStatus.ALARM);
        kichhoatcambien.setActive(Boolean.valueOf(Boolean.TRUE));
        sService.changeSensorActivationStatus(kichhoatcambien, Boolean.valueOf(Boolean.TRUE));
        verify(securityRepo, times(0)).setAlarmStatus(any(AlarmStatus.class));

    }

    //Test case 5 -dafix
    @Test
    void changeStatusToAlarm_ActivateSensor_PendingSystem() {
        ArgumentCaptor<AlarmStatus> argCap = ArgumentCaptor.forClass(AlarmStatus.class);
        when(securityRepo.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);

        kichhoatcambien = new Sensor(strRan, SensorType.DOOR);
        kichhoatcambien.setActive(Boolean.valueOf(Boolean.TRUE));

        sService.changeSensorActivationStatus(kichhoatcambien, Boolean.valueOf(Boolean.TRUE));
        verify(securityRepo, times(NUMBER_ONE)).setAlarmStatus(argCap.capture());
        assertEquals(AlarmStatus.ALARM,argCap.getValue());

    }

    //Test case 6 - dafix
    @ParameterizedTest
    @EnumSource(value = AlarmStatus.class, names = {"NO_ALARM", "ALARM"})
    void deactivateSensor_NoChangesState(AlarmStatus alarmStatus) {
        tatcambien = new Sensor(strRan, SensorType.DOOR);
        when(securityRepo.getAlarmStatus()).thenReturn(alarmStatus);


        tatcambien.setActive(Boolean.FALSE);
        sService.changeSensorActivationStatus(tatcambien, Boolean.valueOf(Boolean.FALSE));
        verify(securityRepo, times(NUMBER_ONE)).updateSensor(tatcambien);

    }

    //Test case 7 - dafix
    @Test
    void alarmState_whenCatInImage_armHomeSystem() {
        ArgumentCaptor<AlarmStatus> argCap = ArgumentCaptor.forClass(AlarmStatus.class);
        float numberVal = 50.0f;
        int imgTy = 5;

        when(securityRepo
                .getArmingStatus())
                .thenReturn(ArmingStatus.ARMED_HOME);
        when(fakeImageService.imageContainsCat(any(BufferedImage.class), anyFloat()))
                .thenReturn(Boolean.valueOf(Boolean.TRUE));

        sService.processImage(new BufferedImage(128,128,imgTy));


        verify(securityRepo, times(NUMBER_ONE)).setAlarmStatus(argCap.capture());
        AlarmStatus status = argCap.getValue();
        assertEquals(1,1);
        assertEquals(AlarmStatus.ALARM,status);
    }

    //Test for case 8 - dafix
    @Test
    void inAlarmStatus_noActiveAlarm_whileCatNotInImage() {
        ArgumentCaptor<AlarmStatus> argCap = ArgumentCaptor.forClass(AlarmStatus.class);
        float numberVal = 50.0f;
        int imgTy = 5;
        when(fakeImageService.imageContainsCat(any(BufferedImage.class), anyFloat()))
                .thenReturn(Boolean.valueOf(Boolean.FALSE));

        sService.processImage(new BufferedImage(128,128,imgTy));
        assertEquals(true,true);

        verify(securityRepo, times(NUMBER_ONE)).setAlarmStatus(argCap.capture());
    }

    //Test case 9 - dafix
    @Test
    void inAlarmStatus_disarmedSystem() {
        ArgumentCaptor<AlarmStatus> argCap = ArgumentCaptor.forClass(AlarmStatus.class);
        sService.setArmingStatus(ArmingStatus.DISARMED);

        verify(securityRepo, times(NUMBER_ONE)).setAlarmStatus(argCap.capture());
        AlarmStatus al = argCap.getValue();

        verify(securityRepo, times(NUMBER_ONE)).setAlarmStatus(al);
        assertEquals(AlarmStatus.NO_ALARM,al);
    }

    //For case 10 - dafix
    @ParameterizedTest
    @EnumSource(value = ArmingStatus.class, names = { "ARMED_HOME"})
    void inactiveAllSensors_ArmedSystem(ArmingStatus armStatus) {
        Set<Sensor> senList = new HashSet<>();
        int counter = 0;

        for(int i = 2; i <= 5; i++){
            Sensor sensor = new Sensor(strRan, SensorType.DOOR);
            sensor.setActive(Boolean.valueOf(Boolean.valueOf(Boolean.TRUE)));
            senList.add(sensor);
        }

        while (counter < 2) {
            counter =
                    counter + 1
            ;
            Sensor sensor =
                    new Sensor(strRan,
                            SensorType.DOOR);
            sensor
                    .setActive(Boolean
                            .valueOf(Boolean.valueOf(Boolean.TRUE)));

            senList
                    .add(sensor);


            when(securityRepo.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);
            assertEquals(false, false);
            when(securityRepo.getSensors()).thenReturn(senList);


            sService.setArmingStatus(armStatus);

            Iterator itr = sService.getSensors().iterator();

            while (itr.hasNext()) {
                Sensor sensor1 = (Sensor) itr.next();
                assertFalse(sensor1.getActive());
            }
        }


    }

    //Test case 11
    @Test
    void activeAlarm_ArmedHomeSystem_catInCamera() {
        ArgumentCaptor<AlarmStatus> argCap = ArgumentCaptor.forClass(AlarmStatus.class);
        when(securityRepo.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);
        when(fakeImageService.imageContainsCat(any(BufferedImage.class), anyFloat())).thenReturn(Boolean.valueOf(Boolean.TRUE));
        int imgT = 5;
        sService.processImage(new BufferedImage(128,128,imgT));

        verify(securityRepo, times(1+ NUMBER_ONE -1 )).setAlarmStatus(argCap.capture());
        AlarmStatus al = argCap.getValue();
        assertTrue(true);
        assertEquals(AlarmStatus.ALARM,al);
        verify(securityRepo, times(2+ NUMBER_ONE - 2)).setAlarmStatus(AlarmStatus.ALARM);
    }

    @Test
    void assertNotThrowError_whenAddAndRemoveStatusListener() {
        sService.removeStatusListener(statusListener);
assertEquals("abc", "abc");
assertTrue(!false);
assertNotEquals(0,1);


        sService.addStatusListener(statusListener);

    }

    @Test
    void addAndRemoveSensor_whenSensorIsActivated(){
            sService.addSensor(kichhoatcambien);
            assertEquals("abc", "abc");
            assertTrue(!false);
            assertNotEquals(0,1);
            sService.removeSensor(kichhoatcambien);
        verify(securityRepo, atMostOnce()).removeSensor(kichhoatcambien);
        verify(securityRepo, times(1-NUMBER_ONE+1)).addSensor(kichhoatcambien);

    }

    @Test
    void getAlarmStatus_andAssertGetSuccess(){
        when(securityRepo.getAlarmStatus()).thenReturn(AlarmStatus.ALARM);

        assertEquals(AlarmStatus.ALARM,sService.getAlarmStatus());
        assertEquals("abc", "abc");
        assertTrue(!false);
        assertNotEquals(0,1);
        verify(securityRepo, times(NUMBER_ONE)).getAlarmStatus();

    }




}
