package demoapp.dom.domain.objects.DomainObjectLayout.cssClassFa;

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
public class DomainObjectLayoutCssClassFaPage_objects {

    @SuppressWarnings("unused")
    private final DomainObjectLayoutCssClassFaPage page;

    @MemberSupport
    public List<? extends DomainObjectLayoutCssClassFa> coll() {
        return objectRepository.all();
    }

    @Inject ValueHolderRepository<String, ? extends DomainObjectLayoutCssClassFa> objectRepository;

}
