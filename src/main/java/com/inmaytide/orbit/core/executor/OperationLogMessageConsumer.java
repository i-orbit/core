package com.inmaytide.orbit.core.executor;

import com.inmaytide.orbit.commons.constants.Constants;
import com.inmaytide.orbit.commons.log.OperationLogMessageProducer;
import com.inmaytide.orbit.commons.log.domain.OperationLog;
import com.inmaytide.orbit.core.mapper.OperationLogMapper;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class OperationLogMessageConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(OperationLogMessageConsumer.class);

    private final OperationLogMapper mapper;

    private final Searcher searcher;

    public OperationLogMessageConsumer(OperationLogMapper mapper, Searcher searcher) {
        this.mapper = mapper;
        this.searcher = searcher;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(OperationLogMessageProducer.ROUTE_KEY_OPERATION_LOG),
            exchange = @Exchange(value = Constants.RabbitMQ.DIRECT_EXCHANGE),
            key = OperationLogMessageProducer.ROUTE_KEY_OPERATION_LOG
    ))
    public void onReceived(OperationLog log) {
        String ipAddress = log.getIpAddress();
        if (StringUtils.isNotBlank(ipAddress)) {
            log.setIpAddress(ipAddress + "(" + searchIpAddressGeolocation(ipAddress) + ")");
        }
        mapper.insert(log);
    }

    private String searchIpAddressGeolocation(@Nullable String ipAddress) {
        if (StringUtils.isBlank(ipAddress)) {
            return Constants.Markers.NOT_APPLICABLE;
        }
        if (StringUtils.equalsIgnoreCase(ipAddress, "localhost") || "127.0.0.1".equals(ipAddress)) {
            return Constants.Markers.LOCAL;
        }
        try {
            String region = searcher.search(ipAddress);
            if (StringUtils.isBlank(region)) {
                return Constants.Markers.NOT_APPLICABLE;
            }
            List<String> regions = Arrays.asList(region.split("\\|"));
            if (regions.stream().anyMatch(e -> Objects.equals("内网IP", e))) {
                return Constants.Markers.LAN;
            }
            String country = regions.getFirst();
            Collections.reverse(regions);
            String city = regions.stream().filter(e -> !"0".equals(e)).findFirst().orElse(StringUtils.EMPTY);
            return StringUtils.equals(city, country) ? country : country + "-" + city;
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.error("Failed to search geolocation of ip address \"{}\" with ip2region Searcher, Cause by: \n", ipAddress, e);
            } else {
                LOG.error("Failed to search geolocation of ip address \"{}\" with ip2region Searcher", ipAddress);
            }
        }
        return Constants.Markers.NOT_APPLICABLE;
    }


}
