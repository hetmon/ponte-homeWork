package hu.ponte.homework.pontevotehomework.controller;

import hu.ponte.homework.pontevotehomework.dto.income.IdeaCommand;
import hu.ponte.homework.pontevotehomework.dto.income.VoteCommand;
import hu.ponte.homework.pontevotehomework.dto.outgoing.AllAcceptedIdeaListItem;
import hu.ponte.homework.pontevotehomework.dto.outgoing.IdeaResponse;
import hu.ponte.homework.pontevotehomework.dto.outgoing.VoteResponse;
import hu.ponte.homework.pontevotehomework.extractor.ExtractorHelper;
import hu.ponte.homework.pontevotehomework.service.IdeaService;
import hu.ponte.homework.pontevotehomework.validator.MakeIdeaCommandValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/idea")
public class VoteController {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private final IdeaService ideaService;
    private final MakeIdeaCommandValidator makeIdeaCommandValidator;


    @Autowired
    public VoteController(IdeaService ideaService, MakeIdeaCommandValidator makeIdeaCommandValidator) {
        this.ideaService = ideaService;
        this.makeIdeaCommandValidator = makeIdeaCommandValidator;

    }

    @PostMapping("/make-idea")
    public ResponseEntity<IdeaResponse> makeIdea(@Valid @RequestBody
                                                 IdeaCommand command,
                                                 HttpServletRequest req) {

        log.info("Post make idea with command: {}", command);
        IdeaResponse response = ideaService.makeIdea(command, req);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all-pending-idea")
    public ResponseEntity<AllAcceptedIdeaListItem> getAllPendingIdea() {
        log.info("Get all pending idea");
        AllAcceptedIdeaListItem response = ideaService.getAllAccepted();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/vote-on-idea")
    public ResponseEntity<VoteResponse> voteOnIdea(@RequestBody @Valid VoteCommand command, HttpServletRequest req) {
        Long voterId = ExtractorHelper.extractUserIdFromSession(req);
        log.info("Vote on idea with idea  with voter id: {},{}", command, voterId);
        VoteResponse response = ideaService.vote(command, voterId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @InitBinder("ideaCommand")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(makeIdeaCommandValidator);
    }

}
