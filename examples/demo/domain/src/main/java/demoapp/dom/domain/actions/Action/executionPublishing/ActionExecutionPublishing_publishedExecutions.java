package demoapp.dom.domain.actions.Action.executionPublishing;

import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.services.bookmark.BookmarkService;
import org.apache.causeway.extensions.executionlog.applib.dom.ExecutionLogEntry;
import org.apache.causeway.extensions.executionlog.applib.dom.ExecutionLogEntryRepository;

import lombok.RequiredArgsConstructor;

//tag::class[]
@Collection()
@RequiredArgsConstructor
public class ActionExecutionPublishing_publishedExecutions {

    @SuppressWarnings("unused")
    private final ActionExecutionPublishing entity;

    @MemberSupport public List<? extends ExecutionLogEntry> coll() {
        return executionLogEntryRepository.findRecentByTarget(bookmarkService.bookmarkForElseFail(entity));
    }

    @Inject ExecutionLogEntryRepository<? extends ExecutionLogEntry> executionLogEntryRepository; // <.>
    @Inject BookmarkService bookmarkService;
}
//end::class[]
