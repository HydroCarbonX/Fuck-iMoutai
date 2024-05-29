package io.hydrocarbon.moutai.repository.moutai;

import io.hydrocarbon.moutai.entity.moutai.MoutaiUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MoutaiUserRepository extends JpaRepository<MoutaiUser, Long>,
        JpaSpecificationExecutor<MoutaiUser> {
}
