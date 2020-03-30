/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.commadapter.vehicle;

import de.fraunhofer.iml.opentcs.example.common.VehicleProperties;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import org.opentcs.data.model.Vehicle;

/**
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class TshCommAdapterFactoryTest {

  private TshCommAdapterFactory commAdapterFactory;

  @Before
  public void setUp() {
    commAdapterFactory = new TshCommAdapterFactory(mock(TshAdapterComponentsFactory.class));
  }

  @Test
  public void provideAdapterForVehicleWithAllProperties() {
    assertTrue(commAdapterFactory.providesAdapterFor(
        new Vehicle("Some vehicle")
            .withProperty(VehicleProperties.PROPKEY_VEHICLE_HOST, "127.0.0.1")
            .withProperty(VehicleProperties.PROPKEY_VEHICLE_PORT, "8888")
    ));
  }

  @Test
  public void provideAdapterForVehicleMissingPort() {
    assertFalse(commAdapterFactory.providesAdapterFor(
        new Vehicle("Some vehicle")
            .withProperty(VehicleProperties.PROPKEY_VEHICLE_HOST, "127.0.0.1")
    ));
  }

  @Test
  public void provideAdapterForVehicleMissingHost() {
    assertFalse(commAdapterFactory.providesAdapterFor(
        new Vehicle("Some vehicle")
            .withProperty(VehicleProperties.PROPKEY_VEHICLE_PORT, "8888")
    ));
  }

  @Test
  public void provideAdapterForVehicleWithUnparsablePort() {
    assertFalse(commAdapterFactory.providesAdapterFor(
        new Vehicle("Some vehicle")
            .withProperty(VehicleProperties.PROPKEY_VEHICLE_HOST, "127.0.0.1")
            .withProperty(VehicleProperties.PROPKEY_VEHICLE_PORT, "xyz")
    ));
  }

}
