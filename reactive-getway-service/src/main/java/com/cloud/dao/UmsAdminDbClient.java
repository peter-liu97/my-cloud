package com.cloud.dao;

import com.cloud.pojo.UmsPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Repository
public class UmsAdminDbClient {

    public final DatabaseClient databaseClient;

    public Flux<UmsPermission> getPermissionList(Long adminId) {
        return databaseClient.execute("  SELECT\n" +
                "            p.*\n" +
                "        FROM\n" +
                "            ums_admin_role_relation ar\n" +
                "            LEFT JOIN ums_role r ON ar.role_id = r.id\n" +
                "            LEFT JOIN ums_role_permission_relation rp ON r.id = rp.role_id\n" +
                "            LEFT JOIN ums_permission p ON rp.permission_id = p.id\n" +
                "        WHERE\n" +
                "            ar.admin_id = :adminId\n" +
                "            AND p.id IS NOT NULL\n" +
                "            AND p.id NOT IN (\n" +
                "                SELECT\n" +
                "                    p.id\n" +
                "                FROM\n" +
                "                    ums_admin_permission_relation pr\n" +
                "                    LEFT JOIN ums_permission p ON pr.permission_id = p.id\n" +
                "                WHERE\n" +
                "                    pr.type = - 1\n" +
                "                    AND pr.admin_id = :adminId\n" +
                "            )\n" +
                "        UNION\n" +
                "        SELECT\n" +
                "            p.*\n" +
                "        FROM\n" +
                "            ums_admin_permission_relation pr\n" +
                "            LEFT JOIN ums_permission p ON pr.permission_id = p.id\n" +
                "        WHERE\n" +
                "            pr.type = 1\n" +
                "            AND pr.admin_id = :adminId")
                .bind("adminId", adminId)
                .as(UmsPermission.class)
                .fetch().all();
    }
}
