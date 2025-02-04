package demoapp.dom.domain.actions.Action.commandPublishing;

import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.MemberSupport;

import lombok.RequiredArgsConstructor;

import demoapp.dom._infra.values.ValueHolderRepository;

//tag::class[]
@Collection()
@RequiredArgsConstructor
public class ActionCommandPublishingPage_objects {

    @SuppressWarnings("unused")
    private final ActionCommandPublishingPage page;

    @MemberSupport public List<? extends ActionCommandPublishing> coll() {
        return objectRepository.all();
    }

    @Inject ValueHolderRepository<String, ? extends ActionCommandPublishing> objectRepository;
}
//end::class[]
