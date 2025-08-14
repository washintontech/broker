package com.washintontech.app.service.trade.inbound;

import com.washintontech.app.model.trade.inbound.TradeNewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradeComplianceService {
    public void validate(final TradeNewRequest req) {
    }
}
