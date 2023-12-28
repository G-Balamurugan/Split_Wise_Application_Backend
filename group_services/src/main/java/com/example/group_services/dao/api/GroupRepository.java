package com.example.group_services.dao.api;

import com.example.group_services.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group , Long> {
    @Override
    List<Group> findAll();
    @Override
    Group save(Group group);
    Group findByGroupId(long groupId);
    Group findByGroupName(String groupName);
    @Query("SELECT group_details FROM Group group_details WHERE lower(replace(group_details.groupName, ' ', '')) LIKE %:normalizedGroupName%")
    List<Group> findGroupByNameIgnoreCaseAndNormalized(@Param("normalizedGroupName") String normalizedGroupName);

}
