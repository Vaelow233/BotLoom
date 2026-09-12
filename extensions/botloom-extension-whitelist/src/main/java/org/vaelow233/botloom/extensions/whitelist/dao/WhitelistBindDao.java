package org.vaelow233.botloom.extensions.whitelist.dao;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.vaelow233.botloom.extensions.whitelist.data.WhitelistBind;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RegisterFieldMapper(WhitelistBind.class)
public interface WhitelistBindDao {
    @SqlQuery("SELECT id, user_id, created_at, name FROM whitelist_binds WHERE id = :id")
    Optional<WhitelistBind> findById(@Bind("id") int id);

    @SqlQuery("SELECT id, user_id, created_at, name FROM whitelist_binds WHERE name = :name")
    List<WhitelistBind> findByName(@Bind("name") String name);

    @SqlQuery("SELECT id, user_id, created_at, name FROM whitelist_binds WHERE user_id = :user_id")
    List<WhitelistBind> findByUserId(@Bind("user_id") String userId);

    @SqlQuery("SELECT id, user_id, created_at, name FROM whitelist_binds WHERE user_id = :user_id and name = :name")
    List<WhitelistBind> findByUserIdAndName(@Bind("user_id") String userId, @Bind("name") String name);

    @SqlUpdate("INSERT INTO whitelist_binds (created_at, user_id, name) VALUES (:createdAt, :userId, :name)")
    @GetGeneratedKeys("id")
    long insert(@Bind("createdAt") LocalDateTime createdAt, @Bind("userId") String userId, @Bind("name") String name);

    @SqlUpdate("DELETE FROM whitelist_binds WHERE id = :id")
    void delete(@Bind("id") long id);
}
