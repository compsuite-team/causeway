package demoapp.dom.domain.properties.Property.editing;

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
public class PropertyEditingPage_objects {

    @SuppressWarnings("unused")
    private final PropertyEditingPage page;

    @MemberSupport
    public List<? extends PropertyEditing> coll() {
        return objectRepository.all();
    }

    @Inject
    ValueHolderRepository<String, ? extends PropertyEditing> objectRepository;

}
