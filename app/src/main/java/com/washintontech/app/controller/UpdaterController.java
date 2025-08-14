package com.washintontech.app.controller;

import com.washintontech.app.service.UpdaterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/client/updater")
public class UpdaterController {
    private static final Logger log = LogManager.getLogger(UpdaterController.class);
    private final UpdaterService updaterService;

    public UpdaterController(final UpdaterService updaterService) {
        this.updaterService = updaterService;
    }

//    @GetMapping(path = "/update/{clientId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<UpdateResponseExt> registerUser(@PathVariable String clientId,
//                                                @RequestParam(value = "updateRequest", required = false)
//                                                UpdateRequestExt updateRequest) {
//        return Flux.just(updateRequest)
//                .doOnEach(logOnNext(req ->
//                        log.debug("Received request for clientId: {}: {}", clientId, updateRequest)))
//                .flatMap(req -> updaterService.update(clientId, req));
//    }
}
