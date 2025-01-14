package demoapp.dom.domain.objects.DomainObject.aliased;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.bookmark.Bookmark;
import org.apache.causeway.applib.services.bookmark.BookmarkService;

import lombok.RequiredArgsConstructor;
import lombok.val;

//tag::class[]
@Action(semantics = SemanticsOf.SAFE)
@RequiredArgsConstructor
public class DomainObjectAliasedPage_lookup {

    @SuppressWarnings("unused")
    private final DomainObjectAliasedPage page;

    @MemberSupport
    public DomainObjectAliased act(final String bookmark) {
        return bookmarkService.lookup(Bookmark.parseElseFail(bookmark), DomainObjectAliased.class).orElseThrow(() -> new org.apache.causeway.applib.exceptions.RecoverableException("No customer exists for that bookmark"));
    }
    public List<String> choices0Act() {
        val bookmarks = new ArrayList<String>();
        val aliases = repository.allInstances();
        aliases.stream().forEach(obj -> {
            bookmarks.add(obj.getBookmark());
            bookmarks.add(obj.getPreviousBookmark());
        });
        return bookmarks;
    }

    @Inject DomainObjectAliasedRepository repository;
    @Inject BookmarkService bookmarkService;
}
//end::class[]
