package com.dix.base.core;

import com.dix.app.BootApplication;
import com.dix.app.model.User;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BootApplication.class)
@ExtendWith(SpringExtension.class)
class CoreQueryTest {

    @Test
    void findByCol() {
        Core.Q().findByCol(User.class, "id", "1");
        Core.Q().updateCol(User.class, 1L, "name", "dd");
    }

}