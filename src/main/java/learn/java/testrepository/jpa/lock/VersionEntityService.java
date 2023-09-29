package learn.java.testrepository.jpa.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VersionEntityService {

    private final VersionEntityRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTitle(final Long id) {
        final VersionEntity version = repository.findById(id).get();
        version.updateTitle("hi");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long save(final VersionEntity entity) {
        return repository.save(entity).getId();
    }
}
