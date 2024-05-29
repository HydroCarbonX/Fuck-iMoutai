package io.hydrocarbon.moutai.repository.moutai;

import io.hydrocarbon.moutai.entity.moutai.MoutaiAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MoutaiAppointmentRepository extends JpaRepository<MoutaiAppointment, Long>,
        JpaSpecificationExecutor<MoutaiAppointment> {

    void deleteByMoutaiUserId(Long moutaiUserId);
}
