package demoapp.dom.domain.properties.Property.commandPublishing;

import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.services.bookmark.BookmarkService;
import org.apache.causeway.extensions.commandlog.applib.dom.CommandLogEntry;
import org.apache.causeway.extensions.commandlog.applib.dom.CommandLogEntryRepository;

import lombok.RequiredArgsConstructor;

//tag::class[]
@Collection()
@RequiredArgsConstructor
public class PropertyCommandPublishing_publishedCommands {

    @SuppressWarnings("unused")
    private final PropertyCommandPublishing entity;

    @MemberSupport public List<? extends CommandLogEntry> coll() {
        return commandLogEntryRepository.findRecentByTarget(bookmarkService.bookmarkForElseFail(entity));
    }

    @Inject CommandLogEntryRepository<? extends CommandLogEntry> commandLogEntryRepository;
    @Inject BookmarkService bookmarkService;
}
//end::class[]
