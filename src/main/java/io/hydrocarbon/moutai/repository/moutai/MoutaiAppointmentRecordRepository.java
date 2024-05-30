package io.hydrocarbon.moutai.repository.moutai;

import io.hydrocarbon.moutai.entity.moutai.MoutaiAppointmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MoutaiAppointmentRecordRepository extends JpaRepository<MoutaiAppointmentRecord, Long>,
        JpaSpecificationExecutor<MoutaiAppointmentRecord> {
}
