package io.hydrocarbon.moutai.repository.moutai;

import io.hydrocarbon.moutai.entity.moutai.MoutaiConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoutaiConfigRepository extends JpaRepository<MoutaiConfig, Long>,
        JpaSpecificationExecutor<MoutaiConfig> {

    Optional<MoutaiConfig> findByKey(String key);
}
