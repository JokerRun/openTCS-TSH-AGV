/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.comm;

import com.google.common.primitives.Ints;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.OrderResponse;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.StateResponse;
import de.fraunhofer.iml.opentcs.example.common.telegrams.Response;
import org.opentcs.contrib.tcp.netty.ConnectionEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

/**
 * Decodes incoming bytes into {@link StateResponse} instances.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class VehicleTelegramDecoder {

    /**
     * This class's Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(VehicleTelegramDecoder.class);
    /**
     * The handler decoded responses are sent to.
     */
    private final ConnectionEventListener<Response> responseHandler;
    /**
     * The minimum bytes required to even try decoding (size of the smallest telegram).
     */
    private final long minimumBytesRequired;

    /**
     * Creates a new instance.
     *
     * @param responseHandler The handler decoded responses are sent to.
     */
    public VehicleTelegramDecoder(ConnectionEventListener<Response> responseHandler) {
        this.responseHandler = requireNonNull(responseHandler, "responseHandler");
        this.minimumBytesRequired = Ints.min(OrderResponse.TELEGRAM_LENGTH,
                StateResponse.TELEGRAM_LENGTH);
    }

    //  @Override
    protected void decode(String responJson) {
        // Don't do anything if we don't have enough bytes.
        LOG.debug("Checking if it's an order response...");
        if (OrderResponse.isOrderResponse(responJson.getBytes())) {
            responseHandler.onIncomingTelegram(new OrderResponse(responJson.getBytes()));
            return;
        } else if (StateResponse.isStateResponse(responJson.getBytes())) {
            LOG.debug("Checking if it's a state response...");
            responseHandler.onIncomingTelegram(new StateResponse(responJson.getBytes()));
        } else {
            // Don't reset reader index and discard bytes
            LOG.warn("Not a valid telegram: {}", responJson);
        }

    }
}
