package io.hydrocarbon.moutai.repository.moutai;

import io.hydrocarbon.moutai.entity.moutai.MoutaiShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MoutaiShopRepository extends JpaRepository<MoutaiShop, Long>,
        JpaSpecificationExecutor<MoutaiShop> {
}
