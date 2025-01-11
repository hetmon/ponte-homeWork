package hu.ponte.homework.pontevotehomework.controller;

import hu.ponte.homework.pontevotehomework.dto.outgoing.IdeaAuthorizeResponse;
import hu.ponte.homework.pontevotehomework.dto.outgoing.VoteStateDetailsListItem;
import hu.ponte.homework.pontevotehomework.dto.outgoing.WaitingStatusIdeaListItem;
import hu.ponte.homework.pontevotehomework.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/list-all-current/order/asc")
    public ResponseEntity<VoteStateDetailsListItem> listAllCurrentOrderAsc() {
        log.info("Get all live ideas order asc");
        VoteStateDetailsListItem details = adminService.getAllLive(true);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @GetMapping("/list-all-current/order/desc")
    public ResponseEntity<VoteStateDetailsListItem> listAllCurrentOrderDesc() {
        log.info("Get all live ideas order desc");
        VoteStateDetailsListItem details = adminService.getAllLive(false);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @GetMapping("/list-all-pending")
    public ResponseEntity<WaitingStatusIdeaListItem> getAllPendingIdeas() {
        log.info("Get all waiting ideas");
        WaitingStatusIdeaListItem response = adminService.getAllPending();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/accept-idea/{id}")
    public ResponseEntity<IdeaAuthorizeResponse> acceptIdea(@PathVariable Long id) {
        log.info("Accepting idea with id {}", id);
        IdeaAuthorizeResponse acceptResponse = adminService.accepting(id);
        return new ResponseEntity<>(acceptResponse, HttpStatus.OK);
    }

    @PutMapping("/decline-idea/{id}")
    public ResponseEntity<IdeaAuthorizeResponse> declineIdea(@PathVariable Long id) {
        log.info("Declining idea with id {}", id);
        IdeaAuthorizeResponse acceptResponse = adminService.decline(id);
        return new ResponseEntity<>(acceptResponse, HttpStatus.OK);
    }


}
