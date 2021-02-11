package com.MNTDemo.Producer.repository;

import com.MNTDemo.Producer.model.LogObject;
import com.MNTDemo.Producer.model.LogType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<LogObject, Integer> {

    List<LogObject> findByService(@Param("service") String service);

    List<LogObject> findByType(LogType type);
}
