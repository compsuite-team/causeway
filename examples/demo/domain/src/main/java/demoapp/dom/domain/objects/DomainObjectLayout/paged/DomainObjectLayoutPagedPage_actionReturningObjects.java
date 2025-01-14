package demoapp.dom.domain.objects.DomainObjectLayout.paged;

import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.MemberSupport;

import lombok.RequiredArgsConstructor;

import demoapp.dom._infra.values.ValueHolderRepository;

@Action()
@RequiredArgsConstructor
public class DomainObjectLayoutPagedPage_actionReturningObjects {

    @SuppressWarnings("unused")
    private final DomainObjectLayoutPagedPage page;

    @MemberSupport
    public List<? extends DomainObjectLayoutPaged> act() {
        return objectRepository.all();
    }

    @Inject ValueHolderRepository<String, ? extends DomainObjectLayoutPaged> objectRepository;

}
