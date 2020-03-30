/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.commadapter.vehicle;

import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.opentcs.customizations.kernel.KernelInjectionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TshKernelInjectionModule
    extends KernelInjectionModule {
  
  private static final Logger LOG = LoggerFactory.getLogger(TshKernelInjectionModule.class);

  @Override
  protected void configure() {
    
    TshCommAdapterConfiguration configuration
        = getConfigBindingProvider().get(TshCommAdapterConfiguration.PREFIX,
                                         TshCommAdapterConfiguration.class);
    
    if (!configuration.enable()) {
      LOG.info("Tsh communication adapter disabled by configuration.");
      return;
    }
    
    install(new FactoryModuleBuilder().build(TshAdapterComponentsFactory.class));
    vehicleCommAdaptersBinder().addBinding().to(TshCommAdapterFactory.class);
  }
}
