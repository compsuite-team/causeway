package demoapp.dom.domain.objects.DomainObjectLayout.describedAs;

import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;

import lombok.RequiredArgsConstructor;

import demoapp.dom._infra.values.ValueHolderRepository;

@Collection()
@CollectionLayout()
@RequiredArgsConstructor
public class DomainObjectLayoutDescribedAsPage_objects {

    @SuppressWarnings("unused")
    private final DomainObjectLayoutDescribedAsPage page;

    @MemberSupport
    public List<? extends DomainObjectLayoutDescribedAs> coll() {
        return objectRepository.all();
    }

    @Inject ValueHolderRepository<String, ? extends DomainObjectLayoutDescribedAs> objectRepository;

}
